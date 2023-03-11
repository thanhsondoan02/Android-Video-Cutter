package com.mobile.videocutter.presentation.home.mystudio

import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.base.extension.hide
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.base.extension.show
import com.mobile.videocutter.databinding.MyStudioActivityBinding
import com.mobile.videocutter.domain.model.LocalVideo
import com.mobile.videocutter.presentation.adjust.LoadingFragment
import com.mobile.videocutter.presentation.home.preview.PreviewVideoActivity
import com.mobile.videocutter.presentation.model.IViewListener
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
//            replaceFragment(ShareFragment().apply {
//                listener = object : ShareFragment.IListener {
//                    override fun onShare() {
//                        // TODO
//                    }
//
//                    override fun onSave() {
//                        // TODO
//                    }
//                }
//            })
            replaceFragment(LoadingFragment())
        }

        viewModel.getMyStudioVideos(contentResolver)
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()

        lifecycleScope.launchWhenCreated {
            viewModel.myStudioVideoState.collect {
                handleUiState(it, object : IViewListener {
                    override fun onSuccess() {
                        myStudioAdapter.submitList(it.data)
                    }
                })
            }
        }
    }

    private fun initRecyclerView() {
        myStudioAdapter.listener = object : MyStudioAdapter.IListener {
            override fun onVideoClick(video: LocalVideo?, state: MyStudioAdapter.STATE, size: Int) {
                when (state) {
                    MyStudioAdapter.STATE.NORMAL -> {
                        startActivity(Intent(this@MyStudioActivity, PreviewVideoActivity::class.java))
                    }
                    MyStudioAdapter.STATE.SELECT -> {
                        setSelectedSize(size)
                    }
                }
            }
        }
        binding.rvMyStudioVideoList.adapter = myStudioAdapter
        binding.rvMyStudioVideoList.layoutManager = GridLayoutManager(this, 4)
    }

    private fun setSelectedSize(size: Int) {
        binding.hvMyStudioHeader.setTextCenter("$size ${getString(R.string.selected_item)}")
    }
}
