package com.example.geoblinker.ui.main.profile

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.window.Popup
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.geoblinker.R
import com.example.geoblinker.TimeUtils
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.BlackMediumButton
import com.example.geoblinker.ui.GreenMediumButton
import com.example.geoblinker.ui.WhiteMediumButton
import com.example.geoblinker.ui.main.viewmodel.AvatarViewModel
import com.example.geoblinker.ui.main.viewmodel.ProfileViewModel
import com.example.geoblinker.ui.theme.sdp

@Composable
fun ProfileScreen(
    viewModel: AvatarViewModel,
    profileViewModel: ProfileViewModel,
    toSubscription: () -> Unit,
    toListDevices: () -> Unit,
    toJournalSignals: () -> Unit,
    toSettings: () -> Unit,
    toNameSettings: () -> Unit,
    toTechSupport: () -> Unit,
    toAboutApp: () -> Unit,
    toAboutCompany: () -> Unit,
    toBack: () -> Unit
) {
    var isShow by remember { mutableStateOf(false) }
    val avatarUri by viewModel.avatarUri.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val subscription by profileViewModel.subscription.collectAsState()
    val name by profileViewModel.name.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (avatarUri == null) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.user_without_photo),
                contentDescription = null,
                modifier = Modifier.size(84.sdp()).clickable { isShow = true },
                tint = Color.Unspecified
            )
        }
        else {
            AsyncImage(
                avatarUri,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(84.sdp())
                    .clickable { isShow = true },
                contentScale = ContentScale.Crop
            )
        }
        Spacer(Modifier.height(10.sdp()))
        Row(
            modifier = Modifier.clickable { toNameSettings() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                name,
                color = Color(0xFF212120),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.width(13.sdp()))
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.pencil),
                contentDescription = null,
                modifier = Modifier.size(10.sdp()),
                tint = Color.Unspecified
            )
        }
        Spacer(Modifier.height(10.sdp()))
        Text(
            buildAnnotatedString {
                append("Подписка активна до: ")
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.typography.bodyMedium.color,
                        fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize
                    )
                ) {
                    append(TimeUtils.formatToLocalTimeDate(subscription))
                }
            },
            color = Color(0xFF737373),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(28.sdp()))
        GreenMediumButton(
            text = "Купить подписку",
            onClick = toSubscription,
            shape = RoundedCornerShape(10.sdp())
        )
        Spacer(Modifier.height(10.sdp()))
        Row {
            BlackMediumButton(
                modifier = Modifier.width(160.sdp()),
                text = "Мои устройства",
                onClick = toListDevices
            )
            Spacer(Modifier.width(10.sdp()))
            BlackMediumButton(
                modifier = Modifier.width(160.sdp()),
                text = "Журнал сигналов",
                onClick = toJournalSignals
            )
        }
        Spacer(Modifier.height(10.sdp()))
        BlackMediumButton(
            text = "Настройки",
            onClick = toSettings,
            icon = R.drawable.settings
        )
        Spacer(Modifier.height(20.sdp()))
        Row {
            WhiteMediumButton(
                modifier = Modifier.width(160.sdp()),
                text = "Техподдержка",
                onClick = toTechSupport
            )
            Spacer(Modifier.width(10.sdp()))
            WhiteMediumButton(
                modifier = Modifier.width(160.sdp()),
                text = "Оставить отзыв",
                onClick = {}
            )
        }
        Spacer(Modifier.height(22.sdp()))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ConstraintLayout(
                modifier = Modifier.wrapContentWidth().clickable { toAboutApp() }
            ) {
                val (text, line) = createRefs()

                Text(
                    stringResource(R.string.about_app),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.constrainAs(text) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )

                Box(
                    modifier = Modifier
                        .height(1.sdp())
                        .background(Color(0xFF737373))
                        .constrainAs(line) {
                            top.linkTo(text.bottom)
                            start.linkTo(text.start)
                            end.linkTo(text.end)
                            width = Dimension.fillToConstraints
                        }
                )
            }
            ConstraintLayout(
                modifier = Modifier.wrapContentWidth().clickable { toAboutCompany() }
            ) {
                val (text, line) = createRefs()

                Text(
                    stringResource(R.string.about_company),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.constrainAs(text) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )

                Box(
                    modifier = Modifier
                        .height(1.sdp())
                        .background(Color(0xFF737373))
                        .constrainAs(line) {
                            top.linkTo(text.bottom)
                            start.linkTo(text.start)
                            end.linkTo(text.end)
                            width = Dimension.fillToConstraints
                        }
                )
            }
        }
    }

    BackButton(
        onClick = toBack
    )

    if (isShow) {
        val context = LocalContext.current
        val pickImageLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            uri?.let { viewModel.handleSelectedImage(it) }
        }

        val permissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // Если разрешение получено - запускаем выбор изображения
                pickImageLauncher.launch("image/*")
            } else {
                viewModel.setErrorMessage("Доступ к галерее запрещен")
            }
        }

        Popup {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color.Black.copy(alpha = 0.2f)
                    )
                    .clickable {
                        isShow = false
                    },
                contentAlignment = Alignment.TopCenter
            ) {
                Surface(
                    modifier = Modifier.width(264.sdp()).offset(y = 50.sdp()),
                    color = Color.White,
                    shape = RoundedCornerShape(16.sdp())
                ) {
                    Column(
                        modifier = Modifier.padding(
                            start = 20.sdp(),
                            top = 25.sdp(),
                            end = 20.sdp()
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (avatarUri == null) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.user_without_photo),
                                contentDescription = null,
                                modifier = Modifier.size(125.sdp()).clickable { isShow = true },
                                tint = Color.Unspecified
                            )
                        } else {
                            AsyncImage(
                                avatarUri,
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(125.sdp())
                                    .clickable { isShow = true },
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(Modifier.height(25.sdp()))
                        if (avatarUri == null) {
                            Text(
                                "Установить фото",
                                modifier = Modifier.clickable {
                                    // Проверяем разрешение
                                    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        Manifest.permission.READ_MEDIA_IMAGES
                                    } else {
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                    }

                                    if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                                        pickImageLauncher.launch("image/*")
                                    } else {
                                        permissionLauncher.launch(permission)
                                    }
                                },
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                        else {
                            var isCheck by remember { mutableStateOf(false) }

                            Text(
                                "Сменить фото",
                                modifier = Modifier.clickable {
                                    isCheck = false
                                    // Проверяем разрешение
                                    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        Manifest.permission.READ_MEDIA_IMAGES
                                    } else {
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                    }

                                    if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                                        pickImageLauncher.launch("image/*")
                                    } else {
                                        permissionLauncher.launch(permission)
                                    }
                                },
                                style = MaterialTheme.typography.headlineSmall
                            )
                            if (isCheck) {
                                Spacer(Modifier.height(40.sdp()))
                                Text(
                                    "Вы действительно желаете удалить фото аккаунта?",
                                    color = Color(0xFFC4162D),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                            Spacer(Modifier.height(15.sdp()))
                            Text(
                                "Удалить фото",
                                modifier = Modifier.clickable {
                                    if (!isCheck)
                                        isCheck = true
                                    else
                                        viewModel.removeAvatar()
                                },
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                        Spacer(Modifier.height(40.sdp()))
                        errorMessage?.let {
                            Text(
                                it,
                                color = Color(0xFFC4162D),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelMedium
                            )
                            Spacer(Modifier.height(30.sdp()))
                        }
                    }
                }
            }
        }
    }
}
