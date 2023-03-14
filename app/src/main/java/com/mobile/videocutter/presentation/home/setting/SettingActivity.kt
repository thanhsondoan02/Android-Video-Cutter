package com.mobile.videocutter.presentation.home.setting

import android.content.Context
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.SettingActivityBinding
import com.mobile.videocutter.domain.model.VIDEO_QUALITY

class SettingActivity : BaseBindingActivity<SettingActivityBinding>(R.layout.setting_activity) {
    companion object {
        const val VIDEO_QUALITY_KEY = "VIDEO_QUALITY_KEY"
    }

    override fun getContainerId(): Int = R.id.flSettingContainer

    override fun onInitView() {
        super.onInitView()
        initOnClick()
        updateVideoQualityText()
    }

    fun getVideoQualityFromSharePref(): VIDEO_QUALITY {
        val sharedPref = getSharedPreferences(VIDEO_QUALITY_KEY, Context.MODE_PRIVATE)
        return ordinalToVideoQuality(sharedPref.getInt(VIDEO_QUALITY_KEY, 2))
    }

    private fun initOnClick() {
        binding.ivSettingBack.setOnSafeClick {
            navigateBack()
        }
        binding.constSettingVideoQuality.setOnSafeClick {
            replaceFragment(VideoQualityFragment().apply {
                listener = object : VideoQualityFragment.IListener {
                    override fun onVideoQualityChanged(videoQuality: VIDEO_QUALITY) {
                        saveVideoQualityKey(videoQuality)
                        updateVideoQualityText()
                    }
                }
            })
        }
    }

    private fun updateVideoQualityText() {
        binding.tvSettingVideoQualityValue.text = getVideoQualityText(getVideoQualityFromSharePref())
    }

    /**
     * Default video quality is high
     */
    private fun getVideoQualityText(videoQuality: VIDEO_QUALITY): String {
        return when (videoQuality) {
            VIDEO_QUALITY.LOW -> getString(R.string.low)
            VIDEO_QUALITY.MEDIUM -> getString(R.string.medium)
            else -> getString(R.string.high)
        }
    }

    private fun ordinalToVideoQuality(ordinal: Int): VIDEO_QUALITY {
        return when (ordinal) {
            0 -> VIDEO_QUALITY.LOW
            1 -> VIDEO_QUALITY.MEDIUM
            else -> VIDEO_QUALITY.HIGH
        }
    }

    private fun saveVideoQualityKey(videoQuality: VIDEO_QUALITY) {
        val sharedPref = getSharedPreferences(VIDEO_QUALITY_KEY, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt(VIDEO_QUALITY_KEY, videoQuality.ordinal)
            apply()
        }
    }
}
