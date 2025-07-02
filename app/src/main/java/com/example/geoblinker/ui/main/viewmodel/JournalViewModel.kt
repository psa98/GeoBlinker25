package com.example.geoblinker.ui.main.viewmodel

import android.app.Application
import android.content.ContentValues
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geoblinker.data.Signal
import com.example.geoblinker.model.Device
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

class JournalViewModel(
    private val application: Application
) : ViewModel() {

    // События для UI (успешное скачивание, ошибка)
    private val _downloadEvent = MutableSharedFlow<DownloadEvent>()
    val downloadEvent = _downloadEvent.asSharedFlow()

    fun downloadJournal(devices: List<Device>, signals: List<Signal>) {
        viewModelScope.launch {
            try {
                // Формирование CSV
                val csvContent = buildCsvContent(devices, signals)

                // Сохранение файла
                saveCsvFile(csvContent)

                // Уведомление об успехе
                _downloadEvent.emit(DownloadEvent.Success)
            } catch (e: Exception) {
                _downloadEvent.emit(DownloadEvent.Error(e.message ?: "Unknown error"))
            }
        }
    }

    private fun buildCsvContent(
        devices: List<Device>,
        signals: List<Signal>
    ): String {
        val csv = StringBuilder()

        // Заголовки
        csv.append("Device Name,Signal Name,DateTime\n")

        // Данные
        signals.sortedByDescending { it.dateTime }.forEach { signal ->
            val deviceName = devices.find { signal.deviceId == it.imei }?.name
            csv.append("${deviceName ?: ""},${signal.name},${signal.dateTime}\n")
        }

        return csv.toString()
    }

    private fun saveCsvFile(content: String) {
        // Создание имени файла
        val fileName = "journal_${System.currentTimeMillis()}.csv"

        // Для Android 10+ (API 29+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = application.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                ?: throw IOException("Failed to create file")

            resolver.openOutputStream(uri)?.use { stream ->
                stream.write(content.toByteArray())
            }

            File(application.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)
        } else {
            // Для старых версий Android
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, fileName)
            file.writeText(content)
        }
    }

    sealed class DownloadEvent {
        data object Success : DownloadEvent()
        data class Error(val message: String) : DownloadEvent()
    }
}