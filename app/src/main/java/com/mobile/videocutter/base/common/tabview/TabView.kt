package com.mobile.videocutter.base.common.tabview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.mobile.videocutter.R
import com.mobile.videocutter.base.extension.setOnSafeClick

class TabView constructor(
    ctx: Context,
    attrs: AttributeSet?
) : LinearLayout(ctx, attrs) {

    private var tvLeft: TextView? = null
    private var leftText: String? = null

    private var tvRight: TextView? = null
    private var rightText: String? = null

    private var isLeftSelected = true
    private var onLeftClick: (() -> Unit)? = null
    private var onRightClick: (() -> Unit)? = null

    init {
        LayoutInflater.from(ctx).inflate(R.layout.tab_view_layout, this, true)
        initView(attrs)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        tvLeft = findViewById(R.id.tvTabViewLeft)
        tvRight = findViewById(R.id.tvTabViewRight)

        if (leftText != null) {
            tvLeft?.text = leftText
        }

        if (rightText != null) {
            tvRight?.text = rightText
        }

        tvLeft?.setOnSafeClick {
            leftSelected()
        }

        tvRight?.setOnSafeClick {
            rightSelected()
        }
    }

    fun setLeftText(text: String?) {
        this.tvLeft?.text = text
    }

    fun setLeftText(charSequence: CharSequence?) {
        tvLeft?.text = charSequence
    }

    fun setRightText(text: String) {
        this.tvRight?.text = text
    }

    fun setRightText(charSequence: CharSequence?) {
        tvRight?.text = charSequence
    }

    fun setOnLeftClick(onClick: () -> Unit) {
        this.onLeftClick = onClick
    }

    fun setOnRightClick(onClick: () -> Unit) {
        this.onRightClick = onClick
    }

    fun select(isLeft: Boolean) {
        if (isLeft) {
            leftSelected()
        } else {
            rightSelected()
        }
    }

    private fun TextView.selectedLeft() {
        this.setTextAppearance(R.style.StyleRobotoLightWhite14)
        this.setTextColor(ContextCompat.getColor(context, R.color.color_white_tab_view))
        this.background = ContextCompat.getDrawable(context, R.drawable.shape_purple_tab_view_corner_left_4)
    }

    private fun TextView.unselectedLeft() {
        this.setTextAppearance(R.style.StyleRobotoLightPurple14)
        this.setTextColor(ContextCompat.getColor(context, R.color.color_purple_tab_view))
        this.background = ContextCompat.getDrawable(context, R.drawable.shape_white_tab_view_stroke_purple_tab_view_corner_left_4)
    }

    private fun TextView.selectedRight() {
        this.setTextAppearance(R.style.StyleRobotoLightWhite14)
        this.setTextColor(ContextCompat.getColor(context, R.color.color_white_tab_view))
        this.background = ContextCompat.getDrawable(context, R.drawable.shape_purple_tab_view_corner_right_4)
    }

    private fun TextView.unselectedRight() {
        this.setTextAppearance(R.style.StyleRobotoLightPurple14)
        this.setTextColor(ContextCompat.getColor(context, R.color.color_purple_tab_view))
        this.background = ContextCompat.getDrawable(context, R.drawable.shape_white_tab_view_stroke_purple_tab_view_corner_right_4)
    }

    private fun initView(attrs: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.TabView, 0, 0)

        if (ta.hasValue(R.styleable.TabView_tbv_left_text)) {
            leftText = ta.getString(R.styleable.TabView_tbv_left_text)
        }

        if (ta.hasValue(R.styleable.TabView_tbv_right_text)) {
            rightText = ta.getString(R.styleable.TabView_tbv_right_text)
        }

        ta.recycle()
    }

    private fun leftSelected() {
        if (!isLeftSelected) {
            tvLeft?.selectedLeft()
            tvRight?.unselectedRight()
            isLeftSelected = true

            onLeftClick?.invoke()
        }
    }

    private fun rightSelected() {
        if (isLeftSelected) {
            tvLeft?.unselectedLeft()
            tvRight?.selectedRight()
            isLeftSelected = false

            onRightClick?.invoke()
        }
    }
}
