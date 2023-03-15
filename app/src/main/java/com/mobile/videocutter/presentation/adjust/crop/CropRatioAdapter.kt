package com.mobile.videocutter.presentation.adjust.crop

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.databinding.ViewDataBinding
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.adapter.BaseAdapter
import com.mobile.videocutter.base.common.adapter.BaseVH
import com.mobile.videocutter.base.extension.getAppColor
import com.mobile.videocutter.base.extension.getAppDimension
import com.mobile.videocutter.base.extension.getAppDrawable
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.CropRatioItemBinding
import com.mobile.videocutter.domain.model.CropRatio

class CropRatioAdapter: BaseAdapter() {
    companion object {
        const val SELECT_PAYLOAD = "SELECT_PAYLOAD"
    }

    override fun getLayoutResource(viewType: Int): Int = R.layout.crop_ratio_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*> {
        return CropRatioVH(binding as CropRatioItemBinding)
    }

    private fun unSelectOld() {
        val indexOfOldSelect = dataList.indexOfFirst { (it as? CropRatioDisplay)?.isSelected == true }
        (dataList[indexOfOldSelect] as? CropRatioDisplay)?.isSelected = false
        notifyItemChanged(indexOfOldSelect, SELECT_PAYLOAD)
    }

    inner class CropRatioVH(private val itemBinding: CropRatioItemBinding) : BaseVH<CropRatioDisplay>(itemBinding) {
        init {
            itemBinding.root.setOnSafeClick {
                unSelectOld()
                (getDataAtPosition(adapterPosition) as? CropRatioDisplay)?.let {
                    it.isSelected = true
                    updateSelect(it)
                }
            }
        }

        override fun onBind(data: CropRatioDisplay) {
            itemBinding.tvCropRatioTitle.text = data.cropRatio.getTitle()
            updateSelect(data)
        }

        override fun onBind(data: CropRatioDisplay, payloads: List<Any>) {
            if (payloads.contains(SELECT_PAYLOAD)) {
                updateSelect(data)
            }
        }

        private fun updateSelect(data: CropRatioDisplay) {
            itemBinding.ivCropRatioPreview.setImageDrawable(getPreviewDrawable(data))
            itemBinding.tvCropRatioTitle.setTextColor(getAppColor(getSelectColor(data.isSelected)))
        }

        private fun getPreviewDrawable(data: CropRatioDisplay): Drawable? {
            return if (data.cropRatio.getRatio() == null) {
                getAppDrawable(getCustomImage(data.isSelected))
            } else {
                val drawable = GradientDrawable()
                drawable.shape = GradientDrawable.RECTANGLE
                drawable.setStroke(
                    getAppDimension(R.dimen.dimen_2).toInt(),
                    getAppColor(getSelectColor(data.isSelected))
                )
                drawable
            }
        }

        private fun getCustomImage(isSelected: Boolean): Int {
            return if (isSelected) {
                R.drawable.ic_crop_ratio_custom
            } else {
                R.drawable.ic_crop_ratio_custom_unselected
            }
        }

        private fun getSelectColor(isSelected: Boolean): Int {
            return if (isSelected) {
                R.color.color_purple
            } else {
                R.color.color_gray_crop_text
            }
        }
    }

    class CropRatioDisplay(val cropRatio: CropRatio, var isSelected: Boolean = false)
}
