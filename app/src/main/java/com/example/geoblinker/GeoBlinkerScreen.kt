package com.example.geoblinker

import android.app.Application
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.geoblinker.ui.GeoBlinkerViewModel
import com.example.geoblinker.ui.authorization.AuthorizationScreen
import com.example.geoblinker.ui.authorization.AuthorizationViewModel
import com.example.geoblinker.ui.registration.RegistrationScreen
import com.example.geoblinker.ui.registration.RegistrationViewModel

enum class GeoBlinkerScreen {
    Authorization,
    Registration
}

@Composable
fun GeoBlinkerScreen(
    application: Application,
    viewModel: GeoBlinkerViewModel,
    navController: NavHostController = rememberNavController()
) {
    val authorizationView = AuthorizationViewModel(application)
    val registrationViewModel = RegistrationViewModel(application)

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = GeoBlinkerScreen.Authorization.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = GeoBlinkerScreen.Authorization.name) {
                AuthorizationScreen(
                    authorizationView,
                    registrationScreen = { navController.navigate(GeoBlinkerScreen.Registration.name) }
                )
            }
            composable(route = GeoBlinkerScreen.Registration.name) {
                RegistrationScreen(
                    registrationViewModel,
                    authorizationScreen = { navController.navigate(GeoBlinkerScreen.Authorization.name) }
                )
            }
        }
    }
}