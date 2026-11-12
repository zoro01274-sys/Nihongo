package com.nihongo.app.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nihongo.app.data.local.datastore.UserPreferences
import com.nihongo.app.domain.model.UserStats
import com.nihongo.app.ui.components.JlptBadge
import com.nihongo.app.ui.components.NihongoCard
import com.nihongo.app.ui.theme.NihongoTheme

/**
 * Profile & Learning Progress Screen (Sections 04 and 18).
 * Strictly driven by real Room SQLite `UserStats` — zero fake progress.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userPreferences: UserPreferences,
    userStats: UserStats,
    onNavigateToSettings: () -> Unit
) {
    val language = userPreferences.appLanguage
    val overallPercent = (userStats.overallProgressFraction * 100f).toInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = language.selectText("প্রোফাইল ও অগ্রগতি", "Profile & Progress"),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = NihongoTheme.colors.primaryText
                )
            },
            actions = {
                IconButton(onClick = onNavigateToSettings) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = language.selectText("সেটিংস", "Settings"),
                        tint = NihongoTheme.colors.ancientWoodBrown
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Learner Identity Header
            item {
                NihongoCard(isSelected = true) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = NihongoTheme.colors.inkGreen,
                                modifier = Modifier.size(54.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "日",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = language.selectText("জাপানি শিক্ষার্থী", "Japanese Learner"),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = NihongoTheme.colors.primaryText
                                )
                                Text(
                                    text = language.selectText(
                                        "অফলাইন রুম ডেটাবেস প্রোফাইল • সম্পন্ন পাঠ: ${userStats.completedLessonsCount}",
                                        "Offline Room DB Profile • Completed Lessons: ${userStats.completedLessonsCount}"
                                    ),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = NihongoTheme.colors.secondaryText
                                )
                            }
                        }
                        JlptBadge(level = userPreferences.currentJlptLevel)
                    }
                }
            }

            // 2. Real Database Statistics Grid (Section 18)
            item {
                Text(
                    text = language.selectText("প্রকৃত শেখার পরিসংখ্যান", "Verified Learning Statistics"),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = NihongoTheme.colors.primaryText
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricMiniCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.LocalFireDepartment,
                        iconTint = NihongoTheme.colors.phoenixFlowerRed,
                        value = "${userStats.currentStreakDays}",
                        label = language.selectText("দিন স্ট্রিক", "Days Streak")
                    )
                    MetricMiniCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Spellcheck,
                        iconTint = NihongoTheme.colors.inkGreen,
                        value = "${userStats.learnedKanaCount}/${userStats.totalKanaCount}",
                        label = language.selectText("কানা সম্পন্ন", "Kana Learned")
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricMiniCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.MenuBook,
                        iconTint = NihongoTheme.colors.ancientWoodBrown,
                        value = "${userStats.learnedKanjiCount}/${userStats.totalKanjiCount}",
                        label = language.selectText("কাঞ্জি সম্পন্ন", "Kanji Learned")
                    )
                    MetricMiniCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Bookmark,
                        iconTint = NihongoTheme.colors.inkGreen,
                        value = "${userStats.learnedVocabCount}/${userStats.totalVocabCount}",
                        label = language.selectText("শব্দভাণ্ডার", "Vocab Learned")
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricMiniCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Quiz,
                        iconTint = NihongoTheme.colors.inkGreen,
                        value = "${userStats.quizAccuracyPercent}%",
                        label = language.selectText("কুইজ নির্ভুলতা", "Quiz Accuracy")
                    )
                    MetricMiniCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Hearing,
                        iconTint = NihongoTheme.colors.phoenixFlowerRed,
                        value = "${userStats.listeningAccuracyPercent}%",
                        label = language.selectText("শ্রবণ দক্ষতা", "Listening Score")
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricMiniCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.HourglassTop,
                        iconTint = NihongoTheme.colors.inkGreen,
                        value = "${userStats.totalStudyMinutes}m",
                        label = language.selectText("মোট অধ্যয়ন সময়", "Total Study Time")
                    )
                    MetricMiniCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.EmojiEvents,
                        iconTint = NihongoTheme.colors.ancientWoodBrown,
                        value = "${userStats.favoriteItemIds.size}",
                        label = language.selectText("পছন্দের তালিকা", "Saved Favorites")
                    )
                }
            }

            // 3. Real Overall Learning Progress Bar
            item {
                NihongoCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = language.selectText(
                                "সামগ্রিক কোর্স অগ্রগতি (Total Progress)",
                                "Total Learning Progress"
                            ),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = NihongoTheme.colors.primaryText
                        )
                        Text(
                            text = "$overallPercent%",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = NihongoTheme.colors.inkGreen
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { userStats.overallProgressFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(MaterialTheme.shapes.extraSmall),
                        color = NihongoTheme.colors.inkGreen,
                        trackColor = NihongoTheme.colors.borderOutline.copy(alpha = 0.35f)
                    )
                }
            }

            // 4. Dynamic Real Achievements Card
            item {
                val hasStartedLearning = userStats.completedLessonsCount > 0 || userStats.totalQuizzesTaken > 0
                NihongoCard(isSelected = hasStartedLearning) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = if (hasStartedLearning) {
                                NihongoTheme.colors.phoenixFlowerRed
                            } else {
                                NihongoTheme.colors.secondaryText
                            },
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (hasStartedLearning) {
                                    language.selectText(
                                        "অর্জন আনলকড: সক্রিয় জাপানি শিক্ষার্থী",
                                        "Achievement Unlocked: Active Japanese Scholar"
                                    )
                                } else {
                                    language.selectText(
                                        "অর্জনসমূহ (প্রথম পাঠ সম্পন্ন করে আনলক করুন)",
                                        "Achievements (Complete your first lesson or quiz to unlock)"
                                    )
                                },
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = NihongoTheme.colors.primaryText
                            )
                            Text(
                                text = language.selectText(
                                    "সম্পন্ন কুইজ: ${userStats.totalQuizzesTaken}টি • নির্ধারিত SRS কার্ড: ${userStats.totalSrsScheduledCount}টি",
                                    "Quizzes completed: ${userStats.totalQuizzesTaken} • Scheduled SRS cards: ${userStats.totalSrsScheduledCount}"
                                ),
                                style = MaterialTheme.typography.bodySmall,
                                color = NihongoTheme.colors.secondaryText
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricMiniCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconTint: androidx.compose.ui.graphics.Color,
    value: String,
    label: String
) {
    NihongoCard(
        modifier = modifier,
        contentPadding = 12.dp
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = NihongoTheme.colors.primaryText
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = NihongoTheme.colors.secondaryText
                )
            }
        }
    }
}
