package com.nihongo.app.ui.screens.kana

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nihongo.app.audio.AudioSourceType
import com.nihongo.app.domain.model.AppLanguage
import com.nihongo.app.domain.model.KanaItem
import com.nihongo.app.ui.components.AudioSpeakerButton
import com.nihongo.app.ui.components.BilingualMeaningBlock
import com.nihongo.app.ui.components.LessonCompletionBadge
import com.nihongo.app.ui.components.NihongoCard
import com.nihongo.app.ui.components.NihongoOutlinedButton
import com.nihongo.app.ui.components.NihongoPrimaryButton
import com.nihongo.app.ui.components.StrokeOrderCanvas
import com.nihongo.app.ui.theme.NihongoTheme

/**
 * Full character learning detail view required by Section 05 & 06:
 * "Each character must have its own learning detail screen."
 * Includes pronunciation breakdown, bilingual example word with audio, and interactive stroke-order pad.
 */
@Composable
fun KanaDetailView(
    item: KanaItem,
    isLearned: Boolean,
    hasPrevious: Boolean,
    hasNext: Boolean,
    language: AppLanguage,
    onToggleLearned: (String) -> Unit,
    onPlayJapaneseAudio: (String, Boolean) -> Unit,
    onPreviousCharacter: () -> Unit,
    onNextCharacter: () -> Unit,
    onCloseDetail: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 0 = Pronunciation & Example Details, 1 = Interactive Stroke Writing Pad
    var selectedSubTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Bar inside Detail View
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NihongoOutlinedButton(
                text = language.selectText("তালিকায় ফিরুন", "Back to Grid"),
                onClick = onCloseDetail,
                leadingIcon = Icons.AutoMirrored.Filled.ArrowBack
            )

            LessonCompletionBadge(
                isCompleted = isLearned,
                language = language
            )
        }

        // Character Hero Card
        NihongoCard(isSelected = true) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.rowGroup,
                        style = MaterialTheme.typography.labelMedium,
                        color = NihongoTheme.colors.inkGreen
                    )
                    Text(
                        text = item.character,
                        style = MaterialTheme.typography.displayLarge,
                        color = NihongoTheme.colors.primaryText
                    )
                    Text(
                        text = "Romaji: ${item.romaji}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = NihongoTheme.colors.phoenixFlowerRed
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    AudioSpeakerButton(
                        japaneseText = item.character,
                        onPlayNormal = { onPlayJapaneseAudio(item.character, false) },
                        onPlaySlow = { onPlayJapaneseAudio(item.character, true) },
                        sourceType = AudioSourceType.SYNTHETIC_TTS,
                        showSourceBadge = true,
                        language = language
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (item.strokeCount > 0) {
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = NihongoTheme.colors.elevatedSurface
                        ) {
                            Text(
                                text = language.selectText(
                                    "স্ট্রোক: ${item.strokeCount}",
                                    "Strokes: ${item.strokeCount}"
                                ),
                                style = MaterialTheme.typography.labelMedium,
                                color = NihongoTheme.colors.primaryText,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Sub-Tab Switcher: Pronunciation & Examples vs Writing Practice
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FilterChip(
                selected = selectedSubTab == 0,
                onClick = { selectedSubTab = 0 },
                label = {
                    Text(text = language.selectText("উচ্চারণ ও উদাহরণ", "Pronunciation & Word"))
                },
                leadingIcon = {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = NihongoTheme.colors.selectedContainer,
                    selectedLabelColor = NihongoTheme.colors.inkGreen
                ),
                modifier = Modifier.weight(1f)
            )

            FilterChip(
                selected = selectedSubTab == 1,
                onClick = { selectedSubTab = 1 },
                label = {
                    Text(text = language.selectText("লেখার অনুশীলন", "Writing Practice"))
                },
                leadingIcon = {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Default.Brush,
                        contentDescription = null
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = NihongoTheme.colors.selectedContainer,
                    selectedLabelColor = NihongoTheme.colors.inkGreen
                ),
                modifier = Modifier.weight(1f)
            )
        }

        if (selectedSubTab == 0) {
            // Bengali & English Pronunciation Guidance
            NihongoCard {
                Text(
                    text = language.selectText("বাংলা উচ্চারণ নির্দেশিকা", "Bengali Pronunciation Guide"),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = NihongoTheme.colors.inkGreen
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.pronunciationBn,
                    style = MaterialTheme.typography.bodyLarge,
                    color = NihongoTheme.colors.primaryText
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = NihongoTheme.colors.borderOutline.copy(alpha = 0.4f)
                )

                Text(
                    text = language.selectText("ইংরেজি ধ্বনিগত ব্যাখ্যা", "English Phonetic Explanation"),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = NihongoTheme.colors.inkGreen
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.explanationEn,
                    style = MaterialTheme.typography.bodyMedium,
                    color = NihongoTheme.colors.secondaryText
                )
            }

            // Example Word with Audio
            NihongoCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = language.selectText("উদাহরণ শব্দ (Example Word)", "Example Vocabulary"),
                            style = MaterialTheme.typography.labelMedium,
                            color = NihongoTheme.colors.secondaryText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.exampleWordJp,
                            style = MaterialTheme.typography.headlineMedium,
                            color = NihongoTheme.colors.primaryText
                        )
                        Text(
                            text = item.exampleWordRomaji,
                            style = MaterialTheme.typography.titleSmall,
                            color = NihongoTheme.colors.inkGreen
                        )
                    }
                    AudioSpeakerButton(
                        japaneseText = item.exampleWordJp,
                        onPlayNormal = { onPlayJapaneseAudio(item.exampleWordJp, false) },
                        onPlaySlow = { onPlayJapaneseAudio(item.exampleWordJp, true) },
                        language = language
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                BilingualMeaningBlock(
                    bengaliMeaning = item.exampleMeaningBn,
                    englishMeaning = item.exampleMeaningEn,
                    primaryLanguage = language
                )
            }

            // Verified Stroke Order Summary
            NihongoCard {
                Text(
                    text = language.selectText("স্ট্রোকের ক্রম (Stroke Order)", "Verified Stroke Order"),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = NihongoTheme.colors.primaryText
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = language.selectText(item.strokeDirectionsBn, item.strokeDirectionsEn),
                    style = MaterialTheme.typography.bodyMedium,
                    color = NihongoTheme.colors.secondaryText
                )
                Spacer(modifier = Modifier.height(10.dp))
                NihongoPrimaryButton(
                    text = language.selectText("ক্যানভাসে লিখে অনুশীলন করুন", "Practice Writing on Canvas"),
                    onClick = { selectedSubTab = 1 },
                    leadingIcon = Icons.Default.Brush,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            // Interactive Calligraphy & Stroke Order Writing Canvas
            StrokeOrderCanvas(
                character = item.character,
                verifiedStrokeCount = item.strokeCount,
                strokeDirectionsBn = item.strokeDirectionsBn,
                strokeDirectionsEn = item.strokeDirectionsEn,
                language = language
            )
        }

        // Mark as Learned + Previous / Next Character Controls
        NihongoPrimaryButton(
            text = if (isLearned) {
                language.selectText("শেখা সম্পন্ন হয়েছে ✓", "Marked as Learned ✓")
            } else {
                language.selectText("শেখা সম্পন্ন হিসেবে চিহ্নিত করুন", "Mark Character as Learned")
            },
            onClick = { onToggleLearned(item.id) },
            leadingIcon = Icons.Default.CheckCircle,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NihongoOutlinedButton(
                text = language.selectText("পূর্ববর্তী বর্ণ", "Previous"),
                onClick = onPreviousCharacter,
                enabled = hasPrevious,
                leadingIcon = Icons.AutoMirrored.Filled.ArrowBack,
                modifier = Modifier.weight(1f)
            )
            NihongoOutlinedButton(
                text = language.selectText("পরবর্তী বর্ণ", "Next"),
                onClick = onNextCharacter,
                enabled = hasNext,
                leadingIcon = Icons.AutoMirrored.Filled.ArrowForward,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
