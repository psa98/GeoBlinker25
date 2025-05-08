package com.example.geoblinker.ui.main

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

enum class MainScreen {
    One
}

@Composable
fun MainScreen(
    application: Application,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = MainScreen.One.name
    ) {
        composable(route = MainScreen.One.name) {
            OneScreen()
        }
    }
}