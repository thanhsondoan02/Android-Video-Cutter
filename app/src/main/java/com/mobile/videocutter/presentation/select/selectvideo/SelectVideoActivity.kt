package com.mobile.videocutter.presentation.select.selectvideo

import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.databinding.SelectVideoActivityBinding

class SelectVideoActivity : BaseBindingActivity<SelectVideoActivityBinding>(R.layout.select_video_activity) {
    override fun onInitView() {
        super.onInitView()
        val idAlbum = intent.getStringExtra("idAlbum")
        val nameAlbum = intent.getStringExtra("nameAlbum")
        binding.hvSelectVideo.setTextCenter(nameAlbum)
        binding.hvSelectVideo.setOnLeftIconClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
