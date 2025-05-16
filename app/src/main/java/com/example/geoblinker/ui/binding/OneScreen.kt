package com.example.geoblinker.ui.binding

import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.geoblinker.R
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.GreenMediumButton
import com.example.geoblinker.ui.ImeiTextField
import com.example.geoblinker.ui.theme.sdp
import com.example.geoblinker.ui.theme.ssp
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

@Composable
fun OneScreen(
    toTwoScreen: (String) -> Unit,
    toBack: () -> Unit
) {
    var imei by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var isShow by remember { mutableStateOf(false) }

    fun onDone() {
        if (imei.length != 15)
            isError = true
        else
            toTwoScreen(imei)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.new_device),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(20.sdp()))
        Image(
            painter = painterResource(R.drawable.new_device_image),
            contentDescription = null,
            modifier = Modifier.size(310.sdp(), 222.sdp())
        )
        Spacer(Modifier.height(50.sdp()))
        ImeiTextField(
            imei,
            {
                imei = it
                isError = false
            },
            { onDone() },
            {
                isShow = true
            },
            isError = isError
        )
        Spacer(Modifier.height(15.sdp()))
        GreenMediumButton(
            text = stringResource(R.string.link),
            onClick = { onDone() },
            height = 65,
            style = MaterialTheme.typography.headlineMedium
        )

        if (isError) {
            Spacer(Modifier.height(10.sdp()))
            Text(
                stringResource(R.string.imei_not_found),
                color = MaterialTheme.colorScheme.error,
                lineHeight = 22.ssp(),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        else{
            Spacer(Modifier.height(32.sdp()))
        }
        Spacer(Modifier.height(30.sdp()))
    }

    BackButton(
        onClick = toBack
    )

    if (isShow) {
        BarcodeScannerScreen {
            imei = it
            onDone()
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
        modifier = Modifier.layout { measurable, _ ->
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