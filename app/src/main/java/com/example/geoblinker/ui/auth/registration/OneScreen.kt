package com.example.geoblinker.ui.auth.registration

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.geoblinker.ui.NameTextField
import com.example.geoblinker.ui.PhoneNumberTextField
import com.example.geoblinker.ui.TypeColor
import com.example.geoblinker.ui.auth.RegisterUiState
import com.example.geoblinker.ui.formatPhoneNumber
import com.example.geoblinker.ui.main.viewmodel.ProfileViewModel
import com.example.geoblinker.ui.theme.sdp

@Composable
fun OneScreen(
    viewModel: RegistrationViewModel,
    profileViewModel: ProfileViewModel,
    twoScreen: () -> Unit,
    toBack: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val uiState = viewModel.registerUiState
    val phone = viewModel.phone
    val name = viewModel.name

    fun onClick() {
        viewModel.register(phone, name)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
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
        HSpacer(33)
        PhoneNumberTextField(
            initial = phone,
            onValueChange = {
                viewModel.updatePhone(it)
                viewModel.resetRegisterUiState()
            },
            onDone = { onClick() },
            isError = uiState is RegisterUiState.Error.ErrorPhone
        )
        HSpacer(20)
        NameTextField(
            value = name,
            placeholder = stringResource(R.string.your_name),
            onValueChange = {
                viewModel.updateName(it)
                viewModel.resetRegisterUiState()
            },
            onDone = { onClick() },
            isError = uiState is RegisterUiState.Error.ErrorName
        )
        HSpacer(20)
        CustomButton(
            text = stringResource(R.string.confirm),
            onClick = { onClick() },
            typeColor = TypeColor.Black,
            height = 81,
            radius = 24,
            style = MaterialTheme.typography.headlineMedium
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HSpacer(230)
        if (uiState is RegisterUiState.Error) {
            Text(
                stringResource(
                    when(uiState) {
                        is RegisterUiState.Error.ErrorPhone -> R.string.invalid_number
                        is RegisterUiState.Error.ErrorName -> R.string.enter_the_user_name
                        is RegisterUiState.Error.ErrorDoublePhone -> R.string.invalid_double_phone
                        is RegisterUiState.Error.ErrorRegister -> R.string.invalid_register
                    }
                ),
                color = Color(0xFFC4162D),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

    BackButton(
        toBack,
        color = Color(0xFFEFEFEF)
    )

    if (uiState is RegisterUiState.Success) {
        LaunchedEffect(Unit) {
            focusManager.clearFocus()
            keyboardController?.hide()
        }

        CustomPopup(
            profileViewModel,
            phone = "+ 7 ${formatPhoneNumber(phone)}",
            onChangeVisible = { viewModel.resetRegisterUiState() },
            sendCode = {
                viewModel.setWays(it)
                twoScreen()
            }
        )
    }
}