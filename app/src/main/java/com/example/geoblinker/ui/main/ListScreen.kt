package com.example.geoblinker.ui.main

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.geoblinker.R
import com.example.geoblinker.TimeUtils
import com.example.geoblinker.data.Device
import com.example.geoblinker.ui.CustomCommentsPopup
import com.example.geoblinker.ui.CustomDiagnosisPopup
import com.example.geoblinker.ui.CustomListPopup
import com.example.geoblinker.ui.GreenMediumButton
import com.example.geoblinker.ui.SearchDevice
import com.example.geoblinker.ui.main.device.calculateDistance
import com.example.geoblinker.ui.main.device.formatSpeed
import com.example.geoblinker.ui.main.viewmodel.DeviceViewModel
import com.example.geoblinker.ui.theme.sdp
import com.google.android.gms.maps.model.LatLng

@Composable
fun ListScreen(
    viewModel: DeviceViewModel,
    toBindingScreen: () -> Unit,
    toDeviceScreen: () -> Unit
) {
    var keySearch by remember { mutableStateOf("") }
    var keySort by remember { mutableIntStateOf(R.string.by_name) }
    var isShow by remember { mutableStateOf(false) }
    var isShowDiagnosis by remember { mutableStateOf(false) }
    var isShowComments by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    val device by viewModel.device.collectAsState()
    val unitsDistance by viewModel.unitsDistance.collectAsState()

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
    val stateDevices = viewModel.devices.collectAsState()
    var sortedDevices = when(keySort) {
        R.string.by_name -> stateDevices.value.sortedBy { it.name }
        R.string.by_binding_date -> stateDevices.value.sortedByDescending { it.bindingTime }
        R.string.by_distance -> if (currentLocation != null) stateDevices.value.sortedBy { calculateDistance(LatLng(it.lat, it.lng), currentLocation!!) } else stateDevices.value.sortedBy { it.name }
        else -> stateDevices.value.sortedBy { it.name }
    }.filter { it.isConnected }
    var sortedNullDevices = stateDevices.value.filter { !it.isConnected }

    if (sortedDevices.isEmpty() && sortedNullDevices.isEmpty()) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                stringResource(R.string.no_active_devices_found),
                modifier = Modifier.offset(y = (-45).sdp()),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
    else {
        sortedDevices = sortedDevices.filter { keySearch.isEmpty() || keySearch in it.name || keySearch in it.imei }
        sortedNullDevices = sortedNullDevices.filter { keySearch.isEmpty() || keySearch in it.imei }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchDevice(keySearch) { keySearch = it }
            Spacer(Modifier.height(25.sdp()))
            if (sortedDevices.isEmpty() && sortedNullDevices.isEmpty()) {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        stringResource(R.string.no_devices_found_for_request),
                        modifier = Modifier.offset(y = (-45).sdp()),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Row(
                        modifier = Modifier
                            .clickable { isShow = true },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            stringResource(keySort),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(Modifier.width(16.sdp()))
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.open_list),
                            contentDescription = null,
                            modifier = Modifier.size(11.sdp()),
                            tint = Color.Unspecified
                        )
                    }
                }
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(sortedDevices) { item ->
                        var isShowAdd by remember { mutableStateOf(false) }

                        Column(
                            modifier = Modifier.width(330.sdp())
                        ) {
                            Spacer(Modifier.height(15.sdp()))
                            Surface(
                                modifier = Modifier.fillMaxWidth().animateContentSize(),
                                shape = RoundedCornerShape(10.sdp()),
                                color = Color(0xFFDAD9D9).copy(alpha = 0.5f)
                            ) {
                                Column {
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                if (isShowAdd) {
                                                    viewModel.setDevice(item)
                                                    toDeviceScreen()
                                                }
                                                isShowAdd = true
                                            },
                                        shape = RoundedCornerShape(10.sdp()),
                                        color = Color.White,
                                        shadowElevation = 4.sdp()
                                    ) {
                                        Column {
                                            Row(
                                                modifier = Modifier.fillMaxWidth()
                                                    .padding(15.sdp()),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Box(
                                                    Modifier.size(9.sdp()).background(
                                                        Color(0xFF12CD4A),
                                                        MaterialTheme.shapes.small
                                                    )
                                                )
                                                Spacer(Modifier.width(11.sdp()))
                                                if (item.name.isNotEmpty()) {
                                                    Text(
                                                        item.name,
                                                        modifier = Modifier.weight(1f),
                                                        overflow = TextOverflow.Ellipsis,
                                                        style = MaterialTheme.typography.labelMedium.copy(
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    )
                                                } else {
                                                    Text(
                                                        stringResource(R.string.an_unnamed_device),
                                                        modifier = Modifier.weight(1f),
                                                        color = Color(0xFF737373),
                                                        style = MaterialTheme.typography.labelMedium.copy(
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    )
                                                }
                                                Icon(
                                                    imageVector = ImageVector.vectorResource(R.drawable.signal_strength),
                                                    contentDescription = null,
                                                    modifier = Modifier.size(24.sdp(), 17.sdp()),
                                                    tint = Color.Unspecified
                                                )
                                            }
                                            HorizontalDivider(
                                                Modifier.fillMaxWidth()
                                                    .padding(horizontal = 15.sdp()),
                                                1.sdp(),
                                                Color(0xFFDAD9D9).copy(alpha = 0.5f)
                                            )
                                            Row(
                                                modifier = Modifier.fillMaxWidth()
                                                    .padding(15.sdp()),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    "Tracker ULTRA 3",
                                                    modifier = Modifier.weight(1f),
                                                    style = MaterialTheme.typography.labelMedium
                                                )
                                                Icon(
                                                    imageVector = ImageVector.vectorResource(R.drawable.battery_full),
                                                    contentDescription = null,
                                                    modifier = Modifier.size(24.sdp()),
                                                    tint = Color.Unspecified
                                                )
                                            }

                                        }
                                    }
                                    if (isShowAdd) {
                                        Column(
                                            modifier = Modifier.padding(15.sdp())
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        viewModel.setDevice(item)
                                                        isShowDiagnosis = true
                                                    },
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Icon(
                                                    imageVector = when(item.typeStatus) {
                                                        Device.TypeStatus.Available -> Icons.Filled.LocationOn
                                                        Device.TypeStatus.Ready -> Icons.Filled.Home
                                                        Device.TypeStatus.RequiresRepair -> Icons.Filled.Build
                                                    },
                                                    contentDescription = null,
                                                    modifier = Modifier.size(24.sdp()),
                                                    tint = Color(0xFF12CD4A)
                                                )
                                                Text(
                                                    TimeUtils.formatToLocalTime(item.bindingTime),
                                                    style = MaterialTheme.typography.labelMedium
                                                )
                                            }
                                            HorizontalDivider(
                                                Modifier.fillMaxWidth().padding(vertical = 15.sdp()),
                                                1.sdp(),
                                                Color(0xFFDAD9D9).copy(alpha = 0.5f)
                                            )
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceAround
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Filled.Speed,
                                                        contentDescription = null,
                                                        modifier = Modifier.size(24.sdp()),
                                                        tint = Color(0xFF12CD4A)
                                                    )
                                                    Spacer(Modifier.width(12.sdp()))
                                                    Text(
                                                        formatSpeed(10.0, unitsDistance),
                                                        style = MaterialTheme.typography.labelMedium
                                                    )
                                                }
                                                Row(
                                                    modifier = Modifier.clickable { isShowComments = true },
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    for (i in 1..5) {
                                                        Icon(
                                                            imageVector = Icons.Filled.StarRate,
                                                            contentDescription = null,
                                                            modifier = Modifier.size(24.sdp()),
                                                            tint = Color.Yellow
                                                        )
                                                        Spacer(Modifier.width(4.sdp()))
                                                    }
                                                    Spacer(Modifier.width(8.sdp()))
                                                    Text(
                                                        "8",
                                                        style = MaterialTheme.typography.labelMedium
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    items(sortedNullDevices) { item ->
                        Column(
                            modifier = Modifier.width(330.sdp())
                        ) {
                            Spacer(Modifier.height(15.sdp()))
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.sdp()),
                                color = Color.White
                            ) {
                                Row(
                                    modifier = Modifier.padding(15.sdp()),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        Modifier.size(9.sdp()).background(
                                            Color(0xFFC4162D),
                                            MaterialTheme.shapes.small
                                        )
                                    )
                                    Spacer(Modifier.width(11.sdp()))
                                    Text(
                                        "IMEI: ",
                                        color = Color(0xFF737373),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        item.imei,
                                        color = Color(0xFF737373),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                    item {
                        Spacer(Modifier.height(115.sdp()))
                    }
                }
            }
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .offset(y = (-32).sdp()),
        contentAlignment = Alignment.BottomCenter
    ) {
        GreenMediumButton(
            modifier = Modifier.width(260.sdp()),
            icon = R.drawable.plus,
            text = stringResource(R.string.link_device),
            onClick = toBindingScreen
        )
    }

    if (isShow) {
        CustomListPopup(
            R.string.sort_devices,
            listOf(R.string.by_name,
                R.string.by_device_type,
                R.string.by_distance,
                R.string.by_binding_date,
                R.string.by_signal_strength,
                R.string.by_charge_level),
            {
                keySort = it
                isShow = false
            },
            {
                isShow = it
            }
        )
    }

    if (isShowDiagnosis) {
        CustomDiagnosisPopup(
            device,
            {
                if (it in arrayOf(Device.TypeStatus.Available, Device.TypeStatus.Ready))
                    viewModel.updateDevice(device.copy(
                        typeStatus = it,
                        breakdownForecast = null,
                        maintenanceRecommendations = null
                    ))
                else
                    viewModel.updateDevice(device.copy(typeStatus = it))
            },
            { isShowDiagnosis = false },
            listOf(
                device.bindingTime,
                device.bindingTime,
                device.bindingTime,
                device.bindingTime,
                device.bindingTime,
                device.bindingTime,
                device.bindingTime,
                device.bindingTime,
                )
        )
    }

    if (isShowComments) {
        CustomCommentsPopup(
            { isShowComments = false }
        )
    }
}