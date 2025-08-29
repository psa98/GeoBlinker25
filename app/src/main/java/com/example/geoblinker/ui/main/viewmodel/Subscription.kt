package com.example.geoblinker.ui.main.viewmodel

import android.icu.util.Currency
import androidx.annotation.DrawableRes

data class Subscription(
    val price: Double,
    val period: Int, // 1, 3, 6 or 12 months
    val labelPeriod: String, // "1 месяц"
    val currencyName:String,
    val currencyCode:String,
    @DrawableRes val draw: Int
)
