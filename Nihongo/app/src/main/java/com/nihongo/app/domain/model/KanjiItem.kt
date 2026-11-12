package com.nihongo.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class KanjiVocabExample(
    val wordJp: String,
    val readingKana: String,
    val romaji: String,
    val meaningBn: String,
    val meaningEn: String
)

/**
 * Complete Kanji model fulfilling Section 07 requirements:
 * Kanji character, Bengali meaning, English meaning, On'yomi, Kun'yomi,
 * Romaji readings, Example vocabulary, Example sentences, Audio support,
 * verified stroke count, stroke-order instructions, JLPT level, and related Kanji.
 */
@Serializable
data class KanjiItem(
    val id: String,
    val character: String,
    val jlptLevel: JlptLevel,
    val meaningBn: String,
    val meaningEn: String,
    val onyomiKana: String,
    val kunyomiKana: String,
    val romajiReadings: String,
    val strokeCount: Int,
    val strokeDirectionsBn: String,
    val strokeDirectionsEn: String,
    val exampleVocabulary: List<KanjiVocabExample>,
    val exampleSentenceJp: String,
    val exampleSentenceReading: String,
    val exampleSentenceRomaji: String,
    val exampleSentenceBn: String,
    val exampleSentenceEn: String,
    val relatedKanji: List<String>
)
