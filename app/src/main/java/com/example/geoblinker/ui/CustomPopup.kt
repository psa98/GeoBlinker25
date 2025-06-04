package com.example.geoblinker.ui

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.round
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import androidx.compose.ui.zIndex
import com.example.geoblinker.R
import com.example.geoblinker.TimeUtils
import com.example.geoblinker.data.Device
import com.example.geoblinker.ui.main.MainScreen
import com.example.geoblinker.ui.main.viewmodel.ProfileViewModel
import com.example.geoblinker.ui.theme.GeoBlinkerTheme
import com.example.geoblinker.ui.theme.sdp

@Composable
fun CustomPopup(
    viewModel: ProfileViewModel,
    phone: String,
    onChangeVisible: (Boolean) -> Unit,
    sendCode: (List<String>) -> Unit
) {
    var isEnterEmail by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var checkedChoice by remember { mutableStateOf(false) }

    val items = remember { mutableStateListOf(
        WayConfirmationCode("Telegram"),
        WayConfirmationCode("WhatsApp"),
        WayConfirmationCode("SMS"),
        WayConfirmationCode("Email")
    ) }
    val stateWays = remember { mutableStateListOf<Boolean>().apply {
        addAll(items.map { it.checked })
    } }
    var draggedIndex by remember { mutableIntStateOf(-1) }
    // Смещение для анимации
    var draggedOffset by remember { mutableStateOf(Offset.Zero) }

    Dialog(
        {},
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .padding(0.sdp())
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .width(310.sdp())
                    .alpha(if (isEnterEmail) 0f else 1f),
                shape = MaterialTheme.shapes.large,
                color = Color.White,
                shadowElevation = 2.sdp(),
                border = BorderStroke(1.sdp(), Color(0xFFBEBEBE))
            ) {
                Column(
                    modifier = Modifier
                        .padding(30.sdp())
                    //.verticalScroll(rememberScrollState()),
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.sms),
                        contentDescription = null,
                        modifier = Modifier.size(28.sdp()),
                        tint = Color.Unspecified
                    )
                    Spacer(Modifier.height(20.sdp()))
                    Text(
                        stringResource(R.string.get_the_confirmation_code),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(12.sdp()))
                    Text(
                        phone,
                        color = Color(0xFF999696),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(Modifier.height(25.sdp()))
                    //Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    items.forEachIndexed { index, draggableItem ->
                        val currentItem = draggableItem.text
                        val offsetY by animateOffsetAsState(
                            if (draggedIndex == index) draggedOffset else Offset.Zero,
                            animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                            label = ""
                        )
                        val focusManager = LocalFocusManager.current

                        Box(
                            modifier = Modifier
                                .padding(bottom = 5.sdp())
                                .zIndex(if (draggedIndex == index) 1f else 0f) // Поднимает элемент над другими
                                .graphicsLayer {
                                    if (draggedIndex == index) {
                                        // Увеличиваем элемент на 10%
                                        scaleX = 1.1f
                                        scaleY = 1.1f

                                        // Центр масштабирования — середина элемента
                                        transformOrigin = TransformOrigin(0.5f, 0.5f)

                                        // Сдвигаем элемент по Y
                                        //translationY = offsetY
                                    } else {
                                        scaleX = 1f
                                        scaleY = 1f
                                    }
                                }
                                .offset {
                                    // Физически перемещаем элемент
                                    offsetY.round()
                                }
                        ) {
                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                color = Color.Unspecified,
                                border = BorderStroke(1.sdp(), Color(0xFFBEBEBE))
                            ) {
                                if (draggableItem.text == "Email") {
                                    Row(
                                        modifier = Modifier
                                            .padding(10.sdp())
                                            .height(40.sdp())
                                            .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Spacer(Modifier.width(72.sdp()))
                                        Text(
                                            email,
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable { isEnterEmail = true },
                                            overflow = TextOverflow.Ellipsis,
                                            textDecoration = TextDecoration.Underline,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Spacer(Modifier.width(52.sdp()))
                                    }
                                }

                                Row(
                                    modifier = Modifier
                                        .padding(10.sdp())
                                        .height(40.sdp())
                                        .fillMaxWidth()
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }) {
                                            // Обработка клика вне Switch
                                            focusManager.clearFocus()
                                        },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.burger),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(16.sdp(), 16.sdp())
                                            .aspectRatio(1f)
                                            .pointerInput(Unit) {
                                                detectDragGestures(
                                                    onDragStart = {
                                                        draggedIndex = index
                                                        draggedOffset = Offset.Zero
                                                    },
                                                    onDrag = { change, dragAmount ->
                                                        change.consume()
                                                        draggedOffset += Offset(
                                                            0f,
                                                            dragAmount.y
                                                        )

                                                        val targetIndex =
                                                            (draggedIndex + round(draggedOffset.y / 100))
                                                        if (targetIndex in 0 until 4 && targetIndex != draggedIndex) {
                                                            val item = items.removeAt(draggedIndex)
                                                            items.add(targetIndex, item)
                                                            stateWays[draggedIndex] = stateWays[targetIndex]
                                                            stateWays[targetIndex] = item.checked
                                                            draggedIndex = targetIndex
                                                            draggedOffset = Offset.Zero
                                                        }
                                                    },
                                                    onDragEnd = {
                                                        draggedIndex = -1
                                                        draggedOffset = Offset.Zero
                                                    }
                                                )
                                            },
                                        tint = Color.Unspecified
                                    )
                                    Spacer(Modifier.width(9.sdp()))
                                    Text(
                                        draggableItem.text,
                                        modifier = Modifier.weight(1f),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Switch(
                                        stateWays[index],
                                        {
                                            items[index].checked = it
                                            stateWays[index] = it

                                            if (draggableItem.text == "Email") {
                                                isEnterEmail = it
                                                email = ""
                                            }
                                        },
                                        modifier = Modifier.size(40.sdp(), 20.sdp()),
                                        colors = SwitchDefaults.colors(
                                            uncheckedThumbColor = Color(0xFFBEBEBE),
                                            uncheckedTrackColor = Color.Unspecified,
                                            uncheckedBorderColor = Color(0xFFBEBEBE),
                                            checkedThumbColor = Color(0xFF12CD4A),
                                            checkedTrackColor = Color.Unspecified,
                                            checkedBorderColor = Color(0xFFBEBEBE),
                                        )
                                    )
                                }
                            }
                        }
                    }
                    //}
                    Spacer(Modifier.height(20.sdp()))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            stringResource(R.string.remember_my_choice),
                            color = Color(0xFF636363),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Switch(
                            checked = checkedChoice,
                            { checkedChoice = it },
                            modifier = Modifier.size(40.sdp(), 21.sdp()),
                            colors = SwitchDefaults.colors(
                                uncheckedThumbColor = Color(0xFFBEBEBE),
                                uncheckedTrackColor = Color.Unspecified,
                                uncheckedBorderColor = Color(0xFFBEBEBE),
                                checkedThumbColor = Color(0xFF212120),
                                checkedTrackColor = Color.Unspecified,
                                checkedBorderColor = Color(0xFFBEBEBE),
                            )
                        )
                    }
                    Spacer(Modifier.height(25.sdp()))
                    GreenButton(
                        text = stringResource(R.string.send_the_code),
                        onClick = {
                            val ways = emptyList<String>().toMutableList()
                            items.forEach { draggableItem ->
                                if (draggableItem.checked) {
                                    ways += draggableItem.text
                                }
                            }
                            if (ways.isNotEmpty()) {
                                if (checkedChoice)
                                    viewModel.setWaysConfirmationCode(items)
                                sendCode(ways)
                            }
                        }
                    )
                    Spacer(Modifier.height(20.sdp()))
                    Text(
                        stringResource(R.string.send_to_another_number),
                        modifier = Modifier.clickable { onChangeVisible(false) },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }

    /*
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BackWhiteButton({ onChangeVisible(false) })
        Spacer(Modifier.height(24.sdp()))
    }
     */
    if (isEnterEmail) {
        CustomEmailPopup(
            email,
            {
                isEnterEmail = false
                email = it
            },
            {
                isEnterEmail = false
            }
        )
    }
}

@Composable
fun CustomEmptyDevicesPopup(
    onChangeVisible: (Boolean) -> Unit,
    toBindingScreen: () -> Unit
) {
    Dialog(
        onDismissRequest = { onChangeVisible(false) },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.2f))
                .clickable { onChangeVisible(false) },
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier.width(310.sdp()),
                shape = MaterialTheme.shapes.large,
                color = Color.White,
                shadowElevation = 2.sdp()
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 30.sdp(), horizontal = 25.sdp()),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.comment_info),
                        contentDescription = null,
                        modifier = Modifier.size(28.sdp()),
                        tint = Color.Unspecified
                    )
                    Spacer(Modifier.height(21.sdp()))
                    Text(
                        stringResource(R.string.ask_add_new_device),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(Modifier.height(53.sdp()))
                    GreenMediumButton(
                        modifier = Modifier.width(260.sdp()),
                        icon = R.drawable.plus,
                        text = stringResource(R.string.link_device),
                        onClick = toBindingScreen
                    )
                    Spacer(Modifier.height(20.sdp()))
                    Text(
                        stringResource(R.string.problems_with_linking),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun CustomListPopup(
    @StringRes label: Int,
    listLabels: List<Int>,
    onClick: (Int) -> Unit,
    changeIsShow: (Boolean) -> Unit,
    alignment: Alignment = Alignment.TopStart
) {
    Popup(
        alignment = alignment,
        onDismissRequest = { changeIsShow(false) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.2f))
                .padding(start = 15.sdp(), top = 96.sdp(), end = 15.sdp())
                .clickable { changeIsShow(false) },
            contentAlignment = alignment
        ) {
            Surface(
                modifier = Modifier.width(246.sdp()),
                shape = MaterialTheme.shapes.large,
                color = Color.White,
                shadowElevation = 2.sdp()
            ) {
                Column(
                    modifier = Modifier.padding(
                        start = 20.sdp(),
                        top = 26.sdp(),
                        bottom = 37.sdp()
                    )
                ) {
                    Text(
                        stringResource(label),
                        color = Color(0xFF747474),
                        style = MaterialTheme.typography.bodySmall
                    )
                    listLabels.forEach { item ->
                        Spacer(Modifier.height(20.sdp()))
                        Text(
                            stringResource(item),
                            modifier = Modifier.clickable {
                                onClick(item)
                            },
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomDiagnosisPopup(
    typeStatus: Device.TypeStatus,
    onChangeStatus: (Device.TypeStatus) -> Unit,
    onChangeVisible: (Boolean) -> Unit,
    breakdowns: List<Long> = emptyList()
) {
    Dialog(
        { onChangeVisible(false) },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.2f))
                .clickable { onChangeVisible(false) },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(
                    Modifier
                        .size(330.sdp(), 160.sdp())
                        .clickable { onChangeVisible(false) })
                Surface(
                    modifier = Modifier.width(330.sdp()),
                    shape = RoundedCornerShape(10.sdp()),
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier.padding(15.sdp())
                    ) {
                        Column {
                            Text(
                                "Прогноз поломок:",
                                color = Color(0xFF737373),
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                "Поломок не обнаружено",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                        HorizontalDivider(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 15.sdp()),
                            1.sdp(),
                            Color(0xFFDAD9D9).copy(alpha = 0.5f)
                        )
                        Column {
                            Text(
                                "Рекомендации по обслуживанию:",
                                color = Color(0xFF737373),
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                "Нет рекомендаций",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                        HorizontalDivider(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 15.sdp()),
                            1.sdp(),
                            Color(0xFFDAD9D9).copy(alpha = 0.5f)
                        )
                        Column {
                            Text(
                                "Критические неисправности:",
                                color = Color(0xFF737373),
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                "Критических неисправностей не обнаружено",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                        HorizontalDivider(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 15.sdp()),
                            1.sdp(),
                            Color(0xFFDAD9D9).copy(alpha = 0.5f)
                        )
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    typeStatus == Device.TypeStatus.Available,
                                    { onChangeStatus(Device.TypeStatus.Available) },
                                    modifier = Modifier.size(24.sdp()),
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Color(0xFF12CD4A)
                                    )
                                )
                                Spacer(Modifier.width(15.sdp()))
                                Text(
                                    stringResource(R.string.available),
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    typeStatus == Device.TypeStatus.Ready,
                                    { onChangeStatus(Device.TypeStatus.Ready) },
                                    modifier = Modifier.size(24.sdp()),
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Color(0xFF12CD4A)
                                    )
                                )
                                Spacer(Modifier.width(15.sdp()))
                                Text(
                                    stringResource(R.string.ready),
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    typeStatus == Device.TypeStatus.RequiresRepair,
                                    { onChangeStatus(Device.TypeStatus.RequiresRepair) },
                                    modifier = Modifier.size(24.sdp()),
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Color(0xFF12CD4A)
                                    )
                                )
                                Spacer(Modifier.width(15.sdp()))
                                Text(
                                    stringResource(R.string.requires_repair),
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                        Spacer(Modifier.height(15.sdp()))
                        GreenMediumButton(
                            text ="Обратиться в сервис",
                            onClick = {}
                        )
                    }
                }
                Spacer(
                    Modifier
                        .size(330.sdp(), 60.sdp())
                        .clickable { onChangeVisible(false) })
            }

            items(breakdowns) { item ->
                Surface(
                    modifier = Modifier.width(310.sdp()),
                    shape = RoundedCornerShape(10.sdp()),
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier.padding(12.sdp())
                    ) {
                        Text(
                            "Вышел из строя механизм затвора дверей. Был проведён ремонт в тех. сервисе X",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        HorizontalDivider(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.sdp()),
                            1.sdp(),
                            Color(0xFFDAD9D9).copy(alpha = 0.5f)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Дата:",
                                color = Color(0xFF737373),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                TimeUtils.formatToLocalTime(item),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
                Spacer(
                    Modifier
                        .size(330.sdp(), 12.sdp())
                        .clickable { onChangeVisible(false) })
            }

            item {
                Spacer(
                    Modifier
                        .size(330.sdp(), 100.sdp())
                        .clickable { onChangeVisible(false) })
            }
        }
    }
}

@Composable
fun CustomCommentsPopup(
    onChangeVisible: (Boolean) -> Unit,
    comments: List<String> = emptyList()
) {
    Dialog(
        { onChangeVisible(false) },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.2f))
                .clickable { onChangeVisible(false) },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(
                    Modifier
                        .size(330.sdp(), 200.sdp())
                        .clickable { onChangeVisible(false) })
            }
            items(8) {
                Surface(
                    modifier = Modifier.width(330.sdp()),
                    shape = RoundedCornerShape(10.sdp()),
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier.padding(15.sdp())
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            for (i in 1..5) {
                                Icon(
                                    imageVector = Icons.Filled.StarRate,
                                    contentDescription = null,
                                    tint = Color.Yellow
                                )
                            }
                        }
                        HorizontalDivider(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 15.sdp()),
                            1.sdp(),
                            Color(0xFFDAD9D9).copy(alpha = 0.5f)
                        )
                        Text(
                            "Отличный сервис. Всё понравилось. Однозначно 5 звёзд из 5",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                Spacer(
                    Modifier
                        .size(330.sdp(), 15.sdp())
                        .clickable { onChangeVisible(false) })
            }

            item {
                Spacer(
                    Modifier
                        .size(330.sdp(), 100.sdp())
                        .clickable { onChangeVisible(false) })
            }
        }
    }
}

@Composable
fun CustomLinkEmailPopup(
    toLinkEmail: () -> Unit,
    cancellation: () -> Unit
) {
    Dialog(
        {},
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.2f))
                .clickable { cancellation() },
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier.width(310.sdp()),
                shape = RoundedCornerShape(16.sdp()),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = 25.sdp(),
                        vertical = 30.sdp()
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.envelope),
                        contentDescription = null,
                        modifier = Modifier.size(28.sdp()),
                        tint = Color.Unspecified
                    )
                    Spacer(Modifier.height(20.sdp()))
                    Text(
                        stringResource(R.string.set_up_email_description),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(Modifier.height(28.sdp()))
                    CustomButton(
                        text = stringResource(R.string.set_up_email),
                        onClick = toLinkEmail,
                        typeColor = TypeColor.Green,
                        height = 55
                    )
                    Spacer(Modifier.height(10.sdp()))
                    CustomButton(
                        text = stringResource(R.string.cancellation),
                        onClick = cancellation,
                        typeColor = TypeColor.White,
                        height = 55
                    )
                }
            }
        }
    }
}

fun round(fl: Float): Int {
    if (fl >= 1)
        return 1
    if (fl <= -1)
        return -1
    return 0
}

@Composable
fun FullScreenBox() {
    // Получаем размеры экрана
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.sdp()
    val screenHeight = configuration.screenHeightDp.sdp()

    Box(
        modifier = Modifier
            .graphicsLayer { clip = false }
            .layout { measurable, _ ->
                // Игнорируем родительские ограничения
                val placeable = measurable.measure(
                    Constraints(
                        minWidth = 0,
                        maxWidth = Constraints.Infinity,
                        minHeight = 0,
                        maxHeight = Constraints.Infinity
                    )
                )
                layout(placeable.width, placeable.height) {
                    placeable.place(0, 0)
                }
            }
            .size(screenWidth, screenHeight) // Явно задаем размер экрана
            .background(Color.Black.copy(alpha = 0.2f))
    )
}

@Composable
fun CustomEmailPopup(
    email: String,
    onDone: (String) -> Unit,
    onCancel: () -> Unit,
    isError: Boolean = false
) {
    var value by remember { mutableStateOf(email) }

    Dialog({}) {
        Surface(
            modifier = Modifier.width(350.sdp()),
            shape = MaterialTheme.shapes.large,
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(30.sdp()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(R.string.specify_the_email),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(40.sdp()))
                EmailTextField(
                    email = value,
                    placeholder = stringResource(R.string.your_email),
                    onValueChange = { value = it },
                    onDone = { onDone(value) },
                    isError = isError
                )
                Spacer(Modifier.height(25.sdp()))
                GreenButton(
                    text = stringResource(R.string.confirm),
                    onClick = { onDone(value) }
                )
                Spacer(Modifier.height(10.sdp()))
                WhiteButton(
                    text = stringResource(R.string.cancellation),
                    onClick = onCancel
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRowSwitch() {
    GeoBlinkerTheme {
        Row(
            modifier = Modifier
                .padding(10.sdp())
                .height(40.sdp())
                .fillMaxWidth()
            ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.burger),
                contentDescription = null,
                modifier = Modifier
                    .size(16.sdp(), 16.sdp())
                    .aspectRatio(1f)
                ,
                tint = Color.Unspecified
            )
            Spacer(Modifier.width(9.sdp()))
            Text(
                "Telegram",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                false,
                {},
                modifier = Modifier.fillMaxHeight(),
                colors = SwitchDefaults.colors(
                    uncheckedThumbColor = Color(0xFFBEBEBE),
                    uncheckedTrackColor = Color.Unspecified,
                    uncheckedBorderColor = Color(0xFFBEBEBE),
                    checkedThumbColor = Color(0xFF12CD4A),
                    checkedTrackColor = Color.Unspecified,
                    checkedBorderColor = Color(0xFFBEBEBE),
                )
            )
        }
    }
}