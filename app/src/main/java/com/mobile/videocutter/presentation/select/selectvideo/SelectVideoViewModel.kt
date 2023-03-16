package com.mobile.videocutter.presentation.select.selectvideo

import androidx.lifecycle.viewModelScope
import com.mobile.videocutter.base.common.BaseUseCase
import com.mobile.videocutter.base.common.BaseViewModel
import com.mobile.videocutter.base.extension.STRING_DEFAULT
import com.mobile.videocutter.domain.model.Album
import com.mobile.videocutter.domain.model.VideoDisplay
import com.mobile.videocutter.domain.usecase.GetAlbumListUseCase
import com.mobile.videocutter.domain.usecase.GetVideoListInAlbumUseCase
import com.mobile.videocutter.thread.FlowResult
import failure
import kotlinx.coroutines.Dispatchers
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
    var listVideoAdd = mutableListOf<VideoDisplay>()

    private val _selectVideoState = MutableStateFlow(FlowResult.newInstance<List<VideoDisplay>>())
    val selectVideoState = _selectVideoState.asStateFlow()

    private val _video = MutableStateFlow(FlowResult.newInstance<List<VideoDisplay>>())
    val video = _video.asStateFlow()

    private val _selectLibraryFolderState = MutableStateFlow(FlowResult.newInstance<List<Album>>())
    val selectLibraryFolderState = _selectLibraryFolderState.asStateFlow()

    private val _count = MutableStateFlow(FlowResult.newInstance<Int>())
    val count = _count.asStateFlow()

    private var listVideoDisplay: MutableList<VideoDisplay>? = null

    init {

    }

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
                    val list = it.map {
                        VideoDisplay(it)
                    }
                    _selectVideoState.success(list)
                }
        }
    }

    fun updateVideoAdded(list: MutableList<VideoDisplay>) {
        viewModelScope.launch(Dispatchers.IO) {
            val listPath = list.map {
                it.video.getPathVideo()
            }

            // chưa hiểu tại sao thằng này ko cần update nên main thread
            listVideoAdd.removeIf {
                listPath.contains(it.video.getPathVideo())
            }
            _count.success(listVideoAdd.size)
        }
    }

    fun updateVideoSelected(list: MutableList<VideoDisplay>) {
        viewModelScope.launch(Dispatchers.IO) {
            val oldList = _selectVideoState.value.data?.toMutableList()

            list.forEachIndexed { newIndex, _ ->

                val item = oldList?.find {
                    it.video.getPathVideo() == list[newIndex].video.getPathVideo()
                }

                val position = oldList?.indexOfFirst {
                    it.video.getPathVideo() == list[newIndex].video.getPathVideo()
                }

                if (item != null && position != null && position > -1) {
                    val newItem = VideoDisplay(item.video, false)
                    oldList[position] = newItem
                }
            }
            _selectVideoState.success(oldList?.toList() ?: listOf())
        }
    }

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

    fun getListSelected(): MutableList<VideoDisplay> {
        return listVideoAdd
    }

    fun addVideoDisplay(videoDisplay: VideoDisplay) {
        viewModelScope.launch(Dispatchers.IO) {
            listVideoDisplay?.clear()
            listVideoDisplay = _video.value.data?.toMutableList()
        }
    }
}
