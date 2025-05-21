package com.example.geoblinker.ui.main.device

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.geoblinker.R
import com.example.geoblinker.data.TypeSignal
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.main.DeviceViewModel
import com.example.geoblinker.ui.theme.sdp

@Composable
fun DeviceTwoScreen(
    viewModel: DeviceViewModel,
    toThree: () -> Unit,
    toBack: () -> Unit
) {
    val device by viewModel.device.collectAsState()
    val typesSignals by viewModel.typesSignals.collectAsState()

    LaunchedEffect(Unit) {
        typesSignals.forEach { item->
            if (item.checked && !(item.checkedPush || item.checkedEmail || item.checkedAlarm))
                viewModel.updateTypeSignal(item.copy(checked = false))
        }
    }

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
            LazyColumn(
                contentPadding = PaddingValues(15.sdp())
            ) {
                itemsIndexed(typesSignals) { index, item ->
                    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                        // Создаем направляющую на 3/5 ширины экрана
                        val guideline = createGuidelineFromStart(0.63f)

                        val (text1, text2, switch) = createRefs()

                        // Первый текст - слева
                        Text(
                            text = when(item.type) {
                                TypeSignal.SignalType.MovementStarted -> "Начато движение"
                                TypeSignal.SignalType.Stop -> "Остановка"
                                TypeSignal.SignalType.LowCharge -> "Низкий заряд"
                                TypeSignal.SignalType.DoorOpen -> "Дверь открыта"
                                TypeSignal.SignalType.ReachedLocation -> "Достиг локации"
                            },
                            modifier = Modifier.constrainAs(text1) {
                                start.linkTo(parent.start)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            },
                            style = MaterialTheme.typography.labelMedium
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
                            modifier = Modifier
                                .constrainAs(text2) {
                                centerAround(guideline) // Центр текста на направляющей
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                }
                                .clickable {
                                    viewModel.setTypeSignal(item)
                                    toThree()
                                },
                            textDecoration = TextDecoration.Underline,
                            style = MaterialTheme.typography.labelMedium
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
                            modifier = Modifier.size(40.sdp(), 20.sdp())
                                .constrainAs(switch) {
                                    end.linkTo(parent.end)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                },
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

    BackButton { toBack() }
}