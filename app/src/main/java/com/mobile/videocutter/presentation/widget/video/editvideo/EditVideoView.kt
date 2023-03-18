package com.mobile.videocutter.presentation.widget.video.editvideo

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class EditVideoView(ctx: Context, attrs: AttributeSet?) : View(ctx, attrs) {

    private val pathList: MutableList<Path> = arrayListOf()

    private val paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.RED
            style = Paint.Style.FILL
        }
    }

    private val paint1 by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            style = Paint.Style.FILL
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val cornerRadius = 20f // Độ cong góc

        val rect = RectF(0F, 0f, width.toFloat()/9, height.toFloat())
        val path = Path()
        path.addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CW)
        canvas?.drawPath(path, paint) // Vẽ đường bao


        val rect1 = RectF(100F*2/5, height.toFloat()/3, 100F*3/5,height.toFloat()*2/3)
        val path1 = Path()
        path1.addRoundRect(rect1, cornerRadius, cornerRadius, Path.Direction.CW)
        canvas?.drawPath(path1, paint1) // Vẽ đường bao
    }
}
