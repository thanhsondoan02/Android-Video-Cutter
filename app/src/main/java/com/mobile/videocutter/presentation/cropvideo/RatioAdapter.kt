package com.mobile.videocutter.presentation.cropvideo

import android.graphics.drawable.Drawable
import androidx.databinding.ViewDataBinding
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.adapter.BaseAdapter
import com.mobile.videocutter.base.common.adapter.BaseVH
import com.mobile.videocutter.base.extension.getAppColor
import com.mobile.videocutter.base.extension.getAppDrawable
import com.mobile.videocutter.base.extension.getAppString
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.RatioItemBinding

class RatioAdapter : BaseAdapter() {

    var listener: IListener? = null

    override fun getLayoutResource(viewType: Int) = R.layout.ratio_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return RatioVH(binding)
    }

    inner class RatioVH(private val binding: ViewDataBinding) : BaseVH<RatioDisplay>(binding) {

        private val viewBinding = binding as RatioItemBinding
        private var oldData: RatioDisplay? = null

        init {
            oldData = getDataAtPosition(0) as RatioDisplay
            viewBinding.root.setOnSafeClick {
                val item = getDataAtPosition(bindingAdapterPosition) as? RatioDisplay
                if (item != null) {
                    item.isSelect = true
                    listener?.onUpdateNewData(item)
                    oldData?.isSelect = false
                    if (oldData != null) {
                        listener?.onUpdateOldData(oldData!!)
                    }
                    oldData = item
                }
            }
        }

        override fun onBind(data: RatioDisplay) {
            super.onBind(data)
            viewBinding.ivRatioItm.setImageDrawable(data.getIcon())
            if (data.isSelect) {
                viewBinding.tvRatioItm.setTextColor(getAppColor(R.color.color_purple))
            } else {
                viewBinding.tvRatioItm.setTextColor(getAppColor(R.color.color_gray_text))
            }
            viewBinding.tvRatioItm.text = data.getTitle()
        }
    }

    class RatioDisplay(var type: RATIO_TYPE, var isSelect: Boolean) {

        fun getTitle(): String {
            return when (this.type) {
                RATIO_TYPE.CUSTOM -> getAppString(R.string.custom)
                RATIO_TYPE.INSTAGRAM -> getAppString(R.string.instagram)
                RATIO_TYPE._4_5 -> getAppString(R.string._4x5)
                RATIO_TYPE._5_4 -> getAppString(R.string._5x4)
                RATIO_TYPE._9_16 -> getAppString(R.string._9x16)
                RATIO_TYPE._16_9 -> getAppString(R.string._16x9)
                RATIO_TYPE._3_2 -> getAppString(R.string._3x2)
                RATIO_TYPE._2_3 -> getAppString(R.string._2x3)
                RATIO_TYPE._4_3 -> getAppString(R.string._4x3)
                RATIO_TYPE._3_4 -> getAppString(R.string._3x4)
            }
        }

        fun getIcon(): Drawable? {
            if (!isSelect) {
                return when (this.type) {
                    RATIO_TYPE.CUSTOM -> getAppDrawable(R.drawable.ic_gray_custom)
                    RATIO_TYPE.INSTAGRAM -> getAppDrawable(R.drawable.ic_gray_instagram)
                    RATIO_TYPE._4_5 -> getAppDrawable(R.drawable.ic_gray_4_5)
                    RATIO_TYPE._5_4 -> getAppDrawable(R.drawable.ic_gray_5_4)
                    RATIO_TYPE._9_16 -> getAppDrawable(R.drawable.ic_gray_9_16)
                    RATIO_TYPE._16_9 -> getAppDrawable(R.drawable.ic_gray_16_9)
                    RATIO_TYPE._3_2 -> getAppDrawable(R.drawable.ic_gray_3_2)
                    RATIO_TYPE._2_3 -> getAppDrawable(R.drawable.ic_gray_2_3)
                    RATIO_TYPE._4_3 -> getAppDrawable(R.drawable.ic_gray_4_3)
                    RATIO_TYPE._3_4 -> getAppDrawable(R.drawable.ic_gray_3_4)
                }
            } else {
                return when (this.type) {
                    RATIO_TYPE.CUSTOM -> getAppDrawable(R.drawable.ic_perple_custom)
                    RATIO_TYPE.INSTAGRAM -> getAppDrawable(R.drawable.ic_perple_instagram)
                    RATIO_TYPE._4_5 -> getAppDrawable(R.drawable.ic_perple_4_5)
                    RATIO_TYPE._5_4 -> getAppDrawable(R.drawable.ic_perple_5_4)
                    RATIO_TYPE._9_16 -> getAppDrawable(R.drawable.ic_perple_9_16)
                    RATIO_TYPE._16_9 -> getAppDrawable(R.drawable.ic_perple_16_9)
                    RATIO_TYPE._3_2 -> getAppDrawable(R.drawable.ic_perple_3_2)
                    RATIO_TYPE._2_3 -> getAppDrawable(R.drawable.ic_perple_2_3)
                    RATIO_TYPE._4_3 -> getAppDrawable(R.drawable.ic_perple_4_3)
                    RATIO_TYPE._3_4 -> getAppDrawable(R.drawable.ic_perple_3_4)
                }
            }
        }
    }

    enum class RATIO_TYPE {
        CUSTOM,
        INSTAGRAM,
        _4_5,
        _5_4,
        _9_16,
        _16_9,
        _3_2,
        _2_3,
        _4_3,
        _3_4
    }

    interface IListener {
        fun onUpdateNewData(ratioDisplay: RatioDisplay)
        fun onUpdateOldData(ratioDisplay: RatioDisplay)
    }
}
