package com.example.geoblinker.ui.main.profile.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.geoblinker.R
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.CodeTextField
import com.example.geoblinker.ui.CustomButton
import com.example.geoblinker.ui.GreenMediumButton
import com.example.geoblinker.ui.PhoneNumberTextField
import com.example.geoblinker.ui.TypeColor
import com.example.geoblinker.ui.formatPhoneNumber
import com.example.geoblinker.ui.main.viewmodel.ProfileViewModel
import com.example.geoblinker.ui.theme.sdp
import kotlinx.coroutines.delay

@Composable
fun PhoneSettingsScreen(
    viewModel: ProfileViewModel,
    toCodeNotComing: () -> Unit,
    toBack: () -> Unit
) {
    var isCheckScreen by remember { mutableStateOf(false) }
    var isCheck by remember { mutableStateOf(false) }
    var value by remember { mutableStateOf("") }

    if (!isCheckScreen) {
        PhoneScreen(
            viewModel,
            isCheck,
            toCheck = {
                value = it
                isCheckScreen = true
            }
        )
    }
    else {
        CheckScreen(
            viewModel,
            value,
            toCodeNotComing = toCodeNotComing,
            toReady = {
                viewModel.setPhone(value)
                isCheck = true
                isCheckScreen = false
            },
            toBack = { isCheckScreen = false }
        )
    }

    BackButton(
        onClick = toBack
    )
}

@Composable
private fun PhoneScreen(
    viewModel: ProfileViewModel,
    isCheck: Boolean = false,
    toCheck: (String) -> Unit
) {
    val phone by viewModel.phone.collectAsState()
    var value by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    fun onClick() {
        if (value.length < 10)
            isError = true
        else
            toCheck(value)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.active_phone),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(Modifier.height(20.sdp()))
        Text(
            "+7 " + formatPhoneNumber(phone),
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Normal
            )
        )
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        PhoneNumberTextField(
            placeholder = ' ' + stringResource(R.string.link_new_phone),
            onValueChange = {
                isError = false
                value = it
            },
            onDone = { onClick() },
            isError = isError,
            radius = 16,
            height = 65
        )
        Spacer(Modifier.height(15.sdp()))
        GreenMediumButton(
            text = stringResource(R.string.confirm),
            onClick = { onClick() },
            height = 65
        )
    }
    Box(
        modifier = Modifier.fillMaxSize().offset(y = 100.sdp()),
        contentAlignment = Alignment.Center
    ) {
        if (isError) {
            Text(
                stringResource(R.string.invalid_number),
                color = Color(0xFFC4162D),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        if (isCheck) {
            Text(
                stringResource(R.string.phone_updated),
                color = Color(0xFF12CD4A),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun CheckScreen(
    viewModel: ProfileViewModel,
    phone: String,
    toReady: () -> Unit,
    toCodeNotComing: () -> Unit,
    toBack: () -> Unit
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
        if (viewModel.checkPhone(value, phone))
            toReady()
        else {
            isError = true
            changeMode = true
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!changeMode) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.comment_alt_dots_1),
                contentDescription = null,
                modifier = Modifier.size(28.sdp())
            )
            Spacer(Modifier.height(20.sdp()))
            Text(
                stringResource(R.string.sms_way_title),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(57.sdp()))
        } else {
            Spacer(Modifier.height(38.sdp()))
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
            } else
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
            isError = isError,
            height = 65,
            radius = 16
        )
        Spacer(Modifier.height(15.sdp()))
        CustomButton(
            text = stringResource(R.string.confirm),
            onClick = { onClick() },
            typeColor = TypeColor.Green,
            enabled = !isError && value.length == 4
        )
        Spacer(Modifier.height(15.sdp()))
        CustomButton(
            text = stringResource(R.string.send_to_another_number),
            onClick = { toBack() },
            typeColor = TypeColor.White,
            radius = 16,
            height = 65
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        if (changeMode) {
            Box(
                modifier = Modifier.offset(y = (-218).sdp())
            ) {
                ConstraintLayout(
                    modifier = Modifier.wrapContentWidth()
                ) {
                    val (text, line) = createRefs()

                    Text(
                        text = if (remainingTime > 0)
                            "${stringResource(R.string.resend)} ... $remainingTime c"
                        else
                            stringResource(R.string.resend),
                        modifier = Modifier.constrainAs(text) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }.clickable {
                            if (remainingTime == 0) {
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
        Box(
            modifier = Modifier.offset(y = (-112).sdp())
        ) {
            Text(
                text = stringResource(R.string.is_the_code_not_coming),
                modifier = Modifier.clickable { toCodeNotComing() },
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}