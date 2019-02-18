package com.example.musicplayerkt

class Song(var name: String?, val raw: Int) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val song = o as Song?
        return name == song?.name && raw == song?.raw
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + raw
        return result
    }


}
