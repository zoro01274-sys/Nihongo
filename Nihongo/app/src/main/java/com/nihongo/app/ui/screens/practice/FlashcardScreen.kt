package com.nihongo.app.ui.screens.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nihongo.app.data.local.datastore.UserPreferences
import com.nihongo.app.domain.model.JlptLevel
import com.nihongo.app.domain.model.LearningCatalog
import com.nihongo.app.domain.model.SrsReviewGrade
import com.nihongo.app.ui.components.AudioSpeakerButton
import com.nihongo.app.ui.components.BilingualMeaningBlock
import com.nihongo.app.ui.components.JlptBadge
import com.nihongo.app.ui.components.NihongoAccentButton
import com.nihongo.app.ui.components.NihongoCard
import com.nihongo.app.ui.components.NihongoOutlinedButton
import com.nihongo.app.ui.components.NihongoPrimaryButton
import com.nihongo.app.ui.theme.NihongoTheme

private data class StudyFlashcard(
    val id: String,
    val contentType: String,
    val frontJapanese: String,
    val reading: String,
    val romaji: String,
    val meaningBn: String,
    val meaningEn: String,
    val exampleJp: String,
    val exampleBn: String,
    val exampleEn: String,
    val level: JlptLevel
)

/**
 * Interactive Flashcards & Spaced Repetition Deck persisted in Room SQLite via `onRecordSrsReview`.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardScreen(
    userPreferences: UserPreferences,
    onRecordSrsReview: (String, String, SrsReviewGrade) -> Unit,
    onNavigateBack: () -> Unit,
    onPlayJapaneseAudio: (String, Boolean) -> Unit
) {
    val language = userPreferences.appLanguage

    val deck = remember {
        val vocabCards = LearningCatalog.vocabularyList.map { v ->
            StudyFlashcard(
                id = v.id,
                contentType = "VOCAB",
                frontJapanese = v.wordJp,
                reading = v.hiraganaReading,
                romaji = v.romaji,
                meaningBn = v.meaningBn,
                meaningEn = v.meaningEn,
                exampleJp = v.exampleSentenceJp,
                exampleBn = v.exampleSentenceBn,
                exampleEn = v.exampleSentenceEn,
                level = v.jlptLevel
            )
        }
        val kanjiCards = LearningCatalog.kanjiList.map { k ->
            StudyFlashcard(
                id = k.id,
                contentType = "KANJI",
                frontJapanese = k.character,
                reading = "${k.onyomiKana} / ${k.kunyomiKana}",
                romaji = k.romajiReadings,
                meaningBn = k.meaningBn,
                meaningEn = k.meaningEn,
                exampleJp = k.exampleSentenceJp,
                exampleBn = k.exampleSentenceBn,
                exampleEn = k.exampleSentenceEn,
                level = k.jlptLevel
            )
        }
        vocabCards + kanjiCards
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }
    var knownCount by remember { mutableIntStateOf(0) }
    var revisionCount by remember { mutableIntStateOf(0) }

    val activeCard = deck[currentIndex % deck.size]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = language.selectText("ফ্ল্যাশকার্ড (SRS Flashcards)", "SRS Flashcards"),
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = language.selectText(
                        "কার্ড: ${(currentIndex % deck.size) + 1} / ${deck.size}",
                        "Card: ${(currentIndex % deck.size) + 1} / ${deck.size}"
                    ),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = NihongoTheme.colors.primaryText
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = language.selectText("জানা আছে: $knownCount", "Known: $knownCount"),
                        style = MaterialTheme.typography.labelMedium,
                        color = NihongoTheme.colors.inkGreen
                    )
                    Text(
                        text = language.selectText("রিভিশন: $revisionCount", "Review: $revisionCount"),
                        style = MaterialTheme.typography.labelMedium,
                        color = NihongoTheme.colors.phoenixFlowerRed
                    )
                }
            }

            NihongoCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                isSelected = isFlipped,
                onClick = { isFlipped = !isFlipped }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    JlptBadge(level = activeCard.level)
                    AudioSpeakerButton(
                        japaneseText = activeCard.frontJapanese,
                        onPlayNormal = { onPlayJapaneseAudio(activeCard.frontJapanese, false) },
                        onPlaySlow = { onPlayJapaneseAudio(activeCard.frontJapanese, true) },
                        language = language
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    if (!isFlipped) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = activeCard.frontJapanese,
                                style = MaterialTheme.typography.displayLarge,
                                color = NihongoTheme.colors.primaryText,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Surface(
                                shape = MaterialTheme.shapes.small,
                                color = NihongoTheme.colors.elevatedSurface
                            ) {
                                Text(
                                    text = language.selectText(
                                        "অর্থ ও উচ্চারণ দেখতে কার্ডে ট্যাপ করুন",
                                        "Tap card to reveal reading & meaning"
                                    ),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = NihongoTheme.colors.secondaryText,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = activeCard.frontJapanese,
                                style = MaterialTheme.typography.headlineLarge,
                                color = NihongoTheme.colors.inkGreen
                            )
                            Text(
                                text = "${activeCard.reading} (${activeCard.romaji})",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = NihongoTheme.colors.phoenixFlowerRed
                            )

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 14.dp),
                                color = NihongoTheme.colors.borderOutline.copy(alpha = 0.4f)
                            )

                            BilingualMeaningBlock(
                                bengaliMeaning = activeCard.meaningBn,
                                englishMeaning = activeCard.meaningEn,
                                primaryLanguage = language
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Surface(
                                shape = MaterialTheme.shapes.small,
                                color = NihongoTheme.colors.elevatedSurface,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = activeCard.exampleJp,
                                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                                        color = NihongoTheme.colors.primaryText
                                    )
                                    Text(
                                        text = language.selectText(activeCard.exampleBn, activeCard.exampleEn),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = NihongoTheme.colors.secondaryText
                                    )
                                }
                            }
                        }
                    }
                }
            }

            NihongoOutlinedButton(
                text = if (isFlipped) {
                    language.selectText("সামনের অংশ দেখুন (Front)", "Show Front")
                } else {
                    language.selectText("উত্তর দেখুন (Flip Card)", "Flip Card")
                },
                onClick = { isFlipped = !isFlipped },
                leadingIcon = Icons.Default.Flip,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                NihongoAccentButton(
                    text = language.selectText("রিভিশন প্রয়োজন (1d)", "Need Revision (1d)"),
                    onClick = {
                        revisionCount++
                        onRecordSrsReview(activeCard.id, activeCard.contentType, SrsReviewGrade.AGAIN)
                        isFlipped = false
                        currentIndex++
                    },
                    leadingIcon = Icons.Default.Replay,
                    modifier = Modifier.weight(1f)
                )

                NihongoPrimaryButton(
                    text = language.selectText("জানা আছে (SM-2)", "Know It (SM-2)"),
                    onClick = {
                        knownCount++
                        onRecordSrsReview(activeCard.id, activeCard.contentType, SrsReviewGrade.GOOD)
                        isFlipped = false
                        currentIndex++
                    },
                    leadingIcon = Icons.Default.CheckCircle,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
