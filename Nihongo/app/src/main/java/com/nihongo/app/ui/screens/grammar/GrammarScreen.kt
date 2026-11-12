package com.nihongo.app.ui.screens.grammar

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.nihongo.app.domain.model.GrammarLesson
import com.nihongo.app.domain.model.JlptLevel
import com.nihongo.app.domain.model.LearningCatalog
import com.nihongo.app.ui.components.AudioSpeakerButton
import com.nihongo.app.ui.components.BilingualMeaningBlock
import com.nihongo.app.ui.components.JlptBadge
import com.nihongo.app.ui.components.NihongoCard
import com.nihongo.app.ui.components.NihongoOutlinedButton
import com.nihongo.app.ui.theme.NihongoTheme

/**
 * Complete Structured Japanese Grammar Course Module from N5 to N1 (Section 09).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrammarScreen(
    userPreferences: UserPreferences,
    onNavigateBack: () -> Unit,
    onPlayJapaneseAudio: (String, Boolean) -> Unit
) {
    val language = userPreferences.appLanguage
    var selectedLevel by remember { mutableStateOf<JlptLevel?>(null) }
    var selectedLesson by remember { mutableStateOf<GrammarLesson?>(null) }

    val filteredLessons = remember(selectedLevel) {
        LearningCatalog.grammarList.filter {
            selectedLevel == null || it.jlptLevel == selectedLevel
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
                    text = language.selectText("জাপানি ব্যাকরণ (N5–N1)", "Japanese Grammar (N5–N1)"),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = NihongoTheme.colors.primaryText
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        if (selectedLesson != null) selectedLesson = null else onNavigateBack()
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

        val currentLesson = selectedLesson
        if (currentLesson != null) {
            GrammarLessonDetailView(
                lesson = currentLesson,
                language = language,
                onPlayJapaneseAudio = onPlayJapaneseAudio,
                onClose = { selectedLesson = null }
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedLevel == null,
                    onClick = { selectedLevel = null },
                    label = { Text(language.selectText("সব লেভেল (N5–N1)", "All Levels (N5–N1)")) },
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
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = filteredLessons,
                    key = { it.id }
                ) { lesson ->
                    NihongoCard(onClick = { selectedLesson = lesson }) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                JlptBadge(level = lesson.jlptLevel)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = lesson.pattern,
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = NihongoTheme.colors.inkGreen
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                BilingualMeaningBlock(
                                    bengaliMeaning = lesson.titleBn,
                                    englishMeaning = lesson.titleEn,
                                    primaryLanguage = language
                                )
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
}

@Composable
private fun GrammarLessonDetailView(
    lesson: GrammarLesson,
    language: AppLanguage,
    onPlayJapaneseAudio: (String, Boolean) -> Unit,
    onClose: () -> Unit
) {
    var selectedAnswerIndex by remember(lesson.id) { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        NihongoOutlinedButton(
            text = language.selectText("ব্যাকরণ তালিকায় ফিরুন", "Back to Grammar List"),
            onClick = onClose,
            leadingIcon = Icons.AutoMirrored.Filled.ArrowBack
        )

        // Pattern Hero Card
        NihongoCard(isSelected = true) {
            JlptBadge(level = lesson.jlptLevel)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = lesson.pattern,
                style = MaterialTheme.typography.headlineLarge,
                color = NihongoTheme.colors.primaryText
            )
            Spacer(modifier = Modifier.height(6.dp))
            BilingualMeaningBlock(
                bengaliMeaning = lesson.titleBn,
                englishMeaning = lesson.titleEn,
                primaryLanguage = language
            )
        }

        // Formation Rules Card
        NihongoCard {
            Text(
                text = language.selectText("গঠন রীতি (Formation Rules)", "Formation Rules"),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = NihongoTheme.colors.inkGreen
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = lesson.formationRulesBn,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = NihongoTheme.colors.primaryText
            )
            Text(
                text = lesson.formationRulesEn,
                style = MaterialTheme.typography.bodyMedium,
                color = NihongoTheme.colors.secondaryText
            )
        }

        // Bengali & English Detailed Explanation
        NihongoCard {
            Text(
                text = language.selectText("বাংলা ব্যাখ্যা", "Bengali Explanation"),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = NihongoTheme.colors.inkGreen
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = lesson.explanationBn,
                style = MaterialTheme.typography.bodyLarge,
                color = NihongoTheme.colors.primaryText
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = NihongoTheme.colors.borderOutline.copy(alpha = 0.4f)
            )

            Text(
                text = language.selectText("ইংরেজি ব্যাখ্যা (English Explanation)", "English Explanation"),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = NihongoTheme.colors.inkGreen
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = lesson.explanationEn,
                style = MaterialTheme.typography.bodyMedium,
                color = NihongoTheme.colors.secondaryText
            )
        }

        // Japanese Example Sentences with Audio
        NihongoCard {
            Text(
                text = language.selectText("উদাহরণ বাক্যসমূহ (Examples)", "Japanese Examples"),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = NihongoTheme.colors.inkGreen
            )
            Spacer(modifier = Modifier.height(8.dp))

            lesson.examples.forEachIndexed { idx, ex ->
                if (idx > 0) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 10.dp),
                        color = NihongoTheme.colors.borderOutline.copy(alpha = 0.4f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = ex.japanese,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = NihongoTheme.colors.primaryText
                        )
                        Text(
                            text = ex.hiraganaReading,
                            style = MaterialTheme.typography.bodySmall,
                            color = NihongoTheme.colors.inkGreen
                        )
                        Text(
                            text = ex.romaji,
                            style = MaterialTheme.typography.bodySmall,
                            color = NihongoTheme.colors.secondaryText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = ex.bengali,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            color = NihongoTheme.colors.primaryText
                        )
                        Text(
                            text = ex.english,
                            style = MaterialTheme.typography.bodySmall,
                            color = NihongoTheme.colors.secondaryText
                        )
                    }
                    AudioSpeakerButton(
                        japaneseText = ex.japanese,
                        onPlayNormal = { onPlayJapaneseAudio(ex.japanese, false) },
                        onPlaySlow = { onPlayJapaneseAudio(ex.japanese, true) },
                        language = language
                    )
                }
            }
        }

        // Common Mistakes Alert Card (Phoenix Flower Red Accent)
        NihongoCard(isHighlighted = true) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.WarningAmber,
                    contentDescription = null,
                    tint = NihongoTheme.colors.phoenixFlowerRed
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = language.selectText("সাধারণ ভুল ও সতর্কতা (Common Mistakes)", "Common Mistakes"),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = NihongoTheme.colors.phoenixFlowerRed
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = lesson.commonMistakesBn,
                style = MaterialTheme.typography.bodyMedium,
                color = NihongoTheme.colors.primaryText
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = lesson.commonMistakesEn,
                style = MaterialTheme.typography.bodySmall,
                color = NihongoTheme.colors.secondaryText
            )
        }

        // Interactive Practice Question inside the Grammar Lesson
        val q = lesson.practiceQuestion
        NihongoCard {
            Text(
                text = language.selectText("অনুশীলন প্রশ্ন (Practice Question)", "Practice Check"),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = NihongoTheme.colors.inkGreen
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = q.promptJp,
                style = MaterialTheme.typography.headlineMedium,
                color = NihongoTheme.colors.primaryText
            )
            Text(
                text = language.selectText(q.promptBn, q.promptEn),
                style = MaterialTheme.typography.bodyMedium,
                color = NihongoTheme.colors.secondaryText
            )
            Spacer(modifier = Modifier.height(10.dp))

            q.options.forEachIndexed { index, optionText ->
                val isSelected = selectedAnswerIndex == index
                val isCorrect = index == q.correctOptionIndex
                NihongoCard(
                    modifier = Modifier.padding(vertical = 4.dp),
                    isSelected = selectedAnswerIndex != null && isCorrect,
                    isHighlighted = isSelected && !isCorrect,
                    contentPadding = 12.dp,
                    onClick = { selectedAnswerIndex = index }
                ) {
                    Text(
                        text = "${index + 1}. $optionText",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = NihongoTheme.colors.primaryText
                    )
                }
            }

            if (selectedAnswerIndex != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = language.selectText(q.explanationBn, q.explanationEn),
                    style = MaterialTheme.typography.bodyMedium,
                    color = NihongoTheme.colors.inkGreen
                )
            }
        }
    }
}
