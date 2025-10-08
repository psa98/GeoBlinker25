package com.example.geoblinker.ui.main.profile.about

import androidx.annotation.StringRes
import com.example.geoblinker.R
import com.example.geoblinker.ui.main.GeoBlinker

enum class AboutCompany(
    @StringRes val title: Int,
    @StringRes val description: Int = R.string.description_pass
) {
    PublicOffer(R.string.public_offer),
    PrivacyPolicy(R.string.privacy_policy);


}

fun AboutCompany.getText() = when (this){
    AboutCompany.PublicOffer -> GeoBlinker.langData.offerText
    AboutCompany.PrivacyPolicy -> GeoBlinker.langData.policyText
}

fun AboutCompany.getTitle() = when (this){
    AboutCompany.PublicOffer -> GeoBlinker.langData.offerTitle
    AboutCompany.PrivacyPolicy -> GeoBlinker.langData.policyTitle
}