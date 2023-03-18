package com.mobile.videocutter.presentation.rotate

import androidx.core.os.bundleOf
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingFragment
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.RotateFragmentBinding
import com.mobile.videocutter.presentation.PlayerFragment

class RotateFragment: BaseBindingFragment<RotateFragmentBinding>(R.layout.rotate_fragment) {
    companion object {
        const val LIST_VIDEO = "LIST_VIDEO"
    }

    private var playerFragment: PlayerFragment? = null

    override fun getContainerId() = R.id.flRotatePlayer

    override fun onInitView() {
        super.onInitView()
        playerFragment = PlayerFragment()
        addFragmentInsideFragment(playerFragment!!, bundleOf(PlayerFragment.LIST_PATH to arguments?.getStringArrayList(LIST_VIDEO)))
        initOnClick()
    }

    private fun initOnClick() {
        binding.root.setOnSafeClick {
            // do nothing
        }
        binding.ivRotateClose.setOnSafeClick {
            backFragment()
        }
        binding.ivRotateDone.setOnSafeClick {
            playerFragment?.saveRotateState()
            backFragment()
        }
        binding.ivRotateLeft.setOnSafeClick {
            playerFragment?.rotateVideoLeft()
        }
        binding.ivRotateRight.setOnSafeClick {
            playerFragment?.rotateVideoRight()
        }
        binding.ivRotateFlipHorizontal.setOnSafeClick {
            playerFragment?.flipVideoHorizontal()
        }
        binding.ivRotateFlipVertical.setOnSafeClick {
            playerFragment?.flipVideoVertical()
        }
    }
}
