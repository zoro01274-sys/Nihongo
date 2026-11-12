package com.nihongo.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nihongo.app.audio.AudioPlaybackState
import com.nihongo.app.data.local.datastore.ThemeMode
import com.nihongo.app.data.local.datastore.UserPreferences
import com.nihongo.app.domain.model.AppLanguage
import com.nihongo.app.domain.model.JlptLevel
import com.nihongo.app.domain.model.KanaScript
import com.nihongo.app.domain.model.QuizMode
import com.nihongo.app.domain.model.SrsReviewGrade
import com.nihongo.app.domain.model.UserStats
import com.nihongo.app.ui.screens.grammar.GrammarScreen
import com.nihongo.app.ui.screens.home.HomeScreen
import com.nihongo.app.ui.screens.jlpt.JlptCourseScreen
import com.nihongo.app.ui.screens.kana.KanaScreen
import com.nihongo.app.ui.screens.kanji.KanjiScreen
import com.nihongo.app.ui.screens.learn.LearnScreen
import com.nihongo.app.ui.screens.listening.ListeningScreen
import com.nihongo.app.ui.screens.practice.FlashcardScreen
import com.nihongo.app.ui.screens.practice.PracticeScreen
import com.nihongo.app.ui.screens.practice.QuizScreen
import com.nihongo.app.ui.screens.practice.SpeakingScreen
import com.nihongo.app.ui.screens.profile.ProfileScreen
import com.nihongo.app.ui.screens.search.GlobalSearchScreen
import com.nihongo.app.ui.screens.settings.SettingsScreen
import com.nihongo.app.ui.screens.vocabulary.VocabularyScreen

/**
 * Complete Navigation Graph wiring all 16 destinations to persistent Room SQLite actions.
 */
@Composable
fun NihongoNavGraph(
    navController: NavHostController,
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
    onPlayJapaneseAudio: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        // 1. Home Dashboard
        composable(Screen.Home.route) {
            HomeScreen(
                userPreferences = userPreferences,
                userStats = userStats,
                onNavigateToHiragana = { navController.navigate(Screen.Hiragana.route) },
                onNavigateToKatakana = { navController.navigate(Screen.Katakana.route) },
                onNavigateToKanji = { navController.navigate(Screen.Kanji.route) },
                onNavigateToVocabulary = { navController.navigate(Screen.Vocabulary.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onPlayJapaneseAudio = onPlayJapaneseAudio,
                onNavigateToPractice = { navController.navigate(Screen.Flashcards.route) }
            )
        }

        // 2. Learn Hub
        composable(Screen.Learn.route) {
            LearnScreen(
                userPreferences = userPreferences,
                onNavigateToHiragana = { navController.navigate(Screen.Hiragana.route) },
                onNavigateToKatakana = { navController.navigate(Screen.Katakana.route) },
                onNavigateToKanji = { navController.navigate(Screen.Kanji.route) },
                onNavigateToVocabulary = { navController.navigate(Screen.Vocabulary.route) },
                onNavigateToGrammar = { navController.navigate(Screen.Grammar.route) },
                onNavigateToJlptCourse = { level ->
                    onSelectJlptLevel(level)
                    navController.navigate(Screen.JlptCourse.createRoute(level.label))
                },
                onNavigateToGlobalSearch = { navController.navigate(Screen.GlobalSearch.route) }
            )
        }

        // 3. Practice Hub
        composable(Screen.Practice.route) {
            PracticeScreen(
                userPreferences = userPreferences,
                onNavigateToFlashcards = { navController.navigate(Screen.Flashcards.route) },
                onNavigateToQuiz = { modeCode ->
                    navController.navigate(Screen.Quiz.createRoute(modeCode))
                },
                onNavigateToSpeaking = { navController.navigate(Screen.Speaking.route) }
            )
        }

        // 4. Listening & Audio Lab
        composable(Screen.Listening.route) {
            ListeningScreen(
                userPreferences = userPreferences,
                audioState = audioState,
                onPlayJapaneseAudio = onPlayJapaneseAudio,
                onSelectSpeed = onUpdatePlaybackSpeed,
                onToggleRepeat = onToggleRepeatMode,
                onReplayLast = onReplayLastAudio,
                onStopAudio = onStopAudio,
                onInstallTtsData = onInstallTtsData,
                onToggleOfflinePack = onToggleOfflinePack
            )
        }

        // 5. Profile & Statistics
        composable(Screen.Profile.route) {
            ProfileScreen(
                userPreferences = userPreferences,
                userStats = userStats,
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }

        // 6. Settings Screen
        composable(Screen.Settings.route) {
            SettingsScreen(
                userPreferences = userPreferences,
                audioState = audioState,
                onNavigateBack = { navController.popBackStack() },
                onSelectLanguage = onSelectLanguage,
                onSelectThemeMode = onSelectThemeMode,
                onSelectJlptLevel = onSelectJlptLevel,
                onUpdateDailyGoalMinutes = onUpdateDailyGoalMinutes,
                onUpdatePlaybackSpeed = onUpdatePlaybackSpeed,
                onSelectJapaneseVoice = onSelectJapaneseVoice,
                onTogglePreferNativeAudio = onTogglePreferNativeAudio,
                onInstallTtsData = onInstallTtsData,
                onToggleReviewReminders = onToggleReviewReminders,
                onResetProgress = onResetProgress
            )
        }

        // 7. Hiragana Module
        composable(Screen.Hiragana.route) {
            KanaScreen(
                script = KanaScript.HIRAGANA,
                userPreferences = userPreferences,
                learnedItemIds = userStats.learnedItemIds,
                onToggleLearned = { id -> onToggleItemLearned(id, "KANA", JlptLevel.N5) },
                onNavigateBack = { navController.popBackStack() },
                onSwitchScript = {
                    navController.navigate(Screen.Katakana.route) {
                        popUpTo(Screen.Hiragana.route) { inclusive = true }
                    }
                },
                onPlayJapaneseAudio = onPlayJapaneseAudio
            )
        }

        // 8. Katakana Module
        composable(Screen.Katakana.route) {
            KanaScreen(
                script = KanaScript.KATAKANA,
                userPreferences = userPreferences,
                learnedItemIds = userStats.learnedItemIds,
                onToggleLearned = { id -> onToggleItemLearned(id, "KANA", JlptLevel.N5) },
                onNavigateBack = { navController.popBackStack() },
                onSwitchScript = {
                    navController.navigate(Screen.Hiragana.route) {
                        popUpTo(Screen.Katakana.route) { inclusive = true }
                    }
                },
                onPlayJapaneseAudio = onPlayJapaneseAudio
            )
        }

        // 9. Kanji Module
        composable(Screen.Kanji.route) {
            KanjiScreen(
                userPreferences = userPreferences,
                learnedItemIds = userStats.learnedItemIds,
                favoriteItemIds = userStats.favoriteItemIds,
                onToggleLearned = { id, level -> onToggleItemLearned(id, "KANJI", level) },
                onToggleFavorite = { id, level -> onToggleItemFavorite(id, "KANJI", level) },
                onNavigateBack = { navController.popBackStack() },
                onPlayJapaneseAudio = onPlayJapaneseAudio
            )
        }

        // 10. Vocabulary Module
        composable(Screen.Vocabulary.route) {
            VocabularyScreen(
                userPreferences = userPreferences,
                learnedItemIds = userStats.learnedItemIds,
                favoriteItemIds = userStats.favoriteItemIds,
                onToggleLearned = { id, level -> onToggleItemLearned(id, "VOCAB", level) },
                onToggleFavorite = { id, level -> onToggleItemFavorite(id, "VOCAB", level) },
                onNavigateBack = { navController.popBackStack() },
                onPlayJapaneseAudio = onPlayJapaneseAudio
            )
        }

        // 11. Grammar Module
        composable(Screen.Grammar.route) {
            GrammarScreen(
                userPreferences = userPreferences,
                onNavigateBack = { navController.popBackStack() },
                onPlayJapaneseAudio = onPlayJapaneseAudio
            )
        }

        // 12. Global Search Module
        composable(Screen.GlobalSearch.route) {
            GlobalSearchScreen(
                userPreferences = userPreferences,
                onNavigateBack = { navController.popBackStack() },
                onPlayJapaneseAudio = onPlayJapaneseAudio
            )
        }

        // 13. SRS Flashcards Module
        composable(Screen.Flashcards.route) {
            FlashcardScreen(
                userPreferences = userPreferences,
                onRecordSrsReview = onRecordSrsReview,
                onNavigateBack = { navController.popBackStack() },
                onPlayJapaneseAudio = onPlayJapaneseAudio
            )
        }

        // 14. Interactive 9-Mode Quiz Screen
        composable(
            route = Screen.Quiz.route,
            arguments = listOf(
                navArgument("mode") {
                    type = NavType.StringType
                    defaultValue = "multiple_choice"
                }
            )
        ) { backStackEntry ->
            val modeCode = backStackEntry.arguments?.getString("mode") ?: "multiple_choice"
            QuizScreen(
                initialModeCode = modeCode,
                userPreferences = userPreferences,
                persistedMissedQuestionIds = userStats.missedQuestionIds,
                onRecordQuizAttempt = onRecordQuizAttempt,
                onNavigateBack = { navController.popBackStack() },
                onPlayJapaneseAudio = onPlayJapaneseAudio
            )
        }

        // 15. Speaking & Pronunciation Practice Screen
        composable(Screen.Speaking.route) {
            SpeakingScreen(
                userPreferences = userPreferences,
                onNavigateBack = { navController.popBackStack() },
                onPlayJapaneseAudio = onPlayJapaneseAudio
            )
        }

        // 16. JLPT N5 to N1 Dedicated Course Screen
        composable(
            route = Screen.JlptCourse.route,
            arguments = listOf(
                navArgument("level") {
                    type = NavType.StringType
                    defaultValue = "N5"
                }
            )
        ) { backStackEntry ->
            val levelArg = backStackEntry.arguments?.getString("level") ?: "N5"
            JlptCourseScreen(
                initialLevelLabel = levelArg,
                userPreferences = userPreferences,
                onNavigateBack = { navController.popBackStack() },
                onSelectTargetLevel = onSelectJlptLevel,
                onNavigateToVocabulary = { navController.navigate(Screen.Vocabulary.route) },
                onNavigateToKanji = { navController.navigate(Screen.Kanji.route) },
                onNavigateToGrammar = { navController.navigate(Screen.Grammar.route) },
                onNavigateToListening = { navController.navigate(Screen.Listening.route) },
                onNavigateToQuiz = { mode ->
                    navController.navigate(Screen.Quiz.createRoute(mode))
                },
                onPlayJapaneseAudio = onPlayJapaneseAudio
            )
        }
    }
}
