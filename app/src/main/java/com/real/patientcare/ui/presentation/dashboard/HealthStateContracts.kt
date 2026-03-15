package com.real.patientcare.ui.presentation.dashboard

sealed class HealthStateContracts {

    data class HealthStates(
        val stateList: List<DashboardFeature> = emptyList(),
        val isLoading: Boolean = false
    )

    sealed class Intent {
        data class ShowHealthDetails(val type: String): Intent()
    }
}