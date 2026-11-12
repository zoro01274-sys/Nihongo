package com.nihongo.app.ui.screens.practice

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nihongo.app.data.local.datastore.UserPreferences
import com.nihongo.app.domain.model.QuizMode
import com.nihongo.app.ui.components.NihongoCard
import com.nihongo.app.ui.theme.NihongoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeScreen(
    userPreferences: UserPreferences,
    onNavigateToFlashcards: () -> Unit,
    onNavigateToQuiz: (String) -> Unit,
    onNavigateToSpeaking: () -> Unit
) {
    val language = userPreferences.appLanguage

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = language.selectText("অনুশীলন • প্র্যাকটিস হাব", "Practice • Assessment Hub"),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = NihongoTheme.colors.primaryText
                )
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
            // 1. Spaced Repetition Flashcards
            item {
                NihongoCard(
                    isSelected = true,
                    onClick = onNavigateToFlashcards
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
                                shape = MaterialTheme.shapes.small,
                                color = NihongoTheme.colors.inkGreen,
                                modifier = Modifier.size(46.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Style,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = language.selectText(
                                        "স্পেসড রিপিটিশন ফ্ল্যাশকার্ড (SRS)",
                                        "Spaced Repetition Flashcards (SRS)"
                                    ),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = NihongoTheme.colors.primaryText
                                )
                                Text(
                                    text = language.selectText(
                                        "SM-2 অ্যালগরিদম ভিত্তিক মেমোরি কার্ড অনুশীলন",
                                        "Adaptive SM-2 review schedule with audio"
                                    ),
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

            // 2. All 9 Quiz Types Grid (Section 14)
            item {
                Text(
                    text = language.selectText("৯ ধরনের ইন্টারঅ্যাক্টিভ কুইজ", "All 9 Interactive Quiz Modes"),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = NihongoTheme.colors.primaryText
                )
            }

            // Row 1: JP to Bengali & JP to English
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    NihongoCard(
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToQuiz(QuizMode.JP_TO_BENGALI.code) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Quiz,
                            contentDescription = null,
                            tint = NihongoTheme.colors.inkGreen,
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = language.selectText("জাপানি ➔ বাংলা", "JP ➔ Bengali"),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = NihongoTheme.colors.primaryText
                        )
                        Text(
                            text = language.selectText("বাংলা অর্থ যাচাই", "Bengali meaning quiz"),
                            style = MaterialTheme.typography.bodySmall,
                            color = NihongoTheme.colors.secondaryText
                        )
                    }

                    NihongoCard(
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToQuiz(QuizMode.JP_TO_ENGLISH.code) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Quiz,
                            contentDescription = null,
                            tint = NihongoTheme.colors.inkGreen,
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = language.selectText("জাপানি ➔ ইংরেজি", "JP ➔ English"),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = NihongoTheme.colors.primaryText
                        )
                        Text(
                            text = language.selectText("ইংরেজি অর্থ যাচাই", "English meaning quiz"),
                            style = MaterialTheme.typography.bodySmall,
                            color = NihongoTheme.colors.secondaryText
                        )
                    }
                }
            }

            // Row 2: Kanji Recognition & Kana Recognition
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    NihongoCard(
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToQuiz(QuizMode.KANJI_RECOGNITION.code) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.HistoryEdu,
                            contentDescription = null,
                            tint = NihongoTheme.colors.phoenixFlowerRed,
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = language.selectText("কাঞ্জি যাচাই", "Kanji Quiz"),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = NihongoTheme.colors.primaryText
                        )
                        Text(
                            text = language.selectText("পাঠ ও অর্থ পরীক্ষা", "Readings & meanings"),
                            style = MaterialTheme.typography.bodySmall,
                            color = NihongoTheme.colors.secondaryText
                        )
                    }

                    NihongoCard(
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToQuiz(QuizMode.KANA_RECOGNITION.code) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Assignment,
                            contentDescription = null,
                            tint = NihongoTheme.colors.inkGreen,
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = language.selectText("কানা যাচাই", "Kana Quiz"),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = NihongoTheme.colors.primaryText
                        )
                        Text(
                            text = language.selectText("হিরাগানা ও কাতাকানা", "Hiragana & Katakana"),
                            style = MaterialTheme.typography.bodySmall,
                            color = NihongoTheme.colors.secondaryText
                        )
                    }
                }
            }

            // Row 3: Match Words & Sentence Ordering
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    NihongoCard(
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToQuiz(QuizMode.MATCH_WORDS.code) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.CompareArrows,
                            contentDescription = null,
                            tint = NihongoTheme.colors.inkGreen,
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = language.selectText("শব্দ মিলকরণ", "Match Words"),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = NihongoTheme.colors.primaryText
                        )
                        Text(
                            text = language.selectText("শব্দ ও অর্থের জোড়া", "Pair words & meanings"),
                            style = MaterialTheme.typography.bodySmall,
                            color = NihongoTheme.colors.secondaryText
                        )
                    }

                    NihongoCard(
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToQuiz(QuizMode.SENTENCE_ORDERING.code) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatListNumbered,
                            contentDescription = null,
                            tint = NihongoTheme.colors.ancientWoodBrown,
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = language.selectText("বাক্য সাজানো", "Sentence Order"),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = NihongoTheme.colors.primaryText
                        )
                        Text(
                            text = language.selectText("সঠিক ক্রমে বাক্য গঠন", "Assemble word tokens"),
                            style = MaterialTheme.typography.bodySmall,
                            color = NihongoTheme.colors.secondaryText
                        )
                    }
                }
            }

            // Row 4: Fill in the Blanks & Listening Quizzes
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    NihongoCard(
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToQuiz(QuizMode.FILL_IN_BLANKS.code) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.EditNote,
                            contentDescription = null,
                            tint = NihongoTheme.colors.inkGreen,
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = language.selectText("শূন্যস্থান পূরণ", "Fill in Blanks"),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = NihongoTheme.colors.primaryText
                        )
                        Text(
                            text = language.selectText("কণা ও ক্রিয়ার রূপ", "Particles & verb forms"),
                            style = MaterialTheme.typography.bodySmall,
                            color = NihongoTheme.colors.secondaryText
                        )
                    }

                    NihongoCard(
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToQuiz(QuizMode.LISTENING_QUIZ.code) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Hearing,
                            contentDescription = null,
                            tint = NihongoTheme.colors.phoenixFlowerRed,
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = language.selectText("শ্রবণ কুইজ", "Listening Quiz"),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = NihongoTheme.colors.primaryText
                        )
                        Text(
                            text = language.selectText("অডিও শুনে উত্তর দিন", "Audio comprehension"),
                            style = MaterialTheme.typography.bodySmall,
                            color = NihongoTheme.colors.secondaryText
                        )
                    }
                }
            }

            // 3. Speaking & Pronunciation Practice (Section 11)
            item {
                NihongoCard(onClick = onNavigateToSpeaking) {
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
                                color = NihongoTheme.colors.highlightContainer,
                                modifier = Modifier.size(44.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = null,
                                    tint = NihongoTheme.colors.phoenixFlowerRed,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = language.selectText(
                                        "উচ্চারণ ও কথন অনুশীলন (Speaking)",
                                        "Speaking & Pronunciation Practice"
                                    ),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = NihongoTheme.colors.primaryText
                                )
                                Text(
                                    text = language.selectText(
                                        "শুনুন এবং মাইক্রোফোনে জাপানি শব্দ ও বাক্য বলুন",
                                        "Listen and repeat words and sentences with ja-JP recognition"
                                    ),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = NihongoTheme.colors.secondaryText
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = NihongoTheme.colors.phoenixFlowerRed
                        )
                    }
                }
            }

            // 4. Mistake Review Queue
            item {
                NihongoCard(onClick = { onNavigateToQuiz(QuizMode.MISTAKE_REVIEW.code) }) {
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
                                color = NihongoTheme.colors.elevatedSurface,
                                modifier = Modifier.size(44.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Replay,
                                    contentDescription = null,
                                    tint = NihongoTheme.colors.ancientWoodBrown,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = language.selectText("ভুল উত্তর রিভিশন", "Mistake Review Queue"),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = NihongoTheme.colors.primaryText
                                )
                                Text(
                                    text = language.selectText(
                                        "কুইজে ভুল হওয়া প্রশ্নগুলো পুনরায় অনুশীলন করুন",
                                        "Re-practice questions you missed in quizzes"
                                    ),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = NihongoTheme.colors.secondaryText
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = NihongoTheme.colors.primaryText
                        )
                    }
                }
            }
        }
    }
}
