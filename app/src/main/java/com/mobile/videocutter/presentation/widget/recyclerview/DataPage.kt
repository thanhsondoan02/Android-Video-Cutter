package com.mobile.videocutter.presentation.widget.recyclerview

import com.mobile.videocutter.AppConfig

class DataPage<DATA> {

    private val limit = AppConfig.ITEM_SIZE

    private var nextPage: Int = 1
    private var currentPage: Int = 0

    var dataList: MutableList<DATA> = arrayListOf()

    fun reset() {
        this.nextPage = 0
        this.currentPage = 0
        this.dataList.clear()
    }

    fun addList(list: List<DATA>?) {
        if (list != null) {
            if (list.size >= 20) {
                for (i in currentPage..nextPage * limit) {
                    dataList.add(list[i])
                }
            }else{
                dataList.addAll(list)
            }
            currentPage++
            nextPage++
        }
    }

    fun hasLoadMore(): Boolean {
        return dataList.count() > limit
    }
}
