package com.gmail.danylo.oliinyk.composetest

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun createBrush(vararg alphas: Float): Brush {
    require(alphas.size >= 2) { "Should be at least two alphas" }

    val whiteOverlay = alphas.map {
        Color(0xFFFFFFFF).copy(alpha = it)
    }
    return Brush.verticalGradient(whiteOverlay)
}
