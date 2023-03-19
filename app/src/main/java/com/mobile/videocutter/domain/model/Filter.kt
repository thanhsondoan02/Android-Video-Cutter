package com.mobile.videocutter.domain.model

import android.graphics.drawable.Drawable
import com.mobile.videocutter.R
import com.mobile.videocutter.base.extension.getAppDrawable
import com.mobile.videocutter.base.extension.getAppString

class Filter(val type: FILTER_TYPE) {
    fun getFilterColor(): Int {
        return when (type) {
            FILTER_TYPE.SPRING -> R.color.filter_spring
            FILTER_TYPE.SUMMER -> R.color.filter_summer
            FILTER_TYPE.FALL -> R.color.filter_fall
            FILTER_TYPE.WINTER -> R.color.filter_winter
            else -> R.color.transparent
        }
    }

    fun getFilterDrawable(): Drawable? {
        return getAppDrawable(getFilterColor())
    }

    fun getFilterTitle(): String {
        return when (type) {
            FILTER_TYPE.ORIGINAL -> getAppString(R.string.original)
            FILTER_TYPE.SPRING -> getAppString(R.string.spring)
            FILTER_TYPE.SUMMER -> getAppString(R.string.summer)
            FILTER_TYPE.FALL -> getAppString(R.string.fall)
            FILTER_TYPE.WINTER -> getAppString(R.string.winter)
        }
    }
}

enum class FILTER_TYPE {
    ORIGINAL,
    SPRING,
    SUMMER,
    FALL,
    WINTER
}
