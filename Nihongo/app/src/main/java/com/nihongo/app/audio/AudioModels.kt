package com.nihongo.app.audio

import com.nihongo.app.domain.model.AppLanguage
import com.nihongo.app.domain.model.JlptLevel

/**
 * Represents the initialization and voice availability state of the Japanese TTS engine.
 */
enum class TtsEngineStatus(
    val messageBn: String,
    val messageEn: String
) {
    INITIALIZING(
        messageBn = "জাপানি অডিও ইঞ্জিন প্রস্তুত হচ্ছে...",
        messageEn = "Initializing Japanese audio engine..."
    ),
    READY_OFFLINE(
        messageBn = "অফলাইন জাপানি কণ্ঠ (ja-JP) সক্রিয় রয়েছে",
        messageEn = "Offline Japanese voice (ja-JP) is active and ready"
    ),
    READY_NETWORK_ONLY(
        messageBn = "জাপানি কণ্ঠ সক্রিয় (অফলাইন প্যাক ডাউনলোড করার পরামর্শ দেওয়া হচ্ছে)",
        messageEn = "Japanese voice active (Offline voice pack download recommended)"
    ),
    MISSING_VOICE_DATA(
        messageBn = "আপনার ফোনে জাপানি (ja-JP) কণ্ঠ ডাউনলোড করা নেই। সিস্টেম সেটিংস থেকে ডাউনলোড করুন।",
        messageEn = "Japanese (ja-JP) voice data is missing on this phone. Tap to install offline voice data."
    ),
    LANGUAGE_NOT_SUPPORTED(
        messageBn = "এই ডিভাইসের টিটিএস ইঞ্জিনে জাপানি ভাষা (ja-JP) সমর্থিত নয়।",
        messageEn = "Japanese (ja-JP) is not supported by the current device TTS engine."
    ),
    ENGINE_ERROR(
        messageBn = "অডিও ইঞ্জিন চালু করতে সমস্যা হয়েছে।",
        messageEn = "Failed to initialize the Android TextToSpeech service."
    );

    fun description(language: AppLanguage): String = language.selectText(messageBn, messageEn)
}

/**
 * Metadata for an installed Japanese (`ja-JP`) system TTS voice.
 */
data class JapaneseVoiceInfo(
    val name: String,
    val displayLabel: String,
    val isOfflineAvailable: Boolean,
    val qualityScore: Int
)

/**
 * Metadata for downloadable/local native audio packs (Section 10 Offline Audio).
 */
data class AudioPackInfo(
    val packId: String,
    val jlptLevel: JlptLevel,
    val titleBn: String,
    val titleEn: String,
    val itemCount: Int,
    val sizeMegabytes: Double,
    val isInstalledLocally: Boolean
)

/**
 * Complete immutable state of the NIHONGO Audio Subsystem exposed to Compose screens.
 */
data class AudioPlaybackState(
    val engineStatus: TtsEngineStatus = TtsEngineStatus.INITIALIZING,
    val isPlaying: Boolean = false,
    val activeJapaneseText: String = "",
    val lastSpokenJapaneseText: String = "",
    val activeSourceType: AudioSourceType = AudioSourceType.SYNTHETIC_TTS,
    val playbackSpeed: Float = 1.0f,
    val isRepeatEnabled: Boolean = false,
    val preferNativeAudio: Boolean = true,
    val availableVoices: List<JapaneseVoiceInfo> = emptyList(),
    val selectedVoiceName: String = "",
    val audioPacks: List<AudioPackInfo> = emptyList()
)
