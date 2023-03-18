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
import com.mobile.videocutter.domain.model.TOOL_VIDEO_TYPE.*
import com.mobile.videocutter.domain.model.ToolVideo
import com.mobile.videocutter.domain.model.getListAllToolVideo
import com.mobile.videocutter.presentation.addmusic.AddMusicActivity
import com.mobile.videocutter.presentation.adjust.crop.CropActivity
import com.mobile.videocutter.presentation.cutvideo.CutVideoActivity
import com.mobile.videocutter.presentation.filter.FilterActivity
import com.mobile.videocutter.presentation.speedvideo.SpeedVideoActivity
import com.mobile.videocutter.presentation.widget.recyclerview.LAYOUT_MANAGER_MODE
import getFormattedTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch

class TasselsVideoActivity : BaseBindingActivity<TasselsVideoActivityBinding>(R.layout.tassels_video_activity) {

    companion object {
        const val VIDEO_PATH = "VIDEO_PATH"
        const val LIST_PATH = "VIDEO_PATH"
        const val DURATION = "DURATION"
    }

    private val viewModel by viewModels<TasselsVideoViewModel>()

    private val adapterTool by lazy {
        TasselsVideoToolAdapter()
    }

    private val adapterTimeLine by lazy {
        TasselsVideoTimeLineAdapter()
    }

    private var mHandler: Handler? = null

    override fun getContainerId() = R.id.constTasselsVideoRoot

    override fun onPrepareInitView() {
        super.onPrepareInitView()
        viewModel.listPath = intent?.getStringArrayListExtra(LIST_PATH)
    }

    override fun onInitView() {
        super.onInitView()

        viewModel.videoPathCurrent = viewModel.listPath?.firstOrNull()
        viewModel.totalTime = intent.getLongExtra(DURATION, 0L)

        setLayoutVideo()
        initView()
        initializePlayer()
        setTextTimeCount()

        binding.crvTasselsVideoImage.post {
            viewModel.getBitMapList(binding.crvTasselsVideoImage.height)
        }

        binding.crvTasselsVideoImage.apply {
            setAdapter(adapterTimeLine)
            setLayoutManagerMode(LAYOUT_MANAGER_MODE.LINEAR_HORIZATION)
        }

        adapterTool.listener = object : TasselsVideoToolAdapter.IListener {
            override fun onToolClick(toolVideo: ToolVideo?) {
                when (toolVideo?.type) {
                    TOOL_VIDEO_TYPE.CROP -> {
                        navigateTo(
                            this@TasselsVideoActivity,
                            CropActivity::class.java,
                        )
                    }
                    TOOL_VIDEO_TYPE.CUT -> {
                        navigateTo(
                            this@TasselsVideoActivity,
                            CutVideoActivity::class.java
                        )
                    }
                    TOOL_VIDEO_TYPE.SPEED -> {
                        navigateTo(
                            this@TasselsVideoActivity,
                            SpeedVideoActivity::class.java
                        )
                    }
                    TOOL_VIDEO_TYPE.FILTER -> {
                        navigateTo(
                            this@TasselsVideoActivity,
                            FilterActivity::class.java
                        )
                    }
                    TOOL_VIDEO_TYPE.MUSIC -> {
                        navigateTo(
                            this@TasselsVideoActivity,
                            AddMusicActivity::class.java
                        )
                    }
                    TOOL_VIDEO_TYPE.ROTATE -> {
                        replaceFragment(
                            RotateFragment(),
                            bundleOf(RotateFragment.LIST_VIDEO to viewModel.listPath)
                        )
                    }
                    null -> {}
                }
            }
        }
        binding.crvTasselsVideoTool.apply {
            setAdapter(adapterTool)
            setLayoutManagerMode(LAYOUT_MANAGER_MODE.LINEAR_HORIZATION)
            submitList(getListAllToolVideo())
        }

        binding.ivTasselsVideoStart.setOnSafeClick {
            if (viewModel.isCheck) {
                startPlayer()
            } else {
                releasePlayer()
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

    override fun onObserverViewModel() {
        super.onObserverViewModel()

        lifecycleScope.launch {
            viewModel.bitmapTimeLineList.collect {
                handleUiState(it, object : IViewListener {
                    override fun onSuccess() {
                        if (it.data != null) {
                            binding.crvTasselsVideoImage.submitList(it.data)
                        }
                    }
                })
            }
        }
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
            if (viewModel.videoPathCurrent != null) {
                val mediaItem = MediaItem.fromUri(viewModel.videoPathCurrent!!)
                this.setMediaItem(mediaItem)
            }
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
        binding.ivTasselsVideoStart.setImageResource(R.drawable.ic_black_play_video)
        binding.ivTasselsVideoEnd.setImageResource(R.drawable.ic_mute_on)
        binding.pvTasselsVideoPlay.player?.playWhenReady = true
        viewModel.isCheck = false
    }

    private fun releasePlayer() {
        binding.ivTasselsVideoStart.setImageResource(R.drawable.ic_black_pause_video)
        binding.ivTasselsVideoEnd.setImageResource(R.drawable.ic_mute_off)
        viewModel.isCheck = true
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
