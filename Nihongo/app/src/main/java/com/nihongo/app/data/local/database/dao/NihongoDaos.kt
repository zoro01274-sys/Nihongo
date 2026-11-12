package com.nihongo.app.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.nihongo.app.data.local.database.entity.AudioPackEntity
import com.nihongo.app.data.local.database.entity.DailyStudyLogEntity
import com.nihongo.app.data.local.database.entity.ExerciseEntity
import com.nihongo.app.data.local.database.entity.GrammarEntity
import com.nihongo.app.data.local.database.entity.KanaEntity
import com.nihongo.app.data.local.database.entity.KanjiEntity
import com.nihongo.app.data.local.database.entity.QuizHistoryEntity
import com.nihongo.app.data.local.database.entity.SrsReviewEntity
import com.nihongo.app.data.local.database.entity.UserProgressEntity
import com.nihongo.app.data.local.database.entity.VocabularyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContentDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertKanaBatch(items: List<KanaEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertKanjiBatch(items: List<KanjiEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertVocabularyBatch(items: List<VocabularyEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGrammarBatch(items: List<GrammarEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExercisesBatch(items: List<ExerciseEntity>)

    @Query("SELECT COUNT(*) FROM kana_items")
    fun observeTotalKanaCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM kanji_items")
    fun observeTotalKanjiCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM vocabulary_items")
    fun observeTotalVocabularyCount(): Flow<Int>

    @Query("SELECT * FROM vocabulary_items ORDER BY jlptLevel DESC, id ASC")
    fun observeAllVocabulary(): Flow<List<VocabularyEntity>>

    @Query("SELECT * FROM listening_exercises ORDER BY id ASC")
    fun observeListeningExercises(): Flow<List<ExerciseEntity>>

    @Query(
        """
        SELECT * FROM vocabulary_items
        WHERE wordJp LIKE '%' || :query || '%'
           OR hiraganaReading LIKE '%' || :query || '%'
           OR romaji LIKE '%' || :query || '%'
           OR meaningBn LIKE '%' || :query || '%'
           OR meaningEn LIKE '%' || :query || '%'
        """
    )
    suspend fun searchVocabularyOffline(query: String): List<VocabularyEntity>
}

@Dao
interface ProgressDao {

    @Query("SELECT * FROM user_item_progress")
    fun observeAllItemProgress(): Flow<List<UserProgressEntity>>

    @Query("SELECT * FROM user_item_progress WHERE itemId = :itemId LIMIT 1")
    suspend fun getItemProgress(itemId: String): UserProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertItemProgress(entity: UserProgressEntity)

    @Query("SELECT * FROM srs_reviews")
    fun observeAllSrsReviews(): Flow<List<SrsReviewEntity>>

    @Query("SELECT * FROM srs_reviews WHERE itemId = :itemId LIMIT 1")
    suspend fun getSrsReview(itemId: String): SrsReviewEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSrsReview(entity: SrsReviewEntity)

    @Query("SELECT * FROM quiz_history ORDER BY completedAtEpochMillis DESC")
    fun observeQuizHistory(): Flow<List<QuizHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizHistory(entity: QuizHistoryEntity)

    @Query("SELECT * FROM daily_study_logs ORDER BY dateIso DESC")
    fun observeDailyStudyLogs(): Flow<List<DailyStudyLogEntity>>

    @Query("SELECT * FROM daily_study_logs WHERE dateIso = :dateIso LIMIT 1")
    suspend fun getDailyLog(dateIso: String): DailyStudyLogEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDailyLog(entity: DailyStudyLogEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAudioPackMetadata(entity: AudioPackEntity)

    @Query("DELETE FROM user_item_progress")
    suspend fun clearAllItemProgress()

    @Query("DELETE FROM srs_reviews")
    suspend fun clearAllSrsReviews()

    @Query("DELETE FROM quiz_history")
    suspend fun clearAllQuizHistory()

    @Query("DELETE FROM daily_study_logs")
    suspend fun clearAllDailyStudyLogs()

    @Transaction
    suspend fun resetAllUserProgressTransaction() {
        clearAllItemProgress()
        clearAllSrsReviews()
        clearAllQuizHistory()
        clearAllDailyStudyLogs()
    }
}
