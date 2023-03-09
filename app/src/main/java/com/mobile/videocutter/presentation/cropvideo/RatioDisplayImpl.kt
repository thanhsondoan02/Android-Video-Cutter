package com.mobile.videocutter.presentation.cropvideo

class RatioDisplayImpl: IRatioDisplay {
    override fun getListRatio(): List<RatioAdapter.RatioDisplay> {
        val list: MutableList<RatioAdapter.RatioDisplay> = arrayListOf()

        list.add(RatioAdapter.RatioDisplay(
            type = RatioAdapter.RATIO_TYPE.CUSTOM,
            isSelect = true
        ))

        list.add(RatioAdapter.RatioDisplay(
            type = RatioAdapter.RATIO_TYPE.INSTAGRAM,
            isSelect = false
        ))

        list.add(RatioAdapter.RatioDisplay(
            type = RatioAdapter.RATIO_TYPE._4_5,
            isSelect = false
        ))

        list.add(RatioAdapter.RatioDisplay(
            type = RatioAdapter.RATIO_TYPE._5_4,
            isSelect = false
        ))

        list.add(RatioAdapter.RatioDisplay(
            type = RatioAdapter.RATIO_TYPE._9_16,
            isSelect = false
        ))

        list.add(RatioAdapter.RatioDisplay(
            type = RatioAdapter.RATIO_TYPE._16_9,
            isSelect = false
        ))

        list.add(RatioAdapter.RatioDisplay(
            type = RatioAdapter.RATIO_TYPE._3_2,
            isSelect = false
        ))

        list.add(RatioAdapter.RatioDisplay(
            type = RatioAdapter.RATIO_TYPE._2_3,
            isSelect = false
        ))

        list.add(RatioAdapter.RatioDisplay(
            type = RatioAdapter.RATIO_TYPE._4_3,
            isSelect = false
        ))

        list.add(RatioAdapter.RatioDisplay(
            type = RatioAdapter.RATIO_TYPE._3_4,
            isSelect = false
        ))

        return list.toList()
    }
}
