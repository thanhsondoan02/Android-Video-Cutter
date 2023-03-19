package com.mobile.videocutter.presentation.speedvideo

import androidx.fragment.app.activityViewModels
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingFragment
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.SpeedFragmentBinding
import com.mobile.videocutter.domain.model.Speed
import com.mobile.videocutter.presentation.tasselsvideo.TasselsVideoActivity
import com.mobile.videocutter.presentation.tasselsvideo.TasselsVideoViewModel
import com.mobile.videocutter.presentation.widget.video.speedvideo.SpeedVideoView

class SpeedFragment : BaseBindingFragment<SpeedFragmentBinding>(R.layout.speed_fragment) {
    private val viewModel by activityViewModels<TasselsVideoViewModel>()

    override fun onInitView() {
        super.onInitView()
        initOnClick()
        binding.svvSpeedChooser.setSpeed(viewModel.speed)
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
            (baseActivity as? TasselsVideoActivity)?.playerFragment?.saveSpeedState(binding.svvSpeedChooser.getCurrentSpeed())
            binding.svvSpeedChooser.saveSpeed()
            backFragment()
        }
        binding.svvSpeedChooser.listener = object : SpeedVideoView.IListener {
            override fun onSpeedChange(speed: Speed) {
                (baseActivity as? TasselsVideoActivity)?.playerFragment?.applySpeed(speed)
            }
        }
    }
}
