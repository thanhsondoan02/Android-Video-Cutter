package com.mobile.videocutter.domain.usecase

import android.graphics.Bitmap
import com.mobile.videocutter.base.common.BaseUseCase
import com.mobile.videocutter.di.RepositoryFactory
import com.mobile.videocutter.domain.model.LocalVideo

class GetBitMapCutVideoListUseCase : BaseUseCase<GetBitMapCutVideoListUseCase.GetBitMapCutVideoListRV, List<Bitmap>>() {
    override suspend fun execute(rv: GetBitMapCutVideoListRV): List<Bitmap> {
        val localVideo = LocalVideo().apply {
            videoPath = rv.videoPathType
            duration = rv.totalTime
        }
        val repo = RepositoryFactory.getLocalDataRepo()

        return repo.getBitmapListFromVideoByTime(localVideo, rv.heightBitmapScaled, rv.maxWidth.toLong())
    }

    class GetBitMapCutVideoListRV(
        val videoPathType: String,
        val totalTime: Long,
        val heightBitmapScaled: Int,
        val maxWidth: Int
    ) : RequestValue
}
