package com.nihongo.app.ui.screens.search

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.nihongo.app.data.local.datastore.UserPreferences
import com.nihongo.app.domain.model.AppLanguage
import com.nihongo.app.domain.model.JlptLevel
import com.nihongo.app.domain.model.KanaCatalog
import com.nihongo.app.domain.model.LearningCatalog
import com.nihongo.app.ui.components.AudioSpeakerButton
import com.nihongo.app.ui.components.BilingualMeaningBlock
import com.nihongo.app.ui.components.JlptBadge
import com.nihongo.app.ui.components.NihongoCard
import com.nihongo.app.ui.components.NihongoSearchBar
import com.nihongo.app.ui.theme.NihongoTheme

enum class SearchContentType(val labelBn: String, val labelEn: String) {
    ALL("সব ধরনের", "All"),
    VOCABULARY("শব্দভাণ্ডার", "Vocabulary"),
    KANJI("কাঞ্জি", "Kanji"),
    GRAMMAR("ব্যাকরণ", "Grammar"),
    KANA("কানা বর্ণ", "Kana");

    fun label(language: AppLanguage): String = language.selectText(labelBn, labelEn)
}

private data class GlobalSearchResult(
    val id: String,
    val contentType: SearchContentType,
    val japaneseHeader: String,
    val readingAndRomaji: String,
    val meaningBn: String,
    val meaningEn: String,
    val jlptLevel: JlptLevel?
)

/**
 * Global Multi-Script Search System (Section 17).
 * Searches Japanese text, Hiragana, Katakana, Kanji, Romaji, Bengali meanings, and English meanings.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlobalSearchScreen(
    userPreferences: UserPreferences,
    onNavigateBack: () -> Unit,
    onPlayJapaneseAudio: (String, Boolean) -> Unit
) {
    val language = userPreferences.appLanguage
    var query by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(SearchContentType.ALL) }
    var selectedLevel by remember { mutableStateOf<JlptLevel?>(null) }

    val allSearchableItems = remember {
        val vocabResults = LearningCatalog.vocabularyList.map { v ->
            GlobalSearchResult(
                id = "s_voc_${v.id}",
                contentType = SearchContentType.VOCABULARY,
                japaneseHeader = "${v.wordJp} (${v.hiraganaReading})",
                readingAndRomaji = v.romaji,
                meaningBn = v.meaningBn,
                meaningEn = v.meaningEn,
                jlptLevel = v.jlptLevel
            )
        }
        val kanjiResults = LearningCatalog.kanjiList.map { k ->
            GlobalSearchResult(
                id = "s_kan_${k.id}",
                contentType = SearchContentType.KANJI,
                japaneseHeader = k.character,
                readingAndRomaji = "${k.onyomiKana} / ${k.kunyomiKana} (${k.romajiReadings})",
                meaningBn = k.meaningBn,
                meaningEn = k.meaningEn,
                jlptLevel = k.jlptLevel
            )
        }
        val grammarResults = LearningCatalog.grammarList.map { g ->
            GlobalSearchResult(
                id = "s_grm_${g.id}",
                contentType = SearchContentType.GRAMMAR,
                japaneseHeader = g.pattern,
                readingAndRomaji = g.formationRulesEn,
                meaningBn = g.titleBn,
                meaningEn = g.titleEn,
                jlptLevel = g.jlptLevel
            )
        }
        val kanaResults = (KanaCatalog.hiraganaList + KanaCatalog.katakanaList).map { kana ->
            GlobalSearchResult(
                id = "s_kna_${kana.id}",
                contentType = SearchContentType.KANA,
                japaneseHeader = kana.character,
                readingAndRomaji = "${kana.romaji} • ${kana.exampleWordJp} (${kana.exampleWordRomaji})",
                meaningBn = "${kana.pronunciationBn} — ${kana.exampleMeaningBn}",
                meaningEn = "${kana.explanationEn} — ${kana.exampleMeaningEn}",
                jlptLevel = JlptLevel.N5
            )
        }
        vocabResults + kanjiResults + grammarResults + kanaResults
    }

    val filteredResults = remember(query, selectedType, selectedLevel) {
        val q = query.trim().lowercase()
        allSearchableItems.filter { item ->
            val matchesType = selectedType == SearchContentType.ALL || item.contentType == selectedType
            val matchesLevel = selectedLevel == null || item.jlptLevel == selectedLevel
            val matchesQuery = q.isEmpty() ||
                item.japaneseHeader.lowercase().contains(q) ||
                item.readingAndRomaji.lowercase().contains(q) ||
                item.meaningBn.lowercase().contains(q) ||
                item.meaningEn.lowercase().contains(q)
            matchesType && matchesLevel && matchesQuery
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = language.selectText("গ্লোবাল সার্চ (Global Search)", "Global Multi-Script Search"),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = NihongoTheme.colors.primaryText
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = language.selectText("ফিরে যান", "Back"),
                        tint = NihongoTheme.colors.ancientWoodBrown
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            NihongoSearchBar(
                query = query,
                onQueryChange = { query = it },
                language = language
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Content Type Filters
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SearchContentType.entries.forEach { type ->
                    FilterChip(
                        selected = selectedType == type,
                        onClick = { selectedType = type },
                        label = { Text(type.label(language)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NihongoTheme.colors.selectedContainer,
                            selectedLabelColor = NihongoTheme.colors.inkGreen
                        )
                    )
                }
            }

            // JLPT Filters
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedLevel == null,
                    onClick = { selectedLevel = null },
                    label = { Text(language.selectText("সব লেভেল", "All JLPT")) }
                )
                JlptLevel.entries.forEach { level ->
                    FilterChip(
                        selected = selectedLevel == level,
                        onClick = { selectedLevel = if (selectedLevel == level) null else level },
                        label = { Text(level.label) }
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = filteredResults,
                key = { it.id }
            ) { res ->
                NihongoCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    shape = MaterialTheme.shapes.extraSmall,
                                    color = NihongoTheme.colors.selectedContainer
                                ) {
                                    Text(
                                        text = res.contentType.label(language),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = NihongoTheme.colors.inkGreen,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                                if (res.jlptLevel != null) {
                                    JlptBadge(level = res.jlptLevel)
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = res.japaneseHeader,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = NihongoTheme.colors.primaryText
                            )
                            Text(
                                text = res.readingAndRomaji,
                                style = MaterialTheme.typography.bodySmall,
                                color = NihongoTheme.colors.inkGreen
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            BilingualMeaningBlock(
                                bengaliMeaning = res.meaningBn,
                                englishMeaning = res.meaningEn,
                                primaryLanguage = language
                            )
                        }

                        AudioSpeakerButton(
                            japaneseText = res.japaneseHeader.substringBefore(" ("),
                            onPlayNormal = {
                                onPlayJapaneseAudio(res.japaneseHeader.substringBefore(" ("), false)
                            },
                            language = language
                        )
                    }
                }
            }
        }
    }
}
