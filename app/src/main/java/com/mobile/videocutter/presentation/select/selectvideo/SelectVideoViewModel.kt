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
import data
import failure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import loading
import onException
import success
import java.util.*

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

            listVideoDisplay = _video.data()?.toMutableList()

            // chưa hiểu tại sao thằng này ko cần update nên main thread
            listVideoDisplay?.removeIf {
                listPath.contains(it.video.getPathVideo())
            }
            _video.success(listVideoDisplay?: listOf())
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
        return _video.data()?.toMutableList() ?: arrayListOf()
    }

    fun addVideoDisplay(videoDisplay: VideoDisplay) {
        viewModelScope.launch(Dispatchers.IO) {
            listVideoDisplay = if (_video.data() == null) {
                arrayListOf()
            } else {
                _video.data()!!.toMutableList()
            }
            val listVideo = _selectVideoState.data()?.toMutableList()
            val item = listVideo?.find {
                it == videoDisplay
            }

            val position = listVideo?.indexOfFirst {
                it == videoDisplay
            }

            if (item != null && position != null && position > -1) {
                val newItem = VideoDisplay(item.video, true)
                listVideo[position] = newItem
                _selectVideoState.success(listVideo)
            }

            listVideoDisplay?.add(videoDisplay)
            _video.success(listVideoDisplay ?: listOf())
        }
    }

    fun removeVideoDisplay(videoDisplay: VideoDisplay) {
        viewModelScope.launch(Dispatchers.IO) {
            val listVideo = _selectVideoState.data()?.toMutableList()

            val item = listVideo?.find {
                it.video == videoDisplay.video
            }

            val position = listVideo?.indexOfFirst {
                it.video == videoDisplay.video
            }

            if (item != null && position != null && position > -1) {
                val newItem = VideoDisplay(item.video, false)
                listVideo[position] = newItem
                _selectVideoState.success(listVideo)
            }

            listVideoDisplay = _video.value.data?.toMutableList()
            listVideoDisplay?.remove(videoDisplay)

            _video.success(listVideoDisplay ?: listOf())
        }
    }

    fun dragVideo(oldPosition: Int, newPosition: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            listVideoDisplay = _video.data()?.toMutableList()
            listVideoDisplay?.let { Collections.swap(it, oldPosition, newPosition) }
            _video.success(listVideoDisplay ?: listOf())
        }
    }
}
