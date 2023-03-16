package com.mobile.videocutter.presentation.addmusic

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    private val listFra: MutableList<Fragment> = mutableListOf()

    override fun getCount() = listFra.size

    override fun getItem(position: Int): Fragment {
        return listFra[position]
    }

    fun addFra(fragmentList: MutableList<Fragment>) {
        this.listFra.clear()
        this.listFra.addAll(fragmentList)
    }
}
