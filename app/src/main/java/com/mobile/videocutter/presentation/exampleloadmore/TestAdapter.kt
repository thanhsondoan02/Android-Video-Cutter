package com.mobile.videocutter.presentation.exampleloadmore

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.adapter.BaseAdapter
import com.mobile.videocutter.base.common.adapter.BaseDiffUtilCallback
import com.mobile.videocutter.base.common.adapter.BaseVH
import com.mobile.videocutter.databinding.TestItemBinding

class TestAdapter : BaseAdapter() {
    override fun getLayoutResource(viewType: Int): Int {
        return R.layout.test_item
    }

    override fun getDiffUtil(oldList: List<Any>, newList: List<Any>): DiffUtil.Callback {
        return DiffCallback(oldList as List<TestModel>, newList as List<TestModel>)
    }

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return TestVH(binding as ViewDataBinding)
    }

    inner class TestVH(private val binding: ViewDataBinding) : BaseVH<TestModel>(binding) {
        private val viewBinding = binding as TestItemBinding

        override fun onBind(data: TestModel) {
            super.onBind(data)
            viewBinding.tvItm.text = data.text
        }
    }

    class DiffCallback(oldData: List<TestModel>, newData: List<TestModel>) :
        BaseDiffUtilCallback<TestModel>(oldData, newData) {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldUser = (getOldItem(oldItemPosition) as? TestModel)
            val newUser = (getNewItem(newItemPosition) as? TestModel)

            return oldUser?.hashCode() == newUser?.hashCode()
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldUser = (getOldItem(oldItemPosition) as? TestModel)
            val newUser = (getNewItem(newItemPosition) as? TestModel)

            return oldUser?.text == newUser?.text
        }
    }

    class TestModel(var text: String)
}
