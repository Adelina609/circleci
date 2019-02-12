package com.example.musicplayerkt

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.media.AudioManager
import android.os.PowerManager

import java.util.ArrayList

class MusicService : Service(), MediaPlayer.OnCompletionListener {

    private var mediaPlayer: MediaPlayer? = null
    private var songs: List<Song> = ArrayList()
    private val musicBind = MusicBinder()
    var currentIdx = 0
    private var isFirstCreating = true

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        initMusicPlayer()
    }

    inner class MusicBinder : Binder() {
        internal val service: MusicService
            get() = this@MusicService
    }

    override fun onBind(intent: Intent): IBinder? {
        return musicBind
    }

    fun initMusicPlayer() {
        mediaPlayer?.setWakeMode(
            applicationContext,
            PowerManager.PARTIAL_WAKE_LOCK
        )
        mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer?.setOnCompletionListener(this)
    }

    fun setList(list: ArrayList<Song>) {
        songs = list
    }

    fun playSong() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        } else {
            if (isFirstCreating) {
                mediaPlayer?.reset()
                mediaPlayer = MediaPlayer.create(applicationContext, songs[currentIdx].raw)
                isFirstCreating = false
            }
            mediaPlayer?.start()
        }
    }

    fun playNext() {
        mediaPlayer?.reset()
        if (currentIdx == songs.size - 1) currentIdx = -1
        mediaPlayer = MediaPlayer.create(applicationContext, songs[++currentIdx].raw)
        mediaPlayer?.start()
    }

    fun playPrev() {
        mediaPlayer?.reset()
        if (currentIdx == 0) currentIdx = songs.size
        mediaPlayer = MediaPlayer.create(applicationContext, songs[--currentIdx].raw)
        mediaPlayer?.start()
    }

    override fun onCompletion(mediaPlayer: MediaPlayer) {
        mediaPlayer.reset()
        playNext()
    }
}
