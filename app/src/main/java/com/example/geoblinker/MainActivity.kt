package com.example.geoblinker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
        
        // Handle payment deep links
        handlePaymentDeepLink(intent)
        
        enableEdgeToEdge()
        setContent {
            GeoBlinkerTheme {
                GeoBlinkerScreen()
            }
        }
    }
    
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handlePaymentDeepLink(intent)
    }
    
    private fun handlePaymentDeepLink(intent: Intent?) {
        val data: Uri? = intent?.data
        if (data != null && data.scheme == "geoblinker" && data.host == "payment") {
            when (data.path) {
                "/success" -> {
                    Log.d("PaymentCallback", "Payment successful")
                    // Show success message or navigate to success screen
                }
                "/canceled" -> {
                    Log.d("PaymentCallback", "Payment canceled")
                    // Show canceled message
                }
            }
        }
    }
}