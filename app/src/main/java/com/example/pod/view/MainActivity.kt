package com.example.pod.view

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.pod.R
import com.example.pod.view.SettingsFragment.Companion.SHARED_PREF_NAME

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val nightModeFlags = resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            val themeId = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
                .getInt(SettingsFragment.SP_THEME, R.style.Theme3)
            setTheme(themeId)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, PODFragment.newInstance())
                .commit()
        }
    }
}