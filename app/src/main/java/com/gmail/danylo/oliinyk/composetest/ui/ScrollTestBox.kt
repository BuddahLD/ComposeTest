package com.gmail.danylo.oliinyk.composetest.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@Composable
fun ScrollTestBox() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Counter for tracking event sequence
            var eventCounter by remember { mutableIntStateOf(0) }

            // Scroll states
            var scrollPreAvailable by remember { mutableStateOf("Pre-Available: 0.0000, 0.0000") }
            var scrollPreSource by remember { mutableStateOf("Pre-Source: N/A") }
            var scrollAvailable by remember { mutableStateOf("Post-Available: 0.0000, 0.0000") }
            var scrollConsumed by remember { mutableStateOf("Post-Consumed: 0.0000, 0.0000") }
            var scrollSource by remember { mutableStateOf("Post-Source: N/A") }

            Text(text = "Scroll Logs:", fontSize = 14.sp, color = Color.Blue)
            Text(text = scrollPreAvailable, fontSize = 12.sp)
            Text(text = scrollPreSource, fontSize = 12.sp)
            Text(text = scrollAvailable, fontSize = 12.sp)
            Text(text = scrollConsumed, fontSize = 12.sp)
            Text(text = scrollSource, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(16.dp))

            // Single scrollable box
            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
            ) {
                SingleScrollBox(
                    onPreScroll = { available, source ->
                        eventCounter++
                        scrollPreAvailable = "[$eventCounter] Pre-Available: ${String.format(Locale.US, "%.4f", available.x)}, ${String.format(Locale.US, "%.4f", available.y)}"
                        scrollPreSource = "[$eventCounter] Pre-Source: $source"
                    },
                    onPostScroll = { available, consumed, source ->
                        eventCounter++
                        scrollAvailable = "[$eventCounter] Post-Available: ${String.format(Locale.US, "%.4f", available.x)}, ${String.format(Locale.US, "%.4f", available.y)}"
                        scrollConsumed = "[$eventCounter] Post-Consumed: ${String.format(Locale.US, "%.4f", consumed.x)}, ${String.format(Locale.US, "%.4f", consumed.y)}"
                        scrollSource = "[$eventCounter] Post-Source: $source"
                    }
                )
            }
        }
    }
}

@Composable
fun SingleScrollBox(
    onPreScroll: (Offset, NestedScrollSource) -> Unit,
    onPostScroll: (Offset, Offset, NestedScrollSource) -> Unit
) {
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                onPreScroll(available, source)
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                onPostScroll(available, consumed, source)
                return Offset.Zero
            }
        }
    }
    
    val scrollState = rememberScrollableState { delta -> delta / 3 }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
            .scrollable(
                orientation = Orientation.Vertical,
                state = scrollState
            )
            .padding(8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Blue.copy(alpha = 0.3f))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Scrollable Box")
            }
        }
    }
}


