package com.nihongo.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Supported interface and explanation languages in NIHONGO.
 * All educational content supports both Bengali and English explanations alongside Japanese.
 */
@Serializable
enum class AppLanguage(
    val code: String,
    val nativeName: String,
    val englishName: String
) {
    BENGALI(
        code = "bn",
        nativeName = "বাংলা",
        englishName = "Bengali"
    ),
    ENGLISH(
        code = "en",
        nativeName = "English",
        englishName = "English"
    );

    /**
     * Helper to select the appropriate localized text based on the active user preference.
     */
    fun selectText(bengaliText: String, englishText: String): String {
        return when (this) {
            BENGALI -> bengaliText.ifBlank { englishText }
            ENGLISH -> englishText.ifBlank { bengaliText }
        }
    }

    companion object {
        fun fromCode(code: String?): AppLanguage {
            return entries.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: BENGALI
        }
    }
}
