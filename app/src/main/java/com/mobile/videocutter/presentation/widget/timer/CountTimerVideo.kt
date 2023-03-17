package com.mobile.videocutter.presentation.widget.timer

import android.os.Handler
import android.os.Looper

class CountTimerVideo(var totalTime: Long, var stepTime: Long) {

    private var handler: Handler? = null

    private var timeSave: Long = 0

    private var runnable: Runnable? = null

    var listener: IListener? = null

    init {
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                timeSave += 1000
                listener?.onTick(timeSave)
                if (timeSave == totalTime) {
                    listener?.onFinish()
                    handler?.removeCallbacks(this)
                    timeSave = 0L
                }
                handler?.postDelayed(this, stepTime)
            }
        }
    }

    fun startVideo() {
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                timeSave += 1000
                listener?.onTick(timeSave)
                if (timeSave == totalTime) {
                    listener?.onFinish()
                    handler?.removeCallbacks(this)
                    timeSave = 0L
                }
                handler?.postDelayed(this, stepTime)
            }
        }
    }

    fun pauseVideo() {
        runnable?.let { handler?.removeCallbacks(it) }
    }

    fun stopVideo() {
        handler?.removeCallbacksAndMessages(null)
    }

    interface IListener {
        fun onTick(timer: Long)
        fun onFinish()
    }
}
