package com.example.geoblinker.data.techsupport

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "message_tech_support",
    foreignKeys = [
        ForeignKey(
            entity = ChatTechSupport::class,
            parentColumns = ["id"],
            childColumns = ["chatId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MessageTechSupport(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val chatId: Long,
    val content: String,
    val timeStamp: Long,
    val isMy: Boolean = true,
    val typeMessage: Type = Type.Text,
    val photoUri: String? = null
) {
    enum class Type {
        Text,
        Image,
        Video,
        Document
    }
}
