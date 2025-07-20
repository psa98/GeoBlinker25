package com.example.geoblinker.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import com.example.geoblinker.R
import com.example.geoblinker.ui.theme.hdp
import com.example.geoblinker.ui.theme.sdp

enum class TypeColor {
    Green,
    Green1,
    Black,
    White,
    White1,
    WhiteRed,
    Blue,
    Red
}

val greenGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF92FFDF), Color(0xFF21E3A5))
)
val green1Gradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF73FAD3), Color(0xFF73FAD3))
)
val blackGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF373736), Color(0xFF212120))
)
val whiteGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFFFFFFFF), Color(0xFFEFEFEF))
)
val grayGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFFF2F2F2), Color(0xFFE0E0E0))
)
val blueGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF90C7EA), Color(0xFF4187D2))
)
val redGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFFC4162D), Color(0xFFC4162D))
)
val blackBorder = Color(0xFF212120)
val gray = Color(0xFF878787)

@Composable
fun CustomButton(
    modifier: Modifier = Modifier.fillMaxWidth(),
    text: String,
    onClick: () -> Unit,
    typeColor: TypeColor,
    leftIcon: ImageVector? = null,
    rightIcon: ImageVector? = null,
    iconSize: Int = 13,
    enabled: Boolean = true,
    height: Int = 65,
    radius: Int = 16,
    style: TextStyle = MaterialTheme.typography.bodyLarge
) {
    val border = when(typeColor) {
        TypeColor.White -> Color(0xFFDFDFDF)
        else -> Color.White
    }
    val brush = if (enabled) when(typeColor) {
        TypeColor.Green -> greenGradient
        TypeColor.Green1 -> green1Gradient
        TypeColor.Black -> blackGradient
        TypeColor.White -> whiteGradient
        TypeColor.White1 -> whiteGradient
        TypeColor.WhiteRed -> whiteGradient
        TypeColor.Blue -> blueGradient
        TypeColor.Red -> redGradient
    }
    else
        grayGradient
    val textColor = if (enabled) when(typeColor) {
        TypeColor.Green -> blackBorder
        TypeColor.Green1 -> blackBorder
        TypeColor.Black -> Color.White
        TypeColor.White -> Color.Black
        TypeColor.White1 -> Color.White
        TypeColor.WhiteRed -> Color(0xFFC4162D)
        TypeColor.Blue -> Color.White
        TypeColor.Red -> Color.White
    }
    else
        gray
    Button(
        onClick = onClick,
        modifier = modifier
            .height(height.hdp())
            .background(
                brush = brush,
                shape = RoundedCornerShape(radius.hdp())
            ),
        enabled = enabled,
        shape = RoundedCornerShape(radius.hdp()),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(
            1.hdp(),
            border
        ),
        contentPadding = PaddingValues(0.hdp())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            leftIcon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier
                        .width(iconSize.hdp())
                        .height(iconSize.hdp()),
                    tint = Color.Unspecified
                )
                Spacer(Modifier.width(17.hdp()))
            }
            Text(
                text,
                color = textColor,
                style = style
            )
            rightIcon?.let {
                Spacer(Modifier.width(17.hdp()))
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier
                        .width(iconSize.hdp())
                        .height(iconSize.hdp()),
                    tint = Color.Unspecified
                )
            }
        }
    }
}

@Composable
fun BackButton(
    onClick: () -> Unit,
    color: Color = Color.White,
    notPosition: Boolean = false
) {
    if (!notPosition) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier.offset(y = (-28).hdp())
            ) {
                Surface(
                    modifier = Modifier.shadow(
                        4.hdp(),
                        CircleShape,
                        clip = false,
                        ambientColor = Color.Black,
                        spotColor = Color.Black.copy(0.25f)
                    ),
                    shape = RoundedCornerShape(100.hdp()),
                    color = Color.White,
                    border = BorderStroke(1.hdp(), Color.White)
                ) {
                    IconButton(
                        onClick = onClick,
                        modifier = Modifier
                            .size(65.hdp(), 65.hdp())
                            .padding(1.hdp()),
                        colors = IconButtonDefaults.iconButtonColors(containerColor = color)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.back),
                            contentDescription = stringResource(R.string.back),
                            modifier = Modifier.size(24.hdp())
                        )
                    }
                }
            }
        }
    }
    else {
        Column {
            Surface(
                modifier = Modifier
                    .size(65.hdp())
                    .shadow(
                        4.hdp(),
                        CircleShape,
                        clip = false,
                        ambientColor = Color.Black,
                        spotColor = Color.Black.copy(0.25f)
                    ),
                shape = RoundedCornerShape(100.hdp()),
                color = Color.White,
                border = BorderStroke(1.hdp(), Color.White)
            ) {
                IconButton(
                    onClick = onClick,
                    colors = IconButtonDefaults.iconButtonColors(containerColor = color)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.back),
                        contentDescription = stringResource(R.string.back),
                        modifier = Modifier.size(24.hdp())
                    )
                }
            }
            HSpacer(28)
        }
    }
}

@Composable
fun OkButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(65.sdp())
            .background(
                brush = if (enabled) greenGradient else whiteGradient,
                shape = MaterialTheme.shapes.small
            ),
        enabled = enabled,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.Transparent
        )
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ok),
            contentDescription = null,
            modifier = Modifier.size(23.sdp(), 17.sdp()),
            tint = Color.Unspecified
        )
    }
}

@Composable
fun CircleCheckbox(
    checked: Boolean,
    onCheckedChange: () -> Unit
) {
    Surface(
        onClick = onCheckedChange,
        modifier = Modifier.size(16.sdp()),
        shape = RoundedCornerShape(100.sdp()),
        color = if (checked) Color(0xFF12CD4A) else Color.Unspecified,
        border = BorderStroke(
            1.sdp(),
            Color(0xFFDAD9D9).copy(alpha = 0.5f)
        )
    ) {}
}