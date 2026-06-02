package com.real.patientcare.ui.presentation.emergencyVideo

interface EmergencyVideoContracts {

    data class EmergencyVideoState(

        val isLoading: Boolean = false,

        val localVideoPath: String? = null,

        val error: String? = null
    )

    sealed interface EmergencyVideoIntent {

        data class LoadVideo(
            val emergencyName: String,
            val eventId: String
        ) : EmergencyVideoIntent

        data object ClearError : EmergencyVideoIntent
    }
}