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
import androidx.compose.foundation.layout.requiredWidth
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
import coil.compose.AsyncImagePainter
import com.example.geoblinker.R
import com.example.geoblinker.TimeUtils
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.CustomButton
import com.example.geoblinker.ui.HSpacer
import com.example.geoblinker.ui.TypeColor
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
    val avatarUri = viewModel.avatarUri
    val errorMessage = viewModel.errorMessage
    val subscription by profileViewModel.subscription.collectAsState()
    val name = profileViewModel.name

    Column(
        modifier = Modifier.requiredWidth(330.sdp()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            var success by remember { mutableStateOf(false) }
            AsyncImage(
                avatarUri,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(84.sdp())
                    .clickable { isShow = true },
                contentScale = ContentScale.Crop,
                onState = { state ->
                    success = when (state) {
                        is AsyncImagePainter.State.Loading -> false
                        is AsyncImagePainter.State.Success -> true
                        else -> false
                    }
                }
            )

            if (!success) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.user_without_photo),
                    contentDescription = null,
                    modifier = Modifier.size(84.sdp()).clickable { isShow = true },
                    tint = Color.Unspecified
                )
            }
        }
        HSpacer(10)
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
        HSpacer(10)
        Text(
            buildAnnotatedString {
                append(stringResource(R.string.subscription_active_until) + " ")
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
        HSpacer(28)
        CustomButton(
            text = stringResource(R.string.buy_subscription),
            onClick = toSubscription,
            typeColor = TypeColor.Green,
            height = 55,
            radius = 10,
            style = MaterialTheme.typography.bodyMedium
        )
        HSpacer(10)
        Row {
            CustomButton(
                modifier = Modifier.width(160.sdp()),
                text = stringResource(R.string.my_devices),
                onClick = toListDevices,
                typeColor = TypeColor.Black,
                height = 55,
                radius = 10,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.width(10.sdp()))
            CustomButton(
                modifier = Modifier.width(160.sdp()),
                text = stringResource(R.string.signal_log),
                onClick = toJournalSignals,
                typeColor = TypeColor.Black,
                height = 55,
                radius = 10,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        HSpacer(10)
        CustomButton(
            text = stringResource(R.string.settings),
            onClick = toSettings,
            typeColor = TypeColor.Black,
            rightIcon = R.drawable.settings,
            iconSize = 19,
            height = 55,
            radius = 10,
            style = MaterialTheme.typography.bodyMedium
        )
        HSpacer(20)
        Row {
            CustomButton(
                modifier = Modifier.width(160.sdp()),
                text = stringResource(R.string.tech_support),
                onClick = toTechSupport,
                typeColor = TypeColor.White,
                height = 55,
                radius = 10,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.width(10.sdp()))
            CustomButton(
                modifier = Modifier.width(160.sdp()),
                text = stringResource(R.string.leave_review),
                onClick = {},
                typeColor = TypeColor.White,
                height = 55,
                radius = 10,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        HSpacer(22)
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
        val message = stringResource(R.string.access_gallery_prohibited)
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
                viewModel.updateErrorMessage(message)
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
                        var success by remember { mutableStateOf(false) }
                        Box {
                            AsyncImage(
                                avatarUri,
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(125.sdp()),
                                contentScale = ContentScale.Crop,
                                onState = { state ->
                                    success = when (state) {
                                        is AsyncImagePainter.State.Loading -> false
                                        is AsyncImagePainter.State.Success -> true
                                        else -> false
                                    }
                                }
                            )

                            if (!success) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.user_without_photo),
                                    contentDescription = null,
                                    modifier = Modifier.size(125.sdp()),
                                    tint = Color.Unspecified
                                )
                            }
                        }
                        HSpacer(25)
                        if (!success) {
                            Text(
                                stringResource(R.string.set_photo),
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
                                stringResource(R.string.change_photo),
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
                                HSpacer(40)
                                Text(
                                    stringResource(R.string.del_photo_description),
                                    color = Color(0xFFC4162D),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                            HSpacer(15)
                            Text(
                                stringResource(R.string.del_photo),
                                modifier = Modifier.clickable {
                                    if (!isCheck)
                                        isCheck = true
                                    else
                                        viewModel.removeAvatar(true)
                                },
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                        HSpacer(40)
                        errorMessage?.let {
                            Text(
                                it,
                                color = Color(0xFFC4162D),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelMedium
                            )
                            HSpacer(30)
                        }
                    }
                }
            }
        }
    }
}
