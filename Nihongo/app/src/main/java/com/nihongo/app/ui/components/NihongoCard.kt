package com.nihongo.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nihongo.app.ui.theme.NihongoTheme

/**
 * Reusable Japanese stationery-inspired card component.
 * Supports default, selected (Ink Green border/tint), and alert/highlight (Phoenix Flower Red) states.
 */
@Composable
fun NihongoCard(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    isHighlighted: Boolean = false,
    contentPadding: Dp = 16.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val containerColor = when {
        isSelected -> NihongoTheme.colors.selectedContainer
        isHighlighted -> NihongoTheme.colors.highlightContainer
        else -> NihongoTheme.colors.cardSurface
    }

    val borderColor = when {
        isSelected -> NihongoTheme.colors.inkGreen
        isHighlighted -> NihongoTheme.colors.phoenixFlowerRed
        else -> NihongoTheme.colors.borderOutline.copy(alpha = 0.55f)
    }

    val borderWidth = if (isSelected || isHighlighted) 1.5.dp else 1.dp

    val clickableModifier = if (onClick != null) {
        Modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
    } else {
        Modifier
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(clickableModifier),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = NihongoTheme.colors.primaryText
        ),
        border = BorderStroke(width = borderWidth, color = borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(contentPadding),
            content = content
        )
    }
}
