package com.example.geoblinker.ui.authorization

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
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
import com.example.geoblinker.R
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.BlackButton
import com.example.geoblinker.ui.CodeTextField
import com.example.geoblinker.ui.theme.sdp
import kotlinx.coroutines.delay

@Composable
fun ThreeScreen(
    fourScreen: () -> Unit,
    toBack: () -> Unit,
    viewModel: AuthorizationViewModel
) {
    var value by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var changeMode by remember { mutableStateOf(false) }
    var remainingTime by remember { mutableIntStateOf(30) }
    var checkTimer by remember { mutableStateOf(true) }
    var textTitle by remember { mutableStateOf(viewModel.getNowWay()) }

    // Запускаем таймер
    LaunchedEffect(checkTimer) {
        while (remainingTime > 0) {
            delay(1000) // Ждем 1 секунду
            remainingTime--
        }
    }

    fun onClick() {
        if (viewModel.checkWay(value))
            fourScreen()
        else {
            isError = true
            changeMode = true
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(51.sdp()))
        if (!changeMode) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.comment_alt_dots_1),
                contentDescription = null,
                modifier = Modifier.size(28.sdp())
            )
            Spacer(Modifier.height(20.sdp()))
            Text(
                stringResource(textTitle ?: R.string.error),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(57.sdp()))
        }
        else {
            if (isError) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.attention),
                    contentDescription = null,
                    modifier = Modifier.size(28.sdp()),
                    tint = Color(0xFFC4162D)
                )
                Spacer(Modifier.height(5.sdp()))
                Text(
                    stringResource(R.string.invalid_code),
                    modifier = Modifier.height(22.sdp()),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.height(57.sdp()))
            }
            else
                Spacer(Modifier.height(112.sdp()))
        }
        CodeTextField(
            value = value,
            onValueChange = {
                value = it
                isError = false
            },
            onDone = { onClick() },
            placeholder = R.string.enter_the_code,
            isError = isError
        )
        Spacer(Modifier.height(20.sdp()))
        BlackButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.confirm),
            onClick = { onClick() },
            enabled = !isError && value.length == 4
        )
        Spacer(Modifier.height(45.sdp()))
        Text(
            text = stringResource(R.string.send_to_another_number),
            modifier = Modifier.clickable { toBack() },
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(15.sdp()))
        Text(
            text = stringResource(R.string.get_the_code_in_another_way),
            modifier = Modifier.clickable {
                if (changeMode) {
                    changeMode = false
                    isError = false
                }
                checkTimer = !checkTimer
                remainingTime = 30
                textTitle = viewModel.getNextWay()
                value = ""
            },
            style = MaterialTheme.typography.bodyLarge
        )

        if (!changeMode) {
            Spacer(Modifier.height(15.sdp()))
            Text(
                text = if (remainingTime > 0)
                    "${stringResource(R.string.resend)} ... $remainingTime c"
                else
                    stringResource(R.string.resend),
                modifier = Modifier.clickable {
                    if (remainingTime == 0) {
                        checkTimer = !checkTimer
                        remainingTime = 30
                        //textTitle = viewModel.getNextWay()
                    }
                },
                color = if (remainingTime > 0)
                    Color(0xFF636363)
                else
                    Color.Unspecified,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier.offset(y = (-112).sdp())
        ) {
            Text(
                text = stringResource(R.string.is_the_code_not_coming),
                color = Color(0xFFC4162D),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

    BackButton(
        onClick = {
            if (changeMode) {
                changeMode = false
                isError = false
            } else
                toBack()
        },
        color = Color(0xFFEFEFEF)
    )
}