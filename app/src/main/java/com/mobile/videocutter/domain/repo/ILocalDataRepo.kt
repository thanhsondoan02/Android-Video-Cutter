package com.mobile.videocutter.domain.repo

import android.graphics.Bitmap
import com.mobile.videocutter.domain.model.Album
import com.mobile.videocutter.domain.model.LocalVideo
import com.mobile.videocutter.domain.model.MusicTrack
import com.mobile.videocutter.presentation.savelibrary.AppInfo
import kotlin.time.Duration

interface ILocalDataRepo {
    fun getAlbumList(): List<Album>
    fun getMyStudioVideoList(albumId: String? = null): List<LocalVideo>
    fun getMusicTrackList(): List<MusicTrack>
    fun getBitmapListFromVideoByTime(pathList: List<String>, durationList: List<Long>, heightBitmapScaled: Int, stepTime: Long): List<Bitmap>
    fun getBitmapListFromVideoByLength(localVideo: LocalVideo, heightBitmapScaled: Int, maxWidth: Int): List<Bitmap>
    fun getPackageAppInfoList() : List<AppInfo>
}
