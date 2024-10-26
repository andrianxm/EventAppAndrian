package com.andrian.mydicodingeventapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrian.mydicodingeventapp.data.Repository
import com.andrian.mydicodingeventapp.data.Result
import com.andrian.mydicodingeventapp.data.local.FavoriteEvent
import com.andrian.mydicodingeventapp.data.response.DetailEventResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel(private val repository: Repository) : ViewModel() {

    private val _detailevent = MediatorLiveData<Result<DetailEventResponse>>()
    val event: LiveData<Result<DetailEventResponse>> = _detailevent

    private val _isFavorite = MediatorLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    fun getEventDetail(eventId: String) {
        val liveData = repository.getDetailEvent(eventId)
        _detailevent.addSource(liveData) { result ->
            _detailevent.value = result
        }
    }

    fun checkIfFavorite(eventId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val isFav = repository.isFavorite(eventId)
            withContext(Dispatchers.Main) {
                _isFavorite.value = isFav
            }
        }
    }

    fun saveFavorite(event: FavoriteEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveFavorite(event)
        }
    }

    fun deleteFavorite(event: FavoriteEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(event)
        }
    }
}