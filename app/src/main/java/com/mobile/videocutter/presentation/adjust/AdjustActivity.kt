package com.mobile.videocutter.presentation.adjust

import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.base.extension.getAppDimension
import com.mobile.videocutter.base.extension.getAppDrawable
import com.mobile.videocutter.databinding.AdjustActivityBinding
import com.mobile.videocutter.domain.model.LocalVideo
import com.mobile.videocutter.presentation.model.IViewListener
import com.mobile.videocutter.presentation.tasselsvideo.TasselsVideoActivity
import com.mobile.videocutter.presentation.widget.recyclerview.CustomRecyclerView
import com.mobile.videocutter.presentation.widget.recyclerview.LAYOUT_MANAGER_MODE
import handleUiState

class AdjustActivity : BaseBindingActivity<AdjustActivityBinding>(R.layout.adjust_activity) {
    companion object {
        const val LIST_PATH = "LIST_PATH"
    }

    private val viewModel by viewModels<AdjustViewModel>()
    private val adapter = AdjustAdapter()

    override fun onPrepareInitView() {
        super.onPrepareInitView()
        viewModel.listPath = intent.getStringArrayListExtra(LIST_PATH)
    }

    override fun onInitView() {
        super.onInitView()
        binding.hvAdjust.apply {
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

            getAppDrawable(R.drawable.shape_purple_bg_corner_6)?.let { setBackgroundTextViewRight(it) }

            setOnRightTextClickListener {
                navigateTo(this@AdjustActivity, TasselsVideoActivity::class.java, bundleOf(TasselsVideoActivity.LIST_PATH to viewModel.listPath))
            }
        }

        binding.crvAdjust.apply {
            setAdapter(adapter)
            setLayoutManagerMode(LAYOUT_MANAGER_MODE.LINEAR_HORIZATION)
            setDragRecyclerView()
            listener = object : CustomRecyclerView.IListener {
                override fun onScroll(position: Int) {
                    binding.crvAdjust.smoothiePosition(position)
                }
            }
        }

        adapter.listener = object : AdjustAdapter.IListener {
            override fun onDelete(localVideo: LocalVideo) {
                viewModel.deleteLocalVideo(localVideo)
            }

            override fun onClick(localVideo: LocalVideo) {
                // TODO: chờ các báo thủ
            }
        }
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()

        lifecycleScope.launchWhenResumed {
            viewModel.localVideoAdjust.collect {
                handleUiState(it, object : IViewListener {
                    override fun onSuccess() {
                        binding.crvAdjust.submitList(it.data)
                    }
                })
            }
        }
    }
}
