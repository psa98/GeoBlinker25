package com.example.geoblinker.ui.authorization

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.geoblinker.R
import com.example.geoblinker.ui.theme.GeoBlinkerTheme

@Composable
fun FourScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.title_logo),
            contentDescription = null,
            modifier = Modifier.size(240.dp, 162.dp)
        )
        Spacer(Modifier.height(65.dp))
        Text(
            text = stringResource(R.string.welcome),
            lineHeight = 40.sp,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Константин Гусевский!",
            lineHeight = 40.sp,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(Modifier.height(100.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFourScreen() {
    GeoBlinkerTheme {
        FourScreen()
    }
}