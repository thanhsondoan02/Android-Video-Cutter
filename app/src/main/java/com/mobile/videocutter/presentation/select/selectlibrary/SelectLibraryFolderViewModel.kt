package com.mobile.videocutter.presentation.select.selectlibrary

import com.mobile.videocutter.base.common.model.Album
import com.mobile.videocutter.thread.FlowResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SelectLibraryFolderViewModel {
    private val _libraryFolderState = MutableStateFlow(FlowResult.newInstance<Album>())
    val libraryFolderState = _libraryFolderState.asStateFlow()

}
