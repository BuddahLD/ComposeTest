package com.gmail.danylo.oliinyk.composetest.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.TypedValue

object BitmapDevelopUtil {
    private const val SIZE_DP = 100

    fun generateBitmap(difference: Int, context: Context): Bitmap {
        // Convert dp to pixels
        val sizePx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            SIZE_DP.toFloat(),
            context.resources.displayMetrics
        ).toInt()

        val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        
        // Base color - red
        val baseColor = Color.rgb(255, 0, 0)
        val newColor = Color.rgb(0, 0, 255)  // Pure blue for contrast
        
        // Calculate split point based on difference percentage
        val splitPoint = sizePx * (100 - difference) / 100f
        
        // Draw the base color (red) on the left portion
        canvas.save()
        canvas.clipRect(0f, 0f, splitPoint, sizePx.toFloat())
        canvas.drawColor(baseColor)
        canvas.restore()
        
        // Draw the new color (blue) on the right portion
        canvas.save()
        canvas.clipRect(splitPoint, 0f, sizePx.toFloat(), sizePx.toFloat())
        canvas.drawColor(newColor)
        canvas.restore()

        return bitmap
    }
}
