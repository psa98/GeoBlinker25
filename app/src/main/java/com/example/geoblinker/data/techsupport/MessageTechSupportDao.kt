package com.example.geoblinker.data.techsupport

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageTechSupportDao {
    @Insert
    suspend fun insert(message: MessageTechSupport)

    @Query("SELECT * FROM message_tech_support WHERE chatId = :chatId")
    fun getMessagesChat(chatId: Long): Flow<List<MessageTechSupport>>
}