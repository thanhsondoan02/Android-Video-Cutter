package com.mobile.videocutter.domain.model

import getFormattedTime

class MusicTrack {
    var musicTrackId: Long? = null
    var musicTrackPath: String? = null
    var musicTrackName: String? = null
    var progress: String? = null
    var duration: Long = 0L
    var downloaded: Boolean = false

    // duration to format 00:00 or 00:00:00
    fun getFormatTime(): String {
        return getFormattedTime(duration)
    }
}

fun mockMusicTrack(): MusicTrack {
    return MusicTrack().apply {
        musicTrackPath = "Nếu lúc đó"
        musicTrackId = 1
        progress = "Nếu lúc đó"
        musicTrackName = "Nếu lúc đó"
        duration = (10..10000).random().toLong()
        downloaded = true
    }
}

fun mockMusicTrackList(size: Int = 6): List<MusicTrack> {
    val listMusicTrack: MutableList<MusicTrack> = mutableListOf()
    for (i in 0 until size) {
        listMusicTrack.add(mockMusicTrack())
    }
    return listMusicTrack
}

