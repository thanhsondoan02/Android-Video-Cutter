package com.mobile.videocutter.presentation.tasselsvideo

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.base.extension.getAppDimension
import com.mobile.videocutter.base.extension.getAppDrawable
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.TasselsVideoActivityBinding
import com.mobile.videocutter.domain.model.VIDEO_ACTIVE_TYPE
import com.mobile.videocutter.domain.model.mockToolVideo
import com.mobile.videocutter.presentation.home.preview.PreviewVideoFragment
import com.mobile.videocutter.presentation.widget.recyclerview.LAYOUT_MANAGER_MODE
import getFormattedTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch

class TasselsVideoActivity : BaseBindingActivity<TasselsVideoActivityBinding>(R.layout.tassels_video_activity) {

    companion object {
        const val VIDEO_PATH = "VIDEO_PATH"
    }

    private val viewModel by viewModels<TasselsVideoViewModel>()

    private val adapter by lazy {
        TasselsVideoAdapter()
    }

    private var mHandler: Handler? = null

    override fun onInitView() {
        super.onInitView()

        viewModel.videoPath = intent.getStringExtra(VIDEO_PATH)

        setLayoutVideo()
        initView()
        initializePlayer()
        setTextTimeCount()

        binding.crvTasselsVideoTool.apply {
            setAdapter(adapter)
            setLayoutManagerMode(LAYOUT_MANAGER_MODE.LINEAR_HORIZATION)
            submitList(mockToolVideo())
        }

        binding.ivTasselsVideoStart.setOnSafeClick {
            when (viewModel.isCheck) {
                VIDEO_ACTIVE_TYPE.START_VIDEO -> {
                    startPlayer()
                }
                VIDEO_ACTIVE_TYPE.PAUSE_VIDEO -> {
                    releasePlayer()
                }
            }
        }

        binding.hvTasselsVideoRoot.setOnLeftIconClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onResume() {
        super.onResume()
        binding.pvTasselsVideoPlay.player?.playWhenReady = true
    }

    override fun onDestroy() {
        binding.pvTasselsVideoPlay.player?.release()
        mHandler?.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    private fun initView() {
        binding.hvTasselsVideoRoot.apply {
            setTextViewRightPadding(getAppDimension(R.dimen.dimen_14), getAppDimension(R.dimen.dimen_4), getAppDimension(R.dimen.dimen_14), getAppDimension(R.dimen.dimen_4))

            setTextViewRightMargin(getAppDimension(R.dimen.dimen_10), getAppDimension(R.dimen.dimen_4), getAppDimension(R.dimen.dimen_10), getAppDimension(R.dimen.dimen_4))

            getAppDrawable(R.drawable.shape_purple_bg_corner_6)?.let { setBackgroundTextViewRight(it) }
        }
    }

    private fun initializePlayer() {
        startPlayer()
        binding.pvTasselsVideoPlay.player = ExoPlayer.Builder(this).build().apply {
            viewModel.videoPath?.let { MediaItem.fromUri(it) }?.let { setMediaItem(it) }
            prepare()
            playWhenReady = true

            // restart video when it ends
            addListener(object : Player.Listener {
                override fun onEvents(player: Player, events: Player.Events) {
                    super.onEvents(player, events)
                    if (events.contains(Player.EVENT_PLAYBACK_STATE_CHANGED)) {
                        if (player.playbackState == Player.STATE_ENDED) {
                            binding.pvTasselsVideoPlay.player?.seekTo(0)
                            binding.pvTasselsVideoPlay.player?.playWhenReady = true
                        }
                    }
                }
            })
        }
    }

    private fun startPlayer() {
        binding.ivTasselsVideoStart.background = getAppDrawable(R.drawable.ic_black_play_video)
        binding.pvTasselsVideoPlay.player?.playWhenReady = true
        viewModel.isCheck = VIDEO_ACTIVE_TYPE.PAUSE_VIDEO
    }

    private fun releasePlayer() {
        binding.ivTasselsVideoStart.background = getAppDrawable(R.drawable.ic_black_pause_video)
        viewModel.isCheck = VIDEO_ACTIVE_TYPE.START_VIDEO
        binding.pvTasselsVideoPlay.player?.playWhenReady = false
    }

    private fun setTextTimeCount() {
        mHandler = Handler(Looper.getMainLooper())
        this.runOnUiThread(object : Runnable {
            @SuppressLint("SetTextI18n")
            override fun run() {
                if (binding.pvTasselsVideoPlay.player != null) {
                    binding.tvTasselsVideoTime.text = getFormattedTime(binding.pvTasselsVideoPlay.player!!.currentPosition)
                }
                mHandler?.postDelayed(this, 10)
            }
        })
    }

    private fun setLayoutVideo() {
        lifecycleScope.launch(Dispatchers.IO) {
            binding.flTasselsVideoFirst.post {
                val width = binding.flTasselsVideoFirst.width
                binding.flTasselsVideoFirst.layoutParams.height = width
            }
        }
    }
}
