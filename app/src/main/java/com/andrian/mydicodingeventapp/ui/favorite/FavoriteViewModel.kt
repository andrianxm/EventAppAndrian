package com.andrian.mydicodingeventapp.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.andrian.mydicodingeventapp.data.local.FavoriteEvent
import com.andrian.mydicodingeventapp.data.local.FavoriteEventDatabase

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val database: FavoriteEventDatabase = FavoriteEventDatabase.getDatabase(application)

    val favoriteEvents: LiveData<List<FavoriteEvent>> = database.favoriteEventDao().getAllFavorites()

}