package com.example.geoblinker.ui.main.device

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.geoblinker.R
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.main.DeviceViewModel
import com.example.geoblinker.ui.theme.sdp

@Composable
fun DeviceTwoScreen(
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
                ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                    // Создаем направляющую на 3/5 ширины экрана
                    val guideline = createGuidelineFromStart(0.63f)

                    val (text1, text2, switch) = createRefs()

                    // Первый текст - слева
                    Text(
                        text = "Начато движение",
                        modifier = Modifier.constrainAs(text1) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        },
                        style = MaterialTheme.typography.labelMedium
                    )

                    // Второй текст - центрирован относительно 3/5 ширины
                    Text(
                        text = "...",
                        modifier = Modifier.constrainAs(text2) {
                            centerAround(guideline) // Центр текста на направляющей
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        },
                        textDecoration = TextDecoration.Underline,
                        style = MaterialTheme.typography.labelMedium
                    )

                    // Свитч - справа
                    Switch(
                        checked = false,
                        onCheckedChange = {},
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
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 15.sdp()),
                    thickness = 1.sdp(),
                    color = Color(0xFFDAD9D9)
                )
                ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                    // Создаем направляющую на 3/5 ширины экрана
                    val guideline = createGuidelineFromStart(0.63f)

                    val (text1, text2, switch) = createRefs()

                    // Первый текст - слева
                    Text(
                        text = "Остановка",
                        modifier = Modifier.constrainAs(text1) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        },
                        style = MaterialTheme.typography.labelMedium
                    )

                    // Второй текст - центрирован относительно 3/5 ширины
                    Text(
                        text = "...",
                        modifier = Modifier.constrainAs(text2) {
                            centerAround(guideline) // Центр текста на направляющей
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        },
                        textDecoration = TextDecoration.Underline,
                        style = MaterialTheme.typography.labelMedium
                    )

                    // Свитч - справа
                    Switch(
                        checked = false,
                        onCheckedChange = {},
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
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 15.sdp()),
                    thickness = 1.sdp(),
                    color = Color(0xFFDAD9D9)
                )
                ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                    // Создаем направляющую на 3/5 ширины экрана
                    val guideline = createGuidelineFromStart(0.63f)

                    val (text1, text2, switch) = createRefs()

                    // Первый текст - слева
                    Text(
                        text = "Низкий заряд",
                        modifier = Modifier.constrainAs(text1) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        },
                        style = MaterialTheme.typography.labelMedium
                    )

                    // Второй текст - центрирован относительно 3/5 ширины
                    Text(
                        text = "...",
                        modifier = Modifier.constrainAs(text2) {
                            centerAround(guideline) // Центр текста на направляющей
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        },
                        textDecoration = TextDecoration.Underline,
                        style = MaterialTheme.typography.labelMedium
                    )

                    // Свитч - справа
                    Switch(
                        checked = false,
                        onCheckedChange = {},
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
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 15.sdp()),
                    thickness = 1.sdp(),
                    color = Color(0xFFDAD9D9)
                )
                ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                    // Создаем направляющую на 3/5 ширины экрана
                    val guideline = createGuidelineFromStart(0.63f)

                    val (text1, text2, switch) = createRefs()

                    // Первый текст - слева
                    Text(
                        text = "Дверь открыта",
                        modifier = Modifier.constrainAs(text1) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        },
                        style = MaterialTheme.typography.labelMedium
                    )

                    // Второй текст - центрирован относительно 3/5 ширины
                    Text(
                        text = "...",
                        modifier = Modifier.constrainAs(text2) {
                            centerAround(guideline) // Центр текста на направляющей
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        },
                        textDecoration = TextDecoration.Underline,
                        style = MaterialTheme.typography.labelMedium
                    )

                    // Свитч - справа
                    Switch(
                        checked = false,
                        onCheckedChange = {},
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
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 15.sdp()),
                    thickness = 1.sdp(),
                    color = Color(0xFFDAD9D9)
                )
                ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                    // Создаем направляющую на 3/5 ширины экрана
                    val guideline = createGuidelineFromStart(0.63f)

                    val (text1, text2, switch) = createRefs()

                    // Первый текст - слева
                    Text(
                        text = "Достиг локации",
                        modifier = Modifier.constrainAs(text1) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        },
                        style = MaterialTheme.typography.labelMedium
                    )

                    // Второй текст - центрирован относительно 3/5 ширины
                    Text(
                        text = "...",
                        modifier = Modifier.constrainAs(text2) {
                            centerAround(guideline) // Центр текста на направляющей
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        },
                        textDecoration = TextDecoration.Underline,
                        style = MaterialTheme.typography.labelMedium
                    )

                    // Свитч - справа
                    Switch(
                        checked = false,
                        onCheckedChange = {},
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
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 15.sdp()),
                    thickness = 1.sdp(),
                    color = Color(0xFFDAD9D9)
                )
                ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                    // Создаем направляющую на 3/5 ширины экрана
                    val guideline = createGuidelineFromStart(0.63f)

                    val (text1, text2, switch) = createRefs()

                    // Первый текст - слева
                    Text(
                        text = "Ещё сигнал",
                        modifier = Modifier.constrainAs(text1) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        },
                        style = MaterialTheme.typography.labelMedium
                    )

                    // Второй текст - центрирован относительно 3/5 ширины
                    Text(
                        text = "...",
                        modifier = Modifier.constrainAs(text2) {
                            centerAround(guideline) // Центр текста на направляющей
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        },
                        textDecoration = TextDecoration.Underline,
                        style = MaterialTheme.typography.labelMedium
                    )

                    // Свитч - справа
                    Switch(
                        checked = false,
                        onCheckedChange = {},
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
            }
        }
    }

    BackButton { toBack() }
}