package com.andrian.mydicodingeventapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrian.mydicodingeventapp.data.Repository
import com.andrian.mydicodingeventapp.data.Result
import com.andrian.mydicodingeventapp.data.response.EventResponse
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: Repository) : ViewModel() {

    private val _searchEvent = MutableLiveData<Result<EventResponse>>()
    val searchEvent: LiveData<Result<EventResponse>> = _searchEvent

    init {
        searchEvents()
    }

    private fun searchEvents() = viewModelScope.launch {
        _searchEvent.value = Result.Loading
        val result = repository.searchEvents(-1, "")
        _searchEvent.value = result
    }
}