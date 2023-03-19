package com.mobile.videocutter.presentation.adjust

import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingFragment
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.AdjustFragmentBinding
import com.mobile.videocutter.domain.model.LocalVideo
import com.mobile.videocutter.presentation.PlayerAdjustFragment
import com.mobile.videocutter.presentation.select.selectvideo.SelectVideoActivity
import com.mobile.videocutter.presentation.select.selectvideo.SelectVideoViewModel
import com.mobile.videocutter.presentation.tasselsvideo.TasselsVideoActivity
import com.mobile.videocutter.presentation.widget.recyclerview.CustomRecyclerView
import com.mobile.videocutter.presentation.widget.recyclerview.LAYOUT_MANAGER_MODE

class AdjustFragment: BaseBindingFragment<AdjustFragmentBinding>(R.layout.adjust_fragment) {
    private val viewModel by activityViewModels<SelectVideoViewModel>()
    private val adapter by lazy {
        AdjustFragmentAdapter()
    }
    private val playerAdjustFragment by lazy {
        PlayerAdjustFragment()
    }

    override fun getContainerId() = R.id.flAdjustVideoPlayer

    override fun onInitView() {
        super.onInitView()
        initOnClick()
        initRecyclerView()
        addFragmentInsideFragment(playerAdjustFragment)
    }

    private fun initOnClick() {
        binding.ivAdjustVideoClose.setOnSafeClick {
            baseActivity.onBackPressed()
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
        adapter.listener = object : AdjustFragmentAdapter.IListener {
            override fun onDelete(item: LocalVideo) {
                (baseActivity as? SelectVideoActivity)?.delete(item)
                binding.crvAdjustVideoList.submitList(listVideoWithItemAdd())
                playerAdjustFragment.restartFragment()
            }

            override fun onAdd() {
                baseActivity.onBackPressed()
            }
        }
        binding.crvAdjustVideoList.apply {
            setAdapter(adapter)
            setLayoutManagerMode(LAYOUT_MANAGER_MODE.LINEAR_HORIZATION)
            setDragRecyclerView()
            listener = object : CustomRecyclerView.IListener {
                override fun onScroll(position: Int) {
                    binding.crvAdjustVideoList.smoothiePosition(position)
                }
            }
            submitList(listVideoWithItemAdd())
        }
    }

    private fun listVideoWithItemAdd(): List<Any> {
        val list = mutableListOf<Any>()
        viewModel.listVideoAdd.forEach {
            list.add(it)
        }
        list.add(list.lastIndex + 1, Any())
        return list
    }
}
