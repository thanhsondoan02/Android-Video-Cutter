package com.mobile.videocutter.base.common.model

import android.net.Uri

data class Album(
    val idAlbum: Long,
    val nameAlbum: String,
    val coverUriAlbum: Uri?,
    val countAlbum: Int
)
