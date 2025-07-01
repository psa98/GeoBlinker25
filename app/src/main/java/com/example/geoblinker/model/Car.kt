package com.example.geoblinker.model

import com.google.gson.annotations.SerializedName

data class Cars(
    @SerializedName("code")
    val code: String,

    @SerializedName("data")
    val data: ListCars
)

data class ListCars(
    @SerializedName("car")
    val cars: Map<String, Car>
)

data class Car(
    @SerializedName("c_id")
    val id: String = "",

    @SerializedName("registration_plate")
    val registrationPlate: String,

    @SerializedName("details")
    val details: Details
)

data class Details(
    @SerializedName("name")
    val name: String,

    @SerializedName("is_connected")
    val isConnected: Boolean = true,

    @SerializedName("binding_time")
    val bindingTime: Long
)

data class ResponseCreateCar(
    @SerializedName("code")
    val code: String,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val data: DataResponseCreateCar
)

data class DataResponseCreateCar(
    @SerializedName("created_car")
    val createdCar: IdDataResponseCreateCar
)

data class IdDataResponseCreateCar(
    @SerializedName("c_id")
    val cId: String
)