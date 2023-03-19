package com.mobile.videocutter.presentation.select.selectvideo

import android.media.MediaMetadataRetriever
import android.os.Handler
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
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
    val listVideoAdd = initListAdd()
    var mHandler: Handler? = null
    var listPath: List<String>? = null
    var listDuration: List<Long>? = null
        set(value) {
            field = value
            totalDuration = getCalculatedTotalDuration()
        }
    var totalDuration: Long = 0L
    var ratio = Float.MAX_VALUE


    private val _selectVideoState = MutableStateFlow(FlowResult.newInstance<List<LocalVideo>>())
    val selectVideoState = _selectVideoState.asStateFlow()

    private val _selectLibraryFolderState = MutableStateFlow(FlowResult.newInstance<List<Album>>())
    val selectLibraryFolderState = _selectLibraryFolderState.asStateFlow()

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
            if (it.videoPath != null) {
                arrayList.add(it.videoPath!!)
            }
        }
        return arrayList
    }

    fun getListDuration(): LongArray {
        val arrayList = arrayListOf<Long>()
        listVideoAdd.forEach {
            arrayList.add(it.duration)
        }
        return arrayList.toLongArray()
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

    fun calculateVideoRatio() {
        listPath?.firstOrNull()?.let {
            ratio = getVideoRatio(it)
        }
    }

    private fun getVideoRatio(path: String): Float {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(path)
        val videoWidth = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
        val videoHeight = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
        if (videoWidth != null && videoHeight != null) {
            return videoWidth.toFloat() / videoHeight.toFloat()
        }
        return -1f
    }

    fun getCurrentIndex(progress: Long): Int {
        if (listDuration != null) {
            var beforeDuration = 0L
            for (i in 0 until listDuration!!.size) {
                beforeDuration += listDuration!![i]
                if (progress <= beforeDuration) {
                    return i
                }
            }
        }
        return 0
    }

    fun getBeforeDuration(index: Int): Long {
        var beforeDuration = 0L
        for (i in 0 until index) {
            beforeDuration += listDuration?.get(i) ?: 0L
        }
        return beforeDuration
    }

    private fun getCalculatedTotalDuration(): Long {
        var totalDuration = 0L
        listDuration?.forEach {
            totalDuration += it
        }
        return totalDuration
    }

    private fun initListAdd(): ObservableArrayList<LocalVideo> {
        return ObservableArrayList<LocalVideo>().apply {
            // set on change listener
            addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableList<LocalVideo>>() {
                override fun onChanged(sender: ObservableList<LocalVideo>?) {
                    listPath = getListPath()
                    listDuration = getListDuration().toList()
                }

                override fun onItemRangeChanged(sender: ObservableList<LocalVideo>?, positionStart: Int, itemCount: Int) {
                    listPath = getListPath()
                    listDuration = getListDuration().toList()
                }

                override fun onItemRangeInserted(sender: ObservableList<LocalVideo>?, positionStart: Int, itemCount: Int) {
                    listPath = getListPath()
                    listDuration = getListDuration().toList()
                }

                override fun onItemRangeMoved(sender: ObservableList<LocalVideo>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
                    listPath = getListPath()
                    listDuration = getListDuration().toList()
                }

                override fun onItemRangeRemoved(sender: ObservableList<LocalVideo>?, positionStart: Int, itemCount: Int) {
                    listPath = getListPath()
                    listDuration = getListDuration().toList()
                }
            })
        }
    }
}
