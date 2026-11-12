package com.nihongo.app.audio

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SpeechRecognitionState(
    val isSupportedOnDevice: Boolean = true,
    val isListening: Boolean = false,
    val recognizedJapaneseText: String = "",
    val confidenceMessageBn: String = "",
    val confidenceMessageEn: String = "",
    val isMatchSuccess: Boolean? = null,
    val errorTextBn: String? = null,
    val errorTextEn: String? = null
)

/**
 * Wraps Android SpeechRecognizer for Japanese (`ja-JP`) Speaking & Pronunciation Practice (Section 11).
 * Compares recognized speech against expected Kanji and Hiragana readings while transparently
 * communicating that mobile speech recognition accuracy varies.
 */
class JapaneseSpeechRecognizer(private val context: Context) {

    private var speechRecognizer: SpeechRecognizer? = null

    private val _state = MutableStateFlow(
        SpeechRecognitionState(
            isSupportedOnDevice = SpeechRecognizer.isRecognitionAvailable(context)
        )
    )
    val state: StateFlow<SpeechRecognitionState> = _state.asStateFlow()

    private var expectedKanjiTarget: String = ""
    private var expectedHiraganaTarget: String = ""

    fun startListening(
        targetJapaneseKanji: String,
        targetHiraganaReading: String
    ) {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            _state.update {
                it.copy(
                    isSupportedOnDevice = false,
                    isListening = false,
                    errorTextBn = "আপনার ফোনে ভয়েস রিকগনিশন সার্ভিস সক্রিয় নেই।",
                    errorTextEn = "Speech recognition service is not available on this device."
                )
            }
            return
        }

        expectedKanjiTarget = normalizeJapanese(targetJapaneseKanji)
        expectedHiraganaTarget = normalizeJapanese(targetHiraganaReading)

        speechRecognizer?.destroy()
        val recognizer = SpeechRecognizer.createSpeechRecognizer(context.applicationContext)
        speechRecognizer = recognizer

        recognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                _state.update {
                    it.copy(
                        isListening = true,
                        recognizedJapaneseText = "",
                        isMatchSuccess = null,
                        errorTextBn = null,
                        errorTextEn = null
                    )
                }
            }

            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                _state.update { it.copy(isListening = false) }
            }

            override fun onError(error: Int) {
                val (msgBn, msgEn) = when (error) {
                    SpeechRecognizer.ERROR_NO_MATCH ->
                        "কোনো জাপানি উচ্চারণ শনাক্ত করা যায়নি। আবার চেষ্টা করুন।" to
                            "No Japanese speech detected. Please try speaking again."
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS ->
                        "মাইক্রোফোন ব্যবহারের অনুমতি (Permission) প্রয়োজন।" to
                            "Microphone permission is required for speaking practice."
                    else ->
                        "কণ্ঠ শনাক্তকরণে সাময়িক সমস্যা হয়েছে (কোড: $error)।" to
                            "Speech recognition encountered an issue (Code: $error)."
                }
                _state.update {
                    it.copy(
                        isListening = false,
                        errorTextBn = msgBn,
                        errorTextEn = msgEn
                    )
                }
            }

            override fun onResults(results: Bundle?) {
                val candidates = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    .orEmpty()

                val bestTranscript = candidates.firstOrNull().orEmpty()
                val normalizedCandidates = candidates.map { normalizeJapanese(it) }

                val matched = normalizedCandidates.any { spoken ->
                    spoken.isNotBlank() && (
                        spoken == expectedKanjiTarget ||
                            spoken == expectedHiraganaTarget ||
                            expectedKanjiTarget.contains(spoken) ||
                            expectedHiraganaTarget.contains(spoken) ||
                            spoken.contains(expectedKanjiTarget) ||
                            spoken.contains(expectedHiraganaTarget)
                        )
                }

                _state.update {
                    it.copy(
                        isListening = false,
                        recognizedJapaneseText = bestTranscript,
                        isMatchSuccess = matched,
                        confidenceMessageBn = if (matched) {
                            "চমৎকার! আপনার উচ্চারণ প্রত্যাশিত জাপানি শব্দের সাথে মিলেছে।"
                        } else {
                            "শনাক্তকৃত পাঠ্য কিছুটা ভিন্ন হয়েছে। নিচের স্পিকার বাটনে শুনে আবার চেষ্টা করুন।"
                        },
                        confidenceMessageEn = if (matched) {
                            "Great job! Your spoken Japanese matched the expected phrase."
                        } else {
                            "The recognized text differed slightly. Listen to the model audio and try again."
                        }
                    )
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val partial = partialResults
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull()
                    .orEmpty()
                if (partial.isNotBlank()) {
                    _state.update { it.copy(recognizedJapaneseText = partial) }
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ja-JP")
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "ja-JP")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5)
        }

        recognizer.startListening(recognizerIntent)
    }

    fun stopListening() {
        speechRecognizer?.stopListening()
        _state.update { it.copy(isListening = false) }
    }

    fun clearResult() {
        _state.update {
            it.copy(
                recognizedJapaneseText = "",
                isMatchSuccess = null,
                errorTextBn = null,
                errorTextEn = null
            )
        }
    }

    fun destroy() {
        speechRecognizer?.destroy()
        speechRecognizer = null
    }

    private fun normalizeJapanese(input: String): String {
        return input
            .replace("。", "")
            .replace("、", "")
            .replace("！", "")
            .replace("？", "")
            .replace(" ", "")
            .replace(" ", "")
            .trim()
    }
}
