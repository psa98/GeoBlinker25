package com.example.geoblinker.ui.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.geoblinker.R
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.CustomButton
import com.example.geoblinker.ui.TypeColor
import com.example.geoblinker.ui.main.profile.about.AboutCompany
import com.example.geoblinker.ui.theme.sdp

@Composable
fun ThreeScreen(
    toFourScreen: () -> Unit,
    toBack: () -> Unit
) {
    var selectPublicOffer by remember { mutableStateOf(false) }
    var selectPrivacyPolicy by remember { mutableStateOf(false) }

    var layoutResultPublicOffer by remember { mutableStateOf<TextLayoutResult?>(null) }
    var layoutResultPrivacyPolicy by remember { mutableStateOf<TextLayoutResult?>(null) }

    val annotatedStringPublicOffer = buildAnnotatedString {
        append(stringResource(R.string.confirmation_public_offer) + " ")
        pushStringAnnotation(tag = "CLICKABLE_TAG", annotation = "custom_action")
        withStyle(SpanStyle(
            color = Color.Blue,
            fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
            fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
            fontSize = MaterialTheme.typography.labelMedium.fontSize,
            textDecoration = TextDecoration.Underline
        )) {
            append(stringResource(R.string.confirmation_public_offer_2))
        }
        pop()
    }
    val annotatedStringPrivacyPolicy = buildAnnotatedString {
        append(stringResource(R.string.confirmation_privacy_policy) + " ")
        pushStringAnnotation(tag = "CLICKABLE_TAG", annotation = "custom_action")
        withStyle(SpanStyle(
            color = Color.Blue,
            fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
            fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
            fontSize = MaterialTheme.typography.labelMedium.fontSize,
            textDecoration = TextDecoration.Underline
        )) {
            append(stringResource(R.string.confirmation_privacy_policy_2))
        }
        pop()
    }

    var selectAbout by remember { mutableStateOf<AboutCompany?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.completion_registration),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(Modifier.height(58.sdp()))
        Row {
            Box(
                modifier = Modifier
                    .padding(top = 4.sdp(), end = 15.sdp())
                    .size(21.sdp())
                    .border(
                        1.sdp(),
                        Color(0xFFBEBEBE),
                        RoundedCornerShape(4.sdp())
                    )
                    .clickable { selectPublicOffer = !selectPublicOffer },
                contentAlignment = Alignment.Center
            ) {
                if (selectPublicOffer) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.green_tick),
                        contentDescription = null,
                        modifier = Modifier.padding(start = 4.sdp()).size(23.sdp(), 17.sdp()),
                        tint = Color.Unspecified
                    )
                }
            }
            BasicText(
                text = annotatedStringPublicOffer,
                style = MaterialTheme.typography.bodyMedium,
                onTextLayout = { layoutResultPublicOffer = it },
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures { offsetPosition ->
                        layoutResultPublicOffer?.let { layout ->
                            val position = layout.getOffsetForPosition(offsetPosition)

                            annotatedStringPublicOffer.getStringAnnotations(
                                tag = "CLICKABLE_TAG",
                                start = position,
                                end = position
                            ).firstOrNull()?.let { _ ->
                                selectAbout = AboutCompany.PublicOffer
                            }
                        }
                    }
                }
            )
        }
        Spacer(Modifier.height(20.sdp()))
        Row {
            Box(
                modifier = Modifier
                    .padding(top = 4.sdp(), end = 15.sdp())
                    .size(21.sdp())
                    .border(
                        1.sdp(),
                        Color(0xFFBEBEBE),
                        RoundedCornerShape(4.sdp())
                    )
                    .clickable { selectPrivacyPolicy = !selectPrivacyPolicy },
                contentAlignment = Alignment.Center
            ) {
                if (selectPrivacyPolicy) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.green_tick),
                        contentDescription = null,
                        modifier = Modifier.padding(start = 4.sdp()).size(23.sdp(), 17.sdp()),
                        tint = Color.Unspecified
                    )
                }
            }
            BasicText(
                text = annotatedStringPrivacyPolicy,
                style = MaterialTheme.typography.bodyMedium,
                onTextLayout = { layoutResultPrivacyPolicy = it },
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures { offsetPosition ->
                        layoutResultPrivacyPolicy?.let { layout ->
                            val position = layout.getOffsetForPosition(offsetPosition)

                            annotatedStringPrivacyPolicy.getStringAnnotations(
                                tag = "CLICKABLE_TAG",
                                start = position,
                                end = position
                            ).firstOrNull()?.let { _ ->
                                selectAbout = AboutCompany.PrivacyPolicy
                            }
                        }
                    }
                }
            )
        }
        Spacer(Modifier.height(95.sdp()))
        CustomButton(
            text = stringResource(R.string.confirm),
            onClick = toFourScreen,
            typeColor = TypeColor.Green,
            enabled = selectPublicOffer && selectPrivacyPolicy,
            style = MaterialTheme.typography.headlineMedium
        )
    }

    BackButton(
        onClick = toBack
    )

    selectAbout?.let {
        Dialog(
            {},
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.2f))
                    .clickable { selectAbout = null },
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.width(310.sdp()).heightIn(max = 412.sdp()),
                    shape = RoundedCornerShape(8.sdp()),
                    color = Color.White
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(start = 15.sdp(), top = 20.sdp(), end = 15.sdp(), bottom = 10.sdp()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Text(
                                stringResource(it.title),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(Modifier.height(20.sdp()))
                            Text(
                                stringResource(it.description),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}