package com.mobile.videocutter.domain.model

import android.os.Parcelable
import com.mobile.videocutter.base.extension.STRING_DEFAULT
import getFormattedTime
import kotlinx.parcelize.Parcelize

@Parcelize
class LocalVideo(
    var videoId: Long = 0,
    var videoName: String = "",
    var authorName: String = "",
    var description: String = "",
    var videoPath: String? = null,
    var videoFolderPath: String? = null,
    var createTime: String? = null,
    var duration: Long = 0,
    var thumbPath: String? = null,
    var rotate: Int = 0,
    private val lat: String? = null,
    private val lon: String? = null,
) : Parcelable {


    fun getImageThumbPath(): String {
        return videoPath ?: STRING_DEFAULT
    }

    fun getTotalTime(): Long {
        return duration
    }

    /**
     * duration to format 00:00 or 00:00:00
     */
    fun getFormattedDuration(): String {
        return getFormattedTime(duration)
    }

    fun getPathVideo(): String {
        return videoPath ?: STRING_DEFAULT
    }

    fun getNameVideo(): String{
        return videoName?: STRING_DEFAULT;
    }
}

fun mockLocalVideo(): LocalVideo {
    return LocalVideo().apply {
        videoId = 1
        videoName = "Video 1"
        authorName = "Author 1"
        description = "Description 1"
        videoPath = "Video 1"
        videoFolderPath = "Video 1"
        createTime = "Video 1"
        duration = (10..10000).random().toLong()
        thumbPath = "Video 1"
        rotate = 1
    }
}

fun mockLocalVideoList(size: Int = 10): List<LocalVideo> {
    val list = mutableListOf<LocalVideo>()
    for (i in 0 until size) {
        list.add(mockLocalVideo())
    }
    return list
}
