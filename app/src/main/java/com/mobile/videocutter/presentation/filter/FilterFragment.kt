package com.mobile.videocutter.presentation.filter

import androidx.fragment.app.activityViewModels
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingFragment
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.FilterFragmentBinding
import com.mobile.videocutter.di.DisplayFactory
import com.mobile.videocutter.domain.model.Filter
import com.mobile.videocutter.presentation.tasselsvideo.TasselsVideoActivity
import com.mobile.videocutter.presentation.tasselsvideo.TasselsVideoViewModel
import com.mobile.videocutter.presentation.widget.recyclerview.LAYOUT_MANAGER_MODE

class FilterFragment: BaseBindingFragment<FilterFragmentBinding>(R.layout.filter_fragment) {
    private val adapter by lazy { FilterAdapter() }
    private val display by lazy {
        DisplayFactory.getFilterDisplay()
    }
    private val viewModel by activityViewModels<TasselsVideoViewModel>()

    override fun onInitView() {
        super.onInitView()
        initOnClick()
        initFilterRecyclerView()
    }

    override fun onDestroy() {
        super.onDestroy()
        (baseActivity as? TasselsVideoActivity)?.playerFragment?.updateBaseOnData()
    }

    private fun initOnClick() {
        binding.vFilterBackground.setOnSafeClick {
            // do nothing
        }
        binding.ivFilterClose.setOnSafeClick {
            backFragment()
        }
        binding.ivFilterDone.setOnSafeClick {
            (baseActivity as? TasselsVideoActivity)?.playerFragment?.saveFilterState(adapter.getSelectFilter())
            backFragment()
        }
    }

    private fun initFilterRecyclerView() {
        adapter.listener = object : FilterAdapter.IListener {
            override fun onFilterClick(filter: Filter) {
                (baseActivity as? TasselsVideoActivity)?.playerFragment?.applyFilter(filter)
            }
        }
        binding.rvFilterList.apply {
            setAdapter(adapter)
            setLayoutManagerMode(LAYOUT_MANAGER_MODE.LINEAR_HORIZATION)
            submitList(this@FilterFragment.display.getListFilter().apply {
                firstOrNull { it.filter.type == viewModel.filter.type }?.isSelect = true
            })
        }
    }
}
