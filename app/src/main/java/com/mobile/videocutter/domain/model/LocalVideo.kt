package com.mobile.videocutter.domain.model

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import com.mobile.videocutter.base.extension.STRING_DEFAULT
import getFormattedTime

class LocalVideo {
    var videoId: Long = 0
    var videoName = ""
    var authorName = ""
    var description = ""
    var videoPath: String? = null
    var videoFolderPath: String? = null
    var createTime: String? = null
    var duration: Long = 0
    var thumbPath: String? = null
    var rotate = 0

    private val lat: String? = null
    private val lon: String? = null

    fun getImageThumbPath(): String {
        return thumbPath ?: STRING_DEFAULT
    }

    private fun getTotalTime(): Long {
        return duration
    }

    /**
     * duration to format 00:00 or 00:00:00
     */
    fun getFormattedDuration(): String {
        return getFormattedTime(duration)
    }

    fun getBitmapListFromVideoByTime(heightBitmapScaled: Int, stepTime: Long): List<Bitmap> {
        val mediaMetadataRetriever = MediaMetadataRetriever()

        mediaMetadataRetriever.setDataSource(videoPath)

        val bitmapList: MutableList<Bitmap> = arrayListOf()

        if (heightBitmapScaled != 0) {

            val countBitmapFullSize = getTotalTime() / stepTime

            for (i in 0 until countBitmapFullSize) {

                val frameTime: Long = stepTime * i

                var bitmapFullSize: Bitmap? = mediaMetadataRetriever.getFrameAtTime(frameTime * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)

                bitmapFullSize = bitmapFullSize?.let {
                    Bitmap.createScaledBitmap(it, heightBitmapScaled, heightBitmapScaled, false)
                }

                bitmapFullSize?.let { bitmapList.add(it) }
            }
        }

        return bitmapList
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
