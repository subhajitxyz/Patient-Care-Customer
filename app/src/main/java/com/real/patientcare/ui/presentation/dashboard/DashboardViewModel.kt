package com.real.patientcare.ui.presentation.dashboard

import androidx.compose.foundation.MutatePriority
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.real.patientcare.domain.repo.HealthRepository
import com.real.patientcare.domain.repo.PatientInfoRepository
import com.real.patientcare.ui.theme.TileBlue
import com.real.patientcare.ui.theme.TileDarkYello
import com.real.patientcare.ui.theme.TileLightBlue
import com.real.patientcare.ui.theme.TileRed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val healthRepository: HealthRepository,
    private val patientInfoRepository: PatientInfoRepository
): ViewModel() {

    private val _headerInfoState = MutableStateFlow(DashBoardHeaderContracts.BasicInfo())
    val headerInfoState: StateFlow<DashBoardHeaderContracts.BasicInfo> = _headerInfoState

    private val _healthState = MutableStateFlow(HealthStateContracts.HealthStates())
    val healthStates: StateFlow<HealthStateContracts.HealthStates> = _healthState

    init {
        loadPatientbasicInfo()
        observeHealthFromFirebase()
    }

    private fun loadPatientbasicInfo() {
        viewModelScope.launch {

            _headerInfoState.update {
                it.copy(isLoading = true)
            }

            val result = patientInfoRepository.getPatientBasicInfo()

            result
                .onSuccess { patient ->
                    _headerInfoState.update {
                        it.copy(
                            name = patient.name,
                            isLoading = false,
                        )
                    }
                }
                .onFailure { throwable ->
                    _headerInfoState.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                }
        }
    }

    private fun observeHealthFromFirebase() {
        _healthState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            healthRepository.observeHealth()
                .collect { result ->
                    result
                        .onSuccess { healthStatus ->

                            val newList = listOf(
                                DashboardFeature(
                                    type = FeatureType.HEART_ATTACK,
                                    name = "Heart\nAttack",
                                    seen = healthStatus.heartAttack?.seen ?: false,
                                    accuracy = healthStatus.heartAttack?.accuracy ?: "0",
                                    date = healthStatus.heartAttack?.date ?: "",
                                    time = healthStatus.heartAttack?.time ?: "",
                                    color = TileLightBlue
                                ),

                                DashboardFeature(
                                    type = FeatureType.BREATHING_PROBLEM,
                                    name = "Breathing\nProblem",
                                    seen = healthStatus.extremeBreath?.seen ?: false,
                                    accuracy = healthStatus.extremeBreath?.accuracy ?: "0",
                                    date = healthStatus.extremeBreath?.date ?: "",
                                    time = healthStatus.extremeBreath?.time ?: "",
                                    color = TileBlue
                                ),

                                DashboardFeature(
                                    type = FeatureType.COUGHING_PROBLEM,
                                    name = "Extreme\nCough",
                                    seen = healthStatus.extremeCough?.seen ?: false,
                                    accuracy = healthStatus.extremeCough?.accuracy ?: "0",
                                    date = healthStatus.extremeCough?.date ?: "",
                                    time = healthStatus.extremeCough?.time ?: "",
                                    color = TileDarkYello
                                )
                            )

                            _healthState.update {
                                it.copy(stateList = newList)
                            }

                            _healthState.update { it.copy(isLoading = false) }
                        }
                        .onFailure {

                            _healthState.update { it.copy(isLoading = false) }
                        }

                }

        }
    }


    fun onIntent(intent: HealthStateContracts.Intent) {
        when(intent) {
            is HealthStateContracts.Intent.ShowHealthDetails -> {}
        }
    }


}