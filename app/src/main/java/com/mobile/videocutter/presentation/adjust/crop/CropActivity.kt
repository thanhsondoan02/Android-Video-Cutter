package com.mobile.videocutter.presentation.adjust.crop

import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.CropActivityBinding

class CropActivity: BaseBindingActivity<CropActivityBinding>(R.layout.crop_activity) {
    private val cropAdapter by lazy { CropRatioAdapter() }

    private val viewModel by viewModels<CropViewModel>()

    override fun onInitView() {
        super.onInitView()
        initRecyclerView()
        initOnClick()
    }

    private fun initRecyclerView() {
        binding.rvCropRatioList.adapter = cropAdapter
        binding.rvCropRatioList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        cropAdapter.submitList(viewModel.getRatioList())
    }

    private fun initOnClick() {
        binding.ivCropClose.setOnSafeClick {
            navigateBack()
        }
        binding.ivCropDone.setOnSafeClick {
            navigateBack()
        }
    }
}
