package com.mobile.videocutter.presentation.select.selectvideo

import android.content.Intent
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.base.extension.gone
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.base.extension.show
import com.mobile.videocutter.databinding.SelectVideoActivityBinding
import com.mobile.videocutter.presentation.adjust.AdjustActivity
import com.mobile.videocutter.presentation.model.IViewListener
import com.mobile.videocutter.presentation.select.preview.PreviewImageFragment
import com.mobile.videocutter.presentation.select.selectlibrary.SelectLibraryFolderFragment
import com.mobile.videocutter.presentation.widget.recyclerview.CustomRecyclerView
import com.mobile.videocutter.presentation.widget.recyclerview.LAYOUT_MANAGER_MODE
import handleUiState

class SelectVideoActivity : BaseBindingActivity<SelectVideoActivityBinding>(R.layout.select_video_activity) {
    private val selectVideoAdapter by lazy { SelectVideoAdapter() }
    private val selectVideoAddAdapter by lazy { SelectVideoAddAdapter() }
    private val viewModel by viewModels<SelectVideoViewModel>()

    override fun getContainerId(): Int = R.id.flSelectVideoContainer

    override fun onInitView() {
        super.onInitView()
        loadVideo()
        initAddRecycleView()
        binding.crvSelectVideoAdd.apply {
            setAdapter(selectVideoAddAdapter)
            setLayoutManagerMode(LAYOUT_MANAGER_MODE.LINEAR_HORIZATION)
            setDragRecyclerView()
            listener = object : CustomRecyclerView.IListener {
                override fun onScroll(position: Int) {
                    binding.crvSelectVideoAdd.smoothiePosition(position)
                }
            }
        }

        binding.hvSelectVideo.setOnCenterClickListener {
            if (getCurrentFragment() == null) {
                replaceFragment(SelectLibraryFolderFragment())
                updateHeader()
            }
        }

        binding.hvSelectVideo.setOnLeftIconClickListener {
            if (getCurrentFragment() == null) {
                navigateBack()
            } else {
                backFragment()
                updateHeader()
            }
        }
        binding.btnSelectVideoAdd.setOnSafeClick {
            val intent = Intent(this@SelectVideoActivity, AdjustActivity::class.java)
            intent.putStringArrayListExtra(AdjustActivity.LIST_VIDEO, viewModel.getListPath())
            startActivity(intent)
        }

        viewModel.getVideoList()
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()
        lifecycleScope.launchWhenCreated {
            viewModel.selectVideoState.collect {
                handleUiState(it, object : IViewListener {
                    override fun onSuccess() {
                        selectVideoAdapter.submitList(it.data?.map { video -> SelectVideoAdapter.VideoDisplay(video) })
                    }
                })
            }
        }
    }

    fun updateAlbumName() {
        if (viewModel.nameAlbum.isNullOrEmpty()) {
            binding.hvSelectVideo.setTextCenter(getString(R.string.video))
        } else {
            binding.hvSelectVideo.setTextCenter(viewModel.nameAlbum)
        }
    }

    fun updateHeader() {
        binding.hvSelectVideo.apply {
            if (getCurrentFragment() != null) {
                setLeftIcon(R.drawable.ic_black_back)
                setCenterIcon(R.drawable.ic_black_down)
            } else {
                setLeftIcon(R.drawable.ic_black_close)
                setCenterIcon(R.drawable.ic_black_up)
            }
        }

    }

    private fun loadVideo() {
        binding.rvSelectVideoToAdd.adapter = selectVideoAdapter
        binding.rvSelectVideoToAdd.layoutManager = GridLayoutManager(this, 4)
        initRecycleView()
    }

    private fun initRecycleView() {
        selectVideoAdapter.listener = object : SelectVideoAdapter.IListener {
            override fun onVideoClick(videoDisplay: SelectVideoAdapter.VideoDisplay) {
                if (videoDisplay.isSelected) {
                    viewModel.listVideoAdd.add(videoDisplay)
                } else {
                    viewModel.listVideoAdd.remove(videoDisplay)
                }
                updateAddView()
                updateSelectInAddAdapter()
                autoScrollLog()
            }

            override fun onVideoLongClick(path: String) {
                replaceFragment(
                    PreviewImageFragment(),
                    bundleOf(PreviewImageFragment.VIDEO_PATH to path)
                )
            }
        }
    }

    private fun autoScrollLog() {
        binding.crvSelectVideoAdd.smoothiePosition(viewModel.listVideoAdd.size - 1)
        binding.crvSelectVideoAdd.invalidate()
    }

    private fun updateAddView() {
        if (viewModel.listVideoAdd.size > 0) {
            binding.llSelectVideoAdd.show()
        } else {
            binding.llSelectVideoAdd.gone()
        }
    }

    private fun initAddRecycleView() {
        selectVideoAddAdapter.listener = object : SelectVideoAddAdapter.IListener {
            override fun onDelete(item: SelectVideoAdapter.VideoDisplay) {
                viewModel.listVideoAdd.remove(item)
                updateSelectInAddAdapter()
                updateAddView()
                item.video.videoPath?.let {
                    selectVideoAdapter.updateSelect(it, false)
                }
            }
        }
    }

    private fun updateSelectInAddAdapter() {
        binding.btnSelectVideoAdd.text = getString(R.string.select_add).replaceFirst("0", viewModel.listVideoAdd.size.toString())
        selectVideoAddAdapter.submitList(viewModel.listVideoAdd)
    }
}
