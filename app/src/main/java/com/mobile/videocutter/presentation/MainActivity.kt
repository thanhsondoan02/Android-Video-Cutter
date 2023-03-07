package com.mobile.videocutter.presentation

import android.util.Log
import androidx.activity.viewModels
import com.mobile.videocutter.R
import com.mobile.videocutter.TestAdapter
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.databinding.ActivityMainBinding
import com.mobile.videocutter.presentation.widget.recyclerview.LAYOUT_MANAGER_MODE

class MainActivity : BaseBindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    private var test = TestAdapter()
    private val viewModel by viewModels<TestViewModel>()
    override fun onInitView() {
        super.onInitView()
        binding.rv.setAdapter(test)
        binding.rv.setLayoutManagerMode(LAYOUT_MANAGER_MODE.LINEAR_VERTICAL)

        binding.rv.submitList(viewModel.list,true)
    }

}
