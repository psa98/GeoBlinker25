package com.example.geoblinker.ui.main

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.geoblinker.R
import com.example.geoblinker.data.AppDatabase
import com.example.geoblinker.data.Device
import com.example.geoblinker.ui.GreenMediumButton
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
import com.example.geoblinker.ui.main.profile.JournalSignalsScreen
import com.example.geoblinker.ui.main.profile.ProfileScreen
import com.example.geoblinker.ui.main.profile.settings.ConfirmationCodeSettingsScreen
import com.example.geoblinker.ui.main.profile.settings.EmailSettingsScreen
import com.example.geoblinker.ui.main.profile.settings.NameSettingsScreen
import com.example.geoblinker.ui.main.profile.settings.NotificationSettingsScreen
import com.example.geoblinker.ui.main.profile.settings.PhoneSettingsScreen
import com.example.geoblinker.ui.main.profile.settings.SettingsScreen
import com.example.geoblinker.ui.main.profile.settings.UnitDistanceSettingsScreen
import com.example.geoblinker.ui.main.profile.subscription.SubscriptionOneScreen
import com.example.geoblinker.ui.main.profile.subscription.SubscriptionReadyScreen
import com.example.geoblinker.ui.main.profile.subscription.SubscriptionTwoScreen
import com.example.geoblinker.ui.main.viewmodel.AvatarViewModel
import com.example.geoblinker.ui.main.viewmodel.DeviceViewModel
import com.example.geoblinker.ui.main.viewmodel.JournalViewModel
import com.example.geoblinker.ui.main.viewmodel.NotificationViewModel
import com.example.geoblinker.ui.main.viewmodel.ProfileViewModel
import com.example.geoblinker.ui.main.viewmodel.SubscriptionViewModel
import com.example.geoblinker.ui.theme.sdp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.Instant

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
    Notifications,
    Profile,
    ProfileOne,
    JournalSignals,
    Subscription,
    SubscriptionOne,
    SubscriptionTwo,
    SubscriptionReady,
    Settings,
    SettingsOne,
    NameSettings,
    PhoneSettings,
    EmailSettings,
    NotificationSettings,
    UnitsDistanceSettings,
    ConfirmationCodeSettings
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
    countNotifications: Int,
    viewModel: AvatarViewModel
) {
    val avatarUri by viewModel.avatarUri.collectAsState()

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
            if (avatarUri == null) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.user_without_photo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.sdp())
                        .clickable {
                            navController.navigate(MainScreen.Profile.name)
                        },
                    tint = Color.Unspecified
                )
            }
            else {
                AsyncImage(
                    avatarUri,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(50.sdp())
                        .clickable {
                            navController.navigate(MainScreen.Profile.name)
                        },
                    contentScale = ContentScale.Crop
                )
            }
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

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun MainScreen(
    viewModel: DeviceViewModel,
    avatarViewModel: AvatarViewModel,
    subscriptionViewModel: SubscriptionViewModel,
    profileViewModel: ProfileViewModel,
    journalViewModel: JournalViewModel,
    notificationViewModel: NotificationViewModel,
    navController: NavHostController = rememberNavController(),
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val scope = rememberCoroutineScope()

    val device by viewModel.device.collectAsState()
    val signals by viewModel.signals.collectAsState()
    val news by viewModel.news.collectAsState()
    val pickSubscription by subscriptionViewModel.pickSubscription.collectAsState()
    val subscription by profileViewModel.subscription.collectAsState()
    var currentRoute by remember { mutableStateOf(MainScreen.Map.name) }
    var bindingImei by remember { mutableStateOf("") }
    var previousScreen by remember { mutableStateOf(MainScreen.Map.name) }
    var countNotifications by remember { mutableIntStateOf(0) }
    var selectedMarker by remember { mutableStateOf<Device?>(null) }
    var isShow by remember { mutableStateOf(false) }

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

    LaunchedEffect(Instant.now().toEpochMilli()) {
        if (Instant.now().toEpochMilli() > subscription)
            isShow = true
        else
            isShow = false
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
            composable(route = MainScreen.BindingOne.name) { backStackEntry ->
                currentRoute = MainScreen.Binding.name
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
            composable(route = MainScreen.DeviceOne.name) { backStackEntry ->
                currentRoute = MainScreen.Device.name
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
                    profileViewModel,
                    {
                        navController.navigate("${MainScreen.EmailSettings.name}?show=true")
                    },
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

        navigation(
            route = MainScreen.Profile.name,
            startDestination = MainScreen.ProfileOne.name
        ) {
            composable(route = MainScreen.ProfileOne.name) {
                currentRoute = MainScreen.Profile.name
                ProfileScreen(
                    avatarViewModel,
                    profileViewModel,
                    toSubscription = { navController.navigate("${MainScreen.Subscription.name}/${MainScreen.Profile.name}") },
                    toListDevices = { navController.navigate(MainScreen.List.name) },
                    toJournalSignals = { navController.navigate(MainScreen.JournalSignals.name) },
                    toSettings = { navController.navigate("${MainScreen.Settings.name}/${MainScreen.Profile.name}") },
                    toNameSettings = { navController.navigate(MainScreen.NameSettings.name) },
                    toBack = { navController.navigateUp() }
                )
            }
            composable(route = MainScreen.JournalSignals.name) {
                JournalSignalsScreen(
                    viewModel,
                    journalViewModel,
                    toBack = { navController.navigateUp() }
                )
            }
        }

        navigation(
            route = "${MainScreen.Subscription.name}/{previousScreen}",
            startDestination = MainScreen.SubscriptionOne.name
        ) {
            composable(route = MainScreen.SubscriptionOne.name) { backStackEntry ->
                currentRoute = MainScreen.Subscription.name
                backStackEntry.arguments?.getString("previousScreen")?.let {
                    previousScreen = it
                }
                SubscriptionOneScreen(
                    subscriptionViewModel,
                    { navController.navigate(MainScreen.SubscriptionTwo.name) },
                    { navController.navigate(previousScreen) }
                )
            }
            composable(route = MainScreen.SubscriptionTwo.name) {
                SubscriptionTwoScreen(
                    subscriptionViewModel,
                    {
                        if (subscriptionViewModel.paySubscription()) {
                            profileViewModel.addMonthsSubscription(pickSubscription.period.toLong())
                            navController.navigate(MainScreen.SubscriptionReady.name)
                        }
                        else {
                            // TODO: добавить экран об ошибке оплаты
                        }
                    },
                    { navController.navigateUp() }
                )
            }
            composable(route = MainScreen.SubscriptionReady.name) {
                SubscriptionReadyScreen(
                    profileViewModel,
                    toBack = { navController.navigate(previousScreen) }
                )
            }
        }

        navigation(
            route = "${MainScreen.Settings.name}/{previousScreen}",
            startDestination = MainScreen.SettingsOne.name
        ) {
            composable(route = MainScreen.SettingsOne.name) { backStackEntry ->
                currentRoute = MainScreen.Settings.name
                backStackEntry.arguments?.getString("previousScreen")?.let {
                    previousScreen = it
                }
                SettingsScreen(
                    toName = { navController.navigate(MainScreen.NameSettings.name) },
                    toPhone = { navController.navigate(MainScreen.PhoneSettings.name) },
                    toEmail = { navController.navigate(MainScreen.EmailSettings.name) },
                    toNotification = { navController.navigate(MainScreen.NotificationSettings.name) },
                    toUnitsDistance = { navController.navigate(MainScreen.UnitsDistanceSettings.name) },
                    toConfirmationCode = { navController.navigate(MainScreen.ConfirmationCodeSettings.name) },
                    toLogout = {
                        scope.launch(Dispatchers.IO) {
                            AppDatabase.getInstance(application).clearAllTables()
                        }
                        avatarViewModel.removeAvatar()
                        notificationViewModel.logout()
                        profileViewModel.logout()
                    },
                    toBack = { navController.navigateUp() }
                )
            }
            composable(route = MainScreen.NameSettings.name) {
                NameSettingsScreen(
                    profileViewModel,
                    toBack = { navController.navigateUp() }
                )
            }
            composable(route = MainScreen.PhoneSettings.name) {
                PhoneSettingsScreen(
                    profileViewModel,
                    toBack = { navController.navigateUp() }
                )
            }
            composable(
                route = "${MainScreen.EmailSettings.name}?show={show}",
                arguments = listOf(
                    navArgument("show") {
                        type = NavType.BoolType
                        nullable = false
                        defaultValue = false
                    }
                )
            ) { backStackEntry ->
                val isShowLocal = backStackEntry.arguments?.getBoolean("show", false)!!
                EmailSettingsScreen(
                    profileViewModel,
                    isShowLocal,
                    toBack = { navController.navigateUp() }
                )
            }
            composable(route = MainScreen.NotificationSettings.name) {
                NotificationSettingsScreen(
                    notificationViewModel,
                    profileViewModel,
                    toLinkEmail = { navController.navigate("${MainScreen.EmailSettings.name}?show=true") },
                    toBack = { navController.navigateUp() }
                )
            }
            composable(route = MainScreen.UnitsDistanceSettings.name) {
                UnitDistanceSettingsScreen(
                    viewModel,
                    toBack = { navController.navigateUp() }
                )
            }
            composable(route = MainScreen.ConfirmationCodeSettings.name) {
                ConfirmationCodeSettingsScreen(
                    profileViewModel,
                    toLinkEmail = { navController.navigate("${MainScreen.EmailSettings.name}?show=true") },
                    toBack = { navController.navigateUp() }
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
        countNotifications,
        avatarViewModel
    )

    if (isShow && currentRoute != MainScreen.Subscription.name) {
        Log.d("CurrentRoute", currentRoute)
        Dialog(
            {},
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.width(310.sdp()),
                    shape = RoundedCornerShape(16.sdp()),
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier.padding(
                            horizontal = 25.sdp(),
                            vertical = 30.sdp()
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.wallet),
                            contentDescription = null,
                            modifier = Modifier.size(28.sdp()),
                            tint = Color.Unspecified
                        )
                        Spacer(Modifier.height(20.sdp()))
                        Text(
                            "Бесплатный пробный период закончился. Оплатите подписку, чтобы пользоваться функциями приложения GeoBlinker.",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(Modifier.height(28.sdp()))
                        GreenMediumButton(
                            text = "Перейти к оплате",
                            onClick = { navController.navigate("${MainScreen.Subscription.name}/${currentRoute}") }
                        )
                    }
                }
            }
        }
    }
}