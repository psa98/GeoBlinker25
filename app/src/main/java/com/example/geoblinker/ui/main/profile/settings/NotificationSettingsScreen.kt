package com.example.geoblinker.ui.main.profile.settings

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.geoblinker.R
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.CustomButton
import com.example.geoblinker.ui.CustomLinkEmailPopup
import com.example.geoblinker.ui.TypeColor
import com.example.geoblinker.ui.main.viewmodel.NotificationViewModel
import com.example.geoblinker.ui.main.viewmodel.ProfileViewModel
import com.example.geoblinker.ui.theme.sc
import com.example.geoblinker.ui.theme.sdp

private enum class Notification {
    Main,
    Types,
    Email
}

@Composable
fun NotificationSettingsScreen(
    viewModel: NotificationViewModel,
    profileViewModel: ProfileViewModel,
    toLinkEmail: () -> Unit,
    toBack: () -> Unit,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController,
        startDestination = Notification.Main.name
    ) {
        composable(route = Notification.Main.name) {
            Main(
                viewModel,
                profileViewModel,
                toConfigureNotificationType = { navController.navigate(Notification.Types.name) },
                toConfigureEmailNotification = {
                    viewModel.toEmail(true)
                    navController.navigate(Notification.Email.name)
                },
                toLinkEmail = toLinkEmail,
                toBack = toBack
            )
        }
        composable(route = Notification.Types.name) {
            Types(
                viewModel,
                toBack = { navController.navigateUp() }
            )
        }
        composable(route = Notification.Email.name) {
            Email(
                viewModel,
                toBack = {
                    viewModel.toEmail(false)
                    navController.navigateUp()
                }
            )
        }
    }
}

@Composable
private fun Main(
    viewModel: NotificationViewModel,
    profileViewModel: ProfileViewModel,
    toConfigureNotificationType: () -> Unit,
    toConfigureEmailNotification: () -> Unit,
    toLinkEmail: () -> Unit,
    toBack: () -> Unit
) {
    val quietMode by viewModel.quietMode.collectAsState()
    val getAllNotification by viewModel.getAllNotification.collectAsState()
    val emailNotification by viewModel.emailNotification.collectAsState()
    val getNotificationAroundClock by viewModel.getNotificationAroundClock.collectAsState()
    val startTime by viewModel.startTime.collectAsState()
    val endTime by viewModel.endTime.collectAsState()
    val email by profileViewModel.email.collectAsState()
    var isShow by remember { mutableStateOf(false) }
    var isShowStart by remember { mutableStateOf(false) }
    var isShowEnd by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.setting_up_signals),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(Modifier.height(70.sdp()))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.width(200.sdp())
            ) {
                Text(
                    stringResource(R.string.quiet_mode),
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(Modifier.height(6.sdp()))
                Text(
                    stringResource(R.string.quiet_mode_description),
                    color = Color(0xFF5D5D5D),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 10.sp * sc()
                    )
                )
            }
            Switch(
                quietMode,
                { viewModel.setQuietMode(it) },
                modifier = Modifier.size(40.sdp(), 21.sdp()),
                colors = SwitchDefaults.colors(
                    uncheckedThumbColor = Color(0xFFBEBEBE),
                    uncheckedTrackColor = Color.Unspecified,
                    uncheckedBorderColor = Color(0xFFBEBEBE),
                    checkedThumbColor = Color(0xFF12CD4A),
                    checkedTrackColor = Color.Unspecified,
                    checkedBorderColor = Color(0xFFBEBEBE),
                )
            )
        }
        Spacer(Modifier.height(20.sdp()))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                stringResource(R.string.get_all_notification),
                style = MaterialTheme.typography.labelMedium
            )
            Switch(
                getAllNotification,
                { viewModel.setGetAllNotification(it) },
                modifier = Modifier.size(40.sdp(), 21.sdp()),
                colors = SwitchDefaults.colors(
                    uncheckedThumbColor = Color(0xFFBEBEBE),
                    uncheckedTrackColor = Color.Unspecified,
                    uncheckedBorderColor = Color(0xFFBEBEBE),
                    checkedThumbColor = Color(0xFF12CD4A),
                    checkedTrackColor = Color.Unspecified,
                    checkedBorderColor = Color(0xFFBEBEBE),
                )
            )
        }
        Spacer(Modifier.height(20.sdp()))
        if (!getAllNotification) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFDAD9D9),
                thickness = 1.sdp()
            )
            Spacer(Modifier.height(8.sdp()))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    stringResource(R.string.configure_notification_type),
                    modifier = Modifier.clickable { toConfigureNotificationType() },
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(Modifier.height(283.sdp()))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.width(200.sdp())
            ) {
                Text(
                    stringResource(R.string.email_notification),
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(Modifier.height(6.sdp()))
                Text(
                    stringResource(R.string.email_notification_description),
                    color = Color(0xFF5D5D5D),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 10.sp * sc()
                    )
                )
            }
            Switch(
                emailNotification,
                {
                    if (email.isEmpty())
                        isShow = true
                    else
                        viewModel.setEmailNotification(it)
                },
                modifier = Modifier.size(40.sdp(), 21.sdp()),
                colors = SwitchDefaults.colors(
                    uncheckedThumbColor = Color(0xFFBEBEBE),
                    uncheckedTrackColor = Color.Unspecified,
                    uncheckedBorderColor = Color(0xFFBEBEBE),
                    checkedThumbColor = Color(0xFF12CD4A),
                    checkedTrackColor = Color.Unspecified,
                    checkedBorderColor = Color(0xFFBEBEBE),
                )
            )
        }
        Spacer(Modifier.height(20.sdp()))
        if (emailNotification) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFDAD9D9),
                thickness = 1.sdp()
            )
            Spacer(Modifier.height(8.sdp()))
            Text(
                stringResource(R.string.configure_notification_type),
                modifier = Modifier.clickable { toConfigureEmailNotification() },
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(Modifier.height(30.sdp()))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                stringResource(R.string.get_notification_around_clock),
                style = MaterialTheme.typography.labelMedium
            )
            Switch(
                getNotificationAroundClock,
                { viewModel.setGetNotificationAroundClock(it) },
                modifier = Modifier.size(40.sdp(), 21.sdp()),
                colors = SwitchDefaults.colors(
                    uncheckedThumbColor = Color(0xFFBEBEBE),
                    uncheckedTrackColor = Color.Unspecified,
                    uncheckedBorderColor = Color(0xFFBEBEBE),
                    checkedThumbColor = Color(0xFF12CD4A),
                    checkedTrackColor = Color.Unspecified,
                    checkedBorderColor = Color(0xFFBEBEBE),
                )
            )
        }
        Spacer(Modifier.height(20.sdp()))
        if (!getNotificationAroundClock) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFDAD9D9),
                thickness = 1.sdp()
            )
            Spacer(Modifier.height(8.sdp()))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.receive_notifications),
                    style = MaterialTheme.typography.labelMedium
                )
                Row(
                    modifier = Modifier.width(150.sdp()),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        stringResource(R.string.from),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        startTime.toTimeString(),
                        modifier = Modifier.clickable {
                            isShowStart = true
                        },
                        textDecoration = TextDecoration.Underline,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        stringResource(R.string.to),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        endTime.toTimeString(),
                        modifier = Modifier.clickable {
                            isShowEnd = true
                        },
                        textDecoration = TextDecoration.Underline,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }

    BackButton(
        onClick = toBack
    )

    if (isShow) {
        CustomLinkEmailPopup(
            toLinkEmail = toLinkEmail,
            cancellation = { isShow = false }
        )
    }

    if (isShowStart) {
        CustomTimePopup(
            startTime / 60,
            startTime % 60,
            confirm = {
                viewModel.setStartTime(it)
                isShowStart = false
            },
            cancellation = { isShowStart = false }
        )
    }

    if (isShowEnd) {
        CustomTimePopup(
            endTime / 60,
            endTime % 60,
            confirm = {
                viewModel.setEndTime(it)
                isShowEnd = false
            },
            cancellation = { isShowEnd = false }
        )
    }
}

@Composable
private fun Types(
    viewModel: NotificationViewModel,
    toBack: () -> Unit
) {
    val allNotification by viewModel.allNotification.collectAsState()
    val news by viewModel.news.collectAsState()
    val typesSignals by viewModel.typesSignals.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                stringResource(R.string.configuring_notification_types),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(Modifier.height(70.sdp()))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.enable_all),
                    style = MaterialTheme.typography.labelMedium
                )
                Switch(
                    allNotification,
                    { viewModel.setAllNotification(it) },
                    modifier = Modifier.size(40.sdp(), 21.sdp()),
                    colors = SwitchDefaults.colors(
                        uncheckedThumbColor = Color(0xFFBEBEBE),
                        uncheckedTrackColor = Color.Unspecified,
                        uncheckedBorderColor = Color(0xFFBEBEBE),
                        checkedThumbColor = Color(0xFF12CD4A),
                        checkedTrackColor = Color.Unspecified,
                        checkedBorderColor = Color(0xFFBEBEBE),
                    )
                )
            }
            Spacer(Modifier.height(20.sdp()))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.news),
                    style = MaterialTheme.typography.labelMedium
                )
                Switch(
                    news,
                    { viewModel.setNews(it) },
                    modifier = Modifier.size(40.sdp(), 21.sdp()),
                    colors = SwitchDefaults.colors(
                        uncheckedThumbColor = Color(0xFFBEBEBE),
                        uncheckedTrackColor = Color.Unspecified,
                        uncheckedBorderColor = Color(0xFFBEBEBE),
                        checkedThumbColor = Color(0xFF12CD4A),
                        checkedTrackColor = Color.Unspecified,
                        checkedBorderColor = Color(0xFFBEBEBE),
                    )
                )
            }
            Spacer(Modifier.height(40.sdp()))
        }
        itemsIndexed(typesSignals) { index, typeSignal ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(typeSignal.type.description),
                    style = MaterialTheme.typography.labelMedium
                )
                Switch(
                    typeSignal.checked,
                    { viewModel.setTypeSignal(index, it) },
                    modifier = Modifier.size(40.sdp(), 21.sdp()),
                    colors = SwitchDefaults.colors(
                        uncheckedThumbColor = Color(0xFFBEBEBE),
                        uncheckedTrackColor = Color.Unspecified,
                        uncheckedBorderColor = Color(0xFFBEBEBE),
                        checkedThumbColor = Color(0xFF12CD4A),
                        checkedTrackColor = Color.Unspecified,
                        checkedBorderColor = Color(0xFFBEBEBE),
                    )
                )
            }
            Spacer(Modifier.height(20.sdp()))
        }
    }

    BackButton(
        onClick = toBack
    )
}

@Composable
private fun Email(
    viewModel: NotificationViewModel,
    toBack: () -> Unit
) {
    val allNotification by viewModel.allNotification.collectAsState()
    val news by viewModel.news.collectAsState()
    val getNotificationAroundClock by viewModel.getNotificationAroundClock.collectAsState()
    val startTime by viewModel.startTime.collectAsState()
    val endTime by viewModel.endTime.collectAsState()
    val typesSignals by viewModel.typesSignals.collectAsState()
    var isShowStart by remember { mutableStateOf(false) }
    var isShowEnd by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                stringResource(R.string.configuring_notification_email),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(Modifier.height(70.sdp()))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.enable_all),
                    style = MaterialTheme.typography.labelMedium
                )
                Switch(
                    allNotification,
                    { viewModel.setAllNotification(it) },
                    modifier = Modifier.size(40.sdp(), 21.sdp()),
                    colors = SwitchDefaults.colors(
                        uncheckedThumbColor = Color(0xFFBEBEBE),
                        uncheckedTrackColor = Color.Unspecified,
                        uncheckedBorderColor = Color(0xFFBEBEBE),
                        checkedThumbColor = Color(0xFF12CD4A),
                        checkedTrackColor = Color.Unspecified,
                        checkedBorderColor = Color(0xFFBEBEBE),
                    )
                )
            }
            Spacer(Modifier.height(20.sdp()))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.news),
                    style = MaterialTheme.typography.labelMedium
                )
                Switch(
                    news,
                    { viewModel.setNews(it) },
                    modifier = Modifier.size(40.sdp(), 21.sdp()),
                    colors = SwitchDefaults.colors(
                        uncheckedThumbColor = Color(0xFFBEBEBE),
                        uncheckedTrackColor = Color.Unspecified,
                        uncheckedBorderColor = Color(0xFFBEBEBE),
                        checkedThumbColor = Color(0xFF12CD4A),
                        checkedTrackColor = Color.Unspecified,
                        checkedBorderColor = Color(0xFFBEBEBE),
                    )
                )
            }
            Spacer(Modifier.height(20.sdp()))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.get_notification_around_clock),
                    style = MaterialTheme.typography.labelMedium
                )
                Switch(
                    getNotificationAroundClock,
                    { viewModel.setGetNotificationAroundClock(it) },
                    modifier = Modifier.size(40.sdp(), 21.sdp()),
                    colors = SwitchDefaults.colors(
                        uncheckedThumbColor = Color(0xFFBEBEBE),
                        uncheckedTrackColor = Color.Unspecified,
                        uncheckedBorderColor = Color(0xFFBEBEBE),
                        checkedThumbColor = Color(0xFF12CD4A),
                        checkedTrackColor = Color.Unspecified,
                        checkedBorderColor = Color(0xFFBEBEBE),
                    )
                )
            }
            if (getNotificationAroundClock) {
                Spacer(Modifier.height(11.sdp()))
            }
            else {
                Spacer(Modifier.height(20.sdp()))
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFDAD9D9),
                    thickness = 1.sdp()
                )
                Spacer(Modifier.height(8.sdp()))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        stringResource(R.string.receive_notifications),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Row(
                        modifier = Modifier.width(150.sdp()),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            stringResource(R.string.from),
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            startTime.toTimeString(),
                            modifier = Modifier.clickable {
                                isShowStart = true
                            },
                            textDecoration = TextDecoration.Underline,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            stringResource(R.string.to),
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            endTime.toTimeString(),
                            modifier = Modifier.clickable {
                                isShowEnd = true
                            },
                            textDecoration = TextDecoration.Underline,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
            Spacer(Modifier.height(29.sdp()))
        }
        itemsIndexed(typesSignals) { index, typeSignal ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(typeSignal.type.description),
                    style = MaterialTheme.typography.labelMedium
                )
                Switch(
                    typeSignal.checked,
                    { viewModel.setTypeSignal(index, it) },
                    modifier = Modifier.size(40.sdp(), 21.sdp()),
                    colors = SwitchDefaults.colors(
                        uncheckedThumbColor = Color(0xFFBEBEBE),
                        uncheckedTrackColor = Color.Unspecified,
                        uncheckedBorderColor = Color(0xFFBEBEBE),
                        checkedThumbColor = Color(0xFF12CD4A),
                        checkedTrackColor = Color.Unspecified,
                        checkedBorderColor = Color(0xFFBEBEBE),
                    )
                )
            }
            Spacer(Modifier.height(20.sdp()))
        }
    }

    BackButton(
        onClick = toBack
    )

    if (isShowStart) {
        CustomTimePopup(
            startTime / 60,
            startTime % 60,
            confirm = {
                viewModel.setStartTime(it)
                isShowStart = false
            },
            cancellation = { isShowStart = false }
        )
    }

    if (isShowEnd) {
        CustomTimePopup(
            endTime / 60,
            endTime % 60,
            confirm = {
                viewModel.setEndTime(it)
                isShowEnd = false
            },
            cancellation = { isShowEnd = false }
        )
    }
}

/**
 * TODO: Доделать попап с выбором времени
 */
@Composable
private fun CustomTimePopup(
    initialHour: Int,
    initialMinute: Int,
    confirm: (Int) -> Unit,
    cancellation: () -> Unit
) {
    var hour by remember { mutableIntStateOf(initialHour) }
    var minute by remember { mutableIntStateOf(initialMinute) }

    Dialog(
        {},
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.2f))
                .clickable { cancellation() },
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier.width(310.sdp()),
                shape = RoundedCornerShape(16.sdp()),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = 25.sdp(),
                        vertical = 30.sdp()
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TimePickerColumn(
                            hour,
                            onValueChange = { hour = it },
                            range = 0..23
                        )
                        Text(
                            ":",
                            style = MaterialTheme.typography.headlineLarge
                        )
                        TimePickerColumn(
                            minute,
                            onValueChange = { minute = it },
                            range = 0..60
                        )
                    }
                    Spacer(Modifier.height(28.sdp()))
                    CustomButton(
                        text = stringResource(R.string.confirm),
                        onClick = { confirm(hour * 60 + minute) },
                        typeColor = TypeColor.Green,
                        height = 55
                    )
                    Spacer(Modifier.height(10.sdp()))
                    CustomButton(
                        text = stringResource(R.string.cancellation),
                        onClick = cancellation,
                        typeColor = TypeColor.White,
                        height = 55
                    )
                }
            }
        }
    }
}

private const val VISIBLE_ITEMS_COUNT = 3
private const val ITEM_HEIGHT = 28
private const val LIST_HEIGHT = VISIBLE_ITEMS_COUNT * ITEM_HEIGHT

@Composable
fun TimePickerColumn(
    initialValue: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier.width(80.sdp())
) {
    val context = LocalContext.current
    val items = remember {
        buildList {
            repeat(VISIBLE_ITEMS_COUNT / 2) { add("") }
            range.forEach { add(it.formatTwoDigits()) }
            repeat(VISIBLE_ITEMS_COUNT / 2) { add("") }
        }
    }

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = initialValue
    )
    val one = 1.sdp()
    val itemHeight = ITEM_HEIGHT.sdp()
    val brush = Brush.verticalGradient(
        colors = listOf(Color.White, Color.White.copy(alpha = 0f), Color.White)
    )

    // Логика выравнивания при скролле
    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val targetIndex = listState.centerItemIndex(context)
            listState.animateScrollToItem(targetIndex)
            onValueChange(targetIndex.coerceIn(range))
        }
    }

    Box(
        modifier = modifier
            .height(LIST_HEIGHT.sdp())
            .border(1.sdp(), Color(0xFFDAD9D9), RoundedCornerShape(8.sdp())),
        contentAlignment = Alignment.Center
    ) {
        // Рамка выбранного элемента
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawLine(
                color = Color(0xFF5D5D5D),
                strokeWidth = one.toPx(),
                start = Offset(0f, size.height / 2 - itemHeight.toPx() / 2),
                end = Offset(size.width, size.height / 2 - itemHeight.toPx() / 2)
            )
            drawLine(
                color = Color(0xFF5D5D5D),
                strokeWidth = one.toPx(),
                start = Offset(0f, size.height / 2 + itemHeight.toPx() / 2),
                end = Offset(size.width, size.height / 2 + itemHeight.toPx() / 2)
            )
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(items) { index, item ->
                Text(
                    text = item,
                    modifier = Modifier
                        .size(width = 60.sdp(), height = ITEM_HEIGHT.sdp())
                        .wrapContentSize(Alignment.Center),
                    color = if (item.isNotEmpty()) Color.Black else Color.Transparent,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .background(brush, RoundedCornerShape(8.sdp()))
        )
    }
}

@SuppressLint("DefaultLocale")
fun Int.formatTwoDigits(): String {
    return String.format("%02d", this)
}

// Функция для вычисления центрального элемента
private fun LazyListState.centerItemIndex(context: Context): Int {
    val firstVisible = firstVisibleItemIndex
    val offset = firstVisibleItemScrollOffset.toDp(context)
    return when {
        offset % ITEM_HEIGHT >= ITEM_HEIGHT / 2 -> firstVisible + 1
        else -> firstVisible
    }
}

// Конвертация пикселей в DP
private fun Int.toDp(context: Context): Float {
    return this / context.resources.displayMetrics.density
}

private fun Int.toTimeString(): String {
    val hours = this / 60
    val minutes = this % 60
    return "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}"
}