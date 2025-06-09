package com.example.geoblinker.ui.main.profile.techsupport

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.view.GestureDetector
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ActivityScenario.launch
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.example.geoblinker.R
import com.example.geoblinker.data.techsupport.MessageTechSupport
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.main.viewmodel.ChatsViewModel
import com.example.geoblinker.ui.theme.sdp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    val images = remember { mutableStateListOf<Uri>() }
    var showImagePicker by remember { mutableStateOf(false) }
    var showImagePreview by remember { mutableStateOf<String?>(null) }
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
        CustomImagePicker(
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

@Composable
fun CustomImagePicker(
    selectedUris: List<Uri>,
    onSelectionChanged: (List<Uri>) -> Unit,
    cancellation: () -> Unit
) {
    val context = LocalContext.current
    val imageUris = remember { mutableStateListOf<Uri>() }
    var isLoading by remember { mutableStateOf(true) }

    // Загрузка изображений из галереи
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val uris = loadGalleryImages(context)
            imageUris.addAll(uris)
            isLoading = false
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
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(5.sdp())
                    ) {
                        items(imageUris) { uri ->
                            GalleryImageItem(
                                uri = uri,
                                isSelected = selectedUris.contains(uri),
                                onSelectToggle = { selected ->
                                    val newSelection = selectedUris.toMutableList()
                                    if (selected) {
                                        newSelection.add(uri)
                                    } else {
                                        newSelection.remove(uri)
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

@Composable
fun GalleryImageItem(
    uri: Uri,
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
            model = uri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .border(
                    if (isSelected) 3.sdp() else 0.sdp(),
                    if (isSelected) Color(0xFF73FAD3) else Color.Transparent,
                    RoundedCornerShape(8.dp))
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

// Функция для загрузки изображений из галереи
private fun loadGalleryImages(context: Context): List<Uri> {
    val imageUris = mutableListOf<Uri>()
    val projection = arrayOf(MediaStore.Images.Media._ID)
    val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

    context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        sortOrder
    )?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val contentUri = ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
            )
            imageUris.add(contentUri)
        }
    }
    return imageUris
}
