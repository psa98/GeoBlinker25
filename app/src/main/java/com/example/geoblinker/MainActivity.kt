package com.example.geoblinker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.geoblinker.ui.GeoBlinkerViewModel
import com.example.geoblinker.ui.theme.GeoBlinkerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GeoBlinkerTheme {
                GeoBlinkerScreen(
                    application,
                    GeoBlinkerViewModel(application)
                )
            }
        }
    }
}