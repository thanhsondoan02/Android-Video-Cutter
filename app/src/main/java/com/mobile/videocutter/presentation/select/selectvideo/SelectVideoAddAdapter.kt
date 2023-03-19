package com.mobile.videocutter.presentation.select.selectvideo

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.adapter.BaseAdapter
import com.mobile.videocutter.base.common.adapter.BaseDiffUtilCallback
import com.mobile.videocutter.base.common.adapter.BaseVH
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.SelectVideoAddItemBinding
import com.mobile.videocutter.domain.model.LocalVideo
import loadImage

class SelectVideoAddAdapter : BaseAdapter() {
    var listener: IListener? = null

    override fun getLayoutResource(viewType: Int): Int = R.layout.select_video_add_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return SelectVideoAddVH(binding as SelectVideoAddItemBinding)
    }

    override fun getDiffUtil(oldList: List<Any>, newList: List<Any>): DiffUtil.Callback {
        return DiffCallback(oldList as List<LocalVideo>, newList as List<LocalVideo>)
    }

    inner class SelectVideoAddVH(private val binding: SelectVideoAddItemBinding) : BaseVH<LocalVideo>(binding) {
        init {
            binding.ivAdjustDeleteItm.setOnSafeClick {
                var item = getDataAtPosition(bindingAdapterPosition) as? LocalVideo
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

            return oldUser?.videoPath == newUser?.videoPath
        }
    }

    interface IListener {
        fun onDelete(item: LocalVideo)
    }
}
