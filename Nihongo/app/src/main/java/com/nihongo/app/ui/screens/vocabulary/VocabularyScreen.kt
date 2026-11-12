package com.nihongo.app.ui.screens.vocabulary

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import com.nihongo.app.audio.AudioSourceType
import com.nihongo.app.data.local.datastore.UserPreferences
import com.nihongo.app.domain.model.JlptLevel
import com.nihongo.app.domain.model.LearningCatalog
import com.nihongo.app.domain.model.VocabCategory
import com.nihongo.app.ui.components.AudioSpeakerButton
import com.nihongo.app.ui.components.BilingualMeaningBlock
import com.nihongo.app.ui.components.JlptBadge
import com.nihongo.app.ui.components.NihongoCard
import com.nihongo.app.ui.components.NihongoSearchBar
import com.nihongo.app.ui.theme.NihongoTheme

/**
 * Complete Vocabulary Learning Module (Section 08) persisted via Room SQLite.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabularyScreen(
    userPreferences: UserPreferences,
    learnedItemIds: Set<String>,
    favoriteItemIds: Set<String>,
    onToggleLearned: (String, JlptLevel) -> Unit,
    onToggleFavorite: (String, JlptLevel) -> Unit,
    onNavigateBack: () -> Unit,
    onPlayJapaneseAudio: (String, Boolean) -> Unit
) {
    val language = userPreferences.appLanguage
    var searchQuery by remember { mutableStateOf("") }
    var selectedLevel by remember { mutableStateOf<JlptLevel?>(null) }
    var selectedCategory by remember { mutableStateOf<VocabCategory?>(null) }
    var showFavoritesOnly by remember { mutableStateOf(false) }

    val filteredVocabulary = remember(
        searchQuery,
        selectedLevel,
        selectedCategory,
        showFavoritesOnly,
        favoriteItemIds
    ) {
        LearningCatalog.vocabularyList.filter { item ->
            val matchesLevel = selectedLevel == null || item.jlptLevel == selectedLevel
            val matchesCategory = selectedCategory == null || item.category == selectedCategory
            val matchesFav = !showFavoritesOnly || favoriteItemIds.contains(item.id)
            val q = searchQuery.trim().lowercase()
            val matchesSearch = q.isEmpty() ||
                item.wordJp.contains(q) ||
                item.hiraganaReading.contains(q) ||
                item.kanjiSpelling.contains(q) ||
                item.romaji.lowercase().contains(q) ||
                item.meaningBn.lowercase().contains(q) ||
                item.meaningEn.lowercase().contains(q)
            matchesLevel && matchesCategory && matchesFav && matchesSearch
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
                    text = language.selectText("শব্দভাণ্ডার (Vocabulary N5–N1)", "Vocabulary (N5–N1)"),
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
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                language = language
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedLevel == null && !showFavoritesOnly,
                    onClick = {
                        selectedLevel = null
                        showFavoritesOnly = false
                    },
                    label = { Text(language.selectText("সব JLPT", "All JLPT")) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NihongoTheme.colors.selectedContainer,
                        selectedLabelColor = NihongoTheme.colors.inkGreen
                    )
                )
                JlptLevel.entries.forEach { level ->
                    FilterChip(
                        selected = selectedLevel == level,
                        onClick = { selectedLevel = if (selectedLevel == level) null else level },
                        label = { Text(level.label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NihongoTheme.colors.selectedContainer,
                            selectedLabelColor = NihongoTheme.colors.inkGreen
                        )
                    )
                }
                FilterChip(
                    selected = showFavoritesOnly,
                    onClick = { showFavoritesOnly = !showFavoritesOnly },
                    label = { Text(language.selectText("পছন্দের শব্দ", "Favorites")) }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedCategory == null,
                    onClick = { selectedCategory = null },
                    label = { Text(language.selectText("সকল ক্যাটাগরি (১৭)", "All 17 Categories")) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NihongoTheme.colors.selectedContainer,
                        selectedLabelColor = NihongoTheme.colors.inkGreen
                    )
                )
                VocabCategory.entries.forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = {
                            selectedCategory = if (selectedCategory == category) null else category
                        },
                        label = { Text(category.label(language)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NihongoTheme.colors.selectedContainer,
                            selectedLabelColor = NihongoTheme.colors.inkGreen
                        )
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(
                items = filteredVocabulary,
                key = { it.id }
            ) { item ->
                val isFav = favoriteItemIds.contains(item.id)
                val isLearned = learnedItemIds.contains(item.id)

                NihongoCard(isSelected = isLearned) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            JlptBadge(level = item.jlptLevel)
                            Surface(
                                shape = MaterialTheme.shapes.small,
                                color = NihongoTheme.colors.elevatedSurface
                            ) {
                                Text(
                                    text = "${item.category.label(language)} • ${
                                        language.selectText(item.partOfSpeechBn, item.partOfSpeechEn)
                                    }",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = NihongoTheme.colors.secondaryText,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { onToggleFavorite(item.id, item.jlptLevel) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = null,
                                    tint = if (isFav) NihongoTheme.colors.phoenixFlowerRed else NihongoTheme.colors.secondaryText
                                )
                            }
                            IconButton(
                                onClick = { onToggleLearned(item.id, item.jlptLevel) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = if (isLearned) NihongoTheme.colors.inkGreen else NihongoTheme.colors.secondaryText.copy(alpha = 0.45f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.wordJp,
                                style = MaterialTheme.typography.headlineMedium,
                                color = NihongoTheme.colors.primaryText
                            )
                            Text(
                                text = "Hiragana: ${item.hiraganaReading}  |  Kanji: ${item.kanjiSpelling}",
                                style = MaterialTheme.typography.bodySmall,
                                color = NihongoTheme.colors.secondaryText
                            )
                            Text(
                                text = "Romaji: ${item.romaji}",
                                style = MaterialTheme.typography.titleSmall,
                                color = NihongoTheme.colors.inkGreen
                            )
                        }

                        AudioSpeakerButton(
                            japaneseText = item.wordJp,
                            onPlayNormal = { onPlayJapaneseAudio(item.wordJp, false) },
                            onPlaySlow = { onPlayJapaneseAudio(item.wordJp, true) },
                            sourceType = if (item.hasNativeAudio) {
                                AudioSourceType.NATIVE_RECORDING
                            } else {
                                AudioSourceType.SYNTHETIC_TTS
                            },
                            showSourceBadge = true,
                            language = language
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    BilingualMeaningBlock(
                        bengaliMeaning = item.meaningBn,
                        englishMeaning = item.meaningEn,
                        primaryLanguage = language
                    )

                    Spacer(modifier = Modifier.height(12.dp))

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
                                        text = item.exampleSentenceJp,
                                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                                        color = NihongoTheme.colors.primaryText
                                    )
                                    Text(
                                        text = item.exampleSentenceReading,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = NihongoTheme.colors.inkGreen
                                    )
                                    Text(
                                        text = item.exampleSentenceRomaji,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = NihongoTheme.colors.secondaryText
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                AudioSpeakerButton(
                                    japaneseText = item.exampleSentenceJp,
                                    onPlayNormal = { onPlayJapaneseAudio(item.exampleSentenceJp, false) },
                                    language = language
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "বাংলা: ${item.exampleSentenceBn}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = NihongoTheme.colors.primaryText
                            )
                            Text(
                                text = "EN: ${item.exampleSentenceEn}",
                                style = MaterialTheme.typography.bodySmall,
                                color = NihongoTheme.colors.secondaryText
                            )
                        }
                    }
                }
            }
        }
    }
}
