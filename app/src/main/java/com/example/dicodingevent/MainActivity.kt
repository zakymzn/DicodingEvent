package com.example.dicodingevent

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dicodingevent.databinding.ActivityMainBinding
import com.example.dicodingevent.ui.settings.SettingsPreferences
import com.example.dicodingevent.ui.settings.dataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val notConnected = intent.getBooleanExtra(ConnectivityManager
                .EXTRA_NO_CONNECTIVITY, false)
            if (notConnected) {
                disconnected()
            } else {
                connected()
            }
        }
    }

    private fun disconnected() {
        binding.llNoInternetConnection.visibility = View.VISIBLE
        binding.clActivityMain.visibility = View.GONE
    }

    private fun connected() {
        binding.llNoInternetConnection.visibility = View.GONE
        binding.clActivityMain.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val preferences = SettingsPreferences.getInstance(dataStore)
        val isDarkTheme = runBlocking { preferences.getThemeSetting().first() }

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            val navView: BottomNavigationView = binding.navView
            val navController = findNavController(R.id.nav_host_fragment_activity_main)

            navView.setupWithNavController(navController)

            val navigateToSettings = intent.getBooleanExtra("navigate_to_settings", false)
            if (navigateToSettings) {
                navController.navigate(R.id.navigation_settings)
                navView.selectedItemId = R.id.navigation_settings

                intent.removeExtra("navigate_to_settings")
            }
        }
    }

    override fun onStart() {
        super.onStart()

        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onStop() {
        super.onStop()

        unregisterReceiver(broadcastReceiver)
    }
}