package com.mobile.videocutter.presentation.filter

import com.mobile.videocutter.domain.model.FILTER_TYPE

class FilterRepoImpl : IFilterRepo {
    override fun getListFilter(): List<FilterAdapter.FilterDisplay> {
        val list: MutableList<FilterAdapter.FilterDisplay> = arrayListOf()

        list.add(
            FilterAdapter.FilterDisplay(
                type = FILTER_TYPE.ORIGINAL,
                true
            )
        )

        list.add(
            FilterAdapter.FilterDisplay(
                type = FILTER_TYPE.SPRING,
                false
            )
        )

        list.add(
            FilterAdapter.FilterDisplay(
                type = FILTER_TYPE.SUMMER,
                false
            )
        )

        list.add(
            FilterAdapter.FilterDisplay(
                type = FILTER_TYPE.FALL,
                false
            )
        )

        list.add(
            FilterAdapter.FilterDisplay(
                type = FILTER_TYPE.WINTER,
                false
            )
        )
        return list
    }
}
