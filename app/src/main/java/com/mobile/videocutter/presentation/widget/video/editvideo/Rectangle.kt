package com.mobile.videocutter.presentation.widget.video.editvideo

class Rectangle {

    companion object {
        const val RADIUS_DEFAULT = 20f
    }

    var leftF: Float = 0f
    var topF: Float = 0f
    var rightF: Float = 0f
    var bottomF: Float = 0f
    var radiusLeftTop: Float = RADIUS_DEFAULT
    var radiusRightTop: Float = RADIUS_DEFAULT
    var radiusRightBottom: Float = RADIUS_DEFAULT
    var radiusLeftBottom: Float = RADIUS_DEFAULT
    var colorCode = COLOR_CODE.BLACK

    enum class COLOR_CODE {
        BLACK,
        WHITE,
        YELLOW,
        VIOLET
    }
}
