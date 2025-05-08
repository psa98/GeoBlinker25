package com.example.geoblinker

import android.app.Application
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.geoblinker.ui.GeoBlinkerViewModel
import com.example.geoblinker.ui.authorization.AuthorizationScreen
import com.example.geoblinker.ui.main.MainScreen
import com.example.geoblinker.ui.registration.RegistrationScreen

enum class GeoBlinkerScreen {
    Authorization,
    Registration,
    Main
}

@Composable
fun GeoBlinkerScreen(
    application: Application,
    viewModel: GeoBlinkerViewModel,
    navController: NavHostController = rememberNavController()
) {
    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = GeoBlinkerScreen.Authorization.name, // TODO: При дебаге Main
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding()
            )
        ) {
            composable(route = GeoBlinkerScreen.Authorization.name) {
                AuthorizationScreen(
                    application,
                    registrationScreen = { navController.navigate(GeoBlinkerScreen.Registration.name) },
                    mainScreen = { navController.navigate(GeoBlinkerScreen.Main.name) }
                )
            }
            composable(route = GeoBlinkerScreen.Registration.name) {
                RegistrationScreen(
                    application,
                    authorizationScreen = { navController.navigate(GeoBlinkerScreen.Authorization.name) },
                    mainScreen = { navController.navigate(GeoBlinkerScreen.Main.name) }
                )
            }
            composable(route = GeoBlinkerScreen.Main.name) {
                MainScreen(
                    application
                )
            }
        }
    }
}