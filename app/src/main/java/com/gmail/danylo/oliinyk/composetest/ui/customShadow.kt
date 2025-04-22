package com.gmail.danylo.oliinyk.composetest.ui

import android.graphics.BlurMaskFilter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Adds a drop shadow effect to the composable.
 *
 * This modifier allows you to draw a shadow behind the composable with various customization options.
 *
 * @param shape The shape of the shadow.
 * @param color The color of the shadow.
 * @param blur The blur radius of the shadow
 * @param offsetY The shadow offset along the Y-axis.
 * @param offsetX The shadow offset along the X-axis.
 * @param horizontalPadding The amount to increase the size of the shadow horizontally.
 * @param verticalPadding The amount to increase the size of the shadow vertically.
 *
 * @return A new `Modifier` with the drop shadow effect applied.
 */
fun Modifier.dropShadow(
    shape: Shape,
    color: Color = Color.Black.copy(0.25f),
    blur: Dp = 4.dp,
    offsetY: Dp = 4.dp,
    offsetX: Dp = 0.dp,
    horizontalPadding: Dp = 0.dp,
    verticalPadding: Dp = 0.dp
) = this.drawBehind {

    // Calculate the shadow size with padding
    val shadowSize = Size(
        size.width - horizontalPadding.toPx() * 2,
        size.height - verticalPadding.toPx() * 2
    )
    val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)

    val paint = Paint()
    paint.color = color

    if (blur.toPx() > 0) {
        paint.asFrameworkPaint().apply {
            maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
        }
    }

    drawIntoCanvas { canvas ->
        canvas.save()
        // Translate the canvas to account for padding and offset
        canvas.translate(
            offsetX.toPx() + horizontalPadding.toPx(),
            offsetY.toPx() + verticalPadding.toPx()
        )
        canvas.drawOutline(shadowOutline, paint)
        canvas.restore()
    }
}
