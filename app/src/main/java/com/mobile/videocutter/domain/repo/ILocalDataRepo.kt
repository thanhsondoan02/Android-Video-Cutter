package com.mobile.videocutter.domain.repo

import android.graphics.Bitmap
import com.mobile.videocutter.domain.model.Album
import com.mobile.videocutter.domain.model.LocalVideo
import com.mobile.videocutter.domain.model.MusicTrack
import com.mobile.videocutter.presentation.savelibrary.AppInfo

interface ILocalDataRepo {
    fun getAlbumList(): List<Album>
    fun getMyStudioVideoList(albumId: String? = null): List<LocalVideo>
    fun getMusicTrackList() : List<MusicTrack>
    fun getBitmapListFromVideoByTime(localVideo: LocalVideo, heightBitmapScaled: Int, stepTime: Long): List<Bitmap>
    fun getPackageAppInfoList() : List<AppInfo>
}
