package com.gmail.danylo.oliinyk.composetest.easymigration

import android.annotation.SuppressLint
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowInsetsCompat
import com.gmail.danylo.oliinyk.composetest.R
import com.gmail.danylo.oliinyk.composetest.easymigration.Colors.BrandAccent
import com.gmail.danylo.oliinyk.composetest.easymigration.Colors.BrandAccent75
import com.gmail.danylo.oliinyk.composetest.easymigration.Colors.BrandPrimary
import com.gmail.danylo.oliinyk.composetest.easymigration.Colors.BrandSecondary
import com.gmail.danylo.oliinyk.composetest.easymigration.Colors.FontDefault
import com.gmail.danylo.oliinyk.composetest.easymigration.Colors.MonochromeBackground
import com.gmail.danylo.oliinyk.composetest.easymigration.Colors.MonochromeDividerDark
import com.gmail.danylo.oliinyk.composetest.easymigration.Colors.MonochromeGrey
import com.gmail.danylo.oliinyk.composetest.easymigration.VersionChecker.isApi28orAbove
import com.gmail.danylo.oliinyk.composetest.easymigration.VersionChecker.isApi30orAbove

private const val DEFAULT_ALPHA_VALUE = .15f
private val DefaultAlpha = RippleAlpha(
    draggedAlpha = DEFAULT_ALPHA_VALUE,
    focusedAlpha = DEFAULT_ALPHA_VALUE,
    hoveredAlpha = DEFAULT_ALPHA_VALUE,
    pressedAlpha = DEFAULT_ALPHA_VALUE
)

private val themeColors = lightColorScheme(
    primary = BrandPrimary,
    onPrimary = Color.White,
    secondary = BrandSecondary,
    onSecondary = Color.White,
    tertiary = BrandAccent,
    onTertiary = MonochromeGrey,
    error = BrandAccent75,
    onError = Color.White,
    background = MonochromeBackground,
    onBackground = MonochromeGrey,
    surface = MonochromeBackground,
    onSurface = MonochromeGrey,
    outline = MonochromeDividerDark
)

/**
 * Roboto Regular -> FontWeight(400)
 * Roboto Medium -> FontWeight(500)
 * Roboto Bold -> FontWeight(700)
 */
private val themeTypography = Typography(
    bodySmall = TextStyle(
        fontSize = 12.sp,
        fontFamily = FontFamily(Font(R.font.roboto_regular)),
        color = FontDefault,
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        fontFamily = FontFamily(Font(R.font.roboto_regular)),
        color = FontDefault,
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        fontFamily = FontFamily(Font(R.font.roboto_regular)),
        color = FontDefault,
    ),
    headlineSmall = TextStyle(
        fontSize = 18.sp,
        fontFamily = FontFamily(Font(R.font.roboto_medium)),
        color = FontDefault,
    ),
    headlineMedium = TextStyle(
        fontSize = 22.sp,
        fontFamily = FontFamily(Font(R.font.roboto_medium)),
        color = FontDefault,
    ),
    headlineLarge = TextStyle(
        fontSize = 24.sp,
        fontFamily = FontFamily(Font(R.font.roboto_medium)),
        color = FontDefault,
    ),
    titleLarge = TextStyle(
        fontSize = 34.sp,
        fontFamily = FontFamily(Font(R.font.roboto_bold)),
        color = FontDefault,
    )
)

private val themeShapes = Shapes(
    extraSmall = RoundedCornerShape(2.dp),
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniVideoChatTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = themeColors,
        typography = themeTypography,
        shapes = themeShapes,
    ) {
        var windowInsets by remember { mutableStateOf(WindowInsets()) }
        var keyboardConfig by remember { mutableStateOf(KeyboardConfig()) }
        var cutoutInfo by remember { mutableStateOf(CutoutInfo()) }
        val density = LocalDensity.current
        val view = LocalView.current

        applyWindowInsets(view, density) { newInsets ->
            windowInsets = windowInsets.copy(insets = newInsets)
        }
        applyWindowInsetsIgnoringVisibility(view, density) { newInsets ->
            windowInsets = windowInsets.copy(insetsIgnoringVisibility = newInsets)
        }

        keyboardConfig = rememberKeyboardConfig(density)
        cutoutInfo = rememberCutoutInfo()

        val rippleConfiguration = RippleConfiguration(color = LocalContentColor.current, rippleAlpha = DefaultAlpha)
        CompositionLocalProvider(
            LocalRippleConfiguration provides rippleConfiguration,
            LocalInsets provides windowInsets,
            LocalKeyboard provides keyboardConfig,
            LocalCutout provides cutoutInfo
        ) {
            content()
//            DebugInfo()
        }
    }
}

@SuppressLint("ComposableNaming")
@Composable
private fun applyWindowInsets(view: View, density: Density, onInsetsChanged: (Insets) -> Unit) {
    if (isApi30orAbove) {
        DisposableEffect(view) {
            val listener = View.OnApplyWindowInsetsListener { _, windowInsets ->
                val systemInsets = windowInsets.getInsets(
                    WindowInsetsCompat.Type.displayCutout() or
                            WindowInsetsCompat.Type.statusBars() or
                            WindowInsetsCompat.Type.systemBars()
                )
                with(density) {
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
            view.setOnApplyWindowInsetsListener(listener)

            onDispose {
                view.setOnApplyWindowInsetsListener(null)
            }
        }
    }
}

@SuppressLint("ComposableNaming")
@Composable
private fun applyWindowInsetsIgnoringVisibility(view: View, density: Density, onInsetsChanged: (Insets) -> Unit) {
    if (isApi30orAbove) {
        DisposableEffect(view) {
            val listener = View.OnApplyWindowInsetsListener { _, windowInsets ->
                val systemInsets = windowInsets.getInsetsIgnoringVisibility(
                    WindowInsetsCompat.Type.displayCutout() or
                            WindowInsetsCompat.Type.statusBars() or
                            WindowInsetsCompat.Type.systemBars()
                )
                Log.d("виріз", "applyWindowInsetsIgnoringVisibility: ${windowInsets.displayCutout}")
                with(density) {
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
            view.setOnApplyWindowInsetsListener(listener)

            onDispose {
                view.setOnApplyWindowInsetsListener(null)
            }
        }
    }
}

@Composable
private fun rememberKeyboardConfig(density: Density): KeyboardConfig {
    val keyboardHeight by KeyboardHeightHolder.state.collectAsState()
    var previousKeyboardHeight by remember { mutableIntStateOf(0) }
    var keyboardConfig by remember { mutableStateOf(KeyboardConfig()) }

    if (keyboardHeight != previousKeyboardHeight) {
        previousKeyboardHeight = keyboardHeight
    }

    keyboardConfig = with(density) {
        keyboardConfig.copy(height = keyboardHeight.toDp())
    }

    return keyboardConfig
}

@Composable
fun rememberCutoutInfo(): CutoutInfo {
    val view = LocalView.current
    var cutoutInfo by remember {
        val boundingRects = getBoundingRects(view)
        mutableStateOf(CutoutInfo(boundingRects = boundingRects))
    }
    val configuration = LocalConfiguration.current
    LaunchedEffect(configuration.orientation, configuration.screenWidthDp, configuration.screenHeightDp) {
        val boundingRects = getBoundingRects(view)
        cutoutInfo = cutoutInfo.copy(boundingRects = boundingRects)
    }

    return cutoutInfo
}

private fun getBoundingRects(view: View): List<Rect> =
    if (isApi28orAbove) {
        Log.d("виріз", "getCutoutInfo: displayCutout = ${view.rootWindowInsets?.displayCutout}")
        view.rootWindowInsets?.displayCutout?.boundingRects ?: emptyList()
    } else {
        emptyList()
    }
