package com.mobile.videocutter.presentation.savelibrary

import androidx.databinding.ViewDataBinding
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.adapter.BaseAdapter
import com.mobile.videocutter.base.common.adapter.BaseVH
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.SocialNetworkItemBinding
import loadImage

class ShareInfoAdapter : BaseAdapter() {
    var listener: IClickSocialItem?= null

    override fun getLayoutResource(viewType: Int) = R.layout.social_network_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return SocialNetworkVH(binding)
    }

    inner class SocialNetworkVH(viewbinding: ViewDataBinding) : BaseVH<AppInfo>(viewbinding){
        private val binding = viewbinding as SocialNetworkItemBinding

        init {
            binding.root.setOnSafeClick {
                val item = getDataAtPosition(adapterPosition) as? AppInfo
                if(item!= null){
                    listener?.onItemClick(item)
                    notifyItemChanged(adapterPosition)
                }
            }
        }

        override fun onBind(data: AppInfo) {
            super.onBind(data)

            binding.ivSocialNetworkItm.loadImage(data.icon)
            binding.tvSocialNetworkItm.text = data.label
        }
    }
    interface IClickSocialItem{
        fun onItemClick(socialItem: AppInfo)
    }
}
