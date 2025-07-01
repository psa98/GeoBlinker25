package com.example.geoblinker.model.imei

import com.google.gson.annotations.SerializedName

data class RequestImei(
    @SerializedName("module")
    val module: String,

    @SerializedName("func")
    val func: String,

    @SerializedName("params")
    val params: ParamsImei
)