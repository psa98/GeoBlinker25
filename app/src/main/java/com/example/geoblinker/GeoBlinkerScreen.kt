package com.example.geoblinker

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.geoblinker.data.Repository
import com.example.geoblinker.ui.GeoBlinkerViewModel
import com.example.geoblinker.ui.authorization.AuthorizationScreen
import com.example.geoblinker.ui.authorization.AuthorizationViewModel
import com.example.geoblinker.ui.device.DeviceViewModel
import com.example.geoblinker.ui.main.MainScreen
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
    viewModel: GeoBlinkerViewModel,
    navController: NavHostController = rememberNavController()
) {
    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = GeoBlinkerScreen.Authorization.name, // TODO: При дебаге Main
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = GeoBlinkerScreen.Authorization.name) {
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
                    DeviceViewModel(repository)
                )
            }
        }
    }
}