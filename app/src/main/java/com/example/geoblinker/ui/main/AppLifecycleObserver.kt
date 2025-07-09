package com.example.geoblinker.ui.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

object AppLifecycleObserver : DefaultLifecycleObserver {
    var isAppInForeground = mutableStateOf(false)

    override fun onStart(owner: LifecycleOwner) {
        isAppInForeground.value = true // Приложение на переднем плане
    }

    override fun onStop(owner: LifecycleOwner) {
        isAppInForeground.value = false // Приложение в фоне
    }
}