package com.example.geoblinker.ui.main.device

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.geoblinker.R
import com.example.geoblinker.ui.main.DeviceViewModel
import com.example.geoblinker.ui.theme.sdp
import java.util.Locale

@Composable
fun DeviceThreeScreen(
    viewModel: DeviceViewModel,
    toBack: () -> Unit
) {
    val device by viewModel.device.collectAsState()

    Column(
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
            stringResource(R.string.setting_up_signals),
            color = Color(0xFF737373),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(25.sdp()))
        Surface(
            modifier = Modifier.size(330.sdp(), 419.sdp()),
            shape = RoundedCornerShape(10.sdp()),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(15.sdp())
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        stringResource(R.string.push).capitalize(),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Switch(
                        checked = false,
                        onCheckedChange = {},
                        modifier = Modifier.size(40.sdp(), 20.sdp()),
                        colors = SwitchDefaults.colors(
                            uncheckedThumbColor = Color(0xFFBEBEBE),
                            uncheckedTrackColor = Color.Unspecified,
                            uncheckedBorderColor = Color(0xFFBEBEBE),
                            checkedThumbColor = Color(0xFF12CD4A),
                            checkedTrackColor = Color.Unspecified,
                            checkedBorderColor = Color(0xFFBEBEBE),
                        )
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 15.sdp()),
                    thickness = 1.sdp(),
                    color = Color(0xFFDAD9D9)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        stringResource(R.string.email).capitalize(),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Switch(
                        checked = false,
                        onCheckedChange = {},
                        modifier = Modifier.size(40.sdp(), 20.sdp()),
                        colors = SwitchDefaults.colors(
                            uncheckedThumbColor = Color(0xFFBEBEBE),
                            uncheckedTrackColor = Color.Unspecified,
                            uncheckedBorderColor = Color(0xFFBEBEBE),
                            checkedThumbColor = Color(0xFF12CD4A),
                            checkedTrackColor = Color.Unspecified,
                            checkedBorderColor = Color(0xFFBEBEBE),
                        )
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 15.sdp()),
                    thickness = 1.sdp(),
                    color = Color(0xFFDAD9D9)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        stringResource(R.string.alert).capitalize(),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Switch(
                        checked = false,
                        onCheckedChange = {},
                        modifier = Modifier.size(40.sdp(), 20.sdp()),
                        colors = SwitchDefaults.colors(
                            uncheckedThumbColor = Color(0xFFBEBEBE),
                            uncheckedTrackColor = Color.Unspecified,
                            uncheckedBorderColor = Color(0xFFBEBEBE),
                            checkedThumbColor = Color(0xFF12CD4A),
                            checkedTrackColor = Color.Unspecified,
                            checkedBorderColor = Color(0xFFBEBEBE),
                        )
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 15.sdp()),
                    thickness = 1.sdp(),
                    color = Color(0xFFDAD9D9)
                )
            }
        }
        Spacer(Modifier.height(25.sdp()))

    }
}