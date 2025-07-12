package com.example.geoblinker.ui.main.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geoblinker.R
import com.example.geoblinker.data.techsupport.ChatTechSupport
import com.example.geoblinker.data.techsupport.MessageTechSupport
import com.example.geoblinker.data.techsupport.TechSupportRepository
import com.example.geoblinker.network.Api
import com.example.geoblinker.ui.main.profile.techsupport.MediaItem
import com.example.geoblinker.ui.main.profile.techsupport.MediaType
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.threeten.bp.Instant
import java.io.File
import java.io.FileOutputStream

private val DEFAULT_CHAT = ChatTechSupport(title = "", lastMessageTime = 0, lastChecked = 0)

class ChatsViewModel(
    private val repository: TechSupportRepository,
    private val application: Application
): ViewModel() {
    private val crashlytics = Firebase.crashlytics
    private val _chats = MutableStateFlow<List<ChatTechSupport>>(emptyList())
    private val _selectedChat = MutableStateFlow(DEFAULT_CHAT)
    private val _messages = MutableStateFlow<List<MessageTechSupport>>(emptyList())
    val chats = _chats.asStateFlow()
    val selectedChat = _selectedChat.asStateFlow()
    val messages = _messages.asStateFlow()
    var uiState: MutableState<DefaultStates> = mutableStateOf(DefaultStates.Input)
        private set

    init {
        viewModelScope.launch {
            launch {
                repository.getAllChats().collect {
                    _chats.value = it
                }
            }
        }
    }

    fun resetUiState() {
        uiState.value = DefaultStates.Input
    }

    fun addRequest(
        theme: String,
        content: String
    ) {
        viewModelScope.launch {
            try {
                val res = Api.retrofitService.sendEmailTechSupport(
                    mapOf(
                        "subject" to theme,
                        "body" to content
                    )
                )
                if (res.code != "200")
                    throw Exception("Code: ${res.code}, message: ${res.message}")
            } catch (e: Exception) {
                Log.e("addRequest", e.toString())
                crashlytics.log(e.toString())
                crashlytics.recordException(e)
                uiState.value = DefaultStates.Error(R.string.server_error)
                return@launch
            }
            uiState.value = DefaultStates.Success
        }
    }

    fun setSelectedChat(chatId: Long) {
        viewModelScope.launch {
            _selectedChat.value = _chats.value.find { it.id == chatId } ?: DEFAULT_CHAT
            repository.getMessagesChat(chatId).collect {
                _messages.value = it
            }
        }
    }

    fun countNotCheckedMessages(chat: ChatTechSupport): Int {
        var ans = 0
        viewModelScope.launch {
            var messages = emptyList<MessageTechSupport>()
            repository.getMessagesChat(chat.id).collect {
                messages = it
            }
            ans = messages.count { it.timeStamp > chat.lastChecked }
        }
        return ans
    }

    fun addMessage(text: String, mediaItems: List<MediaItem>) {
        viewModelScope.launch {
            var time = Instant.now().toEpochMilli()
            mediaItems.forEach { item ->
                when(item.type) {
                    MediaType.IMAGE -> repository.insertMessage(
                        MessageTechSupport(
                            chatId = _selectedChat.value.id,
                            content = "",
                            timeStamp = time++,
                            typeMessage = MessageTechSupport.Type.Image,
                            photoUri = saveImage(item.uri).toString(),
                        )
                    )
                    MediaType.VIDEO -> repository.insertMessage(
                        MessageTechSupport(
                            chatId = _selectedChat.value.id,
                            content = "",
                            timeStamp = time++,
                            typeMessage = MessageTechSupport.Type.Video,
                            photoUri = saveVideo(item.uri).toString()
                        )
                    )
                    MediaType.DOCUMENT -> repository.insertMessage(
                        MessageTechSupport(
                            chatId = _selectedChat.value.id,
                            content = "",
                            timeStamp = time++,
                            typeMessage = MessageTechSupport.Type.Document,
                            photoUri = saveDocument(item.uri, item.name).toString()
                        )
                    )
                }
            }
            if (text.isNotEmpty()) {
                repository.insertMessage(
                    MessageTechSupport(
                        chatId = _selectedChat.value.id,
                        content = text,
                        timeStamp = time
                    )
                )
            }
        }
    }

    private fun saveImage(uri: Uri): Uri {
        val fileName = "image_${System.currentTimeMillis()}.jpg"
        val outputFile = File(application.filesDir, fileName)

        application.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(outputFile).use { output ->
                input.copyTo(output)
            }
        }

        return Uri.fromFile(outputFile)
    }

    private fun saveVideo(uri: Uri): Uri {
        val fileName = "video_${System.currentTimeMillis()}.mp4"
        val outputFile = File(application.filesDir, fileName)

        application.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(outputFile).use { output ->
                val buffer = ByteArray(4 * 1024) // 4KB buffer
                var bytesRead: Int
                while (input.read(buffer).also { bytesRead = it } != -1) {
                    output.write(buffer, 0, bytesRead)
                }
            }
        }

        return Uri.fromFile(outputFile)
    }

    private fun saveDocument(uri: Uri, fileName: String): Uri {
        val outputFile = File(application.filesDir, fileName)

        application.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(outputFile).use { output ->
                input.copyTo(output)
            }
        }

        return Uri.fromFile(outputFile)
    }
}