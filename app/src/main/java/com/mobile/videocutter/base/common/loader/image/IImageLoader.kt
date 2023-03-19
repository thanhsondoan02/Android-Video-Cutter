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

    fun loadImage(
        view: ImageView,
        videoPath: String?,
        placeHolder: Drawable? = null,
        ignoreCache: Boolean = false
    )

    fun loadImage(
        view: ImageView,
        drawable: Drawable?,
        placeHolder: Drawable?= null,
        ignoreCache: Boolean= false
    )
}
