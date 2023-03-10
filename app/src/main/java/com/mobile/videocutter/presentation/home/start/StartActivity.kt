package com.mobile.videocutter.presentation.home.start

import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.databinding.StartActivityBinding
import com.mobile.videocutter.domain.model.mockLocalVideoList
import com.mobile.videocutter.presentation.home.mystudio.MyStudioAdapter
import com.mobile.videocutter.presentation.home.mystudio.MyStudioViewModel
import com.mobile.videocutter.presentation.widget.recyclerview.LAYOUT_MANAGER_MODE

class StartActivity : BaseBindingActivity<StartActivityBinding>(R.layout.start_activity) {

    private val startAdapter = StartAdapter()

    private val viewModel by viewModels<MyStudioViewModel>()

    override fun onInitView() {
        super.onInitView()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.crvMyStudioVideoList.setAdapter(startAdapter)
        binding.crvMyStudioVideoList.setLayoutManagerMode(LAYOUT_MANAGER_MODE.LINEAR_HORIZATION)

        startAdapter.submitList(mockLocalVideoList(50).map { MyStudioAdapter.VideoDisplay(it) })
    }

}
