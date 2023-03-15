package com.mobile.videocutter.presentation.select.selectlibrary

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingFragment
import com.mobile.videocutter.databinding.SelectLibraryFolderActivityBinding
import com.mobile.videocutter.domain.model.Album
import com.mobile.videocutter.presentation.model.IViewListener
import com.mobile.videocutter.presentation.select.selectvideo.SelectVideoActivity
import com.mobile.videocutter.presentation.select.selectvideo.SelectVideoViewModel
import handleUiState

class SelectLibraryFolderFragment : BaseBindingFragment<SelectLibraryFolderActivityBinding>(R.layout.select_library_folder_activity) {
    private val viewModelSelectVideo by activityViewModels<SelectVideoViewModel>()
    private val selectLibFolderAdapter by lazy {
        SelectLibraryFolderAdapter()
    }

    override fun onInitView() {
        super.onInitView()
        binding.rvSelectLibraryFolder.layoutManager = LinearLayoutManager(context)
        binding.rvSelectLibraryFolder.adapter = selectLibFolderAdapter
        binding.hvSelectLibraryFolder.setVisibleViewUnderLine(false)
        binding.hvSelectLibraryFolder.setOnLeftIconClickListener {
            backFragment()
        }
        viewModelSelectVideo.getAlbumList()
        initListener()
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()

        lifecycleScope.launchWhenCreated {
            viewModelSelectVideo.selectLibraryFolderState.collect {
                handleUiState(it, object : IViewListener {
                    override fun onSuccess() {
                        selectLibFolderAdapter.submitList(it.data)
                    }
                })
            }
        }
    }

    private fun initListener() {
        selectLibFolderAdapter.albumListener = object : SelectLibraryFolderAdapter.IAlbumListener {
            override fun onClickAlbum(album: Album) {
                backFragment()
                viewModelSelectVideo.getVideoList(album.idAlbum)
                viewModelSelectVideo.nameAlbum = album.nameAlbum
                (baseActivity as SelectVideoActivity).apply {
                    updateAlbumName()
                    updateHeader()
                }
            }
        }
    }
}
