package com.mobile.videocutter.presentation.adjust

import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingFragment
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.AdjustFragmentBinding
import com.mobile.videocutter.presentation.PlayerAdjustFragment
import com.mobile.videocutter.presentation.select.selectvideo.SelectVideoViewModel
import com.mobile.videocutter.presentation.tasselsvideo.TasselsVideoActivity
import com.mobile.videocutter.presentation.widget.recyclerview.CustomRecyclerView
import com.mobile.videocutter.presentation.widget.recyclerview.LAYOUT_MANAGER_MODE

class AdjustFragment: BaseBindingFragment<AdjustFragmentBinding>(R.layout.adjust_fragment) {
    private val viewModel by activityViewModels<SelectVideoViewModel>()
    private val adapter by lazy {
        AdjustFragmentAdapter()
    }

    override fun getContainerId() = R.id.flAdjustVideoPlayer

    override fun onInitView() {
        super.onInitView()
        initOnClick()
        initRecyclerView()
        addFragmentInsideFragment(PlayerAdjustFragment())
    }

    private fun initOnClick() {
        binding.ivAdjustVideoClose.setOnSafeClick {
            backFragment()
        }
        binding.tvAdjustVideoNext.setOnSafeClick {
            navigateTo(
                requireActivity(),
                TasselsVideoActivity::class.java,
                bundleOf(
                    TasselsVideoActivity.LIST_PATH to viewModel.getListPath(),
                    TasselsVideoActivity.LIST_DURATION to viewModel.getListDuration()
                )
            )
        }
    }

    private fun initRecyclerView() {
        binding.crvAdjustVideoList.apply {
            setAdapter(adapter)
            setLayoutManagerMode(LAYOUT_MANAGER_MODE.LINEAR_HORIZATION)
            setDragRecyclerView()
            listener = object : CustomRecyclerView.IListener {
                override fun onScroll(position: Int) {
                    binding.crvAdjustVideoList.smoothiePosition(position)
                }
            }
            val list = mutableListOf<Any>()
            viewModel.listVideoAdd.forEach {
                list.add(it.video)
            }
            list.add(list.lastIndex + 1, Any())
            submitList(list)
        }
    }
}
