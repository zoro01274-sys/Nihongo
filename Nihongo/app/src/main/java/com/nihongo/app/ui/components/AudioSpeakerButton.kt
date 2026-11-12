package com.nihongo.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nihongo.app.audio.AudioSourceType
import com.nihongo.app.domain.model.AppLanguage
import com.nihongo.app.ui.theme.NihongoTheme

/**
 * Mandatory visible speaker button for Japanese learning items (Section 10).
 * Supports normal playback, slow study playback (0.6x / 0.75x), and an explicit badge
 * showing whether the audio source is Native Recording or Offline TTS (ja-JP).
 */
@Composable
fun AudioSpeakerButton(
    japaneseText: String,
    onPlayNormal: (String) -> Unit,
    modifier: Modifier = Modifier,
    onPlaySlow: ((String) -> Unit)? = null,
    isPlaying: Boolean = false,
    sourceType: AudioSourceType = AudioSourceType.SYNTHETIC_TTS,
    showSourceBadge: Boolean = false,
    language: AppLanguage = AppLanguage.BENGALI
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Primary Play Button (Ink Green when idle, Phoenix Flower Red when actively speaking)
        FilledIconButton(
            onClick = { onPlayNormal(japaneseText) },
            modifier = Modifier.size(38.dp),
            shape = MaterialTheme.shapes.small,
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = if (isPlaying) {
                    NihongoTheme.colors.phoenixFlowerRed
                } else {
                    NihongoTheme.colors.inkGreen
                },
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.GraphicEq else Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = language.selectText("জাপানি উচ্চারণ শুনুন", "Play Japanese pronunciation"),
                modifier = Modifier.size(20.dp)
            )
        }

        // Optional Slow Playback Button (0.75x / Slow Study)
        if (onPlaySlow != null) {
            OutlinedIconButton(
                onClick = { onPlaySlow(japaneseText) },
                modifier = Modifier.size(38.dp),
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(1.dp, NihongoTheme.colors.borderOutline)
            ) {
                Icon(
                    imageVector = Icons.Default.Speed,
                    contentDescription = language.selectText("ধীর গতির উচ্চারণ", "Slow playback"),
                    tint = NihongoTheme.colors.primaryText,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Transparently distinguish Synthetic TTS vs Native Recording
        if (showSourceBadge) {
            Surface(
                shape = MaterialTheme.shapes.extraSmall,
                color = NihongoTheme.colors.elevatedSurface,
                border = BorderStroke(1.dp, NihongoTheme.colors.borderOutline.copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = sourceType.label(language),
                        style = MaterialTheme.typography.labelSmall,
                        color = NihongoTheme.colors.secondaryText
                    )
                }
            }
        }
    }
}
