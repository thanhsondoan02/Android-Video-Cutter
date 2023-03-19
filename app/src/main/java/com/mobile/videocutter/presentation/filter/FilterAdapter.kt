package com.mobile.videocutter.presentation.filter

import androidx.databinding.ViewDataBinding
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.adapter.BaseAdapter
import com.mobile.videocutter.base.common.adapter.BaseVH
import com.mobile.videocutter.base.extension.*
import com.mobile.videocutter.databinding.MyFilterItemBinding
import com.mobile.videocutter.domain.model.FILTER_TYPE
import com.mobile.videocutter.domain.model.Filter

class FilterAdapter : BaseAdapter() {
    var listener: IListener? = null

    companion object {
        private const val CHANGE_STATE_FILTER_PAYLOAD = "CHANGE_STATE_FILTER_PAYLOAD"
    }

    override fun getLayoutResource(viewType: Int) = R.layout.my_filter_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return FilterVH(binding)
    }

    fun getSelectFilter(): Filter {
        return (dataList.firstOrNull { (it as FilterDisplay).isSelect } as? FilterDisplay)?.filter
            ?: Filter(FILTER_TYPE.ORIGINAL)
    }

    private fun select(newPosition: Int) {
        val oldDataPosition = dataList.indexOfFirst {
            (it as FilterDisplay).isSelect
        }
        if (newPosition != oldDataPosition) {
            (dataList[newPosition] as FilterDisplay).isSelect = true
            notifyItemChanged(newPosition, CHANGE_STATE_FILTER_PAYLOAD)
            (dataList[oldDataPosition] as FilterDisplay).isSelect = false
            notifyItemChanged(oldDataPosition, CHANGE_STATE_FILTER_PAYLOAD)
        }
    }

    inner class FilterVH(private val binding: ViewDataBinding) : BaseVH<FilterDisplay>(binding) {
        private val viewBinding = binding as MyFilterItemBinding

        init {
            viewBinding.root.setOnSafeClick {
                val item = getDataAtPosition(bindingAdapterPosition) as? FilterDisplay
                if (item != null) {
                    select(bindingAdapterPosition)
                    listener?.onFilterClick(item.filter)
                }
            }
        }

        override fun onBind(data: FilterDisplay) {
            super.onBind(data)
            viewBinding.ivMyFilterItmPreview.setImageResource(R.drawable.img_filter_original)
            viewBinding.vMyFilterItmFilter.background = data.filter.getFilterDrawable()
            viewBinding.tvMyFilterItmTitle.text = data.filter.getFilterTitle()
            setStateData(data)
        }

        override fun onBind(data: FilterDisplay, payloads: List<Any>) {
            super.onBind(data, payloads)
            payloads.forEach {
                when (it) {
                    CHANGE_STATE_FILTER_PAYLOAD -> {
                        setStateData(data)
                    }
                }
            }
        }

        private fun setStateData(data: FilterDisplay) {
            if (data.isSelect) {
                if (data.filter.type == FILTER_TYPE.ORIGINAL) {
                    viewBinding.ivMyFilterItmNope.show()
                }
                viewBinding.vMyFilterItmTransparent.show()
                viewBinding.tvMyFilterItmTitle.setTextColor(getAppColor(R.color.white))
                viewBinding.tvMyFilterItmTitle.background = getAppDrawable(R.drawable.shape_purple_bg_corner_bottom_4)
            } else {
                viewBinding.tvMyFilterItmTitle.background = null
                viewBinding.ivMyFilterItmNope.gone()
                viewBinding.tvMyFilterItmTitle.setTextColor(getAppColor(R.color.color_black_54))
                viewBinding.vMyFilterItmTransparent.gone()
            }
        }
    }

    class FilterDisplay(val filter: Filter, var isSelect: Boolean = false)

    interface IListener {
        fun onFilterClick(filter: Filter)
    }
}
