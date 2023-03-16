package com.mobile.videocutter.presentation.cutvideo

import android.graphics.Bitmap
import com.mobile.videocutter.base.common.BaseViewModel

class CutVideoViewModel : BaseViewModel() {
    var totalTime: Long = 0L
    var videoPathType: String? = null
    var bitmapList: List<Bitmap> = arrayListOf()
    var isCheck = true
}
