package com.example.geoblinker.ui.theme

import android.util.Log
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun sc(
    baseWidth: Int = 360,
    baseHeight: Int = 720
): Float {
    val current = LocalConfiguration.current
    val width = current.screenWidthDp
    val height = current.screenHeightDp
    Log.i("hieghtDp", height.toString())
    val scale = minOf(width / baseWidth.toFloat(), height / baseHeight.toFloat())
    return scale
}

@Composable
fun hsc(
    baseHeight: Int = 720
): Float {
    val height = LocalConfiguration.current.screenHeightDp
    val scale = height / baseHeight.toFloat()
    return scale
}

@Composable
fun Int.sdp(): Dp {
    return (this * sc()).dp
}
/*
@Composable
fun Int.hdp(): Dp {
    return (this * hsc()).dp
}
*/
@Composable
fun Int.wdp(baseWidth: Int = 360): Dp {
    val width = LocalConfiguration.current.screenWidthDp
    val scale = width / baseWidth.toFloat()
    return (this * scale).dp
}

@Composable
fun Int.ssp(): TextUnit {
    return (this * sc()).sp
}