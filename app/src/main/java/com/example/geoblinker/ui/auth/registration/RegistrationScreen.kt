package com.example.geoblinker.ui.auth.registration

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.geoblinker.ui.auth.CodeScreen
import com.example.geoblinker.ui.main.viewmodel.ProfileViewModel
import com.example.geoblinker.ui.theme.sdp

enum class RegistrationScreen {
    One,
    Two,
    Three,
    Four
}

@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel,
    profileViewModel: ProfileViewModel,
    navController: NavHostController = rememberNavController(),
    authorizationScreen: () -> Unit,
    mainScreen: () -> Unit
) {
    val name = viewModel.name

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
                            profileViewModel,
                            { navController.navigate(RegistrationScreen.Two.name) },
                            { authorizationScreen() }
                        )
                    }
                }
                composable(route = RegistrationScreen.Two.name) {
                    Box(modifier = Modifier.width(310.sdp())) {
                        CodeScreen(
                            {
                                profileViewModel.setName(name)
                                profileViewModel.setPhone(viewModel.phone)
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