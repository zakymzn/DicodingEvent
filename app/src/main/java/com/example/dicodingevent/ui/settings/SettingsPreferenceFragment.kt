package com.example.dicodingevent.ui.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.dicodingevent.MainActivity
import com.example.dicodingevent.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsPreferenceFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)

        if (bottomNavigationView == null) {
            Log.e("SettingsPreferenceFragment", "BottomNavigationView is null")
        }

        val key = getString(R.string.key_dark_theme)
        val switchThemePreference = findPreference<SwitchPreferenceCompat>(key)
        val preferences = SettingsPreferences.getInstance(requireContext().dataStore)
        val settingsViewModelFactory = SettingsViewModelFactory(preferences)

        val settingsViewModel = ViewModelProvider(this, settingsViewModelFactory)[SettingsViewModel::class.java]

        settingsViewModel.getThemeSettings().observe(this) { isDarkThemeActive: Boolean ->
            if (isDarkThemeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchThemePreference?.isChecked = true
                bottomNavigationView.animate()?.translationY(bottomNavigationView.height.toFloat())?.duration = 0
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchThemePreference?.isChecked = false
                bottomNavigationView.animate()?.translationY(bottomNavigationView.height.toFloat())?.duration = 0
            }
        }

        switchThemePreference?.setOnPreferenceChangeListener { _, isChecked ->
            val isDarkTheme = isChecked as Boolean
            settingsViewModel.saveThemeSettings(isDarkTheme)

            val context = requireContext()
            val intent = Intent(context, MainActivity::class.java).apply {
                putExtra("navigate_to_settings", true)
            }
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            requireActivity().finish()
            true
        }
    }
}