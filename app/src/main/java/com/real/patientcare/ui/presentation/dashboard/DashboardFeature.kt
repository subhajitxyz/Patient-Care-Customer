package com.real.patientcare.ui.presentation.dashboard

import androidx.compose.ui.graphics.Color


data class DashboardFeature(
    val type: FeatureType,
    val name: String,
    val seen: Boolean,
    val accuracy: String,
    val date: String,
    val time: String,
    val color: Color
)

enum class FeatureType {
    HEART_ATTACK,
    BREATHING_PROBLEM,
    COUGHING_PROBLEM
}
//sealed class DashboardFeatureType {
//    data class HeartAttack(
//        val name: String = "Heart\nAttack",
//        var seen: Boolean = true,
//        var accuracy: String = "100", // Accuracy will be shown, if heart attack is detected.
//        var time: String = "Current Date \nTime",
//        val color: Color = TileRed
//    ): DashboardFeatureType()
//
//    data class BreathingProblem(
//        val name: String = "Breathing\nProblem",
//        var seen: Boolean = true,
//        var accuracy: String = "100", // Accuracy will be shown, if heart attack is detected.
//        var time: String = "Current Date \nTime",
//        val color: Color = TileBlue
//    ): DashboardFeatureType()
//
//    data class CoughingProblem(
//        val name: String = "Extreme\nCough",
//        var seen: Boolean = false,
//        var accuracy: String = "100", // Accuracy will be shown, if heart attack is detected.
//        var time: String = "Current Date \nTime",
//        val color: Color = TileDarkYello
//    ): DashboardFeatureType()
//}