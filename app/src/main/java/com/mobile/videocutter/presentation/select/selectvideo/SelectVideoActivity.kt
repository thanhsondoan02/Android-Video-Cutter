package com.mobile.videocutter.presentation.select.selectvideo

import android.provider.MediaStore
import androidx.recyclerview.widget.GridLayoutManager
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.databinding.SelectVideoActivityBinding
import com.mobile.videocutter.domain.model.Video
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
class SelectVideoActivity : BaseBindingActivity<SelectVideoActivityBinding>(R.layout.select_video_activity) {
    var idAlbum = ""
    var nameAlbum = ""
    private var listVideo = mutableListOf<Video>()
    private val selectVideoAdapter by lazy { SelectVideoAdapter() }

    override fun onInitView() {
        super.onInitView()
        idAlbum = intent.getStringExtra("idAlbum").toString()
        nameAlbum = intent.getStringExtra("nameAlbum").toString()
        binding.hvSelectVideo.setTextCenter(nameAlbum)
        binding.hvSelectVideo.setOnLeftIconClickListener {
            onBackPressed()
        }
        loadVideo()
    }

    private fun loadVideo() {
        binding.rvSelectVideoToAdd.adapter = selectVideoAdapter
        binding.rvSelectVideoToAdd.layoutManager = GridLayoutManager(this, 4)
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DURATION
        )
        val selection = "${MediaStore.Video.Media.BUCKET_ID} = ?"
        val selectionArgs = arrayOf(idAlbum.toString())

        val sortOrder = "${MediaStore.Video.Media.DATE_MODIFIED} DESC"

        val cursor = contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val videoId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID))
                val videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                val videoDuration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
                val thumbnail = MediaStore.Video.Thumbnails.getThumbnail(baseContext.contentResolver, videoId, MediaStore.Video.Thumbnails.MINI_KIND, null)
                val video = Video(videoId, formatDuration(videoDuration), thumbnail)
                listVideo.add(video)
            }
            cursor.close()
        }
        selectVideoAdapter.submitList(listVideo)
    }

    private fun formatDuration(duration: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes)
        return String.format("%02d:%02d", minutes, seconds)
    }
}
