package com.mobile.videocutter.domain.repo

import com.mobile.videocutter.domain.model.Album
import com.mobile.videocutter.domain.model.LocalVideo
import com.mobile.videocutter.domain.model.MusicTrack

interface ILocalDataRepo {
    fun getAlbumList(): List<Album>
    fun getMyStudioVideoList(albumId: String? = null): List<LocalVideo>
    fun getMusicTrackList() : List<MusicTrack>
}
