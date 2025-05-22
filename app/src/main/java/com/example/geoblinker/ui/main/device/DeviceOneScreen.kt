package com.example.geoblinker.ui.main.device

import android.Manifest
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import com.example.geoblinker.R
import com.example.geoblinker.TimeUtils
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.BlackMediumButton
import com.example.geoblinker.ui.FullScreenBox
import com.example.geoblinker.ui.GreenMediumRightIconButton
import com.example.geoblinker.ui.OkButton
import com.example.geoblinker.ui.WhiteRedMediumButton
import com.example.geoblinker.ui.main.DeviceViewModel
import com.example.geoblinker.ui.main.LocationHelper
import com.example.geoblinker.ui.theme.sdp
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay

@Composable
fun DeviceOneScreen(
    viewModel: DeviceViewModel,
    toTwo: () -> Unit,
    toListSignal: () -> Unit,
    toMap: () -> Unit,
    toDetach: () -> Unit,
    toBack: () -> Unit
) {
    val device by viewModel.device.collectAsState()
    var isShow by remember { mutableStateOf(false) }
    var timer by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            LocationHelper(context) { location ->
                currentLocation = location
            }.getLastLocation()
        }
    }

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    LaunchedEffect(timer) {
        if (timer) {
            delay(500)
            isShow = false
            timer = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = RoundedCornerShape(10.sdp()),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(15.sdp())
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.size(9.sdp()).background(Color(0xFF12CD4A), MaterialTheme.shapes.small))
                    Spacer(Modifier.width(11.sdp()))
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { isShow = true }
                        ,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (device.name.isNotEmpty()) {
                            Text(
                                device.name,
                                modifier = Modifier.weight(1f, fill = false),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        } else {
                            Text(
                                stringResource(R.string.an_unnamed_device),
                                color = Color(0xFF737373),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Spacer(Modifier.width(13.sdp()))
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.pencil),
                            contentDescription = null,
                            modifier = Modifier.size(10.sdp()),
                            tint = Color.Unspecified
                        )
                    }
                    Spacer(Modifier.width(11.sdp()))
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.signal_strength),
                        contentDescription = null,
                        modifier = Modifier.size(24.sdp(), 17.sdp()),
                        tint = Color.Unspecified
                    )
                }
                HorizontalDivider(
                    Modifier.fillMaxWidth().padding(vertical = 15.sdp()),
                    1.sdp(),
                    Color(0xFFDAD9D9).copy(alpha = 0.5f)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Tracker ULTRA 3",
                        modifier = Modifier.weight(1f),
                        color = Color(0xFF737373),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.battery_full),
                        contentDescription = null,
                        modifier = Modifier.size(24.sdp()),
                        tint = Color.Unspecified
                    )
                }
                HorizontalDivider(
                    Modifier.fillMaxWidth().padding(vertical = 15.sdp()),
                    1.sdp(),
                    Color(0xFFDAD9D9).copy(alpha = 0.5f)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "IMEI: ",
                        color = Color(0xFF737373),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        device.imei,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        formatDistance(LatLng(device.lat, device.lng),currentLocation),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                HorizontalDivider(
                    Modifier.fillMaxWidth().padding(vertical = 15.sdp()),
                    1.sdp(),
                    Color(0xFFDAD9D9).copy(alpha = 0.5f)
                )
                Row {
                    Text(
                        "Привязано: ",
                        color = Color(0xFF737373),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        TimeUtils.formatToLocalTime(device.bindingTime),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
        Spacer(Modifier.height(15.sdp()))
        BlackMediumButton(
            icon = R.drawable.bell,
            text = stringResource(R.string.configure_the_signals),
            onClick = toTwo
        )
        Spacer(Modifier.height(15.sdp()))
        BlackMediumButton(
            icon = R.drawable.rectangle_list,
            text = stringResource(R.string.signal_log),
            onClick = toListSignal
        )
        Spacer(Modifier.height(15.sdp()))
        GreenMediumRightIconButton(
            icon = R.drawable.gps_navigation,
            text = stringResource(R.string.view_on_the_map),
            onClick = toMap
        )
        Spacer(Modifier.height(15.sdp()))
        WhiteRedMediumButton(
            text = stringResource(R.string.detach_the_device),
            onClick = toDetach
        )
        Spacer(Modifier.height(64.sdp()))
    }

    BackButton(
        onClick = toBack
    )

    if (isShow) {
        var value by remember { mutableStateOf(device.name) }
        var enabled by remember { mutableStateOf(true) }

        FullScreenBox()
        Dialog(
            { isShow = false }
        ) {
            Surface(
                modifier = Modifier.width(330.sdp()),
                shape = RoundedCornerShape(16.sdp()),
                color = Color.White
            ) {
                Box(
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(
                        modifier = Modifier.padding(
                            start = 15.sdp(),
                            top = 26.sdp(),
                            end = 15.sdp(),
                            bottom = 35.sdp()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            stringResource(R.string.devices_name),
                            color = Color(0xFF747474),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(Modifier.height(20.sdp()))
                        TextField(
                            value,
                            { value = it.filter { c -> c.isLetterOrDigit() || c == ' ' }.take(64) },
                            textStyle = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    viewModel.updateDevice(device.copy(name = value))
                                    enabled = false
                                    timer = true
                                }
                            ),
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedIndicatorColor = Color(0xFFDAD9D9),
                                focusedIndicatorColor = Color(0xFFDAD9D9)
                            )
                        )
                        Spacer(Modifier.height(126.sdp()))
                        OkButton(
                            enabled
                        ) {
                            viewModel.updateDevice(device.copy(name = value))
                            enabled = false
                            timer = true
                        }
                    }

                    if (!enabled) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(Modifier.height(154.sdp()))
                            Text(
                                stringResource(R.string.device_name_updated),
                                color = Color(0xFF12CD4A),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}

fun formatDistance(point1: LatLng, point2: LatLng?): String {
    if (point2 == null)
        return "Unknown"

    val distance = calculateDistance(point1, point2)
    return when {
        distance > 1000 -> "${"%.1f".format(distance/1000)} км"
        else -> "${"%.0f".format(distance)} м"
    }
}

fun calculateDistance(point1: LatLng, point2: LatLng): Double {
    val results = FloatArray(1)
    Location.distanceBetween(
        point1.latitude,
        point1.longitude,
        point2.latitude,
        point2.longitude,
        results
    )
    return results[0].toDouble()
}