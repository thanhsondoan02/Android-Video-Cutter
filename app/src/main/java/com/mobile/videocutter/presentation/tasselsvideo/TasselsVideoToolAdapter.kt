package com.mobile.videocutter.presentation.tasselsvideo

import androidx.databinding.ViewDataBinding
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.adapter.BaseAdapter
import com.mobile.videocutter.base.common.adapter.BaseVH
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.ToolItemBinding
import com.mobile.videocutter.domain.model.ToolVideo

class TasselsVideoToolAdapter : BaseAdapter() {
    var listener: IListener? = null

    override fun getLayoutResource(viewType: Int) = R.layout.tool_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return TasselsVideoVH(binding as ToolItemBinding)
    }

    inner class TasselsVideoVH(val binding: ToolItemBinding) : BaseVH<ToolVideo>(binding) {
        init {
            binding.root.setOnSafeClick {
                listener?.onToolClick(getDataAtPosition(adapterPosition) as? ToolVideo)
            }
        }

        override fun onBind(data: ToolVideo) {
            super.onBind(data)
            binding.ivToolItmAvatar.setImageResource(data.getImage())
            binding.tvToolItmName.text = data.getName()
        }
    }

    interface IListener {
        fun onToolClick(toolVideo: ToolVideo?)
    }
}
