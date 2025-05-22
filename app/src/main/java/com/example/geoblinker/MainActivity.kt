package com.example.geoblinker

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.geoblinker.data.AppDatabase
import com.example.geoblinker.data.Repository
import com.example.geoblinker.ui.GeoBlinkerViewModel
import com.example.geoblinker.ui.theme.GeoBlinkerTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics

class MainActivity : ComponentActivity() {
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        // Включение Crashlytics (можно отключить для debug)
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true

        // Временное отключение всех флагов
        window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)

        TimeUtils.init(this)
        
        enableEdgeToEdge()
        setContent {
            GeoBlinkerTheme {
                GeoBlinkerScreen(
                    Repository(
                        AppDatabase.getInstance(application).deviceDao(),
                        AppDatabase.getInstance(application).typeSignalDao(),
                        AppDatabase.getInstance(application).signalDao(),
                        AppDatabase.getInstance(application).newsDao()
                    ),
                    GeoBlinkerViewModel()
                )
            }
        }
    }
}