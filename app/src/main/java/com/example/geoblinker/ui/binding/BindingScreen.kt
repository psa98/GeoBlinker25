package com.example.geoblinker.ui.binding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.geoblinker.ui.theme.sdp

enum class BindingScreen {
    One,
    Two
}

@Composable
fun BindingScreen(
    navController: NavHostController = rememberNavController(),
    toDevice: (String, String) -> Unit,
    toBack: () -> Unit
) {
    var imei by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF6F6F6)),
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
                    { toDevice(imei, it) },
                    { navController.navigate(BindingScreen.One.name) }
                )
            }
        }
    }
}