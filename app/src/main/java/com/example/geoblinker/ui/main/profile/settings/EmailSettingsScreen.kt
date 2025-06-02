package com.example.geoblinker.ui.main.profile.settings

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.geoblinker.R
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.CustomButton
import com.example.geoblinker.ui.EmailTextField
import com.example.geoblinker.ui.TypeColor
import com.example.geoblinker.ui.main.MainScreen
import com.example.geoblinker.ui.main.viewmodel.ProfileViewModel
import com.example.geoblinker.ui.theme.Manrope
import com.example.geoblinker.ui.theme.black
import com.example.geoblinker.ui.theme.sdp
import kotlinx.coroutines.delay

private enum class Email {
    NoEmail,
    HasEmail,
    IsCheck,
    Ready
}

@Composable
fun EmailSettingsScreen(
    viewModel: ProfileViewModel,
    isShow: Boolean = false,
    toBack: () -> Unit,
    navController: NavHostController = rememberNavController()
) {
    val email by viewModel.email.collectAsState()
    val emailFirst = email
    var value by remember { mutableStateOf("") }

    /**
     * TODO:
     * Пока что это таймер-заглушка, так как нет бека для отправки ссылки с подтверждением почты.
     */
    var remainingTime by remember { mutableIntStateOf(15) }
    var checkTimer by remember { mutableStateOf(false) }

    LaunchedEffect(checkTimer) {
        if (checkTimer) {
            while (remainingTime > 0) {
                delay(1000)
                remainingTime--
            }
            viewModel.setEmail(value)
            navController.navigate(Email.Ready.name)
        }
    }

    NavHost(
        navController,
        startDestination = if (emailFirst.isEmpty()) Email.NoEmail.name else Email.HasEmail.name
    ) {
        composable(route = Email.NoEmail.name) {
            NotEmail(
                isShow,
                onDone = {
                    value = it
                    navController.navigate(Email.IsCheck.name)
                }
            )
        }
        composable(route = Email.IsCheck.name) {
            checkTimer = true
            IsCheck(
                valueDefault = value
            )
        }
        composable(route = Email.Ready.name) {
            Ready(
                email
            )
        }
        composable(route = Email.HasEmail.name) {
            HasEmail(
                email,
                onDone = {
                    value = it
                    navController.navigate(Email.IsCheck.name)
                }
            )
        }
    }

    BackButton(
        toBack
    )
}

@Composable
fun NotEmail(
    isShowDefault: Boolean,
    onDone: (String) -> Unit
) {
    var isShow by remember { mutableStateOf(isShowDefault) }
    var value by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.account_email),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(Modifier.height(30.sdp()))
        Text(
            buildAnnotatedString {
                append(
                    stringResource(R.string.no_email_1)
                )
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.typography.bodyMedium.color,
                        fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize
                    )
                ) {
                    append(
                        ' ' + stringResource(R.string.no_email_span) + ' '
                    )
                }
                append(
                    stringResource(R.string.no_email_2)
                )
            },
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }

    if (isShow) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            EmailTextField(
                value,
                placeholder = stringResource(R.string.enter_email),
                onValueChange = { value = it },
                onDone = { onDone(value) },
                height = 65,
                radius = 16
            )
            Spacer(Modifier.height(15.sdp()))
            CustomButton(
                text = stringResource(R.string.confirm),
                onClick = { onDone(value) },
                typeColor = TypeColor.Green,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
    else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.height(80.sdp()))
            CustomButton(
                text = stringResource(R.string.link_email),
                onClick = { isShow = true },
                typeColor = TypeColor.Green,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Composable
fun IsCheck(
    valueDefault: String
) {
    var remainingTime by remember { mutableIntStateOf(10) } // TODO: пок что будет 10 секунд, потом нужно будет поставить коректное время
    var checkTimer by remember { mutableStateOf(true) }
    var value by remember { mutableStateOf(valueDefault) }

    // Запускаем таймер
    LaunchedEffect(checkTimer) {
        while (remainingTime > 0) {
            delay(1000) // Ждем 1 секунду
            remainingTime--
        }
    }

    fun onDone() {
        // TODO: Добавить отправку ссылки
        checkTimer = !checkTimer
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.account_email),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(Modifier.height(46.sdp()))
        Icon(
            imageVector = ImageVector.vectorResource(if (remainingTime > 0) R.drawable.envelope else R.drawable.triangle_warning),
            contentDescription = null,
            modifier = Modifier.size(28.sdp()),
            tint = Color.Unspecified
        )
        Spacer(Modifier.height(10.sdp()))
        Text(
            stringResource(if (remainingTime > 0) R.string.activation_link_email else R.string.no_activation_link_email),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        EmailTextField(
            value,
            placeholder = stringResource(R.string.enter_email),
            onValueChange = { value = it },
            onDone = { onDone() },
            height = 65,
            radius = 16
        )
        Spacer(Modifier.height(15.sdp()))
        CustomButton(
            text = stringResource(R.string.confirm),
            onClick = { onDone() },
            typeColor = TypeColor.Green,
            style = MaterialTheme.typography.headlineMedium
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier.offset(y = (-112).sdp())
        ) {
            Text(
                text = stringResource(R.string.email_not_coming),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun Ready(
    email: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.account_email),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(Modifier.height(20.sdp()))
        Text(
            email,
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Normal
            )
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            stringResource(R.string.email_connected),
            color = Color(0xFF12CD4A),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun HasEmail(
    email: String,
    onDone: (String) -> Unit
) {
    var value by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.account_email),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(Modifier.height(20.sdp()))
        Text(
            email,
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Normal
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        EmailTextField(
            value,
            placeholder = stringResource(R.string.enter_new_email),
            onValueChange = { value = it },
            onDone = { onDone(value) },
            height = 65,
            radius = 16
        )
        Spacer(Modifier.height(15.sdp()))
        CustomButton(
            text = stringResource(R.string.change_email),
            onClick = { onDone(value) },
            typeColor = TypeColor.Black,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
