package com.real.patientcare.ui.presentation.dashboard

import java.util.Date

interface DashBoardHeaderContracts {

    data class BasicInfo(
        val name: String = "",
        val isLoading: Boolean = false
    )

    sealed class Intent {
        object ClickedUserProfile: Intent()
    }
}