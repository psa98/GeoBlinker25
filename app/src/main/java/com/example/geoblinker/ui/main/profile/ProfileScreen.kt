package com.example.geoblinker.ui.main.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.geoblinker.R
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.BlackMediumButton
import com.example.geoblinker.ui.GreenMediumButton
import com.example.geoblinker.ui.WhiteMediumButton
import com.example.geoblinker.ui.theme.sdp

@Composable
fun ProfileScreen(
    toBack: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.user_without_photo),
            contentDescription = null,
            modifier = Modifier.size(84.sdp()),
            tint = Color.Unspecified
        )
        Spacer(Modifier.height(10.sdp()))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Константин Гусевский",
                color = Color(0xFF212120),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.width(13.sdp()))
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.pencil),
                contentDescription = null,
                modifier = Modifier.size(10.sdp()),
                tint = Color.Unspecified
            )
        }
        Spacer(Modifier.height(10.sdp()))
        Row {
            Text(
                "Подписка активна до: ",
                color = Color(0xFF737373),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "28.05.2025",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
        Spacer(Modifier.height(28.sdp()))
        GreenMediumButton(
            text = "Купить подписку",
            onClick = {},
            shape = RoundedCornerShape(10.sdp())
        )
        Spacer(Modifier.height(10.sdp()))
        Row {
            BlackMediumButton(
                modifier = Modifier.width(160.sdp()),
                text = "Мои устройства",
                onClick = {}
            )
            Spacer(Modifier.width(10.sdp()))
            BlackMediumButton(
                modifier = Modifier.width(160.sdp()),
                text = "Журнал сигналов",
                onClick = {}
            )
        }
        Spacer(Modifier.height(10.sdp()))
        BlackMediumButton(
            text = "Настройки",
            onClick = {},
            icon = R.drawable.settings
        )
        Spacer(Modifier.height(20.sdp()))
        Row {
            WhiteMediumButton(
                modifier = Modifier.width(160.sdp()),
                text = "Техподдержка",
                onClick = {}
            )
            Spacer(Modifier.width(10.sdp()))
            WhiteMediumButton(
                modifier = Modifier.width(160.sdp()),
                text = "Оставить отзыв",
                onClick = {}
            )
        }
        Spacer(Modifier.height(22.sdp()))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ConstraintLayout(
                modifier = Modifier.wrapContentWidth()
            ) {
                val (text, line) = createRefs()

                Text(
                    "О приложении",
                    style = MaterialTheme.typography.bodyMedium,
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
            ConstraintLayout(
                modifier = Modifier.wrapContentWidth()
            ) {
                val (text, line) = createRefs()

                Text(
                    "О компании",
                    style = MaterialTheme.typography.bodyMedium,
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
    }

    BackButton(
        onClick = toBack
    )
}
