package com.mobile.videocutter.base.common.loader.image

import android.graphics.drawable.Drawable
import android.widget.ImageView

interface IImageLoader {
    fun loadImage(
        view: ImageView,
        uri: String?,
        placeHolder: Drawable? = null,
        ignoreCache: Boolean = false
    )
}
