package com.example.geoblinker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class News(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val description: String,
    val dateTime: Long,
    val isSeen: Boolean = false
)
