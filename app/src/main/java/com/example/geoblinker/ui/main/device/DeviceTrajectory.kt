package com.example.geoblinker.ui.main.device

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.geoblinker.R
import com.example.geoblinker.TimeUtils
import com.example.geoblinker.model.imei.TrajectoryImeiItem
import com.example.geoblinker.ui.HSpacer
import com.example.geoblinker.ui.main.ComicBubbleShape
import com.example.geoblinker.ui.main.WebAppInterface
import com.example.geoblinker.ui.main.viewmodel.DeviceViewModel
import com.example.geoblinker.ui.theme.sc
import com.example.geoblinker.ui.theme.sdp

@Composable
fun DeviceTrajectory(
    viewModel: DeviceViewModel
) {
    val context = LocalContext.current
    val webView = remember { WebView(context) }
    var isDarkTheme by rememberSaveable { mutableStateOf(false) }
    var isSatellite by rememberSaveable { mutableStateOf(false) }
    var selectedMarker by rememberSaveable { mutableStateOf(false) }
    var valueSlider by rememberSaveable { mutableFloatStateOf(0f) }
    val trajectory by viewModel.trajectory
    val unitsDistance by viewModel.unitsDistance

    fun getLat(index: Float): Double = (trajectory[index.toInt()].lat / 1000000)

    fun getLng(index: Float): Double = (trajectory[index.toInt()].lng / 1000000)

    fun addLine() {
        webView.evaluateJavascript(
            "addTrackLine('#16d309', ${getLat(valueSlider)}, ${getLng(valueSlider)}, ${getLat(valueSlider + 1)}, ${getLng(valueSlider + 1)})",
            null
        )
        valueSlider++
        webView.evaluateJavascript(
            "updateMarkerPosition('device', ${getLat(valueSlider)}, ${getLng(valueSlider)})",
            null
        )
    }

    fun undoLine() {
        webView.evaluateJavascript(
            "undoLastTrackLine()",
            null
        )
        valueSlider--
        webView.evaluateJavascript(
            "updateMarkerPosition('device', ${getLat(valueSlider)}, ${getLng(valueSlider)})",
            null
        )
    }

    LaunchedEffect(trajectory) {
        while(valueSlider < trajectory.size - 1) {
            addLine()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearTrajectoryDevice()
        }
    }

    MapFromAssets(webView, viewModel, { selectedMarker = true })

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Slider(
            valueSlider,
            {
                while (valueSlider < it) {
                    addLine()
                }
                while (valueSlider > it) {
                    undoLine()
                }
            },
            modifier = Modifier.size(300.sdp(), 40.sdp()),
            enabled = trajectory.size > 1,
            valueRange = 0f..maxOf(1f, trajectory.size.toFloat() - 1f),
            steps = maxOf(0, trajectory.size - 2),
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color(0xFFe8e8e8),
                activeTickColor = Color(0xffc9cecf),
                inactiveTrackColor = Color(0xFFd1d5d6),
                inactiveTickColor = Color(0xff979a9b)
            )
        )
        HSpacer(16)
        Row {
            IconButton(
                { undoLine() },
                modifier = Modifier.size(48.sdp()),
                enabled = trajectory.isNotEmpty() && valueSlider > 0,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    disabledContainerColor = Color(0xffcbd4d7).copy(alpha = 0.5f),
                    disabledContentColor = Color.Black.copy(alpha = 0.5f)
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = null,
                    modifier = Modifier.size(24.sdp())
                )
            }
            Spacer(Modifier.width(120.sdp()))
            IconButton(
                { addLine() },
                modifier = Modifier.size(48.sdp()),
                enabled = trajectory.isNotEmpty() && valueSlider < trajectory.size - 1,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    disabledContainerColor = Color(0xffcbd4d7).copy(alpha = 0.5f),
                    disabledContentColor = Color.Black.copy(alpha = 0.5f)
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    modifier = Modifier.size(24.sdp())
                )
            }
        }
        HSpacer(60)
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
        }
    }

    if (trajectory.isNotEmpty() && selectedMarker) {
        CustomTrajectoryItemPopup(
            trajectory[valueSlider.toInt()],
            webView,
            unitsDistance,
            { selectedMarker = false }
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun MapFromAssets(
    webView: WebView,
    viewModel: DeviceViewModel,
    select: () -> Unit
) {
    val addWidth = 30.sdp()
    val addHeight = 45.sdp()
    val scaleIcons = sc()

    val device by viewModel.device.collectAsState()

    AndroidView(
        factory = { _ ->
            webView.apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                settings.javaScriptEnabled = true
                addJavascriptInterface(
                    WebAppInterface { _ ->
                        select()
                    },
                    "AndroidInterface"
                )
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        webView.evaluateJavascript(
                            "addSvgMarker('device', ${device.lat}, ${device.lng}, 'marker.svg', ${26 * scaleIcons}, ${26 * scaleIcons})",
                            null
                        )
                        webView.evaluateJavascript(
                            "setCenter(${device.lat}, ${device.lng})",
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

@Composable
private fun CustomTrajectoryItemPopup(
    data: TrajectoryImeiItem,
    webView: WebView,
    unitsDistance: Boolean,
    notSelect: () -> Unit
) {
    webView.evaluateJavascript(
        "setCenter(${data.lat / 1000000}, ${data.lng / 1000000})",
        null
    )

    Dialog(
        notSelect,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { notSelect() },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.padding(bottom = 196.sdp())
            ) {
                Surface(
                    modifier = Modifier
                        .width(220.sdp())
                        .animateContentSize(),
                    shape = ComicBubbleShape(
                        cornerRadius = 10.sdp(),
                        pointerHeight = 24.sdp(),
                        pointerWidth = 20.sdp()
                    ),
                    color = Color(0xFFDAD9D9)
                ) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
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
                                Text(
                                    TimeUtils.formatToLocalTime(data.time * 1000),
                                    style = MaterialTheme.typography.labelMedium
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
                                    formatSpeed(data.speed, unitsDistance),
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
                }
            }
        }
    }
}
