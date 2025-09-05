package com.example.geoblinker.ui.main.binding

import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.geoblinker.R
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.CustomButton
import com.example.geoblinker.ui.HSpacer
import com.example.geoblinker.ui.ImeiTextField
import com.example.geoblinker.ui.TypeColor
import com.example.geoblinker.ui.main.viewmodel.DefaultStates
import com.example.geoblinker.ui.main.viewmodel.DeviceViewModel
import com.example.geoblinker.ui.theme.sdp
import com.example.geoblinker.ui.theme.ssp
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

@Composable
fun BindingOneScreen(
    viewModel: DeviceViewModel,
    toTwoScreen: (String) -> Unit,
    toBack: () -> Unit
) {
    val uiState by viewModel.uiState
    var imei by rememberSaveable { mutableStateOf("") }
    var isShow by rememberSaveable { mutableStateOf(false) }

    fun onDone() {
        viewModel.checkImei(imei)
    }

    LaunchedEffect(uiState) {
        if (uiState is DefaultStates.Success) {
            viewModel.resetUiState()
            toTwoScreen(imei)
        }
    }

    Column(
        modifier = Modifier.requiredWidth(310.sdp()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.new_device),
            style = MaterialTheme.typography.titleMedium
        )
        HSpacer(20)
        Image(
            painter = painterResource(R.drawable.new_device_image),
            contentDescription = null,
            modifier = Modifier.size(270.sdp(), 200.sdp())
        )
        HSpacer(30)
        ImeiTextField(
            imei,
            {
                imei = it
                viewModel.resetUiState()
            },
            { onDone() },
            {
                isShow = true
            },
            isError = uiState is DefaultStates.Error
        )
        HSpacer(15)
        SelectType(viewModel,Modifier.size(200.dp,60.dp))
        HSpacer(15)
        CustomButton(
            text = stringResource(R.string.link),
            onClick = { onDone() },
            typeColor = TypeColor.Green,
            height = 65,
            style = MaterialTheme.typography.headlineMedium
        )
        HSpacer(10)
        if (uiState is DefaultStates.Error) {
            Text(
                stringResource((uiState as DefaultStates.Error).message),
                color = MaterialTheme.colorScheme.error,
                lineHeight = 22.ssp(),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

    BackButton(
        onClick = toBack
    )

    if (isShow) {
        BarcodeScannerScreen {
            imei = it
            isShow=false
        }
    }
}

@Composable
fun SelectType(viewModel: DeviceViewModel,
               modifier: Modifier) {

    val list = listOf("Тип 2", "Тип 4")
    var selected by remember { mutableStateOf(list[0]) }
    var expanded by remember { mutableStateOf(false) } // initial value

    OutlinedCard(
        modifier = modifier.clickable {
            expanded = !expanded
        }
    ) {


        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {

            Text(
                text = selected,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Icon(Icons.Outlined.ArrowDropDown, null, modifier = Modifier.padding(8.dp))

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()   // delete this modifier and use .wrapContentWidth() if you would like to wrap the dropdown menu around the content
            ) {
                list.forEach { listEntry ->

                    DropdownMenuItem(
                        onClick = {
                            selected = listEntry
                            expanded = false
                            viewModel.setType(selected)
                        },
                        text = {
                            Text(
                                text = listEntry,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.Start)
                            )
                        },
                    )
                }
            }

        }
    }
}

@Composable
fun BarcodeScannerScreen(onScanResult: (String) -> Unit) {
    //val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.sdp()
    val screenHeight = configuration.screenHeightDp.sdp()

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val cameraExecutor = ContextCompat.getMainExecutor(ctx)

            // Настройка CameraX и ML Kit
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val barcodeScanner = BarcodeScanning.getClient()
                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor) { imageProxy ->
                            processImage(imageProxy, barcodeScanner) { result ->

                                onScanResult(result)

                            }
                        }
                    }

                // Связка с жизненным циклом
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageAnalysis
                )
            }, cameraExecutor)
            previewView
        },
        modifier = Modifier
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
    )
}

@OptIn(ExperimentalGetImage::class)
private fun processImage(
    imageProxy: ImageProxy,
    barcodeScanner: BarcodeScanner,
    onResult: (String) -> Unit
) {
    val mediaImage = imageProxy.image ?: return
    val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

    barcodeScanner.process(image)
        .addOnSuccessListener { barcodes ->
            barcodes.firstOrNull()?.rawValue?.let { scannedValue ->
                onResult(scannedValue)
            }
        }
        .addOnCompleteListener {
            imageProxy.close()
        }
}