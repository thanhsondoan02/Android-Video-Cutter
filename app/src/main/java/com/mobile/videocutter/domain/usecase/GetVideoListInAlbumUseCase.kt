package com.mobile.videocutter.domain.usecase

import com.mobile.videocutter.base.common.BaseUseCase
import com.mobile.videocutter.di.RepositoryFactory
import com.mobile.videocutter.domain.model.LocalVideo

class GetVideoListInAlbumUseCase : BaseUseCase<GetVideoListInAlbumUseCase.GetVideoListRV, List<LocalVideo>>() {
    override suspend fun execute(rv: GetVideoListRV): List<LocalVideo> {
        val repo = RepositoryFactory.getLocalDataRepo()
        return repo.getMyStudioVideoList(rv.id)
    }

    class GetVideoListRV(val id: String) : RequestValue
}
