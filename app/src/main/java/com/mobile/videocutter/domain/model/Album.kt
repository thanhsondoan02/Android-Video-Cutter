package com.mobile.videocutter.domain.model

import android.net.Uri

data class Album(
    var idAlbum: Long,
    var nameAlbum: String,
    var coverUriAlbum: Uri?,
    var countAlbum: Int
)
