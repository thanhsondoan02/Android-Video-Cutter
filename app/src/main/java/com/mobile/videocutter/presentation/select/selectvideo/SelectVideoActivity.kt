package com.mobile.videocutter.presentation.select.selectvideo

import android.content.Intent
import android.provider.MediaStore
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.databinding.SelectVideoActivityBinding
import com.mobile.videocutter.domain.model.LocalVideo
import com.mobile.videocutter.domain.model.Video
import com.mobile.videocutter.presentation.home.mystudio.MyStudioAdapter
import com.mobile.videocutter.presentation.home.preview.PreviewVideoActivity
import java.util.concurrent.TimeUnit

class SelectVideoActivity : BaseBindingActivity<SelectVideoActivityBinding>(R.layout.select_video_activity) {
    var idAlbum = ""
    var nameAlbum = ""
    private var listVideo = mutableListOf<Video>()
    private var listVideoAdd = mutableListOf<Video>()
    private val selectVideoAdapter by lazy { SelectVideoAdapter() }
    private val selectVideoAddAdapter by lazy { SelectVideoAddAdapter() }

    override fun onInitView() {
        super.onInitView()
        idAlbum = intent.getStringExtra("idAlbum").toString()
        nameAlbum = intent.getStringExtra("nameAlbum").toString()
        binding.hvSelectVideo.setTextCenter(nameAlbum)
        binding.hvSelectVideo.setOnLeftIconClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        loadVideo()
    }

    private fun loadVideo() {
        binding.rvSelectVideoToAdd.adapter = selectVideoAdapter
        binding.rvSelectVideoToAdd.layoutManager = GridLayoutManager(this, 4)
        initRecycleView()
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
        selectVideoAdapter.submitList(listVideo.map { SelectVideoAdapter.VideoDisplay(it) })
    }

    private fun initRecycleView(){
        selectVideoAdapter.listener = object : SelectVideoAdapter.IListener {
            override fun onVideoClick(video: Video, state: SelectVideoAdapter.STATE, size: Int) {
                when (state) {
                    SelectVideoAdapter.STATE.NORMAL -> {

                    }
                    SelectVideoAdapter.STATE.SELECT -> {
                        updateSelected(size, video)
                    }
                }
            }
        }
    }

    private fun updateSelected(size: Int, video: Video){
        binding.rvSelectVideoAdd.adapter = selectVideoAddAdapter
        binding.rvSelectVideoAdd.layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.HORIZONTAL, false)
        binding.btnSelectVideoAdd.text = "Add($size)"
        listVideoAdd.add(video)
        selectVideoAddAdapter.submitList(listVideoAdd)

    }
    private fun formatDuration(duration: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes)
        return String.format("%02d:%02d", minutes, seconds)
    }
}
