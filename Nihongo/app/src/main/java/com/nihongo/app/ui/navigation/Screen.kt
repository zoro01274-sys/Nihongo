package com.nihongo.app.ui.navigation

/**
 * Central route definitions for NIHONGO navigation.
 */
sealed class Screen(val route: String) {
    // 5 Primary Bottom Navigation Tabs (Section 04)
    data object Home : Screen("home")
    data object Learn : Screen("learn")
    data object Practice : Screen("practice")
    data object Listening : Screen("listening")
    data object Profile : Screen("profile")

    // Dedicated Learning & Practice Modules (Sections 05 to 19)
    data object Hiragana : Screen("hiragana")
    data object Katakana : Screen("katakana")
    data object Kanji : Screen("kanji")
    data object Vocabulary : Screen("vocabulary")
    data object Grammar : Screen("grammar")
    data object Flashcards : Screen("flashcards")
    data object Quiz : Screen("quiz/{mode}") {
        fun createRoute(mode: String = "multiple_choice") = "quiz/$mode"
    }
    data object Speaking : Screen("speaking")
    data object JlptCourse : Screen("jlpt_course/{level}") {
        fun createRoute(level: String) = "jlpt_course/$level"
    }
    data object GlobalSearch : Screen("global_search")
    data object Settings : Screen("settings")
}
