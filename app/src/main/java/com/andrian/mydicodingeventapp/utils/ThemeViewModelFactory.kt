package com.andrian.mydicodingeventapp.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.andrian.mydicodingeventapp.data.datastore.SettingPreferences
import com.andrian.mydicodingeventapp.ui.setting.SettingViewModel

class ThemeViewModelFactory(private val pref: SettingPreferences) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingViewModel(pref) as T
    }
}