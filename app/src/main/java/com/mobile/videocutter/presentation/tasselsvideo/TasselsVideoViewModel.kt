package com.mobile.videocutter.presentation.tasselsvideo

import com.mobile.videocutter.base.common.BaseViewModel

class TasselsVideoViewModel : BaseViewModel() {

    var videoPath: String? = null
    var playbackPosition: Long = 0L
    var currentItem: Int = 0
    var playWhenReady: Boolean = true
    var isCheck: Boolean = false
}
