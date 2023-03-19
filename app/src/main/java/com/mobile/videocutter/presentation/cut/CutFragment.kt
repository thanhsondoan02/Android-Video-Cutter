package com.mobile.videocutter.presentation.cut

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingFragment
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.CutFragmentBinding
import com.mobile.videocutter.domain.model.Filter
import com.mobile.videocutter.presentation.filter.FilterAdapter
import com.mobile.videocutter.presentation.model.IViewListener
import com.mobile.videocutter.presentation.tasselsvideo.TasselsVideoActivity
import com.mobile.videocutter.presentation.tasselsvideo.TasselsVideoViewModel
import handleUiState
import kotlinx.coroutines.launch

class CutFragment: BaseBindingFragment<CutFragmentBinding>(R.layout.cut_fragment) {
    private val adapter by lazy { FilterAdapter() }
    private val viewModel by activityViewModels<TasselsVideoViewModel>()

    override fun onInitView() {
        super.onInitView()
        initOnClick()
        initFilterRecyclerView()
        initCutView()
    }

    override fun onDestroy() {
        super.onDestroy()
        (baseActivity as? TasselsVideoActivity)?.playerFragment?.updateBaseOnData()
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()
        lifecycleScope.launch {
            viewModel.bitmapCutVideoList.collect {
                handleUiState(it, object : IViewListener {
                    override fun onSuccess() {
                        if (it.data != null) {
                            binding.cvvCut.setBitmapListDisplay(it.data!!)
                        }
                    }
                })
            }
        }
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

    private fun initCutView() {
        binding.cvvCut.post {

            viewModel.getBitmapCutVideoList(binding.cvvCut.getHeightListImage(), binding.cvvCut.getWidthListImage())

//            // hiển thị thanh cutVideo
//            binding.cvvCut.apply {
//                setConfigVideoBegin(viewModel.totalTime)
//                listener = object : CutVideoView.IListener {
//                    override fun onTimeStart(timeStart: Long) {
//                        binding.pvCutVideoRoot.player?.seekTo(timeStart)
//                        setTimeStart(timeStart)
//                        pauseVideo()
//                    }
//
//                    override fun onTimeCenter(timeCenter: Long) {
//                        binding.pvCutVideoRoot.player?.seekTo(timeCenter)
//                        setTimeCenter(timeCenter)
//                        pauseVideo()
//                    }
//
//                    override fun onTimeEnd(timeEnd: Long) {
//                        binding.pvCutVideoRoot.player?.seekTo(timeEnd)
//                        setTimeEnd(timeEnd)
//                        pauseVideo()
//                    }
//                }
//            }
        }
    }
}
