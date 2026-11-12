package com.nihongo.app.domain.usecase

import com.nihongo.app.domain.model.SrsCardState
import com.nihongo.app.domain.model.SrsReviewGrade
import kotlin.math.roundToInt

/**
 * Implements the SuperMemo SM-2 local spaced repetition algorithm (Section 14).
 * Computes the updated interval, ease factor, and next review timestamp entirely offline.
 */
class CalculateSrsScheduleUseCase {

    operator fun invoke(
        current: SrsCardState,
        grade: SrsReviewGrade,
        nowEpochMillis: Long = System.currentTimeMillis()
    ): SrsCardState {
        val q = grade.qualityScore

        val newRepetitionCount: Int
        val newIntervalDays: Int

        if (q < 3) {
            // Failed recall ("Need revision"): reset repetitions and review again in 1 day
            newRepetitionCount = 0
            newIntervalDays = 1
        } else {
            newRepetitionCount = current.repetitionCount + 1
            newIntervalDays = when (newRepetitionCount) {
                1 -> 1
                2 -> 4
                else -> (current.intervalDays * current.easeFactor).roundToInt().coerceAtLeast(1)
            }
        }

        val easeDelta = 0.1f - (5 - q) * (0.08f + (5 - q) * 0.02f)
        val newEaseFactor = (current.easeFactor + easeDelta).coerceIn(1.3f, 3.0f)
        val oneDayMillis = 24L * 60L * 60L * 1000L
        val nextDueMillis = nowEpochMillis + (newIntervalDays * oneDayMillis)

        return current.copy(
            repetitionCount = newRepetitionCount,
            intervalDays = newIntervalDays,
            easeFactor = newEaseFactor,
            lastReviewedEpochMillis = nowEpochMillis,
            nextReviewEpochMillis = nextDueMillis
        )
    }
}
