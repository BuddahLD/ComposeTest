package com.gmail.danylo.oliinyk.composetest.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Base64;
import android.util.Size;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.util.Random;

/**
 * Created by Pavel on 11/10/2016.
 */
public class BitmapUtils {

    private static final int COLOR_MAX = 0xFF;




    public static String compressBitmapAndPackToBase64(@NonNull Bitmap bitmap, int size) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap = scaleBitmap(bitmap, size, size);
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    public static Bitmap compressBitmapToBitmap(@NonNull Bitmap bitmap, int size) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap = scaleBitmap(bitmap, size, size);
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
        return bitmap;
    }

    public static Bitmap prepareBitmapForFaceDetection(@NonNull Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap = scaleBitmap(bitmap, 480, 480);
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return bitmap;
    }

    public static byte[] compressBitmapToByteArray(@NonNull Bitmap bitmap, int size) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap = scaleBitmap(bitmap, size, size);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static Bitmap base64ToBitmap(@NonNull String encoded) {
        byte[] imageAsBytes = Base64.decode(encoded.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    public static String bitmapToBase64(@NonNull Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    public static String bitmapToBase64Raw(@NonNull Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    public static String bitmapToBase64Preview(@NonNull Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    public static Bitmap cropBitmap(@NonNull Bitmap image) {
        Bitmap dstBmp;
        if (image.getWidth() >= image.getHeight()) {
            dstBmp = Bitmap.createBitmap(
                    image,
                    image.getWidth() / 2 - image.getHeight() / 2,
                    0,
                    image.getHeight(),
                    image.getHeight()
            );
        } else {
            dstBmp = Bitmap.createBitmap(
                    image,
                    0,
                    image.getHeight() / 2 - image.getWidth() / 2,
                    image.getWidth(),
                    image.getWidth()
            );
        }
        return dstBmp;
    }

    public static Bitmap scaleBitmapWithResolution(@NonNull Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;
            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
        }
        Bitmap dstBmp;
        if (image.getWidth() >= image.getHeight()) {
            dstBmp = Bitmap.createBitmap(
                    image,
                    image.getWidth() / 2 - image.getHeight() / 2,
                    0,
                    image.getHeight(),
                    image.getHeight()
            );
        } else {
            dstBmp = Bitmap.createBitmap(
                    image,
                    0,
                    image.getHeight() / 2 - image.getWidth() / 2,
                    image.getWidth(),
                    image.getWidth()
            );
        }
        return dstBmp;
    }

    public static Bitmap scaleBitmap(@NonNull Bitmap b, int reqWidth, int reqHeight) {
        Bitmap background = Bitmap.createBitmap(reqWidth, reqHeight, Bitmap.Config.ARGB_8888);
        float originalWidth = b.getWidth(), originalHeight = b.getHeight();
        Canvas canvas = new Canvas(background);
        float scale = reqWidth / originalWidth;
        float xTranslation = 0.0f, yTranslation = (reqHeight - originalHeight * scale) / 2.0f;
        Matrix transformation = new Matrix();
        transformation.postTranslate(xTranslation, yTranslation);
        transformation.preScale(scale, scale);
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        canvas.drawBitmap(b, transformation, paint);
        return background;
    }

    public static Bitmap scaleBitmapWithResolution(@NonNull Bitmap image, int resolution) {
        if (resolution > 0) {
            float width = image.getWidth();
            float height = image.getHeight();
            float finalWidth = width;
            float finalHeight = height;
            if (width > (float) resolution || height > (float) resolution) {
                float ratio = width / height;
                if (ratio > 1) {
                    finalHeight = (float) resolution;
                    finalWidth = (finalHeight * ratio);
                } else {
                    finalWidth = (float) resolution;
                    finalHeight = (finalWidth / ratio);
                }
            }
            image = Bitmap.createScaledBitmap(image, (int) finalWidth, (int) finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    public static Bitmap blurBitmap(@NonNull Bitmap sentBitmap, float scale, float radius, Context context) {
        try {
            int width = Math.round(sentBitmap.getWidth() * scale);
            int height = Math.round(sentBitmap.getHeight() * scale);
            Bitmap inputBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false);
            Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
            RenderScript rs = RenderScript.create(context);
            ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
            Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
            theIntrinsic.setRadius((float) 0.5);
            theIntrinsic.setInput(tmpIn);
            theIntrinsic.forEach(tmpOut);
            tmpOut.copyTo(outputBitmap);
            return outputBitmap;
        } catch (Exception e) {
            return sentBitmap;
        }
    }

    public static Bitmap createNoiseBitmap(int elementWidth, Context context) {
        int[] pixels = new int[elementWidth * elementWidth];
        Random random = new Random();
        int index = 0;
        for (int y = 0; y < elementWidth; ++y) {
            for (int x = 0; x < elementWidth; ++x) {
                index = y * elementWidth + x;
                int randc = random.nextInt(COLOR_MAX);
                int randColor = Color.rgb(randc, randc, randc);
                pixels[index] |= randColor;
            }
        }

        RadialGradient gradient = new RadialGradient(
                elementWidth / 2,
                elementWidth / 2,
                elementWidth / 1.5f,
                0x80000000,
                0xCC000000,
                Shader.TileMode.CLAMP
        );
        Paint paint = new Paint();
        paint.setDither(true);
        paint.setColor(0xFF000000);
        paint.setShader(gradient);
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap outputBitmap = Bitmap.createBitmap(elementWidth, elementWidth, conf);
        outputBitmap.setPixels(pixels, 0, elementWidth, 0, 0, elementWidth, elementWidth);
        outputBitmap = BitmapUtils.blurBitmap(outputBitmap, 1, 1, context);
        Canvas canvas = new Canvas(outputBitmap);
        canvas.drawRect(new Rect(0, 0, elementWidth, elementWidth), paint);

        return outputBitmap;
    }

    public static Bitmap createFlippedBitmap(@NonNull Bitmap source, boolean xFlip, boolean yFlip) {
        Matrix matrix = new Matrix();
        matrix.postScale(xFlip ? -1 : 1, yFlip ? -1 : 1, source.getWidth() / 2f, source.getHeight() / 2f);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static String prepareBitmapForBlackTest(@NonNull Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap = scaleBitmap(bitmap, 160, 160);
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    public static Rect cropVideoFrameSizeForLandscapeFitSize(Size videoFrameSize, Size toFitSize) {
        int croppedWidth = Math.max(videoFrameSize.getWidth(), toFitSize.getWidth());
        int croppedHeight = croppedWidth;
        int topMargin = (Math.max(croppedHeight, toFitSize.getHeight()) - Math.min(
                croppedHeight,
                toFitSize.getHeight()
        )) / 2;
        int leftMargin = (Math.min(croppedWidth, toFitSize.getWidth()) - Math.max(croppedWidth, toFitSize.getWidth()))
                / 2;
        return new Rect(leftMargin, topMargin, croppedWidth, croppedHeight);
    }
}
