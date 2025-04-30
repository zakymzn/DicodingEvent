package com.example.dicodingevent.ui.settings

import android.content.Intent
import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkInfo
import com.example.dicodingevent.MainActivity
import com.example.dicodingevent.R
import com.example.dicodingevent.workmanager.UpcomingEventWorker
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.concurrent.TimeUnit

class SettingsPreferenceFragment : PreferenceFragmentCompat() {

    private lateinit var workManager: WorkManager
    private var periodicWorkRequest: PeriodicWorkRequest? = null
    private var hasHandledPermissionResult = false

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!hasHandledPermissionResult) {
            hasHandledPermissionResult = true
            if (isGranted) {
                Toast.makeText(requireActivity(), "Izin notifikasi diberikan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireActivity(), "Izin notifikasi ditolak", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)

        if (bottomNavigationView == null) {
            Log.e("SettingsPreferenceFragment", "BottomNavigationView is null")
        }

        val darkThemeKey = getString(R.string.key_dark_theme)
        val eventNotificationKey = getString(R.string.key_event_notification)

        val switchThemePreference = findPreference<SwitchPreferenceCompat>(darkThemeKey)
        val switchNotificationPreference = findPreference<SwitchPreferenceCompat>(eventNotificationKey)

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

        settingsViewModel.getNotificationSettings().observe(this) { isNotificationActive: Boolean ->
            if (isNotificationActive) {
                startPeriodicTask()
                switchNotificationPreference?.isChecked = true
            } else {
                cancelPeriodicTask()
                switchNotificationPreference?.isChecked = false
            }
        }

        switchNotificationPreference?.setOnPreferenceChangeListener { _, isChecked ->
            val isNotification = isChecked as Boolean
            settingsViewModel.saveNotificationSettings(isNotification)
            true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        workManager = WorkManager.getInstance(requireActivity())
    }

    private fun startPeriodicTask() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        if (periodicWorkRequest == null) {
            periodicWorkRequest = PeriodicWorkRequest.Builder(UpcomingEventWorker::class.java, 1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .addTag(WORKER_TAG)
                .build()
        }

        workManager.getWorkInfosByTagLiveData(WORKER_TAG).observe(viewLifecycleOwner) { workInfos ->
            val isWorkScheduled = workInfos?.any { workInfo ->
                val state = workInfo.state
                state == WorkInfo.State.ENQUEUED || state == WorkInfo.State.RUNNING
            } ?: false

            if (!isWorkScheduled) {
                workManager.enqueue(periodicWorkRequest!!)
            }
        }
    }

    private fun cancelPeriodicTask() {
        workManager.cancelAllWorkByTag(WORKER_TAG)
    }

    companion object {
        private val WORKER_TAG = "event_worker"
    }
}