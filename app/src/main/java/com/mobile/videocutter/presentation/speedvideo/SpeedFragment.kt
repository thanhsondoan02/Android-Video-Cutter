package com.mobile.videocutter.presentation.speedvideo

import android.util.Log
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingFragment
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.SpeedFragmentBinding
import com.mobile.videocutter.domain.model.Speed
import com.mobile.videocutter.presentation.tasselsvideo.TasselsVideoActivity
import com.mobile.videocutter.presentation.widget.video.speedvideo.SpeedVideoView

class SpeedFragment: BaseBindingFragment<SpeedFragmentBinding>(R.layout.speed_fragment) {

    override fun onInitView() {
        super.onInitView()
        initOnClick()
    }

    override fun onDestroy() {
        super.onDestroy()
        (baseActivity as? TasselsVideoActivity)?.playerFragment?.updateBaseOnData()
    }

    private fun initOnClick() {
        binding.vSpeedBackground.setOnSafeClick {
            // do nothing
        }
        binding.ivSpeedClose.setOnSafeClick {
            backFragment()
        }
        binding.ivSpeedDone.setOnSafeClick {
            (baseActivity as? TasselsVideoActivity)?.playerFragment?.saveSpeedState()
            backFragment()
        }
        binding.svvSpeedChooser.listener = object : SpeedVideoView.IListener {
            override fun onSpeedChange(speed: Speed) {
                Log.d("TAG", "onSpeedChange: ${speed.getSpeedTitle()}")
            }
        }
    }
}
