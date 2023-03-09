package com.mobile.videocutter.domain.model

import android.net.Uri

data class Album(
    var idAlbum: Long? = null,
    var nameAlbum: String? = null,
    var coverUriAlbum: Uri? = null,
    var countAlbum: Int? = null
)
