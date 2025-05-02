package com.example.geoblinker.ui.authorization

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.geoblinker.R
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.BlackButton
import com.example.geoblinker.ui.CodeTextField
import com.example.geoblinker.ui.WhiteButton
import com.example.geoblinker.ui.theme.GeoBlinkerTheme
import kotlinx.coroutines.delay

@Composable
fun ThreeScreen(
    fourScreen: () -> Unit,
    backFun: () -> Unit
) {
    var value by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var changeMode by remember { mutableStateOf(false) }
    var remainingTime by remember { mutableIntStateOf(30) }
    var checkTimer by remember { mutableStateOf(true) }

    // Запускаем таймер
    LaunchedEffect(checkTimer) {
        while (remainingTime > 0) {
            delay(1000) // Ждем 1 секунду
            remainingTime--
        }
    }

    fun onClick() {
        if (value == "1234")
            fourScreen()
        else {
            isError = true
            changeMode = true
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!changeMode) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.comment_alt_dots_1),
                contentDescription = null
            )
            Spacer(Modifier.height(20.dp))
            Text(
                stringResource(R.string.sent_an_SMS),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(57.dp))
        }
        else {
            if (isError) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.attention),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = Color(0xFFC4162D)
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    stringResource(R.string.invalid_code),
                    modifier = Modifier.height(22.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.height(57.dp))
            }
            else
                Spacer(Modifier.height(112.dp))
        }
        CodeTextField(
            onValueChange = {
                value = it
                isError = false
            },
            onDone = { onClick() },
            placeholder = R.string.enter_the_code,
            isError = isError
        )
        Spacer(Modifier.height(20.dp))
        BlackButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.confirm),
            onClick = { onClick() },
            enabled = !isError && value.length == 4
        )
        Spacer(Modifier.height(20.dp))
        WhiteButton(
            text = stringResource(R.string.send_to_another_number),
            onClick = backFun
        )
        Spacer(Modifier.height(29.dp))
        Text(
            text = stringResource(R.string.is_the_code_not_coming),
            style = MaterialTheme.typography.bodyLarge
        )
        if (changeMode) {
            Spacer(Modifier.height(52.dp))
            BlackButton(
                modifier = Modifier.width(266.dp),
                text = if (remainingTime > 0)
                    "${stringResource(R.string.resend)} ... $remainingTime c"
                else
                    stringResource(R.string.resend),
                onClick = {
                    isError = false
                    changeMode = false
                    checkTimer = !checkTimer
                    remainingTime = 30
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                enabled = remainingTime == 0,
                height = 55
            )
            Spacer(Modifier.height(73.dp))
        }
        else {
            Spacer(Modifier.height(85.dp))
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BackButton(backFun)
        Spacer(Modifier.height(36.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewThree() {
    GeoBlinkerTheme {
        ThreeScreen({}, {})
    }
}