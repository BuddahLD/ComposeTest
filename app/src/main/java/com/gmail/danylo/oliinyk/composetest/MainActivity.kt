package com.gmail.danylo.oliinyk.composetest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Container {
                GreenButton()
//                ElevatingCard()
//                Spacer(modifier = Modifier.height(16.dp))
//                RingingBell()
//                Spacer(modifier = Modifier.height(16.dp))
//                ScalingCircle()
//                Spacer(modifier = Modifier.height(16.dp))
//                AnimatedColorsCircle()
            }
        }
    }

    @Composable
    private fun Container(content: @Composable ColumnScope.() -> Unit) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                content()
            }
        }
    }

    @Composable
    private fun ElevatingCard() {
        val value by rememberInfiniteTransition(
            label = "ElevatingCard"
        )
            .animateFloat(
                initialValue = 1.dp.value,
                targetValue = 16.dp.value,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 1000,
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "Hello1"
            )
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = value.dp,
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Цікавий текст"
            )
        }
    }

    @Composable
    private fun RingingBell() {
        val value by rememberInfiniteTransition(
            label = "RingingBell"
        ).animateFloat(
            initialValue = 25f,
            targetValue = -25f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 600,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "Hello2"
        )

        Icon(
            imageVector = Icons.Outlined.Notifications,
            contentDescription = "A ringing bell",
            modifier = Modifier
                .graphicsLayer(
                    transformOrigin = TransformOrigin(
                        pivotFractionX = 0.5f,
                        pivotFractionY = 0.0f,
                    ),
                    rotationZ = value
                )
        )
    }

    @Composable
    private fun ScalingCircle() {
        val value by rememberInfiniteTransition(
            label = "ScalingCircle"
        ).animateFloat(
            initialValue = 0.5f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 600,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "Hello3"
        )

        Box(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = value
                    scaleY = value
                }
                .size(25.dp)
                .clip(CircleShape)
                .background(Color.Red)
        )
    }

    @Composable
    private fun AnimatedColorsCircle() {
        val value by rememberInfiniteTransition(
            label = "AnimatedColorsCircle"
        ).animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1000,
                    easing = LinearEasing
                )
            ),
            label = "Hello4"
        )

        val colors = listOf(
            Color(0xFF405DE6),
            Color(0xFFC13584),
            Color(0xFFFD1D1D),
            Color(0xFFFFDC80)
        )
        val gradientBrush by remember {
            mutableStateOf(
                Brush.horizontalGradient(
                    colors = colors,
                    startX = -10.0f,
                    endX = 400.0f,
                    tileMode = TileMode.Repeated
                )
            )
        }

        Box(
            modifier = Modifier
                .drawBehind {
                    rotate(degrees = value) {
                        drawCircle(
                            brush = gradientBrush,
                            style = Stroke(width = 12.dp.value),
                        )
                    }
                }
                .size(125.dp)
        )
    }
}
