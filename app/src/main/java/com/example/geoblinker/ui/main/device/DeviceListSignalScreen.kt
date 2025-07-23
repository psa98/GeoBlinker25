package com.example.geoblinker.ui.main.device

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.example.geoblinker.R
import com.example.geoblinker.TimeUtils
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.CustomListPopup
import com.example.geoblinker.ui.main.viewmodel.DeviceViewModel
import com.example.geoblinker.ui.theme.sdp

@Composable
fun DeviceListSignalScreen(
    viewModel: DeviceViewModel,
    toBack: () -> Unit
) {
    val device by viewModel.device.collectAsState()
    val signalsDevice by viewModel.signalsDevice.collectAsState()
    var keySort by remember { mutableIntStateOf(R.string.by_date_of_signal) }
    var isShow by remember { mutableStateOf(false) }

    val sortedSignalsDevice = when(keySort) {
        R.string.by_date_of_signal -> signalsDevice.sortedByDescending { it.dateTime }
        R.string.by_signal_type -> signalsDevice.sortedBy { it.name }
        else -> signalsDevice.sortedByDescending { it.dateTime }
    }

    LaunchedEffect(signalsDevice) {
        signalsDevice.forEach { item ->
            if (!item.isSeen)
                viewModel.updateSignal(item.copy(isSeen = true))
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (device.name.isNotEmpty()) {
            Text(
                device.name,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        } else {
            Text(
                stringResource(R.string.an_unnamed_device),
                color = Color(0xFF737373),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Spacer(Modifier.height(10.sdp()))
        Text(
            stringResource(R.string.signal_log),
            color = Color(0xFF737373),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(25.sdp()))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Row(
                modifier = Modifier
                    .clickable { isShow = true },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(keySort),
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.width(16.sdp()))
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.open_list),
                    contentDescription = null,
                    modifier = Modifier.size(11.sdp()),
                    tint = Color.Unspecified
                )
            }
        }
        Spacer(Modifier.height(15.sdp()))
        Surface(
            modifier = Modifier.width(330.sdp()).padding(bottom = 120.sdp()),
            shape = RoundedCornerShape(10.sdp()),
            color = Color.White
        ) {
            LazyColumn(
                contentPadding = PaddingValues(15.sdp())
            ) {
                itemsIndexed(sortedSignalsDevice) { index, item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            item.name,
                            style = MaterialTheme.typography.labelMedium
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "${TimeUtils.formatToLocalTimeTime(item.dateTime)} ",
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                TimeUtils.formatToLocalTimeDate(item.dateTime),
                                color = Color(0xFF737373),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                    if (index < signalsDevice.size - 1)
                        Spacer(Modifier.height(15.sdp()))
                }
            }
        }
    }

    BackButton(
        toBack
    )

    if (isShow) {
        CustomListPopup(
            R.string.sort_signals,
            listOf(R.string.by_date_of_signal, R.string.by_signal_type),
            {
                keySort = it
                isShow = false
            },
            {
                isShow = it
            }
        )
    }
}