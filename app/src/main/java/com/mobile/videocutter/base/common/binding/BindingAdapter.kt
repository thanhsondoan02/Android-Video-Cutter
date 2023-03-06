package ai.ftech.base.common.binding

import ai.ftech.base.extension.setOnSafeClick
import ai.ftech.base.extension.setOnSafeGlobalClick
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("app:rv_set_adapter")
fun <T : RecyclerView.ViewHolder> RecyclerView.applyAdapter(applyAdapter: RecyclerView.Adapter<T>?) {
    applyAdapter?.apply {
        adapter = applyAdapter
    }
}

@BindingAdapter("app:rv_set_fix_size")
fun RecyclerView.setFixSize(set: Boolean) {
    setHasFixedSize(set)
}

@BindingAdapter("app:on_safe_click")
fun View.onSafeClick(listener: View.OnClickListener?) {
    listener?.let {
        this.setOnSafeClick { _ ->
            it.onClick(this)
        }
    }
}

@BindingAdapter("app:on_safe_global_click")
fun View.onSafeGlobalClick(listener: View.OnClickListener?) {
    listener?.let {
        this.setOnSafeGlobalClick { _ ->
            it.onClick(this)
        }
    }
}
