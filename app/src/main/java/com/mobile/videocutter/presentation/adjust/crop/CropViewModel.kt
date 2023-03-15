package com.mobile.videocutter.presentation.adjust.crop

import com.mobile.videocutter.base.common.BaseViewModel
import com.mobile.videocutter.domain.model.CROP_RATIO
import com.mobile.videocutter.domain.model.CropRatio

class CropViewModel: BaseViewModel(){
    var path: String? = null

    fun getRatioList(): List<CropRatioAdapter.CropRatioDisplay> {
        val list = mutableListOf<CROP_RATIO>()
        list.add(CROP_RATIO.CUSTOM)
        list.add(CROP_RATIO.INSTAGRAM)
        list.add(CROP_RATIO.R4X5)
        list.add(CROP_RATIO.R5X4)
        list.add(CROP_RATIO.R9X16)
        list.add(CROP_RATIO.R16X9)
        list.add(CROP_RATIO.R3X2)
        list.add(CROP_RATIO.R2X3)
        list.add(CROP_RATIO.R4X3)
        list.add(CROP_RATIO.R3X4)
        return list.map { CropRatioAdapter.CropRatioDisplay(CropRatio(it)) }.apply {
            first().isSelected = true
        }
    }
}
