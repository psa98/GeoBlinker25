package com.example.geoblinker.model.imei

import com.google.gson.annotations.SerializedName

data class TrajectoryImeiItem(
    @SerializedName("lat")
    val lat: Double,

    @SerializedName("lon")
    val lng: Double,

    @SerializedName("speed")
    val speed: Double,

    @SerializedName("time")
    val time: Long
)

data class TrajectoryImei(
    @SerializedName("errcode")
    val code: Int,

    @SerializedName("error_mesage")
    val message: String,

    @SerializedName("data")
    val data: List<TrajectoryImeiItem>
)

data class ParamsTrajectoryImei(
    @SerializedName("limit_size")
    val limitSize: Int = 2000,

    @SerializedName("simei")
    val simei: String,

    @SerializedName("time_begin")
    val timeBegin: Long = 1,

    @SerializedName("time_end")
    val timeEnd: Long = System.currentTimeMillis() / 1000
): ParamsImei