package com.nihongo.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class GrammarExample(
    val japanese: String,
    val hiraganaReading: String,
    val romaji: String,
    val bengali: String,
    val english: String
)

@Serializable
data class GrammarPracticeQuestion(
    val promptJp: String,
    val promptBn: String,
    val promptEn: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanationBn: String,
    val explanationEn: String
)

/**
 * Structured Japanese Grammar lesson model (Section 09) from JLPT N5 to N1.
 */
@Serializable
data class GrammarLesson(
    val id: String,
    val pattern: String,
    val jlptLevel: JlptLevel,
    val titleBn: String,
    val titleEn: String,
    val explanationBn: String,
    val explanationEn: String,
    val formationRulesBn: String,
    val formationRulesEn: String,
    val examples: List<GrammarExample>,
    val commonMistakesBn: String,
    val commonMistakesEn: String,
    val practiceQuestion: GrammarPracticeQuestion,
    val relatedGrammarPatterns: List<String>
)
