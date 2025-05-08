package com.example.geoblinker.ui.registration

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.geoblinker.ui.theme.sdp

enum class RegistrationScreen {
    One,
    Two,
    Three,
}

@Composable
fun RegistrationScreen(
    application: Application,
    navController: NavHostController = rememberNavController(),
    authorizationScreen: () -> Unit,
    mainScreen: () -> Unit
) {
    val viewModel = RegistrationViewModel(application)
    val name by viewModel.name.collectAsState()

    Scaffold { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalArrangement = Arrangement.Center
        ) {
            NavHost(
                navController = navController,
                startDestination = RegistrationScreen.One.name,
                modifier = Modifier.width(310.sdp())
            ) {
                composable(route = RegistrationScreen.One.name) {
                    OneScreen(
                        { phone, name ->
                            viewModel.updateState(phone, name)
                            navController.navigate(RegistrationScreen.Two.name)
                        },
                        { authorizationScreen() },
                        viewModel
                    )
                }
                composable(route = RegistrationScreen.Two.name) {
                    TwoScreen(
                        { navController.navigate(RegistrationScreen.Three.name) },
                        { navController.navigate(RegistrationScreen.One.name) },
                        viewModel
                    )
                }
                composable(route = RegistrationScreen.Three.name) {
                    ThreeScreen(
                        name,
                        mainScreen
                    )
                }
            }
        }
    }
}