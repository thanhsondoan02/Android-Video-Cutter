package com.mobile.videocutter.presentation.tasselsvideo

import android.os.Handler
import com.mobile.videocutter.base.common.BaseViewModel

class TasselsVideoViewModel : BaseViewModel() {

    // Video state variable
    var listPath: List<String>? = null
    var mHandler: Handler? = null
    var degree: Float = 0f
    var flipHorizontal: Boolean = false
    var flipVertical: Boolean = false
    // end region


    var videoPath: String? = null
    var playbackPosition: Long = 0L
    var currentItem: Int = 0
    var playWhenReady: Boolean = true
    var isCheck: Boolean = false
}
