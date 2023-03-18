package com.mobile.videocutter.domain.usecase

import com.mobile.videocutter.base.common.BaseUseCase
import com.mobile.videocutter.di.RepositoryFactory
import com.mobile.videocutter.domain.model.MusicTrack

class GetMusicTrackListUseCase : BaseUseCase<BaseUseCase.VoidRequest, List<MusicTrack>>() {
    override suspend fun execute(rv: VoidRequest): List<MusicTrack> {
        val repo = RepositoryFactory.getLocalDataRepo()
        return repo.getMusicTrackList()
    }
}
