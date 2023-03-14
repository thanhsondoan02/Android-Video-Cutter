package com.mobile.videocutter.util

import android.graphics.Paint
import android.graphics.Paint.Cap
import com.mobile.videocutter.R
import com.mobile.videocutter.base.extension.getAppColor

class UtilPaint {
    private val myPaint = Paint()

    val strokeWidth = 50f

    private val defaultColor = getAppColor(R.color.color_gray)

    /**
     * để tô mặc định màu xanh blue
     */
    fun getPaintFill(): Paint {
        return myPaint.apply {
            color = defaultColor
            style = Paint.Style.FILL
            strokeWidth = this@UtilPaint.strokeWidth
        }
    }

    /**
     * để vẽ
     */
    fun getPaintStroke(): Paint {
        return myPaint.apply {
            color = defaultColor
            style = Paint.Style.STROKE
            strokeWidth = this@UtilPaint.strokeWidth
        }
    }

    /**
     * vẽ bo tròn ở hai đầu mút của đoạn thẳng
     */
    fun getPainStrokeCapRound(): Paint {
        return myPaint.apply {
            color = defaultColor
            style = Paint.Style.STROKE
            strokeCap = Cap.ROUND
            strokeWidth = this@UtilPaint.strokeWidth
        }
    }

    fun getPainFillCapRound(): Paint {
        return myPaint.apply {
            color = defaultColor
            style = Paint.Style.FILL
            strokeCap = Cap.ROUND
            strokeWidth = this@UtilPaint.strokeWidth
        }
    }

    /**
     * vẽ sẽ sắc cạch ở hai đầu mút
     */
    fun getPainStrokeCapSquare(): Paint {
        return myPaint.apply {
            color = defaultColor
            style = Paint.Style.STROKE
            strokeCap = Cap.SQUARE
            strokeWidth = this@UtilPaint.strokeWidth
        }
    }

    fun getPainFillCapSquare(): Paint {
        return myPaint.apply {
            color = defaultColor
            style = Paint.Style.FILL
            strokeCap = Cap.SQUARE
            strokeWidth = this@UtilPaint.strokeWidth
        }
    }
}
