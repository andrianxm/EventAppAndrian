package com.andrian.mydicodingeventapp.ui.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrian.mydicodingeventapp.data.Repository
import com.andrian.mydicodingeventapp.data.Result
import com.andrian.mydicodingeventapp.data.response.ListEventsItem
import kotlinx.coroutines.launch

class FinishedViewModel(private val repository: Repository) : ViewModel() {

    private val _finishedEvents = MutableLiveData<Result<List<ListEventsItem>>>()
    val finishedEvents: LiveData<Result<List<ListEventsItem>>> = _finishedEvents

    init {
        findFinishedEvent()
    }

    private fun findFinishedEvent() {
        viewModelScope.launch {
            _finishedEvents.value = Result.Loading
            val result = repository.getFinishedEvents()
            _finishedEvents.value = result
        }
    }

}