package com.mobile.videocutter.presentation.select.selectlibrary

import androidx.lifecycle.viewModelScope
import com.mobile.videocutter.base.common.BaseUseCase
import com.mobile.videocutter.base.common.BaseViewModel
import com.mobile.videocutter.domain.model.Album
import com.mobile.videocutter.domain.usecase.GetAlbumListUseCase
import com.mobile.videocutter.thread.FlowResult
import failure
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import loading
import onException
import success

class SelectLibraryFolderViewModel : BaseViewModel() {

    private val _selectLibraryFolderState = MutableStateFlow(FlowResult.newInstance<List<Album>>())
    val selectLibraryFolderState = _selectLibraryFolderState.asStateFlow()

    fun getAlbumList() {
        viewModelScope.launch {
            GetAlbumListUseCase().invoke(BaseUseCase.VoidRequest())
                .onStart {
                    _selectLibraryFolderState.loading()
                }
                .onException {
                    _selectLibraryFolderState.failure(it)
                }
                .collect {
                    _selectLibraryFolderState.success(it)
                }
        }
    }
}
