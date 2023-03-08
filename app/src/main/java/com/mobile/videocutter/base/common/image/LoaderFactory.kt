package com.mobile.videocutter.base.common.image

object LoaderFactory {
    private val imageLoader = GlideImageLoaderImpl()

    fun glide(): IImageLoader {
        return imageLoader
    }
}
