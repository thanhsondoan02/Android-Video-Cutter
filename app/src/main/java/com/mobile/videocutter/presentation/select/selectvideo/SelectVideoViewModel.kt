package com.mobile.videocutter.presentation.select.selectvideo

import androidx.lifecycle.viewModelScope
import com.mobile.videocutter.base.common.BaseUseCase
import com.mobile.videocutter.base.common.BaseViewModel
import com.mobile.videocutter.base.extension.STRING_DEFAULT
import com.mobile.videocutter.domain.model.Album
import com.mobile.videocutter.domain.model.LocalVideo
import com.mobile.videocutter.domain.usecase.GetAlbumListUseCase
import com.mobile.videocutter.domain.usecase.GetVideoListInAlbumUseCase
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
    var idAlbum: String? = STRING_DEFAULT
    var nameAlbum: String? = STRING_DEFAULT
    var listVideoAdd = mutableListOf<SelectVideoAdapter.VideoDisplay>()

    private val _selectVideoState = MutableStateFlow(FlowResult.newInstance<List<LocalVideo>>())
    val selectVideoState = _selectVideoState.asStateFlow()

    fun getVideoList(idAlbum: String? = null) {
        viewModelScope.launch {
            val rv = GetVideoListInAlbumUseCase.GetVideoListRV(idAlbum)
            GetVideoListInAlbumUseCase().invoke(rv)
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

    private val _selectLibraryFolderState = MutableStateFlow(FlowResult.newInstance<List<Album>>())
    val selectLibraryFolderState = _selectLibraryFolderState.asStateFlow()

    fun getAlbumList() {
        viewModelScope.launch {
            GetAlbumListUseCase().invoke(BaseUseCase.VoidRequest())
                .onStart {
                    _selectLibraryFolderState.loading()
                }
                .onException {
                    _selectLibraryFolderState.failure(it)
                }
                .collect { albumList ->
                    var count = 0
                    albumList.forEach {
                        if (it.countAlbum != null) {
                            count += it.countAlbum!!
                        }
                    }

                    _selectLibraryFolderState.success(albumList.toMutableList().apply {
                        add(
                            index = 0,
                            element = Album().apply {
                                idAlbum = null
                                nameAlbum = "Video"
                                countAlbum = count
                            }
                        )
                    })
                }
        }
    }
}
