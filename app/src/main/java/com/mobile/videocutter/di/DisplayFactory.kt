package com.mobile.videocutter.di

import com.mobile.videocutter.presentation.adjust.crop.CropDisplayImpl
import com.mobile.videocutter.presentation.adjust.crop.ICropDisplay
import com.mobile.videocutter.presentation.filter.FilterDisplayImpl
import com.mobile.videocutter.presentation.filter.IFilterDisplay

object DisplayFactory {
    private val filterRepoImpl = FilterDisplayImpl()
    private val cropDisplayImpl by lazy { CropDisplayImpl() }

    fun getFilterDisplay(): IFilterDisplay {
        return filterRepoImpl
    }

    fun getCropDisplay(): ICropDisplay {
        return cropDisplayImpl
    }
}
