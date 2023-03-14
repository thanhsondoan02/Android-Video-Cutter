package com.mobile.videocutter.presentation.home.start

import android.content.Intent
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.base.extension.gone
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.base.extension.show
import com.mobile.videocutter.databinding.StartActivityBinding
import com.mobile.videocutter.domain.model.LocalVideo
import com.mobile.videocutter.domain.model.mockLocalVideoList
import com.mobile.videocutter.presentation.filter.FilterActivity
import com.mobile.videocutter.presentation.home.mystudio.MyStudioActivity
import com.mobile.videocutter.presentation.home.mystudio.MyStudioAdapter
import com.mobile.videocutter.presentation.home.mystudio.MyStudioViewModel
import com.mobile.videocutter.presentation.home.preview.PreviewVideoFragment
import com.mobile.videocutter.presentation.home.setting.SettingActivity
import com.mobile.videocutter.presentation.model.IViewListener
import com.mobile.videocutter.presentation.widget.recyclerview.LAYOUT_MANAGER_MODE
import handleUiState

class StartActivity : BaseBindingActivity<StartActivityBinding>(R.layout.start_activity) {
    private val startAdapter = StartAdapter()

    private val viewModel by viewModels<MyStudioViewModel>()

    private val listStartVideo = mockLocalVideoList(50)

    override fun getContainerId(): Int = R.id.constStartContainer

    override fun onInitView() {
        super.onInitView()
        initRecyclerView()
        binding.tvStartSeeAllMyStudio.setOnSafeClick {
            startActivity(Intent(this, MyStudioActivity::class.java))
        }
        binding.ivStartSetting.setOnSafeClick {
            startActivity(Intent(this, FilterActivity::class.java))
        }
        viewModel.getMyStudioVideos()
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()

        lifecycleScope.launchWhenCreated {
            viewModel.myStudioVideoState.collect {
                handleUiState(it, object : IViewListener {
                    override fun onSuccess() {
                        startAdapter.submitList(it.data?.map { video -> MyStudioAdapter.VideoDisplay(video) })
                    }
                })
            }
        }
    }

    override fun onBackPressedDispatcher() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            clearStackFragment()
        } else {
            super.onBackPressed()
        }
    }

    private fun initRecyclerView() {
        startAdapter.listener = object : StartAdapter.IListener {
            override fun onVideoClick(localVideo: LocalVideo?) {
                replaceFragment(
                    PreviewVideoFragment(),
                    bundleOf(
                        PreviewVideoFragment.VIDEO_PATH to localVideo?.videoPath,
                        PreviewVideoFragment.VIDEO_DURATION to localVideo?.duration
                    )
                )
            }
        }
        binding.crvStartVideoList.setAdapter(startAdapter)
        binding.crvStartVideoList.setLayoutManagerMode(LAYOUT_MANAGER_MODE.LINEAR_HORIZATION)

        if (listStartVideo.isEmpty()) {
            binding.crvStartVideoList.gone()
            binding.llStartNoData.show()
        } else {
            binding.crvStartVideoList.show()
            binding.llStartNoData.gone()
        }
    }
}
