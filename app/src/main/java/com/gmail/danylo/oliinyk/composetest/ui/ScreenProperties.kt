package com.gmail.danylo.oliinyk.composetest.ui

import android.content.res.Configuration
import android.os.Build
import android.util.Log
import android.view.View
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsCompat

@Preview(showSystemUi = true)
@Composable
fun ScreenProperties() {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp
    var insets by remember { mutableStateOf(Insets()) }
    val orientation = LocalConfiguration.current.orientation
    Column {
        val orientationString = when (orientation) {
            Configuration.ORIENTATION_PORTRAIT -> "Portrait"
            Configuration.ORIENTATION_LANDSCAPE -> "Landscape"
            else -> "Undefined"
        }
        Text(text = "Screen orientation: $orientationString")
        Text(text = "Screen width dp: $screenWidth")
        Text(text = "Screen height dp: $screenHeight")
        Text(text = "Status bar height dp: ${insets.top}")
    }
    WindowInsets(onInsetsChanged = { insets = it })
}

@Composable
fun WindowInsets(onInsetsChanged: (Insets) -> Unit) {
    val view = LocalView.current
    val density = LocalDensity.current
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        DisposableEffect(view) {
            val listener = View.OnApplyWindowInsetsListener { _, windowInsets ->
                val systemInsets = windowInsets.getInsetsIgnoringVisibility(
                    WindowInsetsCompat.Type.displayCutout() or
                            WindowInsetsCompat.Type.statusBars() or
                            WindowInsetsCompat.Type.systemBars()
                )
                with(density) {
                    Log.d("відступи", "WindowInsets: $systemInsets")
                    onInsetsChanged(
                        Insets(
                            left = systemInsets.left.toDp(),
                            top = systemInsets.top.toDp(),
                            right = systemInsets.right.toDp(),
                            bottom = systemInsets.bottom.toDp()
                        )
                    )
                }
                windowInsets
            }
            view.rootView.setOnApplyWindowInsetsListener(listener)
            onDispose {
                view.rootView.setOnApplyWindowInsetsListener(null)
            }
        }
    }
}

data class Insets(
    val left: Dp = 0.dp,
    val top: Dp = 0.dp,
    val right: Dp = 0.dp,
    val bottom: Dp = 0.dp,
) {

    val hasSafeArea: Boolean = (left + top + right + bottom) > 0.dp
}
