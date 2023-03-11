package com.mobile.videocutter.presentation.cutvideo

import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.databinding.CutVideoActivityBinding
import com.mobile.videocutter.domain.model.LocalVideo
import kotlin.math.log

class CutVideoActivity : BaseBindingActivity<CutVideoActivityBinding>(R.layout.cut_video_activity) {

    override fun onInitView() {
        super.onInitView()

        val uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.say_i_do)
        binding.vvCutVideoRoot.setVideoURI(uri)
        val metaRetriever = MediaMetadataRetriever()
        metaRetriever.setDataSource(this, uri)
        val duration = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        duration?.let { binding.cvvCutVideo.apply { setTotalTimeVideo(it.toLong()) } }
        val time = binding.cvvCutVideo.getTimeCenter()
        binding.vvCutVideoRoot.start()
        binding.vvCutVideoRoot.seekTo(time.toInt())

    }

}
