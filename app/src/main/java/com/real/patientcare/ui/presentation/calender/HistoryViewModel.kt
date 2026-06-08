package com.real.patientcare.ui.presentation.calender

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.real.patientcare.data.repo.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: HistoryRepository
) : ViewModel() {

    private val _historyEvents =
        MutableStateFlow<List<HistoryEventUi>>(emptyList())

    val historyEvents = _historyEvents.asStateFlow()

    fun loadHistory(date: String) {

        viewModelScope.launch {

            launch {
                repository.observeHistory(date)
                    .collect { events ->
                        _historyEvents.value = events
                    }

            }

            repository.syncHistory()
        }

    }
}

data class HistoryEventUi(
    val eventId: String,
    val eventName: String,
    val status: String,
    val timestamp: String
)