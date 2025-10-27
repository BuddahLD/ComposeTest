package com.gmail.danylo.oliinyk.composetest.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import timber.log.Timber

@Composable
fun EasterEggScreen() {
    val registry = rememberGravityRegistry()
    var gravityOn by remember { mutableStateOf(false) }
    var taps by remember { mutableStateOf(0) }
    
    LaunchedEffect(gravityOn) {
        Timber.d("EasterEgg: gravityOn changed to $gravityOn")
    }

    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
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
                taps++
                Timber.d("EasterEgg: Button tapped, current taps=$taps")
                if (taps >= 7) {
                    Timber.d("EasterEgg: Activating gravity mode, registry items=${registry.items.size}")
                    gravityOn = true
                    taps = 0
                }
            }) {
                Text("Tap me (${if (taps == 0) "" else "${7 - taps} more"})")
            }

            if (gravityOn) {
                Timber.d("EasterEgg: Gravity mode is ON")
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = { 
                    Timber.d("EasterEgg: Returning to normal mode")
                    gravityOn = false 
                }) {
                    Text("Return to normal")
                }
            }
        }

        GravityOverlay(
            visible = gravityOn,
            registry = registry,
            modifier = Modifier.matchParentSize()
        )
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
    Surface(
        modifier = modifier
            .gravityProbe(registry, GravityProbe(id = id, label = label, color = color))
            .defaultMinSize(minWidth = 100.dp, minHeight = 120.dp),
        color = color.copy(alpha = 0.15f),
        shape = MaterialTheme.shapes.small
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = color
            )
        }
    }
}

