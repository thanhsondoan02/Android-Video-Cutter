package com.mobile.videocutter.presentation.home.start

import androidx.activity.viewModels
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.base.extension.gone
import com.mobile.videocutter.base.extension.show
import com.mobile.videocutter.databinding.StartActivityBinding
import com.mobile.videocutter.domain.model.mockLocalVideoList
import com.mobile.videocutter.presentation.home.mystudio.MyStudioAdapter
import com.mobile.videocutter.presentation.home.mystudio.MyStudioViewModel
import com.mobile.videocutter.presentation.widget.recyclerview.LAYOUT_MANAGER_MODE

class StartActivity : BaseBindingActivity<StartActivityBinding>(R.layout.start_activity) {
    private val startAdapter = StartAdapter()

    private val viewModel by viewModels<MyStudioViewModel>()

    private val listStartVideo = mockLocalVideoList(0)

    override fun onInitView() {
        super.onInitView()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.crvStartVideoList.setAdapter(startAdapter)
        binding.crvStartVideoList.setLayoutManagerMode(LAYOUT_MANAGER_MODE.LINEAR_HORIZATION)

        startAdapter.submitList(listStartVideo.map { MyStudioAdapter.VideoDisplay(it) })

        if (listStartVideo.isEmpty()) {
            binding.crvStartVideoList.gone()
            binding.llStartNoData.show()
        } else {
            binding.crvStartVideoList.show()
            binding.llStartNoData.gone()
        }
    }
}
