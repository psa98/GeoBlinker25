package com.example.geoblinker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devices")
data class Device(
    @PrimaryKey
    val imei: String,
    val name: String,
    val isConnected: Boolean = true,
    val bindingTime: Long
)
