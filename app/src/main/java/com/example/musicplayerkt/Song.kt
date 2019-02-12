package com.example.musicplayerkt

import java.util.Objects

class Song(var name: String?, val raw: Int) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val song = o as Song?
        return name == song!!.name && raw == song.raw
    }
}
