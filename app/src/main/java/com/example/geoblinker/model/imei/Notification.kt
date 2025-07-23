package com.example.geoblinker.model.imei

import com.google.gson.annotations.SerializedName

data class NotificationItem(
    @SerializedName("imei")
    val imei: Long,

    @SerializedName("time")
    val time: Long,

    @SerializedName("type")
    val type: String
)

data class NotificationList(
    @SerializedName("error_message")
    val message: String,

    @SerializedName("items")
    val items: List<NotificationItem>
)

data class NotificationParams(
    @SerializedName("end_time")
    val endTime: Long = System.currentTimeMillis() / 1000,

    @SerializedName("is_desc")
    val isDesc: Boolean = true,

    @SerializedName("is_summary")
    val isSummary: Boolean = false,

    @SerializedName("limit_size")
    val limitSize: Int = 100,

    @SerializedName("sfamilyid")
    val familyId: String,

    @SerializedName("simei")
    val simei: List<String>,

    @SerializedName("start_time")
    val startTime: Int = 1, // TODO: Для прода изменить значение на ~30-60 секунд

    @SerializedName("type")
    val type: List<String>
): ParamsImei