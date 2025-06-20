package com.example.geoblinker.model

import com.google.gson.annotations.SerializedName

data class Authorization(
    @SerializedName("code")
    val code: String,

    @SerializedName("auth_user")
    val user: User? = null,

    @SerializedName("auth_hash")
    val hash: String? = null
)

data class User(
    @SerializedName("u_name")
    val name: String,

    @SerializedName("u_email")
    val email: String?,

    @SerializedName("u_photo")
    val photo: String
)

data class Token(
    @SerializedName("data")
    val data: DataToken
)

data class DataToken(
    @SerializedName("token")
    val token: String,

    @SerializedName("u_hash")
    val hash: String
)