package com.example.geoblinker.ui.main.device.detach

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.geoblinker.R
import com.example.geoblinker.ui.OkButton
import com.example.geoblinker.ui.theme.sdp

@Composable
fun DeviceDetachTwoScreen(
    name: String,
    imei: String,
    toMap: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(147.sdp()))
        Text(
            "“$name”",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(10.sdp()))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "IMEI: ",
                color = Color(0xFF737373),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                imei,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(Modifier.height(58.sdp()))
        Text(
            stringResource(R.string.untied),
            color = Color(0xFFC4162D),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(220.sdp()))
        OkButton(
            enabled = true,
            onClick = toMap
        )
    }
}