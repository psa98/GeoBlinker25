package com.example.geoblinker.model

import com.google.gson.annotations.SerializedName

data class Authorization(
    @SerializedName("code")
    val code: String,

    @SerializedName("auth_user")
    val user: User? = null
)

data class User(
    @SerializedName("u_name")
    val name: String,

    @SerializedName("u_email")
    val email: String?
)