package com.mobile.videocutter.domain.usecase

import com.mobile.videocutter.base.common.BaseUseCase
import com.mobile.videocutter.di.RepositoryFactory
import com.mobile.videocutter.domain.model.Album

class GetAlbumListUseCase : BaseUseCase<BaseUseCase.VoidRequest, List<Album>>() {
    override suspend fun execute(rv: VoidRequest): List<Album> {
        val repo = RepositoryFactory.getLocalDataRepo()
        return repo.getAlbumList()
    }
}
