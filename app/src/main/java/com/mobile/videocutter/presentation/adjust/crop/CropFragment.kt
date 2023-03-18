package com.mobile.videocutter.presentation.adjust.crop

import android.annotation.SuppressLint
import android.util.DisplayMetrics
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingFragment
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.CropFragmentBinding
import com.mobile.videocutter.di.DisplayFactory
import com.mobile.videocutter.domain.model.CropRatio
import com.mobile.videocutter.presentation.tasselsvideo.TasselsVideoViewModel

class CropFragment : BaseBindingFragment<CropFragmentBinding>(R.layout.crop_fragment) {
    private val cropAdapter by lazy {
        CropRatioAdapter()
    }
    private val disPlay by lazy {
        DisplayFactory.getCropDisplay()
    }
    private val viewModel by activityViewModels<TasselsVideoViewModel>()

    override fun onInitView() {
        super.onInitView()
        initRecyclerView()
        initOnClick()
        calculateCropViewWidthAndHeight()
        initResolution()
    }

    private fun initRecyclerView() {
        cropAdapter.listener = object : CropRatioAdapter.IListener {
            override fun onCropRatioSelected(cropRatio: CropRatio) {
                binding.cvCrop.ratio = cropRatio.getRatio()
                updateResolution(binding.cvCrop.getWidthRatio(), binding.cvCrop.getHeightRatio())
            }
        }
        binding.rvCropRatioList.adapter = cropAdapter
        binding.rvCropRatioList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        cropAdapter.submitList(disPlay.getRatioList())
    }

    private fun initOnClick() {
        binding.vCropBackground.setOnSafeClick {
            // do nothing
        }
        binding.ivCropClose.setOnSafeClick {
            backFragment()
        }
        binding.ivCropDone.setOnSafeClick {
//            playerFragment?.saveRotateState()
            backFragment()
        }
    }

    private fun calculateCropViewWidthAndHeight() {
        if (viewModel.ratio == Float.MAX_VALUE) {
            viewModel.calculateVideoRatio()
        }
        if (viewModel.ratio != Float.MAX_VALUE) {
            val displayMetrics = DisplayMetrics()
            baseActivity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            var videoWidth = displayMetrics.widthPixels
            var videoHeight = displayMetrics.widthPixels
            if (viewModel.ratio > 1) {
                videoHeight = (videoWidth / viewModel.ratio).toInt()
            } else {
                videoWidth = (videoHeight * viewModel.ratio).toInt()
            }
            binding.cvCrop.layoutParams.width = videoWidth
            binding.cvCrop.layoutParams.height = videoHeight
        }
    }

    private fun initResolution() {
        updateResolution(1f, 1f)

        // Cập nhật resolution khi crop size change
        binding.cvCrop.setOnCropSizeChangeListener { widthRatio, heightRatio ->
            updateResolution(widthRatio, heightRatio)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateResolution(widthRatio: Float, heightRatio: Float) {
        binding.tvCropResolution.text =
            "${(widthRatio * viewModel.resolutionWidth).toInt()} x ${(heightRatio * viewModel.resolutionHeight).toInt()}"
    }
}
