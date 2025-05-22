package com.example.geoblinker.ui.main

import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class ComicBubbleShape(
    private val cornerRadius: Dp = 16.dp,
    private val pointerHeight: Dp = 12.dp,
    private val pointerWidth: Dp = 24.dp
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            Path().apply {
                val radiusPx = with(density) { cornerRadius.toPx() }
                val pointerH = with(density) { pointerHeight.toPx() }
                val pointerW = with(density) { pointerWidth.toPx() }

                // Основное тело с скругленными углами
                addRoundRect(
                    RoundRect(
                        left = 0f,
                        top = 0f,
                        right = size.width,
                        bottom = size.height - pointerH,
                        radiusX = radiusPx,
                        radiusY = radiusPx
                    )
                )

                // Треугольный указатель с плавным переходом
                moveTo(size.width / 2 - pointerW / 2, size.height - pointerH)
                quadraticTo(
                    size.width / 2 - pointerW / 4, size.height - pointerH * 0.8f,
                    size.width / 2, size.height
                )
                quadraticTo(
                    size.width / 2 + pointerW / 4, size.height - pointerH * 0.8f,
                    size.width / 2 + pointerW / 2, size.height - pointerH
                )
                close()
            }
        )
    }
}