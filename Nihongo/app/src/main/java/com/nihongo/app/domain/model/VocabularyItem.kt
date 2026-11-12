package com.nihongo.app.domain.model

import kotlinx.serialization.Serializable

/**
 * All 17 vocabulary categories required by Section 08.
 */
@Serializable
enum class VocabCategory(
    val id: String,
    val labelEn: String,
    val labelBn: String
) {
    GREETINGS("greetings", "Greetings", "অভিবাদন"),
    NUMBERS("numbers", "Numbers", "সংখ্যা"),
    FAMILY("family", "Family", "পরিবার"),
    FOOD("food", "Food", "খাবার"),
    ANIMALS("animals", "Animals", "প্রাণী"),
    SCHOOL("school", "School", "বিদ্যালয়"),
    TRAVEL("travel", "Travel", "ভ্রমণ"),
    TRANSPORTATION("transportation", "Transportation", "যানবাহন"),
    SHOPPING("shopping", "Shopping", "কেনাকাটা"),
    TIME_AND_DATES("time_dates", "Time & Dates", "সময় ও তারিখ"),
    WEATHER("weather", "Weather", "আবহাওয়া"),
    DAILY_CONVERSATION("daily_conv", "Daily Conversation", "দৈনন্দিন কথোপকথন"),
    WORK("work", "Work", "কর্মক্ষেত্র"),
    TECHNOLOGY("technology", "Technology", "প্রযুক্তি"),
    EMOTIONS("emotions", "Emotions", "অনুভূতি"),
    VERBS("verbs", "Verbs", "ক্রিয়াপদ"),
    ADJECTIVES("adjectives", "Adjectives", "বিশেষণ");

    fun label(language: AppLanguage): String = language.selectText(labelBn, labelEn)
}

/**
 * Complete Vocabulary item model fulfilling all 12 fields in Section 08.
 */
@Serializable
data class VocabularyItem(
    val id: String,
    val wordJp: String,
    val hiraganaReading: String,
    val kanjiSpelling: String,
    val romaji: String,
    val meaningBn: String,
    val meaningEn: String,
    val partOfSpeechEn: String,
    val partOfSpeechBn: String,
    val category: VocabCategory,
    val jlptLevel: JlptLevel,
    val exampleSentenceJp: String,
    val exampleSentenceReading: String,
    val exampleSentenceRomaji: String,
    val exampleSentenceBn: String,
    val exampleSentenceEn: String,
    val hasNativeAudio: Boolean = false
)
