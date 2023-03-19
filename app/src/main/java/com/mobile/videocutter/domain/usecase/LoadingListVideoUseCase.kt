package com.mobile.videocutter.domain.usecase

import com.mobile.videocutter.base.common.BaseUseCase

class LoadingListVideoUseCase : BaseUseCase<BaseUseCase.VoidRequest, Int>() {
    override suspend fun execute(rv: VoidRequest): Int {
        // fake delay 3s
        Thread.sleep(3000)
        return 0
    }
}

