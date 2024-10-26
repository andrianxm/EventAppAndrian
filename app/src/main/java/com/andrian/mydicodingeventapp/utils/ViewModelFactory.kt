package com.andrian.mydicodingeventapp.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.andrian.mydicodingeventapp.data.Repository
import com.andrian.mydicodingeventapp.di.Injection
import com.andrian.mydicodingeventapp.ui.detail.DetailViewModel
import com.andrian.mydicodingeventapp.ui.finished.FinishedViewModel
import com.andrian.mydicodingeventapp.ui.home.HomeViewModel
import com.andrian.mydicodingeventapp.ui.search.SearchViewModel
import com.andrian.mydicodingeventapp.ui.upcoming.UpcomingViewModel

class ViewModelFactory(private val repository: Repository): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> return DetailViewModel(repository) as T
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> return SearchViewModel(repository) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> return HomeViewModel(repository) as T
            modelClass.isAssignableFrom(FinishedViewModel::class.java) -> return FinishedViewModel(repository) as T
            modelClass.isAssignableFrom(UpcomingViewModel::class.java) -> return UpcomingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class: " + modelClass.name)
    }

    companion object{
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(Injection.provideRepository(context))
        }.also { instance = it }
    }
}