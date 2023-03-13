package com.mobile.videocutter.presentation.home.setting

import android.content.Context
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.SettingActivityBinding
import com.mobile.videocutter.domain.model.VideoQuality

class SettingActivity : BaseBindingActivity<SettingActivityBinding>(R.layout.setting_activity) {
    companion object {
        const val VIDEO_QUALITY_KEY = "VIDEO_QUALITY_KEY"
    }

//    override fun getContainerId(): Int = R.id.flSettingContainer

    override fun onInitView() {
        super.onInitView()
        initOnClick()
        updateVideoQualityText()
    }

    fun getVideoQualityFromSharePref(): VideoQuality {
        val sharedPref = getSharedPreferences(VIDEO_QUALITY_KEY, Context.MODE_PRIVATE)
        return ordinalToVideoQuality(sharedPref.getInt(VIDEO_QUALITY_KEY, 2))
    }

    private fun initOnClick() {
        binding.ivSettingBack.setOnSafeClick {
            navigateBack()
        }
        binding.constSettingVideoQuality.setOnSafeClick {
//            replaceFragment(VideoQualityFragment().apply {
//                listener = object : VideoQualityFragment.IListener {
//                    override fun onVideoQualityChanged(videoQuality: VideoQuality) {
//                        saveVideoQualityKey(videoQuality)
//                        updateVideoQualityText()
//                    }
//                }
//            })
        }
    }

    private fun updateVideoQualityText() {
        binding.tvSettingVideoQualityValue.text = getVideoQualityText(getVideoQualityFromSharePref())
    }

    /**
     * Default video quality is high
     */
    private fun getVideoQualityText(videoQuality: VideoQuality): String {
        return when (videoQuality) {
            VideoQuality.LOW -> getString(R.string.low)
            VideoQuality.MEDIUM -> getString(R.string.medium)
            else -> getString(R.string.high)
        }
    }

    private fun ordinalToVideoQuality(ordinal: Int): VideoQuality {
        return when (ordinal) {
            0 -> VideoQuality.LOW
            1 -> VideoQuality.MEDIUM
            else -> VideoQuality.HIGH
        }
    }

    private fun saveVideoQualityKey(videoQuality: VideoQuality) {
        val sharedPref = getSharedPreferences(VIDEO_QUALITY_KEY, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt(VIDEO_QUALITY_KEY, videoQuality.ordinal)
            apply()
        }
    }
}
