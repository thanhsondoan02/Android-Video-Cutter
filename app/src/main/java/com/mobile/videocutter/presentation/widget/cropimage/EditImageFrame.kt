package com.mobile.videocutter.presentation.widget.cropimage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.mobile.videocutter.R
import com.mobile.videocutter.base.extension.getAppColor
import com.mobile.videocutter.util.UtilPaint

class EditImageFrame constructor(ctx: Context, attr: AttributeSet?) : View(ctx, attr) {

    private val TAG = "EditImageFrame"

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

    private var source = R.drawable.img_gray_no_data
    private var srcImage = BitmapFactory.decodeResource(resources, source)
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

    init {
        // khởi tạo để lấy chiều cao và chiều rộng của ảnh thật thông qua option
        // với inJustDecodeBounds = true sẽ trả về giá trị gốc và bitmap sẽ bằng null
        BitmapFactory.decodeResource(resources, R.drawable.img_gray_no_data, option)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        widthParent = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        heightParent = MeasureSpec.getSize(heightMeasureSpec).toFloat()

        wightImage = widthParent
        heightImage = if (option.outHeight > 5 / 6 * heightParent) {
            heightParent / 3
        } else {
            heightParent
        }

        coordinateDrawBitmap = heightParent / 2 - heightImage / 2
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        pos1X = (widthParent / 3)
        pos1Y = (heightImage)

        pos2X = (2 * widthParent / 3)
        pos2Y = (2 * heightImage)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // tạo ra một bản sao của bit map với các tỷ lệ mong muốn
        destImage = Bitmap.createScaledBitmap(srcImage, wightImage.toInt(), heightImage.toInt(), true)

        rectFBackground = RectF(0f, 0f, widthParent, heightParent)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        lengthEdgeReal = pos2Y - pos1Y
        withEdgeReal = pos2X - pos1X

        ratioRow = lengthEdgeReal / sizeRow
        ratioCol = withEdgeReal / sizeCol

        canvas?.apply {
            // vẽ ảnh
            drawBitmap(destImage!!, 0f, coordinateDrawBitmap, null)

            // vẽ lớp phủ lên ảnh
            drawRect(rectFBackground!!, paintBackground)

            // cắt phần một hình chữ nhật mong muốn
            clipRect(pos1X, pos1Y, pos2X, pos2Y)

            // vẽ lại ảnh đã cắt bởi clipRect
            drawBitmap(destImage!!, 0f, coordinateDrawBitmap, null)

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

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        return when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                isShowLines = true
                coordinatesFirstDownX = event.x
                coordinatesFirstDownY = event.y
                performClick()
                true
            }

            MotionEvent.ACTION_MOVE -> {
                offset1X = pos1X + (event.x - coordinatesFirstDownX)
                offset1Y = pos1Y + (event.y - coordinatesFirstDownY)
                offset2X = pos2X + (event.x - coordinatesFirstDownX)
                offset2Y = pos2Y + (event.y - coordinatesFirstDownY)

                // di chuyển và check xem nếu đến viền thì ko thay đổi tọa độ
                if (isMove() && isLimitScreen()) {
                    if (offset1X > moveLimit) {
                        pos2X = offset2X
                    }
                    if (offset1Y > heightImage) {
                        pos2Y = offset2Y
                    }
                    if (offset2Y < 2 * heightImage) {
                        pos1Y = offset1Y
                    }
                    if (offset2X < widthParent - moveLimit) {
                        pos1X = offset1X
                    }
                }
                if (isDragLeft() || isDragTop()) {
                    if (offset1Y >= heightImage && pos2Y > offset1Y + sizeDefault) {
                        pos1Y = offset1Y
                    }
                    if (offset1X >= moveLimit && pos2X > offset1X + sizeDefault) {
                        pos1X = offset1X
                    }
                }
                if (isDragBottom() || isDragRight()) {
                    if (offset2Y < 2 * heightImage && offset2Y > pos1Y + sizeDefault) {
                        pos2Y = offset2Y
                    }

                    if (offset2X < widthParent - moveLimit && offset2X > pos1X + sizeDefault) {
                        pos2X = offset2X
                    }
                }

                coordinatesFirstDownX = event.x
                coordinatesFirstDownY = event.y

                invalidate()
                true
            }

            MotionEvent.ACTION_UP -> {
                isShowLines = false
                coordinatesFirstDownX = event.x
                coordinatesFirstDownY = event.y
                invalidate()
                true
            }
            else -> false
        }
    }

    private fun isMove(): Boolean {
        return coordinatesFirstDownX > pos1X + paddingEdgeDefault &&
                coordinatesFirstDownX < pos2X - paddingEdgeDefault &&
                coordinatesFirstDownY > pos1Y + paddingEdgeDefault &&
                coordinatesFirstDownY < pos2Y
    }

    private fun isLimitScreen(): Boolean {
        return offset1X >= moveLimit && offset1Y >= heightImage &&
                offset2X <= widthParent - moveLimit && offset2Y <= 2 * heightImage
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
