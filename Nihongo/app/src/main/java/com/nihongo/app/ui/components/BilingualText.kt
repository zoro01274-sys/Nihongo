package com.nihongo.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nihongo.app.domain.model.AppLanguage
import com.nihongo.app.ui.theme.NihongoTheme

/**
 * Renders both Bengali and English meanings clearly so Bengali-speaking and
 * English-speaking learners receive full bilingual context, prioritizing the user's selected language.
 */
@Composable
fun BilingualMeaningBlock(
    bengaliMeaning: String,
    englishMeaning: String,
    primaryLanguage: AppLanguage,
    modifier: Modifier = Modifier,
    showBothLanguages: Boolean = true
) {
    val firstLine = if (primaryLanguage == AppLanguage.BENGALI) bengaliMeaning else englishMeaning
    val secondLine = if (primaryLanguage == AppLanguage.BENGALI) englishMeaning else bengaliMeaning

    Column(modifier = modifier) {
        Text(
            text = firstLine,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = NihongoTheme.colors.primaryText
        )
        if (showBothLanguages && secondLine.isNotBlank() && secondLine != firstLine) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = secondLine,
                style = MaterialTheme.typography.bodyMedium,
                color = NihongoTheme.colors.secondaryText
            )
        }
    }
}
