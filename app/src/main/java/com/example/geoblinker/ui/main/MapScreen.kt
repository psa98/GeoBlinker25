package com.example.geoblinker.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.example.geoblinker.R
import com.example.geoblinker.model.Device
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.CustomButton
import com.example.geoblinker.ui.CustomCommentsPopup
import com.example.geoblinker.ui.CustomDiagnosisPopup
import com.example.geoblinker.ui.CustomEmptyDevicesPopup
import com.example.geoblinker.ui.HSpacer
import com.example.geoblinker.ui.SearchDevice
import com.example.geoblinker.ui.TypeColor
import com.example.geoblinker.ui.main.device.formatSpeed
import com.example.geoblinker.ui.main.viewmodel.DeviceViewModel
import com.example.geoblinker.ui.theme.sc
import com.example.geoblinker.ui.theme.sdp
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay

@Composable
fun MapScreen(
    viewModel: DeviceViewModel,
    toBindingScreen: () -> Unit,
    toDeviceScreen: (Device) -> Unit
) {
    val devices by viewModel.devices.collectAsState()
    val selectedMarker by viewModel.selectedMarker
    val context = LocalContext.current
    val webView = remember { WebView(context) }
    var isDarkTheme by remember { mutableStateOf(false) }
    var isSatellite by remember { mutableStateOf(false) }
    var isShowPopup by remember { mutableStateOf(false) }
    var isShowPopupSearch by remember { mutableStateOf(false) }
    var dontSearch by remember { mutableStateOf(false) }
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }

    val scaleIcons = sc()
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
        delay(2000)
        if (!viewModel.checkDevices()) {
            isShowPopup = true
        }
    }

    MapFromAssets(webView, viewModel, toDeviceScreen)

    LaunchedEffect(Unit) {
        while (true) {
            devices.forEach { item ->
                if (item.isConnected && item.lat != -999999999.9 && item.lng != -999999999.9)
                    webView.evaluateJavascript(
                        "addSvgMarker('${item.imei}', ${item.lat}, ${item.lng}, 'marker.svg', ${26 * scaleIcons})",
                        null
                    )
                else
                    webView.evaluateJavascript(
                        "removeMarker('${item.imei}')",
                        null
                    )
            }
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            if (hasPermission)
                LocationHelper(context) { location ->
                    currentLocation = location
                }.getLastLocation()
            currentLocation?.let {
                webView.evaluateJavascript(
                    "addSvgMarker('myLocation', ${it.latitude}, ${it.longitude}, 'my_marker.svg', ${26 * scaleIcons})",
                    null
                )
            }
            delay(5000)
        }
    }

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Column {
            HSpacer(66)
            Box(
                Modifier
                    .size(40.sdp())
                    .background(
                        brush = Brush.verticalGradient(listOf(Color(0xFF373736), Color(0xFF212120))),
                        shape = MaterialTheme.shapes.small
                    )
                    .clickable { isShowPopupSearch = true }
                ,
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.magnifier),
                    contentDescription = null,
                    modifier = Modifier.size(24.sdp()),
                    tint = Color.Unspecified
                )
            }
            HSpacer(30)
            Box(
                Modifier
                    .size(40.sdp())
                    .background(
                        color = Color.White,
                        shape = MaterialTheme.shapes.small
                    )
                    .clickable { webView.evaluateJavascript("zoomIn()", null) }
                ,
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.plus_thin),
                    contentDescription = null,
                    modifier = Modifier.size(14.sdp()),
                    tint = Color.Unspecified
                )
            }
            HSpacer(15)
            Box(
                Modifier
                    .size(40.sdp())
                    .background(
                        color = Color.White,
                        shape = MaterialTheme.shapes.small
                    )
                    .clickable { webView.evaluateJavascript("zoomOut()", null) }
                ,
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.minus_thin),
                    contentDescription = null,
                    modifier = Modifier.size(14.sdp()),
                    tint = Color.Unspecified
                )
            }
            HSpacer(30)
            Icon(
                painter = painterResource(R.drawable.theme),
                contentDescription = null,
                modifier = Modifier
                    .size(40.sdp())
                    .clickable {
                        isDarkTheme = !isDarkTheme
                        val theme = if(isSatellite) "satellite" else if(isDarkTheme) "dark" else "light"
                        webView.evaluateJavascript("switchTheme('$theme')", null)
                    }
                ,
                tint = Color.Unspecified
            )
            HSpacer(15)
            Icon(
                painter = painterResource(R.drawable.sputnik),
                contentDescription = null,
                modifier = Modifier
                    .size(40.sdp())
                    .clickable {
                        isSatellite = !isSatellite
                        val theme = if(isSatellite) "satellite" else if(isDarkTheme) "dark" else "light"
                        webView.evaluateJavascript("switchTheme('$theme')", null)
                    }
                ,
                tint = Color.Unspecified
            )
            HSpacer(30)
            Box(
                Modifier
                    .size(40.sdp())
                    .background(
                        brush = Brush.verticalGradient(listOf(Color(0xFF373736), Color(0xFF212120))),
                        shape = CircleShape
                    )
                    .clickable {
                        val hasPermission = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                        if (!hasPermission)
                            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        else
                            LocationHelper(context) { location ->
                                currentLocation = location
                            }.getLastLocation()

                        currentLocation?.let {
                            webView.evaluateJavascript(
                                "setCenter(${it.latitude}, ${it.longitude})",
                                null
                            )
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.MyLocation,
                    contentDescription = null,
                    modifier = Modifier.size(24.sdp()),
                    tint = Color.White
                )
            }
        }
    }

    if (isShowPopupSearch) {
        var keySearch by remember { mutableStateOf("") }
        Dialog(
            onDismissRequest = {
                isShowPopupSearch = false
                dontSearch = false
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.2f))
                    .clickable {
                        isShowPopupSearch = false
                        dontSearch = false
                    },
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.size(350.sdp(), 246.sdp()),
                    shape = RoundedCornerShape(10.sdp()),
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 10.sdp()).padding(top = 28.sdp()),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            stringResource(R.string.map_search),
                            style = MaterialTheme.typography.titleLarge
                        )
                        if (!dontSearch) {
                            Spacer(Modifier.height(23.sdp()))
                            SearchDevice(keySearch) { keySearch = it }
                            Spacer(Modifier.height(15.sdp()))
                            CustomButton(
                                text = stringResource(R.string.find),
                                onClick = {
                                    val findDevices =
                                        devices.filter { (keySearch in it.imei || keySearch in it.name) && it.isConnected }
                                    if (findDevices.isEmpty())
                                        dontSearch = true
                                    else {
                                        isShowPopupSearch = false
                                        viewModel.setSelectedMarker(findDevices[0])
                                    }
                                },
                                typeColor = TypeColor.Green,
                                height = 55
                            )
                        } else {
                            Box(
                                Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    stringResource(R.string.no_devices_found_for_request),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                        }
                    }
                }

                if (dontSearch) {
                    BackButton(
                        onClick = { dontSearch = false }
                    )
                }
            }
        }
    }

    CustomDevicePopup(
        viewModel,
        selectedMarker,
        webView,
        { viewModel.setSelectedMarker() },
        toDeviceScreen
    )

    if (isShowPopup) {
        CustomEmptyDevicesPopup(
            {
                isShowPopup = false
            },
            {
                isShowPopup = false
                toBindingScreen()
            }
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MapFromAssets(
    webView: WebView,
    viewModel: DeviceViewModel,
    toDeviceScreen: (Device) -> Unit
) {
    val addWidth = 30.sdp()
    val addHeight = 45.sdp()
    val scaleIcons = sc()

    val devices by viewModel.devices.collectAsState()
    var selectedMarker by remember { mutableStateOf<Device?>(null) }

    AndroidView(
        factory = { _ ->
            webView.apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                settings.javaScriptEnabled = true
                addJavascriptInterface(
                    WebAppInterface { markerId ->
                        selectedMarker = devices.filter { it.imei == markerId }[0]
                    },
                    "AndroidInterface"
                )
                webViewClient = WebViewClient()
                loadUrl("file:///android_asset/map.html")
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .layout { measurable, constraints ->
                // Игнорируем padding родителя
                val looseConstraints = constraints.copy(
                    maxWidth = constraints.maxWidth + addWidth.roundToPx(),
                    maxHeight = constraints.maxHeight + addHeight.roundToPx() * 2
                )
                val placeable = measurable.measure(looseConstraints)
                layout(placeable.width, placeable.height) {
                    placeable.place(0, -addHeight.roundToPx()) // Смещение
                }
            }
    )

    CustomDevicePopup(
        viewModel,
        selectedMarker,
        webView,
        { selectedMarker = null },
        toDeviceScreen
    )
}

@Composable
fun CustomDevicePopup(
    viewModel: DeviceViewModel,
    selectedMarker: Device?,
    webView: WebView,
    onChangeValueToNull: () -> Unit,
    toDeviceScreen: (Device) -> Unit
) {
    val unitsDistance = viewModel.unitsDistance

    selectedMarker?.let { item ->
        //var isShowAdd by remember { mutableStateOf(false) }
        var isShowDiagnosis by remember { mutableStateOf(false) }
        var isShowComments by remember { mutableStateOf(false) }
        //val offsetYAnimated by animateIntAsState(
        //    targetValue = if (isShowAdd) (-414).sdp().value.toInt() else (-290).sdp().value.toInt(),
        //    label = ""
        //)

        webView.evaluateJavascript(
            "setCenter(${item.lat}, ${item.lng})",
            null
        )

        Dialog(
            onChangeValueToNull,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onChangeValueToNull() },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier.padding(bottom = 196.sdp())
                ) {
                    Surface(
                        modifier = Modifier
                            .width(332.sdp())
                            .animateContentSize(),
                        shape = ComicBubbleShape(
                            cornerRadius = 10.sdp(),
                            pointerHeight = 24.sdp(),
                            pointerWidth = 20.sdp()
                        ),
                        color = Color(0xFFDAD9D9)
                    ) {
                        Column {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        //if (isShowAdd) {
                                        onChangeValueToNull()
                                        toDeviceScreen(item)
                                        //}
                                        //isShowAdd = true
                                    },
                                shape = RoundedCornerShape(10.sdp()),
                                color = Color.White,
                                shadowElevation = 4.sdp()
                            ) {
                                Column(
                                    modifier = Modifier.padding(bottom = 24.sdp())
                                ) {
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
                                                maxLines = 1,
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
                                            imageVector = ImageVector.vectorResource(
                                                when {
                                                    item.signalRate <= 20 -> R.drawable.signal_low
                                                    item.signalRate <= 60 -> R.drawable.signal_half
                                                    else -> R.drawable.signal_strength
                                                }
                                            ),
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
                                            item.modelName,
                                            modifier = Modifier.weight(1f),
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                        Icon(
                                            imageVector = ImageVector.vectorResource(
                                                when {
                                                    item.powerRate <= 25 -> R.drawable.battery_quarter
                                                    item.powerRate <= 50 -> R.drawable.battery_half
                                                    else -> R.drawable.battery_full
                                                }
                                            ),
                                            contentDescription = null,
                                            modifier = Modifier.size(24.sdp()),
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
                                            formatSpeed(item.speed, unitsDistance),
                                            modifier = Modifier.weight(1f),
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                        Icon(
                                            imageVector = Icons.Filled.Speed,
                                            contentDescription = null,
                                            modifier = Modifier.size(24.sdp()),
                                            tint = Color(0xFF12CD4A)
                                        )
                                    }
                                }
                            }
                            /*
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
                                            tint = ColorStar
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
                     */
                        }
                    }
                }
            }
        }

        if (isShowDiagnosis) {
            CustomDiagnosisPopup(
                item,
                {
                    if (it in arrayOf(Device.TypeStatus.Available, Device.TypeStatus.Ready))
                        viewModel.updateDevice(item.copy(
                            typeStatus = it,
                            breakdownForecast = null,
                            maintenanceRecommendations = null
                        ))
                    else
                        viewModel.updateDevice(item.copy(typeStatus = it))
                },
                { isShowDiagnosis = false },
                listOf(
                    item.bindingTime,
                    item.bindingTime,
                    item.bindingTime,
                    item.bindingTime,
                    item.bindingTime,
                    item.bindingTime,
                    item.bindingTime,
                    item.bindingTime,
                )
            )
        }

        if (isShowComments) {
            CustomCommentsPopup(
                { isShowComments = false }
            )
        }
    }
}