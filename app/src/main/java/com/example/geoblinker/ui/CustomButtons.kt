package com.example.geoblinker.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import com.example.geoblinker.R
import com.example.geoblinker.ui.theme.sdp

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
        border = BorderStroke(1.sdp(), Color.White)
    ) {
        Button(
            onClick = onClick,
            modifier = modifier.background(
                brush = greenGradient,
                shape = MaterialTheme.shapes.large)
                .fillMaxWidth()
                .padding(1.sdp())
                .height(height.sdp())
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
                        modifier = Modifier.width(23.sdp()).height(23.sdp())
                    )
                    Spacer(Modifier.width(17.sdp()))
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
fun GreenMediumButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    height: Int = 55,
    shape: Shape = MaterialTheme.shapes.medium,
    style: TextStyle = MaterialTheme.typography.bodyLarge
) {
    Surface(
        modifier = modifier,
        shape = shape,
        border = BorderStroke(1.sdp(), Color.White)
    ) {
        Button(
            onClick = onClick,
            modifier = modifier.background(
                brush = greenGradient,
                shape = shape)
                .fillMaxWidth()
                .padding(1.sdp())
                .height(height.sdp())
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
                        modifier = Modifier.width(13.sdp()).height(13.sdp())
                    )
                    Spacer(Modifier.width(17.sdp()))
                }
                Text(
                    text,
                    style = style
                )
            }
        }
    }
}

@Composable
fun GreenMediumRightIconButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    height: Int = 55,
    style: TextStyle = MaterialTheme.typography.bodyLarge
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.sdp()),
        border = BorderStroke(1.sdp(), Color.White)
    ) {
        Button(
            onClick = onClick,
            modifier = modifier.background(
                brush = greenGradient,
                shape = RoundedCornerShape(10.sdp()))
                .fillMaxWidth()
                .padding(1.sdp())
                .height(height.sdp())
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
                Text(
                    text,
                    style = style
                )
                icon?.let {
                    Spacer(Modifier.width(17.sdp()))
                    Icon(
                        imageVector = ImageVector.vectorResource(it),
                        contentDescription = null,
                        modifier = Modifier.width(18.sdp()).height(18.sdp())
                    )
                }
            }
        }
    }
}

@Composable
fun GreenSmallButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(105.sdp(), 50.sdp()),
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF73FAD3)
        )
    ) {
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun BlackButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    text: String,
    onClick: () -> Unit,
    textStyle: TextStyle = MaterialTheme.typography.headlineMedium,
    enabled: Boolean = true,
    height: Int = 81
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.sdp(), if (enabled) blackBorder else Color.White)
    ) {
        Button(
            onClick = onClick,
            modifier = modifier.background(
                brush = if (enabled) blackGradient else grayGradient,
                shape = MaterialTheme.shapes.large)
                .padding(1.sdp()) // Компенсируем границу Surface
                .height(height.sdp())
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
                        modifier = Modifier.width(23.sdp()).height(23.sdp())
                    )
                    Spacer(Modifier.width(17.sdp()))
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
fun BlackMediumButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Surface(
        shape = RoundedCornerShape(10.sdp()),
        border = BorderStroke(1.sdp(), if (enabled) blackBorder else Color.White)
    ) {
        Button(
            onClick = onClick,
            modifier = modifier.background(
                brush = if (enabled) blackGradient else grayGradient,
                shape = RoundedCornerShape(10.sdp()))
                .fillMaxWidth()
                .padding(1.sdp()) // Компенсируем границу Surface
                .height(55.sdp())
            ,
            enabled = enabled,
            shape = RoundedCornerShape(10.sdp()),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text,
                    color = if (enabled) Color.White else gray,
                    style = MaterialTheme.typography.bodyLarge
                )
                icon?.let {
                    Spacer(Modifier.width(16.sdp()))
                    Icon(
                        imageVector = ImageVector.vectorResource(it),
                        contentDescription = null,
                        modifier = Modifier.width(19.sdp()).height(19.sdp())
                    )
                }
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
        border = BorderStroke(1.sdp(), Color.White)
    ) {
        Button(
            onClick = onClick,
            modifier = modifier.background(
                brush = whiteGradient,
                shape = MaterialTheme.shapes.large)
                .padding(1.sdp()) // Компенсируем границу Surface
                .fillMaxWidth()
                .height(height.sdp())
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
                        modifier = Modifier.width(23.sdp()).height(23.sdp())
                    )
                    Spacer(Modifier.width(17.sdp()))
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
fun WhiteSmallButton(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        border = BorderStroke(1.sdp(), Color(0xFFDFDFDF))
    ) {
        Button(
            onClick,
            modifier = Modifier
                .size(105.sdp(), 50.sdp())
                .background(
                brush = whiteGradient,
                shape = MaterialTheme.shapes.small
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )
        ) {
            Text(
                text,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun WhiteRedMediumButton(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.sdp()),
        border = BorderStroke(1.sdp(), Color.White)
    ) {
        Button(
            onClick,
            modifier = Modifier
                .size(330.sdp(), 55.sdp())
                .background(
                    brush = whiteGradient,
                    shape = RoundedCornerShape(10.sdp())
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )
        ) {
            Text(
                text,
                color = Color(0xFFC4162D),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun RedButton(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.sdp()),
        color = Color(0xFFC4162D),
        border = BorderStroke(1.sdp(), Color.White)
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
                .height(55.sdp()),
            shape = RoundedCornerShape(10.sdp()),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )
        ) {
            Text(
                text,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun BackButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.offset(y = 275.sdp())
        ) {
            Surface(
                modifier = Modifier.shadow(
                    4.sdp(),
                    RoundedCornerShape(100.sdp()),
                    clip = false,
                    ambientColor = Color.Black,
                    spotColor = Color.Black.copy(0.25f)
                ),
                shape = RoundedCornerShape(100.sdp()),
                color = Color.White,
                border = BorderStroke(1.sdp(), Color.White)
            ) {
                IconButton(
                    onClick = onClick,
                    modifier = Modifier
                        .size(65.sdp(), 65.sdp())
                        .padding(1.sdp()),
                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFEFEFEF))
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.back),
                        contentDescription = stringResource(R.string.back),
                        modifier = Modifier.size(24.sdp())
                    )
                }
            }
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