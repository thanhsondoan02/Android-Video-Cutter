package com.mobile.videocutter.presentation.widget.cutvideo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
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
import com.mobile.videocutter.base.extension.gone
import com.mobile.videocutter.base.extension.show
import com.mobile.videocutter.domain.model.LocalVideo
import com.mobile.videocutter.presentation.widget.recyclerview.CustomRecyclerView
import com.mobile.videocutter.presentation.widget.recyclerview.LAYOUT_MANAGER_MODE

class CutVideoView(ctx: Context, attrs: AttributeSet?) : FrameLayout(ctx, attrs) {

    var listener: IListener? = null

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
    private lateinit var rvImageVideo: CustomRecyclerView

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
    private var widthRecyclerView: Int = 0
    private var heightRecyclerView: Int = 0

    private var totalTime: Long = 60000L
    private var timeStartTrim: Long = 0L
    private var timeCenterTrim: Long = 0L
    var timeEndTrim: Long = 0L

    private val adapter by lazy {
        CutVideoAdapter()
    }

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
        widthRecyclerView = rvImageVideo.width
        heightRecyclerView = rvImageVideo.layoutParams.height
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

        tvTimeStart = findViewById(R.id.tvCutVideoTimeStart)
        tvTimeCenter = findViewById(R.id.tvCutVideoTimeCenter)
        tvTimeEnd = findViewById(R.id.tvCutVideoTimeEnd)
        rvImageVideo = findViewById(R.id.rvCutViewRoot)

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

                    var timeStart = getTimeTrimConvertFromVideo(coordinatesX)

                    var lengthStart = coordinatesX - coordinatesBegin + marginLeftStart

                    if (inverseNumber(coordinatesX - coordinatesBegin) > touchSlop) {

                        if (lengthStart < marginLeftTrimStartDefault) {
                            lengthStart = marginLeftTrimStartDefault
                            timeStart = 500L
                        }

                        if (lengthStart > (widthLayout - 2 * vEnd.width - vSelectTime.width - paramsTrimVideo.rightMargin)) {
                            lengthStart = (widthLayout - 2 * vEnd.width - vSelectTime.width - paramsTrimVideo.rightMargin).toFloat()
                            timeStart = getTimeEnd()
                        }

                        paramsTrimVideo.leftMargin = lengthStart.toInt()
                        paramsSelectTime.leftMargin = (lengthStart + marginLeftSelectTimeDefault).toInt()

                        flSecond.layoutParams = paramsTrimVideo

                        setTimeStart(timeStart)
                        listener?.onTimeStart(timeStart.toInt())
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

                    var timeEnd = getTimeTrimConvertFromVideo(coordinatesX)

                    var lengthEnd = coordinatesEnd - coordinatesX + marginRightEnd

                    if (inverseNumber(coordinatesX - coordinatesEnd) > touchSlop) {

                        if (lengthEnd < marginRightTrimEndDefault) {
                            lengthEnd = marginRightTrimEndDefault
                            timeEnd = getTotalTimeVideo()
                        }

                        if (lengthEnd > (widthLayout - 2 * vEnd.width - vSelectTime.width - paramsTrimVideo.leftMargin)) {
                            lengthEnd = (widthLayout - 2 * vEnd.width - vSelectTime.width - paramsTrimVideo.leftMargin).toFloat()
                            timeEnd = getTimeStart()
                        }

                        paramsTrimVideo.rightMargin = lengthEnd.toInt()

                        paramsSelectTime.leftMargin = (widthLayout - 2 * vEnd.width - vSelectTime.width + marginLeftSelectTimeDefault - lengthEnd).toInt()

                        flSecond.layoutParams = paramsTrimVideo

                        setTimeEnd(timeEnd)
                        listener?.onTimeEnd(timeEnd.toInt())
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

                    var timeCenter = getTimeTrimConvertFromVideo(coordinatesX)

                    var lengthLeft = coordinatesX - coordinatesBegin + marginLeftStart

                    if (inverseNumber(coordinatesX - coordinatesBegin) > touchSlop) {

                        if (lengthLeft < (paramsTrimVideo.leftMargin + vStart.width + vSelectTime.width)) {
                            lengthLeft = (paramsTrimVideo.leftMargin + vStart.width + vSelectTime.width).toFloat()
                            timeCenter = getTimeStart()
                        }

                        if (lengthLeft >= (widthLayout - vEnd.width + vSelectTime.width - paramsTrimVideo.rightMargin)) {
                            lengthLeft = (widthLayout - vEnd.width + vSelectTime.width - paramsTrimVideo.rightMargin).toFloat()
                            timeCenter = getTimeEnd()
                        }

                        paramsSelectTime.leftMargin = lengthLeft.toInt()

                        setTimeCenter(timeCenter)
                        listener?.onTimeCenter(timeCenter.toInt())
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

    fun setBitmapListDisplay(bitmapList: List<Bitmap>) {
        Log.d("HEHE", "setBitmapListDisplay: ${bitmapList}")
        rvImageVideo.apply {
            submitList(bitmapList)
            setAdapter(adapter)
            setDragRecyclerView()
            setLayoutManagerMode(LAYOUT_MANAGER_MODE.LINEAR_HORIZATION)
        }
    }

    fun setConfigVideoBegin(totalTime: Long) {
        this.totalTime = totalTime
        this.timeEndTrim = totalTime
        tvTimeEnd.text = getTextTimeVideo(null, totalTime)
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

    fun getHeightListImage(): Int {
        return heightRecyclerView
    }

    fun getWidthListImage(): Int {
        return widthRecyclerView
    }

    private fun getTextTimeVideo(type: TIME_VIDEO_TYPE?, anyDuration: Long = 0L): String {
        val localVideo = LocalVideo().apply {
            duration = when (type) {
                TIME_VIDEO_TYPE.TIME_START -> getTimeStart()
                TIME_VIDEO_TYPE.TIME_PERIOD -> {
                    if (getTimeEnd() >= getTimeStart()) {
                        getTimeEnd() - getTimeStart()
                    } else {
                        0L
                    }
                }
                TIME_VIDEO_TYPE.TIME_END -> getTimeEnd()
                else -> anyDuration
            }
        }
        return localVideo.getFormattedDuration()
    }

    interface IListener {
        fun onTimeStart(timeStart: Int)
        fun onTimeCenter(timeCenter: Int)
        fun onTimeEnd(timeEnd: Int)
    }
}
