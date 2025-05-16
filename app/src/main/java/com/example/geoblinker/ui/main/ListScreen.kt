package com.example.geoblinker.ui.main

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Popup
import com.example.geoblinker.R
import com.example.geoblinker.data.Device
import com.example.geoblinker.ui.FullScreenBox
import com.example.geoblinker.ui.GreenMediumButton
import com.example.geoblinker.ui.SearchDevice
import com.example.geoblinker.ui.device.DeviceViewModel
import com.example.geoblinker.ui.theme.sdp

@Composable
fun ListScreen(
    viewModel: DeviceViewModel,
    toBindingScreen: () -> Unit,
    toDeviceScreen: (Device) -> Unit
) {
    var keySearch by remember { mutableStateOf("") }
    var keySort by remember { mutableIntStateOf(R.string.by_name) }
    var isShow by remember { mutableStateOf(false) }
    val stateDevices = viewModel.devices.collectAsState()
    var sortedDevices = when(keySort) {
        R.string.by_name -> stateDevices.value.sortedBy { it.name }
        R.string.by_binding_date -> stateDevices.value.sortedBy { it.bindingTime }
        else -> stateDevices.value.sortedBy { it.name }
    }.filter { keySearch.isEmpty() || keySearch in it.name || keySearch in it.imei }

    if (!viewModel.checkDevices()) {
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
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchDevice(keySearch) { keySearch = it }
            Spacer(Modifier.height(25.sdp()))
            if (sortedDevices.isEmpty()) {
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
                LazyColumn {
                    items(sortedDevices) { item ->
                        Spacer(Modifier.height(15.sdp()))
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    toDeviceScreen(item)
                                }
                            ,
                            shape = RoundedCornerShape(10.sdp()),
                            color = Color.White
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(15.sdp()),
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
                                    Modifier.fillMaxWidth().padding(horizontal = 15.sdp()),
                                    1.sdp(),
                                    Color(0xFFDAD9D9).copy(alpha = 0.5f)
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(15.sdp()),
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
        FullScreenBox()
        Popup(
            onDismissRequest = { isShow = false }
        ) {
            Surface(
                modifier = Modifier.width(246.sdp()),
                shape = MaterialTheme.shapes.large,
                color = Color.White,
                shadowElevation = 2.sdp()
            ) {
                Column(
                    modifier = Modifier.padding(
                        start = 20.sdp(),
                        top = 26.sdp(),
                        bottom = 37.sdp()
                    )
                ) {
                    Text(
                        stringResource(R.string.sort_devices),
                        color = Color(0xFF747474),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(Modifier.height(20.sdp()))
                    Text(
                        stringResource(R.string.by_name),
                        modifier = Modifier.clickable {
                            keySort = R.string.by_name
                            isShow = false
                        },
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(Modifier.height(15.sdp()))
                    Text(
                        stringResource(R.string.by_device_type),
                        modifier = Modifier.clickable {
                            keySort = R.string.by_device_type
                            isShow = false
                        },
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(Modifier.height(15.sdp()))
                    Text(
                        stringResource(R.string.by_distance),
                        modifier = Modifier.clickable {
                            keySort = R.string.by_distance
                            isShow = false
                        },
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(Modifier.height(15.sdp()))
                    Text(
                        stringResource(R.string.by_binding_date),
                        modifier = Modifier.clickable {
                            keySort = R.string.by_binding_date
                            isShow = false
                        },
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(Modifier.height(15.sdp()))
                    Text(
                        stringResource(R.string.by_signal_strength),
                        modifier = Modifier.clickable {
                            keySort = R.string.by_signal_strength
                            isShow = false
                        },
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(Modifier.height(15.sdp()))
                    Text(
                        stringResource(R.string.by_charge_level),
                        modifier = Modifier.clickable {
                            keySort = R.string.by_charge_level
                            isShow = false
                        },
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }
    }
}