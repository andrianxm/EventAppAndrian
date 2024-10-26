package com.andrian.mydicodingeventapp.ui.setting

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.andrian.mydicodingeventapp.data.datastore.SettingPreferences
import com.andrian.mydicodingeventapp.data.datastore.dataStore
import com.andrian.mydicodingeventapp.databinding.FragmentSettingBinding
import com.andrian.mydicodingeventapp.utils.ThemeViewModelFactory

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private lateinit var settingViewModel: SettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferences = SettingPreferences.getInstance(requireContext().dataStore)
        val factory = ThemeViewModelFactory(preferences)
        settingViewModel = ViewModelProvider(this, factory)[SettingViewModel::class.java]

        settingViewModel.isDarkMode().observe(viewLifecycleOwner) { isDarkMode ->
            binding.switchTheme.isChecked = isDarkMode
            setThemeMode(isDarkMode)
        }

        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked ->
            settingViewModel.toggleTheme(isChecked)
        }
        (activity as AppCompatActivity).supportActionBar?.apply {
            setBackgroundDrawable(ColorDrawable(Color.parseColor("#2d3e50")))
        }

    }

    private fun setThemeMode(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}