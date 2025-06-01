package com.example.geoblinker.ui.main.profile.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.stylusHoverIcon
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.geoblinker.R
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.theme.sc
import com.example.geoblinker.ui.theme.sdp

@Composable
fun SettingsScreen(
    toName: () -> Unit,
    toBack: () -> Unit
) {
    val settings = listOf(
        Pair("Сменить имя", { toName() }),
        Pair("Номер телефона", {}),
        Pair("Email аккаунта", {}),
        Pair("Настройка уведомлений", {}),
        Pair("Единицы расстояния", {}),
        Pair("Код подтверждения", {})
    )
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                "Настройки",
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(Modifier.height(30.sdp()))
        }
        items(settings) { item ->
            HorizontalDivider(
                Modifier.fillMaxWidth(),
                1.sdp(),
                Color(0xFFDAD9D9)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { item.second() }
                    .padding(vertical = 15.sdp()),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    item.first,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        item {
            HorizontalDivider(
                Modifier.fillMaxWidth(),
                1.sdp(),
                Color(0xFFDAD9D9)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {}
                    .padding(vertical = 15.sdp()),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Выйти из аккаунта",
                    color = Color(0xFFC4162D),
                    style = MaterialTheme.typography.titleMedium
                )
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.sign_out),
                    contentDescription = null,
                    modifier = Modifier.size(19.sdp()),
                    tint = Color.Unspecified
                )
            }
            HorizontalDivider(
                Modifier.fillMaxWidth(),
                1.sdp(),
                Color(0xFFDAD9D9)
            )
            Spacer(Modifier.height(30.sdp()))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Обновлять карту",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        "Карта будет обновляться автоматически",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 10.sp * sc()
                        )
                    )
                }
                Switch(
                    true,
                    {},
                    modifier = Modifier.size(40.sdp(), 21.sdp()),
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
            Spacer(Modifier.height(45.sdp()))
            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                ConstraintLayout(
                    modifier = Modifier.wrapContentWidth()
                ) {
                    val (text, line) = createRefs()

                    Text(
                        "Удалить аккаунт",
                        color = Color(0xFF737373),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.constrainAs(text) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    )

                    Box(
                        modifier = Modifier
                            .height(1.sdp())
                            .background(Color(0xFF737373))
                            .constrainAs(line) {
                                top.linkTo(text.bottom)
                                start.linkTo(text.start)
                                end.linkTo(text.end)
                                width = Dimension.fillToConstraints
                            }
                    )
                }
            }
            Spacer(Modifier.height(60.sdp()))
            BackButton(
                onClick = toBack,
                notPosition = true
            )
        }
    }
}