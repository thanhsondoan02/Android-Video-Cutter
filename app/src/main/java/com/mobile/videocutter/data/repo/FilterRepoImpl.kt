package com.mobile.videocutter.data.repo

import com.mobile.videocutter.domain.model.FILTER_TYPE
import com.mobile.videocutter.domain.repo.IFilterRepo
import com.mobile.videocutter.presentation.filter.FilterAdapter

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
