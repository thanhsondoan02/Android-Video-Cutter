package com.mobile.videocutter.domain.usecase

import com.mobile.videocutter.base.common.BaseUseCase
import com.mobile.videocutter.di.RepositoryFactory
import com.mobile.videocutter.presentation.filter.FilterAdapter

class GetListFilterUseCase: BaseUseCase<BaseUseCase.VoidRequest,List<FilterAdapter.FilterDisplay>>() {
    override suspend fun execute(rv: VoidRequest): List<FilterAdapter.FilterDisplay> {
        val repo = RepositoryFactory.getListFilter()
        return repo.getListFilter()
    }
}
