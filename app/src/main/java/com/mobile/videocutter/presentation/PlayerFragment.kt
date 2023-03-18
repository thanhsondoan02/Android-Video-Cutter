package com.mobile.videocutter.presentation

import android.annotation.SuppressLint
import android.os.Handler
import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingFragment
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.PlayerFragmentBinding
import com.mobile.videocutter.presentation.tasselsvideo.TasselsVideoViewModel
import getFormattedTime

class PlayerFragment: BaseBindingFragment<PlayerFragmentBinding>(R.layout.player_fragment) {
    companion object {
        const val LIST_PATH = "LIST_PATH"
    }

    private val viewModel by activityViewModels<TasselsVideoViewModel>()

    override fun onPrepareInitView() {
        super.onPrepareInitView()
        viewModel.listPath = arguments?.getStringArrayList(LIST_PATH)
    }

    override fun onInitView() {
        super.onInitView()
        initPlayer()
        initSeekBar()
        initOnClick()
        initVariable()
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

    fun saveRotateState() {
        viewModel.degree = binding.pvPlayerVideo.rotation
        viewModel.flipHorizontal = binding.pvPlayerVideo.scaleX == -1f
        viewModel.flipVertical = binding.pvPlayerVideo.scaleY == -1f
    }

    fun rotateVideoLeft() {
        binding.pvPlayerVideo.rotation -= 90
    }

    fun rotateVideoRight() {
        binding.pvPlayerVideo.rotation += 90
    }

    fun flipVideoHorizontal() {
        binding.pvPlayerVideo.scaleX = -binding.pvPlayerVideo.scaleX
    }

    fun flipVideoVertical() {
        binding.pvPlayerVideo.scaleY = -binding.pvPlayerVideo.scaleY
    }

    private fun initPlayer() {
        binding.pvPlayerVideo.player = ExoPlayer.Builder(requireContext()).build().apply {
            viewModel.listPath?.forEach { path ->
                addMediaItem(MediaItem.fromUri(path))
            }
            prepare()

            // restart video when it ends
            addListener(object : Player.Listener {
                override fun onEvents(player: Player, events: Player.Events) {
                    super.onEvents(player, events)
                    if (events.contains(Player.EVENT_PLAYBACK_STATE_CHANGED)) {
                        if (player.playbackState == Player.STATE_READY) {
                            binding.sbPlayerVideoController.max = player.duration.toInt()
                        }
                        if (player.playbackState == Player.STATE_ENDED) {
                            binding.pvPlayerVideo.player?.seekTo(0)
                            binding.pvPlayerVideo.player?.playWhenReady = true
                        }
                    }
                }
            })

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
                    binding.sbPlayerVideoController.progress = binding.pvPlayerVideo.player!!.currentPosition.toInt()
                    binding.tvPlayerCurrentTime.text = getFormattedTime(binding.pvPlayerVideo.player!!.currentPosition)
                    binding.tvPlayerTotalTime.text = getFormattedTime(binding.pvPlayerVideo.player!!.duration)
                }
                viewModel.mHandler?.postDelayed(this, 10)
            }
        })

        // update video when seekbar is changed
        binding.sbPlayerVideoController.apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        binding.pvPlayerVideo.player?.seekTo(progress.toLong())
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

    private fun initVariable() {
        binding.pvPlayerVideo.rotation = viewModel.degree
        binding.pvPlayerVideo.scaleX = if (viewModel.flipHorizontal) -1f else 1f
        binding.pvPlayerVideo.scaleY = if (viewModel.flipVertical) -1f else 1f
    }

    private fun updatePlayPauseButton() {
        if (binding.pvPlayerVideo.player?.playWhenReady == true) {
            binding.ivPlayerPlayPause.setImageResource(R.drawable.ic_black_play_video)
        } else {
            binding.ivPlayerPlayPause.setImageResource(R.drawable.ic_black_pause_video)
        }
    }
}