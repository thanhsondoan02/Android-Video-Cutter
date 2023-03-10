package com.mobile.videocutter.presentation.home.mystudio

import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingFragment
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.ConfirmFragmentBinding

class ConfirmFragment: BaseBindingFragment<ConfirmFragmentBinding>(R.layout.confirm_fragment) {
    private var title: String? = null
    private var description: String? = null
    private var leftText: String? = null
    private var rightText: String? = null
    private var listener: IListener? = null

    override fun onInitView() {
        super.onInitView()
        initOnClick()
        binding.tvConfirmTitle.text = title ?: ""
        binding.tvConfirmDescription.text = description ?: ""
        binding.tvConfirmCancel.text = leftText ?: ""
        binding.tvConfirmOk.text = rightText ?: ""
    }

    private fun initOnClick() {
        binding.flConfirmContainer.setOnSafeClick {
            backFragment()
        }
        binding.constConfirmMainContainer.setOnSafeClick {
            // do nothing
        }
        binding.tvConfirmCancel.setOnSafeClick {
            backFragment()
        }
        binding.tvConfirmOk.setOnSafeClick {
            backFragment()
            listener?.onConfirm()
        }
    }

    class Builder {
        private var title: String? = null
        private var description: String? = null
        private var leftText: String? = null
        private var rightText: String? = null
        private var listener: IListener? = null

        fun setTitle(title: String) = apply {
            this.title = title
        }

        fun setDescription(description: String) = apply {
            this.description = description
        }

        fun setLeftText(leftText: String) = apply {
            this.leftText = leftText
        }

        fun setRightText(rightText: String) = apply {
            this.rightText = rightText
        }

        fun setListener(listener: IListener) = apply {
            this.listener = listener
        }

        fun getInstance(): ConfirmFragment {
            val fragment = ConfirmFragment()
            fragment.title = title
            fragment.description = description
            fragment.leftText = leftText
            fragment.rightText = rightText
            fragment.listener = listener
            return fragment
        }
    }

    interface IListener {
        fun onConfirm()
    }
}
