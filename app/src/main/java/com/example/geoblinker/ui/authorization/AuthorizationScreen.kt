package com.example.geoblinker.ui.authorization

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.geoblinker.ui.theme.sdp

enum class AuthorizationScreen {
    One,
    Two,
    Three,
    Four
}

@Composable
fun AuthorizationScreen(
    viewModel: AuthorizationViewModel,
    navController: NavHostController = rememberNavController(),
    registrationScreen: () -> Unit,
    mainScreen: () -> Unit
) {
    Scaffold { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalArrangement = Arrangement.Center
        ) {
            NavHost(
                navController = navController,
                startDestination = AuthorizationScreen.One.name,
                modifier = Modifier.width(310.sdp())
            ) {
                composable(route = AuthorizationScreen.One.name) {
                    OneScreen({ navController.navigate(AuthorizationScreen.Two.name) }, registrationScreen)
                }
                composable(route = AuthorizationScreen.Two.name) {
                    TwoScreen(
                        { navController.navigate(AuthorizationScreen.Three.name) },
                        { navController.navigate(AuthorizationScreen.One.name) },
                        viewModel
                    )
                }
                composable(route = AuthorizationScreen.Three.name) {
                    ThreeScreen(
                        { navController.navigate(AuthorizationScreen.Four.name) },
                        { navController.navigate(AuthorizationScreen.Two.name) },
                        viewModel
                    )
                }
                composable(route = AuthorizationScreen.Four.name) {
                    FourScreen(
                        mainScreen
                    )
                }
            }
        }
    }
}