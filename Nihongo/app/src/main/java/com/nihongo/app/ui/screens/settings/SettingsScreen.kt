package com.nihongo.app.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nihongo.app.audio.AudioPlaybackState
import com.nihongo.app.data.local.datastore.ThemeMode
import com.nihongo.app.data.local.datastore.UserPreferences
import com.nihongo.app.domain.model.AppLanguage
import com.nihongo.app.domain.model.JlptLevel
import com.nihongo.app.ui.components.NihongoAccentButton
import com.nihongo.app.ui.components.NihongoCard
import com.nihongo.app.ui.components.NihongoOutlinedButton
import com.nihongo.app.ui.theme.NihongoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    userPreferences: UserPreferences,
    audioState: AudioPlaybackState,
    onNavigateBack: () -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onSelectThemeMode: (ThemeMode) -> Unit,
    onSelectJlptLevel: (JlptLevel) -> Unit,
    onUpdateDailyGoalMinutes: (Int) -> Unit,
    onUpdatePlaybackSpeed: (Float) -> Unit,
    onSelectJapaneseVoice: (String) -> Unit,
    onTogglePreferNativeAudio: (Boolean) -> Unit,
    onInstallTtsData: () -> Unit,
    onToggleReviewReminders: (Boolean) -> Unit,
    onResetProgress: () -> Unit
) {
    val language = userPreferences.appLanguage
    var showResetDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = language.selectText("অ্যাপ সেটিংস", "App Settings"),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = NihongoTheme.colors.primaryText
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = language.selectText("ফিরে যান", "Go back"),
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
            // 1. Interface Language
            item {
                NihongoCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = null,
                            tint = NihongoTheme.colors.inkGreen
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = language.selectText("অ্যাপের ভাষা (Interface Language)", "Interface Language"),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = NihongoTheme.colors.primaryText
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AppLanguage.entries.forEach { lang ->
                            FilterChip(
                                selected = userPreferences.appLanguage == lang,
                                onClick = { onSelectLanguage(lang) },
                                label = { Text(text = "${lang.nativeName} (${lang.englishName})") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NihongoTheme.colors.selectedContainer,
                                    selectedLabelColor = NihongoTheme.colors.inkGreen
                                )
                            )
                        }
                    }
                }
            }

            // 2. Theme Mode
            item {
                NihongoCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.WaterDrop,
                            contentDescription = null,
                            tint = NihongoTheme.colors.inkGreen
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = language.selectText("থিম মোড (Appearance Theme)", "Appearance Theme"),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = NihongoTheme.colors.primaryText
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ThemeMode.entries.forEach { mode ->
                            FilterChip(
                                selected = userPreferences.themeMode == mode,
                                onClick = { onSelectThemeMode(mode) },
                                label = {
                                    Text(text = language.selectText(mode.labelBn, mode.labelEn))
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NihongoTheme.colors.selectedContainer,
                                    selectedLabelColor = NihongoTheme.colors.inkGreen
                                )
                            )
                        }
                    }
                }
            }

            // 3. Japanese Voice Selection & Offline TTS Installer (Section 10 & 19)
            item {
                NihongoCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.RecordVoiceOver,
                            contentDescription = null,
                            tint = NihongoTheme.colors.inkGreen
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = language.selectText(
                                "জাপানি কণ্ঠ নির্বাচন (ja-JP Voice Selection)",
                                "Japanese (ja-JP) Voice Selection"
                            ),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = NihongoTheme.colors.primaryText
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = audioState.engineStatus.description(language),
                        style = MaterialTheme.typography.bodySmall,
                        color = NihongoTheme.colors.secondaryText
                    )

                    if (audioState.availableVoices.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            audioState.availableVoices.take(6).forEach { voice ->
                                FilterChip(
                                    selected = audioState.selectedVoiceName == voice.name,
                                    onClick = { onSelectJapaneseVoice(voice.name) },
                                    label = { Text(voice.displayLabel) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = NihongoTheme.colors.selectedContainer,
                                        selectedLabelColor = NihongoTheme.colors.inkGreen
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = language.selectText(
                                    "স্থানীয় (Native) রেকর্ডিং অগ্রাধিকার দিন",
                                    "Prioritize Native Recordings When Available"
                                ),
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                color = NihongoTheme.colors.primaryText
                            )
                            Text(
                                text = language.selectText(
                                    "অন্যথায় অফলাইন সিন্থেটিক টিটিএস (ja-JP) ব্যবহৃত হবে",
                                    "Falls back to Offline Synthetic TTS (ja-JP)"
                                ),
                                style = MaterialTheme.typography.bodySmall,
                                color = NihongoTheme.colors.secondaryText
                            )
                        }
                        Switch(
                            checked = userPreferences.preferNativeAudio,
                            onCheckedChange = onTogglePreferNativeAudio,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = NihongoTheme.colors.inkGreen,
                                checkedTrackColor = NihongoTheme.colors.selectedContainer
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    NihongoOutlinedButton(
                        text = language.selectText(
                            "অ্যান্ড্রয়েড অফলাইন জাপানি কণ্ঠ ডাউনলোড/যাচাই করুন",
                            "Install / Verify System Offline Japanese Voice"
                        ),
                        onClick = onInstallTtsData,
                        leadingIcon = Icons.Default.Download,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // 4. Default Japanese Audio Speed Slider
            item {
                NihongoCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = null,
                                tint = NihongoTheme.colors.inkGreen
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = language.selectText("অডিও প্লেব্যাক গতি", "Playback Speed"),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = NihongoTheme.colors.primaryText
                            )
                        }
                        Text(
                            text = String.format("%.2fx", userPreferences.playbackSpeed),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = NihongoTheme.colors.inkGreen
                        )
                    }
                    Slider(
                        value = userPreferences.playbackSpeed,
                        onValueChange = onUpdatePlaybackSpeed,
                        valueRange = 0.6f..1.25f,
                        steps = 5,
                        colors = SliderDefaults.colors(
                            thumbColor = NihongoTheme.colors.inkGreen,
                            activeTrackColor = NihongoTheme.colors.inkGreen
                        )
                    )
                }
            }

            // 5. Target JLPT Level
            item {
                NihongoCard {
                    Text(
                        text = language.selectText("লক্ষ্যমাত্রা JLPT লেভেল", "Target JLPT Level"),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = NihongoTheme.colors.primaryText
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        JlptLevel.entries.forEach { level ->
                            FilterChip(
                                selected = userPreferences.currentJlptLevel == level,
                                onClick = { onSelectJlptLevel(level) },
                                label = { Text(text = level.label) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NihongoTheme.colors.selectedContainer,
                                    selectedLabelColor = NihongoTheme.colors.inkGreen
                                )
                            )
                        }
                    }
                }
            }

            // 6. Daily Study Goal Minutes Slider
            item {
                NihongoCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = language.selectText("দৈনিক শেখার লক্ষ্য", "Daily Study Goal"),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = NihongoTheme.colors.primaryText
                        )
                        Text(
                            text = "${userPreferences.dailyGoalMinutes} min",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = NihongoTheme.colors.inkGreen
                        )
                    }
                    Slider(
                        value = userPreferences.dailyGoalMinutes.toFloat(),
                        onValueChange = { onUpdateDailyGoalMinutes(it.toInt()) },
                        valueRange = 5f..60f,
                        steps = 10,
                        colors = SliderDefaults.colors(
                            thumbColor = NihongoTheme.colors.inkGreen,
                            activeTrackColor = NihongoTheme.colors.inkGreen
                        )
                    )
                }
            }

            // 7. Review Reminders Toggle
            item {
                NihongoCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                tint = NihongoTheme.colors.ancientWoodBrown
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = language.selectText("দৈনিক রিভিশন নোটিফিকেশন", "Daily Review Reminders"),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = NihongoTheme.colors.primaryText
                                )
                                Text(
                                    text = language.selectText(
                                        "স্পেসড রিপিটিশন কার্ডের সময় হলে মনে করিয়ে দিন",
                                        "Alert when SRS flashcards are due"
                                    ),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = NihongoTheme.colors.secondaryText
                                )
                            }
                        }
                        Switch(
                            checked = userPreferences.reviewRemindersEnabled,
                            onCheckedChange = onToggleReviewReminders,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = NihongoTheme.colors.inkGreen,
                                checkedTrackColor = NihongoTheme.colors.selectedContainer
                            )
                        )
                    }
                }
            }

            // 8. Reset Learning Progress
            item {
                NihongoCard(isHighlighted = true) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = null,
                                tint = NihongoTheme.colors.phoenixFlowerRed
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = language.selectText("অগ্রগতি রিসেট করুন", "Reset Learning Progress"),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = NihongoTheme.colors.phoenixFlowerRed
                                )
                                Text(
                                    text = language.selectText(
                                        "সকল অধ্যায়ন ও কুইজের রেকর্ড মুছে ফেলুন",
                                        "Clear study records, streaks & SRS items"
                                    ),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = NihongoTheme.colors.secondaryText
                                )
                            }
                        }
                        NihongoAccentButton(
                            text = language.selectText("রিসেট", "Reset"),
                            onClick = { showResetDialog = true }
                        )
                    }
                }
            }

            // 9. About Nihongo
            item {
                NihongoCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = NihongoTheme.colors.ancientWoodBrown
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "NIHONGO v1.0.0",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = NihongoTheme.colors.primaryText
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = language.selectText(
                            "শতভাগ বিজ্ঞাপনমুক্ত, অফলাইন এবং বিনামূল্যে জাপানি ভাষা শেখার অ্যান্ড্রয়েড অ্যাপ। কোনো গোপন ট্র্যাকিং বা পেইড সাবস্ক্রিপশন নেই।",
                            "100% Ad-free, offline-first, free Japanese learning app. No hidden tracking or paid subscriptions."
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = NihongoTheme.colors.secondaryText
                    )
                }
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = {
                Text(
                    text = language.selectText("আপনি কি নিশ্চিত?", "Are you sure?"),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = NihongoTheme.colors.phoenixFlowerRed
                )
            },
            text = {
                Text(
                    text = language.selectText(
                        "আপনার সকল অধ্যায়ন অগ্রগতি ও কুইজ হিস্ট্রি স্থায়ীভাবে মুছে যাবে। এই কাজটি আর পূর্বাবস্থায় ফিরিয়ে আনা যাবে না।",
                        "All your study progress and quiz history will be permanently cleared. This cannot be undone."
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = NihongoTheme.colors.primaryText
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showResetDialog = false
                        onResetProgress()
                    }
                ) {
                    Text(
                        text = language.selectText("হ্যাঁ, মুছে ফেলুন", "Yes, Reset"),
                        color = NihongoTheme.colors.phoenixFlowerRed,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text(
                        text = language.selectText("বাতিল", "Cancel"),
                        color = NihongoTheme.colors.ancientWoodBrown
                    )
                }
            },
            containerColor = NihongoTheme.colors.cardSurface
        )
    }
}
