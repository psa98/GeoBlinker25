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
import com.example.geoblinker.ui.theme.GeoBlinkerTheme
import com.example.geoblinker.ui.theme.hdp

data class DraggableItem(
    val text: String,
    val isEmail: Boolean = false
)

@Composable
fun CustomPopup(
    phone: String,
    onChangeVisible: (Boolean) -> Unit,
    sendCode: (List<String>) -> Unit
) {
    val stringTelegram = stringResource(R.string.telegram)
    val stringWhatsApp = stringResource(R.string.whatsapp)
    val stringSMS = stringResource(R.string.sms)
    val stringEmail = stringResource(R.string.email).capitalize()
    var isEnterEmail by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var checkedChoice by remember { mutableStateOf(false) }

    val items = remember { mutableStateListOf(
        DraggableItem(stringTelegram),
        DraggableItem(stringWhatsApp),
        DraggableItem(stringSMS),
        DraggableItem(stringEmail, true)
    ) }
    val switchStates = remember {
        mutableStateMapOf(
            stringTelegram to false,
            stringWhatsApp to false,
            stringSMS to false,
            stringEmail to false
        )
    }
    var draggedIndex by remember { mutableIntStateOf(-1) }
    // Смещение для анимации
    var draggedOffset by remember { mutableStateOf(Offset.Zero) }

    LaunchedEffect(draggedIndex) {
        Log.d("DraggedIndex", draggedIndex.toString())
    }

    FullScreenBox()
    Dialog(
        {},
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .width(310.hdp())
                .alpha(if (isEnterEmail) 0f else 1f),
            shape = MaterialTheme.shapes.large,
            color = Color.White,
            shadowElevation = 2.hdp(),
            border = BorderStroke(1.hdp(), Color(0xFFBEBEBE))
        ) {
            Column(
                modifier = Modifier
                    .padding(30.hdp())
                    //.verticalScroll(rememberScrollState()),
                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.sms),
                    contentDescription = null,
                    modifier = Modifier.size(28.hdp()),
                    tint = Color.Unspecified
                )
                Spacer(Modifier.height(20.hdp()))
                Text(
                    stringResource(R.string.get_the_confirmation_code),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(12.hdp()))
                Text(
                    phone,
                    color = Color(0xFF999696),
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(Modifier.height(25.hdp()))
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
                                .padding(bottom = 5.hdp())
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
                                border = BorderStroke(1.hdp(), Color(0xFFBEBEBE))
                            ) {
                                if (draggableItem.isEmail) {
                                    Row(
                                        modifier = Modifier
                                            .padding(10.hdp())
                                            .height(40.hdp())
                                            .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Spacer(Modifier.width(72.hdp()))
                                        Text(
                                            email,
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable { isEnterEmail = true },
                                            overflow = TextOverflow.Ellipsis,
                                            textDecoration = TextDecoration.Underline,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Spacer(Modifier.width(52.hdp()))
                                    }
                                }

                                Row(
                                    modifier = Modifier
                                        .padding(10.hdp())
                                        .height(40.hdp())
                                        .fillMaxWidth()
                                        .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
                                            // Обработка клика вне Switch
                                            focusManager.clearFocus()
                                        }
                                    ,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.burger),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(16.hdp(), 16.hdp())
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
                                                        Log.d(
                                                            "DragItem",
                                                            "${draggedOffset.y} ${draggedOffset.y / 100} $targetIndex $draggedIndex ${draggableItem.text}"
                                                        )
                                                        if (targetIndex in 0 until 4 && targetIndex != draggedIndex) {
                                                            val item = items.removeAt(draggedIndex)
                                                            items.add(targetIndex, item)
                                                            draggedIndex = targetIndex
                                                            draggedOffset = Offset.Zero
                                                        }
                                                    },
                                                    onDragEnd = {
                                                        draggedIndex = -1
                                                        draggedOffset = Offset.Zero
                                                    }
                                                )
                                            }
                                        ,
                                        tint = Color.Unspecified
                                    )
                                    Spacer(Modifier.width(9.hdp()))
                                    Text(
                                        draggableItem.text,
                                        modifier = Modifier.weight(1f),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Switch(
                                        switchStates[currentItem]!!,
                                        {
                                            switchStates[currentItem] = it

                                            if (draggableItem.isEmail) {
                                                isEnterEmail = it
                                                email = ""
                                            }
                                        },
                                        modifier = Modifier.size(40.hdp(), 20.hdp()),
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
                Spacer(Modifier.height(20.hdp()))
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
                        modifier = Modifier.size(40.hdp(), 21.hdp()),
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
                Spacer(Modifier.height(25.hdp()))
                GreenButton(
                    text = stringResource(R.string.send_the_code),
                    onClick = {
                        val ways = emptyList<String>().toMutableList()
                        items.forEach { draggableItem ->
                            if (switchStates[draggableItem.text]!!) {
                                ways += draggableItem.text
                            }
                        }
                        if (ways.isNotEmpty())
                            sendCode(ways)
                    }
                )
                Spacer(Modifier.height(20.hdp()))
                Text(
                    stringResource(R.string.send_to_another_number),
                    modifier = Modifier.clickable { onChangeVisible(false) },
                    style = MaterialTheme.typography.bodyLarge
                )
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
        Spacer(Modifier.height(24.hdp()))
    }
     */
    if (isEnterEmail) {
        CustomEmailPopup(
            email,
            {
                isEnterEmail = false
                email = it
                if (email.isEmpty())
                    switchStates[stringEmail] = false
            },
            {
                isEnterEmail = false
                if (email.isEmpty())
                    switchStates[stringEmail] = false
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
                modifier = Modifier.width(310.hdp()),
                shape = MaterialTheme.shapes.large,
                color = Color.White,
                shadowElevation = 2.hdp()
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 30.hdp(), horizontal = 25.hdp()),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.comment_info),
                        contentDescription = null,
                        modifier = Modifier.size(28.hdp()),
                        tint = Color.Unspecified
                    )
                    Spacer(Modifier.height(21.hdp()))
                    Text(
                        stringResource(R.string.ask_add_new_device),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(Modifier.height(53.hdp()))
                    GreenMediumButton(
                        modifier = Modifier.width(260.hdp()),
                        icon = R.drawable.plus,
                        text = stringResource(R.string.link_device),
                        onClick = toBindingScreen
                    )
                    Spacer(Modifier.height(20.hdp()))
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
                .padding(start = 15.hdp(), top = 96.hdp(), end = 15.hdp())
                .clickable { changeIsShow(false) },
            contentAlignment = alignment
        ) {
            Surface(
                modifier = Modifier.width(246.hdp()),
                shape = MaterialTheme.shapes.large,
                color = Color.White,
                shadowElevation = 2.hdp()
            ) {
                Column(
                    modifier = Modifier.padding(
                        start = 20.hdp(),
                        top = 26.hdp(),
                        bottom = 37.hdp()
                    )
                ) {
                    Text(
                        stringResource(label),
                        color = Color(0xFF747474),
                        style = MaterialTheme.typography.bodySmall
                    )
                    listLabels.forEach { item ->
                        Spacer(Modifier.height(20.hdp()))
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
                Spacer(Modifier.size(330.hdp(), 200.hdp()).clickable { onChangeVisible(false) })
                Surface(
                    modifier = Modifier.width(330.hdp()),
                    shape = RoundedCornerShape(10.hdp()),
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier.padding(15.hdp())
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
                            Modifier.fillMaxWidth().padding(vertical = 15.hdp()),
                            1.hdp(),
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
                            Modifier.fillMaxWidth().padding(vertical = 15.hdp()),
                            1.hdp(),
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
                        Spacer(Modifier.height(15.hdp()))
                        GreenMediumButton(
                            text ="Обратиться в сервис",
                            onClick = {}
                        )
                    }
                }
                Spacer(Modifier.size(330.hdp(), 260.hdp()).clickable { onChangeVisible(false) })
            }

            items(breakdowns) { item ->
                Surface(
                    modifier = Modifier.width(310.hdp()),
                    shape = RoundedCornerShape(10.hdp()),
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier.padding(12.hdp())
                    ) {
                        Text(
                            "Вышел из строя механизм затвора дверей. Был проведён ремонт в тех. сервисе X",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        HorizontalDivider(
                            Modifier.fillMaxWidth().padding(vertical = 12.hdp()),
                            1.hdp(),
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
                Spacer(Modifier.size(330.hdp(), 12.hdp()).clickable { onChangeVisible(false) })
            }

            item {
                Spacer(Modifier.size(330.hdp(), 100.hdp()).clickable { onChangeVisible(false) })
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
                Spacer(Modifier.size(330.hdp(), 200.hdp()).clickable { onChangeVisible(false) })
            }
            items(8) {
                Surface(
                    modifier = Modifier.width(330.hdp()),
                    shape = RoundedCornerShape(10.hdp()),
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier.padding(15.hdp())
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
                            Modifier.fillMaxWidth().padding(vertical = 15.hdp()),
                            1.hdp(),
                            Color(0xFFDAD9D9).copy(alpha = 0.5f)
                        )
                        Text(
                            "Отличный сервис. Всё понравилось. Однозначно 5 звёзд из 5",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                Spacer(Modifier.size(330.hdp(), 15.hdp()).clickable { onChangeVisible(false) })
            }

            item {
                Spacer(Modifier.size(330.hdp(), 100.hdp()).clickable { onChangeVisible(false) })
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
    val screenWidth = configuration.screenWidthDp.hdp()
    val screenHeight = configuration.screenHeightDp.hdp()

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
            modifier = Modifier.width(350.hdp()),
            shape = MaterialTheme.shapes.large,
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(30.hdp()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(R.string.specify_the_email),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(40.hdp()))
                EmailTextField(
                    email = value,
                    placeholder = stringResource(R.string.your_email),
                    onValueChange = { value = it },
                    onDone = { onDone(value) },
                    isError = isError
                )
                Spacer(Modifier.height(25.hdp()))
                GreenButton(
                    text = stringResource(R.string.confirm),
                    onClick = { onDone(value) }
                )
                Spacer(Modifier.height(10.hdp()))
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
                .padding(10.hdp())
                .height(40.hdp())
                .fillMaxWidth()
            ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.burger),
                contentDescription = null,
                modifier = Modifier
                    .size(16.hdp(), 16.hdp())
                    .aspectRatio(1f)
                ,
                tint = Color.Unspecified
            )
            Spacer(Modifier.width(9.hdp()))
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