package com.mobile.videocutter.presentation.home.setting

import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingFragment
import com.mobile.videocutter.base.extension.gone
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.base.extension.show
import com.mobile.videocutter.databinding.VideoQualityFragmentBinding
import com.mobile.videocutter.domain.model.VIDEO_QUALITY

class VideoQualityFragment : BaseBindingFragment<VideoQualityFragmentBinding>(R.layout.video_quality_fragment) {
    var listener: IListener? = null

    override fun onInitView() {
        super.onInitView()
        initOnClick()
        updateCheckedVideoQuality((baseActivity as? SettingActivity)?.getVideoQualityFromSharePref())
    }

    private fun initOnClick() {
        binding.ivVideoQualityBack.setOnSafeClick {
            backFragment()
        }
        binding.constVideoQualityLow.setOnSafeClick {
            backFragment()
            listener?.onVideoQualityChanged(VIDEO_QUALITY.LOW)
        }
        binding.constVideoQualityMedium.setOnSafeClick {
            backFragment()
            listener?.onVideoQualityChanged(VIDEO_QUALITY.MEDIUM)
        }
        binding.constVideoQualityHigh.setOnSafeClick {
            backFragment()
            listener?.onVideoQualityChanged(VIDEO_QUALITY.HIGH)
        }
    }

    private fun updateCheckedVideoQuality(videoQuality: VIDEO_QUALITY?) {
        when (videoQuality) {
            VIDEO_QUALITY.LOW -> {
                binding.ivVideoQualityLowCheck.show()
                binding.ivVideoQualityMediumCheck.gone()
                binding.ivVideoQualityHighCheck.gone()
            }
            VIDEO_QUALITY.MEDIUM -> {
                binding.ivVideoQualityLowCheck.gone()
                binding.ivVideoQualityMediumCheck.show()
                binding.ivVideoQualityHighCheck.gone()
            }
            else -> {
                binding.ivVideoQualityLowCheck.gone()
                binding.ivVideoQualityMediumCheck.gone()
                binding.ivVideoQualityHighCheck.show()
            }
        }
    }

    interface IListener {
        fun onVideoQualityChanged(videoQuality: VIDEO_QUALITY)
    }
}
