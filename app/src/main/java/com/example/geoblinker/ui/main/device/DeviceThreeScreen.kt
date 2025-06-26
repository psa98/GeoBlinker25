package com.example.geoblinker.ui.main.device

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.geoblinker.R
import com.example.geoblinker.data.SignalType
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.CustomButton
import com.example.geoblinker.ui.CustomLinkEmailPopup
import com.example.geoblinker.ui.HSpacer
import com.example.geoblinker.ui.TypeColor
import com.example.geoblinker.ui.main.viewmodel.DeviceViewModel
import com.example.geoblinker.ui.main.viewmodel.ProfileViewModel
import com.example.geoblinker.ui.theme.sdp

@Composable
fun DeviceThreeScreen(
    viewModel: DeviceViewModel,
    profileViewModel: ProfileViewModel,
    toLinkEmail: () -> Unit,
    toBack: () -> Unit
) {
    val device by viewModel.device.collectAsState()
    val typeSignal by viewModel.typeSignal.collectAsState()
    val email by profileViewModel.email.collectAsState()
    var isShow by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.requiredWidth(330.sdp()),
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
            when(typeSignal.type) {
                SignalType.MovementStarted -> "Начато движение"
                SignalType.Stop -> "Остановка"
                SignalType.LowCharge -> "Низкий заряд"
                SignalType.DoorOpen -> "Дверь открыта"
                SignalType.ReachedLocation -> "Достиг локации"
            },
            color = Color(0xFF737373),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(25.sdp()))
        Surface(
            modifier = Modifier.width(330.sdp()),
            shape = RoundedCornerShape(10.sdp()),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(15.sdp())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        stringResource(R.string.push).capitalize(),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Switch(
                        checked = typeSignal.checkedPush,
                        onCheckedChange = {
                            viewModel.updateTypeSignal(
                                typeSignal.copy(
                                    checkedPush = it
                                )
                            )
                        },
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
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Email",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Switch(
                        checked = typeSignal.checkedEmail,
                        onCheckedChange = {
                            if (email.isEmpty()) {
                                isShow = true
                            }
                            else {
                                viewModel.updateTypeSignal(
                                    typeSignal.copy(
                                        checkedEmail = it
                                    )
                                )
                            }
                        },
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
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        stringResource(R.string.alert).capitalize(),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Switch(
                        checked = typeSignal.checkedAlarm,
                        onCheckedChange = {
                            viewModel.updateTypeSignal(
                                typeSignal.copy(
                                    checkedAlarm = it
                                )
                            )
                        },
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
            }
        }
        HSpacer(25)
        CustomButton(
            text = stringResource(R.string.select_alarm_sound),
            onClick = {},
            typeColor = TypeColor.Blue,
            enabled = typeSignal.checkedAlarm,
            height = 55,
            radius = 10,
            style = MaterialTheme.typography.bodyMedium
        )
    }

    BackButton(
        onClick = toBack
    )

    if (isShow) {
        CustomLinkEmailPopup(
            toLinkEmail,
            { isShow = false }
        )
    }
}