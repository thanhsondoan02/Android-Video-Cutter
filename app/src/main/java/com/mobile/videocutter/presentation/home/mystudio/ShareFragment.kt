package com.mobile.videocutter.presentation.home.mystudio

import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingFragment
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.ShareFragmentBinding

class ShareFragment: BaseBindingFragment<ShareFragmentBinding>(R.layout.share_fragment) {
    var listener: IListener? = null

    override fun onInitView() {
        super.onInitView()
        initOnClick()
    }

    private fun initOnClick() {
        binding.flShareContainer.setOnSafeClick {
            backFragment()
        }
        binding.constShareMainContainer.setOnSafeClick {
            // do nothing
        }
        binding.tvShareClose.setOnSafeClick {
            backFragment()
        }
        binding.constShareShare.setOnSafeClick {
            backFragment()
            listener?.onShare()
        }
        binding.constShareSave.setOnSafeClick {
            backFragment()
            listener?.onSave()
        }
    }

    interface IListener {
        fun onShare()
        fun onSave()
    }
}
