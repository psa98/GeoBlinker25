package com.example.geoblinker

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.geoblinker.data.AppDatabase
import com.example.geoblinker.data.Repository
import com.example.geoblinker.ui.authorization.AuthorizationScreen
import com.example.geoblinker.ui.authorization.AuthorizationViewModel
import com.example.geoblinker.ui.main.MainScreen
import com.example.geoblinker.ui.main.viewmodel.AvatarViewModel
import com.example.geoblinker.ui.main.viewmodel.DeviceViewModel
import com.example.geoblinker.ui.main.viewmodel.JournalViewModel
import com.example.geoblinker.ui.main.viewmodel.NotificationViewModel
import com.example.geoblinker.ui.main.viewmodel.ProfileViewModel
import com.example.geoblinker.ui.main.viewmodel.SubscriptionViewModel
import com.example.geoblinker.ui.registration.RegistrationScreen
import com.example.geoblinker.ui.registration.RegistrationViewModel

enum class GeoBlinkerScreen {
    Authorization,
    Registration,
    Main
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun GeoBlinkerScreen(
    profileViewModel: ProfileViewModel,
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val isLogin by profileViewModel.isLogin.collectAsState()
    val startDestination = if (isLogin) GeoBlinkerScreen.Main.name else GeoBlinkerScreen.Authorization.name

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = GeoBlinkerScreen.Authorization.name) {
                AuthorizationScreen(
                    AuthorizationViewModel(),
                    profileViewModel,
                    registrationScreen = { navController.navigate(GeoBlinkerScreen.Registration.name) },
                    mainScreen = {
                        profileViewModel.setInitialSubscription()
                        profileViewModel.setIsLogin(true)
                        navController.navigate(GeoBlinkerScreen.Main.name)
                    }
                )
            }
            composable(route = GeoBlinkerScreen.Registration.name) {
                RegistrationScreen(
                    RegistrationViewModel(),
                    profileViewModel,
                    authorizationScreen = { navController.navigate(GeoBlinkerScreen.Authorization.name) },
                    mainScreen = {
                        profileViewModel.setInitialSubscription()
                        profileViewModel.setIsLogin(true)
                        navController.navigate(GeoBlinkerScreen.Main.name)
                    }
                )
            }
            composable(route = GeoBlinkerScreen.Main.name) {
                MainScreen(
                    DeviceViewModel(
                        Repository(
                            AppDatabase.getInstance(application).deviceDao(),
                            AppDatabase.getInstance(application).typeSignalDao(),
                            AppDatabase.getInstance(application).signalDao(),
                            AppDatabase.getInstance(application).newsDao()),
                        application),
                    AvatarViewModel(application),
                    SubscriptionViewModel(),
                    profileViewModel,
                    JournalViewModel(application),
                    NotificationViewModel(application),
                    toBeginning = { navController.navigate(GeoBlinkerScreen.Authorization.name) }
                )
            }
        }
    }
}