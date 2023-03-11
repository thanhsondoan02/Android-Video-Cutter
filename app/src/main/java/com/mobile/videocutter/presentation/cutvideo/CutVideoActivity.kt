package com.mobile.videocutter.presentation.cutvideo

import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.databinding.CutVideoActivityBinding
import com.mobile.videocutter.domain.model.LocalVideo
import com.mobile.videocutter.presentation.widget.cutvideo.CutVideoView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CutVideoActivity : BaseBindingActivity<CutVideoActivityBinding>(R.layout.cut_video_activity) {

    var time: Long = 0L

    override fun onInitView() {
        super.onInitView()

        val uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.say_i_do)
        binding.vvCutVideoRoot.setVideoURI(uri)
        val metaRetriever = MediaMetadataRetriever()
        metaRetriever.setDataSource(this, uri)
        val duration = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        binding.vvCutVideoRoot.seekTo(500)
        duration?.let {
            binding.cvvCutVideo.apply {
                setConfigVideoBegin(it.toLong())
                listener = object : CutVideoView.IListener {
                    override fun onTimeStart(timeStart: Int) {
                        binding.vvCutVideoRoot.seekTo(timeStart)
                    }

                    override fun onTimeCenter(timeCenter: Int) {
                        binding.vvCutVideoRoot.seekTo(timeCenter)
                    }

                    override fun onTimeEnd(timeEnd: Int) {
                        binding.vvCutVideoRoot.seekTo(timeEnd)
                    }
                }
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {
            LocalVideo().apply {
                val uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.say_i_do)
                val bitmapList =
                    duration?.let {
                        getBitmapListFromVideo(
                            this@CutVideoActivity,
                            uri,
                            binding.cvvCutVideo.getHeightListImage(),
                            binding.cvvCutVideo.getWidthListImage(),
                            it.toLong())
                    }
                bitmapList?.let { binding.cvvCutVideo.setBitmapListDisplay(it) }
//                binding.ivA.setImageBitmap(bitmapList?.get(bitmapList.size-1))
            }
        }

    }

}
