package com.mobile.videocutter.base.common.binding

import com.mobile.videocutter.base.common.BaseFragment
import androidx.databinding.ViewDataBinding

abstract class BaseBindingFragment<DB : ViewDataBinding>(layoutId: Int) : BaseFragment(layoutId) {
    protected val binding get() = _binding!!
    private var _binding: DB? = null

    open fun hasClickable() = false

    override fun onInitView() {
        super.onInitView()
        if (!hasClickable()) {
            disableClickRootView()
        }
    }

    private fun disableClickRootView() {
        binding.root.isFocusable = true
        binding.root.isClickable = true
    }

}
