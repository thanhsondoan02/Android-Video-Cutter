package com.mobile.videocutter.presentation

import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.databinding.ActivityMainBinding
import com.mobile.videocutter.presentation.exampleloadmore.TestAdapter
import com.mobile.videocutter.presentation.widget.recyclerview.LAYOUT_MANAGER_MODE

class MainActivity : BaseBindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    private var test = TestAdapter()
//    private val viewModel by viewModels<TestViewModel>()

    override fun onPrepareInitView() {
        super.onPrepareInitView()
//        viewModel.fakeData()
    }

    override fun onInitView() {
        super.onInitView()
//        binding.rv.setAdapter(test)
//        binding.rv.setLayoutManagerMode(LAYOUT_MANAGER_MODE.LINEAR_VERTICAL)
//
//        binding.rv.setLoadMore {
//            Log.d(TAG, "onInitView: FGHJKLOKJNB BNKL:LKNBNJKL:LMN ")
//            viewModel.fakeData()
//        }
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()

        lifecycleScope.launchWhenResumed {
//            viewModel.observerData.collect {
//                Log.d(TAG, "onInitView: ${it.data?.dataList?.size}")
//                if (it.data != null) {
//                    binding.rv.submitList(it.data?.dataList as List<TestAdapter.TestModel>, viewModel.dataPage.hasLoadMore())
//                }
//            }
        }

        lifecycleScope.launchWhenResumed {
//            viewModel.testObserver.collect {
//                Log.d(TAG, "setDataERTYUI: ${it}")
//            }
//        }
        }
    }
}
