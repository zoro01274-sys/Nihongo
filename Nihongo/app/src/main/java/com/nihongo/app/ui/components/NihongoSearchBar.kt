package com.nihongo.app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nihongo.app.domain.model.AppLanguage
import com.nihongo.app.ui.theme.NihongoTheme

/**
 * Reusable search field supporting Japanese (Kanji/Hiragana/Katakana), Romaji, Bengali, and English queries.
 */
@Composable
fun NihongoSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    language: AppLanguage,
    modifier: Modifier = Modifier,
    placeholderBn: String = "জাপানি, রোমাজি, বাংলা বা ইংরেজি দিয়ে খুঁজুন...",
    placeholderEn: String = "Search Japanese, Romaji, Bengali, or English..."
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        shape = MaterialTheme.shapes.medium,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = NihongoTheme.colors.primaryText
        ),
        placeholder = {
            Text(
                text = language.selectText(placeholderBn, placeholderEn),
                style = MaterialTheme.typography.bodyMedium,
                color = NihongoTheme.colors.secondaryText
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = NihongoTheme.colors.inkGreen
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = language.selectText("মুছে ফেলুন", "Clear search"),
                        tint = NihongoTheme.colors.secondaryText
                    )
                }
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = NihongoTheme.colors.elevatedSurface,
            unfocusedContainerColor = NihongoTheme.colors.elevatedSurface,
            focusedBorderColor = NihongoTheme.colors.inkGreen,
            unfocusedBorderColor = NihongoTheme.colors.borderOutline,
            cursorColor = NihongoTheme.colors.inkGreen
        )
    )
}
