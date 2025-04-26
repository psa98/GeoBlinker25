package com.example.geoblinker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.geoblinker.R

val Manrope = FontFamily(
    Font(R.font.manrope_regular, FontWeight.Normal),
    Font(R.font.manrope_bold, FontWeight.Bold),
    Font(R.font.manrope_semibold, FontWeight.SemiBold),
    Font(R.font.manrope_medium, FontWeight.Medium),
    Font(R.font.manrope_light, FontWeight.Light)
)

val black = Color(0xFF222221)

val Typography = Typography(
    displayMedium = TextStyle(
        color = black,
        fontFamily = Manrope,
        fontWeight = FontWeight.Light,
        fontSize = 32.sp,
        letterSpacing = 0.3.em,
        textAlign = TextAlign.Center
    ),
    headlineLarge = TextStyle(
        color = black,
        fontFamily = Manrope,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp
    ),
    headlineMedium = TextStyle(
        color = black,
        fontFamily = Manrope,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),
    titleLarge = TextStyle(
        color = black,
        fontFamily = Manrope,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp
    ),
    titleMedium = TextStyle(
        color = black,
        fontFamily = Manrope,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.04.em
    ),
    bodyLarge = TextStyle(
        color = black,
        fontFamily = Manrope,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        color = black,
        fontFamily = Manrope,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)