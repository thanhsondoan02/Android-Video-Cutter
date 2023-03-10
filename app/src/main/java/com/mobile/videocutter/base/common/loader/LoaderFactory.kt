package com.mobile.videocutter.base.common.loader

import com.mobile.videocutter.base.common.loader.image.GlideImageLoaderImpl
import com.mobile.videocutter.base.common.loader.image.IImageLoader

object LoaderFactory {
    private val imageLoader = GlideImageLoaderImpl()

    fun glide(): IImageLoader {
        return imageLoader
    }
}
