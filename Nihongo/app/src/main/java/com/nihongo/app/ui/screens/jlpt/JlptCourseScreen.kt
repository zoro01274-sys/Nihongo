package com.nihongo.app.ui.screens.jlpt

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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nihongo.app.data.local.datastore.UserPreferences
import com.nihongo.app.domain.model.AppLanguage
import com.nihongo.app.domain.model.JlptCourseCatalog
import com.nihongo.app.domain.model.JlptLevel
import com.nihongo.app.domain.model.JlptLevelCourseBundle
import com.nihongo.app.domain.model.JlptMockExamQuestion
import com.nihongo.app.domain.model.JlptReadingPassage
import com.nihongo.app.domain.model.LearningCatalog
import com.nihongo.app.ui.components.AudioSpeakerButton
import com.nihongo.app.ui.components.BilingualMeaningBlock
import com.nihongo.app.ui.components.JlptBadge
import com.nihongo.app.ui.components.LessonCompletionBadge
import com.nihongo.app.ui.components.NihongoAccentButton
import com.nihongo.app.ui.components.NihongoCard
import com.nihongo.app.ui.components.NihongoOutlinedButton
import com.nihongo.app.ui.components.NihongoPrimaryButton
import com.nihongo.app.ui.theme.NihongoTheme

/**
 * Complete JLPT N5 to N1 Course System Screen (Section 13).
 * Features:
 * - Five separate learning levels (N5, N4, N3, N2, N1)
 * - Level Dashboard with Topic Completion & Exam Preparation tips
 * - Level Reading Comprehension (Dokkai) passages with audio and comprehension checks
 * - App-created Mock Examinations with test results and Mistake Review
 * - Explicit disclaimer distinguishing app practice tests from official JLPT exams
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JlptCourseScreen(
    initialLevelLabel: String,
    userPreferences: UserPreferences,
    onNavigateBack: () -> Unit,
    onSelectTargetLevel: (JlptLevel) -> Unit,
    onNavigateToVocabulary: () -> Unit,
    onNavigateToKanji: () -> Unit,
    onNavigateToGrammar: () -> Unit,
    onNavigateToListening: () -> Unit,
    onNavigateToQuiz: (String) -> Unit,
    onPlayJapaneseAudio: (String, Boolean) -> Unit
) {
    val language = userPreferences.appLanguage
    var selectedLevel by remember(initialLevelLabel) {
        mutableStateOf(JlptLevel.fromLabel(initialLevelLabel))
    }
    // 0 = Level Dashboard, 1 = Reading Comprehension, 2 = Mock Examination
    var activeSubTab by remember { mutableIntStateOf(0) }

    val completedTopics = remember { mutableStateListOf<String>() }
    val lastMockScores = remember { mutableStateMapOf<JlptLevel, Pair<Int, Int>>() }

    val courseBundle = remember(selectedLevel) {
        JlptCourseCatalog.getCourseBundle(selectedLevel)
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
                        text = "JLPT ${selectedLevel.label} • ${
                            language.selectText(selectedLevel.titleBn, selectedLevel.titleEn)
                        }",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = NihongoTheme.colors.primaryText
                    )
                    Text(
                        text = language.selectText(
                            "পূর্ণাঙ্গ কোর্স, পাঠ অনুধাবন ও মক পরীক্ষা",
                            "Complete Course, Reading & Mock Exam"
                        ),
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

        // Top Level Selector Row (N5, N4, N3, N2, N1)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            JlptLevel.entries.forEach { level ->
                FilterChip(
                    selected = selectedLevel == level,
                    onClick = {
                        selectedLevel = level
                        onSelectTargetLevel(level)
                    },
                    label = {
                        Text(
                            text = "${level.label} (${language.selectText(level.titleBn, level.titleEn)})",
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NihongoTheme.colors.selectedContainer,
                        selectedLabelColor = NihongoTheme.colors.inkGreen
                    )
                )
            }
        }

        // Sub-Tab Switcher: Dashboard | Reading | Mock Exam
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = activeSubTab == 0,
                onClick = { activeSubTab = 0 },
                label = { Text(language.selectText("ড্যাশবোর্ড", "Dashboard")) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AutoStories,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = NihongoTheme.colors.selectedContainer,
                    selectedLabelColor = NihongoTheme.colors.inkGreen
                ),
                modifier = Modifier.weight(1f)
            )

            FilterChip(
                selected = activeSubTab == 1,
                onClick = { activeSubTab = 1 },
                label = { Text(language.selectText("পাঠ (Reading)", "Reading")) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = NihongoTheme.colors.selectedContainer,
                    selectedLabelColor = NihongoTheme.colors.inkGreen
                ),
                modifier = Modifier.weight(1f)
            )

            FilterChip(
                selected = activeSubTab == 2,
                onClick = { activeSubTab = 2 },
                label = { Text(language.selectText("মক পরীক্ষা", "Mock Exam")) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Assignment,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = NihongoTheme.colors.highlightContainer,
                    selectedLabelColor = NihongoTheme.colors.phoenixFlowerRed
                ),
                modifier = Modifier.weight(1f)
            )
        }

        when (activeSubTab) {
            0 -> JlptLevelDashboardTab(
                courseBundle = courseBundle,
                completedTopics = completedTopics,
                lastMockScore = lastMockScores[selectedLevel],
                language = language,
                onToggleTopicComplete = { topicKey ->
                    if (completedTopics.contains(topicKey)) {
                        completedTopics.remove(topicKey)
                    } else {
                        completedTopics.add(topicKey)
                    }
                },
                onNavigateToVocabulary = onNavigateToVocabulary,
                onNavigateToKanji = onNavigateToKanji,
                onNavigateToGrammar = onNavigateToGrammar,
                onOpenReadingTab = { activeSubTab = 1 },
                onNavigateToListening = onNavigateToListening,
                onNavigateToPracticeQuiz = { onNavigateToQuiz("multiple_choice") },
                onOpenMockExamTab = { activeSubTab = 2 }
            )

            1 -> JlptReadingComprehensionTab(
                passages = courseBundle.readingPassages,
                completedTopics = completedTopics,
                language = language,
                onMarkPassageCompleted = { passageId ->
                    if (!completedTopics.contains(passageId)) {
                        completedTopics.add(passageId)
                    }
                },
                onPlayJapaneseAudio = onPlayJapaneseAudio
            )

            2 -> JlptMockExamTab(
                courseBundle = courseBundle,
                language = language,
                onRecordMockResult = { score, total ->
                    lastMockScores[selectedLevel] = score to total
                    val examKey = "mock_done_${selectedLevel.label}"
                    if (!completedTopics.contains(examKey)) {
                        completedTopics.add(examKey)
                    }
                },
                onPlayJapaneseAudio = onPlayJapaneseAudio
            )
        }
    }
}

@Composable
private fun JlptLevelDashboardTab(
    courseBundle: JlptLevelCourseBundle,
    completedTopics: List<String>,
    lastMockScore: Pair<Int, Int>?,
    language: AppLanguage,
    onToggleTopicComplete: (String) -> Unit,
    onNavigateToVocabulary: () -> Unit,
    onNavigateToKanji: () -> Unit,
    onNavigateToGrammar: () -> Unit,
    onOpenReadingTab: () -> Unit,
    onNavigateToListening: () -> Unit,
    onNavigateToPracticeQuiz: () -> Unit,
    onOpenMockExamTab: () -> Unit
) {
    val level = courseBundle.level
    val levelVocabCount = LearningCatalog.vocabularyList.count { it.jlptLevel == level }
    val levelKanjiCount = LearningCatalog.kanjiList.count { it.jlptLevel == level }
    val levelGrammarCount = LearningCatalog.grammarList.count { it.jlptLevel == level }

    val moduleKeys = listOf(
        "vocab_${level.label}",
        "kanji_${level.label}",
        "grammar_${level.label}",
        "reading_${level.label}",
        "listening_${level.label}",
        "quiz_${level.label}",
        "mock_done_${level.label}"
    )
    val completedCount = moduleKeys.count { completedTopics.contains(it) }
    val progressRatio = completedCount.toFloat() / moduleKeys.size.toFloat()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Level Overview & Progress Hero Card
        item {
            NihongoCard(isSelected = true) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    JlptBadge(level = level)
                    Text(
                        text = language.selectText(
                            "অগ্রগতি: $completedCount / ${moduleKeys.size} মডিউল",
                            "Progress: $completedCount / ${moduleKeys.size} Modules"
                        ),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = NihongoTheme.colors.inkGreen
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "JLPT ${level.label} — ${language.selectText(level.titleBn, level.titleEn)}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = NihongoTheme.colors.primaryText
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = language.selectText(level.descriptionBn, level.descriptionEn),
                    style = MaterialTheme.typography.bodyMedium,
                    color = NihongoTheme.colors.secondaryText
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = language.selectText(
                        courseBundle.examDurationGuideBn,
                        courseBundle.examDurationGuideEn
                    ),
                    style = MaterialTheme.typography.labelLarge,
                    color = NihongoTheme.colors.inkGreen
                )

                Spacer(modifier = Modifier.height(10.dp))

                LinearProgressIndicator(
                    progress = { progressRatio },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(MaterialTheme.shapes.extraSmall),
                    color = NihongoTheme.colors.inkGreen,
                    trackColor = NihongoTheme.colors.borderOutline.copy(alpha = 0.35f)
                )

                if (lastMockScore != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = language.selectText(
                            "সর্বশেষ মক পরীক্ষার ফলাফল: ${lastMockScore.first} / ${lastMockScore.second}",
                            "Latest Mock Exam Result: ${lastMockScore.first} / ${lastMockScore.second}"
                        ),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = NihongoTheme.colors.phoenixFlowerRed
                    )
                }
            }
        }

        // 2. All 7 Required Course Components (Vocabulary, Kanji, Grammar, Reading, Listening, Quizzes, Mock Exam)
        item {
            Text(
                text = language.selectText(
                    "JLPT ${level.label} কোর্স মডিউলসমূহ",
                    "JLPT ${level.label} Course Modules"
                ),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = NihongoTheme.colors.primaryText
            )
        }

        // Module 1: Vocabulary
        item {
            CourseModuleRowCard(
                title = language.selectText(
                    "১. শব্দভাণ্ডার (Vocabulary • 語彙)",
                    "1. Vocabulary (語彙)"
                ),
                subtitle = language.selectText(
                    "লক্ষ্যমাত্রা: ${level.targetVocabCount}টি শব্দ (উদাহরণ ও অডিওসহ $levelVocabCount+ মূল পাঠ)",
                    "Target: ${level.targetVocabCount} words ($levelVocabCount+ core curated items with audio)"
                ),
                isCompleted = completedTopics.contains("vocab_${level.label}"),
                language = language,
                onCardClick = onNavigateToVocabulary,
                onToggleCompleted = { onToggleTopicComplete("vocab_${level.label}") }
            )
        }

        // Module 2: Kanji
        item {
            CourseModuleRowCard(
                title = language.selectText(
                    "২. কাঞ্জি (Kanji • 漢字)",
                    "2. Kanji Mastery (漢字)"
                ),
                subtitle = language.selectText(
                    "লক্ষ্যমাত্রা: ${level.targetKanjiCount}টি কাঞ্জি (অন'ইওমি, কুন'ইওমি ও স্ট্রোকসহ $levelKanjiCount+ পাঠ)",
                    "Target: ${level.targetKanjiCount} Kanji ($levelKanjiCount+ core items with verified strokes)"
                ),
                isCompleted = completedTopics.contains("kanji_${level.label}"),
                language = language,
                onCardClick = onNavigateToKanji,
                onToggleCompleted = { onToggleTopicComplete("kanji_${level.label}") }
            )
        }

        // Module 3: Grammar
        item {
            CourseModuleRowCard(
                title = language.selectText(
                    "৩. ব্যাকরণ (Grammar • 文法)",
                    "3. Grammar Patterns (文法)"
                ),
                subtitle = language.selectText(
                    "গঠন রীতি, বাংলা ও ইংরেজি ব্যাখ্যা এবং সাধারণ ভুল ($levelGrammarCount+ অধ্যায়)",
                    "Formation rules, bilingual explanations & common mistakes ($levelGrammarCount+ lessons)"
                ),
                isCompleted = completedTopics.contains("grammar_${level.label}"),
                language = language,
                onCardClick = onNavigateToGrammar,
                onToggleCompleted = { onToggleTopicComplete("grammar_${level.label}") }
            )
        }

        // Module 4: Reading Comprehension
        item {
            CourseModuleRowCard(
                title = language.selectText(
                    "৪. পাঠ অনুধাবন (Reading • 読解)",
                    "4. Reading Comprehension (読解)"
                ),
                subtitle = language.selectText(
                    "${courseBundle.readingPassages.size}টি অনুচ্ছেদ, হিরাগানা সহায়িকা ও অনুধাবন প্রশ্ন",
                    "${courseBundle.readingPassages.size} reading passages with furigana/romaji & questions"
                ),
                isCompleted = completedTopics.contains("reading_${level.label}"),
                language = language,
                onCardClick = onOpenReadingTab,
                onToggleCompleted = { onToggleTopicComplete("reading_${level.label}") }
            )
        }

        // Module 5: Listening
        item {
            CourseModuleRowCard(
                title = language.selectText(
                    "৫. শ্রবণ অনুশীলন (Listening • 聴解)",
                    "5. Listening Comprehension (聴解)"
                ),
                subtitle = language.selectText(
                    "স্বাভাবিক (1.0x) ও ধীর গতির (0.75x) জাপানি বাক্য শ্রবণ",
                    "Normal (1.0x) and slow (0.75x) Japanese sentence listening drills"
                ),
                isCompleted = completedTopics.contains("listening_${level.label}"),
                language = language,
                onCardClick = onNavigateToListening,
                onToggleCompleted = { onToggleTopicComplete("listening_${level.label}") }
            )
        }

        // Module 6: Practice Quizzes
        item {
            CourseModuleRowCard(
                title = language.selectText(
                    "৬. অনুশীলন কুইজ (Practice Quizzes)",
                    "6. Practice Quizzes"
                ),
                subtitle = language.selectText(
                    "দ্রুত যাচাই ও শূন্যস্থান পূরণ কুইজ",
                    "Interactive topic quizzes and sentence building"
                ),
                isCompleted = completedTopics.contains("quiz_${level.label}"),
                language = language,
                onCardClick = onNavigateToPracticeQuiz,
                onToggleCompleted = { onToggleTopicComplete("quiz_${level.label}") }
            )
        }

        // Module 7: Mock Examination
        item {
            CourseModuleRowCard(
                title = language.selectText(
                    "৭. পূর্ণাঙ্গ মক পরীক্ষা (Mock Examination)",
                    "7. Practice Mock Examination"
                ),
                subtitle = language.selectText(
                    "শব্দ, কাঞ্জি, ব্যাকরণ ও শ্রবণের সমন্বিত অনুশীলন পরীক্ষা",
                    "Integrated Vocabulary, Kanji, Grammar & Listening mock test"
                ),
                isCompleted = completedTopics.contains("mock_done_${level.label}"),
                language = language,
                onCardClick = onOpenMockExamTab,
                onToggleCompleted = { onToggleTopicComplete("mock_done_${level.label}") }
            )
        }

        // 3. Exam Preparation Tips Card (Section 13 requirement)
        item {
            NihongoCard {
                Text(
                    text = language.selectText(
                        "JLPT ${level.label} পরীক্ষার প্রস্তুতি নির্দেশিকা (Exam Preparation)",
                        "JLPT ${level.label} Exam Preparation Strategy"
                    ),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = NihongoTheme.colors.inkGreen
                )
                Spacer(modifier = Modifier.height(8.dp))
                val tips = if (language == AppLanguage.BENGALI) {
                    courseBundle.preparationTipsBn
                } else {
                    courseBundle.preparationTipsEn
                }
                tips.forEachIndexed { idx, tip ->
                    Text(
                        text = "• $tip",
                        style = MaterialTheme.typography.bodyMedium,
                        color = NihongoTheme.colors.primaryText,
                        modifier = Modifier.padding(vertical = 3.dp)
                    )
                }
            }
        }

        // 4. Mandatory Official JLPT Distinction & No-Guarantee Disclaimer (Section 13 requirement)
        item {
            NihongoCard(isHighlighted = true) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = NihongoTheme.colors.phoenixFlowerRed
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = language.selectText(
                            "গুরুত্বপূর্ণ শিক্ষাগত বিজ্ঞপ্তি (JLPT Disclaimer)",
                            "Important Educational Notice (JLPT Disclaimer)"
                        ),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = NihongoTheme.colors.phoenixFlowerRed
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = language.selectText(
                        "এই অ্যাপের অনুশীলন প্রশ্ন ও মক পরীক্ষাগুলো স্বতন্ত্রভাবে তৈরি করা শিক্ষা সহায়ক উপকরণ—এগুলো Japan Foundation বা JEES-এর অফিশিয়াল JLPT প্রশ্নপত্র নয়। এই অ্যাপের কোর্স সম্পন্ন করলেই অফিশিয়াল JLPT পরীক্ষায় উত্তীর্ণ হওয়ার শতভাগ নিশ্চয়তা দেওয়া হয় না।",
                        "All practice quizzes and mock examinations in NIHONGO are independently created study aids and are distinct from official JLPT examinations administered by the Japan Foundation and JEES. Completing this app does not guarantee passing the official JLPT."
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = NihongoTheme.colors.primaryText
                )
            }
        }
    }
}

@Composable
private fun CourseModuleRowCard(
    title: String,
    subtitle: String,
    isCompleted: Boolean,
    language: AppLanguage,
    onCardClick: () -> Unit,
    onToggleCompleted: () -> Unit
) {
    NihongoCard(
        isSelected = isCompleted,
        onClick = onCardClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = NihongoTheme.colors.primaryText
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = NihongoTheme.colors.secondaryText
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = onToggleCompleted) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = language.selectText("সম্পন্ন চিহ্নিত করুন", "Toggle complete"),
                    tint = if (isCompleted) {
                        NihongoTheme.colors.inkGreen
                    } else {
                        NihongoTheme.colors.secondaryText.copy(alpha = 0.4f)
                    }
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

@Composable
private fun JlptReadingComprehensionTab(
    passages: List<JlptReadingPassage>,
    completedTopics: List<String>,
    language: AppLanguage,
    onMarkPassageCompleted: (String) -> Unit,
    onPlayJapaneseAudio: (String, Boolean) -> Unit
) {
    var showReadingAid by remember { mutableStateOf(true) }
    var showTranslation by remember { mutableStateOf(false) }
    val selectedAnswers = remember { mutableStateMapOf<String, Int>() }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FilterChip(
                    selected = showReadingAid,
                    onClick = { showReadingAid = !showReadingAid },
                    label = {
                        Text(language.selectText("হিরাগানা ও রোমাজি দেখুন", "Show Kana & Romaji"))
                    }
                )
                FilterChip(
                    selected = showTranslation,
                    onClick = { showTranslation = !showTranslation },
                    label = {
                        Text(language.selectText("অনুবাদ দেখুন", "Show Translation"))
                    }
                )
            }
        }

        items(
            items = passages,
            key = { it.id }
        ) { passage ->
            val chosenOption = selectedAnswers[passage.id]
            val isCompleted = completedTopics.contains(passage.id)

            NihongoCard(isSelected = isCompleted) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        JlptBadge(level = passage.jlptLevel)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = passage.titleJp,
                            style = MaterialTheme.typography.headlineMedium,
                            color = NihongoTheme.colors.primaryText
                        )
                        Text(
                            text = language.selectText(passage.titleBn, passage.titleEn),
                            style = MaterialTheme.typography.titleSmall,
                            color = NihongoTheme.colors.inkGreen
                        )
                    }

                    AudioSpeakerButton(
                        japaneseText = passage.passageJp,
                        onPlayNormal = { onPlayJapaneseAudio(passage.passageJp, false) },
                        onPlaySlow = { onPlayJapaneseAudio(passage.passageJp, true) },
                        language = language
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Main Japanese Passage
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = NihongoTheme.colors.elevatedSurface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = passage.passageJp,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                            color = NihongoTheme.colors.primaryText
                        )

                        if (showReadingAid) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = passage.passageHiragana,
                                style = MaterialTheme.typography.bodySmall,
                                color = NihongoTheme.colors.inkGreen
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = passage.passageRomaji,
                                style = MaterialTheme.typography.bodySmall,
                                color = NihongoTheme.colors.secondaryText
                            )
                        }

                        if (showTranslation) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 10.dp),
                                color = NihongoTheme.colors.borderOutline.copy(alpha = 0.4f)
                            )
                            BilingualMeaningBlock(
                                bengaliMeaning = passage.translationBn,
                                englishMeaning = passage.translationEn,
                                primaryLanguage = language
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Comprehension Question
                Text(
                    text = "問題: ${passage.questionJp}",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = NihongoTheme.colors.primaryText
                )
                Text(
                    text = language.selectText(passage.questionBn, passage.questionEn),
                    style = MaterialTheme.typography.bodyMedium,
                    color = NihongoTheme.colors.secondaryText
                )

                Spacer(modifier = Modifier.height(8.dp))

                passage.options.forEachIndexed { index, optionText ->
                    val isSelected = chosenOption == index
                    val isCorrect = index == passage.correctOptionIndex
                    NihongoCard(
                        modifier = Modifier.padding(vertical = 4.dp),
                        isSelected = chosenOption != null && isCorrect,
                        isHighlighted = isSelected && !isCorrect,
                        contentPadding = 12.dp,
                        onClick = {
                            selectedAnswers[passage.id] = index
                            if (isCorrect) {
                                onMarkPassageCompleted(passage.id)
                            }
                        }
                    ) {
                        Text(
                            text = "${index + 1}. $optionText",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = NihongoTheme.colors.primaryText
                        )
                    }
                }

                if (chosenOption != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = language.selectText(passage.explanationBn, passage.explanationEn),
                        style = MaterialTheme.typography.bodySmall,
                        color = NihongoTheme.colors.inkGreen
                    )
                }
            }
        }
    }
}

@Composable
private fun JlptMockExamTab(
    courseBundle: JlptLevelCourseBundle,
    language: AppLanguage,
    onRecordMockResult: (Int, Int) -> Unit,
    onPlayJapaneseAudio: (String, Boolean) -> Unit
) {
    var isReviewingMistakesOnly by remember(courseBundle.level) { mutableStateOf(false) }
    val missedQuestionIds = remember(courseBundle.level) { mutableStateListOf<String>() }

    val activeQuestions = remember(courseBundle.level, isReviewingMistakesOnly, missedQuestionIds.size) {
        if (isReviewingMistakesOnly) {
            courseBundle.mockExamQuestions.filter { missedQuestionIds.contains(it.id) }
                .ifEmpty { courseBundle.mockExamQuestions }
        } else {
            courseBundle.mockExamQuestions
        }
    }

    var currentIndex by remember(courseBundle.level, isReviewingMistakesOnly) { mutableIntStateOf(0) }
    var selectedOption by remember(currentIndex, courseBundle.level, isReviewingMistakesOnly) {
        mutableStateOf<Int?>(null)
    }
    var correctScore by remember(courseBundle.level, isReviewingMistakesOnly) { mutableIntStateOf(0) }
    var isFinished by remember(courseBundle.level, isReviewingMistakesOnly) { mutableStateOf(false) }

    if (isFinished) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                NihongoCard(isSelected = true) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = NihongoTheme.colors.phoenixFlowerRed
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = language.selectText(
                                "JLPT ${courseBundle.level.label} অনুশীলন মক পরীক্ষার ফলাফল",
                                "JLPT ${courseBundle.level.label} Practice Mock Exam Results"
                            ),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = NihongoTheme.colors.primaryText
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = language.selectText(
                            "প্রাপ্ত স্কোর: $correctScore / ${activeQuestions.size}",
                            "Score Achieved: $correctScore / ${activeQuestions.size}"
                        ),
                        style = MaterialTheme.typography.headlineMedium,
                        color = NihongoTheme.colors.inkGreen
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = language.selectText(
                            "দ্রষ্টব্য: এটি অ্যাপ-নির্মিত একটি অনুশীলন পরীক্ষা, অফিশিয়াল JLPT পরীক্ষা নয়।",
                            "Note: This is an app-created practice test, not an official JLPT examination."
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = NihongoTheme.colors.secondaryText
                    )
                }
            }

            item {
                NihongoPrimaryButton(
                    text = language.selectText("মক পরীক্ষা পুনরায় দিন", "Retake Full Mock Exam"),
                    onClick = {
                        isReviewingMistakesOnly = false
                        currentIndex = 0
                        correctScore = 0
                        isFinished = false
                    },
                    leadingIcon = Icons.Default.Refresh,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (missedQuestionIds.isNotEmpty()) {
                item {
                    NihongoAccentButton(
                        text = language.selectText(
                            "ভুল হওয়া প্রশ্নগুলো রিভিশন করুন (${missedQuestionIds.size})",
                            "Review Missed Questions (${missedQuestionIds.size})"
                        ),
                        onClick = {
                            isReviewingMistakesOnly = true
                            currentIndex = 0
                            correctScore = 0
                            isFinished = false
                        },
                        leadingIcon = Icons.Default.Replay,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    } else {
        val question: JlptMockExamQuestion = activeQuestions[currentIndex]

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // App-Created Mock Exam Banner
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = NihongoTheme.colors.selectedContainer
                    ) {
                        Text(
                            text = "${question.section.badgeJp} • ${question.section.title(language)}",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = NihongoTheme.colors.inkGreen,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                    Text(
                        text = "${currentIndex + 1} / ${activeQuestions.size}",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = NihongoTheme.colors.primaryText
                    )
                }
            }

            item {
                NihongoCard(isSelected = true) {
                    Text(
                        text = language.selectText(
                            question.promptInstructionBn,
                            question.promptInstructionEn
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = NihongoTheme.colors.inkGreen
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = question.promptJp,
                            style = MaterialTheme.typography.headlineMedium,
                            color = NihongoTheme.colors.primaryText,
                            modifier = Modifier.weight(1f)
                        )
                        if (question.audioToSpeak.isNotBlank()) {
                            Spacer(modifier = Modifier.width(8.dp))
                            AudioSpeakerButton(
                                japaneseText = question.audioToSpeak,
                                onPlayNormal = { onPlayJapaneseAudio(question.audioToSpeak, false) },
                                onPlaySlow = { onPlayJapaneseAudio(question.audioToSpeak, true) },
                                language = language
                            )
                        }
                    }
                }
            }

            items(question.options.size) { index ->
                val optionText = question.options[index]
                val isSelected = selectedOption == index
                val isCorrect = index == question.correctOptionIndex

                NihongoCard(
                    isSelected = selectedOption != null && isCorrect,
                    isHighlighted = isSelected && !isCorrect,
                    onClick = {
                        if (selectedOption == null) {
                            selectedOption = index
                            if (isCorrect) {
                                correctScore++
                                missedQuestionIds.remove(question.id)
                            } else if (!missedQuestionIds.contains(question.id)) {
                                missedQuestionIds.add(question.id)
                            }
                            if (question.audioToSpeak.isNotBlank()) {
                                onPlayJapaneseAudio(question.audioToSpeak, false)
                            }
                        }
                    }
                ) {
                    Text(
                        text = "${index + 1}.  $optionText",
                        style = MaterialTheme.typography.titleMedium,
                        color = NihongoTheme.colors.primaryText
                    )
                }
            }

            if (selectedOption != null) {
                item {
                    NihongoCard {
                        Text(
                            text = language.selectText(question.explanationBn, question.explanationEn),
                            style = MaterialTheme.typography.bodyMedium,
                            color = NihongoTheme.colors.inkGreen
                        )
                    }
                }

                item {
                    NihongoPrimaryButton(
                        text = if (currentIndex < activeQuestions.lastIndex) {
                            language.selectText("পরবর্তী প্রশ্ন", "Next Question")
                        } else {
                            language.selectText("পরীক্ষা সম্পন্ন করুন", "Finish Mock Exam")
                        },
                        onClick = {
                            if (currentIndex < activeQuestions.lastIndex) {
                                currentIndex++
                            } else {
                                onRecordMockResult(correctScore, activeQuestions.size)
                                isFinished = true
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
