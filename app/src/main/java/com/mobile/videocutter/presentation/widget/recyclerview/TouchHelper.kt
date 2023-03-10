package com.mobile.videocutter.presentation.widget.recyclerview

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class TouchHelper : ItemTouchHelper.Callback() {

    private val TAG = "ItemTouchHelperCustom"
    /**
     * nếu không phải là phần tử mới đc move còn ko thì không được phép
     */
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return if (isDragLastItem) {
            makeMovementFlags(ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, 0)
        } else {
            return if (dataList != null && viewHolder.bindingAdapterPosition != dataList?.size!! - 1) {
                makeMovementFlags(ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, 0)
            } else {
                0
            }
        }
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        val dragIndex = viewHolder.bindingAdapterPosition
        val targetIndex = target.bindingAdapterPosition

        if (isDragLastItem) {
            this.eventMove(dragIndex, targetIndex)
            return true
        } else {
            if (targetIndex != dataList!!.size - 1) {
                this.eventMove(dragIndex, targetIndex)
                return true
            }
        }
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwipe?.invoke()
    }

    abstract val dataList: MutableList<Any>?

    abstract fun eventMove(oldIndex: Int, newIndex: Int)

    open val isDragLastItem: Boolean = false

    open fun onSwipe(onSwipe: (() -> Unit)?) {
        this.onSwipe = onSwipe
    }

    private var onSwipe: (() -> Unit)? = null
}
