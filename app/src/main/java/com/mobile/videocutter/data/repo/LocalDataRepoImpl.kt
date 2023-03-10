package com.mobile.videocutter.data.repo

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import com.mobile.videocutter.base.extension.getApplication
import com.mobile.videocutter.domain.model.Album
import com.mobile.videocutter.domain.model.Video
import com.mobile.videocutter.domain.repo.ILocalDataRepo
import com.mobile.videocutter.presentation.select.selectvideo.SelectVideoAdapter
import java.util.concurrent.TimeUnit

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
            val albumMap = mutableMapOf<Long, Album>()
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID)
            val albumNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val albumId = cursor.getLong(albumIdColumn)
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

    override fun getVideoList(albumId: String): List<SelectVideoAdapter.VideoDisplay> {
        val listVideo = mutableListOf<Video>()
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DURATION
        )
        val selection = "${MediaStore.Video.Media.BUCKET_ID} = ?"
        val selectionArgs = arrayOf(albumId)

        val sortOrder = "${MediaStore.Video.Media.DATE_MODIFIED} DESC"

        val cursor = this.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val videoId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID))
                val videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                val videoDuration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
                val thumbnail = MediaStore.Video.Thumbnails.getThumbnail(this.contentResolver, videoId, MediaStore.Video.Thumbnails.MINI_KIND, null)
                val video = Video(videoId, formatDuration(videoDuration), thumbnail)
                listVideo.add(video)
            }
            cursor.close()
        }
        return listVideo.map { SelectVideoAdapter.VideoDisplay(it) }
    }

    private fun formatDuration(duration: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes)
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun getVideoCount(contentResolver: ContentResolver, albumId: Long): Int {
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

    private fun getAlbumCoverUri(contentResolver: ContentResolver, albumId: Long): Uri? {
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
