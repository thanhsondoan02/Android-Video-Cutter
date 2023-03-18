package com.mobile.videocutter.presentation.widget.recyclerview

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_IDLE
import androidx.recyclerview.widget.RecyclerView

abstract class TouchHelper : ItemTouchHelper.Callback() {
    private val TAG = "ItemTouchHelperCustom"

    private var onSwipe: (() -> Unit)? = null
    private var oldIndex = 0
    private var newIndex = 0
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
        oldIndex = viewHolder.bindingAdapterPosition
        newIndex = target.bindingAdapterPosition
        if (isDragLastItem) {
            this.eventMove(oldIndex, newIndex)
            return true
        } else {
            if (newIndex != dataList!!.size - 1) {
                this.eventMove(oldIndex, newIndex)
                return true
            }
        }
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwipe?.invoke()
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        onMoveSuccess(oldIndex, newIndex)
    }

    abstract val dataList: MutableList<Any>?

    abstract fun eventMove(oldIndex: Int, newIndex: Int)

    abstract fun onMoveSuccess(oldIndex: Int,newIndex: Int)

    open val isDragLastItem: Boolean = false

    open fun onSwipe(onSwipe: (() -> Unit)?) {
        this.onSwipe = onSwipe
    }
}
