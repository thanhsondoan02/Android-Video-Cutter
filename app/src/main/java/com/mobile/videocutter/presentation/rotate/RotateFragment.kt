package com.mobile.videocutter.presentation.rotate

import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingFragment
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.RotateFragmentBinding
import com.mobile.videocutter.presentation.tasselsvideo.TasselsVideoActivity

class RotateFragment: BaseBindingFragment<RotateFragmentBinding>(R.layout.rotate_fragment) {
    override fun onInitView() {
        super.onInitView()
        initOnClick()
    }

    override fun onDestroy() {
        super.onDestroy()
        (baseActivity as? TasselsVideoActivity)?.playerFragment?.updateBaseOnData()
    }

    private fun initOnClick() {
        binding.vRotateBackground.setOnSafeClick {
            // do nothing
        }
        binding.ivRotateClose.setOnSafeClick {
            backFragment()
        }
        binding.ivRotateDone.setOnSafeClick {
            (baseActivity as? TasselsVideoActivity)?.playerFragment?.saveRotateState()
            backFragment()
        }
        binding.ivRotateLeft.setOnSafeClick {
            (baseActivity as? TasselsVideoActivity)?.playerFragment?.rotateVideoLeft()
        }
        binding.ivRotateRight.setOnSafeClick {
            (baseActivity as? TasselsVideoActivity)?.playerFragment?.rotateVideoRight()
        }
        binding.ivRotateFlipHorizontal.setOnSafeClick {
            (baseActivity as? TasselsVideoActivity)?.playerFragment?.flipVideoHorizontal()
        }
        binding.ivRotateFlipVertical.setOnSafeClick {
            (baseActivity as? TasselsVideoActivity)?.playerFragment?.flipVideoVertical()
        }
    }
}
