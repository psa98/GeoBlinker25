package com.example.geoblinker.ui.auth.registration

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.example.geoblinker.R
import com.example.geoblinker.ui.HSpacer
import com.example.geoblinker.ui.theme.sdp
import com.example.geoblinker.ui.theme.ssp
import kotlinx.coroutines.delay

@Composable
fun FourScreen(
    name: String,
    main: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(4000)
        main()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Image(
            painter = painterResource(R.drawable.background_registration),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
    }

    Column(
        modifier = Modifier.width(310.sdp()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.registration_completed_successfully),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
        HSpacer(48)
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.title_logo),
            contentDescription = null,
            modifier = Modifier.size(240.sdp(), 162.sdp())
        )
        HSpacer(65)
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
        HSpacer(172)
    }
}