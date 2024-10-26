package com.andrian.mydicodingeventapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrian.mydicodingeventapp.data.Repository
import com.andrian.mydicodingeventapp.data.Result
import com.andrian.mydicodingeventapp.data.response.ListEventsItem
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel() {


    private val _textUpcoming = MutableLiveData<String>().apply {
        value = "ACTIVE EVENT"
    }
    val textUpcoming: LiveData<String> = _textUpcoming
    private val _textFinihsed = MutableLiveData<String>().apply {
        value = "FINISHED EVENT"
    }
    val textFinihsed: LiveData<String> = _textFinihsed


    private val _upcomingEvents = MutableLiveData<Result<List<ListEventsItem>>>()
    val getUpcomingEvents: LiveData<Result<List<ListEventsItem>>> = _upcomingEvents

    private val _finishedEvents = MutableLiveData<Result<List<ListEventsItem>>>()
    val getFinishedEvents: LiveData<Result<List<ListEventsItem>>> = _finishedEvents

    init {
        loadUpcomingEvents()
        loadFinishedEvents()
    }

    private fun loadUpcomingEvents() {
        viewModelScope.launch {
            _upcomingEvents.value = Result.Loading
            val result = repository.getEvents(isActive = 1)
            _upcomingEvents.value = result
        }
    }

    private fun loadFinishedEvents() {
        viewModelScope.launch {
            _finishedEvents.value = Result.Loading
            val result = repository.getEvents(isActive = 0)
            _finishedEvents.value = result
        }
    }

}