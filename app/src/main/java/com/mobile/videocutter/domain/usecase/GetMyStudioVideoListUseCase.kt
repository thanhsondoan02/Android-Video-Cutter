package com.mobile.videocutter.domain.usecase

import android.content.ContentResolver
import com.mobile.videocutter.base.common.BaseUseCase
import com.mobile.videocutter.di.RepositoryFactory
import com.mobile.videocutter.domain.model.mockLocalVideoList
import com.mobile.videocutter.presentation.home.mystudio.MyStudioAdapter

class GetMyStudioVideoListUseCase : BaseUseCase<GetMyStudioVideoListUseCase.GetMyStudioVideoListRV, List<MyStudioAdapter.VideoDisplay>>() {
    override suspend fun execute(rv: GetMyStudioVideoListRV): List<MyStudioAdapter.VideoDisplay> {
        val repo = RepositoryFactory.getLocalDataRepo()
//        return repo.getAlbumList(rv.contentResolver)
        return mockLocalVideoList(100).map { MyStudioAdapter.VideoDisplay(it) }
    }

    class GetMyStudioVideoListRV(val contentResolver: ContentResolver) : RequestValue
}
