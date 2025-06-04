package com.example.geoblinker.ui.main.profile.settings

import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.round
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.geoblinker.R
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.CustomLinkEmailPopup
import com.example.geoblinker.ui.WayConfirmationCode
import com.example.geoblinker.ui.main.viewmodel.ProfileViewModel
import com.example.geoblinker.ui.round
import com.example.geoblinker.ui.theme.sdp
import com.example.geoblinker.ui.theme.ssp

@Composable
fun ConfirmationCodeSettingsScreen(
    viewModel: ProfileViewModel,
    toLinkEmail: () -> Unit,
    toBack: () -> Unit
) {
    val email by viewModel.email.collectAsState()
    val waysConfirmationCode by viewModel.waysConfirmationCode.collectAsState()
    val stateWays = remember { mutableStateListOf<Boolean>().apply {
        addAll(waysConfirmationCode.map { it.checked })
    } }
    var isShow by remember { mutableStateOf(false) }
    var draggedIndex by remember { mutableIntStateOf(-1) }
    // Смещение для анимации
    var draggedOffset by remember { mutableStateOf(Offset.Zero) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.ways_confirmation_code),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(Modifier.height(20.sdp()))
        Text(
            stringResource(R.string.procedure_confirmation_code),
            color = Color(0xFF5D5D5D),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 10.ssp()
            )
        )
        Spacer(Modifier.height(40.sdp()))
        waysConfirmationCode.forEachIndexed { index, draggableItem ->
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
                                modifier = Modifier.weight(1f),
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
                                                viewModel.changeConfirmationCode(draggedIndex, targetIndex)
                                                val now = stateWays[draggedIndex]
                                                stateWays[draggedIndex] = stateWays[targetIndex]
                                                stateWays[targetIndex] = now
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
                                if (draggableItem.text == "Email" && email.isEmpty())
                                    isShow = true
                                else {
                                    viewModel.setCheckedWayConfirmationCode(index, it)
                                    stateWays[index] = it
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
    }

    BackButton(
        onClick = toBack
    )

    if (isShow) {
        CustomLinkEmailPopup(
            toLinkEmail = toLinkEmail,
            cancellation = { isShow = false }
        )
    }
}