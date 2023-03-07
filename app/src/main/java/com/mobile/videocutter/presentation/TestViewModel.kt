package com.mobile.videocutter.presentation

import com.mobile.videocutter.TestAdapter
import com.mobile.videocutter.base.common.BaseViewModel
import com.mobile.videocutter.presentation.widget.recyclerview.DataPage

class TestViewModel : BaseViewModel() {

    var dataPage = DataPage<List<com.mobile.videocutter.TestAdapter.TestModel>>()

    var list: MutableList<TestAdapter.TestModel> = arrayListOf()



    private fun fakeData() {
        val list: MutableList<com.mobile.videocutter.TestAdapter.TestModel> = arrayListOf()
        for (i in 0 until 100) {
            list.add(TestAdapter.TestModel("Test ${i + 1}"))
        }
//        dataPage.addList(listOf())
        this.list.addAll(list)
    }

}
