package com.mobile.videocutter.presentation.addmusic

import androidx.databinding.ViewDataBinding
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.adapter.BaseAdapter
import com.mobile.videocutter.base.common.adapter.BaseVH
import com.mobile.videocutter.databinding.MusicTrackItemBinding
import com.mobile.videocutter.domain.model.MusicTrack

class MusicTrackAdapter : BaseAdapter() {
    companion object {
        const val SELECT_MUSIC_PAYLOAD = "SELECT_MUSIC_PAYLOAD"
    }

    var listener: IListener? = null
//    var selectIndexList = mutableListOf<Int>()
//    var state = STATE_MUSIC_TRACK.NORMAL
//        set(value) {
//            field = value
//            if (value == STATE_MUSIC_TRACK.NORMAL) {
//                selectIndexList.forEach {
//                    (getDataAtPosition(it) as? MusicTrackDisplay)?.isSelected = false
//                    notifyItemChanged(it, SELECT_MUSIC_PAYLOAD)
//                }
//                selectIndexList.clear()
//            }
//        }

    override fun getLayoutResource(viewType: Int) = R.layout.music_track_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*> {
        return MusicTrackVH(binding as MusicTrackItemBinding)
    }

    inner class MusicTrackVH(private val binding: MusicTrackItemBinding) : BaseVH<MusicTrackDisplay>(binding) {
//        init {
//            binding.root.setOnClickListener {
////                val musicTrackDisplay = (getDataAtPosition(adapterPosition) as? MusicTrackDisplay)
////                if (state == STATE_MUSIC_TRACK.SELECT) {
////                    (getDataAtPosition(adapterPosition) as? MusicTrackDisplay)?.isSelected = musicTrackDisplay?.isSelected?.not()
////                        ?: false
//////                    updateSelect(musicTrackDisplay?.isSelected == true)
////                    if (musicTrackDisplay?.isSelected == true) {
////                        selectIndexList.add(adapterPosition)
////                    } else {
////                        selectIndexList.remove(absoluteAdapterPosition)
////                    }
////                }
////                listener?.onMusicTrackClick(musicTrackDisplay?.musicTrack, state, selectIndexList.size)
////            }
//        }

        override fun onBind(data: MusicTrackDisplay) {
            binding.tvMusicTrackItmLeftText.text = data.musicTrack.getFormatTime()
            binding.tvMusicTrackItmCenterText.text = data.musicTrack.musicTrackName
//            updateSelect(data.isSelected)
        }

//        override fun onBind(data: MusicTrackDisplay, payloads: List<Any>) {
//            if(payloads.contains(SELECT_MUSIC_PAYLOAD)){
//                u
//            }
//        }

//        fun updateSelect(isSelected: Boolean){
//            if(state == STATE_MUSIC_TRACK.SELECT && isSelected){
//                binding.
//            }
//        }
    }

    class MusicTrackDisplay(
        var musicTrack: MusicTrack,
        var isSelected: Boolean = false
    )

    enum class STATE_MUSIC_TRACK {
        SELECT,
        NORMAL,
    }

    interface IListener {
        fun onMusicTrackClick(musicTrack: MusicTrack?, state: STATE_MUSIC_TRACK, size: Int)
    }
}

