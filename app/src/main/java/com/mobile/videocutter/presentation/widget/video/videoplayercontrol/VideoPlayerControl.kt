package com.mobile.videocutter.presentation.widget.video.videoplayercontrol

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.Player.*
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.mobile.videocutter.AppConfig
import com.mobile.videocutter.R
import com.mobile.videocutter.base.extension.*
import getFormattedTime


class VideoPlayerControl constructor(
    ctx: Context,
    attrs: AttributeSet?
) : ConstraintLayout(ctx, attrs) {
    private val TAG = "VideoPlayerControl"

    //left
    private var flLeft: FrameLayout? = null
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

    // player
    private var player: ExoPlayer? = null

    // control player
    private var _handler: Handler? = null
    private var _runnable: Runnable? = null
    private var progress = 0
    private var indexMediaSeekTo = 0

    // source player
    private var listUri: MutableList<Uri> = arrayListOf()

    private val window = Timeline.Window()

    private var concatenatingMediaSource: ConcatenatingMediaSource? = null

    // old media item index
    private var oldDurationDrag = 0L
    private var oldDurationStopDrag = 0L
    private var progressNew = 0L
    private var durationOld = 0L

    // callback
    var listener: IListener? = null
    var seeBarListener: IListener.ISeeBarListener? = null

    init {
        LayoutInflater.from(ctx).inflate(R.layout.video_player_control_layout, this, true)
        initView(attrs)
        _handler = Handler(Looper.getMainLooper())
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        flLeft = findViewById(R.id.flVideoPlayerControl)
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

        flLeft?.setOnSafeClick {
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

        sbSeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (seekBar != null && fromUser && player != null && concatenatingMediaSource != null) {
                    this@VideoPlayerControl.indexMediaSeekTo = seekToProgress(progress)
                    this@VideoPlayerControl.progress = progress
                    tvLeft?.text = getFormattedTime(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    _handler?.removeCallbacksAndMessages(null)
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null && player != null) {
                    oldDurationStopDrag = 0L
                    if (indexMediaSeekTo != 0) {
                        for (i in 0 until indexMediaSeekTo) {
                            oldDurationStopDrag += concatenatingMediaSource!!.initialTimeline.getWindow(i, window).durationMs
                        }
                        progressNew = this@VideoPlayerControl.progress - oldDurationStopDrag

                    } else {
                        progressNew = this@VideoPlayerControl.progress.toLong()
                    }
                    player?.seekToDefaultPosition(indexMediaSeekTo)
                    player?.seekTo(progressNew)
                    pausePlayer()
                    listener?.onPausePlayer(false)
                }
            }
        })
    }

    private fun seekToProgress(progress: Int): Int {
        oldDurationDrag = 0
        var index = 0
        for (i in 0 until concatenatingMediaSource!!.size) {
            if (progress > oldDurationDrag) {
                oldDurationDrag += concatenatingMediaSource!!.initialTimeline.getWindow(i, window).durationMs
                index = i
            }
        }
        return index
    }

    private fun buildMediaSource(uris: ArrayList<Uri>): ConcatenatingMediaSource {
        val dataSourceFactory = DefaultDataSource.Factory(this.context)

        val progressiveMediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
        val mediaSources = ArrayList<MediaSource>()

        for (uri in uris) {
            mediaSources.add(progressiveMediaSource.createMediaSource(MediaItem.fromUri(uri)))
        }

        val concatenatingMediaSource = ConcatenatingMediaSource()
        concatenatingMediaSource.addMediaSources(mediaSources)

        return concatenatingMediaSource
    }

    private fun disableView(){
        sbSeekBar?.disable()
        flLeft?.disable()
    }

    private fun enableView(){
        sbSeekBar?.enable()
        flLeft?.enable()
    }

    //    fun set
    fun initPlayer() {
        if (player == null) {
            player = ExoPlayer.Builder(this.context).build()
        }
    }

    fun setUrl(listUrl: List<String>, totalDuration: Long) {
        durationOld = 0L
        pausePlayer(STATE_ENDED)
        listUri.clear()

        if (listUrl.isNotEmpty()) {
            enableView()

            listUri = listUrl.map {
                Uri.parse(it)
            }.toMutableList()

            concatenatingMediaSource = buildMediaSource(listUri as ArrayList<Uri>)

            concatenatingMediaSource?.let {
                player?.setMediaSource(it)
            }

            player?.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    when (playbackState) {
                        STATE_READY -> {
                            tvRight?.text = getFormattedTime(totalDuration)
                            sbSeekBar?.max = totalDuration.toInt()

                            var oldDurationSeebar = 0L
                            if (indexMediaSeekTo > 0) {
                                for (i in 0 until indexMediaSeekTo) {
                                    oldDurationSeebar +=
                                        concatenatingMediaSource?.initialTimeline?.getWindow(i, window)?.durationMs
                                            ?: LONG_DEFAULT
                                }
                            }
                            if (durationOld == 0L) {
                                durationOld = oldDurationSeebar
                            }
                            _runnable = object : Runnable {
                                override fun run() {
                                    if (durationOld != C.TIME_UNSET) {
                                        tvLeft?.text = getFormattedTime(player!!.currentPosition + durationOld)
                                        sbSeekBar?.progress = (player!!.currentPosition + durationOld).toInt()
                                    }
                                    _handler?.postDelayed(this, AppConfig.TIME_CONFIG)
                                }
                            }
                        }

                        STATE_ENDED -> {
                            pausePlayer(STATE_ENDED)
                            listener?.onPlayerEnd(false)
                        }
                    }
                }

                override fun onPositionDiscontinuity(oldPosition: PositionInfo, newPosition: PositionInfo, reason: Int) {
                    super.onPositionDiscontinuity(oldPosition, newPosition, reason)
                    when {
                        newPosition.mediaItemIndex == 0 -> {
                            durationOld = 0L
                        }
                        newPosition.mediaItemIndex > 0 -> {
                            durationOld = 0L
                            for (i in 0 until newPosition.mediaItemIndex) {
                                durationOld += concatenatingMediaSource?.initialTimeline?.getWindow(i, window)?.durationMs
                                    ?: LONG_DEFAULT
                            }
                        }
                    }
                }
            })
            player?.prepare()

            listener?.onPlayerReady(player)
        } else {
            disableView()

            tvRight?.text = getAppString(R.string.time_start)
            stopPlayer()
            listener?.onPlayerReady(null)
        }
    }

    fun stopPlayer() {
        _handler?.removeCallbacksAndMessages(null)
        _handler = null

        sbSeekBar!!.progress = INT_DEFAULT
        player?.seekToDefaultPosition()
        player?.stop()
        player?.release()
        player = null
    }

    fun pausePlayer(state: Int = 0) {
        if (state == STATE_ENDED) {
            // reset player
            player?.seekTo(0)
            player?.playWhenReady = false
            player?.seekToDefaultPosition(0)
            // update ui khi end video
            tvLeft?.text = getAppString(R.string.time_start)
            sbSeekBar?.progress = INT_DEFAULT

            indexMediaSeekTo = 0
            durationOld = 0L
        }
        // xóa toàn bộ runable
        _handler?.removeCallbacksAndMessages(null)

        ivLeft?.setImageDrawable(getAppDrawable(R.drawable.ic_black_pause))
        player?.pause()
    }

    fun resumePlayer() {
        if (_runnable != null) {
            _handler?.postDelayed(_runnable!!, AppConfig.TIME_CONFIG)
        }
        ivLeft?.setImageDrawable(getAppDrawable(R.drawable.ic_black_play))
        player?.play()
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

        if (ta.hasValue(R.styleable.VideoPlayerControl_vpc_left_icon_show)) {
            isShowLeftIcon = ta.getBoolean(R.styleable.VideoPlayerControl_vpc_left_icon_show, true)
        }

        if (ta.hasValue(R.styleable.VideoPlayerControl_vpc_left_text)) {
            leftText = ta.getString(R.styleable.VideoPlayerControl_vpc_left_text)
        }

        if (ta.hasValue(R.styleable.VideoPlayerControl_vpc_left_text_show)) {
            isShowLeftText = ta.getBoolean(R.styleable.VideoPlayerControl_vpc_left_text_show, true)
        }

        if (ta.hasValue(R.styleable.VideoPlayerControl_vpc_seekbar_show)) {
            isShowSeekBar = ta.getBoolean(R.styleable.VideoPlayerControl_vpc_seekbar_show, true)
        }

        //right
        if (ta.hasValue(R.styleable.VideoPlayerControl_vpc_right_text)) {
            rightText = ta.getString(R.styleable.VideoPlayerControl_vpc_right_text)
        }

        if (ta.hasValue(R.styleable.VideoPlayerControl_vpc_right_text_show)) {
            isShowRightText = ta.getBoolean(R.styleable.VideoPlayerControl_vpc_right_text_show, true)
        }

        ta.recycle()
    }

    interface IListener {

        fun onPlayerReady(player: ExoPlayer?)
        fun onPlayerEnd(isPlay: Boolean)
        fun onPausePlayer(isPlay: Boolean)

        interface ISeeBarListener {
            fun onSeeBarProgreesListener(seekBar: SeekBar, progress: Int, fromUser: Boolean) {}
            fun onSeeBarStartTrackingTouch(seebar: SeekBar) {}
            fun onSeeBaronStopTrackingTouch(seekBar: SeekBar) {}
        }
    }
}
