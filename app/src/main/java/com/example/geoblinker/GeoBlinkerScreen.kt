package com.example.geoblinker

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.geoblinker.data.Repository
import com.example.geoblinker.ui.authorization.AuthorizationScreen
import com.example.geoblinker.ui.authorization.AuthorizationViewModel
import com.example.geoblinker.ui.main.MainScreen
import com.example.geoblinker.ui.main.viewmodel.AvatarViewModel
import com.example.geoblinker.ui.main.viewmodel.DeviceViewModel
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
    repository: Repository,
    navController: NavHostController = rememberNavController()
) {
    // Получаем контекст
    val context = LocalContext.current

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = GeoBlinkerScreen.Main.name, // TODO: При дебаге Main
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = GeoBlinkerScreen.Authorization.name) {
                BoxWithConstraints(Modifier.fillMaxSize()) {
                    Log.i("Height1", maxHeight.toString())
                }
                AuthorizationScreen(
                    AuthorizationViewModel(),
                    registrationScreen = { navController.navigate(GeoBlinkerScreen.Registration.name) },
                    mainScreen = { navController.navigate(GeoBlinkerScreen.Main.name) }
                )
            }
            composable(route = GeoBlinkerScreen.Registration.name) {
                RegistrationScreen(
                    RegistrationViewModel(),
                    authorizationScreen = { navController.navigate(GeoBlinkerScreen.Authorization.name) },
                    mainScreen = { navController.navigate(GeoBlinkerScreen.Main.name) }
                )
            }
            composable(route = GeoBlinkerScreen.Main.name) {
                MainScreen(
                    DeviceViewModel(repository),
                    AvatarViewModel(context),
                    SubscriptionViewModel(),
                    ProfileViewModel(context)
                )
            }
        }
    }
}