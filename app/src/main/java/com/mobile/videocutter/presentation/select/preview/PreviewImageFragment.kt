package com.mobile.videocutter.presentation.select.preview

import android.util.DisplayMetrics
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingFragment
import com.mobile.videocutter.base.extension.getAppDimension
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.PreviewImageFragmentBinding
import getVideoWidthOrHeight
import loadImage

class PreviewImageFragment: BaseBindingFragment<PreviewImageFragmentBinding>(R.layout.preview_image_fragment) {
    companion object {
        const val VIDEO_PATH = "VIDEO_PATH"
    }

    override fun onInitView() {
        super.onInitView()
        binding.flPreviewImageContainer.setOnSafeClick {
            backFragment()
        }
        binding.ivPreviewThumbnail.setOnSafeClick {
            // do nothing
        }
        calculateWidthHeightAndLoadVideoThumbnail(requireArguments().getString(VIDEO_PATH))
    }

    private fun calculateWidthHeightAndLoadVideoThumbnail(path: String?) {
        if (path == null)  return

        // get video width and height
        val videoWidth = getVideoWidthOrHeight(path, isWidth = true)
        val videoHeight = getVideoWidthOrHeight(path, isWidth = false)
        if (videoWidth == null || videoHeight == null) return

        // get screen width and height
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        // calculate image view width and height
        val videoRatio = videoHeight.toFloat() / videoWidth
        val screenRatio = screenHeight.toFloat() / screenWidth
        val layoutParams = binding.ivPreviewThumbnail.layoutParams
        layoutParams.apply {
            if (videoRatio > screenRatio) {
                height = screenHeight - getAppDimension(R.dimen.dimen_100).toInt()
                width = (height / videoRatio).toInt()
            } else {
                width = screenWidth - getAppDimension(R.dimen.dimen_80).toInt()
                height = (width * videoRatio).toInt()
            }
        }
        binding.ivPreviewThumbnail.layoutParams = layoutParams

        // load thumbnail
        binding.ivPreviewThumbnail.loadImage(path)
    }
}
