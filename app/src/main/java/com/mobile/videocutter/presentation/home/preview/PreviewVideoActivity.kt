package com.mobile.videocutter.presentation.home.preview

import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.databinding.PreviewVideoActivityBinding

class PreviewVideoActivity : BaseBindingActivity<PreviewVideoActivityBinding>(R.layout.preview_video_activity) {
    override fun onInitView() {
        super.onInitView()
        binding.hvPreviewVideoHeader.setOnLeftIconClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
