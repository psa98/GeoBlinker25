package com.example.geoblinker.ui.main.profile.techsupport

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.example.geoblinker.R
import com.example.geoblinker.data.techsupport.MessageTechSupport
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.main.viewmodel.ChatsViewModel
import com.example.geoblinker.ui.theme.sdp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private enum class ChatsScreen {
    Chats,
    Chat
}

private const val MAX_TEXT = 1000

@Composable
fun ChatsScreen(
    viewModel: ChatsViewModel,
    toBack: () -> Unit,
    navController: NavHostController = rememberNavController()
) {
    val chats by viewModel.chats.collectAsState()

    NavHost(
        navController = navController,
        startDestination = ChatsScreen.Chats.name
    ) {
        composable(route = ChatsScreen.Chats.name) {
            if (chats.isEmpty())
                NullChats(
                    toBack = toBack
                )
            else
                Chats(
                    viewModel,
                    toChat = { navController.navigate(ChatsScreen.Chat.name) },
                    toBack = toBack
                )
        }
        composable(route = ChatsScreen.Chat.name) {
            Chat(
                viewModel,
                toBack = { navController.navigateUp() }
            )
        }
    }
}

@Composable
private fun NullChats(
    toBack: () -> Unit
) {
    Text(
        stringResource(R.string.chat_with_tech_support),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold
        )
    )
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            stringResource(R.string.null_requests_description),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }
    BackButton(
        onClick = toBack
    )
}

@Composable
private fun Chats(
    viewModel: ChatsViewModel,
    toChat: () -> Unit,
    toBack: () -> Unit
) {
    val chats by viewModel.chats.collectAsState()

    LazyColumn(
        modifier = Modifier.width(330.sdp()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                stringResource(R.string.chat_with_tech_support),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(Modifier.height(50.sdp()))
        }
        items(chats.sortedByDescending { it.lastMessageTime }) { chat ->
            val notifications = when(chat.decided) {
                true -> 0
                false -> viewModel.countNotCheckedMessages(chat)
            }
            Row(
                modifier = Modifier
                    .width(330.sdp())
                    .clickable {
                        viewModel.setSelectedChat(chat.id)
                        toChat()
                    }
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(10.sdp())
                    )
                    .padding(15.sdp())
                    .border(
                        1.sdp(),
                        if (notifications > 0) Color(0xFF12CD4A) else Color.White
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    chat.title,
                    modifier = Modifier.width(266.sdp()),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmall
                )
                if (chat.decided) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.green_tick),
                        contentDescription = null,
                        modifier = Modifier.size(15.sdp(), 11.sdp()),
                        tint = Color(0xFFBEBEBE)
                    )
                }
                else if (notifications > 0) {
                    Box(
                        Modifier.size(22.sdp()),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            Modifier
                                .background(
                                    color = Color(0xFF12CD4A),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                if (notifications > 99) "99+" else notifications.toString(),
                                modifier = Modifier.padding(horizontal = 6.sdp(), vertical = 1.sdp()),
                                color = Color.White,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(15.sdp()))
        }
    }

    BackButton(
        onClick = toBack
    )
}

@Composable
private fun Chat(
    viewModel: ChatsViewModel,
    toBack: () -> Unit
) {
    val chat by viewModel.selectedChat.collectAsState()
    val messages by viewModel.messages.collectAsState()
    var text by remember { mutableStateOf("") }
    val images = remember { mutableStateListOf<MediaItem>() }
    var showImagePicker by remember { mutableStateOf(false) }
    var showImagePreview by remember { mutableStateOf<String?>(null) }
    var showVideoPreview by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        scrollState.animateScrollToItem(0)
    }

    Column {
        Text(
            stringResource(R.string.chat_with_tech_support),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(Modifier.height(50.sdp()))
        Text(
            chat.title,
            modifier = Modifier
                .width(330.sdp())
                .background(
                    Color(0xFFE8E8E8),
                    RoundedCornerShape(10.sdp())
                )
                .padding(15.sdp()),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(Modifier.height(10.sdp()))
        LazyColumn(
            modifier = Modifier
                .size(330.sdp(), 260.sdp())
                .background(
                    Color.White,
                    RoundedCornerShape(10.sdp())
                )
                .border(
                    1.sdp(),
                    Color(0xFFC0C0C0),
                    RoundedCornerShape(10.sdp())
                ),
            state = scrollState,
            contentPadding = PaddingValues(start = 5.sdp(), end = 5.sdp(), bottom = 5.sdp()),
            reverseLayout = true
        ) {
            items(
                messages.sortedByDescending { it.timeStamp }
            ) { message ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = if (message.isMy) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    when (message.typeMessage) {
                        MessageTechSupport.Type.Text -> {
                            Text(
                                message.content,
                                modifier = Modifier
                                    .widthIn(max = 260.sdp())
                                    .background(
                                        if (message.isMy) Color(0xFF73FAD3) else Color(
                                            0xFFF6F6F6
                                        ),
                                        RoundedCornerShape(8.sdp())
                                    )
                                    .padding(horizontal = 10.sdp(), vertical = 6.sdp()),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        MessageTechSupport.Type.Image -> {
                            var isLoading by remember { mutableIntStateOf(0) }
                            AsyncImage(
                                message.photoUri,
                                contentDescription = null,
                                modifier = Modifier
                                    .widthIn(max = 260.sdp())
                                    .clip(RoundedCornerShape(8.sdp()))
                                    .border(
                                        4.sdp(),
                                        if (message.isMy) Color(0xFF73FAD3) else Color(
                                            0xFFF6F6F6
                                        ),
                                        RoundedCornerShape(8.sdp())
                                    )
                                    .clickable { showImagePreview = message.photoUri },
                                onState = { state ->
                                    isLoading = when (state) {
                                        is AsyncImagePainter.State.Loading -> 0
                                        is AsyncImagePainter.State.Success -> 1
                                        else -> 2
                                    }
                                }
                            )

                            if (isLoading == 0) {
                                Box(
                                    modifier = Modifier
                                        .size(260.sdp(), 350.sdp())
                                        .background(
                                            Color(0xFFE8E8E8),
                                            RoundedCornerShape(8.sdp())
                                        )
                                        .border(
                                            4.sdp(),
                                            if (message.isMy) Color(0xFF73FAD3) else Color(
                                                0xFFF6F6F6
                                            ),
                                            RoundedCornerShape(8.sdp())
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = Color.White)
                                }
                            }
                        }
                        MessageTechSupport.Type.Video -> {
                            val context = LocalContext.current
                            val thumbnailBitmap = remember(message.photoUri) { mutableStateOf<Bitmap?>(null) }
                            var duration by remember { mutableLongStateOf(0L) }
                            LaunchedEffect(Unit) {
                                withContext(Dispatchers.IO) {
                                    thumbnailBitmap.value = createVideoThumbnail(context, Uri.parse(message.photoUri))
                                    duration = getVideoDuration(context, Uri.parse(message.photoUri))
                                }
                            }
                            thumbnailBitmap.value?.let { bitmap ->
                                Box {
                                    Image(
                                        bitmap = bitmap.asImageBitmap(),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .widthIn(max = 260.sdp())
                                            .clip(RoundedCornerShape(8.dp))
                                            .border(
                                                4.sdp(),
                                                if (message.isMy) Color(0xFF73FAD3) else Color(
                                                    0xFFF6F6F6
                                                ),
                                                RoundedCornerShape(8.sdp())
                                            )
                                            .clickable { showVideoPreview = message.photoUri }
                                    )
                                    Text(
                                        text = duration.formatAsTime(),
                                        color = Color.White,
                                        modifier = Modifier
                                            .align(Alignment.TopStart)
                                            .padding(8.dp)
                                            .background(
                                                Color.Black.copy(alpha = 0.5f),
                                                RoundedCornerShape(4.dp)
                                            )
                                            .padding(horizontal = 4.dp, vertical = 2.dp)
                                            .clickable { showVideoPreview = message.photoUri },
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            } ?: run {
                                Box(
                                    modifier = Modifier
                                        .size(260.sdp(), 350.sdp())
                                        .background(
                                            Color(0xFFE8E8E8),
                                            RoundedCornerShape(8.sdp())
                                        )
                                        .border(
                                            4.sdp(),
                                            if (message.isMy) Color(0xFF73FAD3) else Color(
                                                0xFFF6F6F6
                                            ),
                                            RoundedCornerShape(8.sdp())
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = Color.White)
                                }
                            }
                        }
                        MessageTechSupport.Type.Document -> {
                            val context = LocalContext.current
                            val name = loadDocumentsMetadata(context, listOf((Uri.parse(message.photoUri))))[0].name
                            Box(
                                modifier = Modifier
                                    .widthIn(max = 260.sdp())
                                    .background(
                                        if (message.isMy) Color(0xFF73FAD3) else Color(
                                            0xFFF6F6F6
                                        ),
                                        RoundedCornerShape(8.sdp())
                                    )
                                    .padding(5.sdp())
                            ) {
                                Column {
                                    Row {
                                        Text(
                                            name.substring(0, name.indexOfLast { it == '.' }),
                                            modifier = Modifier.weight(1f).padding(end = 5.sdp()),
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Text(
                                            name.substring(name.indexOfLast { it == '.' }),
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                    Spacer(Modifier.height(4.sdp()))
                                    Text(
                                        getDocumentSize(Uri.parse(message.photoUri)).formatAsSize(),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(5.sdp()))
            }
        }
        Spacer(Modifier.height(10.sdp()))
        Row(
            modifier = Modifier
                .size(330.sdp(), 105.sdp())
                .background(
                    Color.White,
                    RoundedCornerShape(10.sdp())
                )
                .border(
                    1.sdp(),
                    Color(0xFFC0C0C0),
                    RoundedCornerShape(10.sdp())
                )
        ) {
            TextField(
                text,
                { text = it.take(MAX_TEXT) },
                modifier = Modifier.weight(1f),
                textStyle = MaterialTheme.typography.labelMedium,
                placeholder = {
                    Text(
                        stringResource(R.string.write_message),
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (text.isNotEmpty() || images.isNotEmpty()) {
                            viewModel.addMessage(text, images.toList())
                            text = ""
                            images.clear()
                        }
                    }
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Column(
                modifier = Modifier.fillMaxHeight().padding(10.sdp()),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box {
                    Box(
                        Modifier.size(38.sdp()),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.attach_file),
                            contentDescription = null,
                            modifier = Modifier
                                .size(38.sdp())
                                .clickable {
                                    showImagePicker = true
                                },
                            tint = Color.Unspecified
                        )
                    }

                    if (images.isNotEmpty()) {
                        Box(
                            Modifier.size(38.sdp()).offset(12.sdp(), (-12).sdp()),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                Modifier
                                    .background(
                                        color = Color(0xFFF1137E),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    if (images.size > 99) "99+" else images.size.toString(),
                                    modifier = Modifier.padding(horizontal = 6.sdp(), vertical = 1.sdp()),
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                }
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.send_message),
                    contentDescription = null,
                    modifier = Modifier
                        .size(38.sdp())
                        .clickable {
                            if (text.isNotEmpty() || images.isNotEmpty()) {
                                viewModel.addMessage(text, images.toList())
                                text = ""
                                images.clear()
                            }
                        },
                    tint = Color.Unspecified
                )
            }
        }
    }

    BackButton(
        onClick = toBack
    )

    if (showImagePicker) {
        CustomPicker(
            selectedUris = images,
            onSelectionChanged = { newSelection ->
                images.clear()
                images.addAll(newSelection)
            },
            cancellation = {
                showImagePicker = false
            }
        )
    }

    showImagePreview?.let {
        FullScreenImage(
            it,
            cancel = { showImagePreview = null }
        )
    }

    showVideoPreview?.let {
        VideoPlayer(
            Uri.parse(it),
            cancel = { showVideoPreview = null }
        )
    }
}

@Composable
fun FullScreenImage(
    uri: String,
    cancel: () -> Unit
) {
    var isLoading by remember { mutableIntStateOf(0) }
    var scale by remember { mutableFloatStateOf(1f) }
    val offset = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
    val scope = rememberCoroutineScope()
    val swipe = with(LocalDensity.current) {
        0.4f * LocalConfiguration.current.screenWidthDp.dp.toPx()
    }

    val dragState = Modifier.pointerInput(Unit) {
        detectDragGestures(
            onDrag = { change, dragAmount ->
                change.consume()
                val newOffset = offset.value + dragAmount

                scope.launch {
                    offset.snapTo(
                        newOffset
                    )
                }
            },
            onDragEnd = {
                if (offset.value.y < -swipe)
                    cancel()

                scope.launch {
                    offset.animateTo(
                        targetValue = Offset.Zero,
                        animationSpec = tween(300)
                    )
                }
            }
        )
    }

    val transformState = Modifier.transformable(
        state = rememberTransformableState { zoomChange, _, _ ->
            scale *= zoomChange
        }
    )

    val doubleTap = Modifier.pointerInput(Unit) {
        detectTapGestures(
            onDoubleTap = {
                scale = if (scale > 1f) 1f else 3f
            }
        )
    }

    Dialog(
        { cancel() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.2f))
                .then(dragState)
                .then(if (scale > 1f) transformState else Modifier)
                .then(doubleTap)
        ) {
            AsyncImage(
                model = uri,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.value.x,
                        translationY = offset.value.y
                    ),
                onState = { state ->
                    isLoading = when (state) {
                        is AsyncImagePainter.State.Loading -> 0
                        is AsyncImagePainter.State.Success -> 1
                        else -> 2
                    }
                }
            )
            if (isLoading == 0) {
                Box(
                    modifier = Modifier
                        .size(260.sdp(), 350.sdp())
                        .background(
                            Color(0xFFE8E8E8),
                            RoundedCornerShape(8.sdp())
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    uri: Uri,
    cancel: () -> Unit = {}
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    var isVideoEnded by remember { mutableStateOf(false) }
    var duration by remember { mutableLongStateOf(0) }
    var maxDuration by remember { mutableLongStateOf(0) }
    val offset = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
    val scope = rememberCoroutineScope()
    val swipe = with(LocalDensity.current) {
        0.4f * LocalConfiguration.current.screenWidthDp.dp.toPx()
    }

    val dragState = Modifier.pointerInput(Unit) {
        detectDragGestures(
            onDrag = { change, dragAmount ->
                change.consume()
                val newOffset = offset.value + dragAmount

                scope.launch {
                    offset.snapTo(
                        newOffset
                    )
                }
            },
            onDragEnd = {
                if (offset.value.y < -swipe)
                    cancel()

                scope.launch {
                    offset.animateTo(
                        targetValue = Offset.Zero,
                        animationSpec = tween(300)
                    )
                }
            }
        )
    }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(androidx.media3.common.MediaItem.fromUri(uri))
            prepare()

            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_ENDED) {
                        isVideoEnded = true
                        isPlaying = false
                    }
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    if (isPlaying) {
                        isVideoEnded = false
                    }
                }
            })
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            duration = exoPlayer.currentPosition
            maxDuration = exoPlayer.duration
            delay(200)
        }
    }

    LaunchedEffect(exoPlayer.isReleased) {
        if (exoPlayer.isReleased)
            duration = exoPlayer.duration
    }

    fun togglePlayback() {
        if (isVideoEnded) {
            // Если видео закончилось - перематываем в начало
            exoPlayer.seekTo(0)
            isVideoEnded = false
        }

        if (isPlaying) {
            exoPlayer.pause()
        } else {
            exoPlayer.play()
        }
        isPlaying = !isPlaying
    }

    Dialog(
        {},
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.2f))
                .clickable { cancel() }
                .then(dragState),
            contentAlignment = Alignment.Center
        ) {
            if (!exoPlayer.isLoading) {
                Box(
                    modifier = Modifier
                        .clickable { togglePlayback() }
                        .graphicsLayer(
                            translationY = minOf(0f, offset.value.y)
                        )
                ) {
                    AndroidView(
                        factory = { ctx ->
                            PlayerView(ctx).apply {
                                player = exoPlayer
                                useController = false // Скрываем стандартные контролы
                                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                            }
                        }
                    )

                    Text(
                        text = "${duration.formatAsTime()}/${maxDuration.formatAsTime()}",
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                            .background(
                                Color.Black.copy(alpha = 0.3f),
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
        }
    }
}

@Composable
fun CustomPicker(
    selectedUris: List<MediaItem>,
    onSelectionChanged: (List<MediaItem>) -> Unit,
    cancellation: () -> Unit
) {
    val context = LocalContext.current
    var isTakePhoto by remember { mutableStateOf(false) }

    val imageUris = remember { mutableStateListOf<MediaItem>() }
    val videoUris = remember { mutableStateListOf<MediaItem>() }
    val documentUris = remember { mutableStateListOf<MediaItem>() }

    var isLoadingImages by remember { mutableStateOf(false) }
    var isLoadingVideos by remember { mutableStateOf(false) }
    var isLoadingDocuments by remember { mutableStateOf(false) }

    var imagesPermissionGranted by remember { mutableStateOf(false) }
    var videosPermissionGranted by remember { mutableStateOf(false) }

    var pickMediaItems = remember { mutableStateListOf<MediaItem>() }
    var isLoading by remember { mutableStateOf(true) }
    var pickMediaType by remember { mutableStateOf(MediaType.IMAGE) }

    val imagesPermissions = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }
    val videosPermissions = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_VIDEO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }

    val imagesPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            isLoadingImages = true
        }
    }
    val videosPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            isLoadingVideos = true
        }
    }

    val documentsPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        val now = loadDocumentsMetadata(context, uris)
        now.forEach {
            if (!documentUris.contains(it)) {
                documentUris.add(it)

                val newSelection = selectedUris.toMutableList()
                newSelection.add(it)
                onSelectionChanged(newSelection)
            }
        }
    }

    when(pickMediaType) {
        MediaType.IMAGE -> {
            isLoading = isLoadingImages
            pickMediaItems = imageUris
        }
        MediaType.VIDEO -> {
            isLoading = isLoadingVideos
            pickMediaItems = videoUris
        }
        MediaType.DOCUMENT -> {
            isLoading = isLoadingDocuments
            pickMediaItems = documentUris
        }
    }

    LaunchedEffect(isLoadingImages) {
        if (isLoadingImages) {
            withContext(Dispatchers.IO) {
                if (imageUris.isEmpty()) {
                    val uris = loadMediaFromStore(
                        context,
                        MediaType.IMAGE
                    )
                    imageUris.addAll(uris)
                }
                isLoadingImages = false
            }
        }
    }
    LaunchedEffect(isLoadingVideos) {
        if (isLoadingVideos) {
            withContext(Dispatchers.IO) {
                if (videoUris.isEmpty()) {
                    val uris = loadMediaFromStore(
                        context,
                        MediaType.VIDEO
                    )
                    videoUris.addAll(uris)
                }
                isLoadingVideos = false
            }
        }
    }
    LaunchedEffect(isLoadingDocuments) {
        if (isLoadingDocuments) {
            withContext(Dispatchers.IO) {
                if (documentUris.isEmpty()) {
                    val uris = loadMediaFromStore(
                        context,
                        MediaType.DOCUMENT
                    )
                    documentUris.addAll(uris)
                }
                isLoadingDocuments = false
            }
        }
    }

    LaunchedEffect(pickMediaType) {
        when (pickMediaType) {
            MediaType.IMAGE -> {
                imagesPermissionGranted = ContextCompat.checkSelfPermission(context, imagesPermissions) == PackageManager.PERMISSION_GRANTED

                if (imagesPermissionGranted) {
                    isLoadingImages = true
                } else {
                    imagesPermissionLauncher.launch(imagesPermissions)
                }
            }
            MediaType.VIDEO -> {
                videosPermissionGranted = ContextCompat.checkSelfPermission(context, videosPermissions) == PackageManager.PERMISSION_GRANTED

                if (videosPermissionGranted) {
                    isLoadingVideos = true
                } else {
                    videosPermissionLauncher.launch(videosPermissions)
                }
            }
            MediaType.DOCUMENT -> {
                isLoadingDocuments = true
            }
        }
    }

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
                modifier = Modifier.size(310.sdp(), 360.sdp()),
                shape = RoundedCornerShape(8.sdp()),
                color = Color.White
            ) {
                Column {
                    LazyRow(
                        modifier = Modifier
                            .padding(
                                top = 10.sdp(),
                                bottom = 5.sdp()
                            )
                    ) {
                        item {
                            Text(
                                "IMAGES",
                                modifier = Modifier.width(150.sdp())
                                    .clickable { pickMediaType = MediaType.IMAGE }
                                    .padding(horizontal = 10.sdp()),
                                color = if (pickMediaType == MediaType.IMAGE) Color(0xFF73FAD3) else Color(
                                    0xFFE8E8E8
                                ),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                "VIDEOS",
                                modifier = Modifier.width(150.sdp())
                                    .clickable { pickMediaType = MediaType.VIDEO }
                                    .padding(horizontal = 10.sdp()),
                                color = if (pickMediaType == MediaType.VIDEO) Color(0xFF73FAD3) else Color(
                                    0xFFE8E8E8
                                ),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                "DOCUMENTS",
                                modifier = Modifier.width(150.sdp())
                                    .clickable { pickMediaType = MediaType.DOCUMENT }
                                    .padding(horizontal = 10.sdp()),
                                color = if (pickMediaType == MediaType.DOCUMENT) Color(0xFF73FAD3) else Color(
                                    0xFFE8E8E8
                                ),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                    if (pickMediaType == MediaType.DOCUMENT) {
                        Text(
                            stringResource(R.string.open_internal_storage),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.sdp(), vertical = 6.sdp())
                                .clickable {
                                    documentsPermissionLauncher.launch(arrayOf("*/*"))
                                },
                            color = Color(0xFF9ba69e),
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.titleMedium
                        )
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth().padding(5.sdp()),
                            1.sdp(),
                            Color(0xFFDAD9D9)
                        )
                    }
                    if (isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(if (pickMediaType == MediaType.DOCUMENT) 1 else 3),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(5.sdp())
                        ) {
                            if (pickMediaType == MediaType.IMAGE) {
                                item {
                                    Icon(
                                        imageVector = Icons.Filled.PhotoCamera,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .aspectRatio(1f)
                                            .padding(4.sdp())
                                            .fillMaxSize()
                                            .clickable { isTakePhoto = true }
                                            .background(
                                                Color(0xFF9ba69e),
                                                RoundedCornerShape(8.sdp())
                                            )
                                            .padding(16.sdp()),
                                        tint = Color.White
                                    )
                                }
                            }
                            items(pickMediaItems) { image ->
                                GalleryMediaItem(
                                    media = image,
                                    isSelected = selectedUris.contains(image),
                                    onSelectToggle = { selected ->
                                        val newSelection = selectedUris.toMutableList()
                                        if (selected) {
                                            newSelection.add(image)
                                        } else {
                                            newSelection.remove(image)
                                        }
                                        onSelectionChanged(newSelection)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (isTakePhoto) {
        TakePhoto(
            {
                val item = MediaItem(
                    uri = it,
                    type = MediaType.IMAGE,
                    name = "",
                    size = 0
                )

                val newImageUris = listOf(item).toMutableList()
                newImageUris.addAll(imageUris)
                imageUris.clear()
                imageUris.addAll(newImageUris)

                val newSelection = selectedUris.toMutableList()
                newSelection.add(item)
                onSelectionChanged(newSelection)
            },
            { isTakePhoto = false }
        )
    }
}

@kotlin.OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TakePhoto(
    onPhotoTaken: (Uri) -> Unit,
    toBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Состояние разрешений
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    // Управление камерой
    val imageCapture = remember { ImageCapture.Builder().build() }
    var cameraProvider: ProcessCameraProvider? by remember { mutableStateOf(null) }

    // Создание временного файла
    var currentTempFile by remember { mutableStateOf<File?>(null) }

    LaunchedEffect(Unit) {
        cameraPermissionState.launchPermissionRequest()
    }

    Dialog(
        {},
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            if (cameraPermissionState.status.isGranted) {
                AndroidView(
                    factory = { ctx ->
                        val previewView = PreviewView(ctx).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        }

                        val cameraExecutor = ContextCompat.getMainExecutor(ctx)
                        ProcessCameraProvider.getInstance(ctx).addListener({
                            cameraProvider = ProcessCameraProvider.getInstance(ctx).get().apply {
                                unbindAll()
                                bindToLifecycle(
                                    lifecycleOwner,
                                    CameraSelector.DEFAULT_BACK_CAMERA,
                                    imageCapture,
                                    Preview.Builder().build().apply {
                                        setSurfaceProvider(previewView.surfaceProvider)
                                    }
                                )
                            }
                        }, cameraExecutor)

                        previewView
                    },
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 48.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier.size(72.sdp())
                            .background(
                                Color.White.copy(alpha = 0.7f),
                                CircleShape
                            )
                            .clickable {
                                currentTempFile?.delete() // Удаляем предыдущий временный файл

                                val file = createTempImageFile(context).apply {
                                    currentTempFile = this
                                }

                                imageCapture.takePicture(
                                    ImageCapture.OutputFileOptions.Builder(file).build(),
                                    ContextCompat.getMainExecutor(context),
                                    object : ImageCapture.OnImageSavedCallback {
                                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                            saveImageToGallery(context, file)?.let {
                                                onPhotoTaken(it)
                                            }
                                            toBack()
                                        }

                                        override fun onError(ex: ImageCaptureException) {
                                            currentTempFile?.delete()
                                            Log.e("Photo", "NoSaved")
                                        }
                                    }
                                )
                            }
                    )
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopStart
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        modifier = Modifier
                            .padding(start = 16.dp, top = 16.dp)
                            .size(36.dp)
                            .clickable { toBack() },
                        tint = Color.White
                    )
                }
            } else {
                Text(
                    stringResource(R.string.camera_access_required),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun GalleryMediaItem(
    media: MediaItem,
    isSelected: Boolean,
    onSelectToggle: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val thumbnailBitmap = remember(media.uri) { mutableStateOf<Bitmap?>(null) }

    // Загрузка миниатюры для видео
    if (media.type == MediaType.VIDEO && thumbnailBitmap.value == null) {
        LaunchedEffect(media.uri) {
            withContext(Dispatchers.IO) {
                thumbnailBitmap.value = createVideoThumbnail(context, media.uri)
            }
        }
    }

    when(media.type) {
        MediaType.IMAGE -> {
            ImageItem(media, isSelected, onSelectToggle)
        }
        MediaType.VIDEO -> {
            VideoItem(media, isSelected, onSelectToggle)
        }
        MediaType.DOCUMENT -> {
            DocumentItem(media, isSelected, onSelectToggle)
        }
    }
}

@Composable
private fun ImageItem(
    media: MediaItem,
    isSelected: Boolean,
    onSelectToggle: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .clickable { onSelectToggle(!isSelected) }
    ) {
        AsyncImage(
            model = media.uri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .border(
                    if (isSelected) 3.sdp() else 0.sdp(),
                    if (isSelected) Color(0xFF73FAD3) else Color.Transparent,
                    RoundedCornerShape(8.dp)
                )
        )
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp),
                tint = Color(0xFF73FAD3)
            )
        }
    }
}

@Composable
private fun VideoItem(
    media: MediaItem,
    isSelected: Boolean,
    onSelectToggle: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val thumbnailBitmap = remember(media.uri) { mutableStateOf<Bitmap?>(null) }

    // Загрузка миниатюры для видео
    if (thumbnailBitmap.value == null) {
        LaunchedEffect(media.uri) {
            withContext(Dispatchers.IO) {
                thumbnailBitmap.value = createVideoThumbnail(context, media.uri)
            }
        }
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .clickable { onSelectToggle(!isSelected) }
    ) {
        thumbnailBitmap.value?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        if (isSelected) 3.sdp() else 0.sdp(),
                        if (isSelected) Color(0xFF73FAD3) else Color.Transparent,
                        RoundedCornerShape(8.dp)
                    )
            )
        } ?: run {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.sdp()),
                    strokeWidth = 2.sdp()
                )
            }
        }

        Text(
            text = media.duration.formatAsTime(),
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(4.dp)
                .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                .padding(horizontal = 4.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall
        )

        if (isSelected) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp),
                tint = Color(0xFF73FAD3)
            )
        }
    }
}

@Composable
private fun DocumentItem(
    media: MediaItem,
    isSelected: Boolean,
    onSelectToggle: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(start = 5.sdp(), end = 5.sdp(), bottom = 5.sdp())
            .background(
                Color(0xFFF6F6F6),
                RoundedCornerShape(8.dp)
            )
            .border(
                if (isSelected) 3.sdp() else 0.sdp(),
                if (isSelected) Color(0xFF73FAD3) else Color.Transparent,
                RoundedCornerShape(8.dp)
            )
            .clickable { onSelectToggle(!isSelected) }
    ) {
        Column(
            modifier = Modifier.padding(4.sdp())
        ) {
            Row {
                Text(
                    media.name.substring(0, media.name.indexOfLast { it == '.' }),
                    modifier = Modifier.weight(1f),
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    media.name.substring(media.name.indexOfLast { it == '.' }),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(Modifier.height(4.sdp()))
            Text(
                media.size.formatAsSize(),
                style = MaterialTheme.typography.bodySmall
            )
        }
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp),
                tint = Color(0xFF73FAD3)
            )
        }
    }
}

private fun createTempImageFile(context: Context): File {
    return File(
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        "temp_${System.currentTimeMillis()}.jpg"
    ).apply { createNewFile() }
}

// Создание миниатюры для видео
private fun createVideoThumbnail(context: Context, uri: Uri): Bitmap? {
    return try {
        val retriever = MediaMetadataRetriever().apply {
            setDataSource(context, uri)
        }
        retriever.frameAtTime
    } catch (e: Exception) {
        Log.e("Thumbnail", "Error creating video thumbnail", e)
        null
    }
}

fun getVideoDuration(context: Context, uri: Uri): Long {
    return try {
        MediaMetadataRetriever().use { retriever ->
            retriever.setDataSource(context, uri)
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0
        }
    } catch (e: Exception) {
        0
    }
}

fun getDocumentSize(uri: Uri): Long {
    return try {
        val file = File(uri.path ?: return 0)
        if (file.exists()) file.length() else 0
    } catch (e: Exception) {
        Log.e("DocumentSize", "Error getting document size", e)
        0
    }
}

private fun loadMediaFromStore(
    context: Context,
    mediaType: MediaType
): List<MediaItem> {
    return when (mediaType) {
        MediaType.IMAGE, MediaType.VIDEO -> {
            val projection = when(mediaType) {
                MediaType.IMAGE -> arrayOf(
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.SIZE
                )
                MediaType.VIDEO -> arrayOf(
                    MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.DISPLAY_NAME,
                    MediaStore.Video.Media.SIZE,
                    MediaStore.Video.Media.DURATION
                )
                else -> arrayOf()
            }
            val content = when(mediaType) {
                MediaType.IMAGE -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                MediaType.VIDEO -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                else -> MediaStore.Files.getContentUri("external")
            }
            val mediaList = mutableListOf<MediaItem>()
            val sortOrder = "${MediaStore.MediaColumns.DATE_ADDED} DESC"

            context.contentResolver.query(
                content,
                projection,
                null,
                null,
                sortOrder
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)
                val durationColumn = if (mediaType == MediaType.VIDEO) {
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                } else -1

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn) ?: "Unknown"
                    val size = cursor.getLong(sizeColumn)
                    val duration = if (durationColumn != -1) cursor.getLong(durationColumn) else 0L

                    val contentUri = ContentUris.withAppendedId(content, id)
                    mediaList.add(MediaItem(contentUri, mediaType, name, size, duration))
                }
            }
            mediaList
        }
        MediaType.DOCUMENT -> {
            return emptyList()
        }
    }
}

private fun loadDocumentsMetadata(
    context: Context,
    uris: List<Uri>
): List<MediaItem> {
    val resolver = context.contentResolver
    val result = mutableListOf<MediaItem>()

    uris.forEach { uri ->
        resolver.query(uri, null, null, null, null)?.use { cursor ->
            cursor.moveToFirst()
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

            result.add(
                MediaItem(
                    uri = uri,
                    type = MediaType.DOCUMENT,
                    name = nameIndex.takeIf { it >= 0 }?.let { cursor.getString(it) } ?: "",
                    size = sizeIndex.takeIf { it >= 0 }?.let { cursor.getLong(it) } ?: 0
                )
            )
        } ?: run {
            // Если запрос не удался, добавляем базовую информацию
            result.add(MediaItem(uri, MediaType.DOCUMENT, uri.lastPathSegment ?: "", 0))
        }
    }
    return result
}

private fun saveImageToGallery(context: Context, sourceFile: File): Uri? {
    val contentResolver = context.contentResolver
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "IMG_$timeStamp.jpg"

    val imageDetails = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, imageFileName)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        } else {
            put(
                MediaStore.MediaColumns.DATA,
                "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)}/$imageFileName"
            )
        }
    }

    return try {
        val imageUri = contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            imageDetails
        )

        imageUri?.let { uri ->
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                FileInputStream(sourceFile).use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            // Для Android Q+ снимаем флаг pending
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                imageDetails.clear()
                imageDetails.put(MediaStore.MediaColumns.IS_PENDING, 0)
                contentResolver.update(uri, imageDetails, null, null)
            }

            // Удаляем временный файл после копирования
            sourceFile.delete()

            uri
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@SuppressLint("DefaultLocale")
private fun Long.formatAsTime(): String {
    val seconds = this / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}

private fun Long.formatAsSize(): String {
    val fthis = this.toFloat()
    return when {
        fthis > 1024 * 1024 * 1024 -> "${"%.2f".format(fthis / 1024 / 1024 / 1024)} GB"
        fthis > 1024 * 1024 -> "${"%.2f".format(fthis / 1024 / 1024)} MB"
        fthis > 1024 -> "${"%.2f".format(fthis / 1024)} KB"
        else -> "$this Byte"
    }
}
