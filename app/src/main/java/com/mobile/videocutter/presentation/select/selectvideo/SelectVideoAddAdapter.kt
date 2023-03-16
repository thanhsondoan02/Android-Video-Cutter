package com.mobile.videocutter.presentation.select.selectvideo

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.adapter.BaseAdapter
import com.mobile.videocutter.base.common.adapter.BaseDiffUtilCallback
import com.mobile.videocutter.base.common.adapter.BaseVH
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.AdjustVideoItemBinding
import com.mobile.videocutter.domain.model.VideoDisplay
import loadImage

class SelectVideoAddAdapter : BaseAdapter() {
    var listener: IListener? = null

    override fun getLayoutResource(viewType: Int): Int = R.layout.adjust_video_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return SelectVideoAddVH(binding as AdjustVideoItemBinding)
    }

    override fun getDiffUtil(oldList: List<Any>, newList: List<Any>): DiffUtil.Callback {
        return DiffCallback(oldList as List<VideoDisplay>, newList as List<VideoDisplay>)
    }

    inner class SelectVideoAddVH(private val binding: AdjustVideoItemBinding) : BaseVH<VideoDisplay>(binding) {
        init {
            binding.ivAdjustDeleteItm.setOnSafeClick {
                var item = getDataAtPosition(bindingAdapterPosition) as? VideoDisplay
                if (item != null) {
                    listener?.onDelete(item)
                }
            }
        }

        override fun onBind(data: VideoDisplay) {
            super.onBind(data)
            binding.tvAdjustVideoItmDuration.text = data.video.getFormattedDuration()
            binding.ivAdjustVideoItmImage.loadImage(data.video.videoPath)
        }
    }

    class DiffCallback(oldData: List<VideoDisplay>, newData: List<VideoDisplay>) :
        BaseDiffUtilCallback<VideoDisplay>(oldData, newData) {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldData = (getOldItem(oldItemPosition) as? VideoDisplay)
            val newData = (getNewItem(newItemPosition) as? VideoDisplay)

            return oldData?.hashCode() == newData?.hashCode()
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldData = (getOldItem(oldItemPosition) as? VideoDisplay)
            val newData = (getNewItem(newItemPosition) as? VideoDisplay)

            return oldData?.isSelected == newData?.isSelected
        }
    }

    interface IListener {
        fun onDelete(item: VideoDisplay)
    }
}
