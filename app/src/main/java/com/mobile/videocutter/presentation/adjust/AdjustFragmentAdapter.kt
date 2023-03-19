package com.mobile.videocutter.presentation.adjust

import androidx.databinding.ViewDataBinding
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.adapter.BaseAdapter
import com.mobile.videocutter.base.common.adapter.BaseVH
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.AddVideoItemBinding
import com.mobile.videocutter.databinding.AdjustVideoItemBinding
import com.mobile.videocutter.domain.model.LocalVideo
import loadImage

class AdjustFragmentAdapter : BaseAdapter() {
    companion object {
        const val VIDEO_TYPE = 321
        const val ADD_TYPE = 123
    }
    
    var listener: IListener? = null

    override fun getLayoutResource(viewType: Int): Int {
        return if (viewType == VIDEO_TYPE) {
            R.layout.adjust_video_item
        } else {
            R.layout.add_video_item
        }
    }

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return if (viewType == VIDEO_TYPE) {
            VideoVH(binding as AdjustVideoItemBinding)
        } else {
            AddVH(binding as AddVideoItemBinding)
        }
    }

    override fun getItemViewTypeCustom(position: Int): Int {
        return if (getDataAtPosition(position) is LocalVideo) {
            VIDEO_TYPE
        } else {
            ADD_TYPE
        }
    }

    inner class VideoVH(private val binding: AdjustVideoItemBinding) : BaseVH<LocalVideo>(binding) {
        init {
            binding.ivAdjustDeleteItm.setOnSafeClick {
                val item = getDataAtPosition(bindingAdapterPosition) as? LocalVideo
                if (item != null) {
                    listener?.onDelete(item)
                }
            }
        }

        override fun onBind(data: LocalVideo) {
            super.onBind(data)
            binding.tvAdjustVideoItmDuration.text = data.getFormattedDuration()
            binding.ivAdjustVideoItmImage.loadImage(data.videoPath)
        }
    }

    inner class AddVH(private val binding: AddVideoItemBinding) : BaseVH<LocalVideo>(binding)

    interface IListener {
        fun onDelete(item: LocalVideo)
    }
}
