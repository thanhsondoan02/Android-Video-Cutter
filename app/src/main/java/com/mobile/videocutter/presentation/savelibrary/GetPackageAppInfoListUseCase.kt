package com.mobile.videocutter.presentation.savelibrary

import com.mobile.videocutter.base.common.BaseUseCase
import com.mobile.videocutter.di.RepositoryFactory

class GetPackageAppInfoListUseCase : BaseUseCase<BaseUseCase.VoidRequest, List<AppInfo>>() {
    override suspend fun execute(rv: VoidRequest): List<AppInfo> {
        val repo = RepositoryFactory.getLocalDataRepo()
        return repo.getPackageAppInfoList()
    }

}
