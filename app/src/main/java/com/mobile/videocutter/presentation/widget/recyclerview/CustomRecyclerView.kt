package com.mobile.videocutter.presentation.widget.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.adapter.BaseAdapter
import com.mobile.videocutter.base.common.adapter.BaseGridAdapter
import com.mobile.videocutter.presentation.widget.recyclerview.scroll.BaseLoadMoreRecyclerView

class CustomRecyclerView constructor(
    ctx: Context,
    attr: AttributeSet?
) : ConstraintLayout(ctx, attr) {

    private var rvList: RecyclerView? = null
    private var baseAdapter: BaseAdapter? = null
    private var baseLoadMoreRecyclerView: BaseLoadMoreRecyclerView? = null
    private var onLoadMore: (() -> Unit)? = null

    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    private var maxItemHorizontal: Int = 2
    private var layoutManagerMode: LAYOUT_MANAGER_MODE = LAYOUT_MANAGER_MODE.LINEAR_VERTICAL

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_recycler_view_layout, this, true)
        initView(attr)
    }

    private fun addLayoutManager() {
        rvList?.layoutManager = when (layoutManagerMode) {
            LAYOUT_MANAGER_MODE.LINEAR_HORIZATION -> {
                getLinearLayoutManagerHorization()
            }

            LAYOUT_MANAGER_MODE.LINEAR_VERTICAL -> {
                getLinearLayoutManagerVertical()
            }

            LAYOUT_MANAGER_MODE.GRID -> {
                getGridLayoutManager()
            }
            else -> {
                getLinearLayoutManagerVertical()
            }
        }
    }

    private fun initView(attr: AttributeSet?) {
        // ánh xạ view
        rvList = findViewById(R.id.rvCustom)
        setLayoutManagerMode()
        addLayoutManager()
    }

    private fun getLinearLayoutManagerVertical(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun getLinearLayoutManagerHorization(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun getGridLayoutManager(): RecyclerView.LayoutManager {
        val spanCount = getOptimalSpanCount(maxItemHorizontal)
        val gridLayoutManager = GridLayoutManager(context, spanCount, GridLayoutManager.VERTICAL, false)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {

                return if (baseAdapter is BaseGridAdapter) {
                    (baseAdapter as BaseGridAdapter).getItemSpanSize(position, spanCount)
                } else {
                    spanCount
                }
            }
        }

        return gridLayoutManager
    }

    private fun getOptimalSpanCount(maxItemHorizontal: Int): Int {
        var spanCount = 1
        if (maxItemHorizontal <= 0) {
            return 0
        }
        for (i in 1..maxItemHorizontal) {
            spanCount *= maxItemHorizontal
        }
        return spanCount
    }

    private fun hasLoadMore(isLoadMore: Boolean) {
        this@CustomRecyclerView.isLastPage = !isLoadMore
    }

    fun setAdapter(baseAdapter: BaseAdapter) {
        this.baseAdapter = baseAdapter
        rvList?.adapter = baseAdapter
    }

    fun setAdapter(baseGridAdapter: BaseGridAdapter) {
        this.baseAdapter = baseGridAdapter
        rvList?.adapter = baseGridAdapter
    }

    /**
     * để set recyclerview theo chiều nào
     */
    fun setLayoutManagerMode(layoutManagerMode: LAYOUT_MANAGER_MODE = LAYOUT_MANAGER_MODE.LINEAR_VERTICAL) {
        this.layoutManagerMode = layoutManagerMode
    }

    fun showLoading() {
        hideLoading()
        baseAdapter?.makeLoading()
    }

    fun hideLoading() {
        baseAdapter?.removeLoading()
    }

    fun submitList(newData: List<Any>? = null, hasLoadMore: Boolean = false) {
        hideLoading()
        if (baseAdapter != null && baseAdapter!!.dataList.isNotEmpty()) {
            baseAdapter?.removeLoadMore()
        }
        hasLoadMore(hasLoadMore)
        baseAdapter?.submitList(newData)
        if (newData != null) {
            if (newData.isEmpty()) {
                baseAdapter?.makeEmpty()
            }
        } else {
            baseAdapter?.makeLoading()
        }
        this.isLoading = false
    }

    fun setLoadMore(onClick: (() -> Unit)?) {
        onLoadMore = onClick

        baseLoadMoreRecyclerView = object : BaseLoadMoreRecyclerView(rvList?.layoutManager!!) {

            override val lastPage: Boolean
                get() = this@CustomRecyclerView.isLastPage

            override val isLoading: Boolean
                get() = this@CustomRecyclerView.isLoading

            override fun onLoadMore() {
                baseAdapter?.makeLoadMore()
                onLoadMore?.invoke()
                this@CustomRecyclerView.isLoading = true
            }
        }

        baseLoadMoreRecyclerView?.let {
            rvList?.addOnScrollListener(it)
        }
    }

    fun getCountData() = baseAdapter?.itemCount
}
