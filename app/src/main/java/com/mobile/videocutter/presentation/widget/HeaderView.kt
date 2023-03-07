package com.mobile.videocutter.presentation.widget

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.mobile.videocutter.R
import com.mobile.videocutter.base.extension.gone
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.base.extension.show

class HeaderView constructor(
    ctx: Context,
    attributeSet: AttributeSet?
) : ConstraintLayout(ctx, attributeSet) {

    // left
    private var ivLeft: ImageView? = null
    private var leftIc: Drawable? = null

    private var tvLeft: TextView? = null
    private var leftTvContent: CharSequence? = null
    private var leftTextSize: Float = 0f
    private var leftFont: Typeface? = null
    private var leftTextColor: Int = 0
    private var leftTvMargin: Float = 0f

    private var ivLeftOnClick: (() -> Unit)? = null

    // center
    private var llCenter: LinearLayout? = null

    private var tvCenter: TextView? = null
    private var centerTvContent: CharSequence? = null
    private var centerTextSize: Float = 0f
    private var centerFont: Typeface? = null
    private var centerTextColor: Int = 0

    private var ivCenter: ImageView? = null
    private var centerIc: Drawable? = null

    private var llCenterOnClick: (() -> Unit)? = null

    // right
    private var ivRight: ImageView? = null
    private var rightIc: Drawable? = null

    private var tvRight: TextView? = null
    private var rightTvContent: CharSequence? = null
    private var rightTextSize: Float = 0f
    private var rightFont: Typeface? = null
    private var rightTextColor: Int = 0
    private var rightTvMargin: Float? = 0f

    private var ivRightOnClick: (() -> Unit)? = null
    private var tvRightOnClick: (() -> Unit)? = null

    // view under
    private var vUnder: View? = null
    private var isShowViewUnder: Boolean = true

    private var newParams: android.view.ViewGroup.MarginLayoutParams? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.header_view_layout, this, true)
        initView(attributeSet)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        // ánh xạ view
        ivLeft = findViewById(R.id.ivHeaderViewLeft)
        tvLeft = findViewById(R.id.tvHeaderViewLeft)

        llCenter = findViewById(R.id.llHeaderViewCenter)
        ivCenter = findViewById(R.id.ivHeaderViewCenter)
        tvCenter = findViewById(R.id.tvHeaderViewCenter)

        ivRight = findViewById(R.id.ivHeaderViewRight)
        tvRight = findViewById(R.id.tvHeaderViewRight)

        vUnder = findViewById(R.id.vHeaderViewUnder)

        // view under line
        if (isShowViewUnder) {
            vUnder?.show()
        } else {
            vUnder?.gone()
        }

        // left
        if (leftIc == null) {
            ivLeft?.gone()
            if (leftTvMargin != 0f) {
                newParams = tvLeft?.layoutParams as MarginLayoutParams
                newParams?.leftMargin = leftTvMargin.toInt()
                tvLeft?.layoutParams = newParams
            }
        } else {
            ivLeft?.show()
            ivLeft?.setImageDrawable(leftIc)
        }

        if (!TextUtils.isEmpty(leftTvContent)) {
            tvLeft?.text = leftTvContent
        }

        if (leftTextColor != 0) {
            tvLeft?.setTextColor(leftTextColor)
        }

        if (leftTextSize != 0f) {
            tvLeft?.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize)
        }

        if (leftFont != null) {
            tvLeft?.typeface = centerFont
        }

        ivLeft?.setOnSafeClick {
            ivLeftOnClick?.invoke()
        }

        // center
        if (centerIc == null) {
            ivCenter?.gone()
        } else {
            ivCenter?.show()
            ivCenter?.setImageDrawable(centerIc)
        }

        if (!TextUtils.isEmpty(centerTvContent)) {
            tvCenter?.text = centerTvContent
        }

        if (centerTextColor != 0) {
            tvCenter?.setTextColor(centerTextColor)
        }

        if (centerTextSize != 0f) {
            tvCenter?.setTextSize(TypedValue.COMPLEX_UNIT_PX, centerTextSize)
        }

        if (centerFont != null) {
            tvCenter?.typeface = centerFont
        }

        llCenter?.setOnSafeClick {
            llCenterOnClick?.invoke()
        }

        // right
        if (rightIc == null) {
            ivRight?.gone()
            if (rightTvMargin != 0f) {
                newParams = tvRight?.layoutParams as MarginLayoutParams
                newParams?.rightMargin = rightTvMargin?.toInt()
                tvRight?.layoutParams = newParams
            }
        } else {
            ivRight?.show()
            ivRight?.setImageDrawable(rightIc)
        }

        if (!TextUtils.isEmpty(rightTvContent)) {
            tvRight?.text = rightTvContent
        }

        if (rightTextColor != 0) {
            tvRight?.setTextColor(rightTextColor)
        }

        if (rightTextSize != 0f) {
            tvRight?.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize)
        }

        if (rightFont != null) {
            tvRight?.typeface = rightFont
        }

        ivRight?.setOnSafeClick {
            ivRightOnClick?.invoke()
        }

        tvRight?.setOnSafeClick {
            tvRightOnClick?.invoke()
        }
    }

    private fun initView(attributeSet: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attributeSet, R.styleable.HeaderView, 0, 0)

        // left
        if (ta.hasValue(R.styleable.HeaderView_hv_left_ic)) {
            leftIc = ta.getDrawable(R.styleable.HeaderView_hv_left_ic)
        }

        if (ta.hasValue(R.styleable.HeaderView_hv_left_tv_text)) {
            leftTvContent = ta.getString(R.styleable.HeaderView_hv_left_tv_text)
        }

        if (ta.hasValue(R.styleable.HeaderView_hv_left_tv_margin)) {
            leftTvMargin = ta.getDimensionPixelOffset(R.styleable.HeaderView_hv_left_tv_margin, 0).toFloat()
        }

        if (ta.hasValue(R.styleable.HeaderView_hv_left_tv_text_color)) {
            leftTextColor = ta.getColor(R.styleable.HeaderView_hv_left_tv_text_color, 0)
        }

        if (ta.hasValue(R.styleable.HeaderView_hv_left_tv_text_size)) {
            leftTextSize = ta.getDimension(R.styleable.HeaderView_hv_left_tv_text_size, 0f)
        }

        if (ta.hasValue(R.styleable.HeaderView_hv_left_tv_font_family)) {
            val fontId = ta.getResourceId(R.styleable.HeaderView_hv_left_tv_font_family, -1)
            if (fontId != -1) {
                leftFont = ResourcesCompat.getFont(context, fontId)
            }
        }

        // center
        if (ta.hasValue(R.styleable.HeaderView_hv_center_ic)) {
            centerIc = ta.getDrawable(R.styleable.HeaderView_hv_center_ic)
        }

        if (ta.hasValue(R.styleable.HeaderView_hv_center_tv_text)) {
            centerTvContent = ta.getString(R.styleable.HeaderView_hv_center_tv_text)
        }

        if (ta.hasValue(R.styleable.HeaderView_hv_center_tv_text_color)) {
            centerTextColor = ta.getColor(R.styleable.HeaderView_hv_center_tv_text_color, 0)
        }

        if (ta.hasValue(R.styleable.HeaderView_hv_center_tv_text_size)) {
            centerTextSize = ta.getDimension(R.styleable.HeaderView_hv_center_tv_text_size, 0f)
        }

        if (ta.hasValue(R.styleable.HeaderView_hv_center_tv_font_family)) {
            val fontId = ta.getResourceId(R.styleable.HeaderView_hv_center_tv_font_family, -1)
            if (fontId != -1) {
                centerFont = ResourcesCompat.getFont(context, fontId)
            }
        }

        // right
        if (ta.hasValue(R.styleable.HeaderView_hv_right_ic)) {
            rightIc = ta.getDrawable(R.styleable.HeaderView_hv_right_ic)
        }

        if (ta.hasValue(R.styleable.HeaderView_hv_right_tv_text)) {
            rightTvContent = ta.getString(R.styleable.HeaderView_hv_right_tv_text)
        }

        if (ta.hasValue(R.styleable.HeaderView_hv_right_tv_margin)) {
            rightTvMargin = ta.getDimensionPixelOffset(R.styleable.HeaderView_hv_right_tv_margin, 0).toFloat()
        }

        if (ta.hasValue(R.styleable.HeaderView_hv_right_tv_text_color)) {
            rightTextColor = ta.getColor(R.styleable.HeaderView_hv_right_tv_text_color, 0)
        }

        if (ta.hasValue(R.styleable.HeaderView_hv_right_tv_text_size)) {
            rightTextSize = ta.getDimension(R.styleable.HeaderView_hv_right_tv_text_size, 0f)
        }

        if (ta.hasValue(R.styleable.HeaderView_hv_right_tv_font_family)) {
            val fontId = ta.getResourceId(R.styleable.HeaderView_hv_right_tv_font_family, -1)
            if (fontId != -1) {
                rightFont = ResourcesCompat.getFont(context, fontId)
            }
        }

        ta.recycle()
    }

    fun setVisibleViewUnderLine(isShow: Boolean) {
        isShowViewUnder = isShow
    }

    fun setOnLeftIconClickListener(onClick: (() -> Unit)?) {
        this.ivLeftOnClick = onClick
    }

    fun setOnRightIconClickListener(onClick: (() -> Unit)?) {
        this.ivRightOnClick = onClick
    }

    fun setOnRightTextClickListener(onClick: (() -> Unit)?) {
        this.tvRightOnClick = onClick
    }

    fun setOnCenterClickListener(onClick: (() -> Unit)?) {
        this.llCenterOnClick = onClick
    }

    fun setLeftIcon(@DrawableRes res: Int) {
        leftIc = ContextCompat.getDrawable(context, res)
        this.ivLeft?.setImageDrawable(leftIc)
    }

    fun showRightText(isShown: Boolean) {
        if (isShown) {
            this.tvRight?.visibility = VISIBLE
        } else {
            this.tvRight?.visibility = GONE
        }
    }

    fun setTextCenter(text: CharSequence?) {
        centerTvContent = text
        this.tvCenter?.text = centerTvContent
    }
}
