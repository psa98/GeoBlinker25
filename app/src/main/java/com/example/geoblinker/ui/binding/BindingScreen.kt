package com.example.geoblinker.ui.binding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.geoblinker.ui.device.DeviceViewModel
import com.example.geoblinker.ui.theme.sdp

enum class BindingScreen {
    One,
    Two,
    Three
}

@Composable
fun BindingScreen(
    viewModel: DeviceViewModel,
    navController: NavHostController = rememberNavController(),
    toBack: () -> Unit
) {
    var imei by remember { mutableStateOf("") }
    val device by viewModel.device.collectAsState()

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center
    ) {
        NavHost(
            navController = navController,
            startDestination = BindingScreen.One.name,
            modifier = Modifier.width(310.sdp())
        ) {
            composable(route = BindingScreen.One.name) {
                OneScreen( {
                    imei = it
                    navController.navigate(BindingScreen.Two.name)
                },
                    toBack
                )
            }
            composable(route = BindingScreen.Two.name) {
                TwoScreen(
                    imei,
                    { name ->
                        viewModel.insertDevice(imei, name)
                        navController.navigate(BindingScreen.Three.name)
                    },
                    { navController.navigate(BindingScreen.One.name) }
                )
            }
            composable(route = BindingScreen.Three.name) {
                ThreeScreen(
                    device,
                    { navController.navigate(BindingScreen.One.name) },
                    toBack
                )
            }
        }
    }
}