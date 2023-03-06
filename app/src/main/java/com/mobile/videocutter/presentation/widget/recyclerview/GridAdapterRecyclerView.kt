package com.mobile.videocutter.presentation.widget.recyclerview

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.IntRange
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.videocutter.base.common.adapter.BaseGridAdapter

class GridAdapterRecyclerView constructor(
    ctx: Context,
    attrs: AttributeSet?
) : RecyclerView(ctx, attrs) {

    private var maxItemHorizontal: Int = 2

    init {
        this.layoutManager = getGridLayoutManager()
        this.overScrollMode = OVER_SCROLL_NEVER
    }

    /**
     * Need to call before notifyDataSetChange
     */
    fun setMaxItemHorizontal(@IntRange(from = 1, to = 10) number: Int) {
        maxItemHorizontal = number
        this.layoutManager = getGridLayoutManager()
    }

    fun setAdapter(adapter: BaseGridAdapter) {
        this.adapter = adapter
    }

    private fun getGridLayoutManager(): LayoutManager {
        val spanCount = getFactorialNumber(maxItemHorizontal)
        return GridLayoutManager(context, spanCount, GridLayoutManager.VERTICAL, false).apply {
            this.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {

                    return if (adapter is BaseGridAdapter) {
                        (adapter as BaseGridAdapter).getItemSpanSize(position, spanCount)
                    } else {
                        spanCount
                    }
                }
            }
        }
    }

    private fun getFactorialNumber(number: Int): Int {
        var factorial = 1
        if (number <= 0) {
            return 0
        }
        for (i in 1..number) {
            factorial *= number
        }
        return factorial
    }
}
