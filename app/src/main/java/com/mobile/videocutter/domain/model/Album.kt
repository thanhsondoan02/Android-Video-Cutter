package com.mobile.videocutter.domain.model

import android.net.Uri

data class Album(
    var idAlbum: String? = null,
    var nameAlbum: String? = null,
    var coverUriAlbum: Uri? = null,
    var countAlbum: Int? = null
)
