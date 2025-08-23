package com.example.geoblinker.model

import com.google.gson.annotations.SerializedName

// Subscription API Models
data class SubscriptionRequest(
    @SerializedName("token")
    val token: String,
    @SerializedName("u_hash")
    val uHash: String,
    @SerializedName("data")
    val data: String // JSON string
)

data class SubscriptionData(
    @SerializedName("tariff")
    val tariff: String,
    @SerializedName("start_date")
    val startDate: Long? = null,
    @SerializedName("end_date")
    val endDate: Long? = null,
    @SerializedName("auto_renew")
    val autoRenew: Int = 0
)

data class SubscriptionResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("data")
    val data: SubscriptionResponseData
)

data class SubscriptionResponseData(
    @SerializedName("subs_id")
    val subsId: String
)

data class SubscriptionInfo(
    @SerializedName("subs_id")
    val subsId: String,
    @SerializedName("u_id")
    val uId: String,
    @SerializedName("tariff")
    val tariff: String,
    @SerializedName("start_date")
    val startDate: Long,
    @SerializedName("end_date")
    val endDate: Long,
    @SerializedName("cancellation_date")
    val cancellationDate: Long?,
    @SerializedName("subs_status")
    val subsStatus: String,
    @SerializedName("auto_renew")
    val autoRenew: Int,
    @SerializedName("p_id")
    val pId: List<String>,
    @SerializedName("paid")
    val paid: Boolean
)

data class SubscriptionListResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("data")
    val data: SubscriptionListData
)

data class SubscriptionListData(
    @SerializedName("subscription")
    val subscription: List<SubscriptionInfo>
)

// Payment API Models
data class PaymentRequest(
    @SerializedName("token")
    val token: String,
    @SerializedName("u_hash")
    val uHash: String,
    @SerializedName("data")
    val data: String, // JSON string
    @SerializedName("appUrl")
    val appUrl: String? = null
)

data class PaymentData(
    @SerializedName("sum")
    val sum: Int,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("payment_service")
    val paymentService: Int,
    @SerializedName("subs_id")
    val subsId: String? = null,
    @SerializedName("payment_way")
    val paymentWay: Int
)

data class PaymentResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("data")
    val data: PaymentResponseData
)

data class PaymentResponseData(
    @SerializedName("p_id")
    val pId: String,
    @SerializedName("confirmation_url")
    val confirmationUrl: String
)

data class PaymentInfo(
    @SerializedName("sum")
    val sum: String,
    @SerializedName("percent")
    val percent: String,
    @SerializedName("total_sum")
    val totalSum: String,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("payment_status")
    val paymentStatus: Int,
    @SerializedName("payment_service")
    val paymentService: Int,
    @SerializedName("from")
    val from: String,
    @SerializedName("to")
    val to: String,
    @SerializedName("timestamp_created")
    val timestampCreated: Long,
    @SerializedName("subs_id")
    val subsId: String?,
    @SerializedName("p_id_outer")
    val pIdOuter: String?
)

data class PaymentInfoResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("data")
    val data: PaymentInfoData
)

data class PaymentInfoData(
    @SerializedName("payment")
    val payment: List<PaymentInfo>
)

// Tariff Models
data class TariffResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("data")
    val data: TariffData
)

data class TariffData(
    @SerializedName("data")
    val data: TariffInfo
)

data class TariffInfo(
    @SerializedName("tariffs")
    val tariffs: Map<String, TariffItem>
)

data class TariffItem(
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("period")
    val period: Int
)
