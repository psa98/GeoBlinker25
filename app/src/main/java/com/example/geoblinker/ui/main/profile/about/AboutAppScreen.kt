package com.example.geoblinker.ui.main.profile.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.geoblinker.R
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.theme.sdp

@Composable
fun AboutAppScreen(
    toBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.about_app),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(Modifier.height(98.sdp()))
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.title_logo),
            contentDescription = null,
            modifier = Modifier.size(200.sdp(), 135.sdp()),
            tint = Color.Unspecified
        )
        Spacer(Modifier.height(15.sdp()))
        Text(
            stringResource(R.string.version),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(79.sdp()))
        Text(
            "Разработано командой Х",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
    }

    BackButton(
        onClick = toBack
    )
}