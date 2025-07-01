package com.example.geoblinker.model.imei

import com.google.gson.annotations.SerializedName

private const val LOGIN_IMEI = "georule"
private const val PASSWORD_IMEI = "8bbe1a8ed834b27261f2a4dfb1418ae7"

data class LoginImei(
    @SerializedName("sid")
    val sid: String,

    @SerializedName("familys")
    val family: List<Map<String, Any>>
)

data class LoginParamsImei(
    @SerializedName("account")
    val account: String = LOGIN_IMEI,

    @SerializedName("platform")
    val platform: String = "web",

    @SerializedName("pwd_md5")
    val password: String = PASSWORD_IMEI,

    @SerializedName("type")
    val type: Int = 58
): ParamsImei