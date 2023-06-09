package com.mobile.videocutter.presentation.home.mystudio

import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.base.extension.hide
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.base.extension.show
import com.mobile.videocutter.databinding.MyStudioActivityBinding
import com.mobile.videocutter.domain.model.LocalVideo
import com.mobile.videocutter.presentation.home.preview.PreviewVideoFragment
import com.mobile.videocutter.presentation.model.IViewListener
import com.mobile.videocutter.presentation.widget.recyclerview.LAYOUT_MANAGER_MODE
import handleUiState

class MyStudioActivity : BaseBindingActivity<MyStudioActivityBinding>(R.layout.my_studio_activity) {
    private val myStudioAdapter by lazy { MyStudioAdapter() }
    private val viewModel by viewModels<MyStudioViewModel>()

    override fun getContainerId(): Int = R.id.flMyStudioContainer

    override fun onInitView() {
        super.onInitView()
        initRecyclerView()
        binding.hvMyStudioHeader.apply {
            setOnLeftIconClickListener {
                when (myStudioAdapter.state) {
                    MyStudioAdapter.STATE.NORMAL -> {
                        navigateBack()
                    }
                    MyStudioAdapter.STATE.SELECT -> {
                        showRightText(true)
                        setLeftIcon(R.drawable.ic_black_back)
                        binding.llMyStudioBottomBar.hide()
                        setTextCenter(getString(R.string.my_studio))
                        myStudioAdapter.state = MyStudioAdapter.STATE.NORMAL
                    }
                }
            }
            setOnRightTextClickListener {
                when (myStudioAdapter.state) {
                    MyStudioAdapter.STATE.NORMAL -> {
                        showRightText(false)
                        setLeftIcon(R.drawable.ic_black_close)
                        binding.llMyStudioBottomBar.show()
                        setSelectedSize(0)
                        myStudioAdapter.state = MyStudioAdapter.STATE.SELECT
                    }
                    MyStudioAdapter.STATE.SELECT -> {}
                }
            }
        }
        binding.flMyStudioDelete.setOnSafeClick {
            replaceFragment(
                ConfirmFragment.Builder()
                    .setTitle(getString(R.string.delete))
                    .setDescription(getString(R.string.delete) + " 3 " + getString(R.string.confirm_delete_description))
                    .setLeftText(getString(R.string.cancel))
                    .setRightText(getString(R.string.delete))
                    .setListener(object : ConfirmFragment.IListener {
                        override fun onConfirm() {
                            // TODO
                        }
                    })
                    .getInstance()
            )
        }
        binding.flMyStudioSave.setOnSafeClick {
            replaceFragment(ShareFragment().apply {
                listener = object : ShareFragment.IListener {
                    override fun onShare() {
                        // TODO
                    }

                    override fun onSave() {
                        // TODO
                    }
                }
            })
        }

        viewModel.getMyStudioVideos()
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()

        lifecycleScope.launchWhenCreated {
            viewModel.myStudioVideoState.collect {
                handleUiState(it, object : IViewListener {
                    override fun onSuccess() {
                        binding.rvMyStudioVideoList.submitList(it.data?.map { video -> MyStudioAdapter.VideoDisplay(video) })
                    }
                })
            }
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            clearStackFragment()
        } else {
            super.onBackPressed()
        }
    }

    private fun initRecyclerView() {
        myStudioAdapter.listener = object : MyStudioAdapter.IListener {
            override fun onVideoClick(video: LocalVideo?, state: MyStudioAdapter.STATE, size: Int) {
                when (state) {
                    MyStudioAdapter.STATE.NORMAL -> {
                        replaceFragment(
                            PreviewVideoFragment(),
                            bundleOf(
                                PreviewVideoFragment.VIDEO_PATH to video?.videoPath,
                                PreviewVideoFragment.VIDEO_DURATION to video?.duration
                            )
                        )
                    }
                    MyStudioAdapter.STATE.SELECT -> {
                        setSelectedSize(size)
                    }
                }
            }
        }
        binding.rvMyStudioVideoList.setAdapter(myStudioAdapter)
        binding.rvMyStudioVideoList.setLayoutManagerMode(LAYOUT_MANAGER_MODE.GRID)
    }

    private fun setSelectedSize(size: Int) {
        binding.hvMyStudioHeader.setTextCenter("$size ${getString(R.string.selected_item)}")
    }
}
