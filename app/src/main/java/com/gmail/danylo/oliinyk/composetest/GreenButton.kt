package com.gmail.danylo.oliinyk.composetest

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmail.danylo.oliinyk.composetest.Colors.darkerGreen
import com.gmail.danylo.oliinyk.composetest.Colors.lighterGreen
import com.gmail.danylo.oliinyk.composetest.Fonts.PT_SANS
import kotlin.math.ceil

private val BUTTON_HEIGHT_DP = 234.dp
private val BUTTON_BOTTOM_PADDING_DP = 16.dp

@Preview(showSystemUi = true)
@Composable
fun GreenButton() {
    Box(modifier = Modifier.fillMaxSize()) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()
        val alpha1 by animateFloatAsState(
            targetValue = if (isPressed) 0.07f else 0.18f,
            animationSpec = tween(durationMillis = if (isPressed) 75 else 150),
            label = "Color1Animation"
        )
        val alpha2 by animateFloatAsState(
            targetValue = if (isPressed) 0f else 0f,
            animationSpec = tween(durationMillis = if (isPressed) 75 else 150),
            label = "Color1Animation"
        )
        val alpha3 by animateFloatAsState(
            targetValue = if (isPressed) 0.15f else 0.35f,
            animationSpec = tween(durationMillis = if (isPressed) 75 else 150),
            label = "Color1Animation"
        )
        val notPressedWhiteOverlayBrush = createBrush(0.21f, 0f, 0.43f)
        val pressedWhiteOverlayBrush = createBrush(0.07f, 0f, 0.15f)
        val whiteOverlayBrush = createBrush(alpha1, alpha2, alpha3)
        val innerBorderWhiteOverlayBrush = createBrush(0.34f, 1f, 0.7f)

        val buttonHeight by animateDpAsState(
            targetValue = if (isPressed) BUTTON_HEIGHT_DP - 15.dp else BUTTON_HEIGHT_DP,
            animationSpec = tween(durationMillis = if (isPressed) 75 else 150),
            label = "buttonHeightAnimation"
        )
        val bottomPadding by animateDpAsState(
            targetValue = if (isPressed) BUTTON_BOTTOM_PADDING_DP - 15.dp else BUTTON_BOTTOM_PADDING_DP,
            animationSpec = tween(durationMillis = if (isPressed) 75 else 150),
            label = "buttonPaddingAnimation"
        )
        val offsetY by animateDpAsState(
            targetValue = if (isPressed) 7.5.dp else 0.dp,
            animationSpec = tween(durationMillis = if (isPressed) 75 else 150),
            label = "offsetYAnimation"
        )
//        Shadowed(
//            modifier = Modifier.align(Alignment.Center),
//            color = Color(0x39000029),
//            offsetX = 0.dp,
//            offsetY = 3.dp,
//            blurRadius = 3.dp
//        ) {
            Box(
                modifier = Modifier
                    .width(222.dp)
                    .height(buttonHeight)
                    .offset(y = offsetY)
                    .clip(RoundedCornerShape(19.dp))
                    .background(color = lighterGreen)
                    .padding(start = 1.dp, top = 1.dp, end = 1.dp, bottom = bottomPadding)
                    .clip(RoundedCornerShape(19.dp))
                    .background(brush = innerBorderWhiteOverlayBrush)
                    .padding(top = 1.dp, bottom = 1.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(color = lighterGreen)
                    .background(brush = whiteOverlayBrush)
                    .align(Alignment.Center)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = { /* Important action */ }
                    )
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = stringResource(id = R.string.start),
                    fontSize = 38.sp,
                    fontFamily = PT_SANS,
                    color = Color.White,
                    fontWeight = FontWeight(400),
                    style = TextStyle.Default.copy(
                        shadow = Shadow(
                            color = darkerGreen,
                            offset = Offset(0f, 1f),
                            blurRadius = 2f
                        ),
                    )
                )
            }
//        }
    }
}

private fun Modifier.surface(
    shape: Shape,
    backgroundColor: Color,
    border: BorderStroke?,
    elevation: Dp
) = this
    .shadow(elevation, shape, clip = false)
    .background(color = backgroundColor, shape = shape)
    .clip(shape)

@Composable
fun Shadowed(
    modifier: Modifier = Modifier,
    color: Color,
    offsetX: Dp,
    offsetY: Dp,
    blurRadius: Dp,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val offsetXPx = with(density) { offsetX.toPx() }.toInt()
    val offsetYPx = with(density) { offsetY.toPx() }.toInt()
    val blurRadiusPx = ceil(with(density) {
        blurRadius.toPx()
    }).toInt()

    // Modifier to render the content in the shadow color, then
    // blur it by blurRadius
    val shadowModifier = Modifier
        .drawWithContent {
            val matrix = shadowColorMatrix(color)
            val filter = ColorFilter.colorMatrix(matrix)
            val paint = Paint().apply {
                colorFilter = filter
            }
            drawIntoCanvas { canvas ->
                canvas.saveLayer(Rect(0f, 0f, size.width, size.height), paint)
                drawContent()
                canvas.restore()
            }
        }
        .blur(radius = blurRadius, BlurredEdgeTreatment.Unbounded)
        .padding(all = blurRadius) // Pad to prevent clipping blur

    // Layout based solely on the content, placing shadow behind it
    Layout(modifier = modifier, content = {
        // measurables[0] = content, measurables[1] = shadow
        content()
        Box(modifier = shadowModifier) { content() }
    }) { measurables, constraints ->
        // Allow shadow to go beyond bounds without affecting layout
        val contentPlaceable = measurables[0].measure(constraints)
        val shadowPlaceable = measurables[1].measure(Constraints(maxWidth = contentPlaceable.width + blurRadiusPx * 2,
            maxHeight = contentPlaceable.height + blurRadiusPx * 2))
        layout(width = contentPlaceable.width, height = contentPlaceable.height) {
            shadowPlaceable.placeRelative(x = offsetXPx - blurRadiusPx, y = offsetYPx - blurRadiusPx)
            contentPlaceable.placeRelative(x = 0, y = 0)
        }
    }
}

// Return a color matrix with which to paint our content
// as a shadow of the given color
private fun shadowColorMatrix(color: Color): ColorMatrix {
    return ColorMatrix().apply {
        set(0, 0, 0f) // Do not preserve original R
        set(1, 1, 0f) // Do not preserve original G
        set(2, 2, 0f) // Do not preserve original B

        set(0, 4, color.red * 255) // Use given color's R
        set(1, 4, color.green * 255) // Use given color's G
        set(2, 4, color.blue * 255) // Use given color's B
        set(3, 3, color.alpha) // Multiply by given color's alpha
    }
}

//fun Modifier.moveOnPress(
//    byDp: Dp,
//    interactionSource: InteractionSource
//) = composed {
//    val isPressed by interactionSource.collectIsPressedAsState()
//    val animatedHeight = animateDpAsState(
//        targetValue = byDp,
//        animationSpec = tween(durationMillis = 75),
//        label = "moveOnPress"
//    )
//}
