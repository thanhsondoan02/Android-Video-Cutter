package com.mobile.videocutter.presentation.widget.cropimage

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.mobile.videocutter.R
import com.mobile.videocutter.base.extension.getAppColor
import com.mobile.videocutter.util.UtilPaint
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class EditImageFrame constructor(ctx: Context, attr: AttributeSet?) : View(ctx, attr) {

    private val TAG = "EditImageFrame"

    companion object {
        private const val URL_IMAGE_DEFAULT = "https://cdn.vjshop.vn/tin-tuc/do-phan-giai-8k-la-gi/8k-la-gi-7.png"
    }

    private val paintShape = UtilPaint().getPaintStroke().apply {
        this.color = getAppColor(R.color.white)
        this.strokeWidth = 2f
    }

    private val paintCorner = UtilPaint().getPainStrokeCapRound().apply {
        this.color = getAppColor(R.color.white)
        this.strokeWidth = 8f
    }

    private val paintLine = UtilPaint().getPainStrokeCapRound().apply {
        this.color = getAppColor(R.color.white)
        this.strokeWidth = 1f
    }

    private val paintBackground = UtilPaint().getPaintFill().apply {
        this.color = getAppColor(R.color.color_transparent_dark_90)
    }

    private var widthParent = 0f
    private var heightParent = 0f

    private var option = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
    }

    private var source: String = URL_IMAGE_DEFAULT
    private var srcImage: Bitmap? = null
    private var destImage: Bitmap? = null

    private var wightImage = 0f
    private var heightImage = 0f

    private var rectFBackground: RectF? = null
    private var coordinateDrawBitmap = 0f

    // tọa độ các điểm của hình chứ nhật
    private var pos1X = 0f
    private var pos1Y = 0f

    private var pos2X = 0f
    private var pos2Y = 0f

    // số lương của đường kẻ mong muốn
    private var sizeRow = 3
    private var sizeCol = 3

    // kích thước chiều dài chiều rộng hình chữ nhật
    private var lengthEdgeReal = 0f
    private var withEdgeReal = 0f

    // tỷ lệ 1 ô theo hàng và cột
    private var ratioRow = 0f
    private var ratioCol = 0f

    // tịnh tiến theo cử chỉ ngón tay
    private var offset1X = 0f
    private var offset1Y = 0f
    private var offset2X = 0f
    private var offset2Y = 0f

    // tọa độ điểm chạm đầu tiên
    private var coordinatesFirstDownX = 0f
    private var coordinatesFirstDownY = 0f

    // kích thước mặc định của hình chữ nhật ko đc thu nhỏ nữa
    private var sizeDefault = 300f

    // khoảng giới hạn tối thiểu có thể di chuyển
    private var moveLimit = 2f

    // vẽ thêm chiều của 4 góc
    private var cornerDefault = 50f

    // kích thước để dễ thao tác kéo ở các cạnh
    private var paddingEdgeDefault = 50f

    // hiển thị các đường kẻ bên trong
    private var isShowLines = false

    // mảng đường thẳng
    private var linesTopLeft = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
    private var linesTopRight = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
    private var linesBottomLeft = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
    private var linesBottomRight = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)

    private var startXDrawBitmap = 0f
    private var startYDrawBitmap = 0f

    private var ratioWidth = 0
    private var ratioHeight = 0

    init {
        Log.d(TAG, "init: ")
        setResource(null)
    }

    fun setRatio(width: Int, height: Int) {
        this.ratioWidth = width
        this.ratioHeight = height
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        widthParent = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        heightParent = MeasureSpec.getSize(heightMeasureSpec).toFloat()

        wightImage = widthParent

        coordinateDrawBitmap = heightParent / 2 - heightImage / 2
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (wightImage < widthParent) {
            // startXDrawBitmap = widthParent / 2 - wightImage / 2
        }

        if (heightImage < heightParent) {
            startYDrawBitmap = heightParent / 2 - heightImage / 2
        }

        pos1X = 0f
        pos1Y = startYDrawBitmap

        pos2X = widthParent
        pos2Y = startYDrawBitmap + heightImage
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        Log.d(TAG, "onSizeChanged: ")

        if (srcImage != null) {
            destImage = Bitmap.createScaledBitmap(srcImage!!, wightImage.toInt(), heightImage.toInt(), true)
        }
        rectFBackground = RectF(0f, 0f, widthParent, heightParent)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        Log.d(TAG, "onDraw: ")

        lengthEdgeReal = pos2Y - pos1Y
        withEdgeReal = pos2X - pos1X

        ratioRow = lengthEdgeReal / sizeRow
        ratioCol = withEdgeReal / sizeCol

        canvas?.apply {
            // vẽ ảnh
            if (destImage != null) {
                drawBitmap(destImage!!, startXDrawBitmap, startYDrawBitmap, null)
            }

            // vẽ lớp phủ lên ảnh
            if (rectFBackground != null) {
                drawRect(rectFBackground!!, paintBackground)
            }

            // cắt phần một hình chữ nhật mong muốn
            clipRect(pos1X, pos1Y, pos2X, pos2Y)

            // vẽ lại ảnh đã cắt bởi clipRect
            if (destImage != null) {
                drawBitmap(destImage!!, startXDrawBitmap, startYDrawBitmap, null)
            }

            // vẽ thêm các chi tiết cho hình chữ nhật đã cắt
            drawRect(pos1X, pos1Y, pos2X, pos2Y, paintShape)

            // bắt đầu 4 góc
            linesTopLeft[0] = pos1X
            linesTopLeft[1] = pos1Y
            linesTopLeft[2] = pos1X + cornerDefault
            linesTopLeft[3] = pos1Y
            linesTopLeft[4] = pos1X
            linesTopLeft[5] = pos1Y
            linesTopLeft[6] = pos1X
            linesTopLeft[7] = pos1Y + cornerDefault
            drawLines(linesTopLeft, paintCorner)

            linesTopRight[0] = pos2X
            linesTopRight[1] = pos1Y
            linesTopRight[2] = pos2X - cornerDefault
            linesTopRight[3] = pos1Y
            linesTopRight[4] = pos2X
            linesTopRight[5] = pos1Y
            linesTopRight[6] = pos2X
            linesTopRight[7] = pos1Y + cornerDefault
            drawLines(linesTopRight, paintCorner)

            linesBottomRight[0] = pos2X
            linesBottomRight[1] = pos2Y
            linesBottomRight[2] = pos2X
            linesBottomRight[3] = pos2Y - cornerDefault
            linesBottomRight[4] = pos2X
            linesBottomRight[5] = pos2Y
            linesBottomRight[6] = pos2X - cornerDefault
            linesBottomRight[7] = pos2Y
            drawLines(linesBottomRight, paintCorner)

            linesBottomLeft[0] = pos1X
            linesBottomLeft[1] = pos2Y
            linesBottomLeft[2] = pos1X
            linesBottomLeft[3] = pos2Y - cornerDefault
            linesBottomLeft[4] = pos1X
            linesBottomLeft[5] = pos2Y
            linesBottomLeft[6] = pos1X + cornerDefault
            linesBottomLeft[7] = pos2Y
            drawLines(linesBottomLeft, paintCorner)
            // kết thúc 4 góc

            if (isShowLines) {
                // vẽ các hàng
                for (i in 1 until sizeRow) {
                    drawLine(pos1X, ratioRow * i + pos1Y, pos2X, ratioRow * i + pos1Y, paintLine)
                }

                // vẽ các cột
                for (i in 1 until sizeCol) {
                    drawLine(ratioCol * i + pos1X, pos1Y, ratioCol * i + pos1X, pos2Y, paintLine)
                }
            }
        }
    }

    fun setResource(url: String?) {
        if (url != null) {
            source = url
        }

        srcImage = getBitmapFromURL(source)

        val bitmapNotScale = srcImage?.copy(Bitmap.Config.ARGB_8888, true)

        bitmapNotScale?.let {
            wightImage = bitmapNotScale.width.toFloat()
            heightImage = bitmapNotScale.height.toFloat()
        }


    }

    private fun getBitmapFromURL(src: String?): Bitmap? {
        var bitmap: Bitmap? = null
        val thread = Thread {
            try {
                val url = URL(src)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input: InputStream = connection.inputStream
                bitmap = BitmapFactory.decodeStream(input)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                bitmap = null
            }
        }
        thread.start()
        thread.join()
        return bitmap
    }

    private fun getImageDimensions(imagePath: String?): IntArray {
        // khởi tạo để lấy chiều cao và chiều rộng của ảnh thật thông qua option
        // với inJustDecodeBounds = true sẽ trả về giá trị gốc và bitmap sẽ bằng null
        BitmapFactory.decodeFile(imagePath, option)
        val imageHeight = option.outHeight
        val imageWidth = option.outWidth
        return intArrayOf(imageWidth, imageHeight)
    }

    private fun isMove(): Boolean {
        return coordinatesFirstDownX > pos1X + paddingEdgeDefault &&
                coordinatesFirstDownX < pos2X - paddingEdgeDefault &&
                coordinatesFirstDownY > pos1Y + paddingEdgeDefault &&
                coordinatesFirstDownY < pos2Y
    }

    private fun isLimitScreen(): Boolean {


        
        return true
    }

    private fun isDragTop(): Boolean {
        return coordinatesFirstDownX > pos1X &&
                coordinatesFirstDownX < pos2X &&
                coordinatesFirstDownY > pos1Y - paddingEdgeDefault &&
                coordinatesFirstDownY < pos1Y + paddingEdgeDefault
    }

    private fun isDragLeft(): Boolean {
        return coordinatesFirstDownX > pos1X - paddingEdgeDefault &&
                coordinatesFirstDownX < pos1X + paddingEdgeDefault &&
                coordinatesFirstDownY - pos1Y > 0 &&
                coordinatesFirstDownY - pos1Y < lengthEdgeReal
    }

    private fun isDragRight(): Boolean {
        return coordinatesFirstDownX > pos2X - paddingEdgeDefault &&
                coordinatesFirstDownX < pos2X + paddingEdgeDefault &&
                coordinatesFirstDownY - pos1Y > 0 &&
                coordinatesFirstDownY - pos1Y < lengthEdgeReal
    }

    private fun isDragBottom(): Boolean {
        return coordinatesFirstDownX - pos1X > 0 &&
                coordinatesFirstDownX - pos1X < withEdgeReal &&
                coordinatesFirstDownY > pos2Y - paddingEdgeDefault &&
                coordinatesFirstDownY < pos2Y + paddingEdgeDefault
    }
}
