package com.mobile.videocutter.presentation

import android.os.Handler
import com.mobile.videocutter.base.common.BaseViewModel

class PlayerViewModel: BaseViewModel() {
    var listPath: List<String>? = null
    var mHandler: Handler? = null
    var degree: Float = 0f
    var flipHorizontal: Boolean = false
    var flipVertical: Boolean = false
}
