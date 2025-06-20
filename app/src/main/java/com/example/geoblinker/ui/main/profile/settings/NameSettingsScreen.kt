package com.example.geoblinker.ui.main.profile.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.geoblinker.R
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.CustomButton
import com.example.geoblinker.ui.NameTextField
import com.example.geoblinker.ui.TypeColor
import com.example.geoblinker.ui.main.viewmodel.DefaultStates
import com.example.geoblinker.ui.main.viewmodel.ProfileViewModel
import com.example.geoblinker.ui.theme.sdp

@Composable
fun NameSettingsScreen(
    viewModel: ProfileViewModel,
    toBack: () -> Unit
) {
    val name = viewModel.name
    val uiState = viewModel.uiState
    var value by rememberSaveable { mutableStateOf("") }

    fun onClick() {
        if (value.isEmpty())
            viewModel.inputErrorUiState()
        else {
            viewModel.updateName(value)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.resetUiState()
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            name,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
    Column(
        modifier = Modifier.requiredWidth(310.sdp()),
        verticalArrangement = Arrangement.Center
    ) {
        NameTextField(
            value = value,
            placeholder = stringResource(R.string.enter_new_name),
            onValueChange = {
                viewModel.resetUiState()
                value = it
            },
            onDone = { onClick() },
            isError = uiState is DefaultStates.Error.InputError,
            radius = 16,
            height = 65
        )
        Spacer(Modifier.height(15.sdp()))
        CustomButton(
            text = stringResource(R.string.change_name),
            onClick = { onClick() },
            typeColor = TypeColor.Green
        )
    }
    when (uiState) {
        is DefaultStates.Error.InputError ->
            Box(
                modifier = Modifier.fillMaxSize().offset(y = 100.sdp()),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.name_cannot_empty),
                    color = Color(0xFFC4162D),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        is DefaultStates.Error.ServerError ->
            Box(
                modifier = Modifier.fillMaxSize().offset(y = 100.sdp()),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.server_error),
                    color = Color(0xFFC4162D),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        is DefaultStates.Success ->
            Box(
                modifier = Modifier.fillMaxSize().offset(y = 100.sdp()),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.name_has_been_updated),
                    color = Color(0xFF12CD4A),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        is DefaultStates.Input -> {}
    }

    BackButton(
        onClick = toBack
    )
}