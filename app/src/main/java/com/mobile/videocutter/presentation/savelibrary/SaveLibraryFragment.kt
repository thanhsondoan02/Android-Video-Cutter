package com.mobile.videocutter.presentation.savelibrary

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingFragment
import com.mobile.videocutter.databinding.SaveLibraryFragmentBinding
import com.mobile.videocutter.presentation.model.IViewListener
import com.mobile.videocutter.presentation.widget.recyclerview.LAYOUT_MANAGER_MODE
import handleUiState
import shareLink

class SaveLibraryFragment : BaseBindingFragment<SaveLibraryFragmentBinding>(R.layout.save_library_fragment) {

    companion object {
        const val LINKED = "https://www.youtube.com/"
    }

    private val viewModel by viewModels<ShareViewModel>()
    private val shareInfoAdapter by lazy { ShareInfoAdapter() }

    override fun onInitView() {
        super.onInitView()
        initView()
        setDataToListSocial()
    }

    fun initView() {
//        binding.
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()
        lifecycleScope.launchWhenCreated {
            viewModel.appInfoList.collect {
                handleUiState(it, object : IViewListener {
                    override fun onSuccess() {
                        shareInfoAdapter.submitList(it.data)
                    }
                })
            }
        }
    }

    private fun setDataToListSocial() {
        binding.apply {
            crvSaveLibrary.setAdapter(shareInfoAdapter)
            crvSaveLibrary.setLayoutManagerMode(LAYOUT_MANAGER_MODE.LINEAR_HORIZATION)
            shareInfoAdapter.listener = object : ShareInfoAdapter.IClickSocialItem {
                override fun onItemClick(socialItem: AppInfo) {
                    context?.shareLink(LINKED)
                }
            }
        }
    }
}
