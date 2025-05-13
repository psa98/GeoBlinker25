@file:OptIn(ExperimentalPermissionsApi::class)

package com.example.geoblinker.ui

import android.Manifest
import androidx.annotation.StringRes
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.geoblinker.R
import com.example.geoblinker.ui.theme.GeoBlinkerTheme
import com.example.geoblinker.ui.theme.sdp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

val red = Color(0xFFC4162D)

@Composable
fun CodeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
    @StringRes placeholder: Int,
    isError: Boolean = false
) {
    var isFocused by remember { mutableStateOf(false) }

    Surface(
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.sdp(), if (isError) red else Color(0xFFBEBEBE))
    ) {
        TextField(
            value = TextFieldValue(
                text = value,
                selection = TextRange(value.length)
            ),
            onValueChange = {
                onValueChange(it.text.take(4))
            },
            modifier = Modifier
                .height(81.sdp())
                .fillMaxWidth()
                .padding(1.sdp())
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

@Composable
fun PhoneNumberTextField(
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
    isError: Boolean = false
) {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = "")) }
    var isFocused by remember { mutableStateOf(false) }

    Surface(
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.sdp(), if (isError) red else Color(0xFFBEBEBE))
    ) {
        TextField(
            value = textFieldValueState,
            onValueChange = { value ->
                val filtered = value.text.filter { it.isDigit() }.take(10)
                val format = formatPhoneNumber(filtered)
                onValueChange(filtered)
                textFieldValueState = TextFieldValue(
                    text = format,
                    selection = TextRange(format.length)
                )
            },
            modifier = Modifier
                .height(81.sdp())
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
    placeholder: String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
    isError: Boolean = false
) {
    var value by remember { mutableStateOf("") }

    Surface(
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.sdp(), if (isError) red else Color(0xFFBEBEBE))
    ) {
        TextField(
            value = value,
            onValueChange = {
                value = it
                onValueChange(value)
            },
            modifier = Modifier
                .height(81.sdp())
                .fillMaxWidth()
            ,
            textStyle = MaterialTheme.typography.headlineMedium.copy(
                color = if (isError) red else Color(0xFF222221)
            ),
            placeholder = {
                Text(
                    text = placeholder,
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

@Composable
fun NameDeviceTextField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
    isError: Boolean = false
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.sdp(), if (isError) red else Color(0xFFBEBEBE))
    ) {
        TextField(
            value = value,
            onValueChange = {
                onValueChange(it.filter { c -> c.isLetterOrDigit() || c == ' ' })
            },
            modifier = Modifier
                .height(65.sdp())
                .fillMaxWidth()
            ,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = if (isError) red else Color(0xFF222221)
            ),
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium
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
            shape = MaterialTheme.shapes.medium,
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
fun EmailTextField(
    email: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
    isError: Boolean = false
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.sdp(), if (isError) red else Color(0xFFBEBEBE))
    ) {
        TextField(
            value = email,
            onValueChange = { newValue ->
                onValueChange(newValue.filter { it != ' ' })
            },
            modifier = Modifier
                .height(81.sdp())
                .fillMaxWidth()
            ,
            textStyle = MaterialTheme.typography.headlineMedium.copy(
                color = if (isError) red else Color(0xFF222221)
            ),
            placeholder = {
                Text(
                    text = placeholder,
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

@Composable
fun ImeiTextField(
    imei: String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
    onPermissionGranted: () -> Unit,
    isError: Boolean = false
) {
    val permissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    val keyboardController = LocalSoftwareKeyboardController.current
    var isFocused by remember { mutableStateOf(false) }
    var isShow by remember { mutableStateOf(false) }

    LaunchedEffect(isShow) {
        if (isShow) {
            if (!permissionState.status.isGranted) {
                permissionState.launchPermissionRequest()
                isShow = false
            } else {
                keyboardController?.hide()
                onPermissionGranted()
            }
        }
    }

    Surface(
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.sdp(), if (isError) red else Color(0xFFC0C0C0))
    ) {
        TextField(
            value = imei,
            onValueChange = { newValue ->
                onValueChange(newValue.filter { it.isDigit() }.take(15))
            },
            modifier = Modifier
                .height(65.sdp())
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                }
            ,
            textStyle = MaterialTheme.typography.headlineMedium.copy(
                color = if (isError) red else Color(0xFF222221)
            ),
            placeholder = {
                Text(
                    text = if (isFocused) stringResource(R.string.imei) else stringResource(R.string.scan_the_imei),
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            prefix = {
                Text("  ")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone =  { onDone() }
            ),
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent, // Убираем линию в фокусе
                unfocusedIndicatorColor = Color.Transparent // Убираем линию без фокуса
            )
        )

        if (isFocused) {
            Box(
                Modifier.fillMaxWidth().padding(5.sdp()),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(
                    onClick = {
                        isShow = true
                    },
                    modifier = Modifier
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF373736), Color(0xFF212120))
                            ),
                            RoundedCornerShape(12.sdp())
                        ),
                    shape = RoundedCornerShape(12.sdp()),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(10.sdp())
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.imei_scan),
                        contentDescription = null,
                        modifier = Modifier.size(35.sdp())
                    )
                }
            }
        }
    }
}