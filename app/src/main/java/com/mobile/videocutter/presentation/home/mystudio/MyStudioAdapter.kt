package com.mobile.videocutter.presentation.home.mystudio

import androidx.databinding.ViewDataBinding
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.adapter.BaseAdapter
import com.mobile.videocutter.base.common.adapter.BaseVH
import com.mobile.videocutter.base.extension.getAppDimension
import com.mobile.videocutter.base.extension.hide
import com.mobile.videocutter.base.extension.show
import com.mobile.videocutter.databinding.MyStudioVideoItemBinding
import com.mobile.videocutter.domain.model.LocalVideo
import loadImage

class MyStudioAdapter: BaseAdapter() {
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
                if (state == STATE.SELECT) {
                    (getDataAtPosition(adapterPosition) as? VideoDisplay)?.isSelected = videoDisplay?.isSelected?.not() ?: false
                    updateSelect(videoDisplay?.isSelected == true)
                    if (videoDisplay?.isSelected == true) {
                        selectedIndexList.add(adapterPosition)
                    } else {
                        selectedIndexList.remove(adapterPosition)
                    }
                }
                listener?.onVideoClick(videoDisplay?.video, state, selectedIndexList.size)
            }
        }

        override fun onBind(data: VideoDisplay) {
            itemBinding.tvMyStudioVideoItmDuration.text = data.video.getFormattedDuration()
            itemBinding.ivMyStudioVideoItmImage.loadImage(data.video.videoPath)
            updateSelect(data.isSelected)
        }

        override fun onBind(data: VideoDisplay, payloads: List<Any>) {
            if (payloads.contains(SELECT_PAYLOAD)) {
                updateSelect(data.isSelected)
            }
        }

        private fun updateSelect(isSelected: Boolean) {
            if (state == STATE.SELECT && isSelected) {
                itemBinding.ivMyStudioVideoItmSelected.show()
                itemBinding.mcvMyStudioVideoItmRoot.strokeWidth = getAppDimension(R.dimen.dimen_2).toInt()
            } else {
                itemBinding.ivMyStudioVideoItmSelected.hide()
                itemBinding.mcvMyStudioVideoItmRoot.strokeWidth = 0
            }
        }
    }

    class VideoDisplay(var video: LocalVideo, var isSelected: Boolean = false)

    enum class STATE {
        NORMAL, SELECT
    }

    interface IListener {
        fun onVideoClick(video: LocalVideo?, state: STATE, size: Int)
    }
}
