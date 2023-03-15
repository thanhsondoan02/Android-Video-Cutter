package com.mobile.videocutter.presentation.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.mobile.videocutter.R
import com.mobile.videocutter.base.extension.getAppColor
import com.mobile.videocutter.base.extension.getAppDimension

class RatioView constructor(
    ctx: Context,
    attributeSet: AttributeSet?
) : View(ctx, attributeSet) {
    var ratio = 1f
    var strokeColor = getAppColor(R.color.color_purple)

    private val paint by lazy {
        Paint().apply {
            color = color
            style = Paint.Style.STROKE
            strokeWidth = getAppDimension(R.dimen.dimen_2)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        var drawWidth = width.toFloat()
        var drawHeight = height.toFloat()
        if (ratio > 1) {
            drawHeight = width / ratio
        } else {
            drawWidth = height * ratio
        }
        val top = (height - drawHeight) / 2
        val left = (width - drawWidth) / 2
        canvas?.drawRect(left, top, left + drawWidth, top + drawHeight, paint)
    }
}
