package com.example.geoblinker.ui.main

import android.webkit.JavascriptInterface

class WebAppInterface(
    private val onMarkerClick: (String) -> Unit
) {
    @JavascriptInterface
    fun onMarkerClicked(markerId: String) {
        onMarkerClick(markerId)
    }
}