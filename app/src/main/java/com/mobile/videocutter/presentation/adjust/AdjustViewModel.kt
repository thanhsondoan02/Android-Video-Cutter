package com.mobile.videocutter.presentation.adjust

import androidx.lifecycle.viewModelScope
import com.mobile.videocutter.base.common.BaseViewModel
import com.mobile.videocutter.base.extension.STRING_DEFAULT
import com.mobile.videocutter.domain.model.VideoDisplay
import com.mobile.videocutter.thread.FlowResult
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

    private var paths: MutableList<String>? = null

    var listVideo: MutableList<VideoDisplay> = arrayListOf()

    var listVideoDelete: MutableList<VideoDisplay> = arrayListOf()

    init {

    }

    fun getListPath() {
        viewModelScope.launch(Dispatchers.IO) {
            val list: MutableList<String> = arrayListOf()
            if (listVideo.isNotEmpty()) {
                listVideo.forEach {
                    list.add(it.video.getPathVideo())
                }
                _listPath.success(list)
            }
        }
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

    fun deleteVideo(videoDisplay: VideoDisplay) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = _localVideoAdjust.value.data?.toMutableList()
            paths = _listPath.value.data?.toMutableList()

            val item = list?.find {
                it == videoDisplay
            }

            if (item != null) {
                paths?.remove((item as VideoDisplay).video.getPathVideo())
                list.remove(item)
                listVideoDelete.add(item as VideoDisplay)
                _localVideoAdjust.success(list)
                _listPath.success(paths?.toList() ?: listOf())
            }
        }
    }

    fun dragVideo(oldPosion: Int, newPosition: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            paths = _listPath.value.data?.toMutableList()

            paths?.let {
                Collections.swap(it, oldPosion, newPosition)
            }
            _listPath.success(paths ?: listOf())
        }
    }
}
