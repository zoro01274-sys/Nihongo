package com.nihongo.app.ui.screens.home

import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Translate
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nihongo.app.audio.AudioSourceType
import com.nihongo.app.data.local.datastore.UserPreferences
import com.nihongo.app.domain.model.UserStats
import com.nihongo.app.ui.components.AudioSpeakerButton
import com.nihongo.app.ui.components.BilingualMeaningBlock
import com.nihongo.app.ui.components.JlptBadge
import com.nihongo.app.ui.components.NihongoCard
import com.nihongo.app.ui.components.NihongoPrimaryButton
import com.nihongo.app.ui.theme.NihongoTheme

/**
 * Home Dashboard Screen bound to real Room database statistics (`UserStats`).
 * Enforces Section 18: "Never display fake progress."
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userPreferences: UserPreferences,
    userStats: UserStats,
    onNavigateToHiragana: () -> Unit,
    onNavigateToKatakana: () -> Unit,
    onNavigateToKanji: () -> Unit,
    onNavigateToVocabulary: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onPlayJapaneseAudio: (String, Boolean) -> Unit,
    onNavigateToPractice: () -> Unit
) {
    val language = userPreferences.appLanguage
    val goalMinutes = userPreferences.dailyGoalMinutes.coerceAtLeast(1)
    val progressFraction = (userStats.todayStudyMinutes.toFloat() / goalMinutes.toFloat()).coerceIn(0f, 1f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = "NIHONGO",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = NihongoTheme.colors.primaryText
                    )
                    Text(
                        text = language.selectText("ধাপে ধাপে জাপানি ভাষা শিখুন", "Learn Japanese. Step by Step."),
                        style = MaterialTheme.typography.bodySmall,
                        color = NihongoTheme.colors.secondaryText
                    )
                }
            },
            actions = {
                JlptBadge(level = userPreferences.currentJlptLevel)
                Spacer(modifier = Modifier.width(8.dp))
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
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // 1. Real Daily Goal & Real Streak Hero Card
            item {
                NihongoCard(isSelected = true) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = NihongoTheme.colors.phoenixFlowerRed.copy(alpha = 0.15f),
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocalFireDepartment,
                                        contentDescription = null,
                                        tint = NihongoTheme.colors.phoenixFlowerRed,
                                        modifier = Modifier.padding(6.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = language.selectText(
                                        "ধারাবাহিক স্ট্রিক: ${userStats.currentStreakDays} দিন",
                                        "Learning Streak: ${userStats.currentStreakDays} Days"
                                    ),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = NihongoTheme.colors.phoenixFlowerRed
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = language.selectText(
                                    "আজকের অধ্যয়ন: ${userStats.todayStudyMinutes} / $goalMinutes মিনিট",
                                    "Today's Study: ${userStats.todayStudyMinutes} / $goalMinutes mins"
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                                color = NihongoTheme.colors.primaryText
                            )
                            Text(
                                text = language.selectText(
                                    "মোট সম্পন্ন পাঠ: ${userStats.completedLessonsCount}",
                                    "Completed Lessons: ${userStats.completedLessonsCount}"
                                ),
                                style = MaterialTheme.typography.bodySmall,
                                color = NihongoTheme.colors.secondaryText
                            )
                        }

                        val strokeColor = NihongoTheme.colors.inkGreen
                        val trackColor = NihongoTheme.colors.borderOutline.copy(alpha = 0.35f)

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(64.dp)
                        ) {
                            Canvas(modifier = Modifier.size(56.dp)) {
                                drawArc(
                                    color = trackColor,
                                    startAngle = 0f,
                                    sweepAngle = 360f,
                                    useCenter = false,
                                    style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                                )
                                drawArc(
                                    color = strokeColor,
                                    startAngle = -90f,
                                    sweepAngle = progressFraction * 360f,
                                    useCenter = false,
                                    style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                                )
                            }
                            Text(
                                text = "${(progressFraction * 100).toInt()}%",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = NihongoTheme.colors.inkGreen
                            )
                        }
                    }
                }
            }

            // 2. Quick Access to Hiragana & Katakana
            item {
                Column {
                    Text(
                        text = language.selectText("মৌলিক বর্ণমালা (Kana)", "Foundational Kana"),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = NihongoTheme.colors.primaryText
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        NihongoCard(
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToHiragana
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "あ",
                                        style = MaterialTheme.typography.displayMedium,
                                        color = NihongoTheme.colors.inkGreen
                                    )
                                    Text(
                                        text = language.selectText("হিরাগানা", "Hiragana"),
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = NihongoTheme.colors.primaryText
                                    )
                                    Text(
                                        text = language.selectText("৪৬টি মূল বর্ণ", "46 Basic Kana"),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = NihongoTheme.colors.secondaryText
                                    )
                                }
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = NihongoTheme.colors.inkGreen
                                )
                            }
                        }

                        NihongoCard(
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToKatakana
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "ア",
                                        style = MaterialTheme.typography.displayMedium,
                                        color = NihongoTheme.colors.phoenixFlowerRed
                                    )
                                    Text(
                                        text = language.selectText("কাতাকানা", "Katakana"),
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = NihongoTheme.colors.primaryText
                                    )
                                    Text(
                                        text = language.selectText("বিদেশি শব্দ ও নাম", "Loanwords & Names"),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = NihongoTheme.colors.secondaryText
                                    )
                                }
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = NihongoTheme.colors.phoenixFlowerRed
                                )
                            }
                        }
                    }
                }
            }

            // 3. Continue Learning Banner (Real Overall Progress)
            item {
                NihongoCard(onClick = onNavigateToKanji) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = null,
                                    tint = NihongoTheme.colors.inkGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = language.selectText("পড়াশোনা চালিয়ে যান", "Continue Learning"),
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = NihongoTheme.colors.inkGreen
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = language.selectText(
                                    "কাঞ্জি শেখা হয়েছে: ${userStats.learnedKanjiCount} / ${userStats.totalKanjiCount} • শব্দ: ${userStats.learnedVocabCount} / ${userStats.totalVocabCount}",
                                    "Kanji Learned: ${userStats.learnedKanjiCount} / ${userStats.totalKanjiCount} • Vocab: ${userStats.learnedVocabCount} / ${userStats.totalVocabCount}"
                                ),
                                style = MaterialTheme.typography.titleSmall,
                                color = NihongoTheme.colors.primaryText
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { userStats.overallProgressFraction },
                                modifier = Modifier
                                    .fillMaxWidth(0.92f)
                                    .height(6.dp)
                                    .clip(MaterialTheme.shapes.extraSmall),
                                color = NihongoTheme.colors.inkGreen,
                                trackColor = NihongoTheme.colors.borderOutline.copy(alpha = 0.35f)
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = NihongoTheme.colors.primaryText
                        )
                    }
                }
            }

            // 4. Daily Vocabulary Card
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = language.selectText("আজকের নির্বাচিত শব্দ", "Daily Vocabulary"),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = NihongoTheme.colors.primaryText
                        )
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = NihongoTheme.colors.elevatedSurface
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = NihongoTheme.colors.phoenixFlowerRed,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "N5",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = NihongoTheme.colors.phoenixFlowerRed
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    NihongoCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "友達 (ともだち)",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = NihongoTheme.colors.primaryText
                                )
                                Text(
                                    text = "Tomodachi • Noun (বিশেষ্য)",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = NihongoTheme.colors.inkGreen
                                )
                            }
                            AudioSpeakerButton(
                                japaneseText = "友達",
                                onPlayNormal = { text -> onPlayJapaneseAudio(text, false) },
                                onPlaySlow = { text -> onPlayJapaneseAudio(text, true) },
                                showSourceBadge = true,
                                sourceType = AudioSourceType.SYNTHETIC_TTS,
                                language = language
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        BilingualMeaningBlock(
                            bengaliMeaning = "বন্ধু / বান্ধবী",
                            englishMeaning = "Friend / Companion",
                            primaryLanguage = language
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            color = NihongoTheme.colors.elevatedSurface,
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "田中さんは私の友達です。",
                                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                                        color = NihongoTheme.colors.primaryText
                                    )
                                    IconButton(
                                        onClick = { onPlayJapaneseAudio("田中さんは私の友達です。", false) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Translate,
                                            contentDescription = null,
                                            tint = NihongoTheme.colors.inkGreen,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Tanaka-san wa watashi no tomodachi desu.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = NihongoTheme.colors.secondaryText
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = language.selectText(
                                        "তানাকা সাহেব আমার বন্ধু।",
                                        "Mr. Tanaka is my friend."
                                    ),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = NihongoTheme.colors.ancientWoodBrown
                                )
                            }
                        }
                    }
                }
            }

            // 5. Real Spaced Repetition (SRS) Review Reminder Card
            item {
                NihongoCard(isHighlighted = userStats.dueSrsReviewCount > 0) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = NihongoTheme.colors.phoenixFlowerRed.copy(alpha = 0.15f),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsActive,
                                    contentDescription = null,
                                    tint = NihongoTheme.colors.phoenixFlowerRed,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = language.selectText(
                                        "স্পেসড রিপিটিশন সিডিউল (SRS)",
                                        "Spaced Repetition Schedule"
                                    ),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (userStats.dueSrsReviewCount > 0) {
                                        NihongoTheme.colors.phoenixFlowerRed
                                    } else {
                                        NihongoTheme.colors.inkGreen
                                    }
                                )
                                Text(
                                    text = if (userStats.dueSrsReviewCount > 0) {
                                        language.selectText(
                                            "${userStats.dueSrsReviewCount}টি কার্ড আজ রিভিশনের জন্য অপেক্ষমাণ",
                                            "${userStats.dueSrsReviewCount} cards due for SRS review right now"
                                        )
                                    } else {
                                        language.selectText(
                                            "কোনো বকেয়া রিভিশন নেই — নতুন ফ্ল্যাশকার্ড অনুশীলন শুরু করুন",
                                            "0 cards due — start a new flashcard session"
                                        )
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = NihongoTheme.colors.primaryText
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        NihongoPrimaryButton(
                            text = language.selectText("অনুশীলন", "Practice"),
                            onClick = onNavigateToPractice
                        )
                    }
                }
            }
        }
    }
}
