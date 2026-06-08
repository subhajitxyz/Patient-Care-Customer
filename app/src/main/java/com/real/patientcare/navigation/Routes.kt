package com.real.patientcare.navigation

sealed class Screens(val route: String) {
    object ScreenDashboard: Screens("dashboard_screen")
    object ScreenLogin: Screens("login_screen")
    object ScreenProfile: Screens("profile_screen")

    object ScreenEmergencyVideo :
        Screens("emergency_video_screen/{emergencyName}/{eventId}") {

        fun createRoute(emergencyName: String, eventId: String): String {
            return "emergency_video_screen/$emergencyName/$eventId"
        }
    }

    object ScreenHistory :
        Screens("history_screen/{date}") {

        fun createRoute(date: String): String {
            return "history_screen/$date"
        }
    }
}