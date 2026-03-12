package com.real.patientcare.navigation

sealed class Screens(val route: String) {
    object ScreenDashboard: Screens("dashboard_screen")
    object ScreenLogin: Screens("login_screen")
}