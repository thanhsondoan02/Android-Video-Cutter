package com.mobile.videocutter.domain.model

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import com.mobile.videocutter.base.extension.STRING_DEFAULT
import java.io.File

class LocalVideo {
    companion object {
        fun buildVideo(context: Context?, videoPath: String?): LocalVideo {
            val info = LocalVideo()
            info.videoPath = videoPath
            try {
                val mp = MediaPlayer.create(context, Uri.fromFile(File(videoPath)))
                if (mp != null) {
                    info.duration = mp.duration.toLong()
                    mp.release()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return info
        }
    }

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

    fun calcDuration(): LocalVideo {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        try {
            mediaMetadataRetriever.setDataSource(videoPath)
            val time = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            duration = time!!.toLong()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return this
    }

    fun getImageThumbPath(): String {
        return thumbPath ?: STRING_DEFAULT
    }

    /**
     * duration to format 00:00 or 00:00:00
     */
    fun getFormattedDuration(): String {
        val seconds = duration / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val secondsLeft = seconds % 60
        val minutesLeft = minutes % 60
        val hoursLeft = hours % 60
        return if (hoursLeft > 0) {
            String.format("%02d:%02d:%02d", hoursLeft, minutesLeft, secondsLeft)
        } else {
            String.format("%02d:%02d", minutesLeft, secondsLeft)
        }
    }

    fun getBitmapListFromVideo(context: Context?, uri: Uri, heightBitmapScaled: Int, maxWidth: Int, totalTime: Long): List<Bitmap> {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(context, uri)
        val startPosition = 0L
        val bitmapList: MutableList<Bitmap> = arrayListOf()
        val widthBitmap = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
        val heightBitmap = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
        if (heightBitmap != null && widthBitmap != null) {

            val widthBitmapScaled = (3 * heightBitmapScaled) / 4
            val countBitmapFullSize = maxWidth / widthBitmapScaled
            val interval: Long = ((totalTime - startPosition) / countBitmapFullSize)
            for (i in 0 until countBitmapFullSize) {
                val frameTime: Long = startPosition + interval * i
                var bitmapFullSize: Bitmap? = mediaMetadataRetriever.getFrameAtTime(frameTime * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                bitmapFullSize = bitmapFullSize?.let { Bitmap.createScaledBitmap(it, widthBitmapScaled, heightBitmapScaled, false) }
                bitmapFullSize?.let { bitmapList.add(it) }
            }

//            if (maxWidth % widthBitmapScaled > 0) {
//                val widthLack = (maxWidth % widthBitmapScaled) * widthBitmapScaled
//                val bitmapLack: Bitmap? = mediaMetadataRetriever.getFrameAtTime(((totalTime - interval * (countBitmapFullSize-1)) * 1000), MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
//                bitmapLack?.let { Bitmap.createScaledBitmap(it, widthLack, heightBitmapScaled, false) }
//                bitmapLack?.let { bitmapList.add(it) }
//            }
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
