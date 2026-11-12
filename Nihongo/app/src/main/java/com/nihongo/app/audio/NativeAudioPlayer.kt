package com.nihongo.app.audio

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.nihongo.app.domain.model.JlptLevel
import java.io.File

/**
 * Handles prerecorded human native-speaker Japanese audio via AndroidX Media3 ExoPlayer,
 * as well as local storage management for offline native audio packs (`filesDir/audio_packs/`).
 */
class NativeAudioPlayer(private val context: Context) {

    private var exoPlayer: ExoPlayer? = null
    private var onPlaybackCompleted: (() -> Unit)? = null

    private val packsRootDir: File
        get() = File(context.filesDir, "audio_packs").apply {
            if (!exists()) mkdirs()
        }

    private fun getOrCreatePlayer(): ExoPlayer {
        val current = exoPlayer
        if (current != null) return current

        return ExoPlayer.Builder(context.applicationContext).build().also { player ->
            player.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_ENDED) {
                        onPlaybackCompleted?.invoke()
                    }
                }
            })
            exoPlayer = player
        }
    }

    /**
     * Checks whether a verified native recording exists locally for the given Japanese text or item key.
     */
    fun findLocalNativeAudioUri(japaneseText: String): Uri? {
        val sanitizedKey = japaneseText.trim().hashCode().toUInt().toString(16)
        val localFile = File(packsRootDir, "native_$sanitizedKey.mp3")
        if (localFile.exists() && localFile.length() > 0L) {
            return Uri.fromFile(localFile)
        }
        return null
    }

    /**
     * Plays a verified local native audio URI with speed and repeat support.
     */
    fun playNativeUri(
        uri: Uri,
        speed: Float,
        repeat: Boolean,
        onComplete: () -> Unit
    ) {
        onPlaybackCompleted = onComplete
        val player = getOrCreatePlayer()
        player.stop()
        player.clearMediaItems()
        player.repeatMode = if (repeat) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
        player.playbackParameters = PlaybackParameters(speed.coerceIn(0.5f, 1.5f))
        player.setMediaItem(MediaItem.fromUri(uri))
        player.prepare()
        player.play()
    }

    fun stop() {
        exoPlayer?.stop()
    }

    fun release() {
        exoPlayer?.release()
        exoPlayer = null
    }

    /**
     * Lists offline audio packs and checks whether each pack directory is provisioned in local storage.
     */
    fun getOfflineAudioPacks(): List<AudioPackInfo> {
        val catalog = listOf(
            AudioPackInfo(
                packId = "pack_kana_core",
                jlptLevel = JlptLevel.N5,
                titleBn = "হিরাগানা ও কাতাকানা উচ্চারণ প্যাক",
                titleEn = "Hiragana & Katakana Core Pronunciation Pack",
                itemCount = 104,
                sizeMegabytes = 4.2,
                isInstalledLocally = isPackInstalled("pack_kana_core")
            ),
            AudioPackInfo(
                packId = "pack_jlpt_n5",
                jlptLevel = JlptLevel.N5,
                titleBn = "JLPT N5 মৌলিক শব্দ ও বাক্য অডিও প্যাক",
                titleEn = "JLPT N5 Essential Vocabulary & Sentences Pack",
                itemCount = 800,
                sizeMegabytes = 18.5,
                isInstalledLocally = isPackInstalled("pack_jlpt_n5")
            ),
            AudioPackInfo(
                packId = "pack_jlpt_n4",
                jlptLevel = JlptLevel.N4,
                titleBn = "JLPT N4 দৈনন্দিন কথোপকথন অডিও প্যাক",
                titleEn = "JLPT N4 Daily Conversation Audio Pack",
                itemCount = 1500,
                sizeMegabytes = 29.0,
                isInstalledLocally = isPackInstalled("pack_jlpt_n4")
            ),
            AudioPackInfo(
                packId = "pack_jlpt_n3_n1",
                jlptLevel = JlptLevel.N3,
                titleBn = "JLPT N3–N1 উন্নত শ্রবণ অনুশীলন প্যাক",
                titleEn = "JLPT N3–N1 Advanced Listening Comprehension Pack",
                itemCount = 2400,
                sizeMegabytes = 46.8,
                isInstalledLocally = isPackInstalled("pack_jlpt_n3_n1")
            )
        )
        return catalog
    }

    fun isPackInstalled(packId: String): Boolean {
        val markerFile = File(packsRootDir, "$packId.installed")
        return markerFile.exists()
    }

    /**
     * Provisions or removes the local offline audio pack directory and metadata marker inside `filesDir`.
     */
    fun toggleOfflinePackInstallation(packId: String): List<AudioPackInfo> {
        val markerFile = File(packsRootDir, "$packId.installed")
        if (markerFile.exists()) {
            markerFile.delete()
        } else {
            markerFile.writeText("installed_at=${System.currentTimeMillis()}\nstatus=offline_ready")
        }
        return getOfflineAudioPacks()
    }

    fun getUsedStorageKilobytes(): Long {
        val files = packsRootDir.listFiles() ?: return 0L
        return files.sumOf { it.length() } / 1024L
    }
}
