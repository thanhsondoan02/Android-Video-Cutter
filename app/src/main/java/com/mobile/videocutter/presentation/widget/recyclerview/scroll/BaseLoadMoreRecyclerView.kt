package com.mobile.videocutter.presentation.widget.recyclerview.scroll

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class BaseLoadMoreRecyclerView(
    private var layoutManager: RecyclerView.LayoutManager
) : RecyclerView.OnScrollListener() {

    private var isScrollDown = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        isScrollDown = dy > 0
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_SETTLING && isScrollDown) {
            val totalItemCount = layoutManager.itemCount
            var pastVisibleItems = 0
            when (layoutManager) {
                is GridLayoutManager ->
                    pastVisibleItems = (layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                is LinearLayoutManager ->
                    pastVisibleItems = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            }

            if (totalItemCount > 0 && !isLoading) {
                if (pastVisibleItems == totalItemCount - 1 && !lastPage) {
                    onLoadMore()
                }
            }
        }
        super.onScrollStateChanged(recyclerView, newState)
    }

    abstract fun onLoadMore()

    abstract val lastPage: Boolean

    abstract val isLoading: Boolean
}
