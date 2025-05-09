package com.example.dicodingevent.ui.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] == true
        }
    }

    fun getNotificationSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[NOTIFICATION_KEY] == true
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkModeActive
        }
    }

    suspend fun saveNotificationSetting(isNotificationActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[NOTIFICATION_KEY] = isNotificationActive
        }
    }

    companion object {
        private val THEME_KEY = booleanPreferencesKey("theme_setting")
        private val NOTIFICATION_KEY = booleanPreferencesKey("notification_setting")

        @Volatile
        private var INSTANCE: SettingsPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingsPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingsPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}