package com.mobile.videocutter.presentation.adjust.crop

import android.os.Handler
import com.mobile.videocutter.base.common.BaseViewModel

class CropViewModel: BaseViewModel(){
    var path: String? = null
    var isInitMax = false
    var mHandler: Handler? = null
    var resolutionHeight: Int? = null
    var resolutionWidth: Int? = null
}
