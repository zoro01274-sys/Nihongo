package com.nihongo.app.ui.screens.kana

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nihongo.app.data.local.datastore.UserPreferences
import com.nihongo.app.domain.model.AppLanguage
import com.nihongo.app.domain.model.KanaCatalog
import com.nihongo.app.domain.model.KanaCategory
import com.nihongo.app.domain.model.KanaItem
import com.nihongo.app.domain.model.KanaScript
import com.nihongo.app.ui.components.AudioSpeakerButton
import com.nihongo.app.ui.components.NihongoCard
import com.nihongo.app.ui.components.NihongoPrimaryButton
import com.nihongo.app.ui.theme.NihongoTheme

/**
 * Complete Hiragana & Katakana Learning Module Screen persisted via Room Database (`learnedItemIds`).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KanaScreen(
    script: KanaScript,
    userPreferences: UserPreferences,
    learnedItemIds: Set<String>,
    onToggleLearned: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onSwitchScript: () -> Unit,
    onPlayJapaneseAudio: (String, Boolean) -> Unit
) {
    val language = userPreferences.appLanguage
    var selectedCategory by remember { mutableStateOf(KanaCategory.BASIC) }
    var isRecognitionPracticeMode by remember { mutableStateOf(false) }
    var selectedItemIndex by remember { mutableStateOf<Int?>(null) }

    val activeCategoryItems = remember(script, selectedCategory) {
        KanaCatalog.getKanaByScriptAndCategory(script, selectedCategory)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = "${script.title(language)} (${script.titleEn})",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = NihongoTheme.colors.primaryText
                    )
                    Text(
                        text = language.selectText(
                            "বর্ণে ট্যাপ করে উচ্চারণ ও স্ট্রোক অর্ডার শিখুন",
                            "Tap any character for pronunciation & writing practice"
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = NihongoTheme.colors.secondaryText
                    )
                }
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        if (selectedItemIndex != null) {
                            selectedItemIndex = null
                        } else {
                            onNavigateBack()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = language.selectText("ফিরে যান", "Back"),
                        tint = NihongoTheme.colors.ancientWoodBrown
                    )
                }
            },
            actions = {
                FilterChip(
                    selected = false,
                    onClick = {
                        selectedItemIndex = null
                        onSwitchScript()
                    },
                    label = {
                        Text(
                            text = if (script == KanaScript.HIRAGANA) {
                                language.selectText("কাতাকানা দেখুন", "Katakana →")
                            } else {
                                language.selectText("হিরাগানা দেখুন", "← Hiragana")
                            },
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )

        val currentDetailIndex = selectedItemIndex
        if (currentDetailIndex != null && currentDetailIndex in activeCategoryItems.indices) {
            val detailItem = activeCategoryItems[currentDetailIndex]
            KanaDetailView(
                item = detailItem,
                isLearned = learnedItemIds.contains(detailItem.id),
                hasPrevious = currentDetailIndex > 0,
                hasNext = currentDetailIndex < activeCategoryItems.lastIndex,
                language = language,
                onToggleLearned = onToggleLearned,
                onPlayJapaneseAudio = onPlayJapaneseAudio,
                onPreviousCharacter = {
                    if (currentDetailIndex > 0) selectedItemIndex = currentDetailIndex - 1
                },
                onNextCharacter = {
                    if (currentDetailIndex < activeCategoryItems.lastIndex) {
                        selectedItemIndex = currentDetailIndex + 1
                    }
                },
                onCloseDetail = { selectedItemIndex = null }
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                KanaCategory.entries.forEach { category ->
                    FilterChip(
                        selected = !isRecognitionPracticeMode && selectedCategory == category,
                        onClick = {
                            isRecognitionPracticeMode = false
                            selectedCategory = category
                        },
                        label = {
                            Text(
                                text = category.label(language),
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
                    selected = isRecognitionPracticeMode,
                    onClick = { isRecognitionPracticeMode = true },
                    label = {
                        Text(
                            text = language.selectText("যাচাই কুইজ", "Recognition Quiz"),
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Quiz,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NihongoTheme.colors.highlightContainer,
                        selectedLabelColor = NihongoTheme.colors.phoenixFlowerRed
                    )
                )
            }

            if (isRecognitionPracticeMode) {
                KanaRecognitionPracticePanel(
                    items = activeCategoryItems,
                    language = language,
                    onPlayJapaneseAudio = onPlayJapaneseAudio
                )
            } else {
                PaddingBox(
                    description = selectedCategory.description(language),
                    totalCount = activeCategoryItems.size,
                    learnedCount = activeCategoryItems.count { learnedItemIds.contains(it.id) },
                    language = language
                )

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 104.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    itemsIndexed(
                        items = activeCategoryItems,
                        key = { _, item -> item.id }
                    ) { index, kana ->
                        val isLearned = learnedItemIds.contains(kana.id)
                        KanaGridTile(
                            item = kana,
                            isLearned = isLearned,
                            onClick = {
                                onPlayJapaneseAudio(kana.character, false)
                                selectedItemIndex = index
                            },
                            onSpeakClick = {
                                onPlayJapaneseAudio(kana.character, false)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PaddingBox(
    description: String,
    totalCount: Int,
    learnedCount: Int,
    language: AppLanguage
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
        NihongoCard(contentPadding = 12.dp) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = NihongoTheme.colors.primaryText,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = NihongoTheme.colors.selectedContainer
                ) {
                    Text(
                        text = language.selectText(
                            "$learnedCount / $totalCount সম্পন্ন",
                            "$learnedCount / $totalCount Learned"
                        ),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = NihongoTheme.colors.inkGreen,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun KanaGridTile(
    item: KanaItem,
    isLearned: Boolean,
    onClick: () -> Unit,
    onSpeakClick: () -> Unit
) {
    NihongoCard(
        isSelected = isLearned,
        contentPadding = 10.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.romaji,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = NihongoTheme.colors.inkGreen
            )
            if (isLearned) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = NihongoTheme.colors.inkGreen,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                IconButton(
                    onClick = onSpeakClick,
                    modifier = Modifier.size(22.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = null,
                        tint = NihongoTheme.colors.secondaryText,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.character,
                style = if (item.character.length <= 2) {
                    MaterialTheme.typography.displayMedium
                } else {
                    MaterialTheme.typography.headlineMedium
                },
                color = NihongoTheme.colors.primaryText,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = item.pronunciationBn.substringBefore(" ("),
            style = MaterialTheme.typography.bodySmall,
            color = NihongoTheme.colors.secondaryText,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun KanaRecognitionPracticePanel(
    items: List<KanaItem>,
    language: AppLanguage,
    onPlayJapaneseAudio: (String, Boolean) -> Unit
) {
    if (items.size < 4) return

    var currentQuestionIndex by remember(items) { mutableIntStateOf(0) }
    var selectedOptionId by remember(currentQuestionIndex, items) { mutableStateOf<String?>(null) }
    var correctScore by remember(items) { mutableIntStateOf(0) }
    var attemptedCount by remember(items) { mutableIntStateOf(0) }

    val targetItem = items[currentQuestionIndex % items.size]
    val options = remember(currentQuestionIndex, items) {
        val distractors = items.filter { it.id != targetItem.id }.shuffled().take(3)
        (distractors + targetItem).shuffled()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        NihongoCard(isSelected = true) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = language.selectText("কানা চেনার অনুশীলন", "Kana Recognition Practice"),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = NihongoTheme.colors.primaryText
                )
                Text(
                    text = language.selectText(
                        "স্কোর: $correctScore / $attemptedCount",
                        "Score: $correctScore / $attemptedCount"
                    ),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = NihongoTheme.colors.inkGreen
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = targetItem.character,
                        style = MaterialTheme.typography.displayLarge,
                        color = NihongoTheme.colors.primaryText
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AudioSpeakerButton(
                        japaneseText = targetItem.character,
                        onPlayNormal = { onPlayJapaneseAudio(targetItem.character, false) },
                        onPlaySlow = { onPlayJapaneseAudio(targetItem.character, true) },
                        language = language
                    )
                }
            }
        }

        Text(
            text = language.selectText(
                "সঠিক রোমাজি ও বাংলা উচ্চারণটি নির্বাচন করুন:",
                "Select the correct Romaji & pronunciation:"
            ),
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = NihongoTheme.colors.primaryText
        )

        options.forEach { option ->
            val isChosen = selectedOptionId == option.id
            val isCorrectOption = option.id == targetItem.id

            NihongoCard(
                isSelected = selectedOptionId != null && isCorrectOption,
                isHighlighted = isChosen && !isCorrectOption,
                onClick = {
                    if (selectedOptionId == null) {
                        selectedOptionId = option.id
                        attemptedCount++
                        if (isCorrectOption) {
                            correctScore++
                        }
                        onPlayJapaneseAudio(targetItem.character, false)
                    }
                }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${option.romaji}  •  ${option.pronunciationBn}",
                        style = MaterialTheme.typography.titleMedium,
                        color = NihongoTheme.colors.primaryText
                    )
                    if (selectedOptionId != null && isCorrectOption) {
                        Text(
                            text = "✓",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = NihongoTheme.colors.inkGreen
                        )
                    } else if (isChosen && !isCorrectOption) {
                        Text(
                            text = "✗",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = NihongoTheme.colors.phoenixFlowerRed
                        )
                    }
                }
            }
        }

        if (selectedOptionId != null) {
            NihongoPrimaryButton(
                text = language.selectText("পরবর্তী বর্ণ", "Next Character"),
                onClick = {
                    currentQuestionIndex = (currentQuestionIndex + 1) % items.size
                },
                leadingIcon = Icons.Default.Refresh,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
