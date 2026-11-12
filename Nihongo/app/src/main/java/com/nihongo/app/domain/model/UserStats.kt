package com.nihongo.app.domain.model

/**
 * Real, database-backed learning progress metrics (Section 18).
 * Never displays fake progress: all counters default to 0 until the user completes lessons,
 * marks items as learned, or finishes quizzes.
 */
data class UserStats(
    val todayStudyMinutes: Int = 0,
    val totalStudyMinutes: Int = 0,
    val currentStreakDays: Int = 0,
    val learnedKanaCount: Int = 0,
    val totalKanaCount: Int = 214,
    val learnedKanjiCount: Int = 0,
    val totalKanjiCount: Int = 10,
    val learnedVocabCount: Int = 0,
    val totalVocabCount: Int = 20,
    val learnedGrammarCount: Int = 0,
    val completedLessonsCount: Int = 0,
    val totalQuizzesTaken: Int = 0,
    val quizAccuracyPercent: Int = 0,
    val listeningAccuracyPercent: Int = 0,
    val dueSrsReviewCount: Int = 0,
    val totalSrsScheduledCount: Int = 0,
    val learnedItemIds: Set<String> = emptySet(),
    val favoriteItemIds: Set<String> = emptySet(),
    val missedQuestionIds: Set<String> = emptySet()
) {
    /**
     * Calculates honest overall mastery percentage across Kana, Kanji, and Vocabulary.
     */
    val overallProgressFraction: Float
        get() {
            val totalItems = (totalKanaCount + totalKanjiCount + totalVocabCount).coerceAtLeast(1)
            val learnedTotal = learnedKanaCount + learnedKanjiCount + learnedVocabCount
            return (learnedTotal.toFloat() / totalItems.toFloat()).coerceIn(0f, 1f)
        }
}
