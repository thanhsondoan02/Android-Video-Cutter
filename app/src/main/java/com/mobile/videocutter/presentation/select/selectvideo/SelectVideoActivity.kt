package com.mobile.videocutter.presentation.select.selectvideo

import android.content.Intent
import android.provider.MediaStore
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.databinding.SelectVideoActivityBinding
import com.mobile.videocutter.domain.model.LocalVideo
import com.mobile.videocutter.domain.model.Video
import com.mobile.videocutter.presentation.home.mystudio.MyStudioAdapter
import com.mobile.videocutter.presentation.home.preview.PreviewVideoActivity
import com.mobile.videocutter.presentation.model.IViewListener
import handleUiState
import java.util.concurrent.TimeUnit

class SelectVideoActivity : BaseBindingActivity<SelectVideoActivityBinding>(R.layout.select_video_activity) {
    private var listVideoAdd = mutableListOf<Video>()
    private val selectVideoAdapter by lazy { SelectVideoAdapter() }
    private val selectVideoAddAdapter by lazy { SelectVideoAddAdapter() }

    private val viewModel by viewModels<SelectVideoViewModel>()

    override fun onInitView() {
        super.onInitView()
        viewModel.idAlbum = intent.getStringExtra("idAlbum").toString()
        viewModel.nameAlbum = intent.getStringExtra("nameAlbum").toString()
        binding.hvSelectVideo.setTextCenter(viewModel.nameAlbum)
        binding.hvSelectVideo.setOnLeftIconClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        loadVideo()
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()
        lifecycleScope.launchWhenCreated {
            viewModel.selectVideoState.collect {
                handleUiState(it, object : IViewListener {
                    override fun onSuccess() {
                        selectVideoAdapter.submitList(it.data)
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

    private fun initRecycleView(){
        selectVideoAdapter.listener = object : SelectVideoAdapter.IListener {
            override fun onVideoClick(video: Video, state: SelectVideoAdapter.STATE, size: Int) {
                when (state) {
                    SelectVideoAdapter.STATE.NORMAL -> {

                    }
                    SelectVideoAdapter.STATE.SELECT -> {
                        updateSelected(size, video)
                    }
                }
            }
        }
    }

    private fun updateSelected(size: Int, video: Video){
        binding.rvSelectVideoAdd.adapter = selectVideoAddAdapter
        binding.rvSelectVideoAdd.layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.HORIZONTAL, false)
        binding.btnSelectVideoAdd.text = "Add($size)"
        listVideoAdd.add(video)
        selectVideoAddAdapter.submitList(listVideoAdd)

    }
}
