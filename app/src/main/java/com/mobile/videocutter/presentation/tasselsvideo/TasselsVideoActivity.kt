package com.mobile.videocutter.presentation.tasselsvideo

import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
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
import com.mobile.videocutter.presentation.filter.FilterFragment
import com.mobile.videocutter.presentation.model.IViewListener
import com.mobile.videocutter.presentation.rotate.RotateFragment
import com.mobile.videocutter.presentation.savelibrary.SaveLibraryFragment
import com.mobile.videocutter.presentation.speedvideo.SpeedFragment
import com.mobile.videocutter.presentation.widget.recyclerview.LAYOUT_MANAGER_MODE
import handleUiState
import kotlinx.coroutines.launch

class TasselsVideoActivity : BaseBindingActivity<TasselsVideoActivityBinding>(R.layout.tassels_video_activity) {
    var playerFragment: PlayerFragment? = null

    companion object {
        const val LIST_PATH = "VIDEO_PATH"
        const val LIST_DURATION = "DURATION"
    }

    private val viewModel by viewModels<TasselsVideoViewModel>()

    private val adapterTool by lazy {
        TasselsVideoToolAdapter()
    }

    private val adapterTimeLine by lazy {
        TasselsVideoTimeLineAdapter()
    }

    override fun getContainerId() = R.id.constTasselsVideoRoot

    override fun onPrepareInitView() {
        super.onPrepareInitView()
        viewModel.listPath = intent?.getStringArrayListExtra(LIST_PATH)
        viewModel.listDuration = intent?.getLongArrayExtra(LIST_DURATION)?.toList()
    }

    override fun onInitView() {
        super.onInitView()
        initOnClick()
        playerFragment = PlayerFragment()
        addFragment(playerFragment!!, containerId = R.id.flTasselsVideoPlayer)
        initToolsRecyclerView()

        binding.crvTasselsVideoTimeLine.post {
            viewModel.getBitMapList(binding.crvTasselsVideoTimeLine.height)
        }
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
            replaceFragment(SaveLibraryFragment())
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
//                        addFragment(CutFragment())
                        navigateTo(
                            this@TasselsVideoActivity,
                            CutVideoActivity::class.java,
                            bundleOf(
                                CutVideoActivity.VIDEO_PATH to viewModel.listPath?.firstOrNull(),
                                CutVideoActivity.VIDEO_DURATION to viewModel.listDuration?.firstOrNull()
                            )
                        )
                    }
                    SPEED -> {
                        addFragment(SpeedFragment())
                    }
                    FILTER -> {
                        addFragment(FilterFragment())
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

        binding.crvTasselsVideoTimeLine.apply {
            setAdapter(adapterTimeLine)
            setLayoutManagerMode(LAYOUT_MANAGER_MODE.LINEAR_HORIZATION)
        }
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()
        lifecycleScope.launch {
            viewModel.bitmapTimeLineList.collect {
                handleUiState(it, object : IViewListener {
                    override fun onSuccess() {
                        if (it.data != null) {
                            binding.crvTasselsVideoTimeLine.submitList(it.data)
                        }
                    }
                })
            }
        }
    }
}
