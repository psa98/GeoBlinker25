package com.example.geoblinker.model

import kotlin.random.Random

data class Device(
    val imei: String,
    val id: String,
    val name: String,
    val isConnected: Boolean = true,
    val bindingTime: Long,
    val simei: String = "",
    val registrationPlate: String,
    val modelName: String = "",
    val powerRate: Int = 0,
    val signalRate: Int = 0,
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
