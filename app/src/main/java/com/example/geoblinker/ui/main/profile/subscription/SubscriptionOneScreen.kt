package com.example.geoblinker.ui.main.profile.subscription

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.main.viewmodel.SubscriptionViewModel
import com.example.geoblinker.ui.theme.sdp

@Composable
fun SubscriptionOneScreen(
    viewModel: SubscriptionViewModel,
    toPaySubscription: () -> Unit,
    toBack: () -> Unit
) {
    val subscriptionOptions by viewModel.subscriptionOptions.collectAsState()
    
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                "Купить подписку",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(25.sdp()))
        }
        itemsIndexed(subscriptionOptions) { index, subscription ->
            Surface(
                modifier = Modifier.size(330.sdp(), 120.sdp()).clickable {
                    viewModel.setPickSubscription(index)
                    toPaySubscription()
                },
                shape = RoundedCornerShape(15.sdp()),
                color = Color.White
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Image(
                        painter = painterResource(subscription.draw),
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
                        Text(
                            "${if (subscription.price % 1.0 == 0.0) subscription.price.toInt() else subscription.price} USD",
                            style = MaterialTheme.typography.displaySmall
                        )
                        Text(
                            subscription.labelPeriod,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
            Spacer(Modifier.height(15.sdp()))
        }
        item {
            BackButton(
                toBack,
                notPosition = true
            )
        }
    }
}