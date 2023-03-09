package com.mobile.videocutter.presentation.adjust

import androidx.lifecycle.viewModelScope
import com.mobile.videocutter.base.common.BaseViewModel
import com.mobile.videocutter.domain.model.LocalVideo
import com.mobile.videocutter.domain.usecase.adjust.GetLocalVideoAdjustUseCase
import com.mobile.videocutter.thread.FlowResult
import data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import onException
import success

class AdjustViewModel : BaseViewModel() {

    private var _localVideoAdjust = MutableStateFlow(FlowResult.newInstance<List<Any>>())

    val localVideoAdjust = _localVideoAdjust.asStateFlow()

    init {
        mockLocalVideoAdjust()
    }

    private fun mockLocalVideoAdjust() {
        viewModelScope.launch(Dispatchers.IO) {
            val rv = GetLocalVideoAdjustUseCase.GetLocalVideoAdjustUseCaseRV()
            GetLocalVideoAdjustUseCase().invoke(rv)
                .onStart {

                }
                .onException {

                }
                .collect {
                    _localVideoAdjust.success(it)
                }
        }
    }

    fun deleteLocalVideo(localVideo: LocalVideo) {
        viewModelScope.launch(Dispatchers.IO) {
            /**
             * phải tạo ra một list mới ko thì stateFlow sẽ ko biết rằng cái đối tượng đó đã thay đổi
             */
            val list = _localVideoAdjust.data()?.toMutableList()

            val item = list?.find {
                it == localVideo
            }

            if (item != null) {
                list.remove(item)
                _localVideoAdjust.success(list)
            }
        }
    }
}
