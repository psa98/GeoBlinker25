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
import com.example.geoblinker.ui.theme.hdp
import com.example.geoblinker.ui.theme.hdp

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
val blueGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF90C7EA), Color(0xFF4187D2))
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
        border = BorderStroke(1.hdp(), Color.White)
    ) {
        Button(
            onClick = onClick,
            modifier = modifier.background(
                brush = greenGradient,
                shape = MaterialTheme.shapes.large)
                .size(310.hdp(), height.hdp())
                .padding(1.hdp())
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
                        modifier = Modifier.width(23.hdp()).height(23.hdp())
                    )
                    Spacer(Modifier.width(17.hdp()))
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
        border = BorderStroke(1.hdp(), Color.White)
    ) {
        Button(
            onClick = onClick,
            modifier = modifier.background(
                brush = greenGradient,
                shape = shape)
                .fillMaxWidth()
                .height(height.hdp())
                .padding(1.hdp())
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
                        modifier = Modifier.width(13.hdp()).height(13.hdp())
                    )
                    Spacer(Modifier.width(17.hdp()))
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
        shape = RoundedCornerShape(10.hdp()),
        border = BorderStroke(1.hdp(), Color.White)
    ) {
        Button(
            onClick = onClick,
            modifier = modifier.background(
                brush = greenGradient,
                shape = RoundedCornerShape(10.hdp()))
                .fillMaxWidth()
                .height(height.hdp())
                .padding(1.hdp())
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
                    Spacer(Modifier.width(17.hdp()))
                    Icon(
                        imageVector = ImageVector.vectorResource(it),
                        contentDescription = null,
                        modifier = Modifier.width(18.hdp()).height(18.hdp())
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
        modifier = Modifier.size(105.hdp(), 50.hdp()),
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
        border = BorderStroke(1.hdp(), if (enabled) blackBorder else Color.White)
    ) {
        Button(
            onClick = onClick,
            modifier = modifier.background(
                brush = if (enabled) blackGradient else grayGradient,
                shape = MaterialTheme.shapes.large)
                .size(310.hdp(), height.hdp())
                .padding(1.hdp()) // Компенсируем границу Surface
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
                        modifier = Modifier.width(23.hdp()).height(23.hdp())
                    )
                    Spacer(Modifier.width(17.hdp()))
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
        shape = RoundedCornerShape(10.hdp()),
        border = BorderStroke(1.hdp(), if (enabled) blackBorder else Color.White)
    ) {
        Button(
            onClick = onClick,
            modifier = modifier.background(
                brush = if (enabled) blackGradient else grayGradient,
                shape = RoundedCornerShape(10.hdp()))
                .fillMaxWidth()
                .height(55.hdp())
                .padding(1.hdp()) // Компенсируем границу Surface
            ,
            enabled = enabled,
            shape = RoundedCornerShape(10.hdp()),
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
                    Spacer(Modifier.width(16.hdp()))
                    Icon(
                        imageVector = ImageVector.vectorResource(it),
                        contentDescription = null,
                        modifier = Modifier.width(19.hdp()).height(19.hdp())
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
        border = BorderStroke(1.hdp(), Color.White)
    ) {
        Button(
            onClick = onClick,
            modifier = modifier.background(
                brush = whiteGradient,
                shape = MaterialTheme.shapes.large)
                .fillMaxWidth()
                .height(height.hdp())
                .padding(1.hdp()) // Компенсируем границу Surface
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
                        modifier = Modifier.width(23.hdp()).height(23.hdp())
                    )
                    Spacer(Modifier.width(17.hdp()))
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
        border = BorderStroke(1.hdp(), Color(0xFFDFDFDF))
    ) {
        Button(
            onClick,
            modifier = Modifier
                .size(105.hdp(), 50.hdp())
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
        shape = RoundedCornerShape(10.hdp()),
        border = BorderStroke(1.hdp(), Color.White)
    ) {
        Button(
            onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.hdp())
                .background(
                    brush = whiteGradient,
                    shape = RoundedCornerShape(10.hdp())
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
        shape = RoundedCornerShape(10.hdp()),
        color = Color(0xFFC4162D),
        border = BorderStroke(1.hdp(), Color.White)
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
                .height(55.hdp()),
            shape = RoundedCornerShape(10.hdp()),
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
fun BlueButton(
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.hdp()),
        border = BorderStroke(1.hdp(), Color.White)
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.hdp())
                .background(
                    if (enabled) blueGradient else whiteGradient,
                    RoundedCornerShape(10.hdp())
                ),
            enabled = enabled,
            shape = RoundedCornerShape(10.hdp()),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            )
        ) {
            Text(
                text,
                color = if (enabled) Color.White else Color(0xFF878787),
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
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier.offset(y = (-28).hdp())
        ) {
            Surface(
                modifier = Modifier.shadow(
                    4.hdp(),
                    RoundedCornerShape(100.hdp()),
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
                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFEFEFEF))
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

@Composable
fun OkButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(65.hdp())
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
            modifier = Modifier.size(23.hdp(), 17.hdp()),
            tint = Color.Unspecified
        )
    }
}