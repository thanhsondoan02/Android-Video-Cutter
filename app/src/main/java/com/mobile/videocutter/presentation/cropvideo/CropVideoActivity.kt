package com.mobile.videocutter.presentation.cropvideo

import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.databinding.CropVideoActivityBinding
import com.mobile.videocutter.presentation.model.IViewListener
import com.mobile.videocutter.presentation.widget.recyclerview.LAYOUT_MANAGER_MODE
import handleUiState

class CropVideoActivity : BaseBindingActivity<CropVideoActivityBinding>(R.layout.crop_video_activity) {

    private val adapter = RatioAdapter()

    private val viewModel by viewModels<CropVideoViewModel>()

    override fun onPrepareInitView() {
        super.onPrepareInitView()
        viewModel.getRatio()
    }

    override fun onInitView() {
        super.onInitView()
        binding.crvCropVideoRatio.apply {
            setAdapter(adapter)
            setLayoutManagerMode(LAYOUT_MANAGER_MODE.LINEAR_HORIZATION)
        }
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()
        lifecycleScope.launchWhenCreated {
            viewModel.ratioCropVideo.collect {
                handleUiState(it, object : IViewListener {
                    override fun onSuccess() {
                        binding.crvCropVideoRatio.submitList(it.data)
                    }
                })
            }
        }
    }
}
