package com.example.geoblinker.network

import com.example.geoblinker.model.imei.AddImei
import com.example.geoblinker.model.imei.GetDetailImei
import com.example.geoblinker.model.imei.GetDeviceListImei
import com.example.geoblinker.model.imei.LoginImei
import com.example.geoblinker.model.imei.RequestImei
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

private const val BASE_URL = "https://www.gps666.net/"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface ApiServiceImei {
    @POST("mapi")
    suspend fun login(
        @Body request: RequestImei
    ): LoginImei

    @POST("mapi")
    suspend fun getDeviceList(
        @Query("sid") sid: String,
        @Body request: RequestImei
    ): GetDeviceListImei

    @POST("mapi")
    suspend fun add(
        @Query("sid") sid: String,
        @Body request: RequestImei
    ): AddImei

    @POST("mapi")
    suspend fun getDetail(
        @Query("sid") sid: String,
        @Body request: RequestImei
    ): GetDetailImei
}

object ApiImei {
    val retrofitService: ApiServiceImei by lazy {
        retrofit.create(ApiServiceImei::class.java)
    }
}