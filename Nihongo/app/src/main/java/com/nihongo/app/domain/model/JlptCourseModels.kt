package com.nihongo.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents the तीन (3) core test sections of a JLPT practice examination.
 */
@Serializable
enum class MockExamSection(
    val badgeJp: String,
    val titleBn: String,
    val titleEn: String
) {
    VOCAB_KANJI(
        badgeJp = "文字・語彙",
        titleBn = "শব্দভাণ্ডার ও কাঞ্জি",
        titleEn = "Vocabulary & Kanji"
    ),
    GRAMMAR_READING(
        badgeJp = "文法・読解",
        titleBn = "ব্যাকরণ ও পাঠ অনুধাবন",
        titleEn = "Grammar & Reading"
    ),
    LISTENING(
        badgeJp = "聴解",
        titleBn = "শ্রবণ অনুধাবন",
        titleEn = "Listening Comprehension"
    );

    fun title(language: AppLanguage): String = language.selectText(titleBn, titleEn)
}

/**
 * Reading comprehension passage (`読解` - Dokkai) for JLPT N5 through N1.
 */
@Serializable
data class JlptReadingPassage(
    val id: String,
    val jlptLevel: JlptLevel,
    val titleJp: String,
    val titleBn: String,
    val titleEn: String,
    val passageJp: String,
    val passageHiragana: String,
    val passageRomaji: String,
    val translationBn: String,
    val translationEn: String,
    val questionJp: String,
    val questionBn: String,
    val questionEn: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanationBn: String,
    val explanationEn: String
)

/**
 * App-created practice question for the JLPT N5–N1 Mock Examination.
 */
@Serializable
data class JlptMockExamQuestion(
    val id: String,
    val jlptLevel: JlptLevel,
    val section: MockExamSection,
    val promptJp: String,
    val promptInstructionBn: String,
    val promptInstructionEn: String,
    val audioToSpeak: String = "",
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanationBn: String,
    val explanationEn: String
)

/**
 * Complete course bundle for a single JLPT level (N5 to N1).
 */
@Serializable
data class JlptLevelCourseBundle(
    val level: JlptLevel,
    val examDurationGuideBn: String,
    val examDurationGuideEn: String,
    val preparationTipsBn: List<String>,
    val preparationTipsEn: List<String>,
    val readingPassages: List<JlptReadingPassage>,
    val mockExamQuestions: List<JlptMockExamQuestion>
)
