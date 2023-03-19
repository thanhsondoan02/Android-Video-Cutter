package com.mobile.videocutter.domain.model

import com.mobile.videocutter.R
import com.mobile.videocutter.base.extension.getAppString

class Speed(val type: SPEED_TYPE) {
    fun getSpeedValue(): Float {
        return when (type) {
            SPEED_TYPE.SPEED_0_75 -> 0.75f
            SPEED_TYPE.SPEED_0_5 -> 0.5f
            SPEED_TYPE.SPEED_0_25 -> 0.25f
            SPEED_TYPE.SPEED_1 -> 1f
            SPEED_TYPE.SPEED_2 -> 2f
            SPEED_TYPE.SPEED_3 -> 3f
            SPEED_TYPE.SPEED_4 -> 4f
        }
    }

    fun getSpeedTitle(): String {
        return when (type) {
            SPEED_TYPE.SPEED_0_75 -> getAppString(R.string._0_75x)
            SPEED_TYPE.SPEED_0_5 -> getAppString(R.string._0_5x)
            SPEED_TYPE.SPEED_0_25 -> getAppString(R.string._0_25x)
            SPEED_TYPE.SPEED_1 -> getAppString(R.string._1x)
            SPEED_TYPE.SPEED_2 -> getAppString(R.string._2x)
            SPEED_TYPE.SPEED_3 -> getAppString(R.string._3x)
            SPEED_TYPE.SPEED_4 -> getAppString(R.string._4x)
        }
    }
}

enum class SPEED_TYPE {
    SPEED_0_75,
    SPEED_0_5,
    SPEED_0_25,
    SPEED_1,
    SPEED_2,
    SPEED_3,
    SPEED_4
}
