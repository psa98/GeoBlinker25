package com.example.geoblinker.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.geoblinker.R
import com.example.geoblinker.ui.theme.GeoBlinkerTheme

val red = Color(0xFFC4162D)

@Composable
fun CodeTextField(
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
    @StringRes placeholder: Int,
    isError: Boolean = false
) {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = "")) }
    var isFocused by remember { mutableStateOf(false) }

    Surface(
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.dp, if (isError) red else Color.White)
    ) {
        TextField(
            value = textFieldValueState,
            onValueChange = {
                textFieldValueState = TextFieldValue(
                    text = it.text.take(4),
                    selection = TextRange(it.text.take(4).length)
                )
                onValueChange(textFieldValueState.text)
            },
            modifier = Modifier
                .height(81.dp)
                .fillMaxWidth()
                .padding(1.dp)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                }
            ,
            textStyle = MaterialTheme.typography.displayMedium.copy(
                color = if (isError) red else Color(0xFF222221)
            ),
            placeholder = {
                if (!isFocused)
                    Text(
                        text = stringResource(placeholder),
                        style = MaterialTheme.typography.headlineMedium
                    )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onDone() }
            ),
            singleLine = true,
            shape = MaterialTheme.shapes.large,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent, // Убираем линию в фокусе
                unfocusedIndicatorColor = Color.Transparent // Убираем линию без фокуса
            )
        )
    }
}

@Composable
fun PhoneNumberTextField(
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
    isError: Boolean = false
) {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = "")) }
    var isFocused by remember { mutableStateOf(false) }

    fun formatPhoneNumber(phoneNumber: String): String {
        val formatted = StringBuilder()

        phoneNumber.forEachIndexed { index, c ->
            when (index) {
                3, 6, 8 -> formatted.append(" $c")
                else -> formatted.append(c)
            }
        }
        return formatted.toString()
    }

    Surface(
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.dp, if (isError) red else Color(0xFFBEBEBE))
    ) {
        TextField(
            value = textFieldValueState,
            onValueChange = { value ->
                val filtered = value.text.filter { it.isDigit() }.take(10)
                val format = formatPhoneNumber(filtered)
                onValueChange("+7${filtered}")
                textFieldValueState = TextFieldValue(
                    text = format,
                    selection = TextRange(format.length)
                )
            },
            modifier = Modifier
                .height(81.dp)
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            textStyle = MaterialTheme.typography.headlineMedium.copy(
                color = if (isError) red else Color(0xFF222221)
            ),
            placeholder = {
                if (!isFocused)
                    Text(
                        text = "  ${stringResource(R.string.enter_the_number)}",
                        style = MaterialTheme.typography.headlineMedium
                    )
            },
            prefix = {
                if (isFocused || textFieldValueState.text.isNotEmpty())
                    Text(
                        text = "  + 7 ",
                        color = if (isError) red else Color.Unspecified,
                        style = MaterialTheme.typography.headlineMedium
                    )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone =  { onDone() }
            ),
            singleLine = true,
            shape = MaterialTheme.shapes.large,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent, // Убираем линию в фокусе
                unfocusedIndicatorColor = Color.Transparent // Убираем линию без фокуса
            )
        )
    }
}

@Composable
fun NameTextField(
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
    isError: Boolean = false
) {
    var value by remember { mutableStateOf("") }

    Surface(
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.dp, if (isError) red else Color(0xFFBEBEBE))
    ) {
        TextField(
            value = value,
            onValueChange = {
                value = it
                onValueChange(value)
            },
            modifier = Modifier
                .height(81.dp)
                .fillMaxWidth()
            ,
            textStyle = MaterialTheme.typography.headlineMedium.copy(
                color = if (isError) red else Color(0xFF222221)
            ),
            placeholder = {
                Text(
                    text = stringResource(R.string.your_name),
                    style = MaterialTheme.typography.headlineMedium
                )
            },
            prefix = {
                Text("  ")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone =  { onDone() }
            ),
            singleLine = true,
            shape = MaterialTheme.shapes.large,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent, // Убираем линию в фокусе
                unfocusedIndicatorColor = Color.Transparent // Убираем линию без фокуса
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSimpleTextField() {
    GeoBlinkerTheme {
        Surface(Modifier.fillMaxSize()) {
            CodeTextField({}, {}, R.string.enter_the_code)
        }
    }
}