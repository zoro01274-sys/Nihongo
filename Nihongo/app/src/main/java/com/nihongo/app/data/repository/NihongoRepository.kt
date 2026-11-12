package com.nihongo.app.data.repository

import com.nihongo.app.data.local.database.NihongoDatabase
import com.nihongo.app.data.local.database.entity.DailyStudyLogEntity
import com.nihongo.app.data.local.database.entity.QuizHistoryEntity
import com.nihongo.app.data.local.database.entity.SrsReviewEntity
import com.nihongo.app.data.local.database.entity.UserProgressEntity
import com.nihongo.app.domain.model.JlptLevel
import com.nihongo.app.domain.model.KanaCatalog
import com.nihongo.app.domain.model.LearningCatalog
import com.nihongo.app.domain.model.SrsCardState
import com.nihongo.app.domain.model.SrsReviewGrade
import com.nihongo.app.domain.model.UserStats
import com.nihongo.app.domain.usecase.CalculateSrsScheduleUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate

/**
 * Single source of truth connecting the Room Database to the UI layer.
 * Computes honest, real-time learning statistics and persists user progress,
 * favorites, SM-2 spaced repetition intervals, quiz accuracy, and study streaks.
 */
class NihongoRepository(
    private val database: NihongoDatabase,
    private val calculateSrsScheduleUseCase: CalculateSrsScheduleUseCase = CalculateSrsScheduleUseCase()
) {
    private val progressDao = database.progressDao()

    private val totalKanaInCatalog =
        KanaCatalog.hiraganaList.size + KanaCatalog.katakanaList.size
    private val totalKanjiInCatalog = LearningCatalog.kanjiList.size
    private val totalVocabInCatalog = LearningCatalog.vocabularyList.size

    val userStatsFlow: Flow<UserStats> = combine(
        progressDao.observeAllItemProgress(),
        progressDao.observeAllSrsReviews(),
        progressDao.observeQuizHistory(),
        progressDao.observeDailyStudyLogs()
    ) { itemProgressList, srsList, quizList, dailyLogs ->
        val nowMillis = System.currentTimeMillis()
        val todayIso = LocalDate.now().toString()

        val learnedItems = itemProgressList.filter { it.isLearned }
        val favoriteItems = itemProgressList.filter { it.isFavorite }

        val learnedKana = learnedItems.count { it.contentType == "KANA" }
        val learnedKanji = learnedItems.count { it.contentType == "KANJI" }
        val learnedVocab = learnedItems.count { it.contentType == "VOCAB" }
        val learnedGrammar = learnedItems.count { it.contentType == "GRAMMAR" }
        val completedModules = learnedItems.count { it.contentType == "LESSON" }

        val todayMinutes = dailyLogs.firstOrNull { it.dateIso == todayIso }?.studyMinutes ?: 0
        val totalMinutes = dailyLogs.sumOf { it.studyMinutes }
        val activeStreak = calculateConsecutiveStreakDays(dailyLogs)

        val totalAnswered = quizList.sumOf { it.totalQuestions }
        val totalCorrect = quizList.sumOf { it.correctCount }
        val overallQuizAccuracy = if (totalAnswered > 0) {
            ((totalCorrect.toFloat() / totalAnswered.toFloat()) * 100f).toInt()
        } else {
            0
        }

        val listeningQuizzes = quizList.filter { it.isListeningQuiz }
        val listeningTotal = listeningQuizzes.sumOf { it.totalQuestions }
        val listeningCorrect = listeningQuizzes.sumOf { it.correctCount }
        val listeningAccuracy = if (listeningTotal > 0) {
            ((listeningCorrect.toFloat() / listeningTotal.toFloat()) * 100f).toInt()
        } else {
            0
        }

        val dueSrsCount = srsList.count { it.nextReviewEpochMillis <= nowMillis }

        val missedIds = quizList
            .flatMap { it.missedQuestionIdsCsv.split(",") }
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .toSet()

        UserStats(
            todayStudyMinutes = todayMinutes,
            totalStudyMinutes = totalMinutes,
            currentStreakDays = activeStreak,
            learnedKanaCount = learnedKana,
            totalKanaCount = totalKanaInCatalog,
            learnedKanjiCount = learnedKanji,
            totalKanjiCount = totalKanjiInCatalog,
            learnedVocabCount = learnedVocab,
            totalVocabCount = totalVocabInCatalog,
            learnedGrammarCount = learnedGrammar,
            completedLessonsCount = learnedKana + learnedKanji + learnedVocab + learnedGrammar + completedModules,
            totalQuizzesTaken = quizList.size,
            quizAccuracyPercent = overallQuizAccuracy,
            listeningAccuracyPercent = listeningAccuracy,
            dueSrsReviewCount = dueSrsCount,
            totalSrsScheduledCount = srsList.size,
            learnedItemIds = learnedItems.map { it.itemId }.toSet(),
            favoriteItemIds = favoriteItems.map { it.itemId }.toSet(),
            missedQuestionIds = missedIds
        )
    }

    suspend fun toggleItemLearned(
        itemId: String,
        contentType: String,
        jlptLevel: JlptLevel = JlptLevel.N5
    ) {
        val existing = progressDao.getItemProgress(itemId)
        val newLearnedState = !(existing?.isLearned ?: false)
        progressDao.upsertItemProgress(
            UserProgressEntity(
                itemId = itemId,
                contentType = contentType,
                jlptLevel = jlptLevel.label,
                isLearned = newLearnedState,
                isFavorite = existing?.isFavorite ?: false,
                updatedAtEpochMillis = System.currentTimeMillis()
            )
        )
        if (newLearnedState) {
            recordStudyActivity(minutesToAdd = 1, lessonsCompletedDelta = 1)
        }
    }

    suspend fun toggleItemFavorite(
        itemId: String,
        contentType: String,
        jlptLevel: JlptLevel = JlptLevel.N5
    ) {
        val existing = progressDao.getItemProgress(itemId)
        progressDao.upsertItemProgress(
            UserProgressEntity(
                itemId = itemId,
                contentType = contentType,
                jlptLevel = jlptLevel.label,
                isLearned = existing?.isLearned ?: false,
                isFavorite = !(existing?.isFavorite ?: false),
                updatedAtEpochMillis = System.currentTimeMillis()
            )
        )
    }

    suspend fun recordFlashcardSrsReview(
        itemId: String,
        contentType: String,
        grade: SrsReviewGrade
    ) {
        val existing = progressDao.getSrsReview(itemId)
        val currentState = if (existing != null) {
            SrsCardState(
                itemId = existing.itemId,
                contentType = existing.contentType,
                repetitionCount = existing.repetitionCount,
                intervalDays = existing.intervalDays,
                easeFactor = existing.easeFactor,
                lastReviewedEpochMillis = existing.lastReviewedEpochMillis,
                nextReviewEpochMillis = existing.nextReviewEpochMillis
            )
        } else {
            SrsCardState(itemId = itemId, contentType = contentType)
        }

        val updated = calculateSrsScheduleUseCase(currentState, grade)
        progressDao.upsertSrsReview(
            SrsReviewEntity(
                itemId = updated.itemId,
                contentType = updated.contentType,
                repetitionCount = updated.repetitionCount,
                intervalDays = updated.intervalDays,
                easeFactor = updated.easeFactor,
                lastReviewedEpochMillis = updated.lastReviewedEpochMillis,
                nextReviewEpochMillis = updated.nextReviewEpochMillis
            )
        )
        recordStudyActivity(minutesToAdd = 1, lessonsCompletedDelta = 0)
    }

    suspend fun recordQuizAttempt(
        quizMode: String,
        jlptLevel: JlptLevel,
        correctCount: Int,
        totalQuestions: Int,
        isListeningQuiz: Boolean,
        missedQuestionIds: List<String>
    ) {
        progressDao.insertQuizHistory(
            QuizHistoryEntity(
                quizMode = quizMode,
                jlptLevel = jlptLevel.label,
                correctCount = correctCount,
                totalQuestions = totalQuestions,
                isListeningQuiz = isListeningQuiz,
                missedQuestionIdsCsv = missedQuestionIds.joinToString(",")
            )
        )
        recordStudyActivity(minutesToAdd = 2, lessonsCompletedDelta = 1)
    }

    suspend fun recordStudyActivity(minutesToAdd: Int, lessonsCompletedDelta: Int) {
        val todayIso = LocalDate.now().toString()
        val existing = progressDao.getDailyLog(todayIso)
        val updatedMinutes = (existing?.studyMinutes ?: 0) + minutesToAdd
        val updatedLessons = (existing?.completedLessonsCount ?: 0) + lessonsCompletedDelta
        progressDao.upsertDailyLog(
            DailyStudyLogEntity(
                dateIso = todayIso,
                studyMinutes = updatedMinutes,
                completedLessonsCount = updatedLessons,
                updatedAtEpochMillis = System.currentTimeMillis()
            )
        )
    }

    suspend fun resetAllLearningProgress() {
        progressDao.resetAllUserProgressTransaction()
    }

    private fun calculateConsecutiveStreakDays(logs: List<DailyStudyLogEntity>): Int {
        val activeDates = logs
            .filter { it.studyMinutes > 0 || it.completedLessonsCount > 0 }
            .mapNotNull { runCatching { LocalDate.parse(it.dateIso) }.getOrNull() }
            .toSet()

        if (activeDates.isEmpty()) return 0

        val today = LocalDate.now()
        var cursorDate = if (activeDates.contains(today)) {
            today
        } else if (activeDates.contains(today.minusDays(1))) {
            today.minusDays(1)
        } else {
            return 0
        }

        var streak = 0
        while (activeDates.contains(cursorDate)) {
            streak++
            cursorDate = cursorDate.minusDays(1)
        }
        return streak
    }
}
