package com.nihongo.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.ui.graphics.vector.ImageVector
import com.nihongo.app.domain.model.AppLanguage

/**
 * Defines the five official bottom navigation tabs required in Section 04:
 * 1. HOME
 * 2. LEARN
 * 3. PRACTICE
 * 4. LISTENING
 * 5. PROFILE
 */
enum class BottomNavItem(
    val route: String,
    val labelEn: String,
    val labelBn: String,
    val icon: ImageVector
) {
    HOME(
        route = Screen.Home.route,
        labelEn = "Home",
        labelBn = "হোম",
        icon = Icons.Default.Home
    ),
    LEARN(
        route = Screen.Learn.route,
        labelEn = "Learn",
        labelBn = "শিখুন",
        icon = Icons.Default.AutoStories
    ),
    PRACTICE(
        route = Screen.Practice.route,
        labelEn = "Practice",
        labelBn = "অনুশীলন",
        icon = Icons.Default.Psychology
    ),
    LISTENING(
        route = Screen.Listening.route,
        labelEn = "Listening",
        labelBn = "শ্রবণ",
        icon = Icons.Default.Headphones
    ),
    PROFILE(
        route = Screen.Profile.route,
        labelEn = "Profile",
        labelBn = "প্রোফাইল",
        icon = Icons.Default.Person
    );

    fun title(language: AppLanguage): String = language.selectText(labelBn, labelEn)
}
