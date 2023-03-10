package com.mobile.videocutter.domain.usecase

import android.content.ContentResolver
import com.mobile.videocutter.base.common.BaseUseCase
import com.mobile.videocutter.di.RepositoryFactory
import com.mobile.videocutter.presentation.select.selectvideo.SelectVideoAdapter

class GetVideoListUseCase : BaseUseCase<GetVideoListUseCase.GetVideoListRV, List<SelectVideoAdapter.VideoDisplay>>() {
    override suspend fun execute(rv: GetVideoListRV): List<SelectVideoAdapter.VideoDisplay> {
        val repo = RepositoryFactory.getLocalDataRepo()
        return repo.getVideoList(rv.id)
    }

    class GetVideoListRV(val id: String) : RequestValue
}
