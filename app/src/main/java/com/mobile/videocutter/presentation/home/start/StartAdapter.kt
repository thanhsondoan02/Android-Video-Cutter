package com.mobile.videocutter.presentation.home.start

import androidx.databinding.ViewDataBinding
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.adapter.BaseAdapter
import com.mobile.videocutter.base.common.adapter.BaseVH
import com.mobile.videocutter.databinding.StartVideoItemBinding
import com.mobile.videocutter.presentation.home.mystudio.MyStudioAdapter

class StartAdapter : BaseAdapter() {
    override fun getLayoutResource(viewType: Int) = R.layout.start_video_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*> {
        return VideoVH(binding as StartVideoItemBinding)
    }

    inner class VideoVH(private val itemBinding: StartVideoItemBinding) : BaseVH<MyStudioAdapter.VideoDisplay>(itemBinding) {
        override fun onBind(data: MyStudioAdapter.VideoDisplay) {
            itemBinding.tvStartVideoItmDuration.text = data.video.getFormattedDuration()
            itemBinding.ivStartVideoItmImage.setImageResource(R.color.color_red)
        }
    }
}
