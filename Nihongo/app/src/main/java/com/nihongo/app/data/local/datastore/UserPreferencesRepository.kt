package com.nihongo.app.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.nihongo.app.domain.model.AppLanguage
import com.nihongo.app.domain.model.JlptLevel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "nihongo_user_preferences"
)

enum class ThemeMode(val code: String, val labelEn: String, val labelBn: String) {
    SYSTEM("system", "System Default", "সিস্টেম ডিফল্ট"),
    LIGHT("light", "Light (Mist Gray)", "হালকা (মিস্ট গ্রে)"),
    DARK("dark", "Dark (Forest Ink)", "গাঢ় (ফরেস্ট ইঙ্ক)");

    companion object {
        fun fromCode(code: String?): ThemeMode {
            return entries.firstOrNull { it.code == code } ?: SYSTEM
        }
    }
}

data class UserPreferences(
    val appLanguage: AppLanguage = AppLanguage.BENGALI,
    val themeMode: ThemeMode = ThemeMode.LIGHT,
    val currentJlptLevel: JlptLevel = JlptLevel.N5,
    val dailyGoalMinutes: Int = 15,
    val playbackSpeed: Float = 1.0f,
    val preferredJapaneseVoiceName: String = "",
    val preferNativeAudio: Boolean = true,
    val reviewRemindersEnabled: Boolean = true,
    val isInitialDatabaseSeeded: Boolean = false
)

class UserPreferencesRepository(private val context: Context) {

    private object Keys {
        val APP_LANGUAGE = stringPreferencesKey("app_language")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val CURRENT_JLPT_LEVEL = stringPreferencesKey("current_jlpt_level")
        val DAILY_GOAL_MINUTES = intPreferencesKey("daily_goal_minutes")
        val PLAYBACK_SPEED = floatPreferencesKey("playback_speed")
        val PREFERRED_VOICE_NAME = stringPreferencesKey("preferred_voice_name")
        val PREFER_NATIVE_AUDIO = booleanPreferencesKey("prefer_native_audio")
        val REVIEW_REMINDERS = booleanPreferencesKey("review_reminders")
        val INITIAL_DB_SEEDED = booleanPreferencesKey("initial_db_seeded")
    }

    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { prefs ->
            UserPreferences(
                appLanguage = AppLanguage.fromCode(prefs[Keys.APP_LANGUAGE]),
                themeMode = ThemeMode.fromCode(prefs[Keys.THEME_MODE]),
                currentJlptLevel = JlptLevel.fromLabel(prefs[Keys.CURRENT_JLPT_LEVEL]),
                dailyGoalMinutes = prefs[Keys.DAILY_GOAL_MINUTES] ?: 15,
                playbackSpeed = prefs[Keys.PLAYBACK_SPEED] ?: 1.0f,
                preferredJapaneseVoiceName = prefs[Keys.PREFERRED_VOICE_NAME] ?: "",
                preferNativeAudio = prefs[Keys.PREFER_NATIVE_AUDIO] ?: true,
                reviewRemindersEnabled = prefs[Keys.REVIEW_REMINDERS] ?: true,
                isInitialDatabaseSeeded = prefs[Keys.INITIAL_DB_SEEDED] ?: false
            )
        }

    suspend fun setAppLanguage(language: AppLanguage) {
        context.dataStore.edit { it[Keys.APP_LANGUAGE] = language.code }
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { it[Keys.THEME_MODE] = mode.code }
    }

    suspend fun setCurrentJlptLevel(level: JlptLevel) {
        context.dataStore.edit { it[Keys.CURRENT_JLPT_LEVEL] = level.label }
    }

    suspend fun setDailyGoalMinutes(minutes: Int) {
        context.dataStore.edit { it[Keys.DAILY_GOAL_MINUTES] = minutes.coerceIn(5, 120) }
    }

    suspend fun setPlaybackSpeed(speed: Float) {
        context.dataStore.edit { it[Keys.PLAYBACK_SPEED] = speed.coerceIn(0.5f, 1.5f) }
    }

    suspend fun setPreferredJapaneseVoiceName(voiceName: String) {
        context.dataStore.edit { it[Keys.PREFERRED_VOICE_NAME] = voiceName }
    }

    suspend fun setPreferNativeAudio(preferNative: Boolean) {
        context.dataStore.edit { it[Keys.PREFER_NATIVE_AUDIO] = preferNative }
    }

    suspend fun setReviewRemindersEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.REVIEW_REMINDERS] = enabled }
    }

    suspend fun setInitialDatabaseSeeded(seeded: Boolean) {
        context.dataStore.edit { it[Keys.INITIAL_DB_SEEDED] = seeded }
    }
}
