package com.mobile.videocutter.presentation.home

import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.databinding.HomeActivityBinding
import com.mobile.videocutter.domain.model.mockLocalVideoList
import com.mobile.videocutter.presentation.home.mystudio.MyStudioAdapter
import com.mobile.videocutter.presentation.home.mystudio.MyStudioViewModel

class HomeActivity : BaseBindingActivity<HomeActivityBinding>(R.layout.home_activity) {

    private val myStudioAdapter = MyStudioAdapter()

    private val viewModel by viewModels<MyStudioViewModel>()

    override fun onInitView() {
        super.onInitView()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.rvMyStudioVideoList.adapter = myStudioAdapter
        binding.rvMyStudioVideoList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        myStudioAdapter.submitList(mockLocalVideoList(50).map { MyStudioAdapter.VideoDisplay(it) })
    }

}
