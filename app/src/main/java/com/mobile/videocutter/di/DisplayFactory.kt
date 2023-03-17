package com.mobile.videocutter.di

import com.mobile.videocutter.presentation.filter.FilterDisplayImpl
import com.mobile.videocutter.presentation.filter.IFilterDisplay

object DisplayFactory {
    private val filterRepoImpl = FilterDisplayImpl()

    fun getListFilter(): IFilterDisplay {
        return filterRepoImpl
    }
}
