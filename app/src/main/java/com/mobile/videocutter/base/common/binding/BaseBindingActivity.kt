package com.mobile.videocutter.base.common.binding

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.mobile.videocutter.base.common.BaseActivity
import com.mobile.videocutter.base.common.BaseView

/**
 * cần thêm thẻ layout vào file xml
 */
abstract class BaseBindingActivity<DB : ViewDataBinding>(layoutId: Int) : BaseActivity(layoutId),
    BaseView {

    protected val binding
        get() = _binding!!
    private var _binding: DB? = null

    protected var initSetFullScreen = false

    init {

    }

    //region lifecycle
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onInitView() {
        super.onInitView()
    }

    override fun attachView() {
        _binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
    }
}
