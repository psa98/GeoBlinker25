package com.example.geoblinker.ui.auth.authorization

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.geoblinker.ui.CustomButton
import com.example.geoblinker.ui.CustomPopup
import com.example.geoblinker.ui.HSpacer
import com.example.geoblinker.ui.PhoneNumberTextField
import com.example.geoblinker.ui.TypeColor
import com.example.geoblinker.ui.formatPhoneNumber
import com.example.geoblinker.ui.theme.sdp
import com.skydoves.cloudy.cloudy

@Composable
fun TwoScreen(
    threeScreen: (String) -> Unit,
    toBack: () -> Unit,
    viewModel: AuthorizationViewModel
) {
    var phone by rememberSaveable { mutableStateOf("") }
    var isPhoneNumberIncorrect by rememberSaveable { mutableStateOf(false) }
    var visiblePopup by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.fillMaxSize().cloudy(16, visiblePopup),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HSpacer(60)
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.title_logo),
            contentDescription = null,
            modifier = Modifier.width(200.sdp()).height(135.sdp())
        )
        HSpacer(15)
        Text(
            stringResource(R.string.version),
            modifier = Modifier.alpha(0.7f),
            style = MaterialTheme.typography.titleSmall
        )
        HSpacer(134)
        PhoneNumberTextField(
            initial = phone,
            onValueChange = {
                phone = it
                isPhoneNumberIncorrect = false
            },
            onDone = {
                if (phone.length < 10)
                    isPhoneNumberIncorrect = true
                else
                    visiblePopup = true
            },
            isError = isPhoneNumberIncorrect
        )
        HSpacer(20)
        CustomButton(
            text = stringResource(R.string.enter),
            onClick = {
                if (phone.length < 10)
                    isPhoneNumberIncorrect = true
                else
                    visiblePopup = true
            },
            typeColor = TypeColor.Black,
            height = 81,
            radius = 24,
            style = MaterialTheme.typography.headlineMedium
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
            HSpacer(5)
            Text(
                stringResource(R.string.invalid_number),
                style = MaterialTheme.typography.bodyLarge
            )
            HSpacer(56)
        }
    }

    if (visiblePopup) {
        LaunchedEffect(Unit) {
            focusManager.clearFocus()
            keyboardController?.hide()
        }

        CustomPopup(
            phone = "+ 7 ${formatPhoneNumber(phone)}",
            onChangeVisible = { visiblePopup = false },
            sendCode = {
                visiblePopup = false
                viewModel.setWays(it)
                viewModel.updatePhone(phone)
                threeScreen(phone)
            },
            viewModel,
            null
        )
    }
}