package com.mobile.videocutter.presentation.addmusic

import androidx.fragment.app.Fragment
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingActivity
import com.mobile.videocutter.base.extension.getAppString
import com.mobile.videocutter.databinding.AddMusicActivtityBinding

class AddMusicActivity : BaseBindingActivity<AddMusicActivtityBinding>(R.layout.add_music_activtity) {
    private var viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
    private val fragmentList = mutableListOf<Fragment>(ITunesFragment(), MusicTrackFragment())

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

    private fun initViewPager() {
        viewPagerAdapter.addFra(fragmentList)
        binding.cvpAddMusic.apply{
            adapter = viewPagerAdapter
            offscreenPageLimit = viewPagerAdapter.count
            currentItem = 0
            addOnPageSelected {
                binding.tabViewAddMusic.select(it==0)
            }
        }
    }
}
