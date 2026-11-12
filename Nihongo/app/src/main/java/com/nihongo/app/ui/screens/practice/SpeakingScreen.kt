package com.nihongo.app.ui.screens.practice

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.nihongo.app.audio.JapaneseSpeechRecognizer
import com.nihongo.app.data.local.datastore.UserPreferences
import com.nihongo.app.domain.model.LearningCatalog
import com.nihongo.app.ui.components.AudioSpeakerButton
import com.nihongo.app.ui.components.BilingualMeaningBlock
import com.nihongo.app.ui.components.JlptBadge
import com.nihongo.app.ui.components.NihongoAccentButton
import com.nihongo.app.ui.components.NihongoCard
import com.nihongo.app.ui.components.NihongoOutlinedButton
import com.nihongo.app.ui.components.NihongoPrimaryButton
import com.nihongo.app.ui.theme.NihongoTheme

/**
 * Optional Speaking & Pronunciation Practice Screen (Section 11).
 * Uses Android SpeechRecognizer (`ja-JP`) to practice words and example sentences,
 * and includes the mandatory accuracy disclaimer in Bengali and English.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeakingScreen(
    userPreferences: UserPreferences,
    onNavigateBack: () -> Unit,
    onPlayJapaneseAudio: (String, Boolean) -> Unit
) {
    val context = LocalContext.current
    val language = userPreferences.appLanguage
    val recognizer = remember { JapaneseSpeechRecognizer(context) }
    val recognitionState by recognizer.state.collectAsState()

    DisposableEffect(Unit) {
        onDispose { recognizer.destroy() }
    }

    var practiceSentencesMode by remember { mutableStateOf(false) }
    var currentIndex by remember { mutableIntStateOf(0) }

    val vocabItems = LearningCatalog.vocabularyList
    val currentItem = vocabItems[currentIndex % vocabItems.size]

    val targetKanjiText = if (practiceSentencesMode) {
        currentItem.exampleSentenceJp
    } else {
        currentItem.wordJp
    }

    val targetHiraganaText = if (practiceSentencesMode) {
        currentItem.exampleSentenceReading
    } else {
        currentItem.hiraganaReading
    }

    val targetRomajiText = if (practiceSentencesMode) {
        currentItem.exampleSentenceRomaji
    } else {
        currentItem.romaji
    }

    val targetMeaningBn = if (practiceSentencesMode) {
        currentItem.exampleSentenceBn
    } else {
        currentItem.meaningBn
    }

    val targetMeaningEn = if (practiceSentencesMode) {
        currentItem.exampleSentenceEn
    } else {
        currentItem.meaningEn
    }

    val micPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            recognizer.startListening(
                targetJapaneseKanji = targetKanjiText,
                targetHiraganaReading = targetHiraganaText
            )
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
                    text = language.selectText(
                        "উচ্চারণ ও কথন অনুশীলন (Speaking)",
                        "Japanese Speaking Practice"
                    ),
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

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Mode Toggle: Practice Words vs Practice Example Sentences
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FilterChip(
                        selected = !practiceSentencesMode,
                        onClick = {
                            practiceSentencesMode = false
                            recognizer.clearResult()
                        },
                        label = {
                            Text(language.selectText("জাপানি শব্দ বলুন", "Practice Words"))
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NihongoTheme.colors.selectedContainer,
                            selectedLabelColor = NihongoTheme.colors.inkGreen
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    FilterChip(
                        selected = practiceSentencesMode,
                        onClick = {
                            practiceSentencesMode = true
                            recognizer.clearResult()
                        },
                        label = {
                            Text(language.selectText("উদাহরণ বাক্য বলুন", "Practice Sentences"))
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NihongoTheme.colors.selectedContainer,
                            selectedLabelColor = NihongoTheme.colors.inkGreen
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Target Japanese Phrase Card (Listen and Repeat)
            item {
                NihongoCard(isSelected = true) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        JlptBadge(level = currentItem.jlptLevel)
                        AudioSpeakerButton(
                            japaneseText = targetKanjiText,
                            onPlayNormal = { onPlayJapaneseAudio(targetKanjiText, false) },
                            onPlaySlow = { onPlayJapaneseAudio(targetKanjiText, true) },
                            showSourceBadge = true,
                            language = language
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = targetKanjiText,
                        style = MaterialTheme.typography.headlineLarge,
                        color = NihongoTheme.colors.primaryText
                    )
                    Text(
                        text = targetHiraganaText,
                        style = MaterialTheme.typography.titleMedium,
                        color = NihongoTheme.colors.inkGreen
                    )
                    Text(
                        text = targetRomajiText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = NihongoTheme.colors.secondaryText
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    BilingualMeaningBlock(
                        bengaliMeaning = targetMeaningBn,
                        englishMeaning = targetMeaningEn,
                        primaryLanguage = language
                    )
                }
            }

            // Microphone Action Card
            item {
                NihongoCard(
                    isSelected = recognitionState.isMatchSuccess == true,
                    isHighlighted = recognitionState.isMatchSuccess == false
                ) {
                    Text(
                        text = language.selectText(
                            "১. প্রথমে স্পিকার আইকনে ট্যাপ করে উচ্চারণটি শুনুন।\n২. এরপর নিচের মাইক্রোফোন বাটনে ট্যাপ করে স্পষ্টভাবে জাপানিতে বলুন।",
                            "1. Tap the speaker icon above to listen first.\n2. Tap the microphone button below and repeat clearly in Japanese."
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = NihongoTheme.colors.primaryText
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    if (recognitionState.isListening) {
                        NihongoAccentButton(
                            text = language.selectText("শোনা হচ্ছে... (থামাতে ট্যাপ করুন)", "Listening... (Tap to Stop)"),
                            onClick = { recognizer.stopListening() },
                            leadingIcon = Icons.Default.Stop,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        NihongoPrimaryButton(
                            text = language.selectText("মাইক্রোফোনে জাপানি বলুন", "Tap to Speak Japanese"),
                            onClick = {
                                val hasMicPerm = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.RECORD_AUDIO
                                ) == PackageManager.PERMISSION_GRANTED

                                if (hasMicPerm) {
                                    recognizer.startListening(
                                        targetJapaneseKanji = targetKanjiText,
                                        targetHiraganaReading = targetHiraganaText
                                    )
                                } else {
                                    micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                }
                            },
                            leadingIcon = Icons.Default.Mic,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    if (recognitionState.recognizedJapaneseText.isNotBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = language.selectText(
                                "শনাক্তকৃত উচ্চারণ: 「${recognitionState.recognizedJapaneseText}」",
                                "Recognized Speech: 「${recognitionState.recognizedJapaneseText}」"
                            ),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (recognitionState.isMatchSuccess == true) {
                                NihongoTheme.colors.inkGreen
                            } else {
                                NihongoTheme.colors.phoenixFlowerRed
                            }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = language.selectText(
                                recognitionState.confidenceMessageBn,
                                recognitionState.confidenceMessageEn
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = NihongoTheme.colors.primaryText
                        )
                    }

                    if (recognitionState.errorTextBn != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = language.selectText(
                                recognitionState.errorTextBn.orEmpty(),
                                recognitionState.errorTextEn.orEmpty()
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = NihongoTheme.colors.phoenixFlowerRed
                        )
                    }
                }
            }

            // Next / Previous Phrase Navigation
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    NihongoOutlinedButton(
                        text = language.selectText("পূর্ববর্তী", "Previous"),
                        onClick = {
                            recognizer.clearResult()
                            currentIndex = if (currentIndex > 0) currentIndex - 1 else vocabItems.lastIndex
                        },
                        leadingIcon = Icons.AutoMirrored.Filled.ArrowBack,
                        modifier = Modifier.weight(1f)
                    )
                    NihongoOutlinedButton(
                        text = language.selectText("পরবর্তী অনুশীলন", "Next Phrase"),
                        onClick = {
                            recognizer.clearResult()
                            currentIndex = (currentIndex + 1) % vocabItems.size
                        },
                        leadingIcon = Icons.AutoMirrored.Filled.ArrowForward,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Mandatory Accuracy Disclaimer (Section 11 requirement)
            item {
                NihongoCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = NihongoTheme.colors.ancientWoodBrown
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = language.selectText(
                                "উচ্চারণ যাচাই সংক্রান্ত নোট",
                                "Speech Recognition Accuracy Note"
                            ),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = NihongoTheme.colors.primaryText
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = language.selectText(
                            "অ্যান্ড্রয়েড স্পিচ রিকগনিশনের নির্ভুলতা ডিভাইস ও পারিপার্শ্বিক শব্দের ওপর ভিত্তি করে পরিবর্তিত হতে পারে। এটি কথন অনুশীলনের একটি সহায়ক মাধ্যম এবং শতভাগ নিখুঁত উচ্চারণ মূল্যায়নের নিশ্চয়তা দেয় না।",
                            "Speech recognition accuracy varies across devices and environments and does not guarantee exact pronunciation scoring. Use it as a helpful self-practice aid."
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = NihongoTheme.colors.secondaryText
                    )
                }
            }
        }
    }
}
