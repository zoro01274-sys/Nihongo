package com.nihongo.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents the five structured Japanese-Language Proficiency Test levels (N5 to N1).
 */
@Serializable
enum class JlptLevel(
    val label: String,
    val rankOrder: Int,
    val titleEn: String,
    val titleBn: String,
    val descriptionEn: String,
    val descriptionBn: String,
    val targetKanjiCount: Int,
    val targetVocabCount: Int
) {
    N5(
        label = "N5",
        rankOrder = 1,
        titleEn = "Absolute Beginner",
        titleBn = "প্রাথমিক স্তর (শুরু)",
        descriptionEn = "Basic Hiragana, Katakana, foundational Kanji, daily greetings, and essential grammar.",
        descriptionBn = "মৌলিক হিরাগানা, কাতাকানা, প্রাথমিক কাঞ্জি, দৈনন্দিন অভিবাদন এবং অপরিহার্য ব্যাকরণ।",
        targetKanjiCount = 100,
        targetVocabCount = 800
    ),
    N4(
        label = "N4",
        rankOrder = 2,
        titleEn = "Elementary",
        titleBn = "প্রাথমিক পরবর্তী স্তর",
        descriptionEn = "Everyday conversations, basic reading passages, polite vs. casual speech, and core verb forms.",
        descriptionBn = "দৈনন্দিন কথোপকথন, সহজ অনুচ্ছেদ পাঠ, ভদ্র ও সাধারণ বাচনভঙ্গি এবং ক্রিয়ার রূপ।",
        targetKanjiCount = 300,
        targetVocabCount = 1500
    ),
    N3(
        label = "N3",
        rankOrder = 3,
        titleEn = "Intermediate",
        titleBn = "মধ্যম স্তর",
        descriptionEn = "Bridge to fluency: practical workplace situations, nuanced grammar, and natural speed listening.",
        descriptionBn = "সাবলীলতার সেতুবন্ধন: বাস্তব পরিস্থিতি, সূক্ষ্ম ব্যাকরণ এবং স্বাভাবিক গতির শ্রবণ অনুশীলন।",
        targetKanjiCount = 650,
        targetVocabCount = 3750
    ),
    N2(
        label = "N2",
        rankOrder = 4,
        titleEn = "Upper-Intermediate",
        titleBn = "উচ্চ-মধ্যম স্তর",
        descriptionEn = "Business Japanese, newspaper articles, formal expressions, and complex reading comprehension.",
        descriptionBn = "পেশাদার জাপানি ভাষা, সংবাদপত্রের নিবন্ধ, আনুষ্ঠানিক প্রকাশভঙ্গি এবং জটিল পাঠ অনুধাবন।",
        targetKanjiCount = 1000,
        targetVocabCount = 6000
    ),
    N1(
        label = "N1",
        rankOrder = 5,
        titleEn = "Advanced",
        titleBn = "উন্নত স্তর",
        descriptionEn = "Academic, literary, and abstract Japanese across wide-ranging professional and cultural topics.",
        descriptionBn = "একাডেমিক, সাহিত্যিক এবং উচ্চস্তরের পেশাদার ও সাংস্কৃতিক জাপানি ভাষার পূর্ণ দক্ষতা।",
        targetKanjiCount = 2000,
        targetVocabCount = 10000
    );

    companion object {
        fun fromLabel(label: String?): JlptLevel {
            return entries.firstOrNull { it.label.equals(label, ignoreCase = true) } ?: N5
        }
    }
}
