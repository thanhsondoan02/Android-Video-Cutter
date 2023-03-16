package com.mobile.videocutter.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class VideoDisplay(var video: LocalVideo, var isSelected: Boolean = false) : Parcelable
