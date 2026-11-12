package com.nihongo.app.domain.model

import kotlinx.serialization.Serializable

/**
 * All 9 quiz types required in Section 14 plus Mistake Review.
 */
@Serializable
enum class QuizMode(
    val code: String,
    val titleBn: String,
    val titleEn: String,
    val subtitleBn: String,
    val subtitleEn: String
) {
    MULTIPLE_CHOICE(
        code = "multiple_choice",
        titleBn = "বহুনির্বাচনী কুইজ",
        titleEn = "Multiple Choice",
        subtitleBn = "শব্দ, কাঞ্জি ও ব্যাকরণের সমন্বিত প্রশ্ন",
        subtitleEn = "Mixed vocabulary, Kanji, and grammar questions"
    ),
    JP_TO_BENGALI(
        code = "jp_to_bn",
        titleBn = "জাপানি থেকে বাংলা",
        titleEn = "Japanese to Bengali",
        subtitleBn = "জাপানি শব্দের সঠিক বাংলা অর্থ নির্বাচন",
        subtitleEn = "Select the accurate Bengali meaning for Japanese words"
    ),
    JP_TO_ENGLISH(
        code = "jp_to_en",
        titleBn = "জাপানি থেকে ইংরেজি",
        titleEn = "Japanese to English",
        subtitleBn = "জাপানি শব্দের সঠিক ইংরেজি অর্থ নির্বাচন",
        subtitleEn = "Select the accurate English meaning for Japanese words"
    ),
    MATCH_WORDS(
        code = "match_words",
        titleBn = "শব্দ মিলকরণ (Match Words)",
        titleEn = "Match Words",
        subtitleBn = "জাপানি শব্দের সাথে অর্থের জোড়া মেলান",
        subtitleEn = "Pair Japanese words with their corresponding meanings"
    ),
    KANA_RECOGNITION(
        code = "kana",
        titleBn = "কানা বর্ণ যাচাই",
        titleEn = "Kana Recognition",
        subtitleBn = "হিরাগানা ও কাতাকানা বর্ণ ও উচ্চারণ শনাক্তকরণ",
        subtitleEn = "Identify Hiragana and Katakana characters and readings"
    ),
    KANJI_RECOGNITION(
        code = "kanji",
        titleBn = "কাঞ্জি যাচাই কুইজ",
        titleEn = "Kanji Recognition",
        subtitleBn = "কাঞ্জির অন'ইওমি, কুন'ইওমি ও অর্থ পরীক্ষা",
        subtitleEn = "Test Kanji On'yomi, Kun'yomi readings, and meanings"
    ),
    FILL_IN_BLANKS(
        code = "fill_blanks",
        titleBn = "শূন্যস্থান পূরণ",
        titleEn = "Fill in the Blanks",
        subtitleBn = "জাপানি বাক্যে সঠিক কণা বা শব্দ বসান",
        subtitleEn = "Complete the Japanese sentence with the missing word or particle"
    ),
    LISTENING_QUIZ(
        code = "listening",
        titleBn = "শ্রবণ অনুধাবন কুইজ",
        titleEn = "Listening Quizzes",
        subtitleBn = "জাপানি অডিও শুনে সঠিক শব্দ, বর্ণ বা অর্থ চয়ন",
        subtitleEn = "Listen to Japanese audio and choose the right word or meaning"
    ),
    SENTENCE_ORDERING(
        code = "sentence_order",
        titleBn = "বাক্য সাজানো (Sentence Ordering)",
        titleEn = "Sentence Ordering",
        subtitleBn = "এলোমেলো জাপানি শব্দগুলো সঠিক ক্রমে সাজিয়ে বাক্য গঠন",
        subtitleEn = "Arrange scrambled Japanese tokens into a grammatical sentence"
    ),
    MISTAKE_REVIEW(
        code = "mistakes",
        titleBn = "ভুল উত্তর রিভিশন",
        titleEn = "Mistake Review",
        subtitleBn = "পূর্বে ভুল হওয়া প্রশ্নগুলো পুনরায় অনুশীলন",
        subtitleEn = "Practice questions you previously answered incorrectly"
    );

    fun title(language: AppLanguage): String = language.selectText(titleBn, titleEn)
    fun subtitle(language: AppLanguage): String = language.selectText(subtitleBn, subtitleEn)

    companion object {
        fun fromCode(code: String?): QuizMode {
            return entries.firstOrNull { it.code.equals(code, ignoreCase = true) }
                ?: MULTIPLE_CHOICE
        }
    }
}

@Serializable
data class WordMatchPair(
    val pairId: String,
    val japaneseText: String,
    val readingRomaji: String,
    val meaningBn: String,
    val meaningEn: String
)

/**
 * Unified question model supporting standard options, audio-first listening prompts,
 * scrambled sentence tokens, and word-matching pairs.
 */
@Serializable
data class QuizQuestion(
    val id: String,
    val mode: QuizMode,
    val jlptLevel: JlptLevel,
    val promptJapanese: String,
    val promptSubtext: String = "",
    val questionInstructionBn: String,
    val questionInstructionEn: String,
    val audioTextToSpeak: String = "",
    val hideJapanesePromptUntilAnswered: Boolean = false,
    val options: List<String> = emptyList(),
    val correctOptionIndex: Int = 0,
    val sentenceTokens: List<String> = emptyList(),
    val correctSentenceOrder: List<String> = emptyList(),
    val matchingPairs: List<WordMatchPair> = emptyList(),
    val explanationBn: String,
    val explanationEn: String
)
