package com.mobile.videocutter.domain.usecase

import com.mobile.videocutter.base.common.BaseUseCase
import com.mobile.videocutter.di.RepositoryFactory
import com.mobile.videocutter.presentation.cropvideo.RatioAdapter

class GetRatioCropVideoUseCase: BaseUseCase<BaseUseCase.VoidRequest,List<RatioAdapter.RatioDisplay>>() {
    override suspend fun execute(rv: VoidRequest): List<RatioAdapter.RatioDisplay> {
        val repo = RepositoryFactory.getRatioDisplay()
        return repo.getListRatio()
    }
}
