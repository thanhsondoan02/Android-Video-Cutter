package com.mobile.videocutter.base.common.image

import android.graphics.drawable.Drawable
import android.widget.ImageView

interface IImageLoader {
    fun loadImage(
        view: ImageView,
        url: String?,
        placeHolder: Drawable? = null,
        ignoreCache: Boolean = false
    )

    fun loadImage(
        view: ImageView,
        drawable: Drawable?,
        placeHolder: Drawable? = null,
        ignoreCache: Boolean = false
    )

    fun loadImageBase64(
        view: ImageView,
        base64: String?,
        placeHolder: Drawable? = null,
        ignoreCache: Boolean = false
    )

    fun loadRoundCornerImage(
        view: ImageView,
        url: String?,
        corner: Int,
        placeHolder: Drawable? = null,
        ignoreCache: Boolean = false,
        cornerType: CORNER_TYPE = CORNER_TYPE.ALL
    )

    fun loadCircleImage(
        view: ImageView,
        url: String?,
        placeHolder: Drawable? = null,
        ignoreCache: Boolean = false
    )
}
