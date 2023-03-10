package com.mobile.videocutter.presentation.select.selectlibrary

import android.net.Uri
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.adapter.BaseAdapter

import com.mobile.videocutter.base.common.adapter.BaseVH
import com.mobile.videocutter.domain.model.Album
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.SelectLibraryFolderItemBinding

class SelectLibraryFolderAdapter : BaseAdapter() {

    var albumListener: IAlbumListener? = null

    override fun getLayoutResource(viewType: Int): Int = R.layout.select_library_folder_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return SelectLibraryFolderVH(binding as SelectLibraryFolderItemBinding)
    }

    inner class SelectLibraryFolderVH(private val binding: SelectLibraryFolderItemBinding) : BaseVH<Album>(binding) {
        init {
            binding.constSelectLibFolderItm.setOnSafeClick {
                var item = getDataAtPosition(adapterPosition)
                (item as? Album)?.let {
                    it.idAlbum?.let { it1 -> it.nameAlbum?.let { it2 -> albumListener?.onClickAlbum(it1, it2) } }
                }
            }
        }

        override fun onBind(data: Album) {
            super.onBind(data)
            Glide.with(binding.root.context)
                .load(Uri.parse(data.coverUriAlbum.toString()))
                .placeholder(R.drawable.ic_default_null_image_album)
                .into(binding.ivSelectLibraryFolderItmBackGround)
            binding.tvSelectLibraryFolderItmName.text = data.nameAlbum
            binding.tvSelectLibraryFolderItmCount.text = "(${data.countAlbum})"
        }
    }

    interface IAlbumListener {
        fun onClickAlbum(idAlbum: Long, nameAlbum: String)
    }
}
