package com.example.geoblinker.ui.main.binding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.example.geoblinker.R
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.GreenMediumButton
import com.example.geoblinker.ui.NameDeviceTextField
import com.example.geoblinker.ui.theme.sdp

@Composable
fun BindingTwoScreen(
    imei: String,
    toDevice: (String) -> Unit,
    toBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column(
                modifier = Modifier.width(310.sdp()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(R.string.new_device),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(87.sdp()))
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.check),
                    contentDescription = null,
                    modifier = Modifier.size(28.sdp()),
                    tint = Color.Unspecified
                )
                Spacer(Modifier.height(15.sdp()))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "${stringResource(R.string.imei)}: ",
                        color = Color(0xFF636363),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        imei,
                        color = Color.Unspecified,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Spacer(Modifier.height(60.sdp()))
                NameDeviceTextField(
                    name,
                    stringResource(R.string.specify_a_name_for_the_new_device),
                    { name = it },
                    onDone = { toDevice(name) }
                )
                Spacer(Modifier.height(15.sdp()))
                GreenMediumButton(
                    text = stringResource(R.string.link),
                    onClick = { toDevice(name) },
                    height = 65,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(Modifier.height(115.sdp()))
            }
        }
    }

    BackButton(
        onClick = toBack
    )
}