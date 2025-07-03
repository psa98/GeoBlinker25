package com.example.geoblinker.ui.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.geoblinker.ui.CodeTextField
import com.example.geoblinker.ui.CustomButton
import com.example.geoblinker.ui.HSpacer
import com.example.geoblinker.ui.TypeColor
import com.example.geoblinker.ui.theme.hdp
import com.example.geoblinker.ui.theme.sdp
import kotlinx.coroutines.delay

@Composable
fun CodeScreen(
    nextScreen: () -> Unit,
    toBack: () -> Unit,
    viewModel: AuthViewModel
) {
    val uiState by viewModel.codeUiState
    var value by rememberSaveable { mutableStateOf("") }
    var changeMode by rememberSaveable { mutableStateOf(false) }
    var remainingTime by rememberSaveable { mutableIntStateOf(30) }
    var checkTimer by rememberSaveable { mutableStateOf(true) }
    var textTitle by remember { mutableStateOf(viewModel.getNowWay()) }

    LaunchedEffect(checkTimer) {
        while (remainingTime > 0) {
            delay(1000) // Ждем 1 секунду
            remainingTime--
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is CodeUiState.Error)
            changeMode = true
        else if (uiState is CodeUiState.Success)
            nextScreen()
    }

    fun onClick() {
        viewModel.checkWay(value)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HSpacer(156)
        CodeTextField(
            value = value,
            onValueChange = {
                value = it
                viewModel.resetCodeUiState()
            },
            onDone = { onClick() },
            placeholder = R.string.enter_the_code,
            isError = uiState is CodeUiState.Error
        )
        HSpacer(20)
        CustomButton(
            text = stringResource(R.string.confirm),
            onClick = { onClick() },
            typeColor = TypeColor.Black,
            enabled = uiState !is CodeUiState.Error && value.length == 4,
            height = 81,
            radius = 24,
            style = MaterialTheme.typography.headlineMedium
        )
        HSpacer(45)
        Text(
            text = stringResource(R.string.send_to_another_number),
            modifier = Modifier.clickable { toBack() },
            style = MaterialTheme.typography.bodyLarge
        )
        HSpacer(15)
        Text(
            text = stringResource(R.string.get_the_code_in_another_way),
            modifier = Modifier.clickable {
                if (changeMode) {
                    changeMode = false
                    viewModel.resetCodeUiState()
                }
                checkTimer = !checkTimer
                remainingTime = 30
                textTitle = viewModel.getNextWay()
                value = ""
            },
            style = MaterialTheme.typography.bodyLarge
        )

        if (!changeMode) {
            HSpacer(15)
            Text(
                text = if (remainingTime > 0)
                    "${stringResource(R.string.resend)} ... $remainingTime c"
                else
                    stringResource(R.string.resend),
                modifier = Modifier.clickable {
                    if (remainingTime == 0) {
                        viewModel.sendCode()
                        checkTimer = !checkTimer
                        remainingTime = 30
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

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HSpacer(10)
        if (!changeMode) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.comment_alt_dots_1),
                contentDescription = null,
                modifier = Modifier.size(28.sdp())
            )
            HSpacer(20)
            Text(
                stringResource(textTitle ?: R.string.error),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else if (uiState is CodeUiState.Error) {
            HSpacer(20)
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.attention),
                contentDescription = null,
                modifier = Modifier.size(28.sdp()),
                tint = Color(0xFFC4162D)
            )
            HSpacer(5)
            Text(
                stringResource(R.string.invalid_code),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier.offset(y = (-112).hdp())
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
                viewModel.resetCodeUiState()
            } else
                toBack()
        },
        color = Color(0xFFEFEFEF)
    )
}