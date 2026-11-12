package com.nihongo.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class KanaScript(
    val titleEn: String,
    val titleBn: String
) {
    HIRAGANA(
        titleEn = "Hiragana",
        titleBn = "হিরাগানা"
    ),
    KATAKANA(
        titleEn = "Katakana",
        titleBn = "কাতাকানা"
    );

    fun title(language: AppLanguage): String = language.selectText(titleBn, titleEn)
}

@Serializable
enum class KanaCategory(
    val labelEn: String,
    val labelBn: String,
    val descriptionEn: String,
    val descriptionBn: String
) {
    BASIC(
        labelEn = "46 Basic",
        labelBn = "মূল ৪৬টি",
        descriptionEn = "Foundational Gojūon characters (a-row to wa/n).",
        descriptionBn = "জাপানি বর্ণমালার ৪৬টি মৌলিক বর্ণ (আ-সারি থেকে ওয়া/ন পর্যন্ত)।"
    ),
    DAKUTEN(
        labelEn = "Dakuten (゛)",
        labelBn = "ডাকুতেন (゛)",
        descriptionEn = "Voiced consonants created by adding two small strokes (g, z, d, b sounds).",
        descriptionBn = "বর্ণের উপরে দুটি ছোট দাগ (゛) যোগে গঠিত ঘোষ ধ্বনি (গ, জ, দ, ব সারি)।"
    ),
    HANDAKUTEN(
        labelEn = "Handakuten (゜)",
        labelBn = "হান্দাকুতেন (゜)",
        descriptionEn = "P-row sounds created by adding a small circle to the H-row.",
        descriptionBn = "হ-সারির বর্ণের উপরে ছোট বৃত্ত (゜) যোগে গঠিত প-ধ্বনি (পা, পি, পু, পে, পো)।"
    ),
    YOON(
        labelEn = "Yōon (Combined)",
        labelBn = "ইয়োন (যুক্তবর্ণ)",
        descriptionEn = "Contracted syllables combining an i-column character with a small ya, yu, or yo.",
        descriptionBn = "ই-কলামের বর্ণের সাথে ছোট ゃ, ゅ, ょ (বা ャ, ュ, ョ) যুক্ত হয়ে গঠিত সংযুক্ত ধ্বনি।"
    ),
    SPECIAL_AND_EXTENDED(
        labelEn = "Special & Foreign",
        labelBn = "বিশেষ ও বিদেশি",
        descriptionEn = "Small tsu (double consonant), long vowels, and foreign word combinations.",
        descriptionBn = "ছোট っ/ッ (দ্বিত্ব ব্যঞ্জন), দীর্ঘ স্বরধ্বনি এবং বিদেশি শব্দের বিশেষ কাতাকানা যুক্তবর্ণ।"
    );

    fun label(language: AppLanguage): String = language.selectText(labelBn, labelEn)
    fun description(language: AppLanguage): String = language.selectText(descriptionBn, descriptionEn)
}

@Serializable
data class KanaItem(
    val id: String,
    val script: KanaScript,
    val category: KanaCategory,
    val rowGroup: String,
    val character: String,
    val romaji: String,
    val pronunciationBn: String,
    val explanationEn: String,
    val strokeCount: Int,
    val strokeDirectionsBn: String,
    val strokeDirectionsEn: String,
    val exampleWordJp: String,
    val exampleWordReading: String,
    val exampleWordRomaji: String,
    val exampleMeaningBn: String,
    val exampleMeaningEn: String
)
