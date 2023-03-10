package com.mobile.videocutter.presentation.select.selectlibrary

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.videocutter.base.common.BaseViewModel
import com.mobile.videocutter.base.extension.getApplication
import com.mobile.videocutter.domain.model.Album
import com.mobile.videocutter.domain.usecase.GetAlbumListUseCase
import com.mobile.videocutter.thread.FlowResult
import failure
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import loading
import onException
import success

class SelectLibraryFolderViewModel : BaseViewModel() {

    private val _selectLibraryFolderState = MutableStateFlow(FlowResult.newInstance<List<Album>>())
    val selectLibraryFolderState = _selectLibraryFolderState.asStateFlow()

    private val contentResolver = getApplication().contentResolver

    fun getAlbumList() {
        viewModelScope.launch {
            val rv = GetAlbumListUseCase.GetAlbumListRV(contentResolver)
            GetAlbumListUseCase().invoke(rv)
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
