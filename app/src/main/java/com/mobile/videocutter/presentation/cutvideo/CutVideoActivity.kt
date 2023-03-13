package com.mobile.videocutter.presentation.cutvideo

import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.lifecycle.lifecycleScope
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.base.extension.getAppDimension
import com.mobile.videocutter.base.extension.getAppDrawable
import com.mobile.videocutter.databinding.CutVideoActivityBinding
import com.mobile.videocutter.domain.model.LocalVideo
import com.mobile.videocutter.presentation.widget.cutvideo.CutVideoView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CutVideoActivity : BaseBindingActivity<CutVideoActivityBinding>(R.layout.cut_video_activity) {

    override fun onInitView() {
        super.onInitView()

        initView()

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
            }
        }

        binding.sbvCutVideoSelect.clickLeft {

        }

        binding.sbvCutVideoSelect.clickRight {

        }

    }

    private fun initView() {
        binding.hvCutVideoBottom.apply {
            setTextViewRightPadding(
                getAppDimension(R.dimen.dimen_14),
                getAppDimension(R.dimen.dimen_4),
                getAppDimension(R.dimen.dimen_14),
                getAppDimension(R.dimen.dimen_4)
            )

            setTextViewRightMargin(
                getAppDimension(R.dimen.dimen_10),
                getAppDimension(R.dimen.dimen_4),
                getAppDimension(R.dimen.dimen_10),
                getAppDimension(R.dimen.dimen_4)
            )

            getAppDrawable(R.drawable.shape_purple_bg_corner_6)?.let { setBackgroundTextViewRight(it) }
        }
    }

}
