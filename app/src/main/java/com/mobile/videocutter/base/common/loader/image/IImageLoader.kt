package com.mobile.videocutter.base.common.loader.image

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView

interface IImageLoader {
    fun loadImage(
        view: ImageView,
        uri: Uri?,
        placeHolder: Drawable? = null,
        ignoreCache: Boolean = false
    )
}
