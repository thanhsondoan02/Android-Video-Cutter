package com.mobile.videocutter.presentation.filter

import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.base.extension.getAppDimension
import com.mobile.videocutter.base.extension.getAppDrawable
import com.mobile.videocutter.databinding.FilterActivityBinding
import com.mobile.videocutter.presentation.model.IViewListener
import com.mobile.videocutter.presentation.widget.recyclerview.LAYOUT_MANAGER_MODE
import handleUiState

class FilterActivity : BaseBindingActivity<FilterActivityBinding>(R.layout.filter_activity) {
    private val viewModel by viewModels<FilterVideoModel>()

    private val adapter by lazy { FilterAdapter() }

    override fun onInitView() {
        super.onInitView()

        binding.hvFilterFooter.apply {
            getAppDrawable(R.drawable.shape_purple_bg_corner_6)?.let { setBackgroundTextViewRight(it) }
            setTextViewRightPadding(
                getAppDimension(R.dimen.dimen_14),
                getAppDimension(R.dimen.dimen_4),
                getAppDimension(R.dimen.dimen_14),
                getAppDimension(R.dimen.dimen_4)
            )

            setTextViewRightMargin(
                getAppDimension(R.dimen.dimen_10),
                getAppDimension(R.dimen.dimen_4),
                getAppDimension(R.dimen.dimen_10),
                getAppDimension(R.dimen.dimen_4)
            )
        }

        binding.crvFilter.apply {
            setAdapter(adapter)
            setLayoutManagerMode(LAYOUT_MANAGER_MODE.LINEAR_HORIZATION)
        }

        viewModel.getFilter()
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()
        lifecycleScope.launchWhenCreated {
            viewModel.filter.collect {
                handleUiState(it, object : IViewListener {
                    override fun onSuccess() {
                        binding.crvFilter.submitList(it.data)
                    }
                })
            }
        }
    }
}
