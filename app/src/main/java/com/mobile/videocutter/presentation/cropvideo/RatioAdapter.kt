package com.mobile.videocutter.presentation.cropvideo

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.adapter.BaseAdapter
import com.mobile.videocutter.base.common.adapter.BaseDiffUtilCallback
import com.mobile.videocutter.base.common.adapter.BaseVH
import com.mobile.videocutter.base.extension.getAppColor
import com.mobile.videocutter.base.extension.getAppDrawable
import com.mobile.videocutter.base.extension.getAppString
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.databinding.RatioItemBinding

class RatioAdapter : BaseAdapter() {

    companion object {
        private const val CHANGE_STATE_RATIO_TYPE = "CHANGE_STATE_RATIO_TYPE"
    }

    init {
        Log.d("CHANGE_STATE_RATIO_TYPE", ": ${dataList.size}")
    }



    var listener: IListener? = null

    override fun getLayoutResource(viewType: Int) = R.layout.ratio_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return RatioVH(binding)
    }

    override fun getDiffUtil(oldList: List<Any>, newList: List<Any>): DiffUtil.Callback {
        return DiffCallback(oldList as List<RatioDisplay>, newList as List<RatioDisplay>)
    }

    inner class RatioVH(private val binding: ViewDataBinding) : BaseVH<RatioDisplay>(binding) {

        private var oldData: RatioDisplay? = null
        private val viewBinding = binding as RatioItemBinding
        private var oldDataPosition = -1

        init {
            oldData = getDataAtPosition(0) as RatioDisplay

            viewBinding.root.setOnSafeClick {
                oldDataPosition = (dataList as? MutableList<RatioDisplay>)?.indexOfFirst {
                    it == oldData
                } ?: -1
                Log.d("tunglv", "oldData: ${oldData.hashCode()}")
                val item = getDataAtPosition(bindingAdapterPosition) as? RatioDisplay
                if (item != null) {

                    item.isSelect = true
                    notifyItemChanged(bindingAdapterPosition, CHANGE_STATE_RATIO_TYPE)

                    if (oldData != null && oldDataPosition != -1) {
                        oldData?.isSelect = false
                        notifyItemChanged(oldDataPosition, CHANGE_STATE_RATIO_TYPE)
                    }
                    oldData = item
                    Log.d("tunglv", ": item ${item}")
                    Log.d("tunglv", ": oldDataoldData ${oldData.hashCode()}")
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

        override fun onBind(data: RatioDisplay, payloads: List<Any>) {
            super.onBind(data, payloads)

            payloads.forEach {
                when (it) {
                    CHANGE_STATE_RATIO_TYPE -> {
                        viewBinding.ivRatioItm.setImageDrawable(data.getIcon())
                        if (data.isSelect) {
                            viewBinding.tvRatioItm.setTextColor(getAppColor(R.color.color_purple))
                        } else {
                            viewBinding.tvRatioItm.setTextColor(getAppColor(R.color.color_gray_text))
                        }
                    }
                }
            }
        }
    }

    class DiffCallback(oldData: List<RatioDisplay>, newData: List<RatioDisplay>) :
        BaseDiffUtilCallback<RatioDisplay>(oldData, newData) {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldData = (getOldItem(oldItemPosition) as? RatioDisplay)
            val newData = (getNewItem(newItemPosition) as? RatioDisplay)

            return true
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldData = (getOldItem(oldItemPosition) as? RatioDisplay)
            val newData = (getNewItem(newItemPosition) as? RatioDisplay)

            return oldData?.isSelect == newData?.isSelect
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            val oldData = (getOldItem(oldItemPosition) as? RatioDisplay)
            val newData = (getNewItem(newItemPosition) as? RatioDisplay)

            return if (oldData?.isSelect != newData?.isSelect) {
                CHANGE_STATE_RATIO_TYPE
            } else {
                null
            }
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
