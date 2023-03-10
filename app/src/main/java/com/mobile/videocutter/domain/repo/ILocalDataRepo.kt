package com.mobile.videocutter.domain.repo

import com.mobile.videocutter.domain.model.Album
import com.mobile.videocutter.presentation.select.selectvideo.SelectVideoAdapter

interface ILocalDataRepo {
    fun getAlbumList(): List<Album>
    fun getVideoList(albumId: String): List<SelectVideoAdapter.VideoDisplay>
}
