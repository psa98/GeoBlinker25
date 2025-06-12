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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.geoblinker.ui.authorization.FourScreen
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
                        profileViewModel,
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
                        {
                            profileViewModel.setName(name)
                            profileViewModel.setPhone(viewModel.phone.value)
                            navController.navigate(RegistrationScreen.Three.name)
                        },
                        { navController.navigate(RegistrationScreen.One.name) },
                        viewModel
                    )
                }
                composable(route = RegistrationScreen.Three.name) {
                    ThreeScreen(
                        { navController.navigate(RegistrationScreen.Four.name) },
                        { navController.navigateUp() }
                    )
                }
                composable(route = RegistrationScreen.Four.name) {
                    FourScreen(
                        name,
                        mainScreen
                    )
                }
            }
        }
    }
}