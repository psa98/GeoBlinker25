package com.example.geoblinker.ui.main.device

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.geoblinker.R
import com.example.geoblinker.data.MarkersRepository
import com.example.geoblinker.ui.BackButton
import com.example.geoblinker.ui.main.viewmodel.DeviceViewModel
import com.example.geoblinker.ui.theme.sdp


@Composable
fun IconChooserScreen(

    viewModel: DeviceViewModel,
    toBack: () -> Unit
) {
    val device by viewModel.device.collectAsState()
    var selectedId by remember { mutableIntStateOf(device.markerId) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            device.name,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            stringResource(R.string.choose_icon_text),
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize = TextUnit(16f, TextUnitType.Sp),
                color = Color(0xFFaaaaaa)
            )
        )
        Spacer(Modifier.height(20.sdp()))
        Card(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(15.dp),
            colors = CardColors(
                containerColor = Color.White,
                contentColor = Color.White,
                disabledContainerColor = Color.White,
                disabledContentColor = Color.White
            )

        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                Modifier
                    .background(Color.White)
                    .padding(6.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp),
            ) {
                items(MarkersRepository.markerChoiceImageList.size) { id ->
                    Spacer(Modifier.height(20.sdp()))
                    Box(
                        modifier = Modifier
                            .height(100.dp) // Example fixed size for each item
                            .background(Color.White)
                            .padding(6.dp)

                    ) {

                        val bitmap = ImageBitmap.imageResource(MarkersRepository.markerChoiceImageList[id])
                        Image(bitmap, null,
                            Modifier
                                .background(Color.White)
                                .drawBehind {
                                    if (id==selectedId) drawCircle(
                                        color = Color.Green, radius = size.maxDimension/1.9f,
                                        style = Stroke(width = 10f)
                                    )
                                }
                                .clickable {
                                    viewModel.changeSelectedMarker(device = viewModel.device.value,id)
                                    selectedId = id
                                }
                        )
                    }
                    Spacer(Modifier.height(20.sdp()))
                }
            }
        }
    }

    BackButton(
        onClick = toBack
    )
}
