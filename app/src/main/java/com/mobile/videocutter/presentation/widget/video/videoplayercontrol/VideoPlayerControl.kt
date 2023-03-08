package com.mobile.videocutter.presentation.widget.video.videoplayercontrol

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.mobile.videocutter.R
import com.mobile.videocutter.base.extension.gone
import com.mobile.videocutter.base.extension.setOnSafeClick
import com.mobile.videocutter.base.extension.show

class VideoPlayerControl constructor(
    ctx: Context,
    attrs: AttributeSet?
) : ConstraintLayout(ctx, attrs) {

    //left
    private var ivLeft: ImageView? = null
    private var leftIcon: Drawable? = null
    private var isShowLeftIcon: Boolean = false

    private var tvLeft: TextView? = null
    private var leftText: CharSequence? = null
    private var isShowLeftText: Boolean = false

    private var onLeftIconClick: (() -> Unit)? = null

    //center
    private var sbSeekBar: SeekBar? = null
    private var isShowSeekBar: Boolean = false

    private var onSeekBarClick: (() -> Unit)? = null

    // right
    private var tvRight: TextView? = null
    private var rightText: CharSequence? = null
    private var isShowRightText: Boolean = false

    init {
        LayoutInflater.from(context).inflate(R.layout.video_player_control, this, true)
        initView(attrs)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        ivLeft = findViewById(R.id.ivVideoPlayerControlLeftIcon)
        tvLeft = findViewById(R.id.tvVideoPlayerControlLeftText)

        sbSeekBar = findViewById(R.id.sbVideoPlayerControlSeekBar)

        tvRight = findViewById(R.id.tvVideoPlayerControlRightText)

        //left
        if (isShowLeftIcon) {
            ivLeft?.show()
        } else {
            ivLeft?.gone()
        }

        if (leftIcon != null) {
            ivLeft?.setImageDrawable(leftIcon)
            ivLeft?.show()
        } else {
            ivLeft?.gone()
        }

        if (!TextUtils.isEmpty(leftText)) {
            tvLeft?.text = leftText
        }

        if (isShowLeftText) {
            tvLeft?.show()
        } else {
            tvLeft?.gone()
        }

        ivLeft?.setOnSafeClick {
            onLeftIconClick?.invoke()
        }

        //center
        if (sbSeekBar == null) {
            sbSeekBar?.gone()
        } else {
            sbSeekBar?.show()
        }

        if (isShowSeekBar) {
            sbSeekBar?.show()
        } else {
            sbSeekBar?.gone()
        }

        sbSeekBar?.setOnSafeClick {
            onSeekBarClick?.invoke()
        }

        //right
        if (!TextUtils.isEmpty(rightText)) {
            tvRight?.text = rightText
        }

        if (isShowRightText) {
            tvRight?.show()
        } else {
            tvRight?.gone()
        }
    }

    fun setLeftText(text: CharSequence?) {
        leftText = text
        this.tvLeft?.text = leftText
    }

    fun setLeftText(text: String?) {
        this.tvLeft?.text = text
    }

    fun showLeftText(isShow: Boolean) {
        if (isShow) {
            this.tvLeft?.show()
        } else {
            this.tvLeft?.gone()
        }
    }

    fun showLeftIcon(isShow: Boolean) {
        if (isShow) {
            this.ivLeft?.show()
        } else {
            this.ivLeft?.gone()
        }
    }

    fun showSeekBar(isShow: Boolean) {
        if (isShow) {
            this.sbSeekBar?.show()
        } else {
            this.sbSeekBar?.gone()
        }
    }

    fun setRightText(text: CharSequence?) {
        rightText = text
        this.tvRight?.text = rightText
    }

    fun setRightText(text: String?) {
        this.tvRight?.text = text
    }

    fun showRightText(isShow: Boolean) {
        if (isShow) {
            this.tvRight?.show()
        } else {
            this.tvRight?.gone()
        }
    }

    fun setLeftIcon(res: Int) {
        leftIcon = ContextCompat.getDrawable(context, res)
        this.ivLeft?.setImageDrawable(leftIcon)
    }

    fun setOnLeftIconClickListener(onClick: (() -> Unit)?) {
        this.onLeftIconClick = onClick
    }

    fun setOnSeekBarClickListener(onClick: (() -> Unit)?) {
        this.onSeekBarClick = onClick
    }

    private fun initView(attrs: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.VideoPlayerControl, 0, 0)

        // left
        if (ta.hasValue(R.styleable.VideoPlayerControl_vpc_left_icon)) {
            leftIcon = ta.getDrawable(R.styleable.VideoPlayerControl_vpc_left_icon)
        }

        if (ta.hasValue(R.styleable.VideoPlayerControl_vpc_show_left_icon)) {
            isShowLeftIcon = ta.getBoolean(R.styleable.VideoPlayerControl_vpc_show_left_icon, true)
        }

        if (ta.hasValue(R.styleable.VideoPlayerControl_vpc_left_text)) {
            leftText = ta.getString(R.styleable.VideoPlayerControl_vpc_left_text)
        }

        if (ta.hasValue(R.styleable.VideoPlayerControl_vpc_show_left_text)) {
            isShowLeftText = ta.getBoolean(R.styleable.VideoPlayerControl_vpc_show_left_text, true)
        }

        if (ta.hasValue(R.styleable.VideoPlayerControl_vpc_show_seekbar)) {
            isShowSeekBar = ta.getBoolean(R.styleable.VideoPlayerControl_vpc_show_seekbar, true)
        }

        //right
        if (ta.hasValue(R.styleable.VideoPlayerControl_vpc_right_text)) {
            rightText = ta.getString(R.styleable.VideoPlayerControl_vpc_right_text)
        }

        if (ta.hasValue(R.styleable.VideoPlayerControl_vpc_show_right_text)) {
            isShowRightText = ta.getBoolean(R.styleable.VideoPlayerControl_vpc_show_right_text, true)
        }

        ta.recycle()
    }
}
