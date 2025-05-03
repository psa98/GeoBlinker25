package com.example.geoblinker.ui

import android.util.Log
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import com.example.geoblinker.R

data class DraggableItem(
    val text: String,
    val isEmail: Boolean = false
)

@Composable
fun CustomPopup(
    phone: String,
    onChangeVisible: (Boolean) -> Unit,
    sendCode: () -> Unit,
    addWay: (String) -> Unit
) {
    val stringTelegram = stringResource(R.string.telegram)
    val stringWhatsApp = stringResource(R.string.whatsapp)
    val stringSMS = stringResource(R.string.sms)
    val stringEmail = stringResource(R.string.email)
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
    Dialog({}) {
        Surface(
            modifier = Modifier
                .width(360.dp)
                .alpha(if (isEnterEmail) 0f else 1f),
            shape = MaterialTheme.shapes.large,
            color = Color.White,
            shadowElevation = 2.dp,
            border = BorderStroke(1.dp, Color(0xFFBEBEBE))
        ) {
            Column(
                modifier = Modifier
                    .padding(30.dp)
                    //.verticalScroll(rememberScrollState()),
                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.sms),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
                Spacer(Modifier.height(20.dp))
                Text(
                    stringResource(R.string.get_the_confirmation_code),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    phone,
                    color = Color(0xFF999696),
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(Modifier.height(25.dp))
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
                                .padding(bottom = 5.dp)
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
                                border = BorderStroke(1.dp, Color(0xFFBEBEBE))
                            ) {
                                if (draggableItem.isEmail) {
                                    Row(
                                        modifier = Modifier
                                            .padding(10.dp)
                                            .height(40.dp)
                                            .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Spacer(Modifier.width(72.dp))
                                        Text(
                                            email,
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable { isEnterEmail = true },
                                            overflow = TextOverflow.Ellipsis,
                                            textDecoration = TextDecoration.Underline,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Spacer(Modifier.width(52.dp))
                                    }
                                }

                                Row(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .height(40.dp)
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
                                            .size(16.dp, 16.dp)
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
                                    Spacer(Modifier.width(9.dp))
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
                    }
                //}
                Spacer(Modifier.height(20.dp))
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
                Spacer(Modifier.height(25.dp))
                GreenButton(
                    text = stringResource(R.string.send_the_code),
                    onClick = {
                        items.forEach { draggableItem ->
                            if (switchStates[draggableItem.text]!!)
                                addWay(draggableItem.text)
                        }
                        sendCode()
                    }
                )
                Spacer(Modifier.height(20.dp))
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
        Spacer(Modifier.height(24.dp))
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
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

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
            modifier = Modifier.width(350.dp),
            shape = MaterialTheme.shapes.large,
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(R.string.specify_the_email),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(40.dp))
                EmailTextField(
                    email = value,
                    placeholder = stringResource(R.string.your_email),
                    onValueChange = { value = it },
                    onDone = { onDone(value) },
                    isError = isError
                )
                Spacer(Modifier.height(25.dp))
                GreenButton(
                    text = stringResource(R.string.confirm),
                    onClick = { onDone(value) }
                )
                Spacer(Modifier.height(10.dp))
                WhiteButton(
                    text = stringResource(R.string.cancellation),
                    onClick = onCancel
                )
            }
        }
    }
}