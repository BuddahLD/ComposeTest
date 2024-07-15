package com.gmail.danylo.oliinyk.composetest

import android.graphics.BlurMaskFilter
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmail.danylo.oliinyk.composetest.Colors.darkerGreen
import com.gmail.danylo.oliinyk.composetest.Colors.lighterGreen
import com.gmail.danylo.oliinyk.composetest.Fonts.PT_SANS

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
        val shadowColor by animateColorAsState(
            targetValue = if (isPressed) Color.Transparent else Color(0x35000029),
            animationSpec = tween(durationMillis = if (isPressed) 75 else 150),
            label = "shadowColorAnimation"
        )
        Box(
            modifier = Modifier
                .width(222.dp)
                .height(buttonHeight)
                .shadowCustom(
                    color = shadowColor,
                    offsetY = 3.dp,
                    shapeRadius = 19.dp,
                    blurRadius = 3.dp,
                )
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
    }
}

fun Modifier.shadowCustom(
    color: Color = Color.Black,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
    shapeRadius: Dp = 0.dp,
) = composed {
    val paint: Paint = remember { Paint() }
    val blurRadiusPx = blurRadius.px(LocalDensity.current)
    val maskFilter = remember {
        BlurMaskFilter(blurRadiusPx, BlurMaskFilter.Blur.NORMAL)
    }
    drawBehind {
        drawIntoCanvas { canvas ->
            val frameworkPaint = paint.asFrameworkPaint()
            if (blurRadius != 0.dp) {
                frameworkPaint.maskFilter = maskFilter
            }
            frameworkPaint.color = color.toArgb()

            val leftPixel = offsetX.toPx()
            val topPixel = offsetY.toPx()
            val rightPixel = size.width + leftPixel
            val bottomPixel = size.height + topPixel

            if (shapeRadius > 0.dp) {
                val radiusPx = shapeRadius.toPx()
                canvas.drawRoundRect(
                    left = leftPixel,
                    top = topPixel,
                    right = rightPixel,
                    bottom = bottomPixel,
                    radiusX = radiusPx,
                    radiusY = radiusPx,
                    paint = paint,
                )
            } else {
                canvas.drawRect(
                    left = leftPixel,
                    top = topPixel,
                    right = rightPixel,
                    bottom = bottomPixel,
                    paint = paint,
                )
            }
        }
    }
}

private fun Dp.px(density: Density): Float =
    with(density) { toPx() }
