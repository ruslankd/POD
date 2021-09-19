package com.example.pod.adapter

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pod.view.CosmoWeatherFragment
import com.example.pod.view.EarthFragment
import com.example.pod.view.MarsFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = arrayOf(
            EarthFragment.newInstance(),
            MarsFragment.newInstance(),
            CosmoWeatherFragment.newInstance()
    )

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]

}