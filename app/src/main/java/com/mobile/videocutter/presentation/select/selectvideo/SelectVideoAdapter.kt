package com.mobile.videocutter.presentation.select.selectvideo

import androidx.databinding.ViewDataBinding
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.adapter.BaseAdapter
import com.mobile.videocutter.base.common.adapter.BaseVH
import com.mobile.videocutter.base.extension.getAppDimension
import com.mobile.videocutter.base.extension.getAppDrawable
import com.mobile.videocutter.base.extension.hide
import com.mobile.videocutter.base.extension.show
import com.mobile.videocutter.databinding.MyStudioVideoItemBinding
import com.mobile.videocutter.domain.model.LocalVideo
import com.mobile.videocutter.domain.model.Video

class SelectVideoAdapter : BaseAdapter() {
    companion object {
        const val SELECT_PAYLOAD = "SELECT_PAYLOAD"
    }

    var listener: IListener? = null
    var state = STATE.NORMAL
        set(value) {
            field = value
            if (value == STATE.NORMAL) {
                selectedIndexList.forEach {
                    (getDataAtPosition(it) as? VideoDisplay)?.isSelected = false
                    notifyItemChanged(it, SELECT_PAYLOAD)
                }
                selectedIndexList.clear()
            }
        }

    private var selectedIndexList = mutableListOf<Int>()

    override fun getLayoutResource(viewType: Int) = R.layout.my_studio_video_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*> {
        return VideoVH(binding as MyStudioVideoItemBinding)
    }

    inner class VideoVH(private val itemBinding: MyStudioVideoItemBinding) : BaseVH<VideoDisplay>(itemBinding) {

        init {
            itemBinding.root.setOnClickListener {
                val videoDisplay = (getDataAtPosition(adapterPosition) as? VideoDisplay)
                (getDataAtPosition(adapterPosition) as? VideoDisplay)?.isSelected = videoDisplay?.isSelected?.not() ?: false
                updateSelect(videoDisplay?.isSelected == true)
                if (videoDisplay?.isSelected == true) {
                    selectedIndexList.add(adapterPosition)
                } else {
                    selectedIndexList.remove(adapterPosition)
                }
                videoDisplay?.video?.let { it1 -> listener?.onVideoClick(it1, state, selectedIndexList.size) }
            }
        }

        override fun onBind(data: VideoDisplay) {
            itemBinding.tvMyStudioVideoItmDuration.text = data.video.durationVideo
            itemBinding.ivMyStudioVideoItmImage.setImageBitmap(data.video.thumbnail)
            updateSelect(data.isSelected)
        }

        override fun onBind(data: VideoDisplay, payloads: List<Any>) {
            if (payloads.contains(SELECT_PAYLOAD)) {
                updateSelect(data.isSelected)
            }
        }

        private fun updateSelect(isSelected: Boolean) {
            if (isSelected) {
                itemBinding.ivMyStudioVideoItmSelected.background = getAppDrawable(R.drawable.ic_select_item)
                itemBinding.mcvMyStudioVideoItmRoot.strokeWidth = getAppDimension(R.dimen.dimen_2).toInt()
            } else {
                itemBinding.ivMyStudioVideoItmSelected.background = getAppDrawable(R.drawable.ic_unselect_item)
                itemBinding.mcvMyStudioVideoItmRoot.strokeWidth = 0
            }
        }
    }

    class VideoDisplay(var video: Video, var isSelected: Boolean = false)

    enum class STATE {
        NORMAL, SELECT
    }

    interface IListener {
        fun onVideoClick(video: Video, state: STATE, size: Int)
    }
}
