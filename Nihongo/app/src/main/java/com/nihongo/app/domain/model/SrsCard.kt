package com.nihongo.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Ratings used by the local Spaced Repetition System (Section 14).
 */
@Serializable
enum class SrsReviewGrade(val qualityScore: Int) {
    AGAIN(1),          // Need immediate revision
    HARD(3),           // Recalled with hesitation
    GOOD(4),           // Know it (standard recall)
    EASY(5)            // Instant effortless recall
}

/**
 * Represents the persistent Spaced Repetition state of any Vocabulary, Kanji, or Kana item.
 */
@Serializable
data class SrsCardState(
    val itemId: String,
    val contentType: String,
    val repetitionCount: Int = 0,
    val intervalDays: Int = 0,
    val easeFactor: Float = 2.5f,
    val lastReviewedEpochMillis: Long = 0L,
    val nextReviewEpochMillis: Long = 0L
) {
    fun isDue(nowEpochMillis: Long = System.currentTimeMillis()): Boolean {
        return nextReviewEpochMillis <= nowEpochMillis
    }
}
