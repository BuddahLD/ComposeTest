package com.gmail.danylo.oliinyk.composetest.easymigration

import android.graphics.Rect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val LocalInsets = compositionLocalOf {
    WindowInsets()
}
val LocalKeyboard = compositionLocalOf {
    KeyboardConfig()
}

val LocalCutout = compositionLocalOf {
    CutoutInfo()
}

data class Insets(
    val left: Dp = 0.dp,
    val top: Dp = 0.dp,
    val right: Dp = 0.dp,
    val bottom: Dp = 0.dp,
) {
    val hasSafeArea: Boolean = (left + top + right + bottom) > 0.dp
}

data class WindowInsets(
    val insets: Insets = Insets(),
    val insetsIgnoringVisibility: Insets = Insets(),
)

data class KeyboardConfig(
    val height: Dp = 0.dp,
) {
    val isVisible: Boolean
        get() = height > 0.dp
}

data class CutoutInfo(
    val boundingRects: List<Rect> = emptyList(),
)
