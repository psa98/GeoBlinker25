package com.example.geoblinker.network

import com.example.geoblinker.model.Authorization
import com.example.geoblinker.model.Code
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

private const val BASE_URL = "https://ibronevik.ru/taxi/api/v1/"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface RegistrationApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @FieldMap request: Map<String, String>
    ): Code

    @FormUrlEncoded
    @POST("auth")
    suspend fun auth(
        @FieldMap request: Map<String, String>
    ): Authorization
}

object RegistrationApi {
    val retrofitService: RegistrationApiService by lazy {
        retrofit.create(RegistrationApiService::class.java)
    }
}