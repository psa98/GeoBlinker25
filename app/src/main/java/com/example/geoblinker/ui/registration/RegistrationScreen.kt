package com.example.geoblinker.ui.registration

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

enum class RegistrationScreen {
    One,
    Two,
    Three,
}

@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel,
    navController: NavHostController = rememberNavController(),
    authorizationScreen: () -> Unit
) {
    Scaffold { innerPadding ->
        val name by viewModel.name.collectAsState()

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalArrangement = Arrangement.Center
        ) {
            NavHost(
                navController = navController,
                startDestination = RegistrationScreen.One.name,
                modifier = Modifier.width(360.dp)
            ) {
                composable(route = RegistrationScreen.One.name) {
                    OneScreen(
                        { phone, name ->
                            viewModel.updateState(phone, name)
                            navController.navigate(RegistrationScreen.Two.name)
                        },
                        { authorizationScreen() }
                    )
                }
                composable(route = RegistrationScreen.Two.name) {
                    TwoScreen(
                        { navController.navigate(RegistrationScreen.Three.name) },
                        { navController.navigate(RegistrationScreen.One.name) }
                    )
                }
                composable(route = RegistrationScreen.Three.name) {
                    ThreeScreen(name)
                }
            }
        }
    }
}