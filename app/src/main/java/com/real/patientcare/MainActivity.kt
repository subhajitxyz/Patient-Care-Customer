package com.real.patientcare

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.real.patientcare.navigation.Screens
import com.real.patientcare.ui.presentation.dashboard.DashboardScreen
import com.real.patientcare.ui.presentation.login.LoginScreen
import com.real.patientcare.ui.presentation.splash.SplashViewModel
import com.real.patientcare.ui.theme.PatientCareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val splashViewModel: SplashViewModel by viewModels<SplashViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            splashViewModel.startDestinationState.value == null
        }
        enableEdgeToEdge()
        setContent {
            PatientCareTheme {
                val navController = rememberNavController()
                val startDestinationState = splashViewModel.startDestinationState.collectAsStateWithLifecycle()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    if(startDestinationState.value!= null) {

                        NavHost(
                            navController = navController,
                            startDestination = startDestinationState.value!!,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable( route = Screens.ScreenLogin.route) {
                                LoginScreen(navController = navController)
                            }

                            composable(route = Screens.ScreenDashboard.route) {
                                DashboardScreen(
                                    navController = navController
                                )
                            }
                        }

                    }

                }
            }
        }
    }
}
