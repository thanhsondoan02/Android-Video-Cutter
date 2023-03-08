package com.mobile.videocutter.presentation.adjust

import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.base.extension.getAppDimension
import com.mobile.videocutter.base.extension.getAppDrawable
import com.mobile.videocutter.databinding.AdjustActivityBinding

class AdjustActivity : BaseBindingActivity<AdjustActivityBinding>(R.layout.adjust_activity) {

    override fun onInitView() {
        super.onInitView()
        binding.hvAdjust.apply {
            setTextViewRightPadding(
                getAppDimension(R.dimen.dimen_14),
                getAppDimension(R.dimen.dimen_4),
                getAppDimension(R.dimen.dimen_14),
                getAppDimension(R.dimen.dimen_4)
            )

            setTextViewRightMargin(
                getAppDimension(R.dimen.dimen_10),
                getAppDimension(R.dimen.dimen_4),
                getAppDimension(R.dimen.dimen_10),
                getAppDimension(R.dimen.dimen_4)
            )

            getAppDrawable(R.drawable.shape_bg_purple_corner_6)?.let { setBackgroundTextViewRight(it) }

            setVisibleViewUnderLine(false)
        }
    }

}
