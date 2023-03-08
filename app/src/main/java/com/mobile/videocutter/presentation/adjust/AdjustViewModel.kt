package com.mobile.videocutter.presentation.adjust

import androidx.lifecycle.viewModelScope
import com.mobile.videocutter.base.common.BaseViewModel
import com.mobile.videocutter.domain.model.LocalVideo
import com.mobile.videocutter.domain.model.mockLocalVideoList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdjustViewModel : BaseViewModel() {

    private var _localVideoAdjust = MutableStateFlow<MutableList<Any>>(arrayListOf())
    val localVideoAdjust = _localVideoAdjust.asStateFlow()

    init {
        mockLocalVideoAdjust()
    }

    private fun mockLocalVideoAdjust() {
        viewModelScope.launch(Dispatchers.IO) {
            val list: MutableList<Any> = arrayListOf()
            list.addAll(mockLocalVideoList(4))
            list.add(Any())
            _localVideoAdjust.value = list
        }
    }

    fun deleteLocalVideo(localVideo: LocalVideo) {
        viewModelScope.launch(Dispatchers.IO) {
            /**
             * phải tạo ra một list mới ko thì stateFlow sẽ ko biết rằng cái đối tượng đó đã thay đổi
             */
            val list = _localVideoAdjust.value.toMutableList()

            val item = list.find {
                it == localVideo
            }

            if (item != null) {
                list.remove(item)
                _localVideoAdjust.value = list
            }
        }
    }
}
