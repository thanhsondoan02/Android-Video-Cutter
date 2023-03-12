package com.mobile.videocutter.domain.repo

import com.mobile.videocutter.domain.model.Album
import com.mobile.videocutter.domain.model.LocalVideo

interface ILocalDataRepo {
    fun getAlbumList(): List<Album>
    fun getMyStudioVideoList(): List<LocalVideo>
}
