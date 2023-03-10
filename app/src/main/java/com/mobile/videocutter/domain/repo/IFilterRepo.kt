package com.mobile.videocutter.domain.repo

import com.mobile.videocutter.presentation.filter.FilterAdapter

interface IFilterRepo {
    fun getListFilter(): List<FilterAdapter.FilterDisplay>
}
