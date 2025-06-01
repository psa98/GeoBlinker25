package com.example.geoblinker.ui.main.profile.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.GreenMediumButton
import com.example.geoblinker.ui.NameTextField
import com.example.geoblinker.ui.main.viewmodel.ProfileViewModel
import com.example.geoblinker.ui.theme.sdp

@Composable
fun NameSettingsScreen(
    viewModel: ProfileViewModel,
    toBack: () -> Unit
) {
    val name by viewModel.name.collectAsState()
    var value by remember { mutableStateOf("") }
    var isShow by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    fun onClick() {
        if (value.isEmpty())
            isError = true
        else {
            viewModel.setName(value)
            isShow = true
        }
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
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        NameTextField(
            placeholder = "Введите новое имя",
            onValueChange = {
                isShow = false
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
            text = "Сменить имя",
            onClick = { onClick() },
            height = 65
        )
    }
    if (isShow) {
        Box(
            modifier = Modifier.fillMaxSize().offset(y = 100.sdp()),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Имя пользователя обновлено!",
                color = Color(0xFF12CD4A),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
    if (isError) {
        Box(
            modifier = Modifier.fillMaxSize().offset(y = 100.sdp()),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Имя не может быть пустым!",
                color = Color(0xFFC4162D),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

    BackButton(
        onClick = toBack
    )
}