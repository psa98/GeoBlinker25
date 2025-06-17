package com.example.geoblinker.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.geoblinker.ui.theme.hdp

@Composable
fun HSpacer(
    height: Int
) {
    Spacer(Modifier.height(height.hdp()))
}