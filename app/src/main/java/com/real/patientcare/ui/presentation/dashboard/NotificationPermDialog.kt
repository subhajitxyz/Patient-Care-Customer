package com.real.patientcare.ui.presentation.dashboard

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun NotificationPermissionDialog(
    onEnableClick: () -> Unit,
    onDismissClick: () -> Unit
) {

    AlertDialog(

        onDismissRequest = onDismissClick,

        title = {
            Text(text = "Enable Notifications")
        },

        text = {
            Text(
                text = "Enable notifications to receive critical health alerts and emergency updates."
            )
        },

        confirmButton = {

            Button(
                onClick = onEnableClick
            ) {
                Text(text = "Enable")
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismissClick
            ) {
                Text(text = "Not Now")
            }
        }
    )
}