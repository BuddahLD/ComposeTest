package com.gmail.danylo.oliinyk.composetest.util

import android.graphics.Bitmap
import android.graphics.Color
import kotlin.math.abs

object ImageDiff {

    private const val PIXEL_DIFF_THRESHOLD = 24f

    private const val R_WEIGHT = 0.3f
    private const val G_WEIGHT = 0.6f
    private const val B_WEIGHT = 0.1f

    fun measure(bitmap1: Bitmap?, bitmap2: Bitmap?, width: Int, height: Int): Float {
        if (bitmap1 == null || bitmap2 == null) return 100f

        val pixels1 = IntArray(width * height)
        val pixels2 = IntArray(width * height)
        bitmap1.getPixels(pixels1, 0, width, 0, 0, width, height)
        bitmap2.getPixels(pixels2, 0, width, 0, 0, width, height)

        var diffScore = 0

        for (i in pixels1.indices) {
            val pixel1 = pixels1[i]
            val pixel2 = pixels2[i]

            val r1 = Color.red(pixel1)
            val g1 = Color.green(pixel1)
            val b1 = Color.blue(pixel1)

            val r2 = Color.red(pixel2)
            val g2 = Color.green(pixel2)
            val b2 = Color.blue(pixel2)

            val rDiff = abs(r1 - r2)
            val gDiff = abs(g1 - g2)
            val bDiff = abs(b1 - b2)

            val pixelDiff = rDiff * R_WEIGHT + gDiff * G_WEIGHT + bDiff * B_WEIGHT

            if (pixelDiff >= PIXEL_DIFF_THRESHOLD) {
                diffScore++
            }
        }

        val result = ((diffScore.toFloat() / pixels1.size) * 100000) / 1000f

        return result.coerceIn(0f, 100f)
    }

    fun computeDiff(base64Image1: String?, base64Image2: String?): Float {
        if (base64Image1 == null || base64Image2 == null) return 100f

        try {
            val bitmap1 = BitmapUtils.base64ToBitmap(base64Image1)
            val bitmap2 = BitmapUtils.base64ToBitmap(base64Image2)

            if (bitmap1 == null || bitmap2 == null) return 100f

            val width = bitmap1.width
            val height = bitmap1.height

            if (width != bitmap2.width || height != bitmap2.height) {
                return 100f
            }

            return measure(bitmap1, bitmap2, width, height)
        } catch (e: Exception) {
            return 100f
        }
    }
}
