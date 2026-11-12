package com.nihongo.app.ui.screens.learn

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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nihongo.app.data.local.datastore.UserPreferences
import com.nihongo.app.domain.model.JlptLevel
import com.nihongo.app.ui.components.NihongoCard
import com.nihongo.app.ui.theme.NihongoTheme

private data class LearnModuleItem(
    val titleBn: String,
    val titleEn: String,
    val subtitleBn: String,
    val subtitleEn: String,
    val glyph: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnScreen(
    userPreferences: UserPreferences,
    onNavigateToHiragana: () -> Unit,
    onNavigateToKatakana: () -> Unit,
    onNavigateToKanji: () -> Unit,
    onNavigateToVocabulary: () -> Unit,
    onNavigateToGrammar: () -> Unit,
    onNavigateToJlptCourse: (JlptLevel) -> Unit,
    onNavigateToGlobalSearch: () -> Unit
) {
    val language = userPreferences.appLanguage

    val modules = listOf(
        LearnModuleItem(
            titleBn = "হিরাগানা (Hiragana)",
            titleEn = "Hiragana",
            subtitleBn = "৪৬টি মৌলিক বর্ণ, ডাকুতেন, হান্দাকুতেন ও ইয়োন",
            subtitleEn = "46 Basic characters, Dakuten, Handakuten & Yoon",
            glyph = "あ",
            icon = Icons.Default.FontDownload,
            onClick = onNavigateToHiragana
        ),
        LearnModuleItem(
            titleBn = "কাতাকানা (Katakana)",
            titleEn = "Katakana",
            subtitleBn = "বিদেশি শব্দ, দেশ ও নামের জন্য ব্যবহৃত বর্ণমালা",
            subtitleEn = "Loanwords, foreign countries, and sound effects",
            glyph = "ア",
            icon = Icons.Default.Translate,
            onClick = onNavigateToKatakana
        ),
        LearnModuleItem(
            titleBn = "কাঞ্জি শিক্ষণ (Kanji N5–N1)",
            titleEn = "Kanji Master (N5–N1)",
            subtitleBn = "অন'ইওমি, কুন'ইওমি, স্ট্রোক অর্ডার ও অর্থ",
            subtitleEn = "On'yomi, Kun'yomi, Stroke Orders & Meanings",
            glyph = "漢",
            icon = Icons.Default.Brush,
            onClick = onNavigateToKanji
        ),
        LearnModuleItem(
            titleBn = "শব্দভাণ্ডার (Vocabulary)",
            titleEn = "Vocabulary Hub",
            subtitleBn = "১৭টি ব্যবহারিক ক্যাটাগরি ও JLPT শব্দ তালিকা",
            subtitleEn = "17 Categorized themes & JLPT level wordlists",
            glyph = "語",
            icon = Icons.Default.Bookmark,
            onClick = onNavigateToVocabulary
        ),
        LearnModuleItem(
            titleBn = "জাপানি ব্যাকরণ (Grammar)",
            titleEn = "Japanese Grammar",
            subtitleBn = "বাক্য গঠনের নিয়ম, কণা (Particles) ও সাধারণ ভুল",
            subtitleEn = "Sentence structures, particles, rules & nuances",
            glyph = "文",
            icon = Icons.Default.MenuBook,
            onClick = onNavigateToGrammar
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = language.selectText("শিখুন • লার্নিং হাব", "Learn • Learning Hub"),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = NihongoTheme.colors.primaryText
                )
            },
            actions = {
                IconButton(onClick = onNavigateToGlobalSearch) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = language.selectText("অনুসন্ধান", "Search"),
                        tint = NihongoTheme.colors.inkGreen
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Column {
                    Text(
                        text = language.selectText("JLPT কোর্স পাথ (N5 থেকে N1)", "JLPT Course Paths (N5 to N1)"),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = NihongoTheme.colors.primaryText
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        JlptLevel.entries.forEach { level ->
                            val isCurrentLevel = level == userPreferences.currentJlptLevel
                            NihongoCard(
                                modifier = Modifier.weight(1f),
                                isSelected = isCurrentLevel,
                                contentPadding = 10.dp,
                                onClick = { onNavigateToJlptCourse(level) }
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = level.label,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = if (isCurrentLevel) NihongoTheme.colors.inkGreen else NihongoTheme.colors.primaryText
                                    )
                                    Text(
                                        text = "${level.targetKanjiCount} 漢",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = NihongoTheme.colors.secondaryText
                                    )
                                }
                            }
                        }
                    }
                }
            }

            items(modules) { module ->
                NihongoCard(onClick = module.onClick) {
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
                                shape = MaterialTheme.shapes.small,
                                color = NihongoTheme.colors.selectedContainer,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = module.glyph,
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                        color = NihongoTheme.colors.inkGreen
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = language.selectText(module.titleBn, module.titleEn),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = NihongoTheme.colors.primaryText
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = language.selectText(module.subtitleBn, module.subtitleEn),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = NihongoTheme.colors.secondaryText
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = NihongoTheme.colors.inkGreen
                        )
                    }
                }
            }
        }
    }
}
