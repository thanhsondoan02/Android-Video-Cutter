package com.mobile.videocutter.presentation.select.selectvideo

import androidx.databinding.ViewDataBinding
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.adapter.BaseAdapter
import com.mobile.videocutter.base.common.adapter.BaseVH
import com.mobile.videocutter.databinding.MyStudioVideoItemBinding
import com.mobile.videocutter.domain.model.Video


class SelectVideoAdapter : BaseAdapter() {
    companion object {
        const val SELECT_PAYLOAD = "SELECT_PAYLOAD"
    }

    var listener: IListener? = null

    private var selectedIndexList = mutableListOf<Int>()

    override fun getLayoutResource(viewType: Int) = R.layout.my_studio_video_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*> {
        return VideoVH(binding as MyStudioVideoItemBinding)
    }

    inner class VideoVH(private val itemBinding: MyStudioVideoItemBinding) : BaseVH<Video>(itemBinding) {

        override fun onBind(data: Video) {
            itemBinding.tvMyStudioVideoItmDuration.text = data.durationVideo.toString()
            itemBinding.ivMyStudioVideoItmImage.setImageBitmap(data.thumbnail)
        }
    }

    class VideoDisplay(var video: Video, var isSelected: Boolean = false)

    enum class STATE {
        NORMAL, SELECT
    }

    interface IListener {
        fun onVideoClick(video: Video?, state: STATE, size: Int)
    }
}
