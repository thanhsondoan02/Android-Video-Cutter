package com.mobile.videocutter.presentation.adjust

import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingFragment
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.LoadingFragmentBinding

class LoadingFragment : BaseBindingFragment<LoadingFragmentBinding>(R.layout.loading_fragment) {
    override fun onInitView() {
        super.onInitView()

        val drawable = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f).apply {
            duration = 1000
            repeatCount = Animation.INFINITE
            fillAfter = false
            interpolator = LinearInterpolator()
        }

        binding.ivLoadingSpinner.startAnimation(drawable)

        initOnClick()
    }

    private fun initOnClick() {
        binding.flLoadingContainer.setOnSafeClick {
            backFragment()
        }

        binding.constLoadingMainContainer.setOnSafeClick {
            // do nothing
        }

        binding.tvLoadingStop.setOnSafeClick {
            backFragment()
        }
    }
}
