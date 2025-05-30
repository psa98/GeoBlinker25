package com.example.geoblinker.ui.authorization

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.example.geoblinker.R
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.BlackButton
import com.example.geoblinker.ui.CustomPopup
import com.example.geoblinker.ui.PhoneNumberTextField
import com.example.geoblinker.ui.formatPhoneNumber
import com.example.geoblinker.ui.theme.sdp
import com.skydoves.cloudy.cloudy

@Composable
fun TwoScreen(
    threeScreen: (String) -> Unit,
    toBack: () -> Unit,
    viewModel: AuthorizationViewModel
) {
    var value by remember { mutableStateOf("") }
    var isPhoneNumberIncorrect by remember { mutableStateOf(false) }
    var visiblePopup by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.fillMaxSize().cloudy(16, visiblePopup),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(88.sdp()))
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.title_logo),
            contentDescription = null,
            modifier = Modifier.width(200.sdp()).height(135.sdp())
        )
        Spacer(Modifier.height(15.sdp()))
        Text(
            stringResource(R.string.version),
            modifier = Modifier.alpha(0.7f),
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(Modifier.height(134.sdp()))
        PhoneNumberTextField(
            onValueChange = {
                value = it
                isPhoneNumberIncorrect = false
            },
            onDone = {
                if (value.length < 10)
                    isPhoneNumberIncorrect = true
                else
                    visiblePopup = true
            },
            isError = isPhoneNumberIncorrect
        )
        Spacer(Modifier.height(20.sdp()))
        BlackButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.enter),
            onClick = {
                if (value.length < 10)
                    isPhoneNumberIncorrect = true
                else
                    visiblePopup = true
            }
        )
    }

    BackButton(
        onClick = toBack,
        color = Color(0xFFEFEFEF)
    )

    if (isPhoneNumberIncorrect) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.attention),
                contentDescription = null,
                modifier = Modifier.size(28.sdp()),
                tint = Color(0xFFC4162D)
            )
            Spacer(Modifier.height(5.sdp()))
            Text(
                stringResource(R.string.invalid_number),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(56.sdp()))
        }
    }

    if (visiblePopup) {
        LaunchedEffect(Unit) {
            focusManager.clearFocus()
            keyboardController?.hide()
        }

        CustomPopup(
            phone = "+ 7 ${formatPhoneNumber(value)}",
            onChangeVisible = { visiblePopup = it },
            sendCode = {
                visiblePopup = false
                viewModel.setWays(it)
                threeScreen(value)
            }
        )
    }
}