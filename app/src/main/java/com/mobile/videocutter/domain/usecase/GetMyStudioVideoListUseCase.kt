package com.mobile.videocutter.domain.usecase

import com.mobile.videocutter.base.common.BaseUseCase
import com.mobile.videocutter.di.RepositoryFactory
import com.mobile.videocutter.domain.model.LocalVideo

class GetMyStudioVideoListUseCase : BaseUseCase<BaseUseCase.VoidRequest, List<LocalVideo>>() {
    override suspend fun execute(rv: VoidRequest): List<LocalVideo> {
        val repo = RepositoryFactory.getLocalDataRepo()
        return repo.getMyStudioVideoList()
    }
}
