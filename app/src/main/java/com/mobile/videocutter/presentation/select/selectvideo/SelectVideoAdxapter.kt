package com.mobile.videocutter.presentation.select.selectvideo

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.adapter.BaseDiffUtilCallback
import com.mobile.videocutter.base.common.adapter.BaseGridAdapter
import com.mobile.videocutter.base.common.adapter.BaseVH
import com.mobile.videocutter.base.extension.getAppDimension
import com.mobile.videocutter.base.extension.show
import com.mobile.videocutter.databinding.MyStudioVideoItemBinding
import com.mobile.videocutter.domain.model.VideoDisplay
import loadImage

class SelectVideoAdxapter : BaseGridAdapter() {
    companion object {
        const val SELECT_PAYLOAD = "SELECT_PAYLOAD"
    }

    var listener: IListener? = null

    override fun getItemCountInRow(viewType: Int) = 4

    override fun getLayoutResource(viewType: Int) = R.layout.my_studio_video_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*> {
        return VideoVH(binding as MyStudioVideoItemBinding)
    }

    override fun getDiffUtil(oldList: List<Any>, newList: List<Any>): DiffUtil.Callback {
        return DiffCallback(oldList as List<VideoDisplay>, newList as List<VideoDisplay>)
    }

    inner class VideoVH(private val itemBinding: MyStudioVideoItemBinding) : BaseVH<VideoDisplay>(itemBinding) {
        init {
            itemBinding.root.setOnClickListener {
                val item = getDataAtPosition(bindingAdapterPosition) as? VideoDisplay
                if (item != null) {
                    item.isSelected = !item.isSelected
                    listener?.onVideoClick(item)
                }
            }
            itemBinding.root.setOnLongClickListener {
                val item = getDataAtPosition(bindingAdapterPosition) as? VideoDisplay
                if (item != null) {
                    item.video.videoPath?.let { path ->
                        listener?.onVideoLongClick(path)
                    }
                }
                true
            }
        }

        override fun onBind(data: VideoDisplay) {
            itemBinding.tvMyStudioVideoItmDuration.text = data.video.getFormattedDuration()
            itemBinding.ivMyStudioVideoItmImage.loadImage(data.video.videoPath)
            itemBinding.ivMyStudioVideoItmSelected.show()
            itemBinding.ivMyStudioVideoItmSelected.setImageResource(R.drawable.ic_unselect)
            updateSelect(data.isSelected)
        }

        override fun onBind(data: VideoDisplay, payloads: List<Any>) {
            payloads.forEach {
                when (it) {
                    SELECT_PAYLOAD -> {
                        updateSelect(data.isSelected)
                    }
                }
            }
        }

        private fun updateSelect(isSelected: Boolean) {
            if (isSelected) {
                itemBinding.ivMyStudioVideoItmSelected.setImageResource(R.drawable.ic_select)
                itemBinding.mcvMyStudioVideoItmRoot.strokeWidth = getAppDimension(R.dimen.dimen_2).toInt()
            } else {
                itemBinding.ivMyStudioVideoItmSelected.setImageResource(R.drawable.ic_unselect)
                itemBinding.mcvMyStudioVideoItmRoot.strokeWidth = 0
            }
        }
    }

    class DiffCallback(oldData: List<VideoDisplay>, newData: List<VideoDisplay>) :
        BaseDiffUtilCallback<VideoDisplay>(oldData, newData) {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldData = (getOldItem(oldItemPosition) as? VideoDisplay)
            val newData = (getNewItem(newItemPosition) as? VideoDisplay)

            return oldData?.video == newData?.video
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldData = (getOldItem(oldItemPosition) as? VideoDisplay)
            val newData = (getNewItem(newItemPosition) as? VideoDisplay)

            return oldData?.hashCode() == newData?.hashCode()
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {

            val oldData = (getOldItem(oldItemPosition) as? VideoDisplay)
            val newData = (getNewItem(newItemPosition) as? VideoDisplay)

            return if (oldData?.hashCode() != newData?.hashCode()) {
                SELECT_PAYLOAD
            } else {
                null
            }
        }
    }

    interface IListener {
        fun onVideoClick(videoDisplay: VideoDisplay)
        fun onVideoLongClick(path: String)
    }
}
