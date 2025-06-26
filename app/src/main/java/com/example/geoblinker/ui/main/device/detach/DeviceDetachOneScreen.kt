package com.example.geoblinker.ui.main.device.detach

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.geoblinker.R
import com.example.geoblinker.ui.CustomButton
import com.example.geoblinker.ui.TypeColor
import com.example.geoblinker.ui.main.viewmodel.DeviceViewModel
import com.example.geoblinker.ui.theme.sdp

@Composable
fun DeviceDetachOneScreen(
    viewModel: DeviceViewModel,
    toTwo: (String, String) -> Unit,
    toBack: () -> Unit
) {
    val device by viewModel.device.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (device.name.isNotEmpty()) {
            Text(
                device.name,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        } else {
            Text(
                stringResource(R.string.an_unnamed_device),
                color = Color(0xFF737373),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Spacer(Modifier.height(10.sdp()))
        Text(
            stringResource(R.string.detach_device),
            color = Color(0xFF737373),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(53.sdp()))
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.triangle_warning),
            contentDescription = null,
            modifier = Modifier.size(28.sdp()),
            tint = Color.Unspecified
        )
        Spacer(Modifier.height(15.sdp()))
        Text(
            "${stringResource(R.string.label_detach_device)} “${device.name}”?",
            color = Color(0xFFC4162D),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(18.sdp()))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "IMEI: ",
                color = Color(0xFF737373),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                device.imei,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(Modifier.height(63.sdp()))
        Text(
            stringResource(R.string.subtitle_detach_device),
            color = Color(0xFF737373),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(75.sdp()))
        Column(
            modifier = Modifier.width(330.sdp()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomButton(
                text = stringResource(R.string.detach),
                onClick = {
                    val name = device.name
                    val imei = device.imei
                    viewModel.updateDevice(device.copy(isConnected = false))
                    toTwo(name, imei)
                },
                typeColor = TypeColor.Red,
                height = 55,
                radius = 10
            )
            Spacer(Modifier.height(15.sdp()))
            CustomButton(
                text = stringResource(R.string.cancellation),
                onClick = toBack,
                typeColor = TypeColor.Black,
                height = 55,
                radius = 10
            )
        }
    }
}