package com.mobile.videocutter.presentation.widget.video.editvideo

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.mobile.videocutter.R
import com.mobile.videocutter.base.extension.getAppColor

class EditVideoView(ctx: Context, attrs: AttributeSet?) : View(ctx, attrs) {

    private val pathInfoList: MutableList<PathInfo> = arrayListOf()
    private val rectangleList: MutableList<Rectangle> = arrayListOf()

    private lateinit var rectangleStart: Rectangle
    private lateinit var rectangleCenterStart: Rectangle

    private lateinit var rectangleEnd: Rectangle
    private lateinit var rectangleCenterEnd: Rectangle

    private lateinit var rectangleTop: Rectangle
    private lateinit var rectangleBottom: Rectangle

    private lateinit var rectangleTassels: Rectangle

    private var widthLayout: Float = 0F
    private var heightLayout: Float = 0F

    private val paintBlack by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            isAntiAlias = true
            color = Color.BLACK
            style = Paint.Style.FILL
        }
    }

    private val paintWhite by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            isAntiAlias = true
            color = Color.WHITE
            style = Paint.Style.FILL
        }
    }

    private val paintYellow by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            isAntiAlias = true
            color = getAppColor(R.color.color_yellow_gradient_end, context)
            style = Paint.Style.FILL
        }
    }

    private val paintViolet by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            isAntiAlias = true
            color = getAppColor(R.color.purple_500, context)
            style = Paint.Style.FILL
        }
    }

    private fun settingViewInfo() {

        //vẽ đối tưởng start trim view
        rectangleStart = Rectangle().apply {
            leftF = 0f
            topF = heightLayout / 18
            rightF = widthLayout / 15
            bottomF = heightLayout * 17 / 18
            radiusRightTop = 0f
            radiusRightBottom = 0f
            colorCode = Rectangle.COLOR_CODE.BLACK
        }

        // vẽ đối tượng nằm trong start trim view
        rectangleCenterStart = Rectangle().apply {
            leftF = (widthLayout / 15) * 2 / 5
            topF = heightLayout / 3
            rightF = (widthLayout / 15) * 3 / 5
            bottomF = heightLayout * 2 / 3
            colorCode = Rectangle.COLOR_CODE.WHITE
        }

        // vẽ đối tượng end trim view
        rectangleEnd = Rectangle().apply {
            leftF = widthLayout * 14 / 15
            topF = heightLayout / 18
            rightF = widthLayout
            bottomF = heightLayout * 17 / 18
            radiusLeftTop = 0f
            radiusLeftBottom = 0f
            colorCode = Rectangle.COLOR_CODE.BLACK
        }

        //vẽ đối tượng nằm trong end trim view
        rectangleCenterEnd = Rectangle().apply {
            leftF = (widthLayout * 14 + widthLayout * 2 / 5) / 15
            topF = heightLayout / 3
            rightF = (widthLayout * 14 + widthLayout * 3 / 5) / 15
            bottomF = heightLayout * 2 / 3
            colorCode = Rectangle.COLOR_CODE.WHITE
        }

        //vẽ đối tượng top trim view
        rectangleTop = Rectangle().apply {
            leftF = (widthLayout / 15) * 3 / 5
            topF = heightLayout / 18
            rightF = (widthLayout * 14 + widthLayout * 2 / 5) / 15
            bottomF = heightLayout * 2 / 18
            radiusLeftBottom = 0f
            radiusRightBottom = 0f
            colorCode = Rectangle.COLOR_CODE.BLACK
        }

        //vẽ đối tượng bottom trim view
        rectangleBottom = Rectangle().apply {
            leftF = (widthLayout / 15) * 3 / 5
            topF = heightLayout * 16 / 18
            rightF = (widthLayout * 14 + widthLayout * 2 / 5) / 15
            bottomF = heightLayout * 17 / 18
            radiusLeftTop = 0f
            radiusRightTop = 0f
            colorCode = Rectangle.COLOR_CODE.BLACK
        }

        //vẽ thanh tua trim video
        rectangleTassels = Rectangle().apply {
            leftF = widthLayout / 15
            topF = 0f
            rightF = (widthLayout + 2 * widthLayout / 5) / 15
            bottomF = heightLayout
            colorCode = Rectangle.COLOR_CODE.VIOLET
        }

        rectangleList.add(rectangleStart)
        rectangleList.add(rectangleCenterStart)
        rectangleList.add(rectangleEnd)
        rectangleList.add(rectangleCenterEnd)
        rectangleList.add(rectangleTop)
        rectangleList.add(rectangleBottom)
        rectangleList.add(rectangleTassels)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        widthLayout = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        heightLayout = MeasureSpec.getSize(heightMeasureSpec).toFloat()
        settingViewInfo()
        initView()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.save()
        pathInfoList.forEach {
            if (it.paint != null) {
                canvas?.drawPath(it.path, it.paint!!)
            }
        }
        canvas?.restore()
    }

    private fun initView(videoType: EDIT_VIDEO_TYPE = EDIT_VIDEO_TYPE.TRIM_VIDEO) {
        if (videoType == EDIT_VIDEO_TYPE.TRIM_VIDEO) {

            rectangleList.forEach {
                val rect = RectF(it.leftF, it.topF, it.rightF, it.bottomF)

                val radius = floatArrayOf(
                    it.radiusLeftTop,
                    it.radiusLeftTop,
                    it.radiusRightTop,
                    it.radiusRightTop,
                    it.radiusRightBottom,
                    it.radiusRightBottom,
                    it.radiusLeftBottom,
                    it.radiusLeftBottom)

                when (it.colorCode) {
                    Rectangle.COLOR_CODE.BLACK -> {
                        val pathInfo = PathInfo().apply {
                            path.addRoundRect(rect, radius, Path.Direction.CW)
                            paint = paintBlack
                        }
                        pathInfoList.add(pathInfo)
                    }

                    Rectangle.COLOR_CODE.WHITE -> {
                        val pathInfo = PathInfo().apply {
                            path.addRoundRect(rect, radius, Path.Direction.CW)
                            paint = paintWhite
                        }
                        pathInfoList.add(pathInfo)
                    }

                    Rectangle.COLOR_CODE.VIOLET -> {
                        val pathInfo = PathInfo().apply {
                            path.addRoundRect(rect, radius, Path.Direction.CW)
                            paint = paintViolet
                        }
                        pathInfoList.add(pathInfo)
                    }

                    else -> {}
                }
            }
        } else {
            rectangleList.forEach {
                val rect = RectF(it.leftF, it.topF, it.rightF, it.bottomF)

                val radius = floatArrayOf(
                    it.radiusLeftTop,
                    it.radiusLeftTop,
                    it.radiusRightTop,
                    it.radiusRightTop,
                    it.radiusRightBottom,
                    it.radiusRightBottom,
                    it.radiusLeftBottom,
                    it.radiusLeftBottom)

                when (it.colorCode) {
                    else -> {}
                }
            }
        }
    }

}
