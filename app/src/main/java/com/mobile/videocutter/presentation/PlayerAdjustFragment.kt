package com.mobile.videocutter.presentation

import android.annotation.SuppressLint
import android.os.Handler
import android.util.DisplayMetrics
import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingFragment
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.PlayerFragmentBinding
import com.mobile.videocutter.domain.model.FILTER_TYPE.*
import com.mobile.videocutter.domain.model.TOOL_VIDEO_TYPE.*
import com.mobile.videocutter.presentation.select.selectvideo.SelectVideoViewModel
import getFormattedTime

class PlayerAdjustFragment: BaseBindingFragment<PlayerFragmentBinding>(R.layout.player_fragment) {
    private val viewModel by activityViewModels<SelectVideoViewModel>()

    override fun onInitView() {
        super.onInitView()
        initPlayer()
        initSeekBar()
        initOnClick()
    }

    override fun onDestroy() {
        binding.pvPlayerVideo.player?.release()
        viewModel.mHandler?.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    override fun onPause() {
        binding.pvPlayerVideo.player?.playWhenReady = false
        updatePlayPauseButton()
        super.onPause()
    }

    fun restartFragment() {
        binding.pvPlayerVideo.player?.release()
        viewModel.mHandler?.removeCallbacksAndMessages(null)
        initPlayer()
        initSeekBar()
        updatePlayPauseButton()
    }

    private fun initPlayer() {
        calculatePlayerViewWidthAndHeight()
        binding.pvPlayerVideo.player = ExoPlayer.Builder(requireContext()).build().apply {
            viewModel.listPath?.let { listPath ->
                val dataSourceFactory = DefaultDataSourceFactory(requireContext(), "exoplayer-codelab")
                val mediaSourceList = mutableListOf<ProgressiveMediaSource>()
                listPath.forEach {
                    mediaSourceList.add(ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(it)))
                }
                val concatenatingMediaSource = ConcatenatingMediaSource()
                mediaSourceList.forEach { concatenatingMediaSource.addMediaSource(it) }
                val loopingMediaSource = LoopingMediaSource(concatenatingMediaSource)
                setMediaSource(loopingMediaSource)
                prepare()

                // init max seekbar
                addListener(object : Player.Listener {
                    override fun onEvents(player: Player, events: Player.Events) {
                        super.onEvents(player, events)
                        if (events.contains(Player.EVENT_PLAYBACK_STATE_CHANGED)) {
                            if (player.playbackState == Player.STATE_READY) {
                                binding.sbPlayerVideoController.max = viewModel.totalDuration.toInt()
                            }
                        }
                    }
                })
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initSeekBar() {
        // auto update seekbar when video is playing
        viewModel.mHandler = Handler()
        baseActivity.runOnUiThread(object : Runnable {
            @SuppressLint("SetTextI18n")
            override fun run() {
                if (binding.pvPlayerVideo.player != null) {
                    binding.sbPlayerVideoController.progress = getCurrentTime().toInt()
                    binding.tvPlayerCurrentTime.text = getFormattedTime(getCurrentTime())
                    binding.tvPlayerTotalTime.text = getFormattedTime(viewModel.totalDuration)
                }
                viewModel.mHandler?.postDelayed(this, 10)
            }
        })

        // update video when seekbar is changed
        binding.sbPlayerVideoController.apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        val currentIndex = viewModel.getCurrentIndex(progress.toLong())
                        binding.pvPlayerVideo.player?.seekTo(currentIndex, getCurrentPositionInPlayer(currentIndex, progress))
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    private fun initOnClick() {
        binding.ivPlayerPlayPause.setOnSafeClick {
            binding.pvPlayerVideo.player?.playWhenReady = !(binding.pvPlayerVideo.player?.playWhenReady ?: false)
            updatePlayPauseButton()
        }
    }

    private fun updatePlayPauseButton() {
        if (binding.pvPlayerVideo.player?.playWhenReady == true) {
            binding.ivPlayerPlayPause.setImageResource(R.drawable.ic_black_play_video)
        } else {
            binding.ivPlayerPlayPause.setImageResource(R.drawable.ic_black_pause_video)
        }
    }

    private fun calculatePlayerViewWidthAndHeight() {
        if (viewModel.ratio == Float.MAX_VALUE) {
            viewModel.calculateVideoRatio()
        }
        if (viewModel.ratio != Float.MAX_VALUE) {
            val displayMetrics = DisplayMetrics()
            baseActivity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            var videoWidth = displayMetrics.widthPixels
            var videoHeight = displayMetrics.widthPixels
            if (viewModel.ratio > 1) {
                videoHeight = (videoWidth / viewModel.ratio).toInt()
            } else {
                videoWidth = (videoHeight * viewModel.ratio).toInt()
            }
            val params = binding.pvPlayerVideo.layoutParams
            params.width = videoWidth
            params.height = videoHeight
            binding.pvPlayerVideo.layoutParams = params
        }
    }

    private fun getCurrentTime(): Long {
        if (binding.pvPlayerVideo.player == null) return 0
        binding.pvPlayerVideo.player!!.apply {
            return viewModel.getBeforeDuration(currentWindowIndex) + currentPosition.toInt()
        }
    }

    private fun getCurrentPositionInPlayer(currentWindowIndex: Int, progress: Int): Long {
        val beforeDuration = viewModel.getBeforeDuration(currentWindowIndex)
        return progress - beforeDuration
    }
}
