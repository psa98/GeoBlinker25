package com.example.geoblinker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    background = Color.White

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun GeoBlinkerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    /*
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    */
    val configuration = LocalConfiguration.current
    fun Int.scaledSp(): androidx.compose.ui.unit.TextUnit {
        val baseScreenWidth = 360 // Базовое разрешение (пример для 360dp ширины)
        val scaleFactor = configuration.screenWidthDp.toFloat() / baseScreenWidth
        return (this * scaleFactor).sp
    }

    val dynamicTypography = remember(configuration) {
        Typography(
            displayMedium = TextStyle(
                color = black,
                fontFamily = Manrope,
                fontWeight = FontWeight.Light,
                fontSize = 32.scaledSp(),
                letterSpacing = 0.3.em,
                textAlign = TextAlign.Center
            ),
            headlineLarge = TextStyle(
                color = black,
                fontFamily = Manrope,
                fontWeight = FontWeight.Medium,
                fontSize = 24.scaledSp()
            ),
            headlineMedium = TextStyle(
                color = black,
                fontFamily = Manrope,
                fontWeight = FontWeight.Normal,
                fontSize = 20.scaledSp()
            ),
            headlineSmall = TextStyle(
                color = black,
                fontFamily = Manrope,
                fontWeight = FontWeight.Normal,
                fontSize = 18.scaledSp()
            ),
            titleLarge = TextStyle(
                color = black,
                fontFamily = Manrope,
                fontWeight = FontWeight.Medium,
                fontSize = 20.scaledSp()
            ),
            titleMedium = TextStyle(
                color = black,
                fontFamily = Manrope,
                fontWeight = FontWeight.Medium,
                fontSize = 18.scaledSp(),
                textAlign = TextAlign.Center
            ),
            titleSmall = TextStyle(
                color = black,
                fontFamily = Manrope,
                fontWeight = FontWeight.Normal,
                fontSize = 16.scaledSp(),
                letterSpacing = 0.04.em
            ),
            bodyLarge = TextStyle(
                color = black,
                fontFamily = Manrope,
                fontWeight = FontWeight.Medium,
                fontSize = 16.scaledSp()
            ),
            bodyMedium = TextStyle(
                color = black,
                fontFamily = Manrope,
                fontWeight = FontWeight.Normal,
                fontSize = 16.scaledSp()
            ),
            labelLarge = TextStyle(
                color = black,
                fontFamily = Manrope,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.scaledSp()
            ),
            labelMedium = TextStyle(
                color = black,
                fontFamily = Manrope,
                fontWeight = FontWeight.Medium,
                fontSize = 14.scaledSp()
            ),
            labelSmall = TextStyle(
                color = black,
                fontFamily = Manrope,
                fontWeight = FontWeight.Medium,
                fontSize = 12.scaledSp()
            )
        ) // Пересоздавайте Typography при изменении конфигурации
    }

    val shapes = Shapes(
        small = RoundedCornerShape(100.sdp()),
        medium = RoundedCornerShape(16.sdp()),
        large = RoundedCornerShape(30.sdp())
    )

    val colorScheme = LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        shapes = shapes,
        typography = dynamicTypography,
        content = content
    )
}