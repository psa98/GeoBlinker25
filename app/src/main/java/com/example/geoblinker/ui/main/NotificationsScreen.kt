package com.example.geoblinker.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.style.TextOverflow
import com.example.geoblinker.R
import com.example.geoblinker.TimeUtils
import com.example.geoblinker.data.News
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.CustomListPopup
import com.example.geoblinker.ui.greenGradient
import com.example.geoblinker.ui.theme.sdp

@Composable
fun NotificationsScreen(
    viewModel: DeviceViewModel,
    toBack: () -> Unit
) {
    val devices by viewModel.devices.collectAsState()
    val signals by viewModel.signals.collectAsState()
    val news by viewModel.news.collectAsState()
    val namesSignals = remember { mutableStateMapOf<String, String>() }
    val listNews = remember { mutableStateMapOf<Int, MutableList<News>>() }
    var keySort by remember { mutableIntStateOf(R.string.by_date_of_signal) }
    var keyFilter by remember { mutableIntStateOf(R.string.all_notifications) }
    var isShowSort by remember { mutableStateOf(false) }
    var isShowFilter by remember { mutableStateOf(false) }

    val sortedSignals = when(keySort) {
        R.string.by_date_of_signal -> signals.sortedByDescending { it.dateTime }
        R.string.by_signal_type -> signals.sortedBy { it.name }
        R.string.by_name -> signals.sortedBy { namesSignals[it.deviceId] }
        else -> signals.sortedByDescending { it.dateTime }
    }

    LaunchedEffect(signals, news) {
        signals.forEach { item ->
            if (!item.isSeen)
                viewModel.updateSignal(item.copy(isSeen = true))
        }
        news.forEach { item ->
            if (!item.isSeen)
                viewModel.updateNews(item.copy(isSeen = true))
        }
    }

    LaunchedEffect(devices) {
        devices.forEach {
            namesSignals[it.imei] = it.name
        }
    }

    LaunchedEffect(news.size) {
        val sortSignals = signals.sortedByDescending { it.dateTime }
        val sortNews = news.sortedByDescending { it.dateTime }
        var i = -1
        var j = 0
        while (i < sortSignals.size - 1 && j < sortNews.size) {
            if (sortSignals[i + 1].dateTime < sortNews[j].dateTime) {
                listNews.getOrPut(i, { mutableStateListOf() })
                listNews[i]!!.add(sortNews[j])
                j++
            }
            else {
                i++
            }
        }
        while (j < sortNews.size) {
            listNews.getOrPut(i, { mutableStateListOf() })
            listNews[i]!!.add(sortNews[j])
            j++
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
        Spacer(Modifier.height(38.sdp()))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .clickable { isShowSort = true },
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
            Row(
                modifier = Modifier
                    .clickable { isShowFilter = true },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(keyFilter),
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
            modifier = Modifier.width(330.sdp()),
            shape = RoundedCornerShape(10.sdp()),
            color = Color.White
        ) {
            LazyColumn(
                contentPadding = PaddingValues(15.sdp())
            ) {
                item {
                    if (keyFilter != R.string.signals_only) {
                        listNews.getOrDefault(-1, mutableListOf()).forEachIndexed { index, news ->
                            var isShowAll by remember { mutableStateOf(false) }
                            Button(
                                { isShowAll = !isShowAll },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        greenGradient,
                                        RoundedCornerShape(10.sdp())
                                    ),
                                shape = RoundedCornerShape(10.sdp()),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent
                                ),
                                contentPadding = PaddingValues(10.sdp())
                            ) {
                                Text(
                                    news.description,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = if (isShowAll) Int.MAX_VALUE else 2,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            if (index < listNews[-1]!!.size - 1 || sortedSignals.isNotEmpty())
                                Spacer(Modifier.height(15.sdp()))
                        }
                    }
                }
                itemsIndexed(sortedSignals) { index, item ->
                    if (keyFilter != R.string.news_promotions_only) {
                        Column {
                            if (namesSignals.getOrDefault(item.deviceId, "Ошибка").isNotEmpty())
                                Text(
                                    namesSignals.getOrDefault(item.deviceId, "Ошибка"),
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            else
                                Text(
                                    stringResource(R.string.an_unnamed_device),
                                    color = Color(0xFF737373),
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
                    }
                    if (keyFilter != R.string.signals_only) {
                        listNews.getOrDefault(index, mutableListOf()).forEachIndexed { index, news ->
                            var isShowAll by remember { mutableStateOf(false) }
                            if (index > 0 || keyFilter != R.string.news_promotions_only)
                                Spacer(Modifier.height(15.sdp()))
                            Button(
                                { isShowAll = !isShowAll },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        greenGradient,
                                        RoundedCornerShape(10.sdp())
                                    ),
                                shape = RoundedCornerShape(10.sdp()),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent
                                ),
                                contentPadding = PaddingValues(10.sdp())
                            ) {
                                Text(
                                    news.description,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = if (isShowAll) Int.MAX_VALUE else 2,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                    if (index < sortedSignals.size - 1)
                        Spacer(Modifier.height(15.sdp()))
                }
            }
        }
    }

    BackButton(
        toBack
    )

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

    if (isShowFilter) {
        CustomListPopup(
            R.string.show_notifications,
            listOf(R.string.all_notifications, R.string.signals_only, R.string.news_promotions_only),
            {
                keyFilter = it
                isShowFilter = false
            },
            {
                isShowFilter = it
            },
            alignment = Alignment.TopEnd
        )
    }
}