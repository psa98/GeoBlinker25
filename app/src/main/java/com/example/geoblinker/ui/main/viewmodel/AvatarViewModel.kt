package com.example.geoblinker.ui.main.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geoblinker.model.Profile
import com.example.geoblinker.network.ProfileApi
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class AvatarViewModel(
    private val application: Application
): ViewModel() {
    private val _prefs = application.getSharedPreferences("avatar_prefs", Context.MODE_PRIVATE)
    private val _profilePrefs = application.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
    private var _token by mutableStateOf("")
    private var _hash by mutableStateOf("")
    var avatarUri: Uri by mutableStateOf(Uri.parse(""))
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadSavedAvatar()
    }

    private fun loadSavedAvatar() {
        viewModelScope.launch {
            avatarUri = Uri.parse(_prefs.getString("avatar_uri", ""))
            _token = _profilePrefs.getString("token", null) ?: ""
            _hash = _profilePrefs.getString("hash", null) ?: ""
        }
    }

    fun handleSelectedImage(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (!isValidImageFormat(uri)) {
                    withContext(Dispatchers.Main) {
                        errorMessage = "Только JPG/PNG форматы"
                    }
                    return@launch
                }

                if (!isValidImageSize(uri, maxSizeMb = 5)) {
                    withContext(Dispatchers.Main) {
                        errorMessage = "Файл слишком большой (макс. 5 МБ)"
                    }
                    return@launch
                }

                saveAvatar(uri)
                withContext(Dispatchers.Main) {
                    errorMessage = null
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    errorMessage = "Ошибка загрузки: ${e.message}"
                }
            }
        }
    }

    fun updateErrorMessage(text: String) {
        errorMessage = text
    }

    private fun isValidImageFormat(uri: Uri): Boolean {
        val mimeType = application.contentResolver.getType(uri) ?: return false
        return mimeType in listOf("image/jpeg", "image/png", "image/jpg")
    }

    private fun isValidImageSize(uri: Uri, maxSizeMb: Int): Boolean {
        val sizeBytes = application.contentResolver.openAssetFileDescriptor(uri, "r")?.use {
            it.length
        } ?: 0

        return sizeBytes <= maxSizeMb * 1024 * 1024
    }

    private suspend fun saveAvatar(sourceUri: Uri) {
        val fileName = "avatar_${System.currentTimeMillis()}.jpg"
        val outputFile = File(application.filesDir, fileName)

        application.contentResolver.openInputStream(sourceUri)?.use { input ->
            FileOutputStream(outputFile).use { output ->
                input.copyTo(output)
            }
        }

        val newUri = Uri.fromFile(outputFile)
        saveAvatarUriToPrefs(newUri)
        avatarUri = newUri

        val base64Photo = "data:image/png;base64," + Base64.encodeToString(outputFile.readBytes(), Base64.DEFAULT)
        val res = ProfileApi.retrofitService.edit(
            mapOf(
                "token" to _token,
                "u_hash" to _hash,
                "data" to Gson().toJson(
                    Profile(
                        photo = base64Photo
                    )
                )
            )
        )
        Log.d("ChangePhoto", "Code: ${res.code}, message: ${res.message}")
    }

    private fun saveAvatarUriToPrefs(uri: Uri) {
        _prefs
            .edit()
            .putString("avatar_uri", uri.toString())
            .apply()
    }

    fun removeAvatar(onServer: Boolean = false) {
        viewModelScope.launch {
            try {
                deleteAvatarFile()
                clearAvatarPrefs()
                avatarUri = Uri.parse("")
                errorMessage = null

                if (onServer) {
                    ProfileApi.retrofitService.edit(
                        mapOf(
                            "token" to _token,
                            "u_hash" to _hash,
                            "data" to Gson().toJson(
                                Profile(
                                    photo = ""
                                )
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    errorMessage = "Ошибка удаления: ${e.message}"
                }
            }
        }
    }

    private fun deleteAvatarFile() {
        val uriString = _prefs.getString("avatar_uri", null)
        uriString?.let {
            val uri = Uri.parse(it)
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