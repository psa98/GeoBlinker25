package com.example.geoblinker.ui.main.profile.techsupport

import androidx.annotation.StringRes
import com.example.geoblinker.R

enum class FrequentQuestions(
    @StringRes val title: Int,
    @StringRes val description: Int = R.string.description_pass
) {
    CantLinkDevice(R.string.cant_link_device),
    CantPaySubscription(R.string.cant_pay_subscription),
    NoSignalsComingFromDevice(R.string.no_signals_coming_from_device),
    CantChangePhone(R.string.cant_change_phone),
    CantLinkEmail(R.string.cant_link_email)
}