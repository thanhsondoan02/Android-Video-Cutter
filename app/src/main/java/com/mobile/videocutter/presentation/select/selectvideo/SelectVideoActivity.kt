package com.mobile.videocutter.presentation.select.selectvideo

import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.base.extension.gone
import com.mobile.videocutter.base.extension.show
import com.mobile.videocutter.databinding.SelectVideoActivityBinding
import com.mobile.videocutter.presentation.model.IViewListener
import com.mobile.videocutter.presentation.select.preview.PreviewImageFragment
import handleUiState

class SelectVideoActivity : BaseBindingActivity<SelectVideoActivityBinding>(R.layout.select_video_activity) {
    private val selectVideoAdapter by lazy { SelectVideoAdapter() }
    private val selectVideoAddAdapter by lazy { SelectVideoAddAdapter() }

    private val viewModel by viewModels<SelectVideoViewModel>()

    override fun onInitView() {
        super.onInitView()
        binding.rvSelectVideoAdd.adapter = selectVideoAddAdapter
        binding.rvSelectVideoAdd.layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.HORIZONTAL, false)
        viewModel.idAlbum = intent.getStringExtra("idAlbum").toString()
        viewModel.nameAlbum = intent.getStringExtra("nameAlbum").toString()
        binding.hvSelectVideo.setTextCenter(viewModel.nameAlbum)
        binding.hvSelectVideo.setOnLeftIconClickListener {
            onBackPressedDispatcher.onBackPressed()
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
                    }
                    false -> {
                        viewModel.listVideoAdd.remove(videoDisplay)
                        updateAddView()
                        updateSelectInAddAdapter()
                    }
                }
            }

            override fun onVideoLongClick(path: String) {
                val intent = Intent(this@SelectVideoActivity, PreviewImageFragment::class.java)
                intent.putExtra("path", path)
                startActivity(intent)
            }
        }
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
