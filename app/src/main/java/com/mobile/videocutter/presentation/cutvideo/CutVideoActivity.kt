package com.mobile.videocutter.presentation.cutvideo

import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.base.extension.getAppDimension
import com.mobile.videocutter.base.extension.getAppDrawable
import com.mobile.videocutter.databinding.CutVideoActivityBinding
import com.mobile.videocutter.domain.model.LocalVideo
import com.mobile.videocutter.presentation.widget.cutvideo.CutVideoView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CutVideoActivity : BaseBindingActivity<CutVideoActivityBinding>(R.layout.cut_video_activity) {

    companion object {
        const val VIDEO_PATH: String = "VIDEO_PATH"
    }

    private val viewModel by viewModels<CutVideoViewModel>()

    private lateinit var player: ExoPlayer

    override fun onInitView() {
        super.onInitView()

        viewModel.videoPathType = intent.getStringExtra(VIDEO_PATH)

        initView()

        binding.hvCutVideoBottom.setOnLeftIconClickListener {
            finish()
        }
    }

    private fun initView() {
        binding.hvCutVideoBottom.apply {
            setTextViewRightPadding(
                getAppDimension(R.dimen.dimen_14),
                getAppDimension(R.dimen.dimen_4),
                getAppDimension(R.dimen.dimen_14),
                getAppDimension(R.dimen.dimen_4))

            setTextViewRightMargin(getAppDimension(R.dimen.dimen_10),
                getAppDimension(R.dimen.dimen_4),
                getAppDimension(R.dimen.dimen_10),
                getAppDimension(R.dimen.dimen_4))

            getAppDrawable(R.drawable.shape_purple_bg_corner_6)?.let {
                setBackgroundTextViewRight(it)
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {

            // lấy tổng time video và list ảnh bitmap
            val localVideo = LocalVideo().apply {
                videoPath = viewModel.videoPathType
                calcDuration()
                viewModel.totalTime = getTotalTime()
            }

            viewModel.bitmapList = localVideo.getBitmapListFromVideo(
                binding.cvvCutVideo.getHeightListImage(),
                binding.cvvCutVideo.getWidthListImage())

            // setting hiển thị video
            initializePlayer()

            // hiển thị thanh cutVideo
            binding.cvvCutVideo.apply {
                setConfigVideoBegin(viewModel.totalTime)
                setBitmapListDisplay(viewModel.bitmapList)
                listener = object : CutVideoView.IListener {
                    override fun onTimeStart(timeStart: Long) {
                        player.seekTo(timeStart)
                    }

                    override fun onTimeCenter(timeCenter: Long) {
                        player.seekTo(timeCenter)
                    }

                    override fun onTimeEnd(timeEnd: Long) {
                        player.seekTo(timeEnd)
                    }
                }
            }
        }
    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(this).build().also { exoPlayer ->

            binding.pvCutVideoRoot.player = exoPlayer

            val mediaItem = viewModel.videoPathType?.let { MediaItem.fromUri(it) }
            if (mediaItem != null) {
                exoPlayer.setMediaItem(mediaItem)
            }
            exoPlayer.seekTo(500L)
            exoPlayer.playWhenReady = true
            exoPlayer.prepare()
        }
    }

    override fun onResume() {
        super.onResume()
        player.playWhenReady = true
    }

    override fun onStop() {
        super.onStop()
        player.playWhenReady = false
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}
