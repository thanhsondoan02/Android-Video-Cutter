package com.mobile.videocutter.presentation.select.selectvideo

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.base.extension.getAppString
import com.mobile.videocutter.base.extension.gone
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.base.extension.show
import com.mobile.videocutter.databinding.SelectVideoActivityBinding
import com.mobile.videocutter.domain.model.VideoDisplay
import com.mobile.videocutter.presentation.adjust.AdjustActivity
import com.mobile.videocutter.presentation.model.IViewListener
import com.mobile.videocutter.presentation.select.preview.PreviewImageFragment
import com.mobile.videocutter.presentation.select.selectlibrary.SelectLibraryFolderFragment
import com.mobile.videocutter.presentation.widget.recyclerview.CustomRecyclerView
import com.mobile.videocutter.presentation.widget.recyclerview.LAYOUT_MANAGER_MODE
import handleUiState

class SelectVideoActivity : BaseBindingActivity<SelectVideoActivityBinding>(R.layout.select_video_activity) {
    companion object {
        const val RESULT_LIST_VIDEO = "RESULT_LIST_VIDEO"
    }

    private val selectVideoAdapter by lazy { SelectVideoAdxapter() }
    private val selectVideoAddAdapter by lazy { SelectVideoAddAdapter() }
    private val viewModel by viewModels<SelectVideoViewModel>()

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result?.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val listVideoDeleted: ArrayList<VideoDisplay> = data?.getParcelableArrayListExtra(RESULT_LIST_VIDEO)
                ?: arrayListOf()
            viewModel.updateVideoAdded(listVideoDeleted)
            viewModel.updateVideoSelected(listVideoDeleted)
        }
    }

    override fun getContainerId(): Int = R.id.flSelectVideoContainer

    override fun onPrepareInitView() {
        super.onPrepareInitView()
        viewModel.getVideoList()
    }

    override fun onInitView() {
        super.onInitView()

        // rv select
        binding.crvSelectVideoToAdd.apply {
            setAdapter(selectVideoAdapter)
            setLayoutManagerMode(LAYOUT_MANAGER_MODE.GRID)
        }

        selectVideoAdapter.listener = object : SelectVideoAdxapter.IListener {
            override fun onVideoClick(videoDisplay: VideoDisplay) {
                if (videoDisplay.isSelected) {
                    viewModel.addVideoDisplay(videoDisplay)
                } else {
                    viewModel.removeVideoDisplay(videoDisplay)
                }
            }

            override fun onVideoLongClick(path: String) {
                replaceFragment(
                    PreviewImageFragment(),
                    bundleOf(PreviewImageFragment.VIDEO_PATH to path)
                )
            }
        }

        // rv add
        binding.crvSelectVideoAdd.apply {
            setAdapter(selectVideoAddAdapter)
            setLayoutManagerMode(LAYOUT_MANAGER_MODE.LINEAR_HORIZATION)
            setDragRecyclerView(true)
            listener = object : CustomRecyclerView.IListener {
                override fun onScroll(newPosition: Int, oldPosition: Int) {
                    binding.crvSelectVideoAdd.smoothiePosition(newPosition)
                    viewModel.dragVideo(oldPosition,newPosition)
                }
            }
        }

        selectVideoAddAdapter.listener = object : SelectVideoAddAdapter.IListener {
            override fun onDelete(item: VideoDisplay) {
                viewModel.removeVideoDisplay(item)
            }
        }

        // end recyclerview
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
            val list = viewModel.getListSelected() as ArrayList<VideoDisplay>
            val intent = Intent(this@SelectVideoActivity, AdjustActivity::class.java)
            intent.putParcelableArrayListExtra(AdjustActivity.LIST_VIDEO, list)
            launcher.launch(intent)
        }

        binding.btnSelectVideoAdd.text = getAppString(R.string.select_add,"0")
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()
        lifecycleScope.launchWhenCreated {
            viewModel.selectVideoState.collect {
                handleUiState(it, object : IViewListener {
                    override fun onSuccess() {
                        binding.crvSelectVideoToAdd.submitList(it.data)
                    }
                })
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.count.collect {
                handleUiState(it, object : IViewListener {
                    override fun onSuccess() {
                        binding.btnSelectVideoAdd.text = getString(R.string.select_add).replaceFirst("0", it.data.toString())
                    }
                })
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.video.collect {
                handleUiState(it, object : IViewListener {
                    override fun onSuccess() {
                        if (it.data != null) {
                            binding.crvSelectVideoAdd.submitList(it.data)
                            updateAddView(it.data!!)
                            binding.btnSelectVideoAdd.text = getAppString(R.string.select_add,it.data!!.size.toString())
                        }
                    }
                })
            }
        }
    }

    private fun updateAddView(list: List<VideoDisplay>) {
        if (list.isNotEmpty()) {
            binding.llSelectVideoAdd.show()
        } else {
            binding.llSelectVideoAdd.gone()
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
}
