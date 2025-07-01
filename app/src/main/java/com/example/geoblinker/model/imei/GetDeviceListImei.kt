package com.example.geoblinker.model.imei

import com.google.gson.annotations.SerializedName

data class GetDeviceListImei(
    @SerializedName("items")
    val items: List<DeviceImei>
)

data class DeviceImei(
    @SerializedName("sgid")
    val sgid: String,

    @SerializedName("imei")
    val imei: Long,

    @SerializedName("simei")
    val simei: String
)

data class GetDeviceListParamsImei(
    @SerializedName("familyid")
    val family: String
): ParamsImei