package com.mobile.videocutter.presentation.select.preview

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.util.DisplayMetrics
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingFragment
import com.mobile.videocutter.base.extension.getAppDimension
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.PreviewImageFragmentBinding
import loadImage
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

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
        val videoWidth = getVideoWidthOrHeight(File(path), isWidth = true)
        val videoHeight = getVideoWidthOrHeight(File(path), isWidth = false)
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
                height = screenHeight - getAppDimension(R.dimen.dimen_18).toInt()
                width = (height / videoRatio).toInt()
            } else {
                width = screenWidth - getAppDimension(R.dimen.dimen_24).toInt()
                height = (width * videoRatio).toInt()
            }
        }
        binding.ivPreviewThumbnail.layoutParams = layoutParams

        // load thumbnail
        binding.ivPreviewThumbnail.loadImage(path)
    }

    private fun getVideoWidthOrHeight(file: File, isWidth: Boolean): Int? {
        var retriever: MediaMetadataRetriever? = null
        var bmp: Bitmap? = null
        var inputStream: FileInputStream? = null
        var mWidthHeight: Int? = 0
        try {
            retriever = MediaMetadataRetriever()
            inputStream = FileInputStream(file.absolutePath)
            retriever.setDataSource(inputStream.fd)
            bmp = retriever.frameAtTime
            mWidthHeight = if (isWidth){
                bmp?.width
            }else {
                bmp?.height
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace();
        } catch (e: IOException) {
            e.printStackTrace();
        } catch (e: RuntimeException) {
            e.printStackTrace();
        } finally{
            retriever?.release()
            inputStream?.close()
        }
        return mWidthHeight
    }
}
