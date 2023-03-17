package com.mobile.videocutter.presentation.tasselsvideo

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.mobile.videocutter.base.common.BaseViewModel
import com.mobile.videocutter.base.extension.LONG_DEFAULT
import com.mobile.videocutter.domain.usecase.GetBitMapListUseCase
import com.mobile.videocutter.thread.FlowResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import onException
import success

class TasselsVideoViewModel : BaseViewModel() {

    private var _bitmapTimeLineList = MutableStateFlow(FlowResult.newInstance<List<Bitmap>>())
    val bitmapTimeLineList = _bitmapTimeLineList

    var videoPathCurrent: String? = null
    var isCheck: Boolean = false
    var totalTime: Long = LONG_DEFAULT
    var stepTime: Long = 2500L

    fun getBitMapList(heightDefault: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (videoPathCurrent != null) {
                val rv = GetBitMapListUseCase.GetBitMapListRV(
                    heightDefault,
                    stepTime,
                    videoPathCurrent!!,
                    totalTime)

                GetBitMapListUseCase().invoke(rv)
                    .onStart {

                    }
                    .onException {

                    }
                    .collect {
                        _bitmapTimeLineList.success(it)
                    }
            }
        }
    }
}
