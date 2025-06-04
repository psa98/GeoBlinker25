package com.example.geoblinker.ui.main.profile.settings

import android.annotation.SuppressLint
import android.app.Application
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
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.stylusHoverIcon
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.geoblinker.R
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.CustomLinkEmailPopup
import com.example.geoblinker.ui.main.viewmodel.NotificationViewModel
import com.example.geoblinker.ui.main.viewmodel.ProfileViewModel
import com.example.geoblinker.ui.theme.Manrope
import com.example.geoblinker.ui.theme.black
import com.example.geoblinker.ui.theme.sc
import com.example.geoblinker.ui.theme.sdp
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.abs

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
}

/**
 * TODO: Доделать попап с выбором времени
 */
@Composable
private fun CustomTimePopup(
    initialHour: Int,
    initialMinute: Int,
    confirm: () -> Unit,
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
                    Row {

                    }
                }
            }
        }
    }
}

private fun Int.toTimeString(): String {
    val hours = this / 60
    val minutes = this % 60
    return "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}"
}