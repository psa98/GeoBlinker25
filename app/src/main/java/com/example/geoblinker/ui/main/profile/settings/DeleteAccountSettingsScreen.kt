package com.example.geoblinker.ui.main.profile.settings

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.example.geoblinker.R
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.CustomButton
import com.example.geoblinker.ui.TypeColor
import com.example.geoblinker.ui.theme.sdp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TOTAL_TIME = 10 // Секунд

@Composable
fun DeleteAccountSettingsScreen(
    deleteAccount: () -> Unit,
    toBack: () -> Unit
) {
    var timeLeft by remember { mutableIntStateOf(TOTAL_TIME) }
    val progress = remember { Animatable(1f) }
    val coroutineScope = rememberCoroutineScope()
    var isTimerActive by remember { mutableStateOf(true) }

    // Запускаем таймер при первом отображении
    LaunchedEffect(Unit) {
        while (timeLeft > 0 && isTimerActive) {
            delay(1000)
            timeLeft--
            // Анимируем прогресс
            coroutineScope.launch {
                progress.animateTo(
                    targetValue = timeLeft.toFloat() / TOTAL_TIME,
                    animationSpec = tween(1000)
                )
            }
        }
    }


    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.delete_account),
            color = Color(0xFFC4162D),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(Modifier.height(84.sdp()))
        Text(
            stringResource(R.string.description_delete_account),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(30.sdp()))
        // Анимированный круг
        CountdownTimer(
            progress = progress.value,
            timeLeft = timeLeft,
        )
        Spacer(Modifier.height(136.sdp()))
        CustomButton(
            text = stringResource(R.string.action_delete_account),
            onClick = deleteAccount,
            typeColor = TypeColor.Black,
            enabled = timeLeft == 0
        )
    }

    BackButton(
        onClick = toBack
    )
}

@Composable
fun CountdownTimer(
    progress: Float,
    timeLeft: Int,
    size: Dp = 32.sdp()
) {
    Box(contentAlignment = Alignment.Center) {
        // Фоновый круг (серый)
        Canvas(modifier = Modifier.size(size)) {
            drawCircle(
                color = Color(0xFFEBEBEB),
                radius = size.toPx() / 2,
                style = Stroke(width = 10f)
            )
        }

        // Прогресс (красный)
        Canvas(modifier = Modifier.size(size)) {
            drawArc(
                color = Color(0xFFC4162D),
                startAngle = -90f,
                sweepAngle = 360 * progress,
                useCenter = false,
                style = Stroke(
                    width = 10f,
                    cap = StrokeCap.Round
                )
            )
        }

        // Текст с временем
        Text(
            text = "$timeLeft",
            color = Color(0xFFC4162D),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}