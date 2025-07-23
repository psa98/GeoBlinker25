package com.example.geoblinker.ui.main

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

object AppLifecycleObserver : DefaultLifecycleObserver {
    var isAppInForeground = mutableStateOf(false)

    override fun onStart(owner: LifecycleOwner) {
        Log.e("Foreground", "Start")
        isAppInForeground.value = true // Приложение на переднем плане
    }

    override fun onStop(owner: LifecycleOwner) {
        Log.e("Foreground", "Stop")
        isAppInForeground.value = false // Приложение в фоне
    }
}