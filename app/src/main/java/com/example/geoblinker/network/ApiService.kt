package com.example.geoblinker.network

import com.example.geoblinker.model.Authorization
import com.example.geoblinker.model.Cars
import com.example.geoblinker.model.Code
import com.example.geoblinker.model.LangResponse
import com.example.geoblinker.model.LanguageResponse
import com.example.geoblinker.model.PaymentInfoResponse
import com.example.geoblinker.model.PaymentResponse
import com.example.geoblinker.model.ResponseCreateCar
import com.example.geoblinker.model.SubscriptionListResponse
import com.example.geoblinker.model.SubscriptionResponse
import com.example.geoblinker.model.TariffResponse
import com.example.geoblinker.model.TariffResponseMap
import com.example.geoblinker.model.Token
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


private const val BASE_URL = "https://ibronevik.ru/taxi/c/0/api/v1/"




private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()


interface ApiService {
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

    @FormUrlEncoded
    @POST("token/authorized")
    suspend fun getToken(
        @Field("auth_hash") hash: String
    ): Token

    @GET("token")
    suspend fun getHash(
        @Query("token") token: String
    ): Token

    @FormUrlEncoded
    @POST("user")
    suspend fun edit(
        @FieldMap request: Map<String, String>
    ): Code

    @FormUrlEncoded
    @POST("car")
    suspend fun addCar(
        @FieldMap request: Map<String, String>
    ): ResponseCreateCar

    @FormUrlEncoded
    @POST("user/authorized/car")
    suspend fun getAllCar(
        @FieldMap request: Map<String, String>
    ): Cars

    @FormUrlEncoded
    @POST("car/{c_id}")
    suspend fun updateCar(
        @Path("c_id") cId: String,
        @FieldMap request: Map<String, String>
    ): Code

    @FormUrlEncoded
    @POST("mail/1/send/")
    suspend fun sendEmailTechSupport(
        @FieldMap request: Map<String, String>
    ): Code

    // Subscription API endpoints
    @FormUrlEncoded
    @POST("subscription/create")
    suspend fun createSubscription(
        @FieldMap request: Map<String, String>
    ): SubscriptionResponse

    @FormUrlEncoded
    @POST("subscription/get")
    suspend fun getSubscription(
        @FieldMap request: Map<String, String>
    ): SubscriptionListResponse

    // Payment API endpoints  
    @FormUrlEncoded
    @POST("payment/create")
    suspend fun createPayment(
        @FieldMap request: Map<String, String>
    ): PaymentResponse

    @FormUrlEncoded
    @POST("payment/get")
    suspend fun getPayment(
        @FieldMap request: Map<String, String>
    ): PaymentInfoResponse

    // Get tariffs
    @GET("data")
    suspend fun getTariffs(): TariffResponseMap

    // Get language translations
    @GET("data")
    suspend fun getData(
        @Query("data.lang_vls") langId: String
    ): LangResponse
    // теперь возвращается другой тип
}

object Api {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}