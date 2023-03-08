package com.mobile.videocutter.presentation.adjust

import androidx.databinding.ViewDataBinding
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.adapter.BaseAdapter
import com.mobile.videocutter.base.common.adapter.BaseVH
import com.mobile.videocutter.databinding.AdjustVideoItemBinding
import com.mobile.videocutter.domain.model.LocalVideo

class AdjustAdapter : BaseAdapter() {
    override fun getLayoutResource(viewType: Int) = R.layout.my_studio_video_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return AdjustVH(binding)
    }

    inner class AdjustVH(private val binding: ViewDataBinding) : BaseVH<LocalVideo>(binding) {

        val viewBinding = binding as AdjustVideoItemBinding

        override fun onBind(data: LocalVideo) {
            super.onBind(data)

            viewBinding.ivAdjustVideoItmImage

        }
    }
}
