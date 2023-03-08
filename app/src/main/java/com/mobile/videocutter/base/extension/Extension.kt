import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.image.CORNER_TYPE
import com.mobile.videocutter.base.common.image.LoaderFactory
import com.mobile.videocutter.base.extension.getAppDrawable
import com.mobile.videocutter.presentation.widget.recyclerview.DataPage
import com.mobile.videocutter.thread.FlowResult
import kotlinx.coroutines.flow.MutableStateFlow

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
    url: String?,
    ignoreCache: Boolean = false,
    placeHolder: Drawable? = getPlaceHolderDefault()
) {
    LoaderFactory.glide().loadImage(
        view = this,
        url = url,
        placeHolder = placeHolder,
        ignoreCache = ignoreCache
    )
}

fun ImageView.loadImage(
    drawable: Drawable?,
    ignoreCache: Boolean = false,
    placeHolder: Drawable? = getPlaceHolderDefault()
) {
    LoaderFactory.glide().loadImage(
        view = this,
        drawable = drawable,
        placeHolder = placeHolder,
        ignoreCache = ignoreCache
    )
}

fun ImageView.loadImageBase64(
    base64: String?,
    ignoreCache: Boolean = false
) {
    LoaderFactory.glide().loadImageBase64(
        view = this,
        base64 = base64,
        placeHolder = getPlaceHolderDefault(),
        ignoreCache = ignoreCache
    )
}

fun ImageView.loadRoundCornerImage(
    url: String?,
    corner: Int,
    ignoreCache: Boolean = false,
    cornerType: CORNER_TYPE = CORNER_TYPE.ALL
) {
    LoaderFactory.glide().loadRoundCornerImage(
        view = this,
        url = url,
        corner = corner,
        placeHolder = getPlaceHolderDefault(),
        ignoreCache = ignoreCache,
        cornerType = cornerType
    )
}

fun ImageView.loadCircleImage(
    url: String?,
    ignoreCache: Boolean = false
) {
    LoaderFactory.glide().loadCircleImage(
        view = this,
        url = url,
        placeHolder = getPlaceHolderUser(),
        ignoreCache = ignoreCache
    )
}

private fun getPlaceHolderDefault(): Drawable? {
    return getAppDrawable(R.drawable.ic_launcher_background)
}

private fun getPlaceHolderUser(): Drawable? {
    return getAppDrawable(R.drawable.ic_launcher_background)
}
