package com.example.geoblinker.ui.main.profile.about

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.geoblinker.R
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.theme.sdp

@Composable
fun AboutCompanyItemScreen(
    aboutCompany: AboutCompany,
    toBack: () -> Unit
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                stringResource(R.string.about_company),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(Modifier.height(20.sdp()))
            Text(
                stringResource(aboutCompany.title),
                color = Color(0xFF747474),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(8.sdp()))
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.sdp(),
                color = Color(0xFFDAD9D9)
            )
            Spacer(Modifier.height(25.sdp()))
            Text(
                stringResource(aboutCompany.description),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }

    BackButton(
        onClick = toBack
    )
}