package com.nihongo.app.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nihongo.app.data.local.database.dao.ContentDao
import com.nihongo.app.data.local.database.dao.ProgressDao
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

/**
 * Offline Room SQLite Database for NIHONGO (Section 15).
 * Includes explicit non-destructive migration pathways so user progress is never lost on update.
 */
@Database(
    entities = [
        KanaEntity::class,
        KanjiEntity::class,
        VocabularyEntity::class,
        GrammarEntity::class,
        ExerciseEntity::class,
        UserProgressEntity::class,
        SrsReviewEntity::class,
        QuizHistoryEntity::class,
        DailyStudyLogEntity::class,
        AudioPackEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class NihongoDatabase : RoomDatabase() {

    abstract fun contentDao(): ContentDao
    abstract fun progressDao(): ProgressDao

    companion object {
        private const val DATABASE_NAME = "nihongo_offline.db"

        /**
         * Example forward-compatible safe migration from schema v1 to v2 that preserves
         * all existing user_item_progress, srs_reviews, quiz_history, and daily_study_logs rows.
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS `index_user_item_progress_jlptLevel` ON `user_item_progress` (`jlptLevel`)"
                )
            }
        }

        @Volatile
        private var INSTANCE: NihongoDatabase? = null

        fun getInstance(context: Context): NihongoDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    NihongoDatabase::class.java,
                    DATABASE_NAME
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
