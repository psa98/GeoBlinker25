package com.example.geoblinker.ui.main

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.geoblinker.R
import com.example.geoblinker.data.Device
import com.example.geoblinker.ui.GreenSmallButton
import com.example.geoblinker.ui.WhiteSmallButton
import com.example.geoblinker.ui.main.binding.BindingOneScreen
import com.example.geoblinker.ui.main.binding.BindingThreeScreen
import com.example.geoblinker.ui.main.binding.BindingTwoScreen
import com.example.geoblinker.ui.main.device.DeviceListSignalScreen
import com.example.geoblinker.ui.main.device.DeviceOneScreen
import com.example.geoblinker.ui.main.device.DeviceThreeScreen
import com.example.geoblinker.ui.main.device.DeviceTwoScreen
import com.example.geoblinker.ui.main.device.detach.DeviceDetachOneScreen
import com.example.geoblinker.ui.main.device.detach.DeviceDetachTwoScreen
import com.example.geoblinker.ui.theme.sdp

enum class MainScreen {
    Map,
    Binding,
    BindingOne,
    BindingTwo,
    BindingThree,
    Device,
    DeviceOne,
    DeviceTwo,
    DeviceThree,
    DeviceListSignalScreen,
    List,
    DeviceDetach,
    DeviceDetachOne,
    DeviceDetachTwo,
    Notifications
}

@Composable
fun Notifications(
    notificationsCount: Int = 0,
    toNotifications: () -> Unit
) {
    Box(
        modifier = Modifier.clickable { toNotifications() }
    ) {
        Box(
            Modifier.size(50.sdp()),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.notifications),
                contentDescription = null,
                modifier = Modifier.size(24.sdp())
            )
        }

        if (notificationsCount > 0) {
            Box(
                Modifier.size(50.sdp()).offset(12.sdp(), (-12).sdp()),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    Modifier
                        .background(
                            color = Color(0xFFF1137E),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        notificationsCount.toString(),
                        modifier = Modifier.padding(horizontal = 6.sdp(), vertical = 1.sdp()),
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun TopBar(
    currentRoute: String,
    navController: NavHostController,
    countNotifications: Int
) {
    Surface(
        modifier = Modifier.shadow(
            4.sdp(),
            RoundedCornerShape(bottomStart = 30.sdp(), bottomEnd = 30.sdp()),
            clip = false,
            ambientColor = Color.Black,
            spotColor = Color.Black.copy(0.25f)
        )
        ,
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
            Notifications(
                countNotifications,
                { navController.navigate(MainScreen.Notifications.name) }
            )
        }
    }
}

@Composable
fun MainScreen(
    viewModel: DeviceViewModel,
    navController: NavHostController = rememberNavController(),
) {
    val device by viewModel.device.collectAsState()
    val signals by viewModel.signals.collectAsState()
    val news by viewModel.news.collectAsState()
    var currentRoute by remember { mutableStateOf(MainScreen.Map.name) }
    var bindingImei by remember { mutableStateOf("") }
    var previousScreen by remember { mutableStateOf(MainScreen.Map.name) }
    var countNotifications by remember { mutableIntStateOf(0) }
    var selectedMarker by remember { mutableStateOf<Device?>(null) }

    fun BackgroundColor(): Color {
        return when(currentRoute) {
            MainScreen.Binding.name -> Color(0xFFF6F6F6)
            else -> Color(0xFFEFEFEF)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.clearDevice()
    }

    LaunchedEffect(signals, news) {
        countNotifications = signals.size + news.size - (signals.count { it.isSeen } + news.count { it.isSeen })
    }

    NavHost(
        navController = navController,
        startDestination = MainScreen.Map.name,
        modifier = Modifier
            .background(BackgroundColor())
            .fillMaxSize()
            .padding(
                start = 15.sdp(),
                top = 90.sdp(),
                end = 15.sdp()
            )
    ) {
        composable(route = MainScreen.Map.name) {
            currentRoute = MainScreen.Map.name
            MapScreen(
                viewModel,
                selectMarker = selectedMarker,
                { navController.navigate("${MainScreen.Binding.name}/${MainScreen.Map.name}") },
                toDeviceScreen = { device ->
                    viewModel.setDevice(device)
                    navController.navigate("${MainScreen.Device.name}/${MainScreen.Map.name}")
                }
            )
            selectedMarker = null
        }
        composable(route = MainScreen.List.name) {
            currentRoute = MainScreen.List.name
            ListScreen(
                viewModel,
                toBindingScreen = { navController.navigate("${MainScreen.Binding.name}/${MainScreen.List.name}") },
                toDeviceScreen = {
                    navController.navigate("${MainScreen.Device.name}/${MainScreen.List.name}")
                }
            )
        }

        navigation(
            route = "${MainScreen.Binding.name}/{previousScreen}",
            startDestination = MainScreen.BindingOne.name
        ) {
            currentRoute = MainScreen.Binding.name
            composable(route = MainScreen.BindingOne.name) { backStackEntry ->
                backStackEntry.arguments?.getString("previousScreen")?.let {
                    previousScreen = it
                }
                BindingOneScreen({
                    bindingImei = it
                    navController.navigate(MainScreen.BindingTwo.name)
                },
                    { navController.navigate(previousScreen) }
                )
            }
            composable(route = MainScreen.BindingTwo.name) {
                BindingTwoScreen(
                    bindingImei,
                    { name ->
                        viewModel.insertDevice(bindingImei, name)
                        navController.navigate(MainScreen.BindingThree.name)
                    },
                    { navController.navigateUp() }
                )
            }
            composable(route = MainScreen.BindingThree.name) {
                BindingThreeScreen(
                    device,
                    { navController.navigate(MainScreen.BindingOne.name) },
                    { navController.navigate(MainScreen.DeviceTwo.name) },
                    {
                        selectedMarker = device
                        navController.navigate(MainScreen.Map.name)
                    },
                    { navController.navigate(previousScreen) }
                )
            }
        }

        navigation(
            route = "${MainScreen.Device.name}/{previousScreen}",
            startDestination = MainScreen.DeviceOne.name
        ) {
            currentRoute = MainScreen.Device.name
            composable(route = MainScreen.DeviceOne.name) { backStackEntry ->
                backStackEntry.arguments?.getString("previousScreen")?.let {
                    previousScreen = it
                }
                DeviceOneScreen(
                    viewModel,
                    { navController.navigate(MainScreen.DeviceTwo.name) },
                    { navController.navigate(MainScreen.DeviceListSignalScreen.name) },
                    {
                        selectedMarker = device
                        navController.navigate(MainScreen.Map.name)
                    },
                    { navController.navigate(MainScreen.DeviceDetachOne.name) },
                    { navController.navigate(previousScreen) }
                )
            }
            composable(route = MainScreen.DeviceTwo.name) {
                DeviceTwoScreen(
                    viewModel,
                    { navController.navigate(MainScreen.DeviceThree.name) },
                    { navController.navigate("${MainScreen.Device.name}/${previousScreen}") }
                )
            }
            composable(route = MainScreen.DeviceThree.name) {
                DeviceThreeScreen(
                    viewModel,
                    { navController.navigateUp() }
                )
            }
            composable(route = MainScreen.DeviceListSignalScreen.name) {
                DeviceListSignalScreen(
                    viewModel,
                    { navController.navigateUp() }
                )
            }
        }

        navigation(
            route = MainScreen.DeviceDetach.name,
            startDestination = MainScreen.DeviceDetachOne.name
        ) {
            composable(route = MainScreen.DeviceDetachOne.name) {
                DeviceDetachOneScreen(
                    viewModel,
                    toTwo = { name, imei ->
                        navController.navigate("${MainScreen.DeviceDetachTwo.name}?name=$name&imei=$imei")
                    },
                    toBack = { navController.navigateUp() }
                )
            }
            composable(
                route = "${MainScreen.DeviceDetachTwo.name}?name={name}&imei={imei}",
                arguments = listOf(
                    navArgument("name") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    },
                    navArgument("imei") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { backStackEntry ->
                val name = backStackEntry.arguments?.getString("name")!!
                val imei = backStackEntry.arguments?.getString("imei")!!

                DeviceDetachTwoScreen(
                    name,
                    imei,
                    { navController.navigate(MainScreen.Map.name) }
                )
            }
        }

        composable(route = MainScreen.Notifications.name) {
            currentRoute = MainScreen.Notifications.name
            NotificationsScreen(
                viewModel,
                { navController.navigateUp() }
            )
        }
    }

    TopBar(
        currentRoute,
        navController,
        countNotifications
    )
}