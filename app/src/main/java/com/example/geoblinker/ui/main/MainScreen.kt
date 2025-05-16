package com.example.geoblinker.ui.main

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.geoblinker.R
import com.example.geoblinker.ui.GreenSmallButton
import com.example.geoblinker.ui.WhiteSmallButton
import com.example.geoblinker.ui.binding.BindingScreen
import com.example.geoblinker.ui.device.DeviceScreen
import com.example.geoblinker.ui.device.DeviceViewModel
import com.example.geoblinker.ui.theme.sdp

enum class MainScreen {
    Map,
    Binding,
    Device,
    List
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun TopBar(
    currentRoute: String,
    navController: NavHostController
) {
    Surface(
        modifier = Modifier.shadow(
            4.sdp(),
            RoundedCornerShape(bottomStart = 30.sdp(), bottomEnd = 30.sdp()),
            clip = false,
            ambientColor = Color.Black,
            spotColor = Color.Black.copy(0.25f)
        ),
        shape = RoundedCornerShape(bottomStart = 30.sdp(), bottomEnd = 30.sdp()),
        color = Color.White.copy(alpha = 0.6f)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(10.sdp()),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.user_without_photo),
                contentDescription = null,
                modifier = Modifier.size(50.sdp()),
                tint = Color.Unspecified
            )
            Spacer(Modifier.width(10.sdp()))
            if (currentRoute == MainScreen.Map.name) {
                GreenSmallButton(
                    stringResource(R.string.map),
                    {}
                )
            } else {
                WhiteSmallButton(
                    stringResource(R.string.map),
                    { navController.navigate(MainScreen.Map.name) }
                )
            }
            Spacer(Modifier.width(10.sdp()))
            if (currentRoute == MainScreen.List.name) {
                GreenSmallButton(
                    stringResource(R.string.list),
                    {}
                )
            } else {
                WhiteSmallButton(
                    stringResource(R.string.list),
                    { navController.navigate(MainScreen.List.name) }
                )
            }
            Spacer(Modifier.width(10.sdp()))
            Notifications(3)
        }
    }
}

@Composable
fun MainScreen(
    viewModel: DeviceViewModel,
    navController: NavHostController = rememberNavController(),
) {
    var currentRoute by remember { mutableStateOf(MainScreen.Map.name) }

    fun BackgroundColor(): Color {
        return when(currentRoute) {
            MainScreen.Binding.name -> Color(0xFFF6F6F6)
            else -> Color(0xFFEFEFEF)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.clearDevice()
    }

    NavHost(
        navController = navController,
        startDestination = MainScreen.Map.name,
        modifier = Modifier.background(BackgroundColor())
    ) {
        composable(route = MainScreen.Map.name) {
            currentRoute = MainScreen.Map.name
            MapScreen(
                { viewModel.checkDevices() },
                { navController.navigate("${MainScreen.Binding.name}/${MainScreen.Map.name}") }
            )
        }
        composable(route = "${MainScreen.Binding.name}/{previousScreen}") { backStackEntry ->
            currentRoute = MainScreen.Binding.name
            val previousScreen = backStackEntry.arguments?.getString("previousScreen")!!
            BindingScreen(
                viewModel,
                toBack = { navController.navigate(previousScreen) }
            )
        }
        composable(route = "${MainScreen.Device.name}/{previousScreen}") { backStackEntry ->
            currentRoute = MainScreen.Device.name
            val previousScreen = backStackEntry.arguments?.getString("previousScreen")!!
            Box(Modifier
                .fillMaxSize()
                .padding(
                    start = 15.sdp(),
                    top = 90.sdp(),
                    end = 15.sdp()
                )) {
                DeviceScreen(
                    viewModel,
                    toBack = { navController.navigate(previousScreen) }
                )
            }
        }
        composable(route = MainScreen.List.name) {
            currentRoute = MainScreen.List.name
            Box(Modifier
                .fillMaxSize()
                .padding(
                    start = 15.sdp(),
                    top = 90.sdp(),
                    end = 15.sdp()
                )) {
                ListScreen(
                    viewModel,
                    toBindingScreen = { navController.navigate("${MainScreen.Binding.name}/${MainScreen.List.name}") },
                    toDeviceScreen = { device ->
                        viewModel.setDevice(device)
                        navController.navigate("${MainScreen.Device.name}/${MainScreen.List.name}")
                    }
                )
            }
        }
    }

    TopBar(
        currentRoute,
        navController
    )
}