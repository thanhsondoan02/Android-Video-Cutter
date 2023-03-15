package com.mobile.videocutter.domain.model

import com.mobile.videocutter.R
import com.mobile.videocutter.base.extension.getAppString

class CropRatio(var type: CROP_RATIO = CROP_RATIO.CUSTOM) {

    fun getRatio(): Float? {
        return when (type) {
            CROP_RATIO.CUSTOM -> null
            CROP_RATIO.INSTAGRAM -> 1f
            CROP_RATIO.R4X5 -> 4/5f
            CROP_RATIO.R5X4 -> 5/4f
            CROP_RATIO.R9X16 -> 9/16f
            CROP_RATIO.R16X9 -> 16/9f
            CROP_RATIO.R3X2 -> 3/2f
            CROP_RATIO.R2X3 -> 2/3f
            CROP_RATIO.R4X3 -> 4/3f
            CROP_RATIO.R3X4 -> 3/4f
        }
    }

    fun getTitle(): String {
        return when (type) {
            CROP_RATIO.CUSTOM -> getAppString(R.string.ratio_custom)
            CROP_RATIO.INSTAGRAM -> getAppString(R.string.ratio_instagram)
            CROP_RATIO.R4X5 -> getAppString(R.string.ratio_4x5)
            CROP_RATIO.R5X4 -> getAppString(R.string.ratio_5x4)
            CROP_RATIO.R9X16 -> getAppString(R.string.ratio_9x16)
            CROP_RATIO.R16X9 -> getAppString(R.string.ratio_16x9)
            CROP_RATIO.R3X2 -> getAppString(R.string.ratio_3x2)
            CROP_RATIO.R2X3 -> getAppString(R.string.ratio_2x3)
            CROP_RATIO.R4X3 -> getAppString(R.string.ratio_4x3)
            CROP_RATIO.R3X4 -> getAppString(R.string.ratio_3x4)
        }
    }
}

enum class CROP_RATIO {
    CUSTOM,
    INSTAGRAM,
    R4X5,
    R5X4,
    R9X16,
    R16X9,
    R3X2,
    R2X3,
    R4X3,
    R3X4
}
