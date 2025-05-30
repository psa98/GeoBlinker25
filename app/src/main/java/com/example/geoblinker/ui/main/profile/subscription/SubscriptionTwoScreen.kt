package com.example.geoblinker.ui.main.profile.subscription

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.geoblinker.R
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.GreenMediumButton
import com.example.geoblinker.ui.main.viewmodel.SubscriptionViewModel
import com.example.geoblinker.ui.theme.sdp

@Composable
fun SubscriptionTwoScreen(
    viewModel: SubscriptionViewModel,
    paySubscription: () -> Unit,
    toBack: () -> Unit
) {
    val pickSubscription by viewModel.pickSubscription.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(330.sdp(), 120.sdp()),
            shape = RoundedCornerShape(15.sdp()),
            color = Color.White
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Image(
                    painter = painterResource(pickSubscription.draw),
                    contentDescription = null,
                    modifier = Modifier.fillMaxHeight(),
                    contentScale = ContentScale.Crop
                )
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight().padding(
                        start = 15.sdp(),
                        top = 15.sdp(),
                        bottom = 15.sdp()
                    ),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "${pickSubscription.price} руб.",
                            style = MaterialTheme.typography.displaySmall
                        )
                        Spacer(Modifier.width(17.sdp()))
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.green_tick),
                            contentDescription = null,
                            modifier = Modifier.size(17.sdp(), 13.sdp()),
                            tint = Color.Unspecified
                        )
                    }
                    Text(
                        pickSubscription.labelPeriod,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
        Spacer(Modifier.height(35.sdp()))
        Text(
            "Оплатите подписку удобным вам способом:",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(20.sdp()))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.visa_mast_mir),
                contentDescription = null,
                modifier = Modifier.size(119.sdp(), 26.sdp()),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(20.sdp()))
            Image(
                painter = painterResource(R.drawable.sbp),
                contentDescription = null,
                modifier = Modifier.size(21.sdp(), 26.sdp()),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(20.sdp()))
            Image(
                painter = painterResource(R.drawable.iomoney),
                contentDescription = null,
                modifier = Modifier.size(93.sdp(), 25.sdp()),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(Modifier.height(54.sdp()))
        Text(
            "Итого к оплате:",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            "${pickSubscription.price} руб.",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(54.sdp()))
        GreenMediumButton(
            text = "Оплатить",
            onClick = paySubscription,
            height = 65,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }

    BackButton(
        onClick = toBack
    )
}