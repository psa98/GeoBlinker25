package com.example.geoblinker.ui.auth.authorization

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.geoblinker.ui.auth.CodeScreen
import com.example.geoblinker.ui.theme.sdp

enum class AuthorizationScreen {
    One,
    Two,
    Three,
    Four
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun AuthorizationScreen(
    navController: NavHostController = rememberNavController(),
    registrationScreen: () -> Unit,
    mainScreen: () -> Unit
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel = AuthorizationViewModel(application)

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
                    CodeScreen(
                        {
                            viewModel.saveData()
                            navController.navigate(AuthorizationScreen.Four.name)
                        },
                        { navController.navigate(AuthorizationScreen.Two.name) },
                        viewModel
                    )
                }
                composable(route = AuthorizationScreen.Four.name) {
                    FourScreen(
                        viewModel.name.value,
                        mainScreen
                    )
                }
            }
        }
    }
}