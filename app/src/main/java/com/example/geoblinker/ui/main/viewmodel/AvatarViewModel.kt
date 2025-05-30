package com.example.geoblinker.ui.main.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class AvatarViewModel(context: Context): ViewModel() {
    @SuppressLint("StaticFieldLeak")
    private val _context = context
    private val _prefs = context.getSharedPreferences("avatar_prefs", Context.MODE_PRIVATE)
    private val _avatarUri = MutableStateFlow<Uri?>(null)
    private val _errorMessage = MutableStateFlow<String?>(null)
    val avatarUri: StateFlow<Uri?> = _avatarUri.asStateFlow()
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadSavedAvatar()
    }

    private fun loadSavedAvatar() {
        viewModelScope.launch {
            // Используем Dispatchers.IO для работы с файлами
            withContext(Dispatchers.IO) {
                val uriString = _prefs.getString("avatar_uri", null)

                // Обновляем значение в основном потоке
                withContext(Dispatchers.Main) {
                    _avatarUri.value = uriString?.let { Uri.parse(it) }
                }
            }
        }
    }

    fun handleSelectedImage(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (!isValidImageFormat(uri)) {
                    withContext(Dispatchers.Main) {
                        _errorMessage.value = "Только JPG/PNG форматы"
                    }
                    return@launch
                }

                if (!isValidImageSize(uri, maxSizeMb = 5)) {
                    withContext(Dispatchers.Main) {
                        _errorMessage.value = "Файл слишком большой (макс. 5 МБ)"
                    }
                    return@launch
                }

                saveAvatar(uri)
                withContext(Dispatchers.Main) {
                    _errorMessage.value = null
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = "Ошибка загрузки: ${e.message}"
                }
            }
        }
    }

    fun setErrorMessage(text: String) {
        _errorMessage.value = text
    }

    private fun isValidImageFormat(uri: Uri): Boolean {
        val mimeType = _context.contentResolver.getType(uri) ?: return false
        return mimeType in listOf("image/jpeg", "image/png", "image/jpg")
    }

    private fun isValidImageSize(uri: Uri, maxSizeMb: Int): Boolean {
        val sizeBytes = _context.contentResolver.openAssetFileDescriptor(uri, "r")?.use {
            it.length
        } ?: 0

        return sizeBytes <= maxSizeMb * 1024 * 1024
    }

    private fun saveAvatar(sourceUri: Uri) {
        val fileName = "avatar_${System.currentTimeMillis()}.jpg"
        val outputFile = File(_context.filesDir, fileName)

        _context.contentResolver.openInputStream(sourceUri)?.use { input ->
            FileOutputStream(outputFile).use { output ->
                input.copyTo(output)
            }
        }

        val newUri = Uri.fromFile(outputFile)

        // Сохраняем URI в SharedPreferences
        saveAvatarUriToPrefs(newUri)

        // Обновляем состояние в основном потоке
        _avatarUri.value = newUri
    }

    private fun saveAvatarUriToPrefs(uri: Uri) {
        _prefs
            .edit()
            .putString("avatar_uri", uri.toString())
            .apply()
    }

    fun removeAvatar() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 1. Удаляем файл аватарки
                deleteAvatarFile()

                // 2. Удаляем URI из SharedPreferences
                clearAvatarPrefs()

                // 3. Обновляем состояние UI
                withContext(Dispatchers.Main) {
                    _avatarUri.value = null
                    _errorMessage.value = null
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = "Ошибка удаления: ${e.message}"
                }
            }
        }
    }

    private fun deleteAvatarFile() {
        val uriString = _prefs.getString("avatar_uri", null)
        uriString?.let { uriString ->
            val uri = Uri.parse(uriString)
            uri.path?.let { filePath ->
                File(filePath).delete()
            }
        }
    }

    private fun clearAvatarPrefs() {
        _prefs
            .edit()
            .remove("avatar_uri")
            .apply()
    }
}