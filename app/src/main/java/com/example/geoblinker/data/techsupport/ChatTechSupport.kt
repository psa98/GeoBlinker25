package com.example.geoblinker.data.techsupport

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_tech_support")
data class ChatTechSupport(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val lastMessageTime: Long,
    val lastChecked: Long,
    val decided: Boolean = false
)