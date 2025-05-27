package com.example.geoblinker.ui.authorization

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.geoblinker.R
import com.example.geoblinker.ui.BlackButton
import com.example.geoblinker.ui.GreenButton
import com.example.geoblinker.ui.theme.GeoBlinkerTheme
import com.example.geoblinker.ui.theme.hdp

@Composable
fun OneScreen(
    twoScreen: () -> Unit,
    registrationScreen: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(88.hdp()))
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.title_logo),
            contentDescription = null,
            modifier = Modifier.width(200.hdp()).height(135.hdp())
        )
        Spacer(Modifier.height(15.hdp()))
        Text(
            stringResource(R.string.version),
            modifier = Modifier.alpha(0.7f),
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(Modifier.height(134.hdp()))
        GreenButton(
            icon = R.drawable.phone,
            text = stringResource(R.string.log_in_by_number),
            onClick = twoScreen
        )
        Spacer(Modifier.height(20.hdp()))
        BlackButton(
            icon = R.drawable.user_add,
            text = stringResource(R.string.new_user),
            onClick = registrationScreen
        )
        Spacer(Modifier.height(50.hdp()))
        Text(
            stringResource(R.string.problems_logging),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOne() {
    GeoBlinkerTheme {
        OneScreen({}, {})
    }
}