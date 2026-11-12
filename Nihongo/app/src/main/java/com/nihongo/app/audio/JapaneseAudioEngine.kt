package com.nihongo.app.audio

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

/**
 * Complete Japanese Audio Engine (Section 10).
 *
 * Strictly enforces `Locale.JAPAN` (`ja-JP`):
 * - Never falls back to an English TTS voice for Japanese text.
 * - Clearly distinguishes Synthetic TTS from Native-speaker recordings.
 * - Supports 0.75x, 1.0x, 1.25x, Slow Study (0.60x), and Repeat playback.
 * - Provides voice discovery/selection and offline voice installation helper.
 */
class JapaneseAudioEngine(
    private val context: Context
) : TextToSpeech.OnInitListener {

    private val engineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val nativeAudioPlayer = NativeAudioPlayer(context)

    private var tts: TextToSpeech? = null
    private var isJapaneseLocaleVerified: Boolean = false

    private val _playbackState = MutableStateFlow(
        AudioPlaybackState(
            audioPacks = nativeAudioPlayer.getOfflineAudioPacks()
        )
    )
    val playbackState: StateFlow<AudioPlaybackState> = _playbackState.asStateFlow()

    init {
        initializeTtsEngine()
    }

    fun initializeTtsEngine() {
        _playbackState.update { it.copy(engineStatus = TtsEngineStatus.INITIALIZING) }
        tts?.shutdown()
        tts = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        val engine = tts
        if (status != TextToSpeech.SUCCESS || engine == null) {
            isJapaneseLocaleVerified = false
            _playbackState.update {
                it.copy(
                    engineStatus = TtsEngineStatus.ENGINE_ERROR,
                    activeSourceType = AudioSourceType.UNAVAILABLE
                )
            }
            return
        }

        // Explicitly require Locale.JAPAN (ja-JP) — never allow English fallback
        val langResult = engine.setLanguage(Locale.JAPAN)
        when (langResult) {
            TextToSpeech.LANG_MISSING_DATA -> {
                isJapaneseLocaleVerified = false
                _playbackState.update {
                    it.copy(
                        engineStatus = TtsEngineStatus.MISSING_VOICE_DATA,
                        activeSourceType = AudioSourceType.UNAVAILABLE
                    )
                }
            }
            TextToSpeech.LANG_NOT_SUPPORTED -> {
                isJapaneseLocaleVerified = false
                _playbackState.update {
                    it.copy(
                        engineStatus = TtsEngineStatus.LANGUAGE_NOT_SUPPORTED,
                        activeSourceType = AudioSourceType.UNAVAILABLE
                    )
                }
            }
            else -> {
                isJapaneseLocaleVerified = true
                configureUtteranceListener(engine)
                discoverJapaneseVoices(engine)
            }
        }
    }

    private fun configureUtteranceListener(engine: TextToSpeech) {
        engine.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                _playbackState.update { it.copy(isPlaying = true) }
            }

            override fun onDone(utteranceId: String?) {
                val currentState = _playbackState.value
                if (currentState.isRepeatEnabled && currentState.activeJapaneseText.isNotBlank()) {
                    engineScope.launch {
                        delay(450L)
                        if (_playbackState.value.isRepeatEnabled) {
                            speakInternal(
                                japaneseText = currentState.activeJapaneseText,
                                speedMultiplier = currentState.playbackSpeed
                            )
                        }
                    }
                } else {
                    _playbackState.update {
                        it.copy(isPlaying = false, activeJapaneseText = "")
                    }
                }
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                _playbackState.update {
                    it.copy(isPlaying = false, activeJapaneseText = "")
                }
            }

            override fun onError(utteranceId: String?, errorCode: Int) {
                _playbackState.update {
                    it.copy(isPlaying = false, activeJapaneseText = "")
                }
            }
        })
    }

    private fun discoverJapaneseVoices(engine: TextToSpeech) {
        val rawVoices: Set<Voice> = runCatching { engine.voices ?: emptySet() }.getOrDefault(emptySet())
        val japaneseVoices = rawVoices
            .filter { voice ->
                voice.locale.language.equals(Locale.JAPAN.language, ignoreCase = true)
            }
            .sortedWith(
                compareByDescending<Voice> { !it.isNetworkConnectionRequired }
                    .thenByDescending { it.quality }
                    .thenBy { it.name }
            )
            .mapIndexed { index, voice ->
                val isOffline = !voice.isNetworkConnectionRequired
                val modeTag = if (isOffline) "Offline" else "Network"
                JapaneseVoiceInfo(
                    name = voice.name,
                    displayLabel = "Japanese Voice #${index + 1} ($modeTag • ${voice.name.takeLast(10)})",
                    isOfflineAvailable = isOffline,
                    qualityScore = voice.quality
                )
            }

        val hasOfflineVoice = japaneseVoices.any { it.isOfflineAvailable }
        val activeVoiceName = _playbackState.value.selectedVoiceName.ifBlank {
            japaneseVoices.firstOrNull()?.name.orEmpty()
        }

        if (activeVoiceName.isNotBlank()) {
            rawVoices.firstOrNull { it.name == activeVoiceName }?.let { matchedVoice ->
                runCatching { engine.voice = matchedVoice }
            }
        }

        _playbackState.update {
            it.copy(
                engineStatus = if (hasOfflineVoice || japaneseVoices.isEmpty()) {
                    TtsEngineStatus.READY_OFFLINE
                } else {
                    TtsEngineStatus.READY_NETWORK_ONLY
                },
                availableVoices = japaneseVoices,
                selectedVoiceName = activeVoiceName,
                activeSourceType = AudioSourceType.SYNTHETIC_TTS
            )
        }
    }

    /**
     * Primary entry point to speak any Japanese word, character, or sentence.
     * Prioritizes verified local Native Audio when available and enabled; otherwise uses `ja-JP` TTS.
     */
    fun speak(
        japaneseText: String,
        forceSlow: Boolean = false
    ) {
        val cleanedText = japaneseText.trim()
        if (cleanedText.isEmpty()) return

        val state = _playbackState.value
        val effectiveSpeed = if (forceSlow) {
            PlaybackSpeed.SLOW.multiplier
        } else {
            state.playbackSpeed
        }

        // 1. Check if a verified prerecorded native audio file exists locally
        if (state.preferNativeAudio) {
            val nativeUri = nativeAudioPlayer.findLocalNativeAudioUri(cleanedText)
            if (nativeUri != null) {
                tts?.stop()
                _playbackState.update {
                    it.copy(
                        isPlaying = true,
                        activeJapaneseText = cleanedText,
                        lastSpokenJapaneseText = cleanedText,
                        activeSourceType = AudioSourceType.NATIVE_RECORDING
                    )
                }
                nativeAudioPlayer.playNativeUri(
                    uri = nativeUri,
                    speed = effectiveSpeed,
                    repeat = state.isRepeatEnabled,
                    onComplete = {
                        _playbackState.update {
                            it.copy(isPlaying = false, activeJapaneseText = "")
                        }
                    }
                )
                return
            }
        }

        // 2. Otherwise use verified Japanese (ja-JP) TTS — never fall back to English
        speakInternal(cleanedText, effectiveSpeed)
    }

    private fun speakInternal(japaneseText: String, speedMultiplier: Float) {
        val engine = tts
        if (!isJapaneseLocaleVerified || engine == null) {
            _playbackState.update {
                it.copy(
                    isPlaying = false,
                    activeSourceType = AudioSourceType.UNAVAILABLE
                )
            }
            return
        }

        nativeAudioPlayer.stop()
        engine.language = Locale.JAPAN
        engine.setSpeechRate(speedMultiplier.coerceIn(0.5f, 1.5f))

        _playbackState.update {
            it.copy(
                isPlaying = true,
                activeJapaneseText = japaneseText,
                lastSpokenJapaneseText = japaneseText,
                activeSourceType = AudioSourceType.SYNTHETIC_TTS
            )
        }

        val params = Bundle().apply {
            putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f)
        }
        val utteranceId = "nihongo_ja_${System.currentTimeMillis()}"
        engine.speak(japaneseText, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
    }

    fun repeatLast() {
        val lastText = _playbackState.value.lastSpokenJapaneseText
        if (lastText.isNotBlank()) {
            speak(lastText, forceSlow = false)
        }
    }

    fun stop() {
        _playbackState.update {
            it.copy(isPlaying = false, activeJapaneseText = "", isRepeatEnabled = false)
        }
        tts?.stop()
        nativeAudioPlayer.stop()
    }

    fun setPlaybackSpeed(speedMultiplier: Float) {
        val clamped = speedMultiplier.coerceIn(0.5f, 1.5f)
        _playbackState.update { it.copy(playbackSpeed = clamped) }
        tts?.setSpeechRate(clamped)
    }

    fun toggleRepeatMode() {
        _playbackState.update { it.copy(isRepeatEnabled = !it.isRepeatEnabled) }
    }

    fun setPreferNativeAudio(preferNative: Boolean) {
        _playbackState.update { it.copy(preferNativeAudio = preferNative) }
    }

    fun selectJapaneseVoice(voiceName: String) {
        val engine = tts ?: return
        val matched = runCatching {
            engine.voices?.firstOrNull { it.name == voiceName }
        }.getOrNull()

        if (matched != null) {
            runCatching { engine.voice = matched }
            _playbackState.update { it.copy(selectedVoiceName = voiceName) }
        }
    }

    fun toggleOfflineAudioPack(packId: String) {
        val updatedPacks = nativeAudioPlayer.toggleOfflinePackInstallation(packId)
        _playbackState.update { it.copy(audioPacks = updatedPacks) }
    }

    /**
     * Creates an Android system Intent to open the device's TTS voice data installer
     * so the user can download offline `ja-JP` voices with one tap.
     */
    fun createInstallTtsDataIntent(): Intent {
        return Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    fun shutdown() {
        stop()
        tts?.shutdown()
        tts = null
        nativeAudioPlayer.release()
    }
}
