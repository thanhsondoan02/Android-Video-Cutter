package com.mobile.videocutter.domain.usecase

import android.graphics.Bitmap
import com.mobile.videocutter.base.common.BaseUseCase
import com.mobile.videocutter.di.RepositoryFactory
import com.mobile.videocutter.domain.model.LocalVideo

class GetBitMapListUseCase : BaseUseCase<GetBitMapListUseCase.GetBitMapListRV, List<Bitmap>>() {
    override suspend fun execute(rv: GetBitMapListRV): List<Bitmap> {
        val localVideo = LocalVideo().apply {
            videoPath = rv.videoPathCurrent
            duration = rv.duration
        }
        val repo = RepositoryFactory.getLocalDataRepo()
        return repo.getBitmapListFromVideoByTime(localVideo, rv.height, rv.stepTime)
    }

    class GetBitMapListRV(
        val height: Int,
        val stepTime: Long,
        val videoPathCurrent: String,
        val duration: Long) : RequestValue
}
