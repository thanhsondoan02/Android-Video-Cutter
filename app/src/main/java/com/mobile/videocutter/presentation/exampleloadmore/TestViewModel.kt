package com.mobile.videocutter.presentation.exampleloadmore

import androidx.lifecycle.viewModelScope
import com.mobile.videocutter.base.common.BaseViewModel
import com.mobile.videocutter.presentation.widget.recyclerview.DataPage
import com.mobile.videocutter.thread.FlowResult
import data
import getDataPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import success

class TestViewModel : BaseViewModel() {
    private val TAG = "TestViewModel"

    var list: MutableList<TestAdapter.TestModel> = arrayListOf()

    var observerData = MutableStateFlow(FlowResult.newInstance<DataPage<TestAdapter.TestModel>>())

    var testObserver: MutableStateFlow<Boolean?> = MutableStateFlow(null)

    var dataPage = getDataPage(observerData.data())

    init {
        testObserver.value = false
    }

    fun fakeData() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(350)
            val list: MutableList<TestAdapter.TestModel> = arrayListOf()

            for (i in 0 until 100) {
                list.add(TestAdapter.TestModel("Test ${i}"))
            }

            dataPage.addList(list)

            observerData.success(dataPage)
        }
    }
}
