package com.nihongo.app.audio

import com.nihongo.app.domain.model.AppLanguage

/**
 * Explicitly distinguishes synthetic Text-to-Speech from prerecorded human native audio,
 * fulfilling Section 10: "Clearly distinguish Synthetic TTS and Native-speaker recordings."
 */
enum class AudioSourceType(
    val badgeEn: String,
    val badgeBn: String
) {
    NATIVE_RECORDING(
        badgeEn = "Native Audio",
        badgeBn = "স্থানীয় কণ্ঠ"
    ),
    SYNTHETIC_TTS(
        badgeEn = "Offline TTS (ja-JP)",
        badgeBn = "অফলাইন টিটিএস (ja-JP)"
    ),
    UNAVAILABLE(
        badgeEn = "Voice Missing",
        badgeBn = "কণ্ঠ অনুপস্থিত"
    );

    fun label(language: AppLanguage): String = language.selectText(badgeBn, badgeEn)
}

/**
 * Supported playback speed presets required by Section 10.
 */
enum class PlaybackSpeed(
    val multiplier: Float,
    val label: String
) {
    SLOW_STUDY(0.60f, "0.6x"),
    SLOW(0.75f, "0.75x"),
    NORMAL(1.00f, "1.0x"),
    FAST(1.25f, "1.25x");

    companion object {
        fun fromMultiplier(value: Float): PlaybackSpeed {
            return entries.minByOrNull { kotlin.math.abs(it.multiplier - value) } ?: NORMAL
        }
    }
}
