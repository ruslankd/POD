package com.example.pod.view

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.pod.R
import com.example.pod.adapter.ViewPagerAdapter
import com.example.pod.databinding.ActivityApiBinding
import com.google.android.material.tabs.TabLayoutMediator

class ApiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityApiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val nightModeFlags = resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            val themeId = getSharedPreferences("POD_SP", Context.MODE_PRIVATE)
                    .getInt(SettingsFragment.SP_THEME, R.style.Theme3)
            setTheme(themeId)
        }
        super.onCreate(savedInstanceState)
        binding = ActivityApiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = ViewPagerAdapter(this)
        binding.viewPager.setPageTransformer(ZoomOutPageTransformer())
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Земля"
                1 -> tab.text = "Марс"
                2 -> tab.text = "Погода"
            }
        }.attach()
        TabLayoutMediator(binding.tabLayout2, binding.viewPager) { _, _ ->
        }.attach()
    }
}