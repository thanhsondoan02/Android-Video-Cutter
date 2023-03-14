package com.mobile.videocutter.presentation.select.selectvideo

import android.content.ContentResolver
import androidx.lifecycle.viewModelScope
import com.mobile.videocutter.base.common.BaseViewModel
import com.mobile.videocutter.base.extension.getApplication
import com.mobile.videocutter.domain.model.Album
import com.mobile.videocutter.domain.model.LocalVideo
import com.mobile.videocutter.domain.model.Video
import com.mobile.videocutter.domain.usecase.GetAlbumListUseCase
import com.mobile.videocutter.domain.usecase.GetVideoListUseCase
import com.mobile.videocutter.thread.FlowResult
import failure
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import loading
import onException
import success

class SelectVideoViewModel : BaseViewModel() {
    var idAlbum = ""
    var nameAlbum = ""
    var listVideoAdd = mutableListOf<SelectVideoAdapter.VideoDisplay>()

    private val _selectVideoState = MutableStateFlow(FlowResult.newInstance<List<LocalVideo>>())
    val selectVideoState = _selectVideoState.asStateFlow()

    fun getVideoList(idAlbum: String) {
        viewModelScope.launch {
            val rv = GetVideoListUseCase.GetVideoListRV(idAlbum)
            GetVideoListUseCase().invoke(rv)
                .onStart {
                    _selectVideoState.loading()
                }
                .onException {
                    _selectVideoState.failure(it)
                }
                .collect {
                    _selectVideoState.success(it)
                }
        }
    }

    fun getListPath(): ArrayList<String> {
        val arrayList = arrayListOf<String>()
        listVideoAdd.forEach {
            if (it.video.videoPath != null) {
                arrayList.add(it.video.videoPath!!)
            }
        }
        return arrayList
    }
}
