package com.example.geoblinker.model

import com.google.gson.annotations.SerializedName

data class Code(
    @SerializedName("code")
    val code: String,

    @SerializedName("message")
    val message: String? = null
)