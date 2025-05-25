package com.example.geoblinker.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import com.example.geoblinker.R
import com.example.geoblinker.data.Device
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.CustomEmptyDevicesPopup
import com.example.geoblinker.ui.FullScreenBox
import com.example.geoblinker.ui.GreenMediumButton
import com.example.geoblinker.ui.SearchDevice
import com.example.geoblinker.ui.theme.sc
import com.example.geoblinker.ui.theme.sdp
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay

@Composable
fun MapScreen(
    viewModel: DeviceViewModel,
    selectMarker: Device? = null,
    toBindingScreen: () -> Unit,
    toDeviceScreen: (Device) -> Unit
) {
    val devices by viewModel.devices.collectAsState()
    val context = LocalContext.current
    val webView = remember { WebView(context) }
    var isDarkTheme by remember { mutableStateOf(false) }
    var isSatellite by remember { mutableStateOf(false) }
    var currentZoom by remember { mutableIntStateOf(10) }
    var isShowPopup by remember { mutableStateOf(false) }
    var isShowPopupSearch by remember { mutableStateOf(false) }
    var selectedMarker by remember { mutableStateOf<Device?>(null) }
    var dontSearch by remember { mutableStateOf(false) }

    // Интерфейс для взаимодействия с JavaScript
    webView.addJavascriptInterface(object {
        @JavascriptInterface
        fun onZoomChanged(zoom: Int) {
            currentZoom = zoom
        }
    }, "Android")

    LaunchedEffect(Unit) {
        delay(2000)
        if (!viewModel.checkDevices()) {
            isShowPopup = true
        }
    }

    MapFromAssets(webView, viewModel, toDeviceScreen)

    LaunchedEffect(Unit) {
        selectMarker?.let {
            selectedMarker = it
        }
    }

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Column {
            Spacer(Modifier.height(66.sdp()))
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
            Spacer(Modifier.height(30.sdp()))
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
            Spacer(Modifier.height(15.sdp()))
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
            Spacer(Modifier.height(30.sdp()))
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
            Spacer(Modifier.height(15.sdp()))
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
        }
    }

    if (isShowPopupSearch) {
        var keySearch by remember { mutableStateOf("") }
        FullScreenBox()
        Dialog(
            onDismissRequest = {
                isShowPopupSearch = false
                dontSearch = false
            }
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
                        GreenMediumButton(
                            text = stringResource(R.string.find),
                            onClick = {
                                val findDevices =
                                    devices.filter { keySearch in it.imei || keySearch in it.name }
                                if (findDevices.isEmpty())
                                    dontSearch = true
                                else {
                                    selectedMarker = findDevices[0]
                                    isShowPopupSearch = false
                                }
                            }
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
        }
    }

    CustomDevicePopup(
        selectedMarker,
        webView,
        { selectedMarker = null },
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

    if (dontSearch) {
        BackButton { dontSearch = false }
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

    if (currentLocation != null) {
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
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            // Вызываем JS-функцию после загрузки страницы
                            devices.forEach { item ->
                                evaluateJavascript(
                                    "addSvgMarker('${item.imei}', ${item.lat}, ${item.lng}, 'marker.svg', ${26 * scaleIcons})",
                                    null
                                )
                            }
                            webView.evaluateJavascript(
                                "setCenter(${currentLocation!!.latitude}, ${currentLocation!!.longitude})",
                                null
                            )
                        }
                    }
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
    }
    else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                stringResource(R.string.add_location_permissions),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }

    CustomDevicePopup(
        selectedMarker,
        webView,
        { selectedMarker = null },
        toDeviceScreen
    )
}

@Composable
fun CustomDevicePopup(
    selectedMarker: Device?,
    webView: WebView,
    onChangeValueToNull: () -> Unit,
    toDeviceScreen: (Device) -> Unit
) {
    selectedMarker?.let { item ->
        webView.evaluateJavascript(
            "setCenter(${item.lat}, ${item.lng})",
            null
        )

        Popup(
            Alignment.Center,
            offset = IntOffset(x = 0, y = (-290).sdp().value.toInt()),
            onDismissRequest = onChangeValueToNull
        ) {
            Surface(
                modifier = Modifier.width(332.sdp()).clickable {
                    onChangeValueToNull()
                    toDeviceScreen(item)
                },
                shape = ComicBubbleShape(
                    cornerRadius = 10.sdp(),
                    pointerHeight = 24.sdp(),
                    pointerWidth = 20.sdp()),
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
                        modifier = Modifier.fillMaxWidth().padding(15.sdp()).padding(bottom = 24.sdp()),
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