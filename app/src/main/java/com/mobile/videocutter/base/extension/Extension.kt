import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.base.common.binding.BaseBindingFragment
import com.mobile.videocutter.base.common.loader.LoaderFactory
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

fun ImageView.loadImage(
    videoPath: String?,
    ignoreCache: Boolean = false,
    placeHolder: Drawable? = getPlaceHolderDefault()
) {
    LoaderFactory.glide().loadImage(
        view = this,
        videoPath = videoPath,
        placeHolder = placeHolder,
        ignoreCache = ignoreCache
    )
}

private fun getPlaceHolderDefault(): Drawable? {
    return null
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

fun <T> BaseBindingFragment<*>.handleUiState(
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

/**
 * time in format of 00:00 or 00:00:00
 */
fun getFormattedTime(time: Long): String {
    val seconds = time / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val secondsLeft = seconds % 60
    val minutesLeft = minutes % 60
    val hoursLeft = hours % 60
    return if (hoursLeft > 0) {
        String.format("%02d:%02d:%02d", hoursLeft, minutesLeft, secondsLeft)
    } else {
        String.format("%02d:%02d", minutesLeft, secondsLeft)
    }
}
