package com.example.geoblinker.ui.main.viewmodel

import androidx.annotation.DrawableRes

data class Subscription(
    val price: Double,
    val period: Int, // 1, 3, 6 or 12 months
    val labelPeriod: String, // "1 месяц"
    @DrawableRes val draw: Int
)
