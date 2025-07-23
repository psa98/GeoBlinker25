package com.example.geoblinker.ui.main.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
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
import androidx.compose.ui.text.style.TextOverflow
import com.example.geoblinker.R
import com.example.geoblinker.TimeUtils
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.CustomButton
import com.example.geoblinker.ui.CustomListPopup
import com.example.geoblinker.ui.TypeColor
import com.example.geoblinker.ui.main.viewmodel.DeviceViewModel
import com.example.geoblinker.ui.main.viewmodel.JournalViewModel
import com.example.geoblinker.ui.theme.sdp

@Composable
fun JournalSignalsScreen(
    viewModel: DeviceViewModel,
    journalViewModel: JournalViewModel,
    toBack: () -> Unit
) {
    val devices by viewModel.devices.collectAsState()
    val signals by viewModel.signals.collectAsState()
    val namesSignals = remember { mutableStateMapOf<String, String>() }
    var keySort by remember { mutableIntStateOf(R.string.by_date_of_signal) }
    var isShowSort by remember { mutableStateOf(false) }

    val sortedSignals = when(keySort) {
        R.string.by_date_of_signal -> signals.sortedByDescending { it.dateTime }
        R.string.by_signal_type -> signals.sortedBy { it.name }
        R.string.by_name -> signals.sortedBy { namesSignals[it.deviceId] }
        else -> signals.sortedByDescending { it.dateTime }
    }

    // Для управления Snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    // Обработка событий скачивания
    LaunchedEffect(viewModel) {
        journalViewModel.downloadEvent.collect { event ->
            when (event) {
                is JournalViewModel.DownloadEvent.Success -> {
                    snackbarHostState.showSnackbar(
                        message = "Журнал сохранён в папку 'Загрузки'",
                        duration = SnackbarDuration.Short
                    )
                }
                is JournalViewModel.DownloadEvent.Error -> {
                    snackbarHostState.showSnackbar(
                        message = "Ошибка: ${event.message}",
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }
    }

    LaunchedEffect(signals) {
        signals.forEach { item ->
            if (!item.isSeen)
                viewModel.updateSignal(item.copy(isSeen = true))
        }
    }

    LaunchedEffect(devices) {
        devices.forEach {
            namesSignals[it.id] = it.name.ifEmpty { it.imei }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.signal_log),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        if (signals.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Ваш журнал пока что пуст",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            Spacer(Modifier.height(38.sdp()))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isShowSort = true },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(15.sdp()))
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
            Spacer(Modifier.height(15.sdp()))
            Surface(
                modifier = Modifier.width(330.sdp()).padding(bottom = 120.sdp()),
                shape = RoundedCornerShape(10.sdp()),
                color = Color.White
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(15.sdp())
                ) {
                    itemsIndexed(sortedSignals) { index, item ->
                        Column {
                            Text(
                                namesSignals.getOrDefault(item.deviceId, "Ошибка"),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(Modifier.height(7.sdp()))
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
                            Spacer(Modifier.height(10.sdp()))
                            HorizontalDivider(
                                Modifier.fillMaxWidth(),
                                1.sdp(),
                                Color(0xFFDAD9D9)
                            )
                        }
                        if (index < sortedSignals.size - 1)
                            Spacer(Modifier.height(15.sdp()))
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row {
            BackButton(
                toBack,
                notPosition = true
            )
            if (signals.isNotEmpty()) {
                Spacer(Modifier.width(20.sdp()))
                CustomButton(
                    modifier = Modifier.width(205.sdp()),
                    text = "Скачать журнал",
                    onClick = { journalViewModel.downloadJournal(devices, signals) },
                    typeColor = TypeColor.Black,
                    rightIcon = ImageVector.vectorResource(R.drawable.download),
                    iconSize = 16
                )
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        SnackbarHost(
            hostState = snackbarHostState
        ) { data ->
            Surface(
                shape = RoundedCornerShape(10.sdp()),
                color = Color(0xFF222221),
                shadowElevation = 4.sdp()
            ) {
                Text(
                    data.visuals.message,
                    modifier = Modifier.padding(10.sdp()),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }

    if (isShowSort) {
        CustomListPopup(
            R.string.sort_signals,
            listOf(R.string.by_date_of_signal, R.string.by_signal_type, R.string.by_name),
            {
                keySort = it
                isShowSort = false
            },
            {
                isShowSort = it
            }
        )
    }
}