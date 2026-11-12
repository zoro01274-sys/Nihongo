package com.nihongo.app.ui.screens.practice

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import com.nihongo.app.domain.model.QuizMode
import com.nihongo.app.domain.model.QuizQuestion
import com.nihongo.app.domain.usecase.GenerateQuizUseCase
import com.nihongo.app.ui.components.AudioSpeakerButton
import com.nihongo.app.ui.components.JlptBadge
import com.nihongo.app.ui.components.NihongoAccentButton
import com.nihongo.app.ui.components.NihongoCard
import com.nihongo.app.ui.components.NihongoPrimaryButton
import com.nihongo.app.ui.theme.NihongoTheme

/**
 * Interactive 9-Mode Quiz & Listening Assessment Screen persisted in Room SQLite.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    initialModeCode: String,
    userPreferences: UserPreferences,
    persistedMissedQuestionIds: Set<String>,
    onRecordQuizAttempt: (QuizMode, JlptLevel, Int, Int, List<String>) -> Unit,
    onNavigateBack: () -> Unit,
    onPlayJapaneseAudio: (String, Boolean) -> Unit
) {
    val language = userPreferences.appLanguage
    val quizGenerator = remember { GenerateQuizUseCase() }
    val sessionMistakeIds = remember {
        mutableStateListOf<String>().apply { addAll(persistedMissedQuestionIds) }
    }

    var activeMode by remember(initialModeCode) {
        mutableStateOf(QuizMode.fromCode(initialModeCode))
    }

    val questions = remember(activeMode, sessionMistakeIds.size) {
        quizGenerator.generateQuiz(activeMode, sessionMistakeIds.toSet())
    }

    var currentIndex by remember(activeMode) { mutableIntStateOf(0) }
    var score by remember(activeMode) { mutableIntStateOf(0) }
    var isQuizFinished by remember(activeMode) { mutableStateOf(false) }

    var selectedOptionIndex by remember(currentIndex, activeMode) { mutableStateOf<Int?>(null) }

    val selectedTokens = remember(currentIndex, activeMode) { mutableStateListOf<String>() }
    var isSentenceOrderSubmitted by remember(currentIndex, activeMode) { mutableStateOf(false) }

    var selectedLeftPairId by remember(currentIndex, activeMode) { mutableStateOf<String?>(null) }
    val matchedPairIds = remember(currentIndex, activeMode) { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = activeMode.title(language),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = NihongoTheme.colors.primaryText
                    )
                    Text(
                        text = activeMode.subtitle(language),
                        style = MaterialTheme.typography.bodySmall,
                        color = NihongoTheme.colors.secondaryText
                    )
                }
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuizMode.entries.forEach { mode ->
                FilterChip(
                    selected = activeMode == mode,
                    onClick = { activeMode = mode },
                    label = { Text(mode.title(language)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NihongoTheme.colors.selectedContainer,
                        selectedLabelColor = NihongoTheme.colors.inkGreen
                    )
                )
            }
        }

        if (isQuizFinished || questions.isEmpty()) {
            QuizSummaryView(
                score = score,
                total = questions.size,
                mistakeCount = sessionMistakeIds.size,
                language = language,
                onRetryQuiz = {
                    currentIndex = 0
                    score = 0
                    isQuizFinished = false
                },
                onPracticeMistakes = {
                    activeMode = QuizMode.MISTAKE_REVIEW
                    currentIndex = 0
                    score = 0
                    isQuizFinished = false
                }
            )
        } else {
            val question = questions[currentIndex]

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = language.selectText(
                                "প্রশ্ন: ${currentIndex + 1} / ${questions.size}",
                                "Question: ${currentIndex + 1} / ${questions.size}"
                            ),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = NihongoTheme.colors.primaryText
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            JlptBadge(level = question.jlptLevel)
                            Text(
                                text = language.selectText("স্কোর: $score", "Score: $score"),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = NihongoTheme.colors.inkGreen
                            )
                        }
                    }
                }

                item {
                    NihongoCard(isSelected = true) {
                        Text(
                            text = language.selectText(
                                question.questionInstructionBn,
                                question.questionInstructionEn
                            ),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = NihongoTheme.colors.inkGreen
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                val displayPrompt = if (
                                    question.hideJapanesePromptUntilAnswered &&
                                    selectedOptionIndex == null
                                ) {
                                    language.selectText(
                                        "🔊 অডিওটি শুনুন (উত্তর দেওয়ার পর টেক্সট দেখা যাবে)",
                                        "🔊 Listen to the audio (Text reveals after answering)"
                                    )
                                } else {
                                    question.promptJapanese
                                }

                                Text(
                                    text = displayPrompt,
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = NihongoTheme.colors.primaryText
                                )
                                if (question.promptSubtext.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = question.promptSubtext,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = NihongoTheme.colors.secondaryText
                                    )
                                }
                            }

                            if (question.audioTextToSpeak.isNotBlank()) {
                                Spacer(modifier = Modifier.width(8.dp))
                                AudioSpeakerButton(
                                    japaneseText = question.audioTextToSpeak,
                                    onPlayNormal = {
                                        onPlayJapaneseAudio(question.audioTextToSpeak, false)
                                    },
                                    onPlaySlow = {
                                        onPlayJapaneseAudio(question.audioTextToSpeak, true)
                                    },
                                    language = language
                                )
                            }
                        }
                    }
                }

                when (question.mode) {
                    QuizMode.SENTENCE_ORDERING -> {
                        item {
                            SentenceOrderingSection(
                                question = question,
                                selectedTokens = selectedTokens,
                                isSubmitted = isSentenceOrderSubmitted,
                                language = language,
                                onToggleToken = { token ->
                                    if (!isSentenceOrderSubmitted) {
                                        if (selectedTokens.contains(token)) {
                                            selectedTokens.remove(token)
                                        } else {
                                            selectedTokens.add(token)
                                        }
                                    }
                                },
                                onSubmitOrder = {
                                    isSentenceOrderSubmitted = true
                                    val isCorrect = selectedTokens.toList() == question.correctSentenceOrder
                                    if (isCorrect) {
                                        score++
                                        sessionMistakeIds.remove(question.id)
                                    } else if (!sessionMistakeIds.contains(question.id)) {
                                        sessionMistakeIds.add(question.id)
                                    }
                                    if (question.audioTextToSpeak.isNotBlank()) {
                                        onPlayJapaneseAudio(question.audioTextToSpeak, false)
                                    }
                                }
                            )
                        }
                    }

                    QuizMode.MATCH_WORDS -> {
                        item {
                            WordMatchingSection(
                                question = question,
                                selectedLeftPairId = selectedLeftPairId,
                                matchedPairIds = matchedPairIds,
                                language = language,
                                onSelectLeft = { id -> selectedLeftPairId = id },
                                onSelectRight = { rightId ->
                                    val leftId = selectedLeftPairId
                                    if (leftId != null) {
                                        if (leftId == rightId && !matchedPairIds.contains(rightId)) {
                                            matchedPairIds.add(rightId)
                                            if (matchedPairIds.size == question.matchingPairs.size) {
                                                score++
                                            }
                                        }
                                        selectedLeftPairId = null
                                    }
                                }
                            )
                        }
                    }

                    else -> {
                        items(question.options.size) { idx ->
                            val optionText = question.options[idx]
                            val isChosen = selectedOptionIndex == idx
                            val isCorrect = idx == question.correctOptionIndex

                            NihongoCard(
                                isSelected = selectedOptionIndex != null && isCorrect,
                                isHighlighted = isChosen && !isCorrect,
                                onClick = {
                                    if (selectedOptionIndex == null) {
                                        selectedOptionIndex = idx
                                        if (isCorrect) {
                                            score++
                                            sessionMistakeIds.remove(question.id)
                                        } else if (!sessionMistakeIds.contains(question.id)) {
                                            sessionMistakeIds.add(question.id)
                                        }
                                        if (question.audioTextToSpeak.isNotBlank()) {
                                            onPlayJapaneseAudio(question.audioTextToSpeak, false)
                                        }
                                    }
                                }
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${idx + 1}.  $optionText",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = NihongoTheme.colors.primaryText,
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (selectedOptionIndex != null && isCorrect) {
                                        Text(
                                            text = "✓",
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = NihongoTheme.colors.inkGreen
                                        )
                                    } else if (isChosen && !isCorrect) {
                                        Text(
                                            text = "✗",
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = NihongoTheme.colors.phoenixFlowerRed
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                val isQuestionCompleted = when (question.mode) {
                    QuizMode.SENTENCE_ORDERING -> isSentenceOrderSubmitted
                    QuizMode.MATCH_WORDS -> matchedPairIds.size == question.matchingPairs.size
                    else -> selectedOptionIndex != null
                }

                if (isQuestionCompleted) {
                    item {
                        NihongoCard {
                            Text(
                                text = language.selectText("ব্যাখ্যা (Explanation):", "Explanation:"),
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = NihongoTheme.colors.inkGreen
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = language.selectText(question.explanationBn, question.explanationEn),
                                style = MaterialTheme.typography.bodyMedium,
                                color = NihongoTheme.colors.primaryText
                            )
                        }
                    }

                    item {
                        NihongoPrimaryButton(
                            text = if (currentIndex < questions.lastIndex) {
                                language.selectText("পরবর্তী প্রশ্ন", "Next Question")
                            } else {
                                language.selectText("ফলাফল সংরক্ষণ করুন", "Save & View Results")
                            },
                            onClick = {
                                if (currentIndex < questions.lastIndex) {
                                    currentIndex++
                                } else {
                                    onRecordQuizAttempt(
                                        activeMode,
                                        userPreferences.currentJlptLevel,
                                        score,
                                        questions.size,
                                        sessionMistakeIds.toList()
                                    )
                                    isQuizFinished = true
                                }
                            },
                            leadingIcon = Icons.AutoMirrored.Filled.ArrowForward,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SentenceOrderingSection(
    question: QuizQuestion,
    selectedTokens: List<String>,
    isSubmitted: Boolean,
    language: AppLanguage,
    onToggleToken: (String) -> Unit,
    onSubmitOrder: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        NihongoCard(
            isSelected = isSubmitted && selectedTokens == question.correctSentenceOrder,
            isHighlighted = isSubmitted && selectedTokens != question.correctSentenceOrder
        ) {
            Text(
                text = language.selectText("আপনার সাজানো বাক্য:", "Your Assembled Sentence:"),
                style = MaterialTheme.typography.labelMedium,
                color = NihongoTheme.colors.secondaryText
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (selectedTokens.isEmpty()) {
                    language.selectText("নিচের শব্দগুলোতে ট্যাপ করুন...", "Tap words below in order...")
                } else {
                    selectedTokens.joinToString(separator = " ")
                },
                style = MaterialTheme.typography.headlineMedium,
                color = NihongoTheme.colors.primaryText
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            question.sentenceTokens.forEach { token ->
                val isUsed = selectedTokens.contains(token)
                FilterChip(
                    selected = isUsed,
                    onClick = { onToggleToken(token) },
                    label = {
                        Text(
                            text = token,
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NihongoTheme.colors.selectedContainer,
                        selectedLabelColor = NihongoTheme.colors.inkGreen
                    )
                )
            }
        }

        if (!isSubmitted) {
            NihongoPrimaryButton(
                text = language.selectText("উত্তর যাচাই করুন", "Check Sentence Order"),
                onClick = onSubmitOrder,
                enabled = selectedTokens.size == question.sentenceTokens.size,
                leadingIcon = Icons.Default.CheckCircle,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun WordMatchingSection(
    question: QuizQuestion,
    selectedLeftPairId: String?,
    matchedPairIds: List<String>,
    language: AppLanguage,
    onSelectLeft: (String) -> Unit,
    onSelectRight: (String) -> Unit
) {
    val rightColumnPairs = remember(question.id) { question.matchingPairs.shuffled() }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            question.matchingPairs.forEach { pair ->
                val isMatched = matchedPairIds.contains(pair.pairId)
                val isSelected = selectedLeftPairId == pair.pairId

                NihongoCard(
                    isSelected = isMatched || isSelected,
                    contentPadding = 12.dp,
                    onClick = {
                        if (!isMatched) onSelectLeft(pair.pairId)
                    }
                ) {
                    Text(
                        text = pair.japaneseText,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = NihongoTheme.colors.primaryText
                    )
                    Text(
                        text = pair.readingRomaji,
                        style = MaterialTheme.typography.bodySmall,
                        color = NihongoTheme.colors.inkGreen
                    )
                }
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            rightColumnPairs.forEach { pair ->
                val isMatched = matchedPairIds.contains(pair.pairId)

                NihongoCard(
                    isSelected = isMatched,
                    contentPadding = 12.dp,
                    onClick = {
                        if (!isMatched) onSelectRight(pair.pairId)
                    }
                ) {
                    Text(
                        text = language.selectText(pair.meaningBn, pair.meaningEn),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = NihongoTheme.colors.primaryText
                    )
                    Text(
                        text = language.selectText(pair.meaningEn, pair.meaningBn),
                        style = MaterialTheme.typography.bodySmall,
                        color = NihongoTheme.colors.secondaryText
                    )
                }
            }
        }
    }
}

@Composable
private fun QuizSummaryView(
    score: Int,
    total: Int,
    mistakeCount: Int,
    language: AppLanguage,
    onRetryQuiz: () -> Unit,
    onPracticeMistakes: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NihongoCard(isSelected = true) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = NihongoTheme.colors.phoenixFlowerRed
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = language.selectText("কুইজ সম্পন্ন হয়েছে!", "Assessment Complete!"),
                    style = MaterialTheme.typography.headlineMedium,
                    color = NihongoTheme.colors.primaryText
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = language.selectText(
                    "আপনার প্রাপ্ত স্কোর: $score / $total",
                    "Your Final Score: $score / $total"
                ),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = NihongoTheme.colors.inkGreen
            )
        }

        NihongoPrimaryButton(
            text = language.selectText("পুনরায় অনুশীলন করুন", "Practice Again"),
            onClick = onRetryQuiz,
            leadingIcon = Icons.Default.Refresh,
            modifier = Modifier.fillMaxWidth()
        )

        if (mistakeCount > 0) {
            NihongoAccentButton(
                text = language.selectText(
                    "ভুল উত্তরগুলো রিভিশন করুন ($mistakeCount)",
                    "Review Mistakes ($mistakeCount)"
                ),
                onClick = onPracticeMistakes,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
