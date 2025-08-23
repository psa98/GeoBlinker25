package com.example.geoblinker

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.geoblinker.data.AppDatabase
import com.example.geoblinker.data.techsupport.TechSupportRepository
import com.example.geoblinker.ui.auth.LoginViewModel
import com.example.geoblinker.ui.auth.authorization.AuthorizationScreen
import com.example.geoblinker.ui.auth.registration.RegistrationScreen
import com.example.geoblinker.ui.main.MainScreen
import com.example.geoblinker.ui.main.viewmodel.ChatsViewModel
import com.example.geoblinker.ui.main.viewmodel.JournalViewModel
import com.example.geoblinker.ui.main.viewmodel.NotificationViewModel
import com.example.geoblinker.ui.main.viewmodel.SubscriptionViewModel

enum class GeoBlinkerScreen {
    Authorization,
    Registration,
    Main
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun GeoBlinkerScreen(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel = LoginViewModel(application)
    val login = viewModel.login
    val startDestination = if (login) GeoBlinkerScreen.Main.name else GeoBlinkerScreen.Authorization.name

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = GeoBlinkerScreen.Authorization.name) {
                AuthorizationScreen(
                    registrationScreen = { navController.navigate(GeoBlinkerScreen.Registration.name) },
                    mainScreen = {
                        viewModel.setInitialSubscription()
                        viewModel.setIsLogin(true)
                        navController.navigate(GeoBlinkerScreen.Main.name)
                    }
                )
            }
            composable(route = GeoBlinkerScreen.Registration.name) {
                RegistrationScreen(
                    authorizationScreen = { navController.navigate(GeoBlinkerScreen.Authorization.name) },
                    mainScreen = {
                        viewModel.setInitialSubscription()
                        viewModel.setIsLogin(true)
                        navController.navigate(GeoBlinkerScreen.Main.name)
                    }
                )
            }
            composable(route = GeoBlinkerScreen.Main.name) {
                MainScreen(
                    SubscriptionViewModel(application),
                    JournalViewModel(application),
                    NotificationViewModel(application),
                    ChatsViewModel(
                        TechSupportRepository(
                            AppDatabase.getInstance(application).ChatTechSupportDao(),
                            AppDatabase.getInstance(application).MessageTechSupportDao()
                        ),
                        application
                    ),
                    toBeginning = { navController.navigate(GeoBlinkerScreen.Authorization.name) }
                )
            }
        }
    }
}