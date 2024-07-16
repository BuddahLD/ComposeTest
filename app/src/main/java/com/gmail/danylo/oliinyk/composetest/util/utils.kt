package com.gmail.danylo.oliinyk.composetest.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BlurMaskFilter
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun createBrush(color: Color, vararg alphas: Float): Brush {
    require(alphas.size >= 2) { "Should be at least two alphas" }

    val whiteOverlay = alphas.map {
        color.copy(alpha = it)
    }
    return Brush.verticalGradient(whiteOverlay)
}

fun createWhiteBrush(vararg alphas: Float): Brush = createBrush(Color(0xFFFFFFFF), *alphas)

@Composable
fun State<Boolean>.animateAlpha(from: Float, to: Float, label: String): State<Float> {
    return animateFloatAsState(
        targetValue = if (value) from else to,
        animationSpec = tween(durationMillis = if (value) 75 else 150),
        label = label
    )
}

@Composable
fun State<Boolean>.animateDp(from: Dp, to: Dp, label: String): State<Dp> {
    return animateDpAsState(
        targetValue = if (value) from else to,
        animationSpec = tween(durationMillis = if (value) 75 else 150),
        label = label
    )
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

fun blurredBitmapFromDrawable(context: Context, @DrawableRes resId: Int): Bitmap {
    val options = BitmapFactory.Options()
    options.inSampleSize = 14
    val bitmap = BitmapFactory.decodeResource(context.resources, resId, options)

    return blurBitmap(context, bitmap)
}

fun blurBitmap(context: Context, bitmap: Bitmap): Bitmap {
    val rs = RenderScript.create(context)
    val bitmapAlloc = Allocation.createFromBitmap(rs, bitmap)
    ScriptIntrinsicBlur.create(rs, bitmapAlloc.element).apply {
        setRadius(25f)
        setInput(bitmapAlloc)
        forEach(bitmapAlloc)
    }
    bitmapAlloc.copyTo(bitmap)
    rs.destroy()

    return bitmap
}
