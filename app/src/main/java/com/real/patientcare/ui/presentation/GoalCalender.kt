//package com.real.patientcare.ui.presentation
//
//import androidx.compose.animation.AnimatedContent
//import androidx.compose.animation.AnimatedContentTransitionScope
//import androidx.compose.animation.AnimatedVisibilityScope
//import androidx.compose.animation.ContentTransform
//import androidx.compose.animation.ExperimentalSharedTransitionApi
//import androidx.compose.animation.SharedTransitionLayout
//import androidx.compose.animation.SharedTransitionScope
//import androidx.compose.animation.animateColorAsState
//import androidx.compose.animation.core.FastOutSlowInEasing
//import androidx.compose.animation.core.Spring
//import androidx.compose.animation.core.animateDpAsState
//import androidx.compose.animation.core.animateFloatAsState
//import androidx.compose.animation.core.spring
//import androidx.compose.animation.core.tween
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.animation.scaleIn
//import androidx.compose.animation.scaleOut
//import androidx.compose.animation.slideInHorizontally
//import androidx.compose.animation.slideOutHorizontally
//import androidx.compose.animation.togetherWith
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.rounded.CheckCircle
//import androidx.compose.material.icons.rounded.ChevronLeft
//import androidx.compose.material.icons.rounded.ChevronRight
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.Stable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableIntStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.drawWithCache
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.BlendMode
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.graphicsLayer
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.real.patientcare.ui.presentation.appTransitionSpec
//import kotlinx.coroutines.launch
//import java.time.LocalDate
//import java.time.format.TextStyle
//import java.util.Locale
//import kotlin.math.max
//
///**
// * Global color palette and gradient brushes.
// * Marked as @Stable to ensure compose compiler optimization.
// */
//@Stable
//object AppTheme {
//    val Background = Color(0xFFF4F6F8)
//    val Surface = Color(0xFFFFFFFF)
//    val TextPrimary = Color(0xFF111827)
//    val TextSecondary = Color(0xFF6B7280)
//    val Track = Color(0xFFE5E7EB)
//
//    val Steps = Color(0xFF10B981)
//    val Cals = Color(0xFFF59E0B)
//    val Act = Color(0xFF3B82F6)
//
//    val Sleep = Color(0xFF8B5CF6)
//    val SleepDeep = Color(0xFF4C1D95)
//    val SleepRem = Color(0xFF2DD4BF)
//    val SleepLight = Color(0xFF8B5CF6)
//    val SleepAwake = Color(0xFFF97316)
//
//    val Heart = Color(0xFFF43F5E)
//    val Stress = Color(0xFFF59E0B)
//
//    val CalendarGradient = Brush.linearGradient(
//        colors = listOf(Color(0xFF4338CA), Color(0xFF3B82F6)),
//        start = Offset(0f, 0f), end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
//    )
//
//    val TextGradient = Brush.linearGradient(colors = listOf(Color(0xFF1E1B4B), Color(0xFF4338CA)))
//    val VitalityGradient =
//        Brush.linearGradient(colors = listOf(Color(0xFF3B82F6), Color(0xFF10B981)))
//    val SleepGradient = Brush.linearGradient(colors = listOf(Color(0xFF4C1D95), Color(0xFF8B5CF6)))
//    val BPMGradient = Brush.linearGradient(colors = listOf(Color(0xFFE11D48), Color(0xFFFB7185)))
//    val StressGradient = Brush.linearGradient(colors = listOf(Color(0xFFFCD34D), Color(0xFFF59E0B)))
//}
//
//@Composable
//fun GoalCalendar(
//    calendarDays: List<CalendarDay>,
//    selectedIndex: Int,
//    onIndexChange: (Int) -> Unit,
//    onCalendarClick: () -> Unit,
//    sharedScope: SharedTransitionScope,
//    animScope: AnimatedVisibilityScope
//) {
//    val coroutineScope = rememberCoroutineScope()
//    val listState = rememberLazyListState(initialFirstVisibleItemIndex = max(0, selectedIndex - 2))
//    val selectedDate = calendarDays[selectedIndex].date
//    val monthName = selectedDate.month.getDisplayName(JavaTextStyle.FULL, Locale.getDefault())
//    val yearName = selectedDate.year
//
//    Box(modifier = Modifier
//        .fillMaxWidth()
//        .cardStyle()
//        .padding(vertical = 24.dp)) {
//        Column(modifier = Modifier.fillMaxWidth()) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 24.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    Icons.Rounded.ChevronLeft,
//                    contentDescription = "Previous",
//                    modifier = Modifier
//                        .size(28.dp)
//                        .clip(CircleShape)
//                        .clickable {
//                            val targetDate = selectedDate.minusMonths(1).withDayOfMonth(1)
//                            val targetIndex = calendarDays.indexOfFirst { it.date == targetDate }
//                            if (targetIndex != -1) {
//                                onIndexChange(targetIndex); coroutineScope.launch {
//                                    listState.animateScrollToItem(
//                                        max(0, targetIndex - 2)
//                                    )
//                                }
//                            }
//                        }
//                        .graphicsLayer(alpha = 0.99f)
//                        .drawWithCache {
//                            onDrawWithContent {
//                                drawContent(); drawRect(
//                                AppTheme.CalendarGradient,
//                                blendMode = BlendMode.SrcAtop
//                            )
//                            }
//                        }
//                )
//                Text(
//                    "$monthName $yearName",
//                    color = AppTheme.TextPrimary,
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold
//                )
//                Icon(
//                    Icons.Rounded.ChevronRight,
//                    contentDescription = "Next",
//                    modifier = Modifier
//                        .size(28.dp)
//                        .clip(CircleShape)
//                        .clickable {
//                            val targetDate = selectedDate.plusMonths(1).withDayOfMonth(1)
//                            val targetIndex = calendarDays.indexOfFirst { it.date == targetDate }
//                            if (targetIndex != -1) {
//                                onIndexChange(targetIndex); coroutineScope.launch {
//                                    listState.animateScrollToItem(
//                                        max(0, targetIndex - 2)
//                                    )
//                                }
//                            }
//                        }
//                        .graphicsLayer(alpha = 0.99f)
//                        .drawWithCache {
//                            onDrawWithContent {
//                                drawContent(); drawRect(
//                                AppTheme.CalendarGradient,
//                                blendMode = BlendMode.SrcAtop
//                            )
//                            }
//                        }
//                )
//            }
//            Spacer(modifier = Modifier.height(20.dp))
//            LazyRow(
//                state = listState,
//                contentPadding = PaddingValues(horizontal = 24.dp),
//                horizontalArrangement = Arrangement.spacedBy(12.dp),
//                verticalAlignment = Alignment.CenterVertically,
//                flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(130.dp)
//            ) {
//                itemsIndexed(
//                    items = calendarDays,
//                    key = { _, day -> day.date.toEpochDay() }) { index, dayInfo ->
//                    val isSelected = selectedIndex == index
//                    val dayOfWeek = dayInfo.date.dayOfWeek.getDisplayName(
//                        JavaTextStyle.SHORT,
//                        Locale.getDefault()
//                    )
//                    val dayOfMonth = dayInfo.date.dayOfMonth.toString().padStart(2, '0')
//                    val monthShort =
//                        dayInfo.date.month.getDisplayName(JavaTextStyle.SHORT, Locale.getDefault())
//
//                    val cardSpring = spring<androidx.compose.ui.unit.Dp>(
//                        dampingRatio = 0.6f,
//                        stiffness = Spring.StiffnessLow
//                    )
//                    val width by animateDpAsState(
//                        if (isSelected) 84.dp else 68.dp,
//                        cardSpring,
//                        label = "width"
//                    )
//                    val height by animateDpAsState(
//                        if (isSelected) 124.dp else 108.dp,
//                        cardSpring,
//                        label = "height"
//                    )
//
//                    val selectionAlpha by animateFloatAsState(
//                        if (isSelected) 1f else 0f,
//                        tween(300),
//                        label = "alpha"
//                    )
//                    val primaryTextColor by animateColorAsState(
//                        if (isSelected) AppTheme.Surface else AppTheme.TextPrimary,
//                        tween(300),
//                        label = "text1"
//                    )
//                    val secondaryTextColor by animateColorAsState(
//                        if (isSelected) AppTheme.Surface.copy(
//                            alpha = 0.8f
//                        ) else AppTheme.TextSecondary, tween(300), label = "text2"
//                    )
//                    val checkColor by animateColorAsState(
//                        if (isSelected) AppTheme.Surface else AppTheme.Steps,
//                        tween(300),
//                        label = "check"
//                    )
//                    val unselectedBg = Color(0xFFF8FAFC)
//
//                    Box(
//                        modifier = Modifier
//                            .width(width)
//                            .height(height)
//                            .then(
//                                if (isSelected) {
//                                    with(sharedScope) {
//                                        Modifier.sharedBounds(
//                                            rememberSharedContentState(
//                                                "calendar_card_bg"
//                                            ),
//                                            animScope,
//                                            resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(),
//                                            boundsTransform = ProSpatialTransform
//                                        )
//                                    }
//                                } else Modifier)
//                            .shadow(
//                                if (isSelected) 16.dp else 0.dp,
//                                RoundedCornerShape(24.dp),
//                                ambientColor = Color(0x05000000),
//                                spotColor = Color(0x0A000000)
//                            )
//                            .background(unselectedBg, RoundedCornerShape(24.dp))
//                            .border(
//                                1.dp,
//                                Brush.linearGradient(
//                                    colors = listOf(
//                                        Color(0xFF4338CA).copy(alpha = 1f - selectionAlpha),
//                                        Color(0xFF3B82F6).copy(alpha = 1f - selectionAlpha)
//                                    )
//                                ),
//                                RoundedCornerShape(24.dp)
//                            )
//                            .clip(RoundedCornerShape(24.dp))
//                            .clickable(
//                                remember { MutableInteractionSource() },
//                                null
//                            ) { if (isSelected) onCalendarClick() else onIndexChange(index) }
//                    ) {
//                        Box(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .graphicsLayer { alpha = selectionAlpha }
//                                .background(AppTheme.CalendarGradient)
//                        )
//                        Box(
//                            modifier = Modifier.fillMaxSize(),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Column(
//                                horizontalAlignment = Alignment.CenterHorizontally,
//                                verticalArrangement = Arrangement.Center
//                            ) {
//                                val dayMod = if (isSelected) with(sharedScope) {
//                                    Modifier.sharedBounds(
//                                        rememberSharedContentState("cal_day"),
//                                        animScope,
//                                        resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(),
//                                        boundsTransform = ProSpatialTransform
//                                    )
//                                } else Modifier
//                                val dateMod = if (isSelected) with(sharedScope) {
//                                    Modifier.sharedBounds(
//                                        rememberSharedContentState("cal_date"),
//                                        animScope,
//                                        resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(),
//                                        boundsTransform = ProSpatialTransform
//                                    )
//                                } else Modifier
//                                val monthMod = if (isSelected) with(sharedScope) {
//                                    Modifier.sharedBounds(
//                                        rememberSharedContentState("cal_month"),
//                                        animScope,
//                                        resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(),
//                                        boundsTransform = ProSpatialTransform
//                                    )
//                                } else Modifier
//
//                                Text(
//                                    dayOfWeek,
//                                    color = secondaryTextColor,
//                                    fontSize = 13.sp,
//                                    fontWeight = FontWeight.SemiBold,
//                                    modifier = dayMod
//                                )
//                                Spacer(modifier = Modifier.height(2.dp))
//                                Text(
//                                    dayOfMonth,
//                                    color = primaryTextColor,
//                                    fontSize = 34.sp,
//                                    fontWeight = FontWeight.Black,
//                                    modifier = dateMod
//                                )
//                                Spacer(modifier = Modifier.height(2.dp))
//                                Text(
//                                    if (dayInfo.isToday) "today" else monthShort,
//                                    color = secondaryTextColor,
//                                    fontSize = 11.sp,
//                                    fontWeight = FontWeight.Bold,
//                                    modifier = monthMod
//                                )
//                                Spacer(modifier = Modifier.height(6.dp))
//                                if (dayInfo.goalAchieved) Icon(
//                                    Icons.Rounded.CheckCircle,
//                                    contentDescription = "Achieved",
//                                    tint = checkColor,
//                                    modifier = Modifier.size(16.dp)
//                                )
//                                else Spacer(modifier = Modifier.height(16.dp))
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//
//@OptIn(ExperimentalSharedTransitionApi::class)
//@Composable
//fun Dashboard(modifier: Modifier = Modifier) {
//
//    var currentScreen by remember { mutableStateOf(HealthAppScreen.Dashboard) }
//
//    // HOISTED: Persistent scroll state ensures the dashboard remembers its position.
//    val dashboardScrollState = rememberScrollState()
//
//    val today = remember { LocalDate.now() }
//    val calendarDays = remember {
//        (-180..180).map { offset ->
//            val date = today.plusDays(offset.toLong())
//            val isAchieved = offset <= 0 && date.dayOfMonth % 3 != 0
//            CalendarDay(date, date == today, isAchieved)
//        }
//    }
//    var selectedCalendarIndex by remember { mutableIntStateOf(180) }
//
//    val morphProgressState = animateFloatAsState(
//        targetValue = if (currentScreen == HealthAppScreen.Details) 1f else 0f,
//        animationSpec = MorphSpring,
//        label = "morph"
//    )
//    val morphProgressProvider = remember { { morphProgressState.value } }
//
//    SharedTransitionLayout {
//        AnimatedContent(
//            targetState = currentScreen,
//            label = "app_navigation",
//            transitionSpec = { appTransitionSpec() }) { targetScreen ->
//
//            GoalCalendar(
//                calendarDays,
//                selectedCalendarIndex,
//                onIndexChange = { selectedCalendarIndex = it },
//                onCalendarClick = { currentScreen = HealthAppScreen.CalendarDetails },
//                sharedScope = this@SharedTransitionLayout,
//                animScope = this@AnimatedContent,
//            )
//
//        }
//    }
//
//
//
//}
//
//
///**
// * Custom spatial physics determining how screens enter and exit the view.
// */
//private fun AnimatedContentTransitionScope<HealthAppScreen>.appTransitionSpec(): ContentTransform {
//    val isProfileTransition =
//        targetState == HealthAppScreen.Profile || initialState == HealthAppScreen.Profile
//    return if (isProfileTransition) {
//        val slideLeft = targetState == HealthAppScreen.Profile
//        val enter = slideInHorizontally(
//            animationSpec = SpatialSpring,
//            initialOffsetX = { if (slideLeft) it else -it }) + fadeIn(tween(400))
//        val exit = slideOutHorizontally(
//            animationSpec = SpatialSpring,
//            targetOffsetX = { if (slideLeft) -it else it }) + scaleOut(
//            targetScale = 0.9f,
//            animationSpec = ScaleSpring
//        ) + fadeOut(tween(400))
//        enter togetherWith exit
//    } else {
//        val enter = fadeIn(tween(400, easing = FastOutSlowInEasing)) + scaleIn(
//            initialScale = 0.92f,
//            animationSpec = DepthSpring
//        )
//        val exit = fadeOut(tween(300, easing = FastOutSlowInEasing)) + scaleOut(
//            targetScale = 0.92f,
//            animationSpec = DepthSpring
//        )
//        enter togetherWith exit
//    }
//}
