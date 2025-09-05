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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.geoblinker.R
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.CustomButton
import com.example.geoblinker.ui.TypeColor
import com.example.geoblinker.ui.main.GeoBlinker.Companion.gson
import com.example.geoblinker.ui.main.viewmodel.SubscriptionViewModel
import com.example.geoblinker.ui.theme.sdp
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

@Composable
fun SubscriptionTwoScreen(
    viewModel: SubscriptionViewModel,
    paySubscription: () -> Unit,
    toBack: () -> Unit
) {
    val pickSubscription by viewModel.pickSubscription.collectAsState()
    val paymentSuccess by viewModel.paymentSuccess.collectAsState()
    
    // Проверяем статус при каждом возврате на экран
    LaunchedEffect(Unit) {
        viewModel.checkPaymentStatus()
    }
    
    // Также проверяем при каждой перекомпозиции
    LaunchedEffect(paymentSuccess) {
        viewModel.refreshPaymentStatus()
    }
    
    // Получаем сообщения из SharedPreferences
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
    val successMessage = prefs.getString("success_message", null)
    val errorMessage = prefs.getString("error_message", null)
    
    // Получаем статус подписки
    val subscriptionActive by viewModel.subscriptionActive.collectAsState()
    
    // Показываем сообщение об успешной оплате
    if (paymentSuccess || successMessage != null) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Text(
                "✅ Успешно оплачено!",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF4CAF50),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                successMessage ?: "Подписка ${pickSubscription.labelPeriod} активирована",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Tanlangan tarifga qarab to'g'ri matnni ko'rsatamiz
            val selectedTariffId = prefs.getInt("selected_tariff_id", 1)
            /*
             todo:
               1. Проверить что в selected_tariff_id сохраняется выбранная подписка
               (и желательно именно в момент подтверждения покупки)
               2. При авторизации мы получаем карту с текущими тарифами - надо там же
               создать карту код тарифа -> название тарифа, и выгрузить ее в префы
               тогда ниже можно будет нормально указать ее имя
             */


            val tariffMapGson = prefs.getString("tariff_names_map","[]")!!
            val mapType: Type = object : TypeToken<HashMap<String, String>>() {}.type
            val tariffNames:HashMap<String, String> =  gson.fromJson(tariffMapGson,mapType)


            val subscriptionText = tariffNames[selectedTariffId.toString()]?:""

            Text(
                subscriptionText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            CustomButton(
                text = "Продолжить",
                typeColor = TypeColor.Green,
                onClick = {
                    viewModel.clearPaymentSuccess()
                    toBack()
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            // Показываем статус подписки - ВСЕГДА АКТИВНА если успешная оплата
            Text(
                "Статус подписки: АКТИВНА ✅",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF4CAF50),
                textAlign = TextAlign.Center
            )
        }
        return
    }
    
    // Показываем сообщение об ошибке
    if (errorMessage != null) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Text(
                "❌ Ошибка оплаты",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFFE53E3E),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                errorMessage,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            CustomButton(
                text = "Попробовать еще раз",
                typeColor = TypeColor.Green,
                onClick = {
                    prefs.edit().remove("error_message").apply()
                    // Остаемся на экране оплаты
                }
            )
        }
        return
    }

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
                        val priceString = (if (pickSubscription.price == pickSubscription.price.toInt().toDouble())
                            pickSubscription.price.toInt().toString() else pickSubscription.price.toString()) +
                                "  " + pickSubscription.currencyName
                        Text(
                            priceString,
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
        val priceString = (if (pickSubscription.price == pickSubscription.price.toInt().toDouble())
            pickSubscription.price.toInt().toString() else pickSubscription.price.toString()) +
        "  " + pickSubscription.currencyName

        Text(
            priceString ,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(54.sdp()))
        CustomButton(
            text = "Оплатить",
            onClick = paySubscription,
            typeColor = TypeColor.Green,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }

    BackButton(
        onClick = toBack
    )
}