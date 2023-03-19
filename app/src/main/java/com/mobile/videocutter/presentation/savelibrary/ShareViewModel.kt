package com.mobile.videocutter.presentation.savelibrary

import androidx.lifecycle.viewModelScope
import com.mobile.videocutter.base.common.BaseUseCase
import com.mobile.videocutter.base.common.BaseViewModel
import com.mobile.videocutter.thread.FlowResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import success

class ShareViewModel : BaseViewModel() {

    private val _appInfoList = MutableStateFlow(FlowResult.newInstance<List<AppInfo>>())
    val appInfoList = _appInfoList.asStateFlow()

    init {
        getAppInfoList()
    }

    private fun getAppInfoList() {
        viewModelScope.launch {
            GetPackageAppInfoListUseCase().invoke(BaseUseCase.VoidRequest())
                .onStart {

                }.collect {
                    _appInfoList.success(it)
                }
        }
    }
}
