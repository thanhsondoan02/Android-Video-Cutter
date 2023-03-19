package com.mobile.videocutter.presentation.filter

import com.mobile.videocutter.domain.model.FILTER_TYPE
import com.mobile.videocutter.domain.model.Filter

class FilterDisplayImpl : IFilterDisplay {
    override fun getListFilter(): List<FilterAdapter.FilterDisplay> {
        val list: MutableList<FILTER_TYPE> = arrayListOf()
        list.add(FILTER_TYPE.ORIGINAL)
        list.add(FILTER_TYPE.SPRING)
        list.add(FILTER_TYPE.SUMMER)
        list.add(FILTER_TYPE.FALL)
        list.add(FILTER_TYPE.WINTER)
        return list.map { FilterAdapter.FilterDisplay(Filter(it)) }.apply {
            firstOrNull()?.isSelect = true
        }
    }
}
