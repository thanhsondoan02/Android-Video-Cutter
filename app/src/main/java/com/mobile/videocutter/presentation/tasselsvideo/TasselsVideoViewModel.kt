package com.mobile.videocutter.presentation.tasselsvideo

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Handler
import androidx.lifecycle.viewModelScope
import com.mobile.videocutter.base.common.BaseViewModel
import com.mobile.videocutter.base.extension.LONG_DEFAULT
import com.mobile.videocutter.domain.model.FILTER_TYPE
import com.mobile.videocutter.domain.model.Filter
import com.mobile.videocutter.domain.model.SPEED_TYPE
import com.mobile.videocutter.domain.model.Speed
import com.mobile.videocutter.domain.usecase.GetBitMapCutVideoListUseCase
import com.mobile.videocutter.domain.usecase.GetBitMapListUseCase
import com.mobile.videocutter.thread.FlowResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import onException
import success

class TasselsVideoViewModel : BaseViewModel() {

    // Video state variable
    var listPath: List<String>? = null
    var listDuration: List<Long>? = null
        set(value) {
            field = value
            totalDuration = getCalculatedTotalDuration()
        }
    var totalDuration: Long = 0L
    var mHandler: Handler? = null
    var degree: Float = 0f
    var flipHorizontal: Boolean = false
    var flipVertical: Boolean = false
    var ratio = Float.MAX_VALUE
    var resolutionHeight = 0
    var resolutionWidth = 0
    var filter = Filter(FILTER_TYPE.ORIGINAL)
    var speed: Speed = Speed(SPEED_TYPE.SPEED_1)
    // end region

    private var _bitmapTimeLineList = MutableStateFlow(FlowResult.newInstance<List<Bitmap>>())
    val bitmapTimeLineList = _bitmapTimeLineList

    var videoPathCurrent: String? = null
    var isCheck: Boolean = false
    var totalTime: Long = LONG_DEFAULT
    var stepTime: Long = 2500L

    fun getBeforeDuration(index: Int): Long {
        var beforeDuration = 0L
        for (i in 0 until index) {
            beforeDuration += listDuration?.get(i) ?: 0L
        }
        return beforeDuration
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

    fun getBitMapList(heightDefault: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (listPath != null && listDuration != null) {

                val rv = GetBitMapListUseCase.GetBitMapListRV(heightDefault, stepTime, listPath!!, listDuration!!)

                GetBitMapListUseCase().invoke(rv).onStart {

                }.onException {

                }.collect {
                    _bitmapTimeLineList.success(it)
                }
            }
        }
    }

    /**
     * Lấy theo tỉ lệ của video đầu tiên
     */
    fun calculateVideoRatio() {
        listPath?.firstOrNull()?.let {
            ratio = getVideoRatio(it)
        }
    }

    /**
     * resolutionHeight bằng resolutionHeight video đầu
     * resolutionWidth bằng resolutionWidth video đầu
     */
    fun calculateResolution() {
        listPath?.firstOrNull()?.let {
            resolutionHeight = getResolutionHeight(it)
            resolutionWidth = getResolutionWidth(it)
        }
    }

    private var _bitmapCutVideoList = MutableStateFlow(FlowResult.newInstance<List<Bitmap>>())
    val bitmapCutVideoList = _bitmapCutVideoList.asStateFlow()

    fun getBitmapCutVideoList(heightBitmapScaled: Int, maxWidth: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (listPath?.firstOrNull() != null) {
                val rv = GetBitMapCutVideoListUseCase.GetBitMapCutVideoListRV(listPath?.firstOrNull()!!, totalTime, heightBitmapScaled, maxWidth)

                GetBitMapCutVideoListUseCase().invoke(rv).onStart {

                }.onException {

                }.collect {
                    _bitmapCutVideoList.success(it)
                }
            }
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

    private fun getResolutionHeight(path: String): Int {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(path)
        val videoHeight = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
        if (videoHeight != null) {
            return videoHeight.toInt()
        }
        return -1
    }

    private fun getResolutionWidth(path: String): Int {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(path)
        val videoWidth = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
        if (videoWidth != null) {
            return videoWidth.toInt()
        }
        return -1
    }

    private fun getCalculatedTotalDuration(): Long {
        var totalDuration = 0L
        listDuration?.forEach {
            totalDuration += it
        }
        return totalDuration
    }
}
