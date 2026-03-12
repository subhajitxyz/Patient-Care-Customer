package com.real.patientcare.ui.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.real.patientcare.domain.repo.LoginRepository
import com.real.patientcare.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val loginRepository: LoginRepository
): ViewModel() {

    var startDestinationState = MutableStateFlow<String?>(null)
        private set

    init {
        checkUserLoginStatus()
    }

    private fun checkUserLoginStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            startDestinationState.value = if( loginRepository.checkUserLoginStatus()) {
                Screens.ScreenDashboard.route
            } else {
                Screens.ScreenLogin.route
            }
        }
    }

}