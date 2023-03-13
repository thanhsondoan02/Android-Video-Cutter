package com.mobile.videocutter.presentation.widget.cutvideo

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.mobile.videocutter.R
import com.mobile.videocutter.base.extension.getAppColor
import com.mobile.videocutter.base.extension.getAppDrawable
import com.mobile.videocutter.base.extension.setOnSafeClick

class SelectButtonView(ctx: Context, attrs: AttributeSet?) : LinearLayout(ctx, attrs) {

    private lateinit var tvLeft: TextView
    private lateinit var tvRight: TextView
    private var leftText: String? = null
    private var rightText: String? = null

    private var onLeftClick: ((View) -> Unit)? = null
    private var onRightClick: ((View) -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.select_button_view, this, true)
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.CutVideoView, 0, 0)

        tvLeft = findViewById(R.id.tvSelectButtonLeft)
        tvRight = findViewById(R.id.tvSelectButtonRight)

        if (ta.hasValue(R.styleable.SelectButtonView_sbv_left_tv_text)) {
            leftText = ta.getString(R.styleable.SelectButtonView_sbv_left_tv_text)
        }

        if (ta.hasValue(R.styleable.SelectButtonView_sbv_right_tv_text)) {
            rightText = ta.getString(R.styleable.SelectButtonView_sbv_right_tv_text)
        }

        tvLeft.text = leftText
        tvRight.text = rightText

        tvLeft.setOnSafeClick {
            onLeftClick?.invoke(it)
        }

        tvRight.setOnSafeClick {
            onRightClick?.invoke(it)
        }
        ta.recycle()
    }

    fun clickLeft(onClick: ((View) -> Unit)?) {
        onLeftClick = onClick
        setBackgroundStatus(true)
    }

    fun clickRight(onClick: ((View) -> Unit)?) {
        onRightClick = onClick
        setBackgroundStatus(false)
    }

    fun setTextLeft(infoLeft: String) {
        tvLeft.text = infoLeft
    }

    fun setTextRight(infoRight: String) {
        tvRight.text = infoRight
    }

    private fun setBackgroundStatus(isCheck: Boolean) {
        if (isCheck) {
            tvLeft.background = getAppDrawable(R.color.color_purple)
            tvLeft.setTextColor(getAppColor(R.color.white))
            tvRight.background = getAppDrawable(R.color.white)
            tvRight.setTextColor(getAppColor(R.color.color_gray_60))
        } else {
            tvRight.background = getAppDrawable(R.color.color_purple)
            tvRight.setTextColor(getAppColor(R.color.white))
            tvLeft.background = getAppDrawable(R.color.white)
            tvLeft.setTextColor(getAppColor(R.color.color_gray_60))
        }
    }

}
