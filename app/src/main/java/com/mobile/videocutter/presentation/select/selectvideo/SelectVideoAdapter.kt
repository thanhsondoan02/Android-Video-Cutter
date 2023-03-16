package com.mobile.videocutter.presentation.select.selectvideo

import androidx.databinding.ViewDataBinding
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.adapter.BaseAdapter
import com.mobile.videocutter.base.common.adapter.BaseVH
import com.mobile.videocutter.base.extension.getAppDimension
import com.mobile.videocutter.base.extension.show
import com.mobile.videocutter.databinding.SelectVideoItemBinding
import com.mobile.videocutter.domain.model.LocalVideo
import loadImage

class SelectVideoAdapter : BaseAdapter() {
    companion object {
        const val SELECT_PAYLOAD = "SELECT_PAYLOAD"
    }

    var listener: IListener? = null
    private var selectedIndexList = mutableListOf<Int>()

    override fun getLayoutResource(viewType: Int) = R.layout.select_video_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*> {
        return VideoVH(binding as SelectVideoItemBinding)
    }

    fun updateSelect(path: String, isSelected: Boolean) {
        val index = dataList.indexOfFirst { (it as? VideoDisplay)?.video?.videoPath == path }
        if (index != -1) {
            (dataList[index] as? VideoDisplay)?.isSelected = isSelected
            notifyItemChanged(index, SELECT_PAYLOAD)
        }
    }

    inner class VideoVH(private val itemBinding: SelectVideoItemBinding) : BaseVH<VideoDisplay>(itemBinding) {
        init {
            itemBinding.root.setOnClickListener {
                val item = getDataAtPosition(adapterPosition) as? VideoDisplay
                if (item != null) {
                    item.isSelected = !item.isSelected
                    updateSelect(item.isSelected)
                    listener?.onVideoClick(item)
                }
            }
            itemBinding.root.setOnLongClickListener {
                val item = getDataAtPosition(adapterPosition) as? VideoDisplay
                if (item != null) {
                    item.video.videoPath?.let { path ->
                        listener?.onVideoLongClick(path)
                    }
                }
                true
            }
        }

        override fun onBind(data: VideoDisplay) {
            itemBinding.tvSelectVideoItmDuration.text = data.video.getFormattedDuration()
            itemBinding.ivSelectVideoItmImage.loadImage(data.video.videoPath)
            itemBinding.ivSelectVideoItmSelected.show()
            itemBinding.ivSelectVideoItmSelected.setImageResource(R.drawable.ic_unselect)
            updateSelect(data.isSelected)
        }

        override fun onBind(data: VideoDisplay, payloads: List<Any>) {
            if (payloads.contains(SELECT_PAYLOAD)) {
                updateSelect(data.isSelected)
            }
        }

        private fun updateSelect(isSelected: Boolean) {
            if (isSelected) {
                itemBinding.ivSelectVideoItmSelected.setImageResource(R.drawable.ic_select)
                itemBinding.mcvSelectVideoItmRoot.strokeWidth = getAppDimension(R.dimen.dimen_2).toInt()
            } else {
                itemBinding.ivSelectVideoItmSelected.setImageResource(R.drawable.ic_unselect)
                itemBinding.mcvSelectVideoItmRoot.strokeWidth = 0
            }
        }
    }

    class VideoDisplay(var video: LocalVideo, var isSelected: Boolean = false)

    interface IListener {
        fun onVideoClick(videoDisplay: VideoDisplay)
        fun onVideoLongClick(path: String)
    }
}
