package com.mobile.videocutter.presentation.tasselsvideo

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Handler
import androidx.lifecycle.viewModelScope
import com.mobile.videocutter.base.common.BaseViewModel
import com.mobile.videocutter.base.extension.LONG_DEFAULT
import com.mobile.videocutter.domain.model.FILTER_TYPE
import com.mobile.videocutter.domain.model.Filter
import com.mobile.videocutter.domain.usecase.GetBitMapListUseCase
import com.mobile.videocutter.thread.FlowResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import onException
import success

class TasselsVideoViewModel : BaseViewModel() {

    // Video state variable
    var listPath: List<String>? = null
    var mHandler: Handler? = null
    var degree: Float = 0f
    var flipHorizontal: Boolean = false
    var flipVertical: Boolean = false
    var ratio = Float.MAX_VALUE
    var resolutionHeight = 0
    var resolutionWidth = 0
    var filter = Filter(FILTER_TYPE.ORIGINAL)
    // end region

    private var _bitmapTimeLineList = MutableStateFlow(FlowResult.newInstance<List<Bitmap>>())
    val bitmapTimeLineList = _bitmapTimeLineList

    var videoPathCurrent: String? = null
    var isCheck: Boolean = false
    var totalTime: Long = LONG_DEFAULT
    var stepTime: Long = 2500L

    fun getBitMapList(heightDefault: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (videoPathCurrent != null) {
                val rv = GetBitMapListUseCase.GetBitMapListRV(
                    heightDefault,
                    stepTime,
                    videoPathCurrent!!,
                    totalTime)

                GetBitMapListUseCase().invoke(rv)
                    .onStart {

                    }
                    .onException {

                    }
                    .collect {
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
}
