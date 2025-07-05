package com.example.geoblinker.model.imei

import com.google.gson.annotations.SerializedName

data class GetDetailImei(
    @SerializedName("last_pos")
    val posString: String?,

    @SerializedName("error_message")
    val error: String
)

data class PosData(
    @SerializedName("lat")
    val lat: Long,

    @SerializedName("lon")
    val lon: Long,
)

data class GetDetailParamsImei(
    @SerializedName("simei")
    val simei: String
): ParamsImei