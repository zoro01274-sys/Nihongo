package com.nihongo.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nihongo.app.audio.AudioPlaybackState
import com.nihongo.app.data.local.datastore.ThemeMode
import com.nihongo.app.data.local.datastore.UserPreferences
import com.nihongo.app.domain.model.AppLanguage
import com.nihongo.app.domain.model.JlptLevel
import com.nihongo.app.domain.model.QuizMode
import com.nihongo.app.domain.model.SrsReviewGrade
import com.nihongo.app.domain.model.UserStats
import com.nihongo.app.ui.navigation.BottomNavItem
import com.nihongo.app.ui.navigation.NihongoNavGraph
import com.nihongo.app.ui.theme.NihongoTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (application as NihongoApplication).container
        val userPreferencesRepository = appContainer.userPreferencesRepository
        val databaseSeeder = appContainer.databaseSeeder
        val nihongoRepository = appContainer.nihongoRepository
        val japaneseAudioEngine = appContainer.japaneseAudioEngine

        setContent {
            val userPreferences by userPreferencesRepository.userPreferencesFlow
                .collectAsState(initial = UserPreferences())
            val userStats by nihongoRepository.userStatsFlow
                .collectAsState(initial = UserStats())
            val audioState by japaneseAudioEngine.playbackState.collectAsState()
            val coroutineScope = rememberCoroutineScope()

            LaunchedEffect(userPreferences.isInitialDatabaseSeeded) {
                if (!userPreferences.isInitialDatabaseSeeded) {
                    databaseSeeder.seedIfNeeded()
                    userPreferencesRepository.setInitialDatabaseSeeded(true)
                }
            }

            LaunchedEffect(
                userPreferences.playbackSpeed,
                userPreferences.preferNativeAudio,
                userPreferences.preferredJapaneseVoiceName
            ) {
                japaneseAudioEngine.setPlaybackSpeed(userPreferences.playbackSpeed)
                japaneseAudioEngine.setPreferNativeAudio(userPreferences.preferNativeAudio)
                if (userPreferences.preferredJapaneseVoiceName.isNotBlank()) {
                    japaneseAudioEngine.selectJapaneseVoice(userPreferences.preferredJapaneseVoiceName)
                }
            }

            val useDarkTheme = when (userPreferences.themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            NihongoTheme(darkTheme = useDarkTheme) {
                NihongoMainScaffold(
                    userPreferences = userPreferences,
                    userStats = userStats,
                    audioState = audioState,
                    onSelectLanguage = { lang ->
                        coroutineScope.launch { userPreferencesRepository.setAppLanguage(lang) }
                    },
                    onSelectThemeMode = { mode ->
                        coroutineScope.launch { userPreferencesRepository.setThemeMode(mode) }
                    },
                    onSelectJlptLevel = { level ->
                        coroutineScope.launch { userPreferencesRepository.setCurrentJlptLevel(level) }
                    },
                    onUpdateDailyGoalMinutes = { minutes ->
                        coroutineScope.launch { userPreferencesRepository.setDailyGoalMinutes(minutes) }
                    },
                    onUpdatePlaybackSpeed = { speed ->
                        japaneseAudioEngine.setPlaybackSpeed(speed)
                        coroutineScope.launch { userPreferencesRepository.setPlaybackSpeed(speed) }
                    },
                    onSelectJapaneseVoice = { voiceName ->
                        japaneseAudioEngine.selectJapaneseVoice(voiceName)
                        coroutineScope.launch {
                            userPreferencesRepository.setPreferredJapaneseVoiceName(voiceName)
                        }
                    },
                    onTogglePreferNativeAudio = { preferNative ->
                        japaneseAudioEngine.setPreferNativeAudio(preferNative)
                        coroutineScope.launch {
                            userPreferencesRepository.setPreferNativeAudio(preferNative)
                        }
                    },
                    onToggleRepeatMode = {
                        japaneseAudioEngine.toggleRepeatMode()
                    },
                    onReplayLastAudio = {
                        japaneseAudioEngine.repeatLast()
                    },
                    onStopAudio = {
                        japaneseAudioEngine.stop()
                    },
                    onInstallTtsData = {
                        runCatching {
                            startActivity(japaneseAudioEngine.createInstallTtsDataIntent())
                        }
                    },
                    onToggleOfflinePack = { packId ->
                        japaneseAudioEngine.toggleOfflineAudioPack(packId)
                    },
                    onToggleReviewReminders = { enabled ->
                        coroutineScope.launch { userPreferencesRepository.setReviewRemindersEnabled(enabled) }
                    },
                    onToggleItemLearned = { itemId, contentType, level ->
                        coroutineScope.launch {
                            nihongoRepository.toggleItemLearned(itemId, contentType, level)
                        }
                    },
                    onToggleItemFavorite = { itemId, contentType, level ->
                        coroutineScope.launch {
                            nihongoRepository.toggleItemFavorite(itemId, contentType, level)
                        }
                    },
                    onRecordSrsReview = { itemId, contentType, grade ->
                        coroutineScope.launch {
                            nihongoRepository.recordFlashcardSrsReview(itemId, contentType, grade)
                        }
                    },
                    onRecordQuizAttempt = { mode, level, correct, total, missedIds ->
                        coroutineScope.launch {
                            nihongoRepository.recordQuizAttempt(
                                quizMode = mode.code,
                                jlptLevel = level,
                                correctCount = correct,
                                totalQuestions = total,
                                isListeningQuiz = mode == QuizMode.LISTENING_QUIZ,
                                missedQuestionIds = missedIds
                            )
                        }
                    },
                    onResetProgress = {
                        coroutineScope.launch {
                            nihongoRepository.resetAllLearningProgress()
                            userPreferencesRepository.setDailyGoalMinutes(15)
                            userPreferencesRepository.setPlaybackSpeed(1.0f)
                            userPreferencesRepository.setCurrentJlptLevel(JlptLevel.N5)
                        }
                    },
                    onPlayJapaneseAudio = { japaneseText, slowPlayback ->
                        japaneseAudioEngine.speak(
                            japaneseText = japaneseText,
                            forceSlow = slowPlayback
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun NihongoMainScaffold(
    userPreferences: UserPreferences,
    userStats: UserStats,
    audioState: AudioPlaybackState,
    onSelectLanguage: (AppLanguage) -> Unit,
    onSelectThemeMode: (ThemeMode) -> Unit,
    onSelectJlptLevel: (JlptLevel) -> Unit,
    onUpdateDailyGoalMinutes: (Int) -> Unit,
    onUpdatePlaybackSpeed: (Float) -> Unit,
    onSelectJapaneseVoice: (String) -> Unit,
    onTogglePreferNativeAudio: (Boolean) -> Unit,
    onToggleRepeatMode: () -> Unit,
    onReplayLastAudio: () -> Unit,
    onStopAudio: () -> Unit,
    onInstallTtsData: () -> Unit,
    onToggleOfflinePack: (String) -> Unit,
    onToggleReviewReminders: (Boolean) -> Unit,
    onToggleItemLearned: (String, String, JlptLevel) -> Unit,
    onToggleItemFavorite: (String, String, JlptLevel) -> Unit,
    onRecordSrsReview: (String, String, SrsReviewGrade) -> Unit,
    onRecordQuizAttempt: (QuizMode, JlptLevel, Int, Int, List<String>) -> Unit,
    onResetProgress: () -> Unit,
    onPlayJapaneseAudio: (String, Boolean) -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val bottomTabs = BottomNavItem.entries

    val showBottomBar = bottomTabs.any { tab ->
        currentDestination?.hierarchy?.any { it.route == tab.route } == true
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = NihongoTheme.colors.cardSurface,
                    contentColor = NihongoTheme.colors.primaryText,
                    tonalElevation = 0.dp
                ) {
                    bottomTabs.forEach { tab ->
                        val selected = currentDestination?.hierarchy?.any {
                            it.route == tab.route
                        } == true

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(tab.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.title(userPreferences.appLanguage)
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title(userPreferences.appLanguage),
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = NihongoTheme.colors.inkGreen,
                                selectedTextColor = NihongoTheme.colors.inkGreen,
                                indicatorColor = NihongoTheme.colors.selectedContainer,
                                unselectedIconColor = NihongoTheme.colors.secondaryText,
                                unselectedTextColor = NihongoTheme.colors.secondaryText
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NihongoNavGraph(
            navController = navController,
            userPreferences = userPreferences,
            userStats = userStats,
            audioState = audioState,
            onSelectLanguage = onSelectLanguage,
            onSelectThemeMode = onSelectThemeMode,
            onSelectJlptLevel = onSelectJlptLevel,
            onUpdateDailyGoalMinutes = onUpdateDailyGoalMinutes,
            onUpdatePlaybackSpeed = onUpdatePlaybackSpeed,
            onSelectJapaneseVoice = onSelectJapaneseVoice,
            onTogglePreferNativeAudio = onTogglePreferNativeAudio,
            onToggleRepeatMode = onToggleRepeatMode,
            onReplayLastAudio = onReplayLastAudio,
            onStopAudio = onStopAudio,
            onInstallTtsData = onInstallTtsData,
            onToggleOfflinePack = onToggleOfflinePack,
            onToggleReviewReminders = onToggleReviewReminders,
            onToggleItemLearned = onToggleItemLearned,
            onToggleItemFavorite = onToggleItemFavorite,
            onRecordSrsReview = onRecordSrsReview,
            onRecordQuizAttempt = onRecordQuizAttempt,
            onResetProgress = onResetProgress,
            onPlayJapaneseAudio = onPlayJapaneseAudio,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
