package com.nihongo.app.ui.screens.listening

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nihongo.app.audio.AudioPlaybackState
import com.nihongo.app.data.local.datastore.UserPreferences
import com.nihongo.app.domain.model.LearningCatalog
import com.nihongo.app.ui.components.AudioControlBar
import com.nihongo.app.ui.components.AudioSpeakerButton
import com.nihongo.app.ui.components.BilingualMeaningBlock
import com.nihongo.app.ui.components.JlptBadge
import com.nihongo.app.ui.components.NihongoCard
import com.nihongo.app.ui.components.NihongoOutlinedButton
import com.nihongo.app.ui.components.NihongoPrimaryButton
import com.nihongo.app.ui.theme.NihongoTheme

/**
 * Upgraded Listening & Pronunciation Hub (Sections 04, 10, and 12).
 * Integrates live JapaneseAudioEngine controls (0.75x, 1.0x, 1.25x, Slow Study, Repeat),
 * Word Pronunciation, Sentence Pronunciation, and Offline Audio Pack management.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListeningScreen(
    userPreferences: UserPreferences,
    audioState: AudioPlaybackState,
    onPlayJapaneseAudio: (String, Boolean) -> Unit,
    onSelectSpeed: (Float) -> Unit,
    onToggleRepeat: () -> Unit,
    onReplayLast: () -> Unit,
    onStopAudio: () -> Unit,
    onInstallTtsData: () -> Unit,
    onToggleOfflinePack: (String) -> Unit
) {
    val language = userPreferences.appLanguage
    // 0 = Word & Sentence Pronunciation, 1 = Offline Audio Packs
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = language.selectText("শ্রবণ ও উচ্চারণ ল্যাব", "Listening & Audio Lab"),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = NihongoTheme.colors.primaryText
                )
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
            // 1. Live Japanese Audio Controller Bar (0.75x / 1.0x / 1.25x / Repeat / Voice Status)
            item {
                AudioControlBar(
                    audioState = audioState,
                    language = language,
                    onSelectSpeed = onSelectSpeed,
                    onToggleRepeat = onToggleRepeat,
                    onReplayLast = onReplayLast,
                    onStopAudio = onStopAudio,
                    onInstallTtsData = onInstallTtsData
                )
            }

            // 2. Mode Switcher: Pronunciation Drills vs Offline Audio Packs
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FilterChip(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        label = {
                            Text(language.selectText("শব্দ ও বাক্য শ্রবণ", "Words & Sentences"))
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Hearing, contentDescription = null)
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NihongoTheme.colors.selectedContainer,
                            selectedLabelColor = NihongoTheme.colors.inkGreen
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    FilterChip(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        label = {
                            Text(language.selectText("অফলাইন অডিও প্যাক", "Offline Audio Packs"))
                        },
                        leadingIcon = {
                            Icon(Icons.Default.CloudDownload, contentDescription = null)
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NihongoTheme.colors.selectedContainer,
                            selectedLabelColor = NihongoTheme.colors.inkGreen
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (selectedTab == 0) {
                // Word & Sentence Pronunciation Practice Items from Verified Catalog
                items(
                    items = LearningCatalog.vocabularyList,
                    key = { it.id }
                ) { vocab ->
                    val isWordPlaying = audioState.isPlaying && audioState.activeJapaneseText == vocab.wordJp
                    val isSentencePlaying = audioState.isPlaying &&
                        audioState.activeJapaneseText == vocab.exampleSentenceJp

                    NihongoCard(isSelected = isWordPlaying || isSentencePlaying) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                JlptBadge(level = vocab.jlptLevel)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "${vocab.wordJp} (${vocab.hiraganaReading})",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = NihongoTheme.colors.primaryText
                                )
                                Text(
                                    text = vocab.romaji,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = NihongoTheme.colors.inkGreen
                                )
                            }

                            AudioSpeakerButton(
                                japaneseText = vocab.wordJp,
                                onPlayNormal = { onPlayJapaneseAudio(vocab.wordJp, false) },
                                onPlaySlow = { onPlayJapaneseAudio(vocab.wordJp, true) },
                                isPlaying = isWordPlaying,
                                sourceType = audioState.activeSourceType,
                                showSourceBadge = true,
                                language = language
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        BilingualMeaningBlock(
                            bengaliMeaning = vocab.meaningBn,
                            englishMeaning = vocab.meaningEn,
                            primaryLanguage = language
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Sentence Pronunciation Sub-Card
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = NihongoTheme.colors.elevatedSurface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = vocab.exampleSentenceJp,
                                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                                            color = NihongoTheme.colors.primaryText
                                        )
                                        Text(
                                            text = vocab.exampleSentenceReading,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = NihongoTheme.colors.inkGreen
                                        )
                                        Text(
                                            text = vocab.exampleSentenceRomaji,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = NihongoTheme.colors.secondaryText
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    AudioSpeakerButton(
                                        japaneseText = vocab.exampleSentenceJp,
                                        onPlayNormal = { onPlayJapaneseAudio(vocab.exampleSentenceJp, false) },
                                        onPlaySlow = { onPlayJapaneseAudio(vocab.exampleSentenceJp, true) },
                                        isPlaying = isSentencePlaying,
                                        language = language
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = language.selectText(vocab.exampleSentenceBn, vocab.exampleSentenceEn),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = NihongoTheme.colors.primaryText
                                )
                            }
                        }
                    }
                }
            } else {
                // Offline Audio Packs Manager (Section 10 Offline Audio requirement)
                item {
                    NihongoCard {
                        Text(
                            text = language.selectText(
                                "অফলাইন অডিও এবং টিটিএস ভয়েস তথ্য",
                                "Offline Audio & TTS Voice Transparency"
                            ),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = NihongoTheme.colors.inkGreen
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = language.selectText(
                                "NIHONGO ডিফল্টভাবে আপনার ফোনের অফলাইন জাপানি (ja-JP) সিন্থেটিক টিটিএস ব্যবহার করে। নিচের প্যাকগুলো ফোনের লোকাল স্টোরেজে সংরক্ষিত থাকলে নেটিভ রেকর্ডিং অগ্রাধিকার পাবে।",
                                "NIHONGO uses your phone's installed offline Japanese (ja-JP) TTS voice by default. When local audio packs are provisioned below, prerecorded native audio is prioritized automatically."
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = NihongoTheme.colors.secondaryText
                        )
                    }
                }

                items(
                    items = audioState.audioPacks,
                    key = { it.packId }
                ) { pack ->
                    NihongoCard(isSelected = pack.isInstalledLocally) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    JlptBadge(level = pack.jlptLevel)
                                    Text(
                                        text = "${pack.itemCount} items • ${pack.sizeMegabytes} MB",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = NihongoTheme.colors.secondaryText
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = language.selectText(pack.titleBn, pack.titleEn),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = NihongoTheme.colors.primaryText
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            if (pack.isInstalledLocally) {
                                NihongoOutlinedButton(
                                    text = language.selectText("সক্রিয় ✓", "Ready ✓"),
                                    onClick = { onToggleOfflinePack(pack.packId) },
                                    leadingIcon = Icons.Default.CheckCircle
                                )
                            } else {
                                NihongoPrimaryButton(
                                    text = language.selectText("সংরক্ষণ", "Enable"),
                                    onClick = { onToggleOfflinePack(pack.packId) },
                                    leadingIcon = Icons.Default.CloudDownload
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
