package com.example.geoblinker.ui.main.device

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.geoblinker.GeoBlinkerScreen
import com.example.geoblinker.R
import com.example.geoblinker.data.SignalType
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.main.viewmodel.DeviceViewModel
import com.example.geoblinker.ui.theme.sdp

@Composable
fun DeviceTwoScreen(
    viewModel: DeviceViewModel,
    toThree: () -> Unit,
    toBack: () -> Unit
) {
    val device by viewModel.device.collectAsState()
    val typesSignals by viewModel.typesSignals.collectAsState()

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
            modifier = Modifier.width(330.sdp()),
            shape = RoundedCornerShape(10.sdp()),
            color = Color.White
        ) {
            LazyColumn(
                contentPadding = PaddingValues(15.sdp())
            ) {
                itemsIndexed(typesSignals) { index, item ->
                    Row(modifier = Modifier.fillMaxWidth()) {

                        // Первый текст - слева


                        Text(
                            SignalType.getScreenName(item.type),
                            modifier = Modifier.fillMaxWidth(0.60f)
                            .wrapContentHeight(),
                            style = MaterialTheme.typography.labelSmall
                        )

                        var trigger = "..."
                        if (item.checkedAlarm)
                            trigger = "тревога"
                        else if (item.checkedEmail)
                            trigger = "email"
                        else if (item.checkedPush)
                            trigger = "пуш"
                        // Второй текст - центрирован относительно 3/5 ширины
                        Text(
                            text = trigger,
                            modifier = Modifier.fillParentMaxWidth(0.30f)
                                .clickable {
                                    viewModel.setTypeSignal(item)
                                    toThree()
                                },
                            textDecoration = TextDecoration.Underline,
                            style = MaterialTheme.typography.labelSmall
                        )

                        // Свитч - справа
                        Switch(
                            checked = item.checked,
                            onCheckedChange = {
                                viewModel.updateTypeSignal(
                                    item.copy(
                                        checked = it
                                    )
                                )
                                if (it) {
                                    toThree()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(0.15f).align(Alignment.Top)
                                ,
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
                    if (index < typesSignals.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 15.sdp()),
                            thickness = 1.sdp(),
                            color = Color(0xFFDAD9D9)
                        )
                    }
                }
            }
        }
    }

    BackButton(
        onClick = toBack
    )
}