package com.nihongo.app.ui.screens.kanji

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nihongo.app.audio.AudioSourceType
import com.nihongo.app.data.local.datastore.UserPreferences
import com.nihongo.app.domain.model.AppLanguage
import com.nihongo.app.domain.model.JlptLevel
import com.nihongo.app.domain.model.KanjiItem
import com.nihongo.app.domain.model.LearningCatalog
import com.nihongo.app.ui.components.AudioSpeakerButton
import com.nihongo.app.ui.components.BilingualMeaningBlock
import com.nihongo.app.ui.components.JlptBadge
import com.nihongo.app.ui.components.LessonCompletionBadge
import com.nihongo.app.ui.components.NihongoCard
import com.nihongo.app.ui.components.NihongoOutlinedButton
import com.nihongo.app.ui.components.NihongoPrimaryButton
import com.nihongo.app.ui.components.NihongoSearchBar
import com.nihongo.app.ui.components.StrokeOrderCanvas
import com.nihongo.app.ui.theme.NihongoTheme

/**
 * Structured Kanji Learning System from N5 to N1 persisted in Room SQLite.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KanjiScreen(
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
    var showFavoritesOnly by remember { mutableStateOf(false) }
    var selectedKanji by remember { mutableStateOf<KanjiItem?>(null) }

    val filteredKanji = remember(searchQuery, selectedLevel, showFavoritesOnly, favoriteItemIds) {
        LearningCatalog.kanjiList.filter { item ->
            val matchesLevel = selectedLevel == null || item.jlptLevel == selectedLevel
            val matchesFav = !showFavoritesOnly || favoriteItemIds.contains(item.id)
            val q = searchQuery.trim().lowercase()
            val matchesQuery = q.isEmpty() ||
                item.character.contains(q) ||
                item.meaningBn.lowercase().contains(q) ||
                item.meaningEn.lowercase().contains(q) ||
                item.romajiReadings.lowercase().contains(q) ||
                item.onyomiKana.contains(q) ||
                item.kunyomiKana.contains(q)
            matchesLevel && matchesFav && matchesQuery
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
                    text = language.selectText("কাঞ্জি শিক্ষণ (N5–N1)", "Kanji Learning (N5–N1)"),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = NihongoTheme.colors.primaryText
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        if (selectedKanji != null) selectedKanji = null else onNavigateBack()
                    }
                ) {
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

        val detailItem = selectedKanji
        if (detailItem != null) {
            KanjiDetailContent(
                kanji = detailItem,
                isFavorite = favoriteItemIds.contains(detailItem.id),
                isLearned = learnedItemIds.contains(detailItem.id),
                language = language,
                onToggleFavorite = { onToggleFavorite(detailItem.id, detailItem.jlptLevel) },
                onToggleLearned = { onToggleLearned(detailItem.id, detailItem.jlptLevel) },
                onSelectRelatedKanji = { char ->
                    LearningCatalog.kanjiList.firstOrNull { it.character == char }?.let {
                        selectedKanji = it
                    }
                },
                onPlayJapaneseAudio = onPlayJapaneseAudio,
                onCloseDetail = { selectedKanji = null }
            )
        } else {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                NihongoSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    language = language,
                    placeholderBn = "কাঞ্জি, রোমাজি, বাংলা বা ইংরেজি অর্থ দিয়ে খুঁজুন...",
                    placeholderEn = "Search Kanji, reading, Bengali, or English..."
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
                        label = { Text(language.selectText("সব লেভেল", "All Levels")) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NihongoTheme.colors.selectedContainer,
                            selectedLabelColor = NihongoTheme.colors.inkGreen
                        )
                    )

                    JlptLevel.entries.forEach { level ->
                        FilterChip(
                            selected = selectedLevel == level,
                            onClick = {
                                selectedLevel = if (selectedLevel == level) null else level
                            },
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
                        label = { Text(language.selectText("পছন্দের তালিকা", "Favorites")) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = NihongoTheme.colors.phoenixFlowerRed,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = filteredKanji,
                    key = { it.id }
                ) { kanji ->
                    val isFav = favoriteItemIds.contains(kanji.id)
                    val isLearned = learnedItemIds.contains(kanji.id)

                    NihongoCard(
                        isSelected = isLearned,
                        onClick = { selectedKanji = kanji }
                    ) {
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
                                    shape = MaterialTheme.shapes.medium,
                                    color = NihongoTheme.colors.elevatedSurface,
                                    modifier = Modifier.size(64.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = kanji.character,
                                            style = MaterialTheme.typography.displayMedium,
                                            color = NihongoTheme.colors.inkGreen
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        JlptBadge(level = kanji.jlptLevel)
                                        Text(
                                            text = language.selectText(
                                                "${kanji.strokeCount} স্ট্রোক",
                                                "${kanji.strokeCount} Strokes"
                                            ),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = NihongoTheme.colors.secondaryText
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    BilingualMeaningBlock(
                                        bengaliMeaning = kanji.meaningBn,
                                        englishMeaning = kanji.meaningEn,
                                        primaryLanguage = language
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "On: ${kanji.onyomiKana}  •  Kun: ${kanji.kunyomiKana}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = NihongoTheme.colors.secondaryText
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                IconButton(
                                    onClick = { onToggleFavorite(kanji.id, kanji.jlptLevel) }
                                ) {
                                    Icon(
                                        imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = null,
                                        tint = if (isFav) NihongoTheme.colors.phoenixFlowerRed else NihongoTheme.colors.secondaryText
                                    )
                                }
                                AudioSpeakerButton(
                                    japaneseText = kanji.character,
                                    onPlayNormal = { onPlayJapaneseAudio(kanji.character, false) },
                                    language = language
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun KanjiDetailContent(
    kanji: KanjiItem,
    isFavorite: Boolean,
    isLearned: Boolean,
    language: AppLanguage,
    onToggleFavorite: () -> Unit,
    onToggleLearned: () -> Unit,
    onSelectRelatedKanji: (String) -> Unit,
    onPlayJapaneseAudio: (String, Boolean) -> Unit,
    onCloseDetail: () -> Unit
) {
    var activeTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NihongoOutlinedButton(
                text = language.selectText("কাঞ্জি তালিকায় ফিরুন", "Back to Kanji List"),
                onClick = onCloseDetail,
                leadingIcon = Icons.AutoMirrored.Filled.ArrowBack
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if (isFavorite) NihongoTheme.colors.phoenixFlowerRed else NihongoTheme.colors.secondaryText
                    )
                }
                LessonCompletionBadge(isCompleted = isLearned, language = language)
            }
        }

        NihongoCard(isSelected = true) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        JlptBadge(level = kanji.jlptLevel)
                        Text(
                            text = language.selectText(
                                "স্ট্রোক সংখ্যা: ${kanji.strokeCount}",
                                "Stroke Count: ${kanji.strokeCount}"
                            ),
                            style = MaterialTheme.typography.labelMedium,
                            color = NihongoTheme.colors.inkGreen
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = kanji.character,
                        style = MaterialTheme.typography.displayLarge,
                        color = NihongoTheme.colors.primaryText
                    )
                    BilingualMeaningBlock(
                        bengaliMeaning = kanji.meaningBn,
                        englishMeaning = kanji.meaningEn,
                        primaryLanguage = language
                    )
                }

                AudioSpeakerButton(
                    japaneseText = kanji.character,
                    onPlayNormal = { onPlayJapaneseAudio(kanji.character, false) },
                    onPlaySlow = { onPlayJapaneseAudio(kanji.character, true) },
                    sourceType = AudioSourceType.SYNTHETIC_TTS,
                    showSourceBadge = true,
                    language = language
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FilterChip(
                selected = activeTab == 0,
                onClick = { activeTab = 0 },
                label = { Text(language.selectText("পাঠ ও উদাহরণ", "Readings & Examples")) },
                leadingIcon = { Icon(Icons.Default.MenuBook, contentDescription = null) },
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = activeTab == 1,
                onClick = { activeTab = 1 },
                label = { Text(language.selectText("স্ট্রোক ও লেখা", "Stroke Writing Pad")) },
                leadingIcon = { Icon(Icons.Default.Brush, contentDescription = null) },
                modifier = Modifier.weight(1f)
            )
        }

        if (activeTab == 0) {
            NihongoCard {
                Text(
                    text = language.selectText("উচ্চারণ (Readings)", "Verified Kanji Readings"),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = NihongoTheme.colors.inkGreen
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "音読み (On'yomi): ${kanji.onyomiKana}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = NihongoTheme.colors.primaryText
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "訓読み (Kun'yomi): ${kanji.kunyomiKana}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = NihongoTheme.colors.primaryText
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Romaji: ${kanji.romajiReadings}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = NihongoTheme.colors.phoenixFlowerRed
                )
            }

            NihongoCard {
                Text(
                    text = language.selectText("উদাহরণ শব্দসমূহ (Example Vocabulary)", "Example Vocabulary"),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = NihongoTheme.colors.inkGreen
                )
                Spacer(modifier = Modifier.height(8.dp))

                kanji.exampleVocabulary.forEachIndexed { idx, ex ->
                    if (idx > 0) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = NihongoTheme.colors.borderOutline.copy(alpha = 0.4f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${ex.wordJp} (${ex.readingKana})",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = NihongoTheme.colors.primaryText
                            )
                            Text(
                                text = ex.romaji,
                                style = MaterialTheme.typography.bodySmall,
                                color = NihongoTheme.colors.inkGreen
                            )
                            Text(
                                text = "${ex.meaningBn} • ${ex.meaningEn}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = NihongoTheme.colors.secondaryText
                            )
                        }
                        AudioSpeakerButton(
                            japaneseText = ex.wordJp,
                            onPlayNormal = { onPlayJapaneseAudio(ex.wordJp, false) },
                            onPlaySlow = { onPlayJapaneseAudio(ex.wordJp, true) },
                            language = language
                        )
                    }
                }
            }

            NihongoCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = language.selectText("উদাহরণ বাক্য (Example Sentence)", "Example Sentence"),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = NihongoTheme.colors.inkGreen
                    )
                    AudioSpeakerButton(
                        japaneseText = kanji.exampleSentenceJp,
                        onPlayNormal = { onPlayJapaneseAudio(kanji.exampleSentenceJp, false) },
                        onPlaySlow = { onPlayJapaneseAudio(kanji.exampleSentenceJp, true) },
                        language = language
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = kanji.exampleSentenceJp,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = NihongoTheme.colors.primaryText
                )
                Text(
                    text = kanji.exampleSentenceReading,
                    style = MaterialTheme.typography.bodySmall,
                    color = NihongoTheme.colors.inkGreen
                )
                Text(
                    text = kanji.exampleSentenceRomaji,
                    style = MaterialTheme.typography.bodySmall,
                    color = NihongoTheme.colors.secondaryText
                )
                Spacer(modifier = Modifier.height(6.dp))
                BilingualMeaningBlock(
                    bengaliMeaning = kanji.exampleSentenceBn,
                    englishMeaning = kanji.exampleSentenceEn,
                    primaryLanguage = language
                )
            }

            if (kanji.relatedKanji.isNotEmpty()) {
                NihongoCard {
                    Text(
                        text = language.selectText("সম্পর্কিত কাঞ্জি (Related Kanji)", "Related Kanji"),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = NihongoTheme.colors.primaryText
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        kanji.relatedKanji.forEach { rel ->
                            FilterChip(
                                selected = false,
                                onClick = { onSelectRelatedKanji(rel) },
                                label = {
                                    Text(
                                        text = rel,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            )
                        }
                    }
                }
            }
        } else {
            StrokeOrderCanvas(
                character = kanji.character,
                verifiedStrokeCount = kanji.strokeCount,
                strokeDirectionsBn = kanji.strokeDirectionsBn,
                strokeDirectionsEn = kanji.strokeDirectionsEn,
                language = language
            )
        }

        NihongoPrimaryButton(
            text = if (isLearned) {
                language.selectText("কাঞ্জিটি শেখা সম্পন্ন ✓", "Kanji Marked as Learned ✓")
            } else {
                language.selectText("কাঞ্জিটি শেখা সম্পন্ন হিসেবে চিহ্নিত করুন", "Mark Kanji as Learned")
            },
            onClick = onToggleLearned,
            leadingIcon = Icons.Default.CheckCircle,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
