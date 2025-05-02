package com.example.geoblinker.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.geoblinker.R

val greenGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF92FFDF), Color(0xFF21E3A5))
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
val blackBorder = Color(0xFF212120)
val gray = Color(0xFF878787)

@Composable
fun GreenButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    height: Int = 81
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.dp, Color.White)
    ) {
        Button(
            onClick = onClick,
            modifier = modifier.background(
                brush = greenGradient,
                shape = MaterialTheme.shapes.large)
                .fillMaxWidth()
                .padding(1.dp)
                .height(height.dp)
            ,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = blackBorder
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon?.let {
                    Icon(
                        imageVector = ImageVector.vectorResource(it),
                        contentDescription = null,
                        modifier = Modifier.width(23.dp).height(23.dp)
                    )
                    Spacer(Modifier.width(17.dp))
                }
                Text(
                    text,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}

@Composable
fun BlackButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    text: String,
    onClick: () -> Unit,
    textStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.headlineMedium,
    enabled: Boolean = true,
    height: Int = 81
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.dp, if (enabled) blackBorder else Color.White)
    ) {
        Button(
            onClick = onClick,
            modifier = modifier.background(
                brush = if (enabled) blackGradient else grayGradient,
                shape = MaterialTheme.shapes.large)
                .padding(1.dp) // Компенсируем границу Surface
                .height(height.dp)
            ,
            enabled = enabled,
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon?.let {
                    Icon(
                        imageVector = ImageVector.vectorResource(it),
                        contentDescription = null,
                        modifier = Modifier.width(23.dp).height(23.dp)
                    )
                    Spacer(Modifier.width(17.dp))
                }
                Text(
                    text,
                    color = if (enabled) Color.White else gray,
                    style = textStyle
                )
            }
        }
    }
}

@Composable
fun WhiteButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    height: Int = 81
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.dp, Color.White)
    ) {
        Button(
            onClick = onClick,
            modifier = modifier.background(
                brush = whiteGradient,
                shape = MaterialTheme.shapes.large)
                .padding(1.dp) // Компенсируем границу Surface
                .fillMaxWidth()
                .height(height.dp)
            ,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = blackBorder
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon?.let {
                    Icon(
                        imageVector = ImageVector.vectorResource(icon),
                        contentDescription = null,
                        modifier = Modifier.width(23.dp).height(23.dp)
                    )
                    Spacer(Modifier.width(17.dp))
                }
                Text(
                    text,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}

@Composable
fun BackButton(
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(100.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color.White)
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(65.dp, 65.dp)
                .padding(1.dp)
            ,
            colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFEFEFEF))
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.back),
                contentDescription = stringResource(R.string.back)
            )
        }
    }
}

@Composable
fun BackWhiteButton(
    onClick: () -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE8E8E8))
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .background(whiteGradient, MaterialTheme.shapes.small)
                .size(65.dp, 65.dp)
                .padding(1.dp)
            ,
            colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFEFEFEF))
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.back),
                contentDescription = stringResource(R.string.back)
            )
        }
    }
}