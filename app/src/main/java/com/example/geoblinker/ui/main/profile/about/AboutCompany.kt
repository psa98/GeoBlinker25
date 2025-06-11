package com.example.geoblinker.ui.main.profile.about

import androidx.annotation.StringRes
import com.example.geoblinker.R

enum class AboutCompany(
    @StringRes val title: Int,
    @StringRes val description: Int = R.string.description_pass
) {
    PublicOffer(R.string.public_offer),
    PrivacyPolicy(R.string.privacy_policy)
}