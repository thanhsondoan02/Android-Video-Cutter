package com.mobile.videocutter.presentation.selectlibrary

import android.net.Uri
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.adapter.BaseAdapter

import com.mobile.videocutter.base.common.adapter.BaseVH
import com.mobile.videocutter.base.common.model.Album
import com.mobile.videocutter.databinding.SelectLibraryFolderItemBinding

class SelectLibraryFolderAdapter() : BaseAdapter() {
    override fun getLayoutResource(viewType: Int): Int = R.layout.select_library_folder_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return SelectLibraryFolderVH(binding as SelectLibraryFolderItemBinding)
    }

    class SelectLibraryFolderVH(private val binding: SelectLibraryFolderItemBinding) : BaseVH<Album>(binding) {
        override fun onBind(data: Album) {
            super.onBind(data)
            Glide.with(binding.root.context)
                .load(Uri.parse(data.coverUri.toString()))
                .placeholder(R.drawable.ic_default)
                .into(binding.ivSelectLibFolderItmBackGround)
            binding.tvSelectLibFolderItmName.text = data.name
            binding.tvSelectLibFolderItmCount.text = "(${data.count})"
        }
    }
}
