package com.mobile.videocutter.domain.repo

import android.content.ContentResolver
import com.mobile.videocutter.domain.model.Album

interface ILocalDataRepo {
    fun getAlbumList(contentResolver: ContentResolver): List<Album>

    fun get()
}
