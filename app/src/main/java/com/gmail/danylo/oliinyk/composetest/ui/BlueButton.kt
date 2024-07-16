package com.gmail.danylo.oliinyk.composetest.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmail.danylo.oliinyk.composetest.R
import com.gmail.danylo.oliinyk.composetest.common.Colors.BlueForBlur
import com.gmail.danylo.oliinyk.composetest.common.Colors.PaleBlue
import com.gmail.danylo.oliinyk.composetest.common.Colors.PaleBlueLight
import com.gmail.danylo.oliinyk.composetest.common.Colors.TransparentBlack75
import com.gmail.danylo.oliinyk.composetest.common.Colors.TransparentBlue
import com.gmail.danylo.oliinyk.composetest.common.Fonts.INTER
import com.gmail.danylo.oliinyk.composetest.util.blurBitmap
import com.gmail.danylo.oliinyk.composetest.util.blurredBitmapFromDrawable
import com.gmail.danylo.oliinyk.composetest.util.createBrush
import com.gmail.danylo.oliinyk.composetest.util.createWhiteBrush
import com.gmail.danylo.oliinyk.composetest.util.shadowCustom
import com.skydoves.cloudy.Cloudy

private val BUTTON_BORDER_RADIUS_DP = 16.dp

@Preview(
    showSystemUi = true,
    backgroundColor = 0xD9435B83,
    showBackground = true
)
@Composable
fun BlueButton() {
    Box(modifier = Modifier.fillMaxSize()) {
//        Cloudy(
//            radius = 25
//        ) {
//            val blurredImage = blurredBitmapFromDrawable(
//                context = LocalContext.current,
//                resId = R.drawable.girl_for_blur
//            )
//            val blurredMore = blurBitmap(
//                context = LocalContext.current,
//                blurredImage
//            )
//            Image(
//                modifier = Modifier
//                    .fillMaxSize(),
//                contentScale = ContentScale.Crop,
//                bitmap = blurredMore.asImageBitmap(),
//                contentDescription = "Girl for blur"
//            )
//        }
//        Box(modifier = Modifier
//            .fillMaxSize()
//            .background(color = BlueForBlur.copy(alpha = .6f))
//        )

        val backgroundBrush = createBrush(color = TransparentBlue, .33f, .13f)
        val borderBrush = createBrush(color = TransparentBlue,.48f, .18f)
        Box(
            modifier = Modifier
                .width(150.dp)
                .height(52.dp)
                .shadowCustom(
                    color = TransparentBlack75,
                    offsetY = 3.dp,
                    shapeRadius = BUTTON_BORDER_RADIUS_DP,
                    blurRadius = 4.dp,
                )
                .clip(RoundedCornerShape(BUTTON_BORDER_RADIUS_DP))
                .background(color = PaleBlueLight)
                .background(brush = borderBrush)
                .padding(1.dp)
                .clip(RoundedCornerShape(BUTTON_BORDER_RADIUS_DP - 1.dp))
                .background(color = PaleBlueLight)
                .background(brush = backgroundBrush)
                .align(Alignment.Center)
                .clickable {
                    /* Some important action */
                },
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(id = R.string.cancel),
                fontSize = 18.sp,
                fontFamily = INTER,
                color = Color.White,
                fontWeight = FontWeight(600),
            )
        }
    }
}
