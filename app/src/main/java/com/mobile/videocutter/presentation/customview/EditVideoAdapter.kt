package com.mobile.videocutter.presentation.customview

import android.graphics.Bitmap
import androidx.databinding.ViewDataBinding
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.adapter.BaseAdapter
import com.mobile.videocutter.base.common.adapter.BaseVH
import com.mobile.videocutter.databinding.EditVideoViewBinding

class EditVideoAdapter : BaseAdapter() {

    override fun getLayoutResource(viewType: Int): Int = R.layout.cut_video_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*> {
        return EditVideoVH(binding as EditVideoViewBinding)
    }

    class EditVideoVH(private val binding: EditVideoViewBinding) : BaseVH<Bitmap>(binding) {

        override fun onBind(data: Bitmap) {
            super.onBind(data)

        }
    }
}
