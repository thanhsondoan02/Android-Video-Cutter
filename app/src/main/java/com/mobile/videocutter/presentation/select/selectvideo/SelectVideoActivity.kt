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
import com.mobile.videocutter.presentation.widget.recyclerview.CustomRecyclerView
import com.mobile.videocutter.presentation.widget.recyclerview.LAYOUT_MANAGER_MODE
import handleUiState

class SelectVideoActivity : BaseBindingActivity<SelectVideoActivityBinding>(R.layout.select_video_activity) {
    companion object {
        const val ALBUM_ID = "ALBUM_ID"
        const val ALBUM_NAME = "ALBUM_NAME"
    }

    private val selectVideoAdapter by lazy { SelectVideoAdapter() }
    private val selectVideoAddAdapter by lazy { SelectVideoAddAdapter() }

    private val viewModel by viewModels<SelectVideoViewModel>()

    override fun getContainerId(): Int = R.id.flSelectVideoContainer

    override fun onInitView() {
        super.onInitView()
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
        viewModel.idAlbum = intent.getStringExtra(ALBUM_ID).toString()
        viewModel.nameAlbum = intent.getStringExtra(ALBUM_NAME).toString()
        binding.hvSelectVideo.setTextCenter(viewModel.nameAlbum)
        binding.hvSelectVideo.setOnLeftIconClickListener {
            navigateBack()
        }
        binding.btnSelectVideoAdd.setOnSafeClick {
            var intent = Intent(this@SelectVideoActivity, AdjustActivity::class.java)
            intent.putStringArrayListExtra(AdjustActivity.LIST_VIDEO, viewModel.getListPath())
            startActivity(intent)
        }
        loadVideo()
        initAddRecycleView()
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

    private fun loadVideo() {
        binding.rvSelectVideoToAdd.adapter = selectVideoAdapter
        binding.rvSelectVideoToAdd.layoutManager = GridLayoutManager(this, 4)
        initRecycleView()
        viewModel.getVideoList(viewModel.idAlbum)
    }

    private fun initRecycleView() {
        selectVideoAdapter.listener = object : SelectVideoAdapter.IListener {
            override fun onVideoClick(videoDisplay: SelectVideoAdapter.VideoDisplay) {
                when (videoDisplay.isSelected) {
                    true -> {
                        viewModel.listVideoAdd.add(videoDisplay)
                        updateAddView()
                        updateSelectInAddAdapter()
                        autoScrollLog()
                    }
                    false -> {
                        viewModel.listVideoAdd.remove(videoDisplay)
                        updateAddView()
                        updateSelectInAddAdapter()
                        autoScrollLog()
                    }
                }
            }

            override fun onVideoLongClick(path: String) {
                replaceFragment(
                    PreviewImageFragment(),
                    bundleOf(PreviewImageFragment.VIDEO_PATH to path)
                )
            }
        }
    }

    private fun autoScrollLog(){
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
