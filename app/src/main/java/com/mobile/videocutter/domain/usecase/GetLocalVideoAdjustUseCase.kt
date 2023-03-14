package com.mobile.videocutter.domain.usecase

import com.mobile.videocutter.base.common.BaseUseCase
import com.mobile.videocutter.domain.model.mockLocalVideoList

class GetLocalVideoAdjustUseCase :
    BaseUseCase<GetLocalVideoAdjustUseCase.GetLocalVideoAdjustUseCaseRV, List<Any>>() {

    override suspend fun execute(rv: GetLocalVideoAdjustUseCaseRV): List<Any> {
        val list: MutableList<Any> = arrayListOf()
        list.addAll(mockLocalVideoList(105))
        list.add(Any())
        return list.toList()
    }

    class GetLocalVideoAdjustUseCaseRV : BaseUseCase.RequestValue
}
