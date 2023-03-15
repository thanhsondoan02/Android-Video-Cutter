package com.mobile.videocutter.presentation.adjust.crop

import android.annotation.SuppressLint
import android.os.Handler
import android.util.DisplayMetrics
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.CropActivityBinding
import com.mobile.videocutter.domain.model.CropRatio
import getFormattedTime
import getVideoWidthOrHeight

class CropActivity: BaseBindingActivity<CropActivityBinding>(R.layout.crop_activity) {
    companion object {
        const val VIDEO_PATH = "VIDEO_PATH"
    }

    private var player: Player? = null
    private var mHandler: Handler? = null
    private val cropAdapter by lazy { CropRatioAdapter() }

    private val viewModel by viewModels<CropViewModel>()

    override fun onPrepareInitView() {
        super.onPrepareInitView()
        viewModel.path = intent.getStringExtra(VIDEO_PATH)
    }

    override fun onInitView() {
        super.onInitView()
        initRecyclerView()
        initOnClick()
        initPlayer()
        calculateCropViewWidthAndHeight()
        initSeekBar()
    }

    override fun onDestroy() {
        binding.pvCropVideo.player?.release()
        mHandler?.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        binding.pvCropVideo.player?.playWhenReady = false
        updatePlayPauseButton()
    }


    private fun initRecyclerView() {
        cropAdapter.listener = object : CropRatioAdapter.IListener{
            override fun onCropRatioSelected(cropRatio: CropRatio) {
                binding.cvCrop.ratio = cropRatio.getRatio()
            }
        }
        binding.rvCropRatioList.adapter = cropAdapter
        binding.rvCropRatioList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        cropAdapter.submitList(viewModel.getRatioList())
    }

    private fun initOnClick() {
        binding.ivCropClose.setOnSafeClick {
            navigateBack()
        }
        binding.ivCropDone.setOnSafeClick {
            navigateBack()
        }
        binding.ivCropPlayPause.setOnSafeClick {
            player?.playWhenReady = !(player?.playWhenReady ?: false)
            updatePlayPauseButton()
        }
    }

    private fun initPlayer() {
        player = ExoPlayer.Builder(this).build().apply {
            intent.getStringExtra(VIDEO_PATH)?.let {
                setMediaItem(MediaItem.fromUri(it))
            }
            prepare()

            // restart video when it ends
            addListener(object : Player.Listener {
                override fun onEvents(player: Player, events: Player.Events) {
                    super.onEvents(player, events)
                    if (events.contains(Player.EVENT_PLAYBACK_STATE_CHANGED)) {
                        if (player.playbackState == Player.STATE_ENDED) {
                            binding.pvCropVideo.player?.seekTo(0)
                            binding.pvCropVideo.player?.playWhenReady = true
                        }
                    }
                }
            })
        }
        binding.pvCropVideo.player = player
    }

    @SuppressLint("SetTextI18n")
    private fun initSeekBar() {
        // auto update seekbar when video is playing
        mHandler = Handler()
        runOnUiThread(object : Runnable {
            @SuppressLint("SetTextI18n")
            override fun run() {
                if (player != null) {
                    binding.sbCropVideoController.progress = player!!.currentPosition.toInt()
                    binding.tvCropCurrentTime.text = getFormattedTime(player!!.currentPosition)
                    binding.tvCropTotalTime.text = "-"+ getFormattedTime(player!!.duration - player!!.currentPosition)
                }
                mHandler?.postDelayed(this, 10)
            }
        })

        // update video when seekbar is changed
        binding.sbCropVideoController.apply {
            max = player?.duration?.toInt() ?: 0
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        player?.seekTo(progress.toLong())
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    private fun calculateCropViewWidthAndHeight() {
        viewModel.path?.let {
            var videoWidth = getVideoWidthOrHeight(it, isWidth = true)
            var videoHeight = getVideoWidthOrHeight(it, isWidth = false)
            if (videoWidth == null || videoHeight == null) return
            val ratio =  videoWidth / videoHeight.toFloat()
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            if (ratio > 1) {
                videoWidth = displayMetrics.widthPixels
                videoHeight = (videoWidth / ratio).toInt()
            } else {
                videoHeight = displayMetrics.widthPixels
                videoWidth = (videoHeight * ratio).toInt()
            }
            binding.cvCrop.layoutParams.width = videoWidth
            binding.cvCrop.layoutParams.height = videoHeight
        }
    }

    private fun updatePlayPauseButton() {
        if (player?.playWhenReady == true) {
            binding.ivCropPlayPause.setImageResource(R.drawable.ic_black_play_video)
        } else {
            binding.ivCropPlayPause.setImageResource(R.drawable.ic_black_pause_video)
        }
    }
}
