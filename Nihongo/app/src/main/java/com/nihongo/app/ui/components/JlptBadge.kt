package com.nihongo.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nihongo.app.domain.model.AppLanguage
import com.nihongo.app.domain.model.JlptLevel
import com.nihongo.app.ui.theme.NihongoTheme

/**
 * Displays a compact JLPT Level indicator (N5 to N1) using the official NIHONGO palette.
 */
@Composable
fun JlptBadge(
    level: JlptLevel,
    modifier: Modifier = Modifier,
    isHighlighted: Boolean = false
) {
    val backgroundColor = if (isHighlighted) {
        NihongoTheme.colors.phoenixFlowerRed
    } else {
        NihongoTheme.colors.inkGreen
    }

    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = backgroundColor,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Text(
            text = "JLPT ${level.label}",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

/**
 * Displays whether a lesson, Kanji, or Vocabulary item has been mastered.
 */
@Composable
fun LessonCompletionBadge(
    isCompleted: Boolean,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val label = if (isCompleted) {
        language.selectText("সম্পন্ন", "Learned")
    } else {
        language.selectText("অসম্পন্ন", "In Progress")
    }

    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = if (isCompleted) {
            NihongoTheme.colors.selectedContainer
        } else {
            NihongoTheme.colors.elevatedSurface
        },
        border = BorderStroke(
            width = 1.dp,
            color = if (isCompleted) NihongoTheme.colors.inkGreen else NihongoTheme.colors.borderOutline
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isCompleted) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = NihongoTheme.colors.inkGreen,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = if (isCompleted) NihongoTheme.colors.inkGreen else NihongoTheme.colors.secondaryText
            )
        }
    }
}
