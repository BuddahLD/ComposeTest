package com.gmail.danylo.oliinyk.composetest.ui.easteregg

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import timber.log.Timber

@Composable
fun EasterEggScreen() {
    val registry = rememberGravityRegistry()
    var gravityOn by remember { mutableStateOf(false) }

    LaunchedEffect(gravityOn) {
        Timber.tag("EasterEgg").d("gravityOn changed to $gravityOn")
    }

    Box(Modifier.fillMaxSize()) {
        // Grid - shown initially, becomes invisible when gravity is on
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .alpha(if (gravityOn) 0f else 1f)  // Hide grid when gravity is on
        ) {
            // First row
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ColoredBox(
                    label = "Home",
                    color = Color(0xFF2196F3),
                    registry = registry,
                    id = "box_home",
                    modifier = Modifier.weight(1f)
                )
                ColoredBox(
                    label = "Search",
                    color = Color(0xFFE91E63),
                    registry = registry,
                    id = "box_search",
                    modifier = Modifier.weight(1f)
                )
                ColoredBox(
                    label = "Cart",
                    color = Color(0xFFFF9800),
                    registry = registry,
                    id = "box_cart",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(12.dp))

            // Second row
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ColoredBox(
                    label = "Profile",
                    color = Color(0xFF4CAF50),
                    registry = registry,
                    id = "box_profile",
                    modifier = Modifier.weight(1f)
                )
                ColoredBox(
                    label = "Settings",
                    color = Color(0xFF9C27B0),
                    registry = registry,
                    id = "box_settings",
                    modifier = Modifier.weight(1f)
                )
                ColoredBox(
                    label = "Help",
                    color = Color(0xFF795548),
                    registry = registry,
                    id = "box_help",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(16.dp))

            // Third row
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ColoredBox(
                    label = "News",
                    color = Color(0xFFF44336),
                    registry = registry,
                    id = "box_news",
                    modifier = Modifier.weight(1f)
                )
                ColoredBox(
                    label = "Mail",
                    color = Color(0xFF00BCD4),
                    registry = registry,
                    id = "box_mail",
                    modifier = Modifier.weight(1f)
                )
                ColoredBox(
                    label = "Backup",
                    color = Color(0xFFFFC107),
                    registry = registry,
                    id = "box_backup",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(24.dp))

            Button(onClick = {
                Timber.tag("EasterEgg")
                    .d("Button tapped, activating gravity mode, registry items=${registry.items.size}")
                gravityOn = true
            }) {
                Text("Enable gravity")
            }

        }
        
        // Gravity overlay - shows physics when enabled
        GravityOverlay(
            visible = gravityOn,
            registry = registry,
            modifier = Modifier.fillMaxSize()
        )
        
        // Disable gravity button - shown when gravity is active
        if (gravityOn) {
            Button(
                onClick = {
                    Timber.tag("EasterEgg").d("Returning to normal mode")
                    gravityOn = false
                },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
            ) {
                Text("Disable gravity")
            }
        }
    }
}

@Composable
private fun ColoredBox(
    label: String,
    color: Color,
    registry: GravityRegistry,
    id: String,
    modifier: Modifier = Modifier
) {
    // Use a fixed size circle instead of flexible box
    val size = 120.dp

    Box(
        modifier = modifier
            .size(size)
            .gravityProbe(registry, GravityProbe(id = id, label = label, color = color)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = color.copy(alpha = 0.9f),
                radius = size.toPx() / 2f
            )
        }

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White
            )
        )
    }
}

