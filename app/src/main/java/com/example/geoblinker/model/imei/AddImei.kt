package com.example.geoblinker.model.imei

import com.google.gson.annotations.SerializedName

data class AddImei(
    @SerializedName("suc_items")
    val items: List<Imei>
)

data class AddParamsImei(
    @SerializedName("info")
    val info: List<Map<String, Long>>,

    @SerializedName("sgid")
    val sgid: String
): ParamsImei

data class Imei(
    @SerializedName("simei")
    val simei: String
)