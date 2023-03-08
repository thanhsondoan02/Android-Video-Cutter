package com.mobile.videocutter.presentation.widget.recyclerview

import android.util.Log
import com.mobile.videocutter.AppConfig

class DataPage<DATA> {

    private val limit = AppConfig.ITEM_SIZE

    private var nextPage: Int = 1
    private var currentPage: Int = 0

    private var isLoadMore: Boolean = false

    var dataList: MutableList<DATA> = arrayListOf()

    fun reset() {
        this.nextPage = 0
        this.currentPage = 0
        this.dataList.clear()
    }

    fun addList(list: List<DATA>?) {
        if (list != null) {
            if (list.size > limit) {
                if (nextPage * limit <= list.size) {
                    for (i in currentPage * limit until nextPage * limit) {
                        dataList.add(list[i])
                    }
                    currentPage++
                    nextPage++
                } else {
                    /**
                     * lấy phần dư ví dụ load page từ 0 -> 26 thì phần dư offset = 6
                     * và cho chạy từ currentPage * limit - currentPage * limit + offset
                     */
                    val resultRemainder = list.size / limit
                    val offset = list.size - resultRemainder * limit
                    for (i in currentPage * limit until currentPage * limit + offset) {
                        dataList.add(list[i])
                    }
                }
            } else {
                dataList.addAll(list)
            }
            isLoadMore = dataList.size < list.size
        }
    }

    fun hasLoadMore(): Boolean {
        return dataList.count() >= limit && isLoadMore
    }

    fun geNextPage() = nextPage

    fun getCurrentPage() = currentPage
}
