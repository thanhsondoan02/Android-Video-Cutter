package com.mobile.videocutter.presentation.adjust

import androidx.lifecycle.viewModelScope
import com.mobile.videocutter.base.common.BaseViewModel
import com.mobile.videocutter.base.extension.STRING_DEFAULT
import com.mobile.videocutter.domain.model.VideoDisplay
import com.mobile.videocutter.thread.FlowResult
import data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import success
import java.util.*

class AdjustViewModel : BaseViewModel() {
    private var _localVideoAdjust = MutableStateFlow(FlowResult.newInstance<List<Any>>())
    val localVideoAdjust = _localVideoAdjust.asStateFlow()

    private var _listPath = MutableStateFlow(FlowResult.newInstance<List<String>>())
    val listPath = _listPath.asStateFlow()

    var listVideo: MutableList<VideoDisplay> = arrayListOf()

    var listVideoDelete: MutableList<VideoDisplay> = arrayListOf()

    var totalDuration = 0L

    var isPlay = false

    init {

    }

    fun getListVideo() {
        viewModelScope.launch(Dispatchers.IO) {
            val list: MutableList<Any> = arrayListOf()
            if (listVideo.isNotEmpty()) {
                list.addAll(listVideo)
                list.add(STRING_DEFAULT)
                _localVideoAdjust.success(list)
            }
        }
    }

    fun getPathsVideo() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = _localVideoAdjust.data()?.toMutableList() ?: return@launch
            val listPath: MutableList<String> = arrayListOf()
            totalDuration = 0L

            list.forEach {
                if (it is VideoDisplay) {
                    listPath.add(it.video.getPathVideo())
                    totalDuration += it.video.duration
                    _listPath.success(listPath)
                }
            }
        }
    }

    fun deleteVideo(videoDisplay: VideoDisplay) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = _localVideoAdjust.value.data?.toMutableList()

            val item = list?.find {
                it == videoDisplay
            }

            if (item != null) {
                list.remove(item)

                listVideoDelete.add(item as VideoDisplay)
                totalDuration -= (item).video.duration

                _localVideoAdjust.success(list)
                isPlay = false
            }
        }
    }

    fun dragVideo(oldPosion: Int, newPosition: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = _localVideoAdjust.value.data?.toMutableList()

            list?.let {
                Collections.swap(it, oldPosion, newPosition)
            }

            isPlay = false
            _localVideoAdjust.success(list!!)
        }
    }
}
