package com.real.patientcare.ui.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.real.patientcare.R
import com.real.patientcare.common.WavyBackgroundView
import com.real.patientcare.ui.theme.PrimaryBackgroundBlue

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel()
) {

    val patientDetails by profileViewModel.profileUiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    Box(
        Modifier
            .fillMaxSize()
            .background(PrimaryBackgroundBlue)
    ) {
        WavyBackgroundView(R.color.purple_700, 800.dp)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            contentAlignment = Alignment.Center
        ) {

            if(patientDetails.isLoading) {
                CircularProgressIndicator(
                    color = Color.Black
                )
            } else {
                ProfileCard(patientDetails)
            }
        }
    }

}

@Composable
private fun ProfileCard(
    patientDetails: ProfileUiState
) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(40.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(containerColor = if(isSystemInDarkTheme()) Color.Black else Color.White)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp)
        ) {

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.user_profile_icon),
                    contentDescription = "Patient Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .padding(10.dp)
                        .clip(CircleShape)
                )
            }

            Text(
                modifier = Modifier.padding(10.dp),
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Name: ")
                    }
                    append(patientDetails.name)
                },
                fontSize = 18.sp
            )

            Text(
                modifier = Modifier.padding(10.dp),
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Role: ")
                    }
                    append(patientDetails.role)
                },
                fontSize = 18.sp
            )

            Text(
                modifier = Modifier.padding(10.dp),
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Email: ")
                    }
                    append(patientDetails.email)
                },
                fontSize = 18.sp
            )

            Text(
                modifier = Modifier.padding(10.dp),
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Phone: ")
                    }
                    append(patientDetails.phone)
                },
                fontSize = 18.sp
            )


            Text(
                modifier = Modifier.padding(10.dp),
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Registered At : ")
                    }
                    append(patientDetails.createdDate)
                },
                fontSize = 18.sp
            )

        }

    }

}