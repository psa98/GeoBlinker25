package com.example.geoblinker.network

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

interface ProfileApiService {
    @FormUrlEncoded
    @POST("user")
    suspend fun edit(
        @FieldMap request: Map<String, String>
    ): Code
}

object ProfileApi {
    val retrofitService: ProfileApiService by lazy {
        retrofit.create(ProfileApiService::class.java)
    }
}