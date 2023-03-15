package com.mobile.videocutter.presentation.addmusic

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager

class CustomViewPager constructor(
    ctx: Context,
    attrs: AttributeSet?
) : ViewPager(ctx, attrs) {

    init {
        overScrollMode = OVER_SCROLL_NEVER
    }

    private var pageIndex: ((Int) -> Unit)? = null

    init {
        addOnPageChangeListener (object : OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                pageIndex?.invoke(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        } )
    }

    fun addOnPageSelected(pageIndex: ((Int) -> Unit)) {
        this.pageIndex = pageIndex
    }

}
