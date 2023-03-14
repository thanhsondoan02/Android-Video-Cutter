package com.mobile.videocutter.presentation.filter

import androidx.lifecycle.viewModelScope
import com.mobile.videocutter.base.common.BaseUseCase
import com.mobile.videocutter.base.common.BaseViewModel
import com.mobile.videocutter.domain.usecase.GetListFilterUseCase
import com.mobile.videocutter.thread.FlowResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import onException
import success

class FilterVideoModel : BaseViewModel() {
    private var _filter = MutableStateFlow(FlowResult.newInstance<List<FilterAdapter.FilterDisplay>>())
    val filter = _filter.asStateFlow()

    init {

    }

    fun getFilter() {
        viewModelScope.launch {
            GetListFilterUseCase().invoke(BaseUseCase.VoidRequest())
                .onStart {

                }
                .onException {

                }
                .collect {
                    _filter.success(it)
                }
        }
    }
}
