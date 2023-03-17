package com.mobile.videocutter.presentation.cutvideo

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.mobile.videocutter.base.common.BaseViewModel
import com.mobile.videocutter.domain.usecase.GetBitMapCutVideoListUseCase
import com.mobile.videocutter.thread.FlowResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import onException
import success

class CutVideoViewModel : BaseViewModel() {

    private var _bitmapCutVideoList = MutableStateFlow(FlowResult.newInstance<List<Bitmap>>())
    val bitmapCutVideoList = _bitmapCutVideoList.asStateFlow()

    var totalTime: Long = 0L
    var videoPathType: String? = null
    var bitmapList: List<Bitmap> = arrayListOf()
    var isCheckStartPause = true
    var isCheckReplace = false

    fun getBitmapCutVideoList(heightBitmapScaled: Int, maxWidth: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (videoPathType != null) {
                val rv = GetBitMapCutVideoListUseCase.GetBitMapCutVideoListRV(
                    videoPathType!!,
                    totalTime,
                    heightBitmapScaled,
                    maxWidth)

                GetBitMapCutVideoListUseCase().invoke(rv)
                    .onStart {

                    }
                    .onException {

                    }
                    .collect {
                        _bitmapCutVideoList.success(it)
                    }
            }
        }
    }
}
