package com.mobile.videocutter.presentation.select.selectlibrary

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.databinding.SelectLibraryFolderActivityBinding
import com.mobile.videocutter.presentation.model.IViewListener
import com.mobile.videocutter.presentation.select.selectvideo.SelectVideoActivity
import handleUiState

class SelectLibraryFolderActivity : BaseBindingActivity<SelectLibraryFolderActivityBinding>(R.layout.select_library_folder_activity) {
    private val viewModel by viewModels<SelectLibraryFolderViewModel>()
    private val selectLibFolderAdapter by lazy {
        SelectLibraryFolderAdapter()
    }

    override fun onInitView() {
        super.onInitView()
        binding.rvSelectLibraryFolder.layoutManager = LinearLayoutManager(baseContext)
        binding.rvSelectLibraryFolder.adapter = selectLibFolderAdapter
        binding.hvSelectLibraryFolder.setVisibleViewUnderLine(false)
        binding.hvSelectLibraryFolder.setOnLeftIconClickListener {
            navigateBack()
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            doRequestPermission(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                object : PermissionListener {
                    override fun onAllow() {
                        viewModel.getAlbumList()
                        initListener()
                    }

                    override fun onDenied(neverAskAgainPermissionList: List<String>) {
                    }
                })
        } else {
            viewModel.getAlbumList()
            initListener()
        }
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()
        lifecycleScope.launchWhenCreated {
            viewModel.selectLibraryFolderState.collect {
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
            override fun onClickAlbum(idAlbum: Long, nameAlbum: String) {
                val intent = Intent(this@SelectLibraryFolderActivity, SelectVideoActivity::class.java)
                intent.putExtra(SelectVideoActivity.ALBUM_ID, idAlbum.toString())
                intent.putExtra(SelectVideoActivity.ALBUM_NAME, nameAlbum)
                startActivity(intent)
            }
        }
    }
}
