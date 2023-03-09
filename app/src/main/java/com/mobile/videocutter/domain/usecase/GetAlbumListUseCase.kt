package com.mobile.videocutter.domain.usecase

import android.content.ContentResolver
import com.mobile.videocutter.base.common.BaseUseCase
import com.mobile.videocutter.di.RepositoryFactory
import com.mobile.videocutter.domain.model.Album

class GetAlbumListUseCase : BaseUseCase<GetAlbumListUseCase.GetAlbumListRV, List<Album>>() {
    override suspend fun execute(rv: GetAlbumListRV): List<Album> {
        val repo = RepositoryFactory.getLocalDataRepo()
        return repo.getAlbumList(rv.contentResolver)
    }

    class GetAlbumListRV(val contentResolver: ContentResolver) : RequestValue
}
