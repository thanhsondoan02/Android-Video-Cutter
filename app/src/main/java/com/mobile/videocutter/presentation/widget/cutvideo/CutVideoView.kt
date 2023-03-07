package com.mobile.videocutter.presentation.widget.cutvideo

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginLeft
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import com.mobile.videocutter.R

class CutVideoView(
    ctx: Context,
    attrs: AttributeSet?
) : FrameLayout(ctx, attrs) {

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
    private lateinit var tvTimeEnd: TextView
    private lateinit var rvImageVideo: RecyclerView

    init {
        LayoutInflater.from(ctx).inflate(R.layout.cut_video_view, this, true)
        initView(attrs)
        moveTrimVideo()
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
        tvTimeEnd = findViewById(R.id.tvCutVideoTimeEnd)
        rvImageVideo = findViewById(R.id.rvCutViewRoot)

        ta.recycle()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun moveTrimVideo() {

        var coordinatesX = 0f
        var coordinatesBegin = 0f

        vStart.setOnTouchListener { view, event ->

            coordinatesX = event.rawX

            when (event.action) {

                MotionEvent.ACTION_DOWN -> {
                    coordinatesBegin = event.rawX
                }

                MotionEvent.ACTION_MOVE -> {
//                    vStart. = (coordinatesX - coordinatesBegin).toInt()
                    invalidate()
                }
            }
            true
        }
    }

}
