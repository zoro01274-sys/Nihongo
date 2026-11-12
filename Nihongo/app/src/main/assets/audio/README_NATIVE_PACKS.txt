NIHONGO OFFLINE NATIVE AUDIO ARCHITECTURE
=========================================
1. Bundled native speaker recordings (.mp3 / .ogg) placed in `assets/audio/` or downloaded into
   `context.filesDir/audio_packs/<level>/` are automatically detected by `NativeAudioPlayer`.
2. File naming convention: `<item_id>.mp3` (for example: `voc_greet_1.mp3`, `h_basic_a.mp3`).
3. When a verified native file is present locally and `preferNativeAudio` is enabled,
   `JapaneseAudioEngine` plays the human recording via Media3 ExoPlayer and displays the
   "Native Audio / স্থানীয় কণ্ঠ" badge.
4. When no native file is present for an item, `JapaneseAudioEngine` uses Android TextToSpeech
   strictly configured for `Locale.JAPAN` (ja-JP) and displays the
   "Offline TTS (ja-JP) / অফলাইন টিটিএস (ja-JP)" badge.
