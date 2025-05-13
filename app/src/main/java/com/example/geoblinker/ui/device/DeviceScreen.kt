package com.example.geoblinker.ui.device

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.geoblinker.ui.theme.sdp

enum class DeviceScreen {
    One
}

@Composable
fun DeviceScreen(
    viewModel: DeviceViewModel,
    navController: NavHostController = rememberNavController(),
    toBindingScreen: () -> Unit,
    toBack: () -> Unit
) {
    val device by viewModel.device.collectAsState()

    Row(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF6F6F6)),
        horizontalArrangement = Arrangement.Center
    ) {
        NavHost(
            navController = navController,
            startDestination = DeviceScreen.One.name,
            modifier = Modifier.width(330.sdp())
        ) {
            composable(route = DeviceScreen.One.name) {
                OneScreen(
                    device,
                    toBindingScreen,
                    toBack
                )
            }
        }
    }
}