package com.mobile.videocutter.presentation.tasselsvideo

import android.graphics.Bitmap
import androidx.databinding.ViewDataBinding
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.adapter.BaseAdapter
import com.mobile.videocutter.base.common.adapter.BaseVH
import com.mobile.videocutter.base.extension.getAppColor
import com.mobile.videocutter.base.extension.getAppDrawable
import com.mobile.videocutter.databinding.TimeLineItemBinding

class TasselsVideoTimeLineAdapter : BaseAdapter() {

    override fun getLayoutResource(viewType: Int) = R.layout.time_line_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*> {

        return TasselsVideoTimeLineVH(binding as TimeLineItemBinding)
    }

    inner class TasselsVideoTimeLineVH(private val binding: TimeLineItemBinding) : BaseVH<Bitmap>(binding) {
        override fun onBind(data: Bitmap) {
            super.onBind(data)
            binding.ivTimeLineAvatar.setImageBitmap(data)
        }
    }
}
