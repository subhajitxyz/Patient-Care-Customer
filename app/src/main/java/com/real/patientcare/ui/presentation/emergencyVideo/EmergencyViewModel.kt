package com.real.patientcare.ui.presentation.emergencyVideo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.real.patientcare.domain.repo.EmergencyVideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmergencyVideoViewModel @Inject constructor(

    private val repository: EmergencyVideoRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EmergencyVideoContracts.EmergencyVideoState())
    val state = _state.asStateFlow()

    fun onIntent(intent: EmergencyVideoContracts.EmergencyVideoIntent) {

        when (intent) {

            is EmergencyVideoContracts.EmergencyVideoIntent.LoadVideo -> {
                loadVideo(intent.emergencyName, intent.eventId)
            }

            EmergencyVideoContracts.EmergencyVideoIntent.ClearError -> {
                _state.update {
                    it.copy(error = null)
                }
            }
        }
    }

    private fun loadVideo(
        emergencyName: String,
        eventId: String
    ) {

        viewModelScope.launch {

            _state.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            repository.observeVideo(eventId)
                .onEach { cached ->

                    if (cached != null) {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                localVideoPath = cached.localPath,
                                error = null
                            )
                        }
                    }
                }
                .launchIn(this)

            val result = repository.loadVideo(emergencyName = emergencyName, eventId)

            result.onFailure {

                _state.update {
                    it.copy(
                        isLoading = false,
                        error = it.error
                            ?: "Video Loading, Try After few seconds."
                    )
                }
            }
        }
    }
}