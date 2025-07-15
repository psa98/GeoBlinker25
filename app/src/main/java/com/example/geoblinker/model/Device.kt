package com.example.geoblinker.model

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
    val speed: Double = 0.0,
    val lat: Double = -999999999.9,
    val lng: Double = -999999999.9,
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
