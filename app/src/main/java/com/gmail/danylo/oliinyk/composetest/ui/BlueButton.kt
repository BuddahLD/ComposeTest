package com.gmail.danylo.oliinyk.composetest.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.gmail.danylo.oliinyk.composetest.common.Colors.DarkBlue
import com.gmail.danylo.oliinyk.composetest.common.Colors.MidBlue
import com.gmail.danylo.oliinyk.composetest.common.Colors.PaleBlue
import com.gmail.danylo.oliinyk.composetest.common.Colors.PaleBlueLight
import com.gmail.danylo.oliinyk.composetest.common.Colors.TransparentBlack85
import com.gmail.danylo.oliinyk.composetest.common.Colors.TransparentBlue
import com.gmail.danylo.oliinyk.composetest.common.Fonts.INTER
import com.gmail.danylo.oliinyk.composetest.util.animateAlphaOnPressed
import com.gmail.danylo.oliinyk.composetest.util.animateDpOnPressed
import com.gmail.danylo.oliinyk.composetest.util.blurRenderScript
import com.gmail.danylo.oliinyk.composetest.util.createBrush
import com.gmail.danylo.oliinyk.composetest.util.innerShadow
import com.gmail.danylo.oliinyk.composetest.util.shadowCustom

private val BUTTON_BORDER_RADIUS_DP = 16.dp

@Preview(
    showSystemUi = true,
    backgroundColor = 0xD9435B83,
    showBackground = true
)
@Composable
fun BlueButton() {
    Box(modifier = Modifier.fillMaxSize()) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()
        val context = LocalContext.current
        val blurredImage: MutableState<Bitmap?> = remember {
            mutableStateOf(null)
        }
        LaunchedEffect(Unit) {
            val options = BitmapFactory.Options()
            options.inSampleSize = 24
            val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.girl_for_blur, options)
            blurredImage.value = blurRenderScript(context, bitmap, 25)
        }
        with(interactionSource.collectIsPressedAsState()) {
            val buttonBackgroundColor by animateColorAsState(
                targetValue = if (isPressed) MidBlue else PaleBlueLight,
                animationSpec = tween(durationMillis = 150),
                label = "buttonBackgroundColorAnimation"
            )
            val buttonBorderColor by animateColorAsState(
                targetValue = if (isPressed) DarkBlue else PaleBlueLight,
                animationSpec = tween(durationMillis = 150),
                label = "buttonBorderColorAnimation"
            )
            val buttonShadowColor by animateColorAsState(
                targetValue = if (isPressed) Color.Transparent else TransparentBlack85,
                animationSpec = tween(durationMillis = 150),
                label = "buttonShadowColorAnimation"
            )
            val buttonOffsetY by animateDpOnPressed(
                pressed = 3.dp,
                released = 0.dp,
                label = "buttonOffsetYAnimation"
            )
            blurredImage.value?.let {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Girl for blur"
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = DarkBlue.copy(alpha = .85f))
            )
            val alpha by animateFloatAsState(
                targetValue = if (isPressed) 0f else 1f,
                animationSpec = tween(durationMillis = 150),
                label = "alphaAnimation"
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = PaleBlue.copy(alpha = alpha))
            )
            val alphasBackground = buildList {
                add(animateAlphaOnPressed(
                    pressed = 0f,
                    released = .33f,
                    pressedDuration = 150,
                    releasedDuration = 150,
                    label = "AlphasBackground1Animation").value)
                add(animateAlphaOnPressed(
                    pressed = 0f,
                    released = .13f,
                    pressedDuration = 150,
                    releasedDuration = 150,
                    label = "AlphasBackground2Animation"
                ).value)
            }
            val backgroundBrush = createBrush(color = TransparentBlue, *alphasBackground.toFloatArray())
            val alphasBorder = buildList {
                add(animateAlphaOnPressed(
                    pressed = 0f,
                    released = .48f,
                    pressedDuration = 150,
                    releasedDuration = 150,
                    label = "AlphasBorder1Animation"
                ).value)
                add(animateAlphaOnPressed(
                    pressed = 0f,
                    released = .18f,
                    pressedDuration = 150,
                    releasedDuration = 150,
                    label = "AlphasBorder2Animation"
                ).value)
            }
            val borderBrush = createBrush(color = TransparentBlue, *alphasBorder.toFloatArray())
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(52.dp)
                    .shadowCustom(
                        color = buttonShadowColor,
                        offsetY = 3.dp,
                        shapeRadius = BUTTON_BORDER_RADIUS_DP,
                        blurRadius = 4.dp,
                    )
                    .offset(y = buttonOffsetY)
                    .then(
                        if (isPressed) {
                            Modifier.innerShadow(
                                shape = RoundedCornerShape(BUTTON_BORDER_RADIUS_DP),
                                color = TransparentBlack85,
                                blur = 1.dp,
                                offsetY = 2.dp,
                                offsetX = 0.dp
                            )
                        } else {
                            Modifier
                        }
                    )
                    .clip(RoundedCornerShape(BUTTON_BORDER_RADIUS_DP))
                    .background(color = buttonBorderColor)
                    .background(brush = borderBrush)
                    .padding(1.dp)
                    .clip(RoundedCornerShape(BUTTON_BORDER_RADIUS_DP - 1.dp))
                    .background(color = buttonBackgroundColor)
                    .background(brush = backgroundBrush)
                    .align(Alignment.Center)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = { /* Some important action */ }
                    )
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
}
