package com.example.geoblinker.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun sc(baseWidth: Int = 360): Float {
    val width = LocalConfiguration.current.screenWidthDp
    val scale = width / baseWidth.toFloat()
    return scale
}

@Composable
fun Int.sdp(baseWidth: Int = 360): Dp {
    val width = LocalConfiguration.current.screenWidthDp
    val scale = width / baseWidth.toFloat()
    return (this * scale).dp
}

@Composable
fun Int.ssp(
    baseWidth: Int = 360, // Базовая ширина экрана для масштабирования
): TextUnit {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val scale = screenWidth / baseWidth.toFloat()
    return (this * scale).sp
}