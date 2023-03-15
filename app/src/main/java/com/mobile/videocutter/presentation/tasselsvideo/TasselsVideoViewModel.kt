package com.mobile.videocutter.presentation.tasselsvideo

import com.mobile.videocutter.base.common.BaseViewModel
import com.mobile.videocutter.domain.model.VIDEO_ACTIVE_TYPE

class TasselsVideoViewModel : BaseViewModel() {

    var videoPath: String? = null
    var playbackPosition: Long = 0L
    var currentItem: Int = 0
    var playWhenReady: Boolean = true
    var isCheck: VIDEO_ACTIVE_TYPE = VIDEO_ACTIVE_TYPE.PAUSE_VIDEO
}
