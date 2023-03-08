package com.mobile.videocutter.domain.model

import android.graphics.Bitmap

data class Video(
    var idVideo: Long?,
    var durationVideo: String,
    var thumbnail: Bitmap?
)
