package com.example.musicplayerkt

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.os.Bundle
import android.os.IBinder
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class PlayingActivity : AppCompatActivity() {

    val NAME = "name"
    val RAW = "raw"
    var musicSrv: MusicService? = null

    internal lateinit var logo: ImageView
    internal lateinit var next: ImageButton
    internal lateinit var prev: ImageButton
    internal lateinit var start: ImageButton
    internal lateinit var tv_name: TextView

    private var musicBound: Boolean = false
    private var playIntent: Intent? = null
    private var raw = 0
    private var themeName: String? = null

    private val musicConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            val binder = iBinder as MusicService.MusicBinder
            musicSrv = binder.service
            musicSrv?.setList(songsUtil.fillIn())
            musicSrv?.currentIdx = position
            musicBound = true
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            musicBound = false
        }
    }

    val position: Int
        get() {
            for (i in 0..4) {
                if (raw == songsUtil.fillIn().get(i).getRaw()) {
                    return i
                }
            }
            return 0
        }
    //private SongsUtil songsUtil = new SongsUtil();

    override fun onCreate(savedInstanceState: Bundle?) {
        val pref = PreferenceManager
            .getDefaultSharedPreferences(this)
        themeName = pref.getString("theme", "Orange")
        setAppTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        logo = findViewById(R.id.iv_cover)
        next = findViewById(R.id.btn_next)
        prev = findViewById(R.id.btn_prev)
        start = findViewById(R.id.btn_start)
        tv_name = findViewById(R.id.tv_name_in_play)
        val intent = intent
        val name = intent.getStringExtra(NAME)
        raw = intent.getIntExtra(RAW, 0)
        tv_name.text = name

        start.setOnClickListener { musicSrv?.playSong() }
        next.setOnClickListener {
            musicSrv?.playNext()
            tv_name.setText(songsUtil.fillIn().get(musicSrv!!.currentIdx).getName())
        }
        prev.setOnClickListener {
            musicSrv?.playPrev()
            tv_name.setText(songsUtil.fillIn().get(musicSrv!!.currentIdx).getName())
        }
    }

    override fun onStop() {
        super.onStop()
        if (musicBound) {
            unbindService(musicConnection)
            musicBound = false
        }
    }

    fun setAppTheme() {
        when (themeName) {
            "Orange" -> setTheme(R.style.OrangeAppTheme)
            "Purple" -> setTheme(R.style.PurpleAppTheme)
            "Green" -> setTheme(R.style.TealAppTheme)
        }
    }

    override fun onStart() {
        super.onStart()
        if (playIntent == null) {
            playIntent = Intent(this, MusicService::class.java)
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.playing_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_end -> {
                stopService(playIntent)
                musicSrv = null
                System.exit(0)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
