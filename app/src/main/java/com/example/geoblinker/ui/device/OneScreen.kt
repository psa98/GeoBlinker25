package com.example.geoblinker.ui.device

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import com.example.geoblinker.R
import com.example.geoblinker.TimeUtils
import com.example.geoblinker.data.Device
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.BlackMediumButton
import com.example.geoblinker.ui.GreenMediumButton
import com.example.geoblinker.ui.theme.sdp

@Composable
fun OneScreen(
    device: Device,
    toBindingScreen: () -> Unit,
    toBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.new_device),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.width(15.sdp()))
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.check),
                contentDescription = null,
                modifier = Modifier.size(28.sdp()),
                tint = Color.Unspecified
            )
        }
        Spacer(Modifier.height(24.sdp()))
        Surface(
            shape = RoundedCornerShape(10.sdp()),
            color = Color.White
        ) {
            Column {
                Row(
                    Modifier.padding(15.sdp()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.size(9.sdp()).background(Color(0xFF12CD4A), MaterialTheme.shapes.small))
                    Spacer(Modifier.width(11.sdp()))
                    Text(
                        device.name,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                HorizontalDivider(
                    Modifier.fillMaxWidth().padding(horizontal = 15.sdp()),
                    1.sdp(),
                    Color(0xFFDAD9D9).copy(alpha = 0.5f)
                )
                Text(
                    "Tracker ULTRA 3",
                    modifier = Modifier.padding(15.sdp()),
                    style = MaterialTheme.typography.labelMedium
                )
                HorizontalDivider(
                    Modifier.fillMaxWidth().padding(horizontal = 15.sdp()),
                    1.sdp(),
                    Color(0xFFDAD9D9).copy(alpha = 0.5f)
                )
                Text(
                    "IMEI: ${device.imei}",
                    modifier = Modifier.padding(15.sdp()),
                    style = MaterialTheme.typography.labelMedium
                )
                HorizontalDivider(
                    Modifier.fillMaxWidth().padding(horizontal = 15.sdp()),
                    1.sdp(),
                    Color(0xFFDAD9D9).copy(alpha = 0.5f)
                )
                Text(
                    "Привязано: ${TimeUtils.formatToLocalTime(device.bindingTime)}",
                    modifier = Modifier.padding(15.sdp()),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
        Spacer(Modifier.height(15.sdp()))
        BlackMediumButton(
            icon = R.drawable.bell,
            text = stringResource(R.string.configure_the_signals),
            onClick = {}
        )
        Spacer(Modifier.height(15.sdp()))
        BlackMediumButton(
            icon = R.drawable.gps_navigation,
            text = stringResource(R.string.view_on_the_map),
            onClick = {}
        )
        Spacer(Modifier.height(15.sdp()))
        GreenMediumButton(
            icon = R.drawable.plus,
            text = stringResource(R.string.link_device),
            onClick = toBindingScreen
        )
        Spacer(Modifier.height(64.sdp()))
    }

    BackButton(
        onClick = toBack
    )
}