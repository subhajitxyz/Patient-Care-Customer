package com.yourapp.ui.components
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class HistoryDate(
    val date: Date,
    val isToday: Boolean
)

@Composable
fun PatientHistoryCalendar(
    modifier: Modifier = Modifier,
    onDateClick: (Date) -> Unit
) {

    val todayCalendar = remember {
        Calendar.getInstance()
    }

    val dates = remember {

        val list = mutableListOf<HistoryDate>()

        for (offset in -15..0) {

            val calendar = Calendar.getInstance()

            calendar.add(Calendar.DAY_OF_YEAR, offset)

            val isToday = offset == 0

            list.add(
                HistoryDate(
                    date = calendar.time,
                    isToday = isToday
                )
            )
        }

        list
    }

    var selectedIndex by rememberSaveable {
        mutableIntStateOf(dates.lastIndex)
    }

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = dates.lastIndex
    )

    val dayFormatter = remember {
        SimpleDateFormat("EEE", Locale.getDefault())
    }

    val monthFormatter = remember {
        SimpleDateFormat("MMM", Locale.getDefault())
    }

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {

        itemsIndexed(dates) { index, item ->

            val selected = index == selectedIndex

            val backgroundColor by animateColorAsState(
                targetValue =
                    if (selected)
                        MaterialTheme.colorScheme.primary
                    else
                        Color(0xFFF5F5F5),
                label = ""
            )

            val textColor by animateColorAsState(
                targetValue =
                    if (selected)
                        Color.White
                    else
                        Color.Black,
                label = ""
            )

            val calendar = Calendar.getInstance()
            calendar.time = item.date

            val dayNumber =
                calendar.get(Calendar.DAY_OF_MONTH).toString()

            Card(
                modifier = Modifier
                    .width(75.dp)
                    .height(110.dp)
                    .clickable {

                        selectedIndex = index

                        onDateClick(item.date)
                    },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = backgroundColor
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation =
                        if (selected) 6.dp else 1.dp
                )
            ) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = dayFormatter.format(item.date),
                        color = textColor,
                        fontSize = 12.sp
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text = dayNumber,
                        color = textColor,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text =
                            if (item.isToday)
                                "Today"
                            else
                                monthFormatter.format(item.date),
                        color = textColor,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun Dash(modifier: Modifier = Modifier) {
    PatientHistoryCalendar(
        onDateClick = { date ->

            val selectedDate =
                SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).format(date)

//            navController.navigate(
//                "history/$selectedDate"
//            )
        }
    )
}