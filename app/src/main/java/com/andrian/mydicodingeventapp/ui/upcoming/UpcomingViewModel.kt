package com.andrian.mydicodingeventapp.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrian.mydicodingeventapp.data.Repository
import com.andrian.mydicodingeventapp.data.Result
import com.andrian.mydicodingeventapp.data.response.ListEventsItem
import kotlinx.coroutines.launch

class UpcomingViewModel (private val repository: Repository) : ViewModel() {

    private val _upcomingEvents = MutableLiveData<Result<List<ListEventsItem>>>()
    val upcomingEvents: LiveData<Result<List<ListEventsItem>>> = _upcomingEvents

    init {
        findUpcomingEvent()
    }

    private fun findUpcomingEvent() {
        viewModelScope.launch {
            _upcomingEvents.value = Result.Loading
            val result = repository.getUpcomingEvents()
            _upcomingEvents.value = result
        }
    }

}