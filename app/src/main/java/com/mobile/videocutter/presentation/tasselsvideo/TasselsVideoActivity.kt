package com.mobile.videocutter.presentation.tasselsvideo

import androidx.activity.viewModels
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.TasselsVideoActivityBinding
import com.mobile.videocutter.domain.model.TOOL_VIDEO_TYPE.*
import com.mobile.videocutter.domain.model.ToolVideo
import com.mobile.videocutter.domain.model.getListAllToolVideo
import com.mobile.videocutter.presentation.PlayerFragment
import com.mobile.videocutter.presentation.addmusic.AddMusicActivity
import com.mobile.videocutter.presentation.adjust.crop.CropFragment
import com.mobile.videocutter.presentation.cutvideo.CutVideoActivity
import com.mobile.videocutter.presentation.filter.FilterActivity
import com.mobile.videocutter.presentation.rotate.RotateFragment
import com.mobile.videocutter.presentation.speedvideo.SpeedVideoActivity
import com.mobile.videocutter.presentation.widget.recyclerview.LAYOUT_MANAGER_MODE

class TasselsVideoActivity : BaseBindingActivity<TasselsVideoActivityBinding>(R.layout.tassels_video_activity) {
    var playerFragment: PlayerFragment? = null

    companion object {
        const val LIST_PATH = "VIDEO_PATH"
        const val DURATION = "DURATION"
    }

    private val viewModel by viewModels<TasselsVideoViewModel>()

    private val adapterTool by lazy {
        TasselsVideoToolAdapter()
    }

    override fun getContainerId() = R.id.constTasselsVideoRoot

    override fun onPrepareInitView() {
        super.onPrepareInitView()
        viewModel.listPath = intent?.getStringArrayListExtra(LIST_PATH)
    }

    override fun onInitView() {
        super.onInitView()
        initOnClick()
        playerFragment = PlayerFragment()
        addFragment(playerFragment!!, containerId = R.id.flTasselsVideoPlayer)
        initToolsRecyclerView()
    }

    override fun onBackPressed() {
        if (getCurrentFragment() is PlayerFragment) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    private fun initOnClick() {
        binding.ivTasselsVideoClose.setOnSafeClick {
            onBackPressed()
        }
        binding.tvTasselsVideoSave.setOnSafeClick {
            // TODO
        }
    }

    private fun initToolsRecyclerView() {
        adapterTool.listener = object : TasselsVideoToolAdapter.IListener {
            override fun onToolClick(toolVideo: ToolVideo?) {
                when (toolVideo?.type) {
                    CROP -> {
                        addFragment(CropFragment())
                    }
                    CUT -> {
                        navigateTo(
                            this@TasselsVideoActivity,
                            CutVideoActivity::class.java
                        )
                    }
                    SPEED -> {
                        navigateTo(
                            this@TasselsVideoActivity,
                            SpeedVideoActivity::class.java
                        )
                    }
                    FILTER -> {
                        navigateTo(
                            this@TasselsVideoActivity,
                            FilterActivity::class.java
                        )
                    }
                    MUSIC -> {
                        navigateTo(
                            this@TasselsVideoActivity,
                            AddMusicActivity::class.java
                        )
                    }
                    ROTATE -> {
                        addFragment(RotateFragment())
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
    }
}
