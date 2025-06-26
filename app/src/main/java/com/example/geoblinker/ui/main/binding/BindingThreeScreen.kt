package com.example.geoblinker.ui.main.binding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
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
import com.example.geoblinker.ui.CustomButton
import com.example.geoblinker.ui.HSpacer
import com.example.geoblinker.ui.TypeColor
import com.example.geoblinker.ui.theme.sdp

@Composable
fun BindingThreeScreen(
    device: Device,
    toBindingScreen: () -> Unit,
    toConfigureSignals: () -> Unit,
    toMap: () -> Unit,
    toBack: () -> Unit
) {
    Column(
        modifier = Modifier.requiredWidth(330.sdp()),
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
        HSpacer(24)
        Surface(
            shape = RoundedCornerShape(10.sdp()),
            color = Color.White
        ) {
            Column {
                Row(
                    Modifier.padding(15.sdp()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier.size(9.sdp())
                            .background(Color(0xFF12CD4A), MaterialTheme.shapes.small)
                    )
                    Spacer(Modifier.width(11.sdp()))
                    if (device.name.isNotEmpty()) {
                        Text(
                            device.name,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    } else {
                        Text(
                            stringResource(R.string.an_unnamed_device),
                            color = Color(0xFF737373),
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
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
                    "${stringResource(R.string.linked)} ${TimeUtils.formatToLocalTime(device.bindingTime)}",
                    modifier = Modifier.padding(15.sdp()),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
        HSpacer(15)
        CustomButton(
            text = stringResource(R.string.configure_the_signals),
            onClick = toConfigureSignals,
            typeColor = TypeColor.Black,
            rightIcon = R.drawable.bell,
            iconSize = 18,
            height = 55,
            radius = 10
        )
        HSpacer(15)
        CustomButton(
            text = stringResource(R.string.view_on_the_map),
            onClick = toMap,
            typeColor = TypeColor.Black,
            rightIcon = R.drawable.gps_navigation,
            iconSize = 18,
            height = 55,
            radius = 10
        )
        HSpacer(15)
        CustomButton(
            text = stringResource(R.string.link_device_again),
            onClick = toBindingScreen,
            typeColor = TypeColor.Green,
            leftIcon = R.drawable.plus,
            height = 55,
            radius = 10
        )
    }

    BackButton(
        onClick = toBack
    )
}