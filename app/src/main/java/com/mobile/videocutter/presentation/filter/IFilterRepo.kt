package com.mobile.videocutter.presentation.filter

import com.mobile.videocutter.presentation.filter.FilterAdapter

interface IFilterRepo {
    fun getListFilter(): List<FilterAdapter.FilterDisplay>
}
