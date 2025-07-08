package com.gmail.danylo.oliinyk.composetest.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.gmail.danylo.oliinyk.composetest.easymigration.Colors

@Composable
fun ClearMeAfterUsage() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.MonochromeBackground),
        //        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .width(180.dp)
                .fillMaxHeight()
                .background(color = White),
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .width(200.dp)
                .fillMaxHeight()
        ) {
            LazyColumn {
                items(count = 20) {
                    Row(modifier = Modifier.padding(16.dp)) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    Colors.BrandPrimary,
                                    shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp)
                                )
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .background(
                                    White,
                                    shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp)
                                )
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .dropShadow(
                    shape = RectangleShape,
                    shadow = Shadow(
                        radius = 4.dp,
                        spread = (-2).dp,
                        offset = DpOffset(x = 0.dp, y = (-2).dp),
                        blendMode = BlendMode.Darken,
                        color = Colors.MonochromeBackground
                    )
                )
                .fillMaxWidth()
                .height(64.dp)
                .background(White)
        )
    }
}
