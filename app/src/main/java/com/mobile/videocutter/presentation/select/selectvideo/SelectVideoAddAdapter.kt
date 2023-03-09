package com.mobile.videocutter.presentation.select.selectvideo

import android.net.Uri
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.adapter.BaseAdapter
import com.mobile.videocutter.base.common.adapter.BaseVH
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.MyStudioVideoItemBinding
import com.mobile.videocutter.databinding.SelectLibraryFolderItemBinding
import com.mobile.videocutter.domain.model.Album
import com.mobile.videocutter.domain.model.Video


class SelectVideoAddAdapter : BaseAdapter() {
    override fun getLayoutResource(viewType: Int): Int = R.layout.my_studio_video_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return SelectVideoAddVH(binding as MyStudioVideoItemBinding)
    }

    @Suppress("DEPRECATION")
    inner class SelectVideoAddVH(private val binding: MyStudioVideoItemBinding) : BaseVH<Video>(binding) {

        override fun onBind(data: Video) {
            super.onBind(data)
            binding.tvMyStudioVideoItmDuration.text = data.durationVideo
            binding.ivMyStudioVideoItmImage.setImageBitmap(data.thumbnail)
        }
    }
}
