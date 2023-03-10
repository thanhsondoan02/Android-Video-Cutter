package com.mobile.videocutter.di

import com.mobile.videocutter.data.repo.FilterRepoImpl
import com.mobile.videocutter.data.repo.LocalDataRepoImpl
import com.mobile.videocutter.domain.repo.IFilterRepo
import com.mobile.videocutter.domain.repo.ILocalDataRepo

object RepositoryFactory {
    private val localDataRepoImpl = LocalDataRepoImpl()
    private val filterRepoImpl = FilterRepoImpl()

    fun getLocalDataRepo(): ILocalDataRepo {
        return localDataRepoImpl
    }

    fun getListFilter(): IFilterRepo {
        return filterRepoImpl
    }
}
