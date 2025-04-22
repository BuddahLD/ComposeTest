package com.gmail.danylo.oliinyk.composetest.ui.ready

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gmail.danylo.oliinyk.composetest.easymigration.Colors.BrandPrimary

@Composable
fun CirclesLoader(
    modifier: Modifier = Modifier,
    circleSpacing: Dp = 5.dp,
    circleSize: Dp = 14.dp,
    animationDuration: Int = 600,
    staticPeriod: Int = 200,
    color: Color = BrandPrimary
) {
    val totalDuration = animationDuration + staticPeriod

    val delayDurations = List(3) { index ->
        (animationDuration * (index + 1) * 0.12).toInt()
    }

    val infiniteTransition = rememberInfiniteTransition(label = "infinite_circles")
    val scales = delayDurations.map { delay ->
        infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 0.3f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = totalDuration
                    1f at 0 using LinearEasing
                    0.3f at (animationDuration * 0.3).toInt() using LinearEasing
                    1f at animationDuration using LinearEasing
                    1f at totalDuration using LinearEasing // Full size during the static period
                },
                initialStartOffset = StartOffset(offsetMillis = delay),
            ),
            label = "circles_loader"
        )
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        scales.forEachIndexed { index, scale ->
            Box(
                modifier = Modifier
                    .size(circleSize)
                    .scale(scale.value)
                    .background(color = color, shape = CircleShape)
            )
            if (index != scales.lastIndex) {
                Spacer(modifier = Modifier.width(circleSpacing))
            }
        }
    }
}

@Preview
@Composable
fun CircleAnimationPreview() {
    CirclesLoader()
}
