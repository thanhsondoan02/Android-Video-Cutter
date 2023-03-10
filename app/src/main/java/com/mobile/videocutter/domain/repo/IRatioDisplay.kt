package com.mobile.videocutter.domain.repo

import com.mobile.videocutter.presentation.cropvideo.RatioAdapter

interface IRatioDisplay {
    fun getListRatio(): List<RatioAdapter.RatioDisplay>
}
