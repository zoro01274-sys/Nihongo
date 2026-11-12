package com.nihongo.app.data.local.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "kana_items",
    indices = [Index(value = ["script", "category"]), Index(value = ["romaji"])]
)
data class KanaEntity(
    @PrimaryKey val id: String,
    val script: String,
    val category: String,
    val rowGroup: String,
    val character: String,
    val romaji: String,
    val pronunciationBn: String,
    val explanationEn: String,
    val strokeCount: Int,
    val strokeDirectionsBn: String,
    val strokeDirectionsEn: String,
    val exampleWordJp: String,
    val exampleWordReading: String,
    val exampleWordRomaji: String,
    val exampleMeaningBn: String,
    val exampleMeaningEn: String
)

@Entity(
    tableName = "kanji_items",
    indices = [Index(value = ["jlptLevel"]), Index(value = ["character"])]
)
data class KanjiEntity(
    @PrimaryKey val id: String,
    val character: String,
    val jlptLevel: String,
    val meaningBn: String,
    val meaningEn: String,
    val onyomiKana: String,
    val kunyomiKana: String,
    val romajiReadings: String,
    val strokeCount: Int,
    val strokeDirectionsBn: String,
    val strokeDirectionsEn: String,
    val exampleSentenceJp: String,
    val exampleSentenceReading: String,
    val exampleSentenceRomaji: String,
    val exampleSentenceBn: String,
    val exampleSentenceEn: String,
    val relatedKanjiCsv: String
)

@Entity(
    tableName = "vocabulary_items",
    indices = [Index(value = ["jlptLevel"]), Index(value = ["category"])]
)
data class VocabularyEntity(
    @PrimaryKey val id: String,
    val wordJp: String,
    val hiraganaReading: String,
    val kanjiSpelling: String,
    val romaji: String,
    val meaningBn: String,
    val meaningEn: String,
    val partOfSpeechEn: String,
    val partOfSpeechBn: String,
    val category: String,
    val jlptLevel: String,
    val exampleSentenceJp: String,
    val exampleSentenceReading: String,
    val exampleSentenceRomaji: String,
    val exampleSentenceBn: String,
    val exampleSentenceEn: String,
    val hasNativeAudio: Boolean
)

@Entity(
    tableName = "grammar_lessons",
    indices = [Index(value = ["jlptLevel"])]
)
data class GrammarEntity(
    @PrimaryKey val id: String,
    val pattern: String,
    val jlptLevel: String,
    val titleBn: String,
    val titleEn: String,
    val explanationBn: String,
    val explanationEn: String,
    val formationRulesBn: String,
    val formationRulesEn: String,
    val commonMistakesBn: String,
    val commonMistakesEn: String
)

@Entity(
    tableName = "listening_exercises",
    indices = [Index(value = ["jlptLevel"])]
)
data class ExerciseEntity(
    @PrimaryKey val id: String,
    val jlptLevel: String,
    val exerciseType: String,
    val japaneseAudioText: String,
    val questionBn: String,
    val questionEn: String,
    val optionsCsv: String,
    val correctOptionIndex: Int,
    val explanationBn: String,
    val explanationEn: String
)

/**
 * Stores persistent learned status and favorite bookmarks for Kana, Kanji, Vocabulary,
 * Grammar lessons, and JLPT course modules.
 */
@Entity(
    tableName = "user_item_progress",
    indices = [Index(value = ["contentType", "isLearned"]), Index(value = ["isFavorite"])]
)
data class UserProgressEntity(
    @PrimaryKey val itemId: String,
    val contentType: String, // "KANA", "KANJI", "VOCAB", "GRAMMAR", "LESSON"
    val jlptLevel: String,
    val isLearned: Boolean = false,
    val isFavorite: Boolean = false,
    val updatedAtEpochMillis: Long = System.currentTimeMillis()
)

/**
 * Stores local SM-2 Spaced Repetition review schedules.
 */
@Entity(
    tableName = "srs_reviews",
    indices = [Index(value = ["nextReviewEpochMillis"])]
)
data class SrsReviewEntity(
    @PrimaryKey val itemId: String,
    val contentType: String,
    val repetitionCount: Int,
    val intervalDays: Int,
    val easeFactor: Float,
    val lastReviewedEpochMillis: Long,
    val nextReviewEpochMillis: Long
)

/**
 * Stores completed Quiz and Listening test attempts for accuracy and mistake tracking.
 */
@Entity(tableName = "quiz_history")
data class QuizHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val quizMode: String,
    val jlptLevel: String,
    val correctCount: Int,
    val totalQuestions: Int,
    val isListeningQuiz: Boolean,
    val missedQuestionIdsCsv: String,
    val completedAtEpochMillis: Long = System.currentTimeMillis()
)

/**
 * Tracks daily study minutes and completed lessons by ISO date ("YYYY-MM-DD")
 * to calculate honest daily goals, total study time, and multi-day streaks.
 */
@Entity(tableName = "daily_study_logs")
data class DailyStudyLogEntity(
    @PrimaryKey val dateIso: String,
    val studyMinutes: Int,
    val completedLessonsCount: Int,
    val updatedAtEpochMillis: Long = System.currentTimeMillis()
)

/**
 * Tracks downloaded offline audio pack metadata.
 */
@Entity(tableName = "audio_pack_metadata")
data class AudioPackEntity(
    @PrimaryKey val packId: String,
    val jlptLevel: String,
    val isDownloaded: Boolean,
    val localDirectoryPath: String,
    val sizeBytes: Long,
    val updatedAtEpochMillis: Long = System.currentTimeMillis()
)
