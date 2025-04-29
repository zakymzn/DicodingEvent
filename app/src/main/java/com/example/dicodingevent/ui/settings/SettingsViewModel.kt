package com.example.dicodingevent.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SettingsViewModel(private val preferences: SettingsPreferences) : ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return preferences.getThemeSetting().asLiveData()
    }

    fun getNotificationSettings(): LiveData<Boolean> {
        return preferences.getNotificationSetting().asLiveData()
    }

    fun saveThemeSettings(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            preferences.saveThemeSetting(isDarkModeActive)
        }
    }

    fun saveNotificationSettings(isNotificationActive: Boolean) {
        viewModelScope.launch {
            preferences.saveNotificationSetting(isNotificationActive)
        }
    }
}