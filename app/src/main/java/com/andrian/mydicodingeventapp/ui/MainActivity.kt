package com.andrian.mydicodingeventapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.andrian.mydicodingeventapp.R
import com.andrian.mydicodingeventapp.data.datastore.SettingPreferences
import com.andrian.mydicodingeventapp.data.datastore.dataStore
import com.andrian.mydicodingeventapp.databinding.ActivityMainBinding
import com.andrian.mydicodingeventapp.ui.setting.SettingViewModel
import com.andrian.mydicodingeventapp.utils.ThemeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var settingViewModel: SettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferences = SettingPreferences.getInstance(dataStore)
        val factory = ThemeViewModelFactory(preferences)
        settingViewModel = ViewModelProvider(this, factory)[SettingViewModel::class.java]

        lifecycleScope.launch {
            val isDarkMode = preferences.getThemeSetting().first()
            setThemeMode(isDarkMode) //
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            setupNavigation()
        }
    }

    private fun setThemeMode(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun setupNavigation() {
        val navView: BottomNavigationView = binding.navView
        navView.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_1))
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_upcoming, R.id.navigation_home, R.id.navigation_finished
            )

        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}