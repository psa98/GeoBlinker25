package com.example.geoblinker.model

import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("u_name")
    val name: String? = null,

    @SerializedName("u_phone")
    val phone: String? = null,

    @SerializedName("u_email")
    val email: String? = null,

    @SerializedName("u_photo")
    val photo: String? = null
)
