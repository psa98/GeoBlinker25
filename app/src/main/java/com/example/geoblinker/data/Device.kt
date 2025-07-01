package com.example.geoblinker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity(tableName = "devices")
data class Device(
    @PrimaryKey
    val imei: String,
    val id: String,
    val name: String,
    val isConnected: Boolean = true,
    val bindingTime: Long,
    val simei: String = "",
    val lat: Double = Random.nextDouble(-90.0, 90.0),
    val lng: Double = Random.nextDouble(-180.0, 180.0),
    val typeStatus: TypeStatus = TypeStatus.Available,
    val breakdownForecast: String? = null,
    val maintenanceRecommendations: String? = null
) {
    enum class TypeStatus {
        Available,
        Ready,
        RequiresRepair
    }
}
