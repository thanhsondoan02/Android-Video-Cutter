package com.mobile.videocutter.presentation.home.mystudio

import com.mobile.videocutter.base.common.BaseViewModel
import com.mobile.videocutter.thread.FlowResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyStudioViewModel: BaseViewModel() {
    private val _myStudioVideoState = MutableStateFlow(FlowResult.newInstance<Unit>())
    val myStudioVideoState = _myStudioVideoState.asStateFlow()

    fun getMyStudioVideos() {

    }
}
