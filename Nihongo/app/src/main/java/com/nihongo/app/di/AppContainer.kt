package com.nihongo.app.di

import android.content.Context
import com.nihongo.app.audio.JapaneseAudioEngine
import com.nihongo.app.data.local.database.DatabaseSeeder
import com.nihongo.app.data.local.database.NihongoDatabase
import com.nihongo.app.data.local.datastore.UserPreferencesRepository
import com.nihongo.app.data.repository.NihongoRepository

/**
 * Application-scoped dependency container holding singletons for DataStore preferences,
 * Room Offline Database, DatabaseSeeder, NihongoRepository, and JapaneseAudioEngine.
 */
class AppContainer(context: Context) {
    private val appContext = context.applicationContext

    val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(appContext)
    }

    val database: NihongoDatabase by lazy {
        NihongoDatabase.getInstance(appContext)
    }

    val databaseSeeder: DatabaseSeeder by lazy {
        DatabaseSeeder(appContext, database)
    }

    val nihongoRepository: NihongoRepository by lazy {
        NihongoRepository(database)
    }

    val japaneseAudioEngine: JapaneseAudioEngine by lazy {
        JapaneseAudioEngine(appContext)
    }
}
