package com.mobile.videocutter.base.common.model

import android.net.Uri

data class Album(
    val id: Long,
    val name: String,
    val coverUri: Uri?,
    val count: Int
)
