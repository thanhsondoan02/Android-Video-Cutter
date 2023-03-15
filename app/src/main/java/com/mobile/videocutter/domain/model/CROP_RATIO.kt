package com.mobile.videocutter.domain.model

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

fun getCropRatio(cropRatio: CROP_RATIO): Float? {
    return when (cropRatio) {
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
