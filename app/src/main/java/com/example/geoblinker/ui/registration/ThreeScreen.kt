package com.example.geoblinker.ui.registration

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.geoblinker.R
import com.example.geoblinker.ui.theme.GeoBlinkerTheme
import com.example.geoblinker.ui.theme.sdp
import com.example.geoblinker.ui.theme.ssp
import kotlinx.coroutines.delay

@Composable
fun ThreeScreen(
    name: String,
    main: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(4000)
        main()
    }

    // Фоновое изображение
    Image(
        painter = painterResource(R.drawable.background_registration),
        contentDescription = "Background",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.registration_completed_successfully),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(48.sdp()))
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.title_logo),
            contentDescription = null,
            modifier = Modifier.size(240.sdp(), 162.sdp())
        )
        Spacer(Modifier.height(65.sdp()))
        Text(
            text = stringResource(R.string.welcome),
            lineHeight = 40.ssp(),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "$name!",
            lineHeight = 40.ssp(),
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(Modifier.height(202.sdp()))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFourScreen() {
    GeoBlinkerTheme {
        ThreeScreen("Константин Гусевский", {})
    }
}