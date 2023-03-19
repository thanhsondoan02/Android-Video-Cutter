package com.mobile.videocutter.presentation.adjust

import androidx.lifecycle.viewModelScope
import com.mobile.videocutter.base.common.BaseUseCase
import com.mobile.videocutter.base.common.BaseViewModel
import com.mobile.videocutter.domain.usecase.LoadingListVideoUseCase
import com.mobile.videocutter.thread.FlowResult
import failure
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import loading
import onException
import success

class AdjustFragmentViewModel: BaseViewModel() {
    var isEnableLoading = false

    private val _loadingState = MutableStateFlow(FlowResult.newInstance<Int>())
    val loadingState = _loadingState.asStateFlow()

    fun loadingVideo() {
        isEnableLoading = true
        viewModelScope.launch {
            LoadingListVideoUseCase().invoke(BaseUseCase.VoidRequest())
                .onStart {
                    _loadingState.loading()
                }
                .onException {
                    _loadingState.failure(it)
                }
                .collect {
                    _loadingState.success(it)
                }
        }
    }
}
