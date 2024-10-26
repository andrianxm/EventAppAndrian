package com.andrian.mydicodingeventapp.ui.setting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.andrian.mydicodingeventapp.data.datastore.SettingPreferences
import kotlinx.coroutines.launch

class SettingViewModel(private val preferences: SettingPreferences) : ViewModel() {

    fun isDarkMode(): LiveData<Boolean> {
        return preferences.getThemeSetting().asLiveData()
    }

    fun toggleTheme(isDarkMode: Boolean) {
        viewModelScope.launch {
            Log.d("SettingViewModel", "Mengganti Mode Ke: $isDarkMode")
            preferences.saveThemeSetting(isDarkMode)
        }
    }
}