package com.example.geoblinker.ui.auth.authorization

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.geoblinker.R
import com.example.geoblinker.ui.CustomButton
import com.example.geoblinker.ui.HSpacer
import com.example.geoblinker.ui.TypeColor
import com.example.geoblinker.ui.theme.sdp

@Composable
fun OneScreen(
    twoScreen: () -> Unit,
    registrationScreen: () -> Unit,
    faqScreen: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HSpacer(60)
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.title_logo),
            contentDescription = null,
            modifier = Modifier.width(200.sdp()).height(135.sdp())
        )
        HSpacer(15)
        Text(
            stringResource(R.string.version),
            modifier = Modifier.alpha(0.7f),
            style = MaterialTheme.typography.titleSmall
        )
        HSpacer(134)
        CustomButton(
            text = stringResource(R.string.log_in_by_number),
            onClick = twoScreen,
            typeColor = TypeColor.Green,
            leftIcon = ImageVector.vectorResource(R.drawable.phone),
            iconSize = 23,
            height = 81,
            radius = 24,
            style = MaterialTheme.typography.headlineMedium
        )
        HSpacer(20)
        CustomButton(
            text = stringResource(R.string.new_user),
            onClick = registrationScreen,
            typeColor = TypeColor.Black,
            leftIcon = ImageVector.vectorResource(R.drawable.user_add),
            iconSize = 23,
            height = 81,
            radius = 24,
            style = MaterialTheme.typography.headlineMedium
        )
        HSpacer(50)
        Text(
            stringResource(R.string.problems_logging),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.clickable {
                faqScreen()
            }
        )
    }
}