package com.mobile.videocutter.presentation.cut

import androidx.fragment.app.activityViewModels
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingFragment
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.CutFragmentBinding
import com.mobile.videocutter.domain.model.Filter
import com.mobile.videocutter.presentation.filter.FilterAdapter
import com.mobile.videocutter.presentation.tasselsvideo.TasselsVideoActivity
import com.mobile.videocutter.presentation.tasselsvideo.TasselsVideoViewModel

class CutFragment: BaseBindingFragment<CutFragmentBinding>(R.layout.cut_fragment) {
    private val adapter by lazy { FilterAdapter() }
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
        binding.vCutBackground.setOnSafeClick {
            // do nothing
        }
        binding.ivCutClose.setOnSafeClick {
            backFragment()
        }
        binding.ivCutDone.setOnSafeClick {
//            (baseActivity as? TasselsVideoActivity)?.playerFragment?.saveCutState(adapter.getSelectFilter())
            backFragment()
        }
    }

    private fun initFilterRecyclerView() {
        adapter.listener = object : FilterAdapter.IListener {
            override fun onFilterClick(filter: Filter) {
                (baseActivity as? TasselsVideoActivity)?.playerFragment?.applyFilter(filter)
            }
        }
    }
}
