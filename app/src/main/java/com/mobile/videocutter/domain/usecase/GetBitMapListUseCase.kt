package com.mobile.videocutter.domain.usecase

import android.graphics.Bitmap
import com.mobile.videocutter.base.common.BaseUseCase
import com.mobile.videocutter.di.RepositoryFactory

class GetBitMapListUseCase : BaseUseCase<GetBitMapListUseCase.GetBitMapListRV, List<Bitmap>>() {
    override suspend fun execute(rv: GetBitMapListRV): List<Bitmap> {
        val repo = RepositoryFactory.getLocalDataRepo()
        return repo.getBitmapListFromVideoByTime(rv.pathList, rv.durationList, rv.height, rv.stepTime)
    }

    class GetBitMapListRV(
        val height: Int,
        val stepTime: Long,
        val pathList: List<String>,
        val durationList: List<Long>) : RequestValue
}
