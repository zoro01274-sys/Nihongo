package com.nihongo.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nihongo.app.audio.AudioPlaybackState
import com.nihongo.app.audio.PlaybackSpeed
import com.nihongo.app.audio.TtsEngineStatus
import com.nihongo.app.domain.model.AppLanguage
import com.nihongo.app.ui.theme.NihongoTheme

/**
 * Reusable Japanese Audio Control & Voice Status Component (Section 10).
 * Provides:
 * - Speed controls (0.6x Slow Study, 0.75x, 1.0x, 1.25x)
 * - Repeat mode toggle & Replay last spoken Japanese text
 * - Stop playback control
 * - Explicit Synthetic TTS (ja-JP) vs Native Audio badge
 * - One-tap system voice installer banner if Japanese voice data is missing
 */
@Composable
fun AudioControlBar(
    audioState: AudioPlaybackState,
    language: AppLanguage,
    onSelectSpeed: (Float) -> Unit,
    onToggleRepeat: () -> Unit,
    onReplayLast: () -> Unit,
    onStopAudio: () -> Unit,
    onInstallTtsData: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isMissingVoice = audioState.engineStatus == TtsEngineStatus.MISSING_VOICE_DATA ||
        audioState.engineStatus == TtsEngineStatus.LANGUAGE_NOT_SUPPORTED

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (isMissingVoice) {
            NihongoCard(isHighlighted = true) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.WarningAmber,
                            contentDescription = null,
                            tint = NihongoTheme.colors.phoenixFlowerRed
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = language.selectText(
                                    "জাপানি (ja-JP) ভয়েস ডেটা প্রয়োজন",
                                    "Japanese (ja-JP) Voice Required"
                                ),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = NihongoTheme.colors.phoenixFlowerRed
                            )
                            Text(
                                text = audioState.engineStatus.description(language),
                                style = MaterialTheme.typography.bodySmall,
                                color = NihongoTheme.colors.primaryText
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    NihongoAccentButton(
                        text = language.selectText("ইনস্টল", "Install"),
                        onClick = onInstallTtsData,
                        leadingIcon = Icons.Default.Download
                    )
                }
            }
        }

        NihongoCard(isSelected = audioState.isPlaying) {
            // Top Row: Active status + Source Type Badge (Synthetic TTS vs Native Audio)
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
                        imageVector = Icons.Default.GraphicEq,
                        contentDescription = null,
                        tint = if (audioState.isPlaying) {
                            NihongoTheme.colors.phoenixFlowerRed
                        } else {
                            NihongoTheme.colors.inkGreen
                        },
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (audioState.isPlaying && audioState.activeJapaneseText.isNotBlank()) {
                            language.selectText(
                                "চলছে: ${audioState.activeJapaneseText}",
                                "Playing: ${audioState.activeJapaneseText}"
                            )
                        } else if (audioState.lastSpokenJapaneseText.isNotBlank()) {
                            language.selectText(
                                "সর্বশেষ উচ্চারণ: ${audioState.lastSpokenJapaneseText}",
                                "Last spoken: ${audioState.lastSpokenJapaneseText}"
                            )
                        } else {
                            audioState.engineStatus.description(language)
                        },
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = NihongoTheme.colors.primaryText,
                        maxLines = 1
                    )
                }

                Surface(
                    shape = MaterialTheme.shapes.extraSmall,
                    color = NihongoTheme.colors.elevatedSurface,
                    border = BorderStroke(1.dp, NihongoTheme.colors.borderOutline)
                ) {
                    Text(
                        text = audioState.activeSourceType.label(language),
                        style = MaterialTheme.typography.labelSmall,
                        color = NihongoTheme.colors.inkGreen,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Speed Presets (0.6x Slow, 0.75x, 1.0x, 1.25x) + Repeat & Replay Controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PlaybackSpeed.entries.forEach { preset ->
                    val isSelected = kotlin.math.abs(audioState.playbackSpeed - preset.multiplier) < 0.04f
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectSpeed(preset.multiplier) },
                        label = {
                            Text(
                                text = preset.label,
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NihongoTheme.colors.selectedContainer,
                            selectedLabelColor = NihongoTheme.colors.inkGreen
                        )
                    )
                }

                FilterChip(
                    selected = audioState.isRepeatEnabled,
                    onClick = onToggleRepeat,
                    label = {
                        Text(
                            text = language.selectText("রিপিট", "Repeat"),
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Repeat,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NihongoTheme.colors.highlightContainer,
                        selectedLabelColor = NihongoTheme.colors.phoenixFlowerRed
                    )
                )

                IconButton(
                    onClick = onReplayLast,
                    enabled = audioState.lastSpokenJapaneseText.isNotBlank()
                ) {
                    Icon(
                        imageVector = Icons.Default.Replay,
                        contentDescription = language.selectText("পুনরায় শুনুন", "Replay last"),
                        tint = NihongoTheme.colors.inkGreen
                    )
                }

                if (audioState.isPlaying) {
                    IconButton(onClick = onStopAudio) {
                        Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = language.selectText("থামান", "Stop audio"),
                            tint = NihongoTheme.colors.phoenixFlowerRed
                        )
                    }
                }
            }
        }
    }
}
