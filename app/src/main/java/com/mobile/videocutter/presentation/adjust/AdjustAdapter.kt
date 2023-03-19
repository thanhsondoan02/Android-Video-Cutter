package com.mobile.videocutter.presentation.adjust

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.adapter.BaseAdapter
import com.mobile.videocutter.base.common.adapter.BaseDiffUtilCallback
import com.mobile.videocutter.base.common.adapter.BaseVH
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.AddVideoItemBinding
import com.mobile.videocutter.databinding.AdjustVideoItemBinding
import com.mobile.videocutter.domain.model.LocalVideo
import com.mobile.videocutter.domain.model.VideoDisplay
import com.mobile.videocutter.presentation.exampleloadmore.TestAdapter
import getFormattedTime
import loadImage

class AdjustAdapter : BaseAdapter() {

    companion object {
        private const val DATA_ADJUST_VIDEO_ITEM = 0
        private const val ADD_DATA_ADJUST_VIDEO_ITEM = 1
    }

    var listener: IListener? = null

    override fun getLayoutResource(viewType: Int): Int {
        return when (viewType) {
            DATA_ADJUST_VIDEO_ITEM -> R.layout.adjust_video_item
            ADD_DATA_ADJUST_VIDEO_ITEM -> R.layout.add_video_item
            else -> R.layout.adjust_video_item
        }
    }

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return when (viewType) {
            DATA_ADJUST_VIDEO_ITEM -> AdjustVH(binding)
            ADD_DATA_ADJUST_VIDEO_ITEM -> AddAdjustVH(binding)
            else -> AdjustVH(binding)
        }
    }

    override fun getItemViewTypeCustom(position: Int): Int {
        return when (dataList[position]) {
            is VideoDisplay -> DATA_ADJUST_VIDEO_ITEM
            else -> ADD_DATA_ADJUST_VIDEO_ITEM
        }
    }

    override fun getDiffUtil(oldList: List<Any>, newList: List<Any>): DiffUtil.Callback {
        return DiffCallback(oldList as List<VideoDisplay>, newList as List<VideoDisplay>)
    }

    inner class AddAdjustVH(private val binding: ViewDataBinding) : BaseVH<Any>(binding){
        private val viewBinding =  binding as AddVideoItemBinding

        init {
            viewBinding.root.setOnSafeClick {
                listener?.onAddMore()
            }
        }
    }

    inner class AdjustVH(private val binding: ViewDataBinding) : BaseVH<VideoDisplay>(binding) {
        private val viewBinding = binding as AdjustVideoItemBinding

        init {
            viewBinding.ivAdjustDeleteItm.setOnSafeClick {
                val item = getDataAtPosition(bindingAdapterPosition) as? VideoDisplay
                if (item != null) {
                    listener?.onDelete(item)
                }
            }
        }

        override fun onBind(data: VideoDisplay) {
            super.onBind(data)
            viewBinding.ivAdjustVideoItmImage.loadImage(data.video.getImageThumbPath())
            viewBinding.tvAdjustVideoItmDuration.text = getFormattedTime(data.video.duration)
        }
    }

    class DiffCallback(oldData: List<VideoDisplay>, newData: List<VideoDisplay>) :
        BaseDiffUtilCallback<VideoDisplay>(oldData, newData) {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldUser = (getOldItem(oldItemPosition) as? VideoDisplay)
            val newUser = (getNewItem(newItemPosition) as? VideoDisplay)

            return oldUser?.hashCode() == newUser?.hashCode()
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldUser = (getOldItem(oldItemPosition) as? VideoDisplay)
            val newUser = (getNewItem(newItemPosition) as? VideoDisplay)

            return oldUser == newUser
        }
    }

    interface IListener {
        fun onDelete(localVideo: VideoDisplay)
        fun onAddMore()
    }
}
