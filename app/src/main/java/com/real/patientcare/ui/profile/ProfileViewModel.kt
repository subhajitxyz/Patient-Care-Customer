package com.real.patientcare.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.real.patientcare.domain.repo.PatientInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val patientInfoRepository: PatientInfoRepository
): ViewModel() {

    private val _profileState = MutableStateFlow(ProfileUiState())
    val profileUiState: StateFlow<ProfileUiState> = _profileState

    init {
        loadProfile()
    }

    private fun loadProfile() {
        _profileState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = patientInfoRepository.getPatientBasicInfo()

            result
                .onSuccess { profile ->
                    _profileState.update {
                        it.copy(
                            uid = profile.uid,
                            name = profile.name,
                            email = profile.email,
                            phone = profile.phone,
                            role = profile.role,
                            dob = profile.dob,
                            createdDate = profile.createdDate,
                            createdTime = profile.createdTime
                        )
                    }
                    _profileState.update { it.copy(isLoading = false) }
                }
                .onFailure {
                    _profileState.update { it.copy(isLoading = false) }
                }
        }
    }
}