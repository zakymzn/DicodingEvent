package com.example.dicodingevent.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.dicodingevent.R

class SettingsPreferenceFragment : PreferenceFragmentCompat() {

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val preferences = SettingsPreferences.getInstance(requireContext().dataStore)
        val settingsViewModelFactory = SettingsViewModelFactory(preferences)

        settingsViewModel = ViewModelProvider(this, settingsViewModelFactory)[SettingsViewModel::class.java]

        val key = getString(R.string.key_dark_theme)
        val darkThemePreference = findPreference<SwitchPreferenceCompat>(key)

        viewLifecycleOwnerLiveData.observe(this) { owner ->
            settingsViewModel.getThemeSettings().observe(owner) { isDarkThemeActive ->
                if (darkThemePreference?.isChecked != isDarkThemeActive) {
                    darkThemePreference?.isChecked = isDarkThemeActive
                }
            }
        }

        darkThemePreference?.setOnPreferenceChangeListener { _, newValue ->
            val isDarkTheme = newValue as Boolean
            settingsViewModel.saveThemeSettings(isDarkTheme)

            val mode = if (isDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }

            AppCompatDelegate.setDefaultNightMode(mode)
            requireActivity().recreate()
            true
        }
    }
}