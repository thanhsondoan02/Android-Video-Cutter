package com.mobile.videocutter.presentation.widget.recyclerview.scroll

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class BaseLoadMoreRecyclerView(
    private var layoutManager: RecyclerView.LayoutManager
) : RecyclerView.OnScrollListener() {

    open val lastPage: Boolean = false
    open val isLoading: Boolean = false

    abstract fun onLoadMore()

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
            val totalItemCount = layoutManager.itemCount
            var pastVisibleItems = 0
            when (layoutManager) {
                is GridLayoutManager ->
                    pastVisibleItems = (layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                is LinearLayoutManager ->
                    pastVisibleItems = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            }

            if (!isLoading && totalItemCount > 0) {
                if (pastVisibleItems == totalItemCount - 1 && !lastPage) {
                    onLoadMore()
                }
            }
        }
        super.onScrollStateChanged(recyclerView, newState)
    }
}
