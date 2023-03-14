package com.mobile.videocutter.presentation.select.selectvideo

import android.net.Uri
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.adapter.BaseAdapter
import com.mobile.videocutter.base.common.adapter.BaseDiffUtilCallback
import com.mobile.videocutter.base.common.adapter.BaseVH
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.AddVideoItemBinding
import com.mobile.videocutter.databinding.AdjustVideoItemBinding
import com.mobile.videocutter.databinding.MyStudioVideoItemBinding
import com.mobile.videocutter.databinding.SelectLibraryFolderItemBinding
import com.mobile.videocutter.domain.model.Album
import com.mobile.videocutter.domain.model.Video
import com.mobile.videocutter.presentation.exampleloadmore.TestAdapter
import loadImage

class SelectVideoAddAdapter : BaseAdapter() {
    var listener: IListener? = null
    override fun getLayoutResource(viewType: Int): Int = R.layout.adjust_video_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return SelectVideoAddVH(binding as AdjustVideoItemBinding)
    }

    inner class SelectVideoAddVH(private val binding: AdjustVideoItemBinding) : BaseVH<SelectVideoAdapter.VideoDisplay>(binding) {
        init {
            binding.ivAdjustDeleteItm.setOnSafeClick {
                var item = getDataAtPosition(bindingAdapterPosition) as SelectVideoAdapter.VideoDisplay
                if (item != null) {
                    listener?.onDelete(item)
                }
            }
        }

        override fun onBind(data: SelectVideoAdapter.VideoDisplay) {
            super.onBind(data)
            binding.tvAdjustVideoItmDuration.text = data.video.getFormattedDuration()
            binding.ivAdjustVideoItmImage.loadImage(data.video.videoPath)
        }
    }

    interface IListener {
        fun onDelete(item: SelectVideoAdapter.VideoDisplay)
    }
}
