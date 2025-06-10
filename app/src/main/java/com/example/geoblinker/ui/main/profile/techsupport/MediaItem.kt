package com.example.geoblinker.ui.main.profile.techsupport

import android.net.Uri

data class MediaItem(
    val uri: Uri,
    val type: MediaType,
    val duration: Long = 0 // Для видео
)
