package com.example.geoblinker.ui.main.profile.techsupport

import androidx.annotation.StringRes
import com.example.geoblinker.R
import com.example.geoblinker.ui.main.GeoBlinker

enum class FrequentQuestions(
    @StringRes val title: Int,
    val langKey:String,
    //@StringRes val description: Int = R.string.description_pass

) {
    CantLinkDevice(R.string.cant_link_device,"e_faq_1"),
    CantPaySubscription(R.string.cant_pay_subscription,"e_faq_2"),
    NoSignalsComingFromDevice(R.string.no_signals_coming_from_device,"e_faq_3"),
    CantChangePhone(R.string.cant_change_phone,"e_faq_4"),
    CantLinkEmail(R.string.cant_link_email,"e_faq_5");
    companion object{
        fun getScreenName (type: FrequentQuestions):String =  GeoBlinker.langData.getNameForFaq(type.langKey)
    }


}

