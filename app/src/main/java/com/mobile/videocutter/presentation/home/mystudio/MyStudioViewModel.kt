package com.mobile.videocutter.presentation.home.mystudio

import androidx.lifecycle.viewModelScope
import com.mobile.videocutter.base.common.BaseUseCase
import com.mobile.videocutter.base.common.BaseViewModel
import com.mobile.videocutter.domain.model.LocalVideo
import com.mobile.videocutter.domain.usecase.GetMyStudioVideoListUseCase
import com.mobile.videocutter.thread.FlowResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import loading
import success

class MyStudioViewModel: BaseViewModel() {
    private val _myStudioVideoState = MutableStateFlow(FlowResult.newInstance<List<LocalVideo>>())
    val myStudioVideoState = _myStudioVideoState.asStateFlow()

    fun getMyStudioVideos() {
        viewModelScope.launch {
            GetMyStudioVideoListUseCase().invoke(BaseUseCase.VoidRequest())
                .onStart {
                    _myStudioVideoState.loading()
                }.collect {
                    _myStudioVideoState.success(it)
                }
        }
    }
}
