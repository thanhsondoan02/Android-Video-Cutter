package com.mobile.videocutter.presentation.addmusic

import androidx.lifecycle.viewModelScope
import com.mobile.videocutter.base.common.BaseUseCase
import com.mobile.videocutter.base.common.BaseViewModel
import com.mobile.videocutter.domain.model.MusicTrack
import com.mobile.videocutter.domain.usecase.GetMusicTrackListUseCase
import com.mobile.videocutter.thread.FlowResult
import failure
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import loading
import onException
import success

class MusicTrackViewModel : BaseViewModel() {
    private val _musicTrackState = MutableStateFlow(FlowResult.newInstance<List<MusicTrack>>())
    val musicTrackState = _musicTrackState.asStateFlow()

    fun getAddMusicTrack() {
        viewModelScope.launch {
            GetMusicTrackListUseCase().invoke(BaseUseCase.VoidRequest())
                .onStart {
                    _musicTrackState.loading()
                }.onException {
                    _musicTrackState.failure(it)
                }.collect {
                    _musicTrackState.success(it)
                }
        }
    }
}
