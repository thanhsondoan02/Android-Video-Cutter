package com.mobile.videocutter.presentation.cropvideo

import androidx.lifecycle.viewModelScope
import com.mobile.videocutter.base.common.BaseUseCase
import com.mobile.videocutter.base.common.BaseViewModel
import com.mobile.videocutter.domain.usecase.GetRatioCropVideoUseCase
import com.mobile.videocutter.thread.FlowResult
import data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import onException
import success

class CropVideoViewModel : BaseViewModel() {

    private val _ratioCropVideo = MutableStateFlow(FlowResult.newInstance<MutableList<RatioAdapter.RatioDisplay>>())
    val ratioCropVideo = _ratioCropVideo.asStateFlow()

    init {

    }

    fun getRatio() {
        viewModelScope.launch {
            GetRatioCropVideoUseCase().invoke(BaseUseCase.VoidRequest())
                .onStart { }
                .onException { }
                .collect {
                    _ratioCropVideo.success(it.toMutableList())
                }
        }
    }

    fun updateNewRatio(ratioDisplay: RatioAdapter.RatioDisplay) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = _ratioCropVideo.data()?.toMutableList()

            val item = list?.find {
                it == ratioDisplay
            }

            val postion = list?.indexOfFirst {
                it == ratioDisplay
            }

            if (item != null && postion != null) {
                val newData = RatioAdapter.RatioDisplay(item.type,true)
                list[postion] = newData
                _ratioCropVideo.success(list)
            }
        }
    }

    fun updateOldRatio(ratioDisplay: RatioAdapter.RatioDisplay) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = _ratioCropVideo.data()?.toMutableList()

            val item = list?.find {
                it == ratioDisplay
            }

            val postion = list?.indexOfFirst {
                it == ratioDisplay
            }

            if (item != null && postion != null) {
                val newData = RatioAdapter.RatioDisplay(item.type,false)
                list[postion] = newData
                _ratioCropVideo.success(list)
            }
        }
    }
}
