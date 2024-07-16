package com.gmail.danylo.oliinyk.composetest.ui

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmail.danylo.oliinyk.composetest.R
import com.gmail.danylo.oliinyk.composetest.common.Colors.DarkerGreen
import com.gmail.danylo.oliinyk.composetest.common.Colors.LighterGreen
import com.gmail.danylo.oliinyk.composetest.common.Colors.TransparentBlack35
import com.gmail.danylo.oliinyk.composetest.common.Fonts.PT_SANS
import com.gmail.danylo.oliinyk.composetest.util.animateAlphaOnPressed
import com.gmail.danylo.oliinyk.composetest.util.animateDpOnPressed
import com.gmail.danylo.oliinyk.composetest.util.createWhiteBrush
import com.gmail.danylo.oliinyk.composetest.util.shadowCustom

private val BUTTON_HEIGHT_DP = 234.dp
private val BUTTON_BOTTOM_PADDING_DP = 16.dp
private val BUTTON_OUTER_BORDER_RADIUS_DP = 19.dp
private val BUTTON_INNER_BORDER_RADIUS_DP = 18.dp

@Preview(showSystemUi = true)
@Composable
fun GreenButton() {
    Box(modifier = Modifier.fillMaxSize()) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()

        with(interactionSource.collectIsPressedAsState()) {
            val alphas = buildList {
                add(animateAlphaOnPressed(pressed = .07f, released = .18f, label = "Alpha1Animation").value)
                add(0f)
                add(animateAlphaOnPressed(pressed = .15f, released = .35f, label = "Alpha3Animation").value)
            }
            val whiteOverlayBrush = createWhiteBrush(*alphas.toFloatArray())
            val innerBorderWhiteOverlayBrush = createWhiteBrush(.34f, 1f, .7f)

            val buttonHeight by animateDpOnPressed(
                pressed = BUTTON_HEIGHT_DP - 15.dp,
                released = BUTTON_HEIGHT_DP,
                label = "buttonHeightAnimation"
            )
            val bottomPadding by animateDpOnPressed(
                pressed = BUTTON_BOTTOM_PADDING_DP - 15.dp,
                released = BUTTON_BOTTOM_PADDING_DP,
                label = "buttonPaddingAnimation"
            )
            val offsetY by animateDpOnPressed(
                pressed = 7.5.dp,
                released = 0.dp,
                label = "offsetYAnimation"
            )
            val shadowColor by animateColorAsState(
                targetValue = if (isPressed) Color.Transparent else TransparentBlack35,
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
                        shapeRadius = BUTTON_OUTER_BORDER_RADIUS_DP,
                        blurRadius = 3.dp,
                    )
                    .offset(y = offsetY)
                    .clip(RoundedCornerShape(BUTTON_OUTER_BORDER_RADIUS_DP))
                    .background(color = LighterGreen)
                    .padding(start = 1.dp, top = 1.dp, end = 1.dp, bottom = bottomPadding)
                    .clip(RoundedCornerShape(BUTTON_OUTER_BORDER_RADIUS_DP))
                    .background(brush = innerBorderWhiteOverlayBrush)
                    .padding(top = 1.dp, bottom = 1.dp)
                    .clip(RoundedCornerShape(BUTTON_INNER_BORDER_RADIUS_DP))
                    .background(color = LighterGreen)
                    .background(brush = whiteOverlayBrush)
                    .align(Alignment.Center)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = { /* Some important action */ }
                    )
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(id = R.string.start),
                    fontSize = 38.sp,
                    fontFamily = PT_SANS,
                    color = Color.White,
                    fontWeight = FontWeight(400),
                    style = TextStyle.Default.copy(
                        shadow = Shadow(
                            color = DarkerGreen,
                            offset = Offset(0f, 1f),
                            blurRadius = 2f
                        ),
                    )
                )
            }
        }
    }
}