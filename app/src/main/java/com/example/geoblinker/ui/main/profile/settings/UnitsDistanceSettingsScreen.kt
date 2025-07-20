package com.example.geoblinker.ui.main.profile.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.geoblinker.R
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.main.viewmodel.DeviceViewModel
import com.example.geoblinker.ui.theme.sdp

@Composable
fun UnitDistanceSettingsScreen(
    viewModel: DeviceViewModel,
    toBack: () -> Unit
) {
    val unitsDistance by viewModel.unitsDistance

    Text(
        stringResource(R.string.units_distance_map),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold
        )
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(128.sdp()))
        Surface(
            modifier = Modifier
                .size(229.sdp(), 48.sdp())
                .clickable { viewModel.updateUnitsDistance(true) },
            shape = RoundedCornerShape(99.sdp()),
            color = Color.Unspecified,
            border = BorderStroke(
                1.sdp(),
                if (unitsDistance) Color(0xFF12CD4A) else Color(0xFF999696)
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.kilometers_meters),
                    color = if (unitsDistance) Color(0xFF12CD4A) else Color.Unspecified,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        Spacer(Modifier.height(15.sdp()))
        Surface(
            modifier = Modifier
                .size(229.sdp(), 48.sdp())
                .clickable { viewModel.updateUnitsDistance(false) },
            shape = RoundedCornerShape(99.sdp()),
            color = Color.Unspecified,
            border = BorderStroke(
                1.sdp(),
                if (!unitsDistance) Color(0xFF12CD4A) else Color(0xFF999696)
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.miles_feet),
                    color = if (!unitsDistance) Color(0xFF12CD4A) else Color.Unspecified,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }

    BackButton(
        onClick = toBack
    )
}