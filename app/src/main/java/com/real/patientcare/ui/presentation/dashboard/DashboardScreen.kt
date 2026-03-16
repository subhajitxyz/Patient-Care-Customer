package com.real.patientcare.ui.presentation.dashboard

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.real.patientcare.R
import com.real.patientcare.navigation.Screens
import com.real.patientcare.ui.theme.PrimaryBackgroundBlue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    navController: NavController,
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
    paddingValues: PaddingValues
) {
    val healthStates by dashboardViewModel.healthStates.collectAsStateWithLifecycle()
    val patientBasicInfo by dashboardViewModel.headerInfoState.collectAsStateWithLifecycle()
    val currentDate = remember {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            .format(Date())
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(PrimaryBackgroundBlue)
            .padding(paddingValues),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ParentInfoHeader(patientName = patientBasicInfo.name ,currentDate = currentDate) {
            navController.navigate(route = Screens.ScreenProfile.route)
        }

        //Features List Section
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            elevation = CardDefaults.elevatedCardElevation(8.dp),
            colors = if (isSystemInDarkTheme()) {
                CardDefaults.cardColors(Color.Black)
            } else {
                CardDefaults.cardColors(Color.White)
            }
        ) {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    modifier = Modifier.padding(start = 16.dp , top = 20.dp),
                    text = "Health Metrics",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
                Spacer(modifier = Modifier.fillMaxWidth().height(3.dp))

                if(healthStates.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }

                } else {

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(healthStates.stateList) { item ->
                            DashboardTile(item, currentDate)
                        }
                    }

                }
            }



        }
    }
}

@Composable
fun ParentInfoHeader(
    patientName: String,
    currentDate: String,
    showProfileDetails:() -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column {
            Text(
                text = patientName,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                color = Color.White,
                modifier = Modifier.padding(top = 10.dp)
            )
            Text(
                text = currentDate,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.LightGray,
                modifier = Modifier.padding(top = 6.dp, bottom = 30.dp)
            )
        }

        Image(
            modifier = Modifier.padding(top = 12.dp).size(40.dp)
                .clickable{
                    showProfileDetails()
                },
            painter = painterResource(R.drawable.user_profile_icon),
            contentDescription = "User Profile Icon"
        )

    }
}

@Composable
fun DashboardTile(
    feature: DashboardFeature,
    currentDate: String
) {

    val infiniteTransition = rememberInfiniteTransition(label = "flash")

    val animatedColor by infiniteTransition.animateColor(
        initialValue = feature.color,
        targetValue = Color.Gray,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "TileColor"
    )

    val showEmergencyColor: Boolean = (currentDate == feature.date) && feature.seen
    // if the current date matches with detected date. and seen is true.


    Card(
        elevation = CardDefaults.elevatedCardElevation(12.dp),
        modifier = Modifier.fillMaxWidth()
            .padding(8.dp)
            .clickable {
            },
        colors = CardDefaults.cardColors(containerColor = if(showEmergencyColor) animatedColor else feature.color)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = feature.name,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.4f),
                        offset = Offset(2f, 2f),
                        blurRadius = 6f
                    )
                )
            )
            Spacer(Modifier.height(2.dp))

            Text(
                text = if (feature.seen) "Detected" else "Good Condition",
                color = if (feature.seen) Color.Red else Color.Green,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.4f),
                        offset = Offset(2f, 2f),
                        blurRadius = 6f
                    )
                )
            )
            Spacer(Modifier.height(4.dp))

            Box(
                modifier = Modifier.align(Alignment.CenterHorizontally)
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(5.dp),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${feature.accuracy}%",
                        color = Color.Black,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Spacer(Modifier.height(1.dp))

                    Text(
                        text = "Accuracy",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
            }
            Spacer(Modifier.height(6.dp))

            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                text = feature.date + " " + feature.time,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Thin,
                maxLines = 2,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.4f),
                        offset = Offset(2f, 2f),
                        blurRadius = 2f
                    )
                )
            )
        }
    }
}
