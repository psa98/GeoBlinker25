package com.example.geoblinker.ui.main

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner

class GeoBlinker : Application() {
    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleObserver)
    }
}