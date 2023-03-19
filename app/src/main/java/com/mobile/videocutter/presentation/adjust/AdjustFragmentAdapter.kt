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

    override fun getDiffUtil(oldList: List<Any>, newList: List<Any>): DiffUtil.Callback {
        return DiffCallback(oldList as List<LocalVideo>, newList as List<LocalVideo>)
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

    inner class AddVH(private val binding: AddVideoItemBinding) : BaseVH<LocalVideo>(binding) {
        init {
            binding.ivAddVideoItmButton.setOnSafeClick {
                listener?.onAdd()
            }
        }
    }

    class DiffCallback(oldData: List<LocalVideo>, newData: List<LocalVideo>) :
        BaseDiffUtilCallback<LocalVideo>(oldData, newData) {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldUser = (getOldItem(oldItemPosition) as? LocalVideo)
            val newUser = (getNewItem(newItemPosition) as? LocalVideo)

            return oldUser?.hashCode() == newUser?.hashCode()
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldUser = (getOldItem(oldItemPosition) as? LocalVideo)
            val newUser = (getNewItem(newItemPosition) as? LocalVideo)

            return oldUser == newUser
        }
    }

    interface IListener {
        fun onDelete(item: LocalVideo)
        fun onAdd()
    }
}
