package com.mobile.videocutter.presentation.widget.cutvideo

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.recyclerview.widget.RecyclerView
import com.mobile.videocutter.R
import com.mobile.videocutter.base.extension.getAppColor
import com.mobile.videocutter.base.extension.getAppString
import com.mobile.videocutter.base.extension.gone
import com.mobile.videocutter.base.extension.show
import com.mobile.videocutter.domain.model.LocalVideo

class CutVideoView(ctx: Context, attrs: AttributeSet?) : FrameLayout(ctx, attrs) {

    private lateinit var flRoot: FrameLayout
    private lateinit var flFirst: FrameLayout
    private lateinit var flSecond: FrameLayout

    private lateinit var vStart: View
    private lateinit var vEnd: View
    private lateinit var vTop: View
    private lateinit var vBottom: View
    private lateinit var vSelectTime: View

    private lateinit var llSeekBarTime: LinearLayout
    private lateinit var tvTimeStart: TextView
    private lateinit var tvTimeCenter: TextView
    private lateinit var tvTimeEnd: TextView
    private lateinit var rvImageVideo: RecyclerView

    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    private lateinit var paramsTrimVideo: LayoutParams
    private lateinit var paramsSelectTime: LayoutParams
    private lateinit var paramsTimeBegin: LayoutParams
    private lateinit var paramsTimeEnd: LayoutParams

    private var coordinatesBegin = 0f
    private var coordinatesEnd = 0f

    private var coordinatesX = 0f
    private var marginLeftStart = 0f
    private var marginRightEnd = 0f

    private var marginLeftTrimStartDefault = 0f
    private var marginRightTrimEndDefault = 0f

    private var marginLeftSelectTimeDefault = 0f
    private var marginLeftTimeDefault = 0f

    private var marginRightSelectTimeDefault = 0f
    private var marginRightTimeDefault = 0f

    private var widthLayout: Int = 0
    private var totalTime: Long = 60000L
    private var timeStartTrim: Long = 0L
    private var timeCenterTrim: Long = 0L
    private var timeEndTrim: Long = totalTime

    init {
        LayoutInflater.from(ctx).inflate(R.layout.cut_video_view, this, true)
        initView(attrs)
        getParam()
        moveTrimVideoStart()
        moveTrimVideoEnd()
        moveSelectTime()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        widthLayout = flFirst.width
    }

    private fun initView(attrs: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.CutVideoView, 0, 0)

        flRoot = findViewById(R.id.flCutVideoRoot)
        flFirst = findViewById(R.id.flCutVideoFirst)
        flSecond = findViewById(R.id.flCutVideoSecond)

        vStart = findViewById(R.id.vCutVideoStart)
        vEnd = findViewById(R.id.vCutVideoEnd)
        vTop = findViewById(R.id.vCutVideoTop)
        vBottom = findViewById(R.id.vCutVideoBottom)
        vSelectTime = findViewById(R.id.vCutViewSelectTime)

        llSeekBarTime = findViewById(R.id.llCutViewTime)
        tvTimeStart = findViewById(R.id.tvCutVideoTimeStart)
        tvTimeCenter = findViewById(R.id.tvCutVideoTimeCenter)
        tvTimeEnd = findViewById(R.id.tvCutVideoTimeEnd)
        rvImageVideo = findViewById(R.id.rvCutViewRoot)

        Log.d("ACC", "initView: ${getTotalTimeVideo()}")

        ta.recycle()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun moveTrimVideoStart() {

        // kéo view trim begin
        vStart.setOnTouchListener { _, event ->
            when (event.action) {

                MotionEvent.ACTION_DOWN -> {
                    coordinatesBegin = event.rawX
                    marginLeftStart = flSecond.marginLeft.toFloat()
                }

                MotionEvent.ACTION_MOVE -> {
                    coordinatesX = event.rawX

                    var lengthLeft = coordinatesX - coordinatesBegin + marginLeftStart

                    if (inverseNumber(coordinatesX - coordinatesBegin) > touchSlop) {

                        if (lengthLeft < marginLeftTrimStartDefault) {
                            lengthLeft = marginLeftTrimStartDefault
                        }

                        if (lengthLeft > (widthLayout - 2 * vEnd.width - vSelectTime.width - paramsTrimVideo.rightMargin)) {
                            lengthLeft = (widthLayout - 2 * vEnd.width - vSelectTime.width - paramsTrimVideo.rightMargin).toFloat()
                        }

                        paramsTrimVideo.leftMargin = lengthLeft.toInt()
                        paramsSelectTime.leftMargin = (lengthLeft + marginLeftSelectTimeDefault).toInt()

                        flSecond.layoutParams = paramsTrimVideo

                        val timeStart = getTimeTrimConvertFromVideo(coordinatesX)

                        setTimeStart(timeStart)
                        showCountTime(false)
                        setBackGroundOutline(paramsTrimVideo.leftMargin.toFloat(), paramsTrimVideo.rightMargin.toFloat())
                        invalidate()

                    }
                }

                MotionEvent.ACTION_UP -> {
                    showCountTime(true)
                }
            }
            true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun moveTrimVideoEnd() {
        vEnd.setOnTouchListener { _, event ->
            when (event.action) {

                MotionEvent.ACTION_DOWN -> {
                    coordinatesEnd = event.rawX
                    marginRightEnd = flSecond.marginRight.toFloat()
                }

                MotionEvent.ACTION_MOVE -> {
                    coordinatesX = event.rawX

                    var lengthEnd = coordinatesEnd - coordinatesX + marginRightEnd

                    if (inverseNumber(coordinatesX - coordinatesEnd) > touchSlop) {

                        if (lengthEnd < marginRightTrimEndDefault) {
                            lengthEnd = marginRightTrimEndDefault
                        }

                        if (lengthEnd > (widthLayout - 2 * vEnd.width - vSelectTime.width - paramsTrimVideo.leftMargin)) {
                            lengthEnd = (widthLayout - 2 * vEnd.width - vSelectTime.width - paramsTrimVideo.leftMargin).toFloat()
                        }

                        paramsTrimVideo.rightMargin = lengthEnd.toInt()

                        paramsSelectTime.leftMargin = (marginLeftSelectTimeDefault + paramsTrimVideo.leftMargin).toInt()

                        flSecond.layoutParams = paramsTrimVideo

                        val timeEnd = getTimeTrimConvertFromVideo(coordinatesX)

                        setTimeEnd(timeEnd)
                        showCountTime(false)
                        setBackGroundOutline(paramsTrimVideo.leftMargin.toFloat(), paramsTrimVideo.rightMargin.toFloat())
                        invalidate()
                    }
                }

                MotionEvent.ACTION_UP -> {
                    showCountTime(true)
                }
            }
            true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun moveSelectTime() {
        vSelectTime.setOnTouchListener { _, event ->
            when (event.action) {

                MotionEvent.ACTION_DOWN -> {
                    coordinatesBegin = event.rawX
                    marginLeftStart = vSelectTime.marginLeft.toFloat()
                }

                MotionEvent.ACTION_MOVE -> {
                    coordinatesX = event.rawX

                    var lengthLeft = coordinatesX - coordinatesBegin + marginLeftStart

                    if (inverseNumber(coordinatesX - coordinatesBegin) > touchSlop) {

                        if (lengthLeft < (paramsTrimVideo.leftMargin + vStart.width + vSelectTime.width)) {
                            lengthLeft = (paramsTrimVideo.leftMargin + vStart.width + vSelectTime.width).toFloat()
                        }

                        if (lengthLeft >= (widthLayout - vEnd.width + vSelectTime.width - paramsTrimVideo.rightMargin)) {
                            lengthLeft = (widthLayout - vEnd.width + vSelectTime.width - paramsTrimVideo.rightMargin).toFloat()
                        }

                        paramsSelectTime.leftMargin = lengthLeft.toInt()

                        val timeCenter = getTimeTrimConvertFromVideo(lengthLeft)

                        setTimeCenter(timeCenter)
                        flSecond.layoutParams = paramsTrimVideo
                        invalidate()

                    }
                }
            }
            true
        }
    }

    private fun getTimeTrimConvertFromVideo(coordinatePosition: Float): Long {
        val totalLength = widthLayout - vStart.width - vEnd.width
        val timeTrim = ((coordinatePosition - vStart.width - vEnd.width) / totalLength) * getTotalTimeVideo()
        return if (timeTrim < 0) {
            0L
        } else {
            if (timeTrim > getTotalTimeVideo()) {
                getTotalTimeVideo()
            } else {
                timeTrim.toLong()
            }
        }
    }

    private fun getParam() {
        paramsTrimVideo = flSecond.layoutParams as LayoutParams
        paramsSelectTime = vSelectTime.layoutParams as LayoutParams
        paramsTimeBegin = tvTimeStart.layoutParams as LayoutParams
        paramsTimeEnd = tvTimeEnd.layoutParams as LayoutParams

        marginLeftTrimStartDefault = paramsTrimVideo.leftMargin.toFloat()
        marginLeftSelectTimeDefault = paramsSelectTime.leftMargin.toFloat()
        marginLeftTimeDefault = paramsTimeBegin.leftMargin.toFloat()

        marginRightTrimEndDefault = paramsTrimVideo.rightMargin.toFloat()
        marginRightSelectTimeDefault = paramsSelectTime.rightMargin.toFloat()
        marginRightTimeDefault = paramsTimeEnd.rightMargin.toFloat()
    }

    private fun setBackGroundOutline(marginLeft: Float, marginRight: Float) {
        if (marginLeft == marginLeftTrimStartDefault && marginRight == marginRightTrimEndDefault) {
            vStart.setBackgroundResource(R.drawable.ic_black_start)
            vEnd.setBackgroundResource(R.drawable.ic_black_end_point)
            vTop.setBackgroundColor(getAppColor(R.color.black, context))
            vBottom.setBackgroundColor(getAppColor(R.color.black, context))
        } else {
            vStart.setBackgroundResource(R.drawable.ic_yellow_start)
            vEnd.setBackgroundResource(R.drawable.ic_yellow_end)
            vTop.setBackgroundColor(getAppColor(R.color.color_yellow_gradient_end, context))
            vBottom.setBackgroundColor(getAppColor(R.color.color_yellow_gradient_end, context))
        }
    }

    private fun inverseNumber(number: Float): Float {
        return if (number > 0) number else -number
    }

    private fun showCountTime(checkTime: Boolean) {
        if (checkTime) {
            vSelectTime.show()
            tvTimeStart.show()
            tvTimeEnd.show()
            tvTimeCenter.gone()
            tvTimeStart.text = getTextTimeVideo(TIME_VIDEO_TYPE.TIME_START)
            tvTimeEnd.text = getTextTimeVideo(TIME_VIDEO_TYPE.TIME_END)
        } else {
            vSelectTime.gone()
            tvTimeStart.gone()
            tvTimeEnd.gone()
            tvTimeCenter.show()
            tvTimeCenter.text = getTextTimeVideo(TIME_VIDEO_TYPE.TIME_PERIOD)
        }
    }

    fun setTotalTimeVideo(totalTime: Long) {
        this.totalTime = totalTime
    }

    private fun getTotalTimeVideo(): Long {
        return totalTime
    }

    private fun setTimeStart(time: Long) {
        this.timeStartTrim = time
    }

    private fun getTimeStart(): Long {
        return timeStartTrim
    }

    private fun setTimeCenter(time: Long) {
        this.timeCenterTrim = time
    }

    fun getTimeCenter(): Long {
        return timeCenterTrim
    }

    private fun setTimeEnd(time: Long) {
        this.timeEndTrim = time
    }

    private fun getTimeEnd(): Long {
        return timeEndTrim
    }

    private fun getTextTimeVideo(type: TIME_VIDEO_TYPE): String {
        val localVideo = LocalVideo().apply {
            duration = when (type) {
                TIME_VIDEO_TYPE.TIME_START -> getTimeStart()
                TIME_VIDEO_TYPE.TIME_PERIOD -> getTimeEnd() - getTimeStart()
                TIME_VIDEO_TYPE.TIME_END -> getTimeEnd()
            }
        }
        return localVideo.getFormattedDuration()
    }
}
