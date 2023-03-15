package com.mobile.videocutter.presentation.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.mobile.videocutter.R
import com.mobile.videocutter.base.extension.getAppColor
import com.mobile.videocutter.base.extension.getAppDimension

class CropView constructor(
    ctx: Context,
    attributeSet: AttributeSet?
) : View(ctx, attributeSet) {
    var ratio: Float? = null
        set(value) {
            field = value
            requestLayout()
        }

    private var cropStrokeWidth = getAppDimension(R.dimen.dimen_2)
    private var cropStrokeColor = getAppColor(R.color.color_purple)
    private var minCropWidth = getAppDimension(R.dimen.dimen_100)
    private var minCropHeight = getAppDimension(R.dimen.dimen_100)
    private var near = getAppDimension(R.dimen.dimen_10)
    private var cropOutsideColor = getAppColor(R.color.color_black_40)
    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = cropStrokeColor
        strokeWidth = cropStrokeWidth
    }

    private var cropTop: Float = 0f
    private var cropLeft: Float = 0f
    private var cropBottom: Float = 0f
    private var cropRight: Float = 0f
    private var oldX = 0f
    private var oldY = 0f
    private var rectChangeType: RectChangeType = RectChangeType.STAY

    init {
        initView(attributeSet)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (ratio == null) {
            cropLeft= cropStrokeWidth * 3 / 2
            cropTop = cropStrokeWidth * 3 / 2
            cropRight = width.toFloat() - cropStrokeWidth * 3 / 2
            cropBottom = height.toFloat() - cropStrokeWidth * 3 / 2
        } else {
            val screenRatio = width / height.toFloat()
            if (ratio!! > screenRatio) {
                cropLeft = cropStrokeWidth * 3 / 2
                cropRight = width.toFloat() - cropStrokeWidth * 3 / 2
                cropTop = cropStrokeWidth * 3 / 2
                cropBottom = cropTop + (width / ratio!!)
            } else {
                cropTop = cropStrokeWidth * 3 / 2
                cropBottom = height.toFloat() - cropStrokeWidth * 3 / 2
                cropLeft = cropStrokeWidth * 3 / 2
                cropRight = cropLeft + (height * ratio!!)
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawOutside(canvas)
        drawMain(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                rectChangeType = rectChangeType(touchX, touchY)
                if (rectChangeType != RectChangeType.STAY) {
                    oldX = touchX
                    oldY = touchY
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (rectChangeType != RectChangeType.STAY) {
                    moveRect(touchX, touchY, rectChangeType)
                }
            }
            MotionEvent.ACTION_UP -> {}
            else -> return false
        }
        invalidate()
        return true
    }

    private fun rectChangeType(touchX: Float, touchY: Float): RectChangeType {
        val cropWidth = cropRight - cropLeft
        val cropHeight = cropBottom - cropTop
        return if (
            touchY > cropTop - cropStrokeWidth - near
            && touchY < cropTop - cropStrokeWidth + near
            && touchX > cropLeft + cropWidth / 2 - getAppDimension(R.dimen.dimen_28) / 2 - near
            && touchX < cropLeft + cropWidth / 2 + getAppDimension(R.dimen.dimen_28) / 2 + near
        ) {
            RectChangeType.EXPAND_UP
        } else if (
            touchY > cropTop + cropHeight + cropStrokeWidth - near
            && touchY < cropTop + cropHeight + cropStrokeWidth + near
            && touchX > cropLeft + cropWidth / 2 - getAppDimension(R.dimen.dimen_28) / 2 - near
            && touchX < cropLeft + cropWidth / 2 + getAppDimension(R.dimen.dimen_28) / 2 + near
        ) {
            RectChangeType.EXPAND_DOWN
        } else if (
            touchX > cropLeft - cropStrokeWidth - near
            && touchX < cropLeft - cropStrokeWidth + near
            && touchY > cropTop + cropHeight / 2 - getAppDimension(R.dimen.dimen_16) / 2 - near
            && touchY < cropTop + cropHeight / 2 + getAppDimension(R.dimen.dimen_16) / 2 + near
        ) {
            RectChangeType.EXPAND_LEFT
        } else if (
            touchX > cropLeft + cropWidth + cropStrokeWidth - near
            && touchX < cropLeft + cropWidth + cropStrokeWidth + near
            && touchY > cropTop + cropHeight / 2 - getAppDimension(R.dimen.dimen_16) / 2 - near
            && touchY < cropTop + cropHeight / 2 + getAppDimension(R.dimen.dimen_16) / 2 + near
        ) {
            RectChangeType.EXPAND_RIGHT
        } else if (touchX > cropLeft - cropStrokeWidth - near
            && touchX < cropLeft - cropStrokeWidth + near
            && touchY > cropTop - cropStrokeWidth * 3 / 2 - near
            && touchY < cropTop - cropStrokeWidth * 3 / 2 + getAppDimension(R.dimen.dimen_16) + near
        ) {
            RectChangeType.EXPAND_UP_LEFT
        } else if (touchX > cropLeft - cropStrokeWidth / 2 - near
            && touchX < cropLeft - cropStrokeWidth / 2 + getAppDimension(R.dimen.dimen_28) + near
            && touchY > cropTop - cropStrokeWidth - near
            && touchY < cropTop - cropStrokeWidth + near
        ) {
            RectChangeType.EXPAND_UP_LEFT
        } else if (touchX > cropLeft - cropStrokeWidth - near
            && touchX < cropLeft - cropStrokeWidth + near
            && touchY > cropTop + cropHeight + cropStrokeWidth * 3 / 2 - getAppDimension(R.dimen.dimen_16) - near
            && touchY < cropTop + cropHeight + cropStrokeWidth * 3 / 2 + near
        ) {
            RectChangeType.EXPAND_DOWN_LEFT
        } else if (touchX > cropLeft - cropStrokeWidth / 2 - near
            && touchX < cropLeft - cropStrokeWidth / 2 + getAppDimension(R.dimen.dimen_28) + near
            && touchY > cropTop + cropHeight + cropStrokeWidth - near
            && touchY < cropTop + cropHeight + cropStrokeWidth + near
        ) {
            RectChangeType.EXPAND_DOWN_LEFT
        } else if (touchX > cropLeft + cropWidth + cropStrokeWidth - near
            && touchX < cropLeft + cropWidth + cropStrokeWidth + near
            && touchY > cropTop - cropStrokeWidth * 3 / 2 - near
            && touchY < cropTop - cropStrokeWidth * 3 / 2 + getAppDimension(R.dimen.dimen_16) + near
        ) {
            RectChangeType.EXPAND_UP_RIGHT
        } else if (touchX > cropLeft + cropWidth + cropStrokeWidth / 2 - getAppDimension(R.dimen.dimen_28) - near
            && touchX < cropLeft + cropWidth + cropStrokeWidth / 2 + near
            && touchY > cropTop - cropStrokeWidth - near
            && touchY < cropTop - cropStrokeWidth + near
        ) {
            RectChangeType.EXPAND_UP_RIGHT
        } else if (touchX > cropLeft + cropWidth + cropStrokeWidth - near
            && touchX < cropLeft + cropWidth + cropStrokeWidth + near
            && touchY > cropTop + cropHeight + cropStrokeWidth * 3 / 2 - getAppDimension(R.dimen.dimen_16) - near
            && touchY < cropTop + cropHeight + cropStrokeWidth * 3 / 2 + near
        ) {
            RectChangeType.EXPAND_DOWN_RIGHT
        } else if (touchX > cropLeft + cropWidth + cropStrokeWidth / 2 - getAppDimension(R.dimen.dimen_28) - near
            && touchX < cropLeft + cropWidth + cropStrokeWidth / 2 + near
            && touchY > cropTop + cropHeight + cropStrokeWidth - near
            && touchY < cropTop + cropHeight + cropStrokeWidth + near
        ) {
            RectChangeType.EXPAND_DOWN_RIGHT
        } else if (touchX > cropLeft - near
            && touchX < cropRight + near
            && touchY > cropTop - near
            && touchY < cropBottom + near
        ) {
            RectChangeType.MOVE
        } else {
            RectChangeType.STAY
        }
    }

    private fun moveRect(touchX: Float, touchY: Float, rectChangeType: RectChangeType) {
        when (rectChangeType) {
            RectChangeType.EXPAND_UP -> {
                if (ratio == null) {
                    expandUp(touchY)
                } else {
                    expandUpWithRatio(touchY)
                }
            }
            RectChangeType.EXPAND_DOWN -> {
                if (ratio == null) {
                    expandDown(touchY)
                } else {
                    expandDownWithRatio(touchY)
                }
            }
            RectChangeType.EXPAND_LEFT -> {
                if (ratio == null) {
                    expandLeft(touchX)
                } else {
                    expandLeftWithRatio(touchX)
                }
            }
            RectChangeType.EXPAND_RIGHT -> {
                if (ratio == null) {
                    expandRight(touchX)
                } else {
                    expandRightWithRatio(touchX)
                }
            }
            RectChangeType.EXPAND_UP_LEFT -> {
                if (ratio == null) {
                    expandUp(touchY)
                    expandLeft(touchX)
                } else {
                    expandUpLeftWithRatio(touchX, touchY)
                }
            }
            RectChangeType.EXPAND_UP_RIGHT -> {
                if (ratio == null) {
                    expandUp(touchY)
                    expandRight(touchX)
                } else {
                    expandUpRightWithRatio(touchX, touchY)
                }
            }
            RectChangeType.EXPAND_DOWN_LEFT -> {
                if (ratio == null) {
                    expandDown(touchY)
                    expandLeft(touchX)
                } else {
                    expandDownLeftWithRatio(touchX, touchY)
                }
            }
            RectChangeType.EXPAND_DOWN_RIGHT -> {
                if (ratio == null) {
                    expandDown(touchY)
                    expandRight(touchX)
                } else {
                    expandDownRightWithRatio(touchX, touchY)
                }
            }
            RectChangeType.MOVE -> {
                move(touchX, touchY)
            }
            RectChangeType.STAY -> {
                // do nothing
            }
        }
        oldX = touchX
        oldY = touchY
    }

    private fun expandUp(touchY: Float) {
        val newCropTop = cropTop + touchY - oldY
        cropTop = if (newCropTop <= cropStrokeWidth * 3 / 2) {
            cropStrokeWidth * 3 / 2
        } else if (cropBottom - newCropTop < minCropHeight) {
            cropBottom - minCropHeight
        } else {
            newCropTop
        }
    }

    private fun expandDown(touchY: Float) {
        val newCropBottom = cropBottom + touchY - oldY
        cropBottom = if (newCropBottom >= height.toFloat() - cropStrokeWidth * 3 / 2) {
            height.toFloat() - cropStrokeWidth * 3 / 2
        } else if (newCropBottom - cropTop < minCropHeight) {
            cropTop + minCropHeight
        } else {
            newCropBottom
        }
    }

    private fun expandLeft(touchX: Float) {
        val newCropLeft = cropLeft + touchX - oldX
        cropLeft = if (newCropLeft <= cropStrokeWidth * 3 / 2) {
            cropStrokeWidth * 3 / 2
        } else if (cropRight - newCropLeft < minCropWidth) {
            cropRight - minCropWidth
        } else {
            newCropLeft
        }
    }

    private fun expandRight(touchX: Float) {
        val newCropRight = cropRight + touchX - oldX
        cropRight = if (newCropRight >= width.toFloat() - cropStrokeWidth * 3 / 2) {
            width.toFloat() - cropStrokeWidth * 3 / 2
        } else if (newCropRight - cropLeft < minCropWidth) {
            cropLeft + minCropWidth
        } else {
            newCropRight
        }
    }

    private fun expandDownRightWithRatio(touchX: Float, touchY: Float) {
        if (ratio == null) {
            return
        }
        val deltaX = touchX - oldX
        val deltaY = touchY - oldY
        if (deltaX > 0 && deltaY > 0) {
            if (abs(deltaX) > abs(deltaY)) {
                val newCropRight = min(cropRight + abs(deltaX), width.toFloat() - cropStrokeWidth * 3 / 2)
                val newCropBottom = cropTop + (newCropRight - cropLeft) / ratio!!
                if (newCropBottom > height.toFloat() - cropStrokeWidth * 3 / 2) {
                    cropBottom = height.toFloat() - cropStrokeWidth * 3 / 2
                    cropRight = cropLeft + (cropBottom - cropTop) * ratio!!
                } else {
                    cropRight = newCropRight
                    cropBottom = newCropBottom
                }
            } else {
                val newCropBottom = min(cropBottom + abs(deltaY), height.toFloat() - cropStrokeWidth * 3 / 2)
                val newCropRight = cropLeft + (newCropBottom - cropTop) * ratio!!
                if (newCropRight > width.toFloat() - cropStrokeWidth * 3 / 2) {
                    cropRight = width.toFloat() - cropStrokeWidth * 3 / 2
                    cropBottom = cropTop + (cropRight - cropLeft) / ratio!!
                } else {
                    cropRight = newCropRight
                    cropBottom = newCropBottom
                }
            }
        } else {
            if (abs(deltaX) > abs(deltaY)) {
                var newCropRight = max(cropRight - abs(deltaX), cropLeft + minCropWidth)
                var newCropBottom = cropTop + (newCropRight - cropLeft) / ratio!!
                if (newCropBottom < cropTop + minCropHeight) {
                    newCropBottom = cropTop + minCropHeight
                    newCropRight = cropLeft + minCropHeight * ratio!!
                    if (newCropRight >= minCropWidth) {
                        cropRight = newCropRight
                        cropBottom = newCropBottom
                    }
                } else {
                    cropRight = newCropRight
                    cropBottom = newCropBottom
                }
            } else {
                var newCropBottom = max(cropBottom - abs(deltaY), cropTop + minCropHeight)
                var newCropRight = cropLeft + (newCropBottom - cropTop) * ratio!!
                if (newCropRight < cropLeft + minCropWidth) {
                    newCropRight = cropLeft + minCropWidth
                    newCropBottom = cropTop + minCropWidth / ratio!!
                    if (newCropBottom >= minCropHeight) {
                        cropRight = newCropRight
                        cropBottom = newCropBottom
                    }
                } else {
                    cropRight = newCropRight
                    cropBottom = newCropBottom
                }
            }
        }
    }

    private fun expandDownLeftWithRatio(touchX: Float, touchY: Float) {
        if (ratio == null) {
            return
        }
        val deltaX = touchX - oldX
        val deltaY = touchY - oldY
        if (deltaX < 0 && deltaY > 0) {
            if (abs(deltaX) > abs(deltaY)) {
                val newCropLeft = max(cropLeft - abs(deltaX), cropStrokeWidth * 3 / 2)
                val newCropBottom = cropTop + (cropRight - newCropLeft) / ratio!!
                if (newCropBottom > height.toFloat() - cropStrokeWidth * 3 / 2) {
                    cropBottom = height.toFloat() - cropStrokeWidth * 3 / 2
                    cropLeft = cropRight - (cropBottom - cropTop) * ratio!!
                } else {
                    cropLeft = newCropLeft
                    cropBottom = newCropBottom
                }
            } else {
                val newCropBottom = min(cropBottom + abs(deltaY), height.toFloat() - cropStrokeWidth * 3 / 2)
                val newCropLeft = cropRight - (newCropBottom - cropTop) * ratio!!
                if (newCropLeft < cropStrokeWidth * 3 / 2) {
                    cropLeft = cropStrokeWidth * 3 / 2
                    cropBottom = cropTop + (cropRight - cropLeft) / ratio!!
                } else {
                    cropLeft = newCropLeft
                    cropBottom = newCropBottom
                }
            }
        } else {
            if (abs(deltaX) > abs(deltaY)) {
                var newCropLeft = min(cropLeft + abs(deltaX), cropRight - minCropWidth)
                var newCropBottom = cropTop + (cropRight - newCropLeft) / ratio!!
                if (newCropBottom < cropTop + minCropHeight) {
                    newCropBottom = cropTop + minCropHeight
                    newCropLeft = cropRight - minCropHeight * ratio!!
                    if (newCropLeft <= cropRight - minCropWidth) {
                        cropLeft = newCropLeft
                        cropBottom = newCropBottom
                    }
                } else {
                    cropLeft = newCropLeft
                    cropBottom = newCropBottom
                }
            } else {
                var newCropBottom = max(cropBottom - abs(deltaY), cropTop + minCropHeight)
                var newCropLeft = cropRight - (newCropBottom - cropTop) * ratio!!
                if (newCropLeft > cropRight - minCropWidth) {
                    newCropLeft = cropRight - minCropWidth
                    newCropBottom = cropTop + minCropWidth / ratio!!
                    if (newCropBottom >= minCropHeight) {
                        cropLeft = newCropLeft
                        cropBottom = newCropBottom
                    }
                } else {
                    cropLeft = newCropLeft
                    cropBottom = newCropBottom
                }
            }
        }
    }

    private fun expandUpLeftWithRatio(touchX: Float, touchY: Float) {
        if (ratio == null) {
            return
        }
        val deltaX = touchX - oldX
        val deltaY = touchY - oldY
        if (deltaX < 0 && deltaY < 0) {
            if (abs(deltaX) > abs(deltaY)) {
                val newCropLeft = max(cropLeft - abs(deltaX), cropStrokeWidth * 3 / 2)
                val newCropTop = cropBottom - (cropRight - newCropLeft) / ratio!!
                if (newCropTop < cropStrokeWidth * 3 / 2) {
                    cropTop = cropStrokeWidth * 3 / 2
                    cropLeft = cropRight - (cropBottom - cropTop) * ratio!!
                } else {
                    cropLeft = newCropLeft
                    cropTop = newCropTop
                }
            } else {
                val newCropTop = max(cropTop - abs(deltaY), cropStrokeWidth * 3 / 2)
                val newCropLeft = cropRight - (cropBottom - newCropTop) * ratio!!
                if (newCropLeft < cropStrokeWidth * 3 / 2) {
                    cropLeft = cropStrokeWidth * 3 / 2
                    cropTop = cropBottom - (cropRight - cropLeft) / ratio!!
                } else {
                    cropLeft = newCropLeft
                    cropTop = newCropTop
                }
            }
        } else {
            if (abs(deltaX) > abs(deltaY)) {
                var newCropLeft = min(cropLeft + abs(deltaX), cropRight - minCropWidth)
                var newCropTop = cropBottom - (cropRight - newCropLeft) / ratio!!
                if (newCropTop > cropBottom - minCropHeight) {
                    newCropTop = cropBottom - minCropHeight
                    newCropLeft = cropRight - minCropHeight * ratio!!
                    if (newCropLeft <= cropRight - minCropWidth) {
                        cropLeft = newCropLeft
                        cropTop = newCropTop
                    }
                } else {
                    cropLeft = newCropLeft
                    cropTop = newCropTop
                }
            } else {
                var newCropTop = min(cropTop + abs(deltaY), cropBottom - minCropHeight)
                var newCropLeft = cropRight - (cropBottom - newCropTop) * ratio!!
                if (newCropLeft > cropRight - minCropWidth) {
                    newCropLeft = cropRight - minCropWidth
                    newCropTop = cropBottom - minCropWidth / ratio!!
                    if (newCropTop <= cropBottom - minCropHeight) {
                        cropLeft = newCropLeft
                        cropTop = newCropTop
                    }
                } else {
                    cropLeft = newCropLeft
                    cropTop = newCropTop
                }
            }
        }
    }

    private fun expandUpRightWithRatio(touchX: Float, touchY: Float) {
        if (ratio == null) {
            return
        }
        val deltaX = touchX - oldX
        val deltaY = touchY - oldY
        if (deltaX > 0 && deltaY < 0) {
            if (abs(deltaX) > abs(deltaY)) {
                val newCropRight = min(cropRight + abs(deltaX), width.toFloat() - cropStrokeWidth * 3 / 2)
                val newCropTop = cropBottom - (newCropRight - cropLeft) / ratio!!
                if (newCropTop < cropStrokeWidth * 3 / 2) {
                    cropTop = cropStrokeWidth * 3 / 2
                    cropRight = cropLeft + (cropBottom - cropTop) * ratio!!
                } else {
                    cropRight = newCropRight
                    cropTop = newCropTop
                }
            } else {
                val newCropTop = max(cropTop - abs(deltaY), cropStrokeWidth * 3 / 2)
                val newCropRight = cropLeft + (cropBottom - newCropTop) * ratio!!
                if (newCropRight > width.toFloat() - cropStrokeWidth * 3 / 2) {
                    cropRight = width.toFloat() - cropStrokeWidth * 3 / 2
                    cropTop = cropBottom - (cropRight - cropLeft) / ratio!!
                } else {
                    cropRight = newCropRight
                    cropTop = newCropTop
                }
            }
        } else {
            if (abs(deltaX) > abs(deltaY)) {
                var newCropRight = max(cropRight - abs(deltaX), cropLeft + minCropWidth)
                var newCropTop = cropBottom - (newCropRight - cropLeft) / ratio!!
                if (newCropTop > cropBottom - minCropHeight) {
                    newCropTop = cropBottom - minCropHeight
                    newCropRight = cropLeft + minCropHeight * ratio!!
                    if (newCropRight >= cropLeft + minCropWidth) {
                        cropRight = newCropRight
                        cropTop = newCropTop
                    }
                } else {
                    cropRight = newCropRight
                    cropTop = newCropTop
                }
            } else {
                var newCropTop = min(cropTop + abs(deltaY), cropBottom - minCropHeight)
                var newCropRight = cropLeft + (cropBottom - newCropTop) * ratio!!
                if (newCropRight < cropLeft + minCropWidth) {
                    newCropRight = cropLeft + minCropWidth
                    newCropTop = cropBottom - minCropWidth / ratio!!
                    if (newCropTop <= cropBottom - minCropHeight) {
                        cropRight = newCropRight
                        cropTop = newCropTop
                    }
                } else {
                    cropRight = newCropRight
                    cropTop = newCropTop
                }
            }
        }
    }

    private fun expandDownWithRatio(touchY: Float) {
        if (ratio == null) return
        val deltaY = touchY - oldY
        if (deltaY > 0) {
            val newCropBottom = min(cropBottom + abs(deltaY), height.toFloat() - cropStrokeWidth * 3 / 2)
            val newCropLeft = cropLeft - abs(newCropBottom - cropBottom) * ratio!! / 2
            val newCropRight = cropRight + abs(newCropBottom - cropBottom) * ratio!! / 2
            if (newCropLeft >= cropStrokeWidth * 3 / 2 && newCropRight <= width.toFloat() - cropStrokeWidth * 3 / 2) {
                cropBottom = newCropBottom
                cropLeft = newCropLeft
                cropRight = newCropRight
            } else if (newCropLeft >= cropStrokeWidth * 3 / 2 && newCropRight > width.toFloat() - cropStrokeWidth * 3 / 2) {
                val cropWidth = newCropRight - newCropLeft
                cropRight = width.toFloat() - cropStrokeWidth * 3 / 2
                cropLeft = max(cropRight - cropWidth, cropStrokeWidth * 3 / 2)
                cropBottom = cropTop + (newCropRight - newCropLeft) / ratio!!
            } else if (newCropLeft < cropStrokeWidth * 3 / 2 && newCropRight <= width.toFloat() - cropStrokeWidth * 3 / 2) {
                val cropWidth = newCropRight - newCropLeft
                cropLeft = cropStrokeWidth * 3 / 2
                cropRight = min(cropLeft + cropWidth, width.toFloat() - cropStrokeWidth * 3 / 2)
                cropBottom = cropTop + (newCropRight - newCropLeft) / ratio!!
            } else {
                // do nothing
            }
        } else {
            val newCropBottom = max(cropBottom - abs(deltaY), minCropHeight)
            val newCropLeft = cropLeft + abs(cropBottom - newCropBottom) * ratio!! / 2
            val newCropRight = cropRight - abs(cropBottom - newCropBottom) * ratio!! / 2
            if (newCropRight - newCropLeft >= minCropWidth) {
                cropBottom = newCropBottom
                cropLeft = newCropLeft
                cropRight = newCropRight
            } else {
                // do nothing
            }
        }
    }

    private fun expandUpWithRatio(touchY: Float) {
        if (ratio == null) return
        val deltaY = touchY - oldY
        if (deltaY < 0) {
            val newCropTop = max(cropTop - abs(deltaY), cropStrokeWidth * 3 / 2)
            val newCropLeft = cropLeft - abs(newCropTop - cropTop) * ratio!! / 2
            val newCropRight = cropRight + abs(newCropTop - cropTop) * ratio!! / 2
            if (newCropLeft >= cropStrokeWidth * 3 / 2 && newCropRight <= width.toFloat() - cropStrokeWidth * 3 / 2) {
                cropTop = newCropTop
                cropLeft = newCropLeft
                cropRight = newCropRight
            } else if (newCropLeft >= cropStrokeWidth * 3 / 2 && newCropRight > width.toFloat() - cropStrokeWidth * 3 / 2) {
                val cropWidth = newCropRight - newCropLeft
                cropRight = width.toFloat() - cropStrokeWidth * 3 / 2
                cropLeft = max(cropRight - cropWidth, cropStrokeWidth * 3 / 2)
                cropTop = cropBottom - (newCropRight - newCropLeft) / ratio!!
            } else if (newCropLeft < cropStrokeWidth * 3 / 2 && newCropRight <= width.toFloat() - cropStrokeWidth * 3 / 2) {
                val cropWidth = newCropRight - newCropLeft
                cropLeft = cropStrokeWidth * 3 / 2
                cropRight = min(cropLeft + cropWidth, width.toFloat() - cropStrokeWidth * 3 / 2)
                cropTop = cropBottom - (newCropRight - newCropLeft) / ratio!!
            } else {
                // do nothing
            }
        } else {
            val newCropTop = min(cropTop + abs(deltaY), height.toFloat() - minCropHeight)
            val newCropLeft = cropLeft + abs(newCropTop - cropTop) * ratio!! / 2
            val newCropRight = cropRight - abs(newCropTop - cropTop) * ratio!! / 2
            if (newCropRight - newCropLeft >= minCropWidth) {
                cropTop = newCropTop
                cropLeft = newCropLeft
                cropRight = newCropRight
            } else {
                // do nothing
            }
        }
    }

    private fun expandLeftWithRatio(touchX: Float) {
        if (ratio == null) return
        val deltaX = touchX - oldX
        if (deltaX < 0) {
            val newCropLeft = max(cropLeft - abs(deltaX), cropStrokeWidth * 3 / 2)
            val newCropTop = cropTop - abs(newCropLeft - cropLeft) / ratio!! / 2
            val newCropBottom = cropBottom + abs(newCropLeft - cropLeft) / ratio!! / 2
            if (newCropTop >= cropStrokeWidth * 3 / 2 && newCropBottom <= height.toFloat() - cropStrokeWidth * 3 / 2) {
                cropLeft = newCropLeft
                cropTop = newCropTop
                cropBottom = newCropBottom
            } else if (newCropTop >= cropStrokeWidth * 3 / 2 && newCropBottom > height.toFloat() - cropStrokeWidth * 3 / 2) {
                val cropHeight = newCropBottom - newCropTop
                cropBottom = height.toFloat() - cropStrokeWidth * 3 / 2
                cropTop = max(cropBottom - cropHeight, cropStrokeWidth * 3 / 2)
                cropLeft = cropRight - (cropBottom - cropTop) * ratio!!
            } else if (newCropTop < cropStrokeWidth * 3 / 2 && newCropBottom <= height.toFloat() - cropStrokeWidth * 3 / 2) {
                val cropHeight = newCropBottom - newCropTop
                cropTop = cropStrokeWidth * 3 / 2
                cropBottom = min(cropTop + cropHeight, height.toFloat() - cropStrokeWidth * 3 / 2)
                cropLeft = cropRight - (cropBottom - cropTop) * ratio!!
            } else {
                // do nothing
            }
        } else {
            val newCropLeft = min(cropLeft + abs(deltaX), cropRight - minCropWidth)
            val newCropTop = cropTop + abs(newCropLeft - cropLeft) / ratio!! / 2
            val newCropBottom = cropBottom - abs(newCropLeft - cropLeft) / ratio!! / 2
            if (newCropBottom - newCropTop >= minCropHeight) {
                cropLeft = newCropLeft
                cropTop = newCropTop
                cropBottom = newCropBottom
            } else {
                // do nothing
            }
        }
    }

    private fun expandRightWithRatio(touchX: Float) {
        if (ratio == null) return
        val deltaX = touchX - oldX
        if (deltaX > 0) {
            val newCropRight = min(cropRight + abs(deltaX), width.toFloat() - cropStrokeWidth * 3 / 2)
            val newCropTop = cropTop - abs(newCropRight - cropRight) / ratio!! / 2
            val newCropBottom = cropBottom + abs(newCropRight - cropRight) / ratio!! / 2
            if (newCropTop >= cropStrokeWidth * 3 / 2 && newCropBottom <= height.toFloat() - cropStrokeWidth * 3 / 2) {
                cropRight = newCropRight
                cropTop = newCropTop
                cropBottom = newCropBottom
            } else if (newCropTop >= cropStrokeWidth * 3 / 2 && newCropBottom > height.toFloat() - cropStrokeWidth * 3 / 2) {
                val cropHeight = newCropBottom - newCropTop
                cropBottom = height.toFloat() - cropStrokeWidth * 3 / 2
                cropTop = max(cropBottom - cropHeight, cropStrokeWidth * 3 / 2)
                cropRight = cropLeft + (cropBottom - cropTop) * ratio!!
            } else if (newCropTop < cropStrokeWidth * 3 / 2 && newCropBottom <= height.toFloat() - cropStrokeWidth * 3 / 2) {
                val cropHeight = newCropBottom - newCropTop
                cropTop = cropStrokeWidth * 3 / 2
                cropBottom = min(cropTop + cropHeight, height.toFloat() - cropStrokeWidth * 3 / 2)
                cropRight = cropLeft + (cropBottom - cropTop) * ratio!!
            } else {
                // do nothing
            }
        } else {
            val newCropRight = max(cropRight - abs(deltaX), cropLeft + minCropWidth)
            val newCropTop = cropTop + abs(newCropRight - cropRight) / ratio!! / 2
            val newCropBottom = cropBottom - abs(newCropRight - cropRight) / ratio!! / 2
            if (newCropBottom - newCropTop >= minCropHeight) {
                cropRight = newCropRight
                cropTop = newCropTop
                cropBottom = newCropBottom
            } else {
                // do nothing
            }
        }
    }

    private fun move(touchX: Float, touchY: Float) {
        val newCropLeft = cropLeft + touchX - oldX
        val newCropRight = cropRight + touchX - oldX
        val newCropTop = cropTop + touchY - oldY
        val newCropBottom = cropBottom + touchY - oldY
        val cropWidth = cropRight - cropLeft
        val cropHeight = cropBottom - cropTop
        if (touchX - oldX < 0) {
            if (newCropLeft < cropStrokeWidth * 3 / 2) {
                cropLeft = cropStrokeWidth * 3 / 2
                cropRight = cropLeft + cropWidth
            } else {
                cropLeft = newCropLeft
                cropRight = newCropRight
            }
        } else {
            if (newCropRight > width.toFloat() - cropStrokeWidth * 3 / 2) {
                cropRight = width.toFloat() - cropStrokeWidth * 3 / 2
                cropLeft = cropRight - cropWidth
            } else {
                cropRight = newCropRight
                cropLeft = newCropLeft
            }
        }
        if (touchY - oldY < 0) {
            if (newCropTop < cropStrokeWidth * 3 / 2) {
                cropTop = cropStrokeWidth * 3 / 2
                cropBottom = cropTop + cropHeight
            } else {
                cropTop = newCropTop
                cropBottom = newCropBottom
            }
        } else {
            if (newCropBottom > height.toFloat() - cropStrokeWidth * 3 / 2) {
                cropBottom = height.toFloat() - cropStrokeWidth * 3 / 2
                cropTop = cropBottom - cropHeight
            } else {
                cropBottom = newCropBottom
                cropTop = newCropTop
            }
        }
    }

    private fun min(a: Float, b: Float): Float {
        return if (a < b) a else b
    }

    private fun max(a: Float, b: Float): Float {
        return if (a > b) a else b
    }

    private fun abs(a: Float): Float {
        return if (a > 0) a else -a
    }

    private fun initView(attributeSet: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attributeSet, R.styleable.CropView, 0, 0)
        if (ta.hasValue(R.styleable.CropView_cropStrokeColor)) {
            cropStrokeColor = ta.getColor(R.styleable.CropView_cropStrokeColor, getAppColor(R.color.color_purple))
            paint.color = cropStrokeColor
        }
        if (ta.hasValue(R.styleable.CropView_cropStrokeWidth)) {
            cropStrokeWidth = ta.getDimension(R.styleable.CropView_cropStrokeWidth, getAppDimension(R.dimen.dimen_2))
            paint.strokeWidth = cropStrokeWidth
        }
        if (ta.hasValue(R.styleable.CropView_cropMinWidth)) {
            minCropWidth = max(
                ta.getDimension(R.styleable.CropView_cropMinWidth, getAppDimension(R.dimen.dimen_100)),
                getAppDimension(R.dimen.dimen_100)
            )
        }
        if (ta.hasValue(R.styleable.CropView_cropMinHeight)) {
            minCropHeight = max(
                ta.getDimension(R.styleable.CropView_cropMinHeight, getAppDimension(R.dimen.dimen_100)),
                getAppDimension(R.dimen.dimen_100)
            )
        }
        if (ta.hasValue(R.styleable.CropView_cropNearDistance)) {
            near = min(
                ta.getDimension(R.styleable.CropView_cropNearDistance, getAppDimension(R.dimen.dimen_10)),
                getAppDimension(R.dimen.dimen_30)
            )
        }
        if (ta.hasValue(R.styleable.CropView_cropOutsideColor)) {
            cropOutsideColor = ta.getColor(R.styleable.CropView_cropOutsideColor, getAppColor(R.color.color_black_40))
        }
    }

    private fun drawOutside(canvas: Canvas?) {
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = cropOutsideColor

        canvas?.drawRect(0f, 0f, cropLeft, height.toFloat(), paint)
        canvas?.drawRect(cropRight, 0f, width.toFloat(), height.toFloat(), paint)
        canvas?.drawRect(cropLeft + cropStrokeWidth, 0f, cropRight - cropStrokeWidth, cropTop, paint)
        canvas?.drawRect(cropLeft + cropStrokeWidth, cropBottom, cropRight - cropStrokeWidth, height.toFloat(), paint)

        paint.style = Paint.Style.STROKE
        paint.color = cropStrokeColor
    }

    private fun drawMain(canvas: Canvas?) {
        val cropWidth = cropRight - cropLeft
        val cropHeight = cropBottom - cropTop

        // Vẽ hình chữ nhật rìa ngoài
        canvas?.drawRect(cropLeft, cropTop, cropLeft + cropWidth, cropTop + cropHeight, paint)

        // Vẽ các đường chia 3x3
        canvas?.drawLine(cropLeft + cropWidth / 3, cropTop, cropLeft + cropWidth / 3, cropTop + cropHeight, paint)
        canvas?.drawLine(cropLeft + cropWidth * 2 / 3, cropTop, cropLeft + cropWidth * 2 / 3, cropTop + cropHeight, paint)
        canvas?.drawLine(cropLeft, cropTop + cropHeight / 3, cropLeft + cropWidth, cropTop + cropHeight / 3, paint)
        canvas?.drawLine(cropLeft, cropTop + cropHeight * 2 / 3, cropLeft + cropWidth, cropTop + cropHeight * 2 / 3, paint)

        // Vẽ các góc giữa
        canvas?.drawLine(
            cropLeft - cropStrokeWidth,
            cropTop + cropHeight / 2 - getAppDimension(R.dimen.dimen_16) / 2,
            cropLeft - cropStrokeWidth,
            cropTop + cropHeight / 2 + getAppDimension(R.dimen.dimen_16) / 2,
            paint
        )
        canvas?.drawLine(
            cropLeft + cropWidth + cropStrokeWidth,
            cropTop + cropHeight / 2 - getAppDimension(R.dimen.dimen_16) / 2,
            cropLeft + cropWidth + cropStrokeWidth,
            cropTop + cropHeight / 2 + getAppDimension(R.dimen.dimen_16) / 2,
            paint
        )
        canvas?.drawLine(
            cropLeft + cropWidth / 2 - getAppDimension(R.dimen.dimen_28) / 2,
            cropTop - cropStrokeWidth,
            cropLeft + cropWidth / 2 + getAppDimension(R.dimen.dimen_28) / 2,
            cropTop - cropStrokeWidth,
            paint
        )
        canvas?.drawLine(
            cropLeft + cropWidth / 2 - getAppDimension(R.dimen.dimen_28) / 2,
            cropTop + cropHeight + cropStrokeWidth,
            cropLeft + cropWidth / 2 + getAppDimension(R.dimen.dimen_28) / 2,
            cropTop + cropHeight + cropStrokeWidth,
            paint
        )

        // Vẽ các góc vuông

        // Top left
        canvas?.drawLine(
            cropLeft - cropStrokeWidth,
            cropTop - cropStrokeWidth * 3 / 2,
            cropLeft - cropStrokeWidth,
            cropTop - cropStrokeWidth * 3 / 2 + getAppDimension(R.dimen.dimen_16),
            paint
        )
        canvas?.drawLine(
            cropLeft - cropStrokeWidth / 2,
            cropTop - cropStrokeWidth,
            cropLeft - cropStrokeWidth / 2 + getAppDimension(R.dimen.dimen_28),
            cropTop - cropStrokeWidth,
            paint
        )

        // Bot left
        canvas?.drawLine(
            cropLeft - cropStrokeWidth,
            cropTop + cropHeight + cropStrokeWidth * 3 / 2 - getAppDimension(R.dimen.dimen_16),
            cropLeft - cropStrokeWidth,
            cropTop + cropHeight + cropStrokeWidth * 3 / 2,
            paint
        )
        canvas?.drawLine(
            cropLeft - cropStrokeWidth / 2,
            cropTop + cropHeight + cropStrokeWidth,
            cropLeft - cropStrokeWidth / 2 + getAppDimension(R.dimen.dimen_28),
            cropTop + cropHeight + cropStrokeWidth,
            paint
        )

        // Top right
        canvas?.drawLine(
            cropLeft + cropWidth + cropStrokeWidth,
            cropTop - cropStrokeWidth * 3 / 2,
            cropLeft + cropWidth + cropStrokeWidth,
            cropTop - cropStrokeWidth * 3 / 2 + getAppDimension(R.dimen.dimen_16),
            paint
        )
        canvas?.drawLine(
            cropLeft + cropWidth + cropStrokeWidth / 2 - getAppDimension(R.dimen.dimen_28),
            cropTop - cropStrokeWidth,
            cropLeft + cropWidth + cropStrokeWidth / 2,
            cropTop - cropStrokeWidth,
            paint
        )


        // Bot right
        canvas?.drawLine(
            cropLeft + cropWidth + cropStrokeWidth,
            cropTop + cropHeight + cropStrokeWidth * 3 / 2 - getAppDimension(R.dimen.dimen_16),
            cropLeft + cropWidth + cropStrokeWidth,
            cropTop + cropHeight + cropStrokeWidth * 3 / 2,
            paint
        )
        canvas?.drawLine(
            cropLeft + cropWidth + cropStrokeWidth / 2 - getAppDimension(R.dimen.dimen_28),
            cropTop + cropHeight + cropStrokeWidth,
            cropLeft + cropWidth + cropStrokeWidth / 2,
            cropTop + cropHeight + cropStrokeWidth,
            paint
        )
    }

    enum class RectChangeType {
        EXPAND_RIGHT, EXPAND_LEFT, EXPAND_UP, EXPAND_DOWN,
        EXPAND_UP_RIGHT, EXPAND_UP_LEFT, EXPAND_DOWN_RIGHT, EXPAND_DOWN_LEFT,
        STAY, MOVE
    }
}
