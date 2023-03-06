package com.mobile.videocutter.presentation.widget.video.speedvideo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.mobile.videocutter.R

class SpeedVideoView constructor(
    ctx: Context,
    attributeSet: AttributeSet?
) : ConstraintLayout(ctx, attributeSet) {

    private val TAG = "SpeedVideo"
    private var newParamsIVSelect: MarginLayoutParams? = null

    // khởi tạo
    private var flRoot: FrameLayout? = null

    private var ivSelect: ImageView? = null
    private var ivChose0Dot75X: ImageView? = null
    private var ivChose0Dot5X: ImageView? = null
    private var ivChose0Dot25X: ImageView? = null
    private var ivChose1X: ImageView? = null
    private var ivChose2X: ImageView? = null
    private var ivChose3X: ImageView? = null
    private var ivChose4X: ImageView? = null

    private var vUnSelect0At75X: View? = null
    private var vUnSelect0At5X: View? = null
    private var vUnSelect0At25X: View? = null
    private var vUnSelect1X: View? = null
    private var vUnSelect2X: View? = null
    private var vUnSelect3X: View? = null
    private var vUnSelect4X: View? = null

    private var tvUnSelect0At75X: TextView? = null
    private var tvUnSelect0At5X: TextView? = null
    private var tvUnSelect0At25X: TextView? = null
    private var tvUnSelect1X: TextView? = null
    private var tvUnSelect2X: TextView? = null
    private var tvUnSelect3X: TextView? = null
    private var tvUnSelect4X: TextView? = null

    // lấy tọa độ X của view
    private var flCoordinateX = 0

    private var ivSelectCoordinateX = 0
    private var ivNavigationCoordinateX = 0

    private var vUnSelect0At75CoordinateX = 0
    private var vUnSelect0At5CoordinateX = 0
    private var vUnSelect0At25CoordinateX = 0
    private var vUnSelect1CoordinateX = 0
    private var vUnSelect2CoordinateX = 0
    private var vUnSelect3CoordinateX = 0
    private var vUnSelect4CoordinateX = 0

    // tọa độ điểm chạm đầu tiên
    private var coordinateXFirst = 0f

    // margin left ban đầu của iv select
    private var originMarginIvSelect = 0

    // tạo biến để thay đổi trạng thái
    private var viewOld: View? = null
    private var textViewOld: TextView? = null
    private var ivOld: ImageView? = null

    private var widthParent = 0

    //khoảng cách giữa 2 speed
    private var distanceSpeed = 0

    private var listener: IListener? = null

    init {
        LayoutInflater.from(ctx).inflate(R.layout.speed_video_layout, this, true)
        mapView()
        initViewOrigin()
        eventView()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        widthParent = MeasureSpec.getSize(widthMeasureSpec)
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        flCoordinateX = getCoordinateXView(flRoot as View)

        ivSelectCoordinateX = getCoordinateXView(ivSelect as View)
        ivNavigationCoordinateX = getCoordinateXView(ivChose0Dot75X as View)

        vUnSelect0At75CoordinateX = getCoordinateXView(vUnSelect0At75X as View)
        vUnSelect0At5CoordinateX = getCoordinateXView(vUnSelect0At5X as View)
        vUnSelect0At25CoordinateX = getCoordinateXView(vUnSelect0At25X as View)
        vUnSelect1CoordinateX = getCoordinateXView(vUnSelect1X as View)
        vUnSelect2CoordinateX = getCoordinateXView(vUnSelect2X as View)
        vUnSelect3CoordinateX = getCoordinateXView(vUnSelect3X as View)
        vUnSelect4CoordinateX = getCoordinateXView(vUnSelect4X as View)

        widthParent -= vUnSelect1X!!.width
        distanceSpeed = vUnSelect4CoordinateX - vUnSelect3CoordinateX
    }

    private fun eventView() {
        dragSelectSpeed()
        selectSpeed()
    }

    private fun initViewOrigin() {
        vUnSelect1X!!.isVisible = false
        tvUnSelect1X!!.apply {
            setTextColor(Color.RED)
        }
        ivChose1X!!.isVisible = true
        viewOld = vUnSelect1X
        textViewOld = tvUnSelect1X
        ivOld = ivChose1X
    }

    private fun mapView() {
        flRoot = findViewById(R.id.imSpeedVideoBackground)

        ivSelect = findViewById(R.id.ivSpeedVideoSelected)
        ivChose0Dot75X = findViewById(R.id.ivSpeedVideoChose0_75X)
        ivChose0Dot5X = findViewById(R.id.ivSpeedVideoChose0_5X)
        ivChose0Dot25X = findViewById(R.id.ivSpeedVideoChose0_25X)
        ivChose1X = findViewById(R.id.ivSpeedVideoChose1X)
        ivChose2X = findViewById(R.id.ivSpeedVideoChose2X)
        ivChose3X = findViewById(R.id.ivSpeedVideoChose3X)
        ivChose4X = findViewById(R.id.ivSpeedVideoChose4X)

        vUnSelect0At75X = findViewById(R.id.vSpeedVideo0_75X)
        vUnSelect0At5X = findViewById(R.id.vSpeedVideo0_5X)
        vUnSelect0At25X = findViewById(R.id.vSpeedVideo0_25X)
        vUnSelect1X = findViewById(R.id.vSpeedVideo1X)
        vUnSelect2X = findViewById(R.id.vSpeedVideo2X)
        vUnSelect3X = findViewById(R.id.vSpeedVideo3X)
        vUnSelect4X = findViewById(R.id.vSpeedVideo4X)

        tvUnSelect0At75X = findViewById(R.id.tvSpeedVideo0_75X)
        tvUnSelect0At5X = findViewById(R.id.tvSpeedVideo0_5X)
        tvUnSelect0At25X = findViewById(R.id.tvSpeedVideo0_25X)
        tvUnSelect1X = findViewById(R.id.tvSpeedVideo1X)
        tvUnSelect2X = findViewById(R.id.tvSpeedVideo2X)
        tvUnSelect3X = findViewById(R.id.tvSpeedVideo3X)
        tvUnSelect4X = findViewById(R.id.tvSpeedVideo4X)

        newParamsIVSelect = ivSelect!!.layoutParams as MarginLayoutParams
    }

    private fun getCoordinateXView(view: View): Int {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        return location[0]
    }

    private fun selectSpeed0At75X() {
        setEvent0At75X()
        newParamsIVSelect!!.leftMargin = vUnSelect0At75CoordinateX - widthParent / 2
        ivSelect?.layoutParams = newParamsIVSelect
        listener?.setSpeedVideo0Dot75X()
    }

    private fun selectSpeed0At5X() {
        setEvent0At5X()
        newParamsIVSelect!!.leftMargin = vUnSelect0At5CoordinateX - widthParent / 2
        ivSelect?.layoutParams = newParamsIVSelect
        listener?.setSpeedVideo0Dot5X()
    }

    private fun selectSpeed0At25X() {
        setEvent0At25X()
        newParamsIVSelect!!.leftMargin = vUnSelect0At25CoordinateX - widthParent / 2
        ivSelect?.layoutParams = newParamsIVSelect
        listener?.setSpeedVideo0Dot25X()
    }

    private fun selectSpeed1X() {
        setEvent1X()
        newParamsIVSelect!!.leftMargin = vUnSelect1CoordinateX - widthParent / 2
        ivSelect?.layoutParams = newParamsIVSelect
        listener?.setSpeedVideo1X()
    }

    private fun selectSpeed2X() {
        setEvent2X()
        newParamsIVSelect!!.leftMargin = vUnSelect2CoordinateX - widthParent / 2
        ivSelect?.layoutParams = newParamsIVSelect
        listener?.setSpeedVideo2X()
    }

    private fun selectSpeed3X() {
        setEvent3X()
        newParamsIVSelect!!.leftMargin = vUnSelect3CoordinateX - widthParent / 2
        ivSelect?.layoutParams = newParamsIVSelect
        listener?.setSpeedVideo3X()
    }

    private fun selectSpeed4X() {
        setEvent4X()
        newParamsIVSelect!!.leftMargin = vUnSelect4CoordinateX - widthParent / 2
        ivSelect?.layoutParams = newParamsIVSelect
        listener?.setSpeedVideo4X()
    }

    private fun selectSpeed() {
        tvUnSelect0At75X?.setOnClickListener {
            selectSpeed0At75X()
        }

        tvUnSelect0At5X!!.setOnClickListener {
            selectSpeed0At5X()
        }

        tvUnSelect0At25X!!.setOnClickListener {
            selectSpeed0At25X()
        }

        tvUnSelect1X!!.setOnClickListener {
            selectSpeed1X()
        }

        tvUnSelect2X!!.setOnClickListener {
            selectSpeed2X()
        }

        tvUnSelect3X!!.setOnClickListener {
            selectSpeed3X()
        }

        tvUnSelect4X!!.setOnClickListener {
            selectSpeed4X()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun dragSelectSpeed() {

        ivSelect?.setOnTouchListener { iv, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    originMarginIvSelect = (iv.layoutParams as MarginLayoutParams).leftMargin
                    coordinateXFirst = event.rawX
                }

                MotionEvent.ACTION_MOVE -> {
                    val coordinateXSecond = event.rawX - coordinateXFirst

                    var expectedMarginLeft = originMarginIvSelect + coordinateXSecond

                    if (expectedMarginLeft <= vUnSelect0At75CoordinateX - widthParent / 2) {
                        expectedMarginLeft = (vUnSelect0At75CoordinateX - widthParent / 2).toFloat()
                    }
                    if (expectedMarginLeft >= vUnSelect4CoordinateX - widthParent / 2) {
                        expectedMarginLeft = (vUnSelect4CoordinateX - widthParent / 2).toFloat()
                    }

                    newParamsIVSelect!!.leftMargin = expectedMarginLeft.toInt()
                    iv?.layoutParams = newParamsIVSelect
                    when {
                        event.rawX <= vUnSelect0At75CoordinateX.toFloat() + distanceSpeed / 2 -> {
                            setEvent0At75X()
                        }

                        event.rawX <= vUnSelect0At5CoordinateX.toFloat() + distanceSpeed / 2 &&
                                event.rawX > vUnSelect0At5CoordinateX.toFloat() - distanceSpeed / 2 -> {
                            setEvent0At5X()
                        }

                        event.rawX <= vUnSelect0At25CoordinateX.toFloat() + distanceSpeed / 2 &&
                                event.rawX > vUnSelect0At25CoordinateX.toFloat() - distanceSpeed / 2 -> {
                            setEvent0At25X()
                        }

                        event.rawX <= vUnSelect1CoordinateX.toFloat() + distanceSpeed / 2 &&
                                event.rawX > vUnSelect1CoordinateX.toFloat() - distanceSpeed / 2 -> {
                            setEvent1X()
                        }

                        event.rawX <= vUnSelect2CoordinateX.toFloat() + distanceSpeed / 2 &&
                                event.rawX > vUnSelect2CoordinateX.toFloat() - distanceSpeed / 2 -> {
                            setEvent2X()
                        }

                        event.rawX <= vUnSelect3CoordinateX.toFloat() + distanceSpeed / 2 &&
                                event.rawX > vUnSelect3CoordinateX.toFloat() - distanceSpeed / 2 -> {
                            setEvent3X()
                        }

                        event.rawX >= vUnSelect4CoordinateX.toFloat() - distanceSpeed / 2 -> {
                            setEvent4X()
                        }
                    }
                    invalidate()
                }

                MotionEvent.ACTION_UP -> {
                    when {
                        event.rawX <= vUnSelect0At75CoordinateX.toFloat() + distanceSpeed / 2 -> {
                            selectSpeed0At75X()
                        }

                        event.rawX <= vUnSelect0At5CoordinateX.toFloat() + distanceSpeed / 2 &&
                                event.rawX > vUnSelect0At5CoordinateX.toFloat() - distanceSpeed / 2 -> {
                            selectSpeed0At5X()
                        }

                        event.rawX <= vUnSelect0At25CoordinateX.toFloat() + distanceSpeed / 2 &&
                                event.rawX > vUnSelect0At25CoordinateX.toFloat() - distanceSpeed / 2 -> {
                            selectSpeed0At25X()
                        }

                        event.rawX <= vUnSelect1CoordinateX.toFloat() + distanceSpeed / 2 &&
                                event.rawX > vUnSelect1CoordinateX.toFloat() - distanceSpeed / 2 -> {
                            selectSpeed1X()
                        }

                        event.rawX <= vUnSelect2CoordinateX.toFloat() + distanceSpeed / 2 &&
                                event.rawX > vUnSelect2CoordinateX.toFloat() - distanceSpeed / 2 -> {
                            selectSpeed2X()
                        }

                        event.rawX <= vUnSelect3CoordinateX.toFloat() + distanceSpeed / 2 &&
                                event.rawX > vUnSelect3CoordinateX.toFloat() - distanceSpeed / 2 -> {
                            selectSpeed3X()
                        }

                        event.rawX >= vUnSelect4CoordinateX.toFloat() - distanceSpeed / 2 -> {
                            selectSpeed4X()
                        }
                    }
                    invalidate()
                }
            }
            true
        }
    }

    private fun setEvent4X() {
        resetViewOld()
        vUnSelect4X?.isVisible = false
        tvUnSelect4X?.apply {
            setTextColor(Color.RED)
        }
        ivChose4X!!.isVisible = true
        viewOld = vUnSelect4X
        textViewOld = tvUnSelect4X
        ivOld = ivChose4X
    }

    private fun setEvent3X() {
        resetViewOld()
        vUnSelect3X?.isVisible = false
        tvUnSelect3X?.apply {
            setTextColor(Color.RED)
        }
        ivChose3X!!.isVisible = true
        viewOld = vUnSelect3X
        textViewOld = tvUnSelect3X
        ivOld = ivChose3X
    }

    private fun setEvent2X() {
        resetViewOld()
        vUnSelect2X?.isVisible = false
        tvUnSelect2X?.apply {
            setTextColor(Color.RED)
        }
        ivChose2X!!.isVisible = true
        viewOld = vUnSelect2X
        textViewOld = tvUnSelect2X
        ivOld = ivChose2X
    }

    private fun setEvent1X() {
        resetViewOld()
        vUnSelect1X?.isVisible = false
        tvUnSelect1X?.apply {
            setTextColor(Color.RED)
        }
        ivChose1X!!.isVisible = true
        viewOld = vUnSelect1X
        textViewOld = tvUnSelect1X
        ivOld = ivChose1X
    }

    private fun setEvent0At25X() {
        resetViewOld()
        vUnSelect0At25X?.isVisible = false
        tvUnSelect0At25X?.apply {
            setTextColor(Color.RED)
        }
        ivChose0Dot25X!!.isVisible = true
        viewOld = vUnSelect0At25X
        textViewOld = tvUnSelect0At25X
        ivOld = ivChose0Dot25X
    }

    private fun setEvent0At5X() {
        resetViewOld()
        vUnSelect0At5X?.isVisible = false
        tvUnSelect0At5X?.apply {
            setTextColor(Color.RED)
        }
        ivChose0Dot5X!!.isVisible = true
        viewOld = vUnSelect0At5X
        textViewOld = tvUnSelect0At5X
        ivOld = ivChose0Dot5X
    }

    private fun setEvent0At75X() {
        resetViewOld()
        vUnSelect0At75X?.isVisible = false
        tvUnSelect0At75X?.apply {
            setTextColor(Color.RED)
        }
        ivChose0Dot75X!!.isVisible = true
        viewOld = vUnSelect0At75X
        textViewOld = tvUnSelect0At75X
        ivOld = ivChose0Dot75X
    }

    private fun resetViewOld() {
        viewOld!!.isVisible = true
        textViewOld!!.apply {
            setTextColor(Color.BLACK)
        }
        ivOld!!.isVisible = false
    }

    interface IListener {
        fun setSpeedVideo0Dot75X()
        fun setSpeedVideo0Dot5X()
        fun setSpeedVideo0Dot25X()
        fun setSpeedVideo1X()
        fun setSpeedVideo2X()
        fun setSpeedVideo3X()
        fun setSpeedVideo4X()
    }
}
