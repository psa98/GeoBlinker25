package com.example.geoblinker.ui.main.binding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.example.geoblinker.ui.CustomButton
import com.example.geoblinker.ui.HSpacer
import com.example.geoblinker.ui.NameDeviceTextField
import com.example.geoblinker.ui.TypeColor
import com.example.geoblinker.ui.main.viewmodel.DefaultStates
import com.example.geoblinker.ui.main.viewmodel.DeviceViewModel
import com.example.geoblinker.ui.theme.ColorError
import com.example.geoblinker.ui.theme.sdp

@Composable
fun BindingTwoScreen(
    viewModel: DeviceViewModel,
    toDevice: () -> Unit,
    toBack: () -> Unit
) {
    val uiState = viewModel.uiState
    val device by viewModel.device.collectAsState()
    var name by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(uiState) {
        if (uiState is DefaultStates.Success) {
            viewModel.resetUiState()
            toDevice()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.new_device),
            style = MaterialTheme.typography.titleMedium
        )
        HSpacer(87)
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.check),
            contentDescription = null,
            modifier = Modifier.size(28.sdp()),
            tint = Color.Unspecified
        )
        HSpacer(15)
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "IMEI: ",
                color = Color(0xFF636363),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                device.imei,
                color = Color.Unspecified,
                style = MaterialTheme.typography.titleLarge
            )
        }
        HSpacer(60)
        Column(
            modifier = Modifier.requiredWidth(310.sdp())
        ) {
            NameDeviceTextField(
                name,
                stringResource(R.string.specify_a_name_for_the_new_device),
                {
                    viewModel.resetUiState()
                    name = it
                },
                onDone = { viewModel.insertDevice(name) }
            )
            HSpacer(15)
            CustomButton(
                text = stringResource(R.string.link),
                onClick = { viewModel.insertDevice(name) },
                typeColor = TypeColor.Green,
                height = 65,
                style = MaterialTheme.typography.headlineMedium
            )
        }
        HSpacer(20)
        if (uiState is DefaultStates.Error.ServerError) {
            Text(
                stringResource(R.string.server_error),
                color = ColorError,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

    BackButton(
        onClick = toBack
    )
}