package com.example.geoblinker.ui.main.profile.about

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.geoblinker.R
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.main.profile.techsupport.FrequentQuestions
import com.example.geoblinker.ui.theme.sdp

@Composable
fun AboutCompanyScreen(
    toDescriptionScreen: (AboutCompany) -> Unit,
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
            Spacer(Modifier.height(50.sdp()))
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.sdp(),
                color = Color(0xFFDAD9D9)
            )
        }
        items(AboutCompany.entries) { quest ->
            Text(
                stringResource(quest.title),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { toDescriptionScreen(quest) }
                    .padding(vertical = 15.sdp())
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.sdp(),
                color = Color(0xFFDAD9D9)
            )
        }
    }

    BackButton(
        onClick = toBack
    )
}