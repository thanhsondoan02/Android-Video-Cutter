package com.mobile.videocutter.presentation.addmusic

import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.base.extension.getAppString
import com.mobile.videocutter.databinding.AddMusicActivtityBinding

class AddMusicActivity : BaseBindingActivity<AddMusicActivtityBinding>(R.layout.add_music_activtity) {
    private var viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
    private val fragmentList = mutableListOf<Fragment>(ITunesFragment(), MusicTrackFragment())
    private var pageIndex: ((Int) -> Unit)? = null

    override fun onInitView() {
        super.onInitView()
        initViewPager()

        binding.tabViewAddMusic.apply {
            setLeftText(getAppString(R.string.itunes))
            setRightText(getAppString(R.string.music_track))

            setOnLeftClick {
                binding.cvpAddMusic.currentItem = 0
            }
            setOnRightClick {
                binding.cvpAddMusic.currentItem = 1
            }
        }
    }

    private fun addOnPageSelected(pageIndex: ((Int) -> Unit)) {
        this.pageIndex = pageIndex
    }

    private fun initViewPager() {
        viewPagerAdapter.addFra(fragmentList)
        binding.cvpAddMusic.apply {
            adapter = viewPagerAdapter
            offscreenPageLimit = viewPagerAdapter.count
            currentItem = 0

            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    pageIndex?.invoke(position)
                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })

            addOnPageSelected {
                binding.tabViewAddMusic.select(it == 0)
            }
        }
    }
}
