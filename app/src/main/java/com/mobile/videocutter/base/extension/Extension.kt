import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.base.common.loader.LoaderFactory
import com.mobile.videocutter.base.extension.getAppDrawable
import com.mobile.videocutter.presentation.model.IViewListener
import com.mobile.videocutter.presentation.widget.recyclerview.DataPage
import com.mobile.videocutter.thread.FlowResult
import com.mobile.videocutter.thread.UI_STATE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch

fun <DATA> MutableStateFlow<FlowResult<DATA>>.data(): DATA? {
    return this.value.data
}

fun <DATA> MutableStateFlow<FlowResult<DATA>>.success(data: DATA) {
    this.value = FlowResult.newInstance<DATA>().success(data)
}

fun <DATA> MutableStateFlow<FlowResult<DATA>>.failure(throwable: Throwable, data: DATA? = null) {
    this.value = FlowResult.newInstance<DATA>().failure(throwable, data)
}

fun <DATA> MutableStateFlow<FlowResult<DATA>>.loading(message: String? = null) {
    this.value = FlowResult.newInstance<DATA>().loading(message)
}

fun <DATA> MutableStateFlow<FlowResult<DATA>>.initial() {
    this.value = FlowResult.newInstance<DATA>().initial()
}

fun <T> getDataPage(dataPage: DataPage<T>?): DataPage<T> {
    var _dataPage = dataPage
    if (_dataPage == null) {
        _dataPage = DataPage()
    }
    return _dataPage
}

fun ImageView.loadImage(
    uri: Uri?,
    ignoreCache: Boolean = false,
    placeHolder: Drawable? = getPlaceHolderDefault()
) {
    LoaderFactory.glide().loadImage(
        view = this,
        uri = uri,
        placeHolder = placeHolder,
        ignoreCache = ignoreCache
    )
}

private fun getPlaceHolderDefault(): Drawable? {
    return getAppDrawable(R.drawable.ic_placeholder)
}

fun <T> BaseBindingActivity<*>.handleUiState(
    flowResult: FlowResult<T>,
    listener: IViewListener? = null,
    canShowLoading: Boolean = false,
    canHideLoading: Boolean = false,
    canShowError: Boolean = true
) {
    when (flowResult.getUiState()) {
        UI_STATE.INITIAL -> {
            listener?.onInitial()
        }
        UI_STATE.LOADING -> {
            listener?.onLoading()
        }
        UI_STATE.FAILURE -> {
            listener?.onFailure()
        }
        UI_STATE.SUCCESS -> {
            listener?.onSuccess()
        }
    }
}

fun <T> Flow<T>.onException(onCatch: suspend (Throwable) -> Unit): Flow<T> {
    return catch { e ->
        e.printStackTrace()
        onCatch(e)
    }
}
