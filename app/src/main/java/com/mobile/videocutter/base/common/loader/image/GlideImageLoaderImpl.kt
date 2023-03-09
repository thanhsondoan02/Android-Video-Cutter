package com.mobile.videocutter.base.common.loader.image

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class GlideImageLoaderImpl: IImageLoader {
    override fun loadImage(view: ImageView, uri: Uri?, placeHolder: Drawable?, ignoreCache: Boolean) {
        try {
            Glide.with(view)
                .load(uri)
                .placeholder(placeHolder)
                .skipMemoryCache(ignoreCache)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(view)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
