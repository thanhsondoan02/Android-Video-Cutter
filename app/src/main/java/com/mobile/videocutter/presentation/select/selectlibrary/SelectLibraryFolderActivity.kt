package com.mobile.videocutter.presentation.select.selectlibrary

import android.Manifest
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.domain.model.Album
import com.mobile.videocutter.databinding.SelectLibraryFolderActivityBinding
import com.mobile.videocutter.presentation.select.selectvideo.SelectVideoActivity

class SelectLibraryFolderActivity : BaseBindingActivity<SelectLibraryFolderActivityBinding>(R.layout.select_library_folder_activity) {

    private var selectLibFolderAdapter: SelectLibraryFolderAdapter = SelectLibraryFolderAdapter()

    override fun onInitView() {
        super.onInitView()
        binding.hvSelectLibraryFolder.setVisibleViewUnderLine(false)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            doRequestPermission(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                object : PermissionListener {
                    override fun onAllow() {
                        loadAlbums()
                        initListener()
                    }

                    override fun onDenied(neverAskAgainPermissionList: List<String>) {
                        TODO("Not yet implemented")
                    }
                })
        } else {
            loadAlbums()
            initListener()
        }
    }

    private fun loadAlbums() {
        binding.rvSelectLibraryFolder.layoutManager = LinearLayoutManager(baseContext)
        binding.rvSelectLibraryFolder.adapter = selectLibFolderAdapter
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME
        )
        val sortOrder = "${MediaStore.Video.Media.BUCKET_DISPLAY_NAME} ASC"

        val albumList = mutableListOf<Album>()
        val contentResolver = contentResolver
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
        Log.d(TAG, "loadAlbums: ${albumList}")
        selectLibFolderAdapter.submitList(albumList)
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

    private fun initListener() {
        selectLibFolderAdapter.albumListener = object : SelectLibraryFolderAdapter.IAlbumListener {
            override fun onClickAlbum(idAlbum: Long, nameAlbum: String) {
                val intent = Intent(this@SelectLibraryFolderActivity, SelectVideoActivity::class.java)
                intent.putExtra("idAlbum", idAlbum.toString())
                intent.putExtra("nameAlbum", nameAlbum)
                startActivity(intent)
            }
        }
    }
}
