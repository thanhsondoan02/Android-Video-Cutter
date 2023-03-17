package com.mobile.videocutter.presentation.addmusic

import androidx.databinding.ViewDataBinding
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.adapter.BaseAdapter
import com.mobile.videocutter.base.common.adapter.BaseVH
import com.mobile.videocutter.base.extension.*
import com.mobile.videocutter.databinding.MusicTrackItemBinding
import com.mobile.videocutter.domain.model.MusicTrack

class MusicTrackAdapter : BaseAdapter() {
    companion object {
        const val SELECT_MUSIC_PAYLOAD = "SELECT_MUSIC_PAYLOAD"
    }

    var listener: OnClickItem? = null

    override fun getLayoutResource(viewType: Int) = R.layout.music_track_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*> {
        return MusicTrackVH(binding as MusicTrackItemBinding)
    }

    inner class MusicTrackVH(private val binding: MusicTrackItemBinding) : BaseVH<MusicTrackDisplay>(binding) {
        init {
            binding.root.setOnClickListener {
                for (i in dataList.indices) {
                    val musicTrackDisplay = dataList[i] as MusicTrackDisplay
                    if (musicTrackDisplay.isSelected) {
                        musicTrackDisplay.isSelected = false
                        notifyItemChanged(i, SELECT_MUSIC_PAYLOAD)
                    }
                }
                (dataList[adapterPosition] as? MusicTrackDisplay)?.let {
                    it.isSelected = true
                    notifyItemChanged(adapterPosition, SELECT_MUSIC_PAYLOAD)
                    listener?.onMusicTrackClick(it)
                }
            }
        }

        override fun onBind(data: MusicTrackDisplay) {
            if (adapterPosition == 0) {
                with(binding) {
                    ivMusicTrackItmIconNone.show()
                    ivMusicTrackItmIconNone.setImageDrawable(getAppDrawable(R.drawable.ic_gray_none))
                    tvMusicTrackItmDuration.gone()
                    tvMusicTrackItmName.text = getAppString(R.string.music_track_none)
                }
            } else {
                with(binding) {
                    ivMusicTrackItmIconNone.gone()
                    tvMusicTrackItmName.show()
                    tvMusicTrackItmDuration.show()
                    tvMusicTrackItmDuration.text = data.musicTrack.getFormatTime()
                    tvMusicTrackItmName.text = data.musicTrack.musicTrackName
                }
            }
            updateSelected(data.isSelected)
        }

        override fun onBind(data: MusicTrackDisplay, payloads: List<Any>) {
            if (payloads.contains(SELECT_MUSIC_PAYLOAD)) {
                updateSelected(data.isSelected)
            }
        }

        private fun unSelectOld() {
            val indexOfOldSelect = dataList.indexOfFirst {
                (it as? MusicTrackDisplay)?.isSelected == true
            }
            if(indexOfOldSelect!=-1){
                (dataList[indexOfOldSelect] as? MusicTrackDisplay)?.isSelected = false
                notifyItemChanged(indexOfOldSelect, SELECT_MUSIC_PAYLOAD)
            }
        }

        private fun updateSelected(isSelected: Boolean) {
            with(binding) {
                if (isSelected) {
                    ivMusicTrackItmSelected.show()
                } else {
                    ivMusicTrackItmSelected.hide()
                }
            }
        }
    }

    class MusicTrackDisplay(
        var musicTrack: MusicTrack,
        var isSelected: Boolean = false
    )

    interface OnClickItem {
        fun onMusicTrackClick(musicTrackDisplay: MusicTrackDisplay)
    }
}
