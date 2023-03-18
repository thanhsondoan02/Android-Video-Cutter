package com.mobile.videocutter.domain.model

import com.mobile.videocutter.R
import com.mobile.videocutter.base.extension.getAppString

data class ToolVideo(val type: TOOL_VIDEO_TYPE) {
    fun getImage(): Int {
        return when (type) {
            TOOL_VIDEO_TYPE.CROP -> R.drawable.ic_black_tool_crop
            TOOL_VIDEO_TYPE.CUT -> R.drawable.ic_black_tool_cut
            TOOL_VIDEO_TYPE.SPEED -> R.drawable.ic_black_tool_speed
            TOOL_VIDEO_TYPE.FILTER -> R.drawable.ic_black_tool_filter
            TOOL_VIDEO_TYPE.MUSIC -> R.drawable.ic_black_tool_music
            TOOL_VIDEO_TYPE.ROTATE -> R.drawable.ic_black_tool_rotate
        }
    }

    fun getName(): String {
        return when (type) {
            TOOL_VIDEO_TYPE.CROP -> getAppString(R.string.crop)
            TOOL_VIDEO_TYPE.CUT -> getAppString(R.string.cut)
            TOOL_VIDEO_TYPE.SPEED -> getAppString(R.string.speed)
            TOOL_VIDEO_TYPE.FILTER -> getAppString(R.string.filter)
            TOOL_VIDEO_TYPE.MUSIC -> getAppString(R.string.music)
            TOOL_VIDEO_TYPE.ROTATE -> getAppString(R.string.rotate)
        }
    }
}

enum class TOOL_VIDEO_TYPE {
    CROP,
    CUT,
    SPEED,
    FILTER,
    MUSIC,
    ROTATE
}

fun getListAllToolVideo(): List<ToolVideo> {
    return listOf(
        ToolVideo(TOOL_VIDEO_TYPE.CROP),
        ToolVideo(TOOL_VIDEO_TYPE.CUT),
        ToolVideo(TOOL_VIDEO_TYPE.SPEED),
        ToolVideo(TOOL_VIDEO_TYPE.FILTER),
        ToolVideo(TOOL_VIDEO_TYPE.MUSIC),
        ToolVideo(TOOL_VIDEO_TYPE.ROTATE)
    )
}
