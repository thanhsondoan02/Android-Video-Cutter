package com.mobile.videocutter.presentation.exampleloadmore

import androidx.lifecycle.viewModelScope
import com.mobile.videocutter.base.common.BaseViewModel
import com.mobile.videocutter.presentation.widget.recyclerview.DataPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TestViewModel : BaseViewModel() {
    private val TAG = "TestViewModel"
    var dataPage = DataPage()

    var list: MutableList<TestAdapter.TestModel> = arrayListOf()

    var observerData: MutableStateFlow<MutableList<TestAdapter.TestModel>?> = MutableStateFlow(null)

    var testObserver: MutableStateFlow<Boolean?> = MutableStateFlow(null)

    init {
        testObserver.value = false
    }

    fun fakeData() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            observerData.value = null
            val list: MutableList<TestAdapter.TestModel> = arrayListOf()

            for (i in 0 until 60) {
                list.add(TestAdapter.TestModel("Test ${i}"))
            }

            dataPage.addList(list)

            observerData.value = dataPage.dataList as MutableList<TestAdapter.TestModel>
        }
    }

    fun reser() {
        testObserver.value = true
    }

}
