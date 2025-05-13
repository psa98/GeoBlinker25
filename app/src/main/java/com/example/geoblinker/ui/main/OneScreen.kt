package com.example.geoblinker.ui.main

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.viewinterop.AndroidView
import com.example.geoblinker.R
import com.example.geoblinker.ui.CustomEmptyDevicesPopup
import com.example.geoblinker.ui.GreenMediumButton
import com.example.geoblinker.ui.GreenSmallButton
import com.example.geoblinker.ui.WhiteSmallButton
import com.example.geoblinker.ui.theme.GeoBlinkerTheme
import com.example.geoblinker.ui.theme.sc
import com.example.geoblinker.ui.theme.sdp
import kotlinx.coroutines.delay

@Composable
fun OneScreen(
    checkDevices: () -> Boolean,
    toBindingScreen: () -> Unit
) {
    val context = LocalContext.current
    val webView = remember { WebView(context) }
    var isDarkTheme by remember { mutableStateOf(false) }
    var isSatellite by remember { mutableStateOf(false) }
    var currentZoom by remember { mutableIntStateOf(10) }
    var isShowPopup by remember { mutableStateOf(false) }

    // Интерфейс для взаимодействия с JavaScript
    webView.addJavascriptInterface(object {
        @JavascriptInterface
        fun onZoomChanged(zoom: Int) {
            currentZoom = zoom
        }
    }, "Android")

    LaunchedEffect(Unit) {
        if (!checkDevices()) {
            delay(2000)
            isShowPopup = true
        }
    }

    MapFromAssets(webView)

    Box(
        Modifier
            .fillMaxSize()
            .offset(x = (-15).sdp())
        ,
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
                    .clickable {  }
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

    if (isShowPopup) {
        CustomEmptyDevicesPopup(
            {
                isShowPopup = false
            },
            toBindingScreen
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MapFromAssets(webView: WebView, lat: Double = 55.7558, lng: Double = 37.6176, zoom: Int = 10) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.sdp()
    val screenHeight = configuration.screenHeightDp.sdp()
    val scaleIcons = sc()

    val markers = remember {
        listOf(
            Triple(55.823262 to 37.645905, "marker_tractor.svg", 38),
        )
    }

    AndroidView(
        factory = { _ ->
            webView.apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                settings.javaScriptEnabled = true
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        // Вызываем JS-функцию после загрузки страницы
                        markers.forEach { (coord, svg, size) ->
                            evaluateJavascript(
                                "addSvgMarker(${coord.first}, ${coord.second}, '$svg', ${size * scaleIcons})",
                                null
                            )
                        }
                    }
                }
                loadUrl("file:///android_asset/map.html")
            }
        },
        modifier = Modifier.layout { measurable, _ ->
            // Игнорируем родительские ограничения
            val placeable = measurable.measure(
                Constraints(
                    minWidth = 0,
                    maxWidth = Constraints.Infinity,
                    minHeight = 0,
                    maxHeight = Constraints.Infinity
                )
            )
            layout(placeable.width, placeable.height) {
                placeable.place(0, 0)
            }
        }
            .size(screenWidth, screenHeight) // Явно задаем размер экрана
    )
}

@Composable
fun Notifications(
    notificationsCount: Int = 0
) {
    Box {
        Box(
            Modifier.size(50.sdp()),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.notifications),
                contentDescription = null,
                modifier = Modifier.size(24.sdp())
            )
        }

        if (notificationsCount > 0) {
            Box(
                Modifier.size(50.sdp()).offset(12.sdp(), (-12).sdp()),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    Modifier
                        .background(
                        color = Color(0xFFF1137E),
                        shape = CircleShape
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        notificationsCount.toString(),
                        modifier = Modifier.padding(horizontal = 6.sdp(), vertical = 1.sdp()),
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNotifications() {
    GeoBlinkerTheme {
        Notifications(33)
    }
}