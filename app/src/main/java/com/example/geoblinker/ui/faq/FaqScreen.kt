package com.example.geoblinker.ui.faq

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.geoblinker.ui.BackButton

@Composable
fun FaqScreen(
    onBack: () -> Unit
) {
    val viewModel: FaqViewModel = viewModel()
    val faqContent by viewModel.faqContent.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFaqContent()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorMessage ?: "Произошла ошибка",
                        color = Color.Red
                    )
                }
            }
            faqContent != null -> {
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            webViewClient = WebViewClient()
                            settings.javaScriptEnabled = false
                            settings.loadWithOverviewMode = true
                            settings.useWideViewPort = true
                        }
                    },
                    update = { webView ->
                        val htmlContent = """
                            <html>
                            <head>
                                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                <style>
                                    body { 
                                        font-family: Arial, sans-serif; 
                                        padding: 20px; 
                                        margin: 0;
                                        line-height: 1.6;
                                    }
                                </style>
                            </head>
                            <body>
                                ${faqContent}
                            </body>
                            </html>
                        """.trimIndent()
                        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        BackButton(
            onClick = onBack,
            color = Color(0xFFEFEFEF)
        )
    }
}
