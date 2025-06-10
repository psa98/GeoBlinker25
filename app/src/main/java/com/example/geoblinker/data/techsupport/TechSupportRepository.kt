package com.example.geoblinker.data.techsupport

import kotlinx.coroutines.flow.Flow

class TechSupportRepository(
    private val chatsDao: ChatTechSupportDao,
    private val messageDao: MessageTechSupportDao
) {
    fun getAllChats(): Flow<List<ChatTechSupport>> = chatsDao.getAll()

    suspend fun insertMessage(message: MessageTechSupport) {
        messageDao.insert(message)
    }

    fun getMessagesChat(chatId: Long): Flow<List<MessageTechSupport>> = messageDao.getMessagesChat(chatId)

    suspend fun insertRequest(chat: ChatTechSupport, message: MessageTechSupport) {
        val chatId = chatsDao.insert(chat)
        messageDao.insert(message.copy(chatId = chatId))
    }
}