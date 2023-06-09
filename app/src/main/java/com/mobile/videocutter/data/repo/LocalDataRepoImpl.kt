package com.mobile.videocutter.data.repo

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import com.mobile.videocutter.base.extension.getApplication
import com.mobile.videocutter.domain.model.Album
import com.mobile.videocutter.domain.model.LocalVideo
import com.mobile.videocutter.domain.model.MusicTrack
import com.mobile.videocutter.domain.repo.ILocalDataRepo
import com.mobile.videocutter.presentation.savelibrary.AppInfo

class LocalDataRepoImpl : ILocalDataRepo {
    private val contentResolver = getApplication().contentResolver

    override fun getAlbumList(): List<Album> {
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME
        )
        val sortOrder = "${MediaStore.Video.Media.BUCKET_DISPLAY_NAME} ASC"

        val albumList = mutableListOf<Album>()
        val cursor = contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )

        if (cursor != null) {
            val albumMap = mutableMapOf<String, Album>()
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID)
            val albumNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)

            while (cursor.moveToNext()) {
                val albumId = cursor.getString(albumIdColumn)
                val albumName = cursor.getString(albumNameColumn)

                val videoCount = getVideoCount(contentResolver, albumId)
                val albumCoverUri = getAlbumCoverUri(contentResolver, albumId)

                val album = albumMap[albumId]
                    ?: Album(albumId, albumName, albumCoverUri, videoCount)
                albumMap[albumId] = album
                if (albumCoverUri != null && !albumList.contains(album)) {
                    albumList.add(album)
                }
            }
            cursor.close()
        }
        return albumList
    }

    override fun getMyStudioVideoList(albumId: String?): List<LocalVideo> {
        val videoList = mutableListOf<LocalVideo>()
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Video.VideoColumns.DATA,
            MediaStore.Video.VideoColumns.DISPLAY_NAME,
            MediaStore.Video.VideoColumns.DURATION
        )
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        var sortOrder: String? = null
        if (albumId != null) {
            selection = "${MediaStore.Video.Media.BUCKET_ID} = ?"
            selectionArgs = arrayOf(albumId)
            sortOrder = "${MediaStore.Video.Media.DATE_MODIFIED} DESC"
        }
        val cursor = contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)

        cursor?.let {
            while (it.moveToNext()) {
                val path = it.getString(it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                val name = it.getString(it.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME))
                val duration = it.getLong(it.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
                videoList.add(LocalVideo().apply {
                    this.videoPath = path
                    this.videoName = name
                    this.duration = duration
                })
            }
            it.close()
        }
        return videoList
    }

    override fun getMusicTrackList(): List<MusicTrack> {
        val musicTrackList = mutableListOf<MusicTrack>()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION
        )

        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
        val sortOrder = MediaStore.Audio.Media.DISPLAY_NAME + " ASC"

        val cursor = contentResolver.query(uri, projection, selection, null, sortOrder)
        if (cursor != null) {
            val filePath = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val name = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val duration = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            while (cursor.moveToNext()) {
                musicTrackList.add(MusicTrack().apply {
                    this.musicTrackPath = cursor.getString(filePath)
                    this.musicTrackName = cursor.getString(name)
                    this.duration = cursor.getLong(duration)
                    this.musicTrackName = this.musicTrackName?.substringBeforeLast(".")
                })
            }
            cursor.close()
        }
        return musicTrackList
    }

    override fun getBitmapListFromVideoByTime(
        pathList: List<String>,
        durationList: List<Long>,
        heightBitmapScaled: Int,
        stepTime: Long
    ): List<Bitmap> {

        val mediaMetadataRetriever = MediaMetadataRetriever()

        val bitmapList: MutableList<Bitmap> = arrayListOf()

        for (i in pathList.indices) {

            mediaMetadataRetriever.setDataSource(pathList[i])

            if (heightBitmapScaled != 0) {

                val countBitmapFullSize = durationList[i] / stepTime

                for (j in 0 until countBitmapFullSize) {

                    val frameTime: Long = stepTime * j

                    var bitmapFullSize: Bitmap? = mediaMetadataRetriever.getFrameAtTime(frameTime * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)

                    bitmapFullSize = bitmapFullSize?.let {
                        Bitmap.createScaledBitmap(it, heightBitmapScaled, heightBitmapScaled, false)
                    }

                    bitmapFullSize?.let { bitmapList.add(it) }
                }
            }
        }

        return bitmapList
    }

    override fun getBitmapListFromVideoByLength(localVideo: LocalVideo, heightBitmapScaled: Int, maxWidth: Int): List<Bitmap> {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(localVideo.videoPath)

        val bitmapList: MutableList<Bitmap> = arrayListOf()

        val widthBitmap = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)

        val heightBitmap = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)

        if (heightBitmap != null && widthBitmap != null && heightBitmapScaled != 0) {

            val countBitmapFullSize = maxWidth / heightBitmapScaled

            val interval: Long = localVideo.getTotalTime() / countBitmapFullSize

            for (i in 0 until countBitmapFullSize + 1) {

                val frameTime: Long = interval * i

                var bitmapFullSize: Bitmap? = mediaMetadataRetriever.getFrameAtTime(frameTime * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)

                bitmapFullSize = bitmapFullSize?.let {
                    Bitmap.createScaledBitmap(it, heightBitmapScaled, heightBitmapScaled, false)
                }

                bitmapFullSize?.let { bitmapList.add(it) }
            }
        }

        return bitmapList
    }

    override fun getPackageAppInfoList(): List<AppInfo> {
        val appInfo = mutableListOf<AppInfo>()
        val pm: PackageManager = getApplication().packageManager
        val intent = Intent(Intent.ACTION_SEND, null)
        intent.type = "text/plain"
        val resolveInfo = pm.queryIntentActivities(intent, 0)
        resolveInfo.forEach { info ->
            val applicationInfo = info.activityInfo.applicationInfo
            val name = applicationInfo.loadLabel(pm)
            val icon = applicationInfo.loadIcon(pm)
            val packageName = applicationInfo.packageName
            appInfo += AppInfo(name.toString(), icon, packageName)
        }
        return appInfo
    }

    private fun getVideoCount(contentResolver: ContentResolver, albumId: String): Int {
        val projection = arrayOf(
            MediaStore.Video.Media._ID
        )
        val selection = "${MediaStore.Video.Media.BUCKET_ID} = ?"
        val selectionArgs = arrayOf(albumId.toString())

        val cursor = contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )

        val videoCount = cursor?.count ?: 0

        cursor?.close()

        return videoCount
    }

    private fun getAlbumCoverUri(contentResolver: ContentResolver, albumId: String): Uri? {
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Video.Media._ID)
        val selection = "${MediaStore.Video.Media.BUCKET_ID} = ? AND ${MediaStore.Video.Media._ID} != ?"
        val selectionArgs = arrayOf(albumId.toString(), "0")
        val sortOrder = "${MediaStore.Video.Media.DATE_MODIFIED} DESC"

        val cursor = contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)

        var coverUri: Uri? = null
        if (cursor != null && cursor.moveToFirst()) {
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val id = cursor.getLong(idColumn)
            coverUri = ContentUris.withAppendedId(uri, id)
        }
        cursor?.close()
        return coverUri
    }
}
