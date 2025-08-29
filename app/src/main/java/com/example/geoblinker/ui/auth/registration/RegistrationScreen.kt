package com.example.geoblinker.ui.auth.registration

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.geoblinker.ui.auth.CodeScreen
import com.example.geoblinker.ui.theme.sdp

enum class RegistrationScreen {
    One,
    Two,
    Three,
    Four
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun RegistrationScreen(
    navController: NavHostController = rememberNavController(),
    authorizationScreen: () -> Unit,
    mainScreen: () -> Unit,
    faqScreen: () -> Unit
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel = RegistrationViewModel(application)
    val name by viewModel.name

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            NavHost(
                navController = navController,
                startDestination = RegistrationScreen.One.name
            ) {
                composable(route = RegistrationScreen.One.name) {
                    Box(modifier = Modifier.width(310.sdp())) {
                        OneScreen(
                            viewModel,
                            { navController.navigate(RegistrationScreen.Two.name) },
                            { authorizationScreen() },
                            faqScreen
                        )
                    }
                }
                composable(route = RegistrationScreen.Two.name) {
                    Box(modifier = Modifier.width(310.sdp())) {
                        CodeScreen(
                            {
                                viewModel.saveData()
                                navController.navigate(RegistrationScreen.Three.name)
                            },
                            { navController.navigate(RegistrationScreen.One.name) },
                            viewModel
                        )
                    }
                }
                composable(route = RegistrationScreen.Three.name) {
                    Box(modifier = Modifier.width(310.sdp())) {
                        ThreeScreen(
                            { navController.navigate(RegistrationScreen.Four.name) },
                            { navController.navigateUp() }
                        )
                    }
                }
                composable(route = RegistrationScreen.Four.name) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        FourScreen(
                            name,
                            mainScreen
                        )
                    }
                }
            }
        }
    }
}