package com.example.geoblinker.ui.main.profile.techsupport

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.example.geoblinker.R
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.CustomButton
import com.example.geoblinker.ui.TypeColor
import com.example.geoblinker.ui.theme.sdp

private const val MAX_THEME_REQUEST = 64
private const val MAX_REQUEST = 1000

@Composable
fun MakeRequestScreen(
    toBack: () -> Unit
) {
    var themeRequest by remember { mutableStateOf("") }
    var request by remember { mutableStateOf("") }
    var isSend by remember { mutableStateOf(false) }
    // Создаем референсы для фокуса
    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }

    Column(
        modifier = Modifier.width(330.sdp()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.ask_specialist_question),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(Modifier.height(20.sdp()))
        if (isSend) {
            Spacer(Modifier.height(83.sdp()))
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.green_tick),
                contentDescription = null,
                modifier = Modifier.size(27.sdp(), 20.sdp()),
                tint = Color.Unspecified
            )
            Spacer(Modifier.height(39.sdp()))
            Text(
                stringResource(R.string.received_request),
                color = Color(0xFF12CD4A),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        else {
            Box(
                modifier = Modifier.height(50.sdp())
            ) {
                TextField(
                    themeRequest,
                    { themeRequest = it.take(MAX_THEME_REQUEST) },
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            1.sdp(),
                            Color(0xFFC0C0C0),
                            RoundedCornerShape(10.sdp())
                        )
                        .focusRequester(focusRequester1),
                    placeholder = {
                        Text(
                            stringResource(R.string.theme_request),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusRequester2.requestFocus() }
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(10.sdp()),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Text(
                        "${themeRequest.length}/$MAX_THEME_REQUEST",
                        modifier = Modifier.offset(x = (-10).sdp(), y = (-1).sdp()),
                        color = Color(0xFF747474),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            Spacer(Modifier.height(10.sdp()))
            Box(
                modifier = Modifier.height(300.sdp())
            ) {
                TextField(
                    request,
                    { request = it.take(MAX_REQUEST) },
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            1.sdp(),
                            Color(0xFFC0C0C0),
                            RoundedCornerShape(10.sdp())
                        )
                        .focusRequester(focusRequester2),
                    placeholder = {
                        Text(
                            stringResource(R.string.your_request),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = { isSend = true } // TODO: Добавить отправку обращения
                    ),
                    shape = RoundedCornerShape(10.sdp()),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Text(
                        "${request.length}/$MAX_REQUEST",
                        modifier = Modifier.offset(x = (-10).sdp(), y = (-1).sdp()),
                        color = Color(0xFF747474),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            Spacer(Modifier.height(10.sdp()))
            CustomButton(
                text = stringResource(R.string.send),
                onClick = { isSend = true },
                typeColor = TypeColor.Green,
                enabled = themeRequest.isNotEmpty(),
                height = 55,
                radius = 10,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

    BackButton(
        onClick = toBack
    )
}