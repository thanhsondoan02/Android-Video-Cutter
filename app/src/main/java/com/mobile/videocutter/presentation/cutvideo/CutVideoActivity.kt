package com.mobile.videocutter.presentation.cutvideo

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

    private var mHandler: Handler? = null

    override fun onInitView() {
        super.onInitView()

        viewModel.videoPathType = intent.getStringExtra(VIDEO_PATH)

        initView()
        setSelectTimeAction()

        binding.hvCutVideoBottom.setOnLeftIconClickListener {
            finish()
        }

        binding.vpcCutVideoTime.setOnLeftIconClickListener {
            if (viewModel.isCheck) {
                startVideo()
            } else {
                pauseVideo()
            }
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
                        binding.pvCutVideoRoot.player?.seekTo(timeStart)
                    }

                    override fun onTimeCenter(timeCenter: Long) {
                        binding.pvCutVideoRoot.player?.seekTo(timeCenter)
                    }

                    override fun onTimeEnd(timeEnd: Long) {
                        binding.pvCutVideoRoot.player?.seekTo(timeEnd)
                    }
                }
            }
        }
    }

    override fun onResume() {
        binding.pvCutVideoRoot.player?.playWhenReady = true
        super.onResume()
    }

    override fun onStop() {
        binding.pvCutVideoRoot.player?.playWhenReady = false
        super.onStop()
    }

    override fun onDestroy() {
        binding.pvCutVideoRoot.player?.release()
        mHandler?.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    private fun initializePlayer() {
        binding.pvCutVideoRoot.player = ExoPlayer.Builder(this).build().apply {
            viewModel.videoPathType?.let { MediaItem.fromUri(it) }?.let { setMediaItem(it) }
            prepare()
            playWhenReady = false

            addListener(object : Player.Listener {
                override fun onEvents(player: Player, events: Player.Events) {
                    super.onEvents(player, events)
                    if (events.contains(Player.EVENT_PLAYBACK_STATE_CHANGED)) {
                        if (player.playbackState == Player.STATE_ENDED) {
                            binding.pvCutVideoRoot.player?.seekTo(0)
                            binding.pvCutVideoRoot.player?.playWhenReady = true

                        }
                    }
                }
            })
        }
    }

    private fun setSelectTimeAction() {
        mHandler = Handler(Looper.getMainLooper())
//        this.runOnUiThread(object : Runnable {
//            @SuppressLint("SetTextI18n")
//            override fun run() {
//                val time = binding.pvCutVideoRoot.player?.currentPosition
//                time?.let { binding.cvvCutVideo.setAction(it) }
//                mHandler?.postDelayed(this, 10)
//            }
//        })
    }

    private fun startVideo() {
        binding.vpcCutVideoTime.setLeftIcon(R.drawable.ic_black_play_video)
        binding.pvCutVideoRoot.player?.playWhenReady = true
        viewModel.isCheck = false
    }

    private fun pauseVideo() {
        binding.vpcCutVideoTime.setLeftIcon(R.drawable.ic_black_pause_video)
        binding.pvCutVideoRoot.player?.playWhenReady = false
        viewModel.isCheck = true
    }
}
