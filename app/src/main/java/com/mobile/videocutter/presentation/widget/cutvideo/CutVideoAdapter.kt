package com.mobile.videocutter.presentation.widget.cutvideo

import android.graphics.Bitmap
import androidx.databinding.ViewDataBinding
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.adapter.BaseAdapter
import com.mobile.videocutter.base.common.adapter.BaseVH
import com.mobile.videocutter.databinding.CutVideoItemBinding

class CutVideoAdapter : BaseAdapter() {

    override fun getLayoutResource(viewType: Int) = R.layout.cut_video_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*> {
        return EditVideoVH(binding as CutVideoItemBinding)
    }

    inner class EditVideoVH(private val binding: CutVideoItemBinding) : BaseVH<Bitmap>(binding) {
        override fun onBind(data: Bitmap) {
            super.onBind(data)
            binding.ivCutVideoItmRoot.setImageBitmap(data)
        }
    }
}
