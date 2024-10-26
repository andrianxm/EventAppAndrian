package com.andrian.mydicodingeventapp.di

import android.content.Context
import com.andrian.mydicodingeventapp.data.Repository
import com.andrian.mydicodingeventapp.data.local.FavoriteEventDatabase
import com.andrian.mydicodingeventapp.data.response.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()
        val database = FavoriteEventDatabase.getDatabase(context)
        val dao = database.favoriteEventDao()
        return Repository.getInstance(apiService, dao)
    }
}

