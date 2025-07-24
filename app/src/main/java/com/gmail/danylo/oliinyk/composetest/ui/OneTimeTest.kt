package com.gmail.danylo.oliinyk.composetest.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun ClearMeAfterUsage() {
    RedBoxWithTopFade()
}

@Composable
fun RedBoxWithTopFade() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Your content (fully red box)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red)
        )

        // Apply the fade mask using BlendMode.DstIn
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    // This blend mode uses the alpha channel of the overlay
                    blendMode = BlendMode.DstIn
                }
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,     // Top = invisible
                            Color.Black            // Bottom = visible
                        )
                    )
                )
        )
    }
}
