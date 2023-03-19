package com.mobile.videocutter.presentation.adjust

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.ExoPlayer
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.base.extension.STRING_DEFAULT
import com.mobile.videocutter.base.extension.getAppDimension
import com.mobile.videocutter.base.extension.getAppDrawable
import com.mobile.videocutter.databinding.AdjustActivityBinding
import com.mobile.videocutter.domain.model.VideoDisplay
import com.mobile.videocutter.presentation.model.IViewListener
import com.mobile.videocutter.presentation.select.selectvideo.SelectVideoActivity
import com.mobile.videocutter.presentation.tasselsvideo.TasselsVideoActivity
import com.mobile.videocutter.presentation.widget.recyclerview.CustomRecyclerView
import com.mobile.videocutter.presentation.widget.recyclerview.LAYOUT_MANAGER_MODE
import com.mobile.videocutter.presentation.widget.video.videoplayercontrol.VideoPlayerControl
import handleUiState

class AdjustActivity : BaseBindingActivity<AdjustActivityBinding>(R.layout.adjust_activity) {
    companion object {
        const val LIST_VIDEO = "list_video"
    }

    private val viewModel by viewModels<AdjustViewModel>()
    private val adapter = AdjustAdapter()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onPrepareInitView() {
        super.onPrepareInitView()
        viewModel.apply {
            listVideo.addAll(intent?.extras?.getParcelableArrayList(LIST_VIDEO) ?: arrayListOf())

            getListVideo()
        }
    }

    override fun getContainerId() = R.id.clAdjust

    override fun onInitView() {
        super.onInitView()
        binding.vpcAdjust.initPlayer()
        binding.hvAdjust.apply {
            setTextViewRightPadding(
                getAppDimension(R.dimen.dimen_14),
                getAppDimension(R.dimen.dimen_4),
                getAppDimension(R.dimen.dimen_14),
                getAppDimension(R.dimen.dimen_4)
            )

            setTextViewRightMargin(
                getAppDimension(R.dimen.dimen_10),
                getAppDimension(R.dimen.dimen_4),
                getAppDimension(R.dimen.dimen_10),
                getAppDimension(R.dimen.dimen_4)
            )

            getAppDrawable(R.drawable.shape_purple_bg_corner_6)?.let { setBackgroundTextViewRight(it) }

            setOnLeftIconClickListener {
                onBackPressed()
            }

            setOnRightTextClickListener {
                navigateTo(this@AdjustActivity, TasselsVideoActivity::class.java)
            }
        }

        binding.crvAdjust.apply {
            setAdapter(adapter)
            setLayoutManagerMode(LAYOUT_MANAGER_MODE.LINEAR_HORIZATION)
            setDragRecyclerView()
            listener = object : CustomRecyclerView.IListener {
                override fun onScroll(newPosition: Int, oldPosition: Int) {
                    binding.crvAdjust.smoothiePosition(newPosition)
                    viewModel.dragVideo(oldPosition, newPosition)
                }
            }
        }

        binding.vpcAdjust.apply {
            listener = object : VideoPlayerControl.IListener {
                override fun onPlayerReady(player: ExoPlayer?) {
                    binding.pvAdjust.player = player
                }

                override fun onPlayerEnd(isPlay: Boolean) {
                    viewModel.isPlay = isPlay
                }

                override fun onPausePlayer(isPlay: Boolean) {
                    viewModel.isPlay = isPlay
                }
            }

            setOnLeftIconClickListener {
                viewModel.isPlay = !viewModel.isPlay
                if (viewModel.isPlay) {
                    resumePlayer()
                } else {
                    pausePlayer()
                }
            }
        }

        adapter.listener = object : AdjustAdapter.IListener {

            override fun onDelete(localVideo: VideoDisplay) {
                viewModel.deleteVideo(localVideo)
            }

            override fun onAddMore() {
                sendResultData()
                finish()
            }
        }
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()

        lifecycleScope.launchWhenResumed {
            viewModel.localVideoAdjust.collect {
                handleUiState(it, object : IViewListener {
                    override fun onSuccess() {
                        binding.crvAdjust.submitList(it.data)
                        viewModel.getPathsVideo()
                    }
                })
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.listPath.collect {
                handleUiState(it, object : IViewListener {
                    override fun onSuccess() {
                        binding.vpcAdjust.setUrl(it.data ?: listOf(), viewModel.totalDuration)
                    }
                })
            }
        }
    }

    override fun onBackPressed() {
        sendResultData()
        super.onBackPressed()
    }

    override fun onStop() {
        super.onStop()
        viewModel.isPlay = false
        binding.vpcAdjust.pausePlayer()
    }

    override fun onCleaned() {
        super.onCleaned()
        binding.vpcAdjust.stopPlayer()
    }

    private fun sendResultData() {
        val intent = Intent()
        intent.putParcelableArrayListExtra(SelectVideoActivity.RESULT_LIST_VIDEO, viewModel.listVideoDelete as ArrayList<out Parcelable>)
        setResult(Activity.RESULT_OK, intent)
    }
}
