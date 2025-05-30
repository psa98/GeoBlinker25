package com.example.geoblinker.ui.main.profile.subscription

import android.graphics.Paint.Align
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import com.example.geoblinker.R
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.theme.Manrope
import com.example.geoblinker.ui.theme.black
import com.example.geoblinker.ui.theme.sdp

@Composable
fun SubscriptionReadyScreen(
    toBack: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.green_tick),
            contentDescription = null,
            modifier = Modifier.size(27.sdp(), 20.sdp()),
            tint = Color.Black
        )
        Spacer(Modifier.height(24.sdp()))
        Text(
            "Благодарим вас за покупку!",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Medium
            )
        )
        Spacer(Modifier.height(116.sdp()))
        Text(
            buildAnnotatedString {
                append("Ваша текущая подписка продлена до: ")
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.typography.titleMedium.color,
                        fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize
                    )
                ) {
                    append("12.10.26")
                }
            },
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall
        )
    }

    BackButton(
        onClick = toBack
    )
}