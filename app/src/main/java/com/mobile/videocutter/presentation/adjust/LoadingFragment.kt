package com.mobile.videocutter.presentation.adjust

import android.graphics.drawable.AnimationDrawable
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingFragment
import com.mobile.videocutter.databinding.LoadingFragmentBinding

class LoadingFragment : BaseBindingFragment<LoadingFragmentBinding>(R.layout.loading_fragment) {

    override fun onInitView() {
        super.onInitView()

        val drawable = resources.getDrawable(R.drawable.selector_item_spinner_loading) as AnimationDrawable
        drawable.start()

    }

}
