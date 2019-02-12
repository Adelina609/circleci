package com.example.musicplayerkt

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    val NAME = "name"
    val RAW = "raw"
    val THEME = "theme"

    internal lateinit var tvName: TextView
    internal lateinit var recyclerView: RecyclerView
    internal var adapter: MusicAdapter? = null
    internal var sp: SharedPreferences? = null

    private val SETTINGS_ACTION = 1
    var themeName: String? = null
    private SongsUtil songsUtil = new SongsUtil();
    private var themeId = 0

    public override fun onCreate(savedInstanceState: Bundle?) {
        val pref = PreferenceManager
            .getDefaultSharedPreferences(this)
        themeName = pref.getString("theme", "Orange")
        setAppThemeId()
        setTheme(themeId)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.rv_main)
        tvName = findViewById(R.id.tv_nameOfSong)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val onItemClickListener = object : MusicAdapter.OnItemClickListener {
            override fun onItemClick(song: Song) {
                val intent = Intent(this@MainActivity, PlayingActivity::class.java)
            intent.putExtra(NAME, song.name)
            intent.putExtra(RAW, song.raw)
            intent.putExtra(THEME, themeName)
            startActivity(intent)
            }
        }
        adapter = new MusicAdapter(songsUtil.fillIn(), onItemClickListener)
        recyclerView.adapter = adapter
    }

    fun setAppThemeId() {
        when (themeName) {
            "Orange" -> themeId = R.style.OrangeAppTheme
            "Purple" -> themeId = R.style.PurpleAppTheme
            "Green" -> themeId = R.style.TealAppTheme
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.settings -> startActivityForResult(
                Intent(
                    this,
                    ThemePreferenceActivity::class.java
                ), SETTINGS_ACTION
            )
        }
        return super.onOptionsItemSelected(item)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SETTINGS_ACTION) {
            if (resultCode == ThemePreferenceActivity.RESULT_CODE_THEME_UPDATED) {
                finish()
                startActivity(intent)
                return
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
