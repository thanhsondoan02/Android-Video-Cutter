package com.mobile.videocutter.presentation.adjust.crop

import androidx.databinding.ViewDataBinding
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.adapter.BaseAdapter
import com.mobile.videocutter.base.common.adapter.BaseVH

class CropAdapter: BaseAdapter() {
    override fun getLayoutResource(viewType: Int): Int = R.layout.crop_ratio_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        TODO("Not yet implemented")
    }
}
