package com.example.geoblinker.ui.registration

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import com.example.geoblinker.R
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.BlackButton
import com.example.geoblinker.ui.CustomPopup
import com.example.geoblinker.ui.NameTextField
import com.example.geoblinker.ui.PhoneNumberTextField
import com.example.geoblinker.ui.formatPhoneNumber

@Composable
fun OneScreen(
    twoScreen: (String, String) -> Unit,
    backFun: () -> Unit,
    viewModel: RegistrationViewModel
) {
    var phone by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var isErrorPhone by remember { mutableStateOf(false) }
    var isErrorName by remember { mutableStateOf(false) }
    var visiblePopup by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    fun onClick() {
        if (phone.length < 10)
            isErrorPhone = true
        if (name.isEmpty())
            isErrorName = true
        if (!isErrorPhone && !isErrorName)
            visiblePopup = true
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.title_logo),
            contentDescription = null
        )
        Spacer(Modifier.height(15.dp))
        Text(
            stringResource(R.string.version),
            modifier = Modifier.alpha(0.7f),
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(Modifier.height(33.dp))
        if (isErrorPhone) {
            Text(
                stringResource(R.string.invalid_number),
                color = Color(0xFFC4162D),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(15.dp))
        }
        else if (isErrorName) {
            Text(
                stringResource(R.string.enter_the_user_name),
                color = Color(0xFFC4162D),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(15.dp))
        }
        PhoneNumberTextField(
            onValueChange = {
                phone = it
                isErrorPhone = false
                isErrorName = false
            },
            onDone = { onClick() },
            isError = isErrorPhone
        )
        Spacer(Modifier.height(20.dp))
        NameTextField(
            placeholder = stringResource(R.string.your_name),
            onValueChange = {
                name = it
                isErrorPhone = false
                isErrorName = false
            },
            onDone = { onClick() },
            isError = isErrorName
        )
        Spacer(Modifier.height(20.dp))
        BlackButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.confirm),
            onClick = { onClick() },
        )
        Spacer(Modifier.height(56.dp))
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BackButton(backFun)
        Spacer(Modifier.height(40.dp))
    }

    if (visiblePopup) {
        viewModel.clearWays()
        LaunchedEffect(Unit) {
            focusManager.clearFocus()
            keyboardController?.hide()
        }

        CustomPopup(
            phone = "+ 7 ${formatPhoneNumber(phone)}",
            onChangeVisible = { visiblePopup = it },
            sendCode = {
                visiblePopup = false
                twoScreen(phone, name)
            },
            addWay = { viewModel.addWay(it) }
        )
    }
}