package com.mobile.videocutter.di

import com.mobile.videocutter.presentation.filter.FilterRepoImpl
import com.mobile.videocutter.presentation.filter.IFilterRepo

object DisplayFactory {
    private val filterRepoImpl = FilterRepoImpl()

    fun getListFilter(): IFilterRepo {
        return filterRepoImpl
    }
}
