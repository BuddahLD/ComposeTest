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
fun NestedScrollTestBoxes() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Labels go here
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Counter for tracking event sequence
            var eventCounter by remember { mutableIntStateOf(0) }

            // Post-scroll states
            var outerAvailable by remember { mutableStateOf("Outer Post-Available: 0.0000, 0.0000") }
            var outerConsumed by remember { mutableStateOf("Outer Post-Consumed: 0.0000, 0.0000") }
            var outerSource by remember { mutableStateOf("Outer Post-Source: N/A") }

            var middleAvailable by remember { mutableStateOf("Middle Post-Available: 0.0000, 0.0000") }
            var middleConsumed by remember { mutableStateOf("Middle Post-Consumed: 0.0000, 0.0000") }
            var middleSource by remember { mutableStateOf("Middle Post-Source: N/A") }

            var innerAvailable by remember { mutableStateOf("Inner Post-Available: 0.0000, 0.0000") }
            var innerConsumed by remember { mutableStateOf("Inner Post-Consumed: 0.0000, 0.0000") }
            var innerSource by remember { mutableStateOf("Inner Post-Source: N/A") }

            // Pre-scroll states
            var outerPreAvailable by remember { mutableStateOf("Outer Pre-Available: 0.0000, 0.0000") }
            var outerPreSource by remember { mutableStateOf("Outer Pre-Source: N/A") }

            var middlePreAvailable by remember { mutableStateOf("Middle Pre-Available: 0.0000, 0.0000") }
            var middlePreSource by remember { mutableStateOf("Middle Pre-Source: N/A") }

            var innerPreAvailable by remember { mutableStateOf("Inner Pre-Available: 0.0000, 0.0000") }
            var innerPreSource by remember { mutableStateOf("Inner Pre-Source: N/A") }

            Text(text = "Outer Scroll:", fontSize = 14.sp, color = Color.Red)
            Text(text = outerPreAvailable, fontSize = 12.sp)
            Text(text = outerPreSource, fontSize = 12.sp)
            Text(text = outerAvailable, fontSize = 12.sp)
            Text(text = outerConsumed, fontSize = 12.sp)
            Text(text = outerSource, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Middle Scroll:", fontSize = 14.sp, color = Color.Green)
            Text(text = middlePreAvailable, fontSize = 12.sp)
            Text(text = middlePreSource, fontSize = 12.sp)
            Text(text = middleAvailable, fontSize = 12.sp)
            Text(text = middleConsumed, fontSize = 12.sp)
            Text(text = middleSource, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Inner Scroll:", fontSize = 14.sp, color = Color.Blue)
            Text(text = innerPreAvailable, fontSize = 12.sp)
            Text(text = innerPreSource, fontSize = 12.sp)
            Text(text = innerAvailable, fontSize = 12.sp)
            Text(text = innerConsumed, fontSize = 12.sp)
            Text(text = innerSource, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(16.dp))

            // Nested Boxes instead of LazyColumns
            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
            ) {
                OuterBox(
                    onOuterPreScroll = { available, source ->
                        eventCounter++
                        outerPreAvailable = "[$eventCounter] Outer Pre-Available: ${String.format(Locale.US, "%.4f", available.x)}, ${String.format(Locale.US, "%.4f", available.y)}"
                        outerPreSource = "[$eventCounter] Outer Pre-Source: $source"
                    },
                    onOuterPostScroll = { available, consumed, source ->
                        eventCounter++
                        outerAvailable = "[$eventCounter] Outer Post-Available: ${String.format(Locale.US, "%.4f", available.x)}, ${String.format(Locale.US, "%.4f", available.y)}"
                        outerConsumed = "[$eventCounter] Outer Post-Consumed: ${String.format(Locale.US, "%.4f", consumed.x)}, ${String.format(Locale.US, "%.4f", consumed.y)}"
                        outerSource = "[$eventCounter] Outer Post-Source: $source"
                    },
                    onMiddlePreScroll = { available, source ->
                        eventCounter++
                        middlePreAvailable = "[$eventCounter] Middle Pre-Available: ${String.format(Locale.US, "%.4f", available.x)}, ${String.format(Locale.US, "%.4f", available.y)}"
                        middlePreSource = "[$eventCounter] Middle Pre-Source: $source"
                    },
                    onMiddlePostScroll = { available, consumed, source ->
                        eventCounter++
                        middleAvailable = "[$eventCounter] Middle Post-Available: ${String.format(Locale.US, "%.4f", available.x)}, ${String.format(Locale.US, "%.4f", available.y)}"
                        middleConsumed = "[$eventCounter] Middle Post-Consumed: ${String.format(Locale.US, "%.4f", consumed.x)}, ${String.format(Locale.US, "%.4f", consumed.y)}"
                        middleSource = "[$eventCounter] Middle Post-Source: $source"
                    },
                    onInnerPreScroll = { available, source ->
                        eventCounter++
                        innerPreAvailable = "[$eventCounter] Inner Pre-Available: ${String.format(Locale.US, "%.4f", available.x)}, ${String.format(Locale.US, "%.4f", available.y)}"
                        innerPreSource = "[$eventCounter] Inner Pre-Source: $source"
                    },
                    onInnerPostScroll = { available, consumed, source ->
                        eventCounter++
                        innerAvailable = "[$eventCounter] Inner Post-Available: ${String.format(Locale.US, "%.4f", available.x)}, ${String.format(Locale.US, "%.4f", available.y)}"
                        innerConsumed = "[$eventCounter] Inner Post-Consumed: ${String.format(Locale.US, "%.4f", consumed.x)}, ${String.format(Locale.US, "%.4f", consumed.y)}"
                        innerSource = "[$eventCounter] Inner Post-Source: $source"
                    }
                )
            }
        }
    }
}

@Composable
fun OuterBox(
    onOuterPreScroll: (Offset, NestedScrollSource) -> Unit,
    onOuterPostScroll: (Offset, Offset, NestedScrollSource) -> Unit,
    onMiddlePreScroll: (Offset, NestedScrollSource) -> Unit,
    onMiddlePostScroll: (Offset, Offset, NestedScrollSource) -> Unit,
    onInnerPreScroll: (Offset, NestedScrollSource) -> Unit,
    onInnerPostScroll: (Offset, Offset, NestedScrollSource) -> Unit
) {
    val outerNestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                onOuterPreScroll(available, source)
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                onOuterPostScroll(available, consumed, source)
                return Offset.Zero
            }
        }
    }
    val scrollState = rememberScrollableState { delta -> delta / 2 }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(outerNestedScrollConnection)
            .scrollable(
                orientation = Orientation.Vertical,
                state = scrollState
            )
            .padding(8.dp)
            .background(Color.Red.copy(alpha = 0.1f))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red.copy(alpha = 0.3f))
                    .padding(8.dp)
            ) {
                MiddleBox(
                    onMiddlePreScroll = onMiddlePreScroll,
                    onMiddlePostScroll = onMiddlePostScroll,
                    onInnerPreScroll = onInnerPreScroll,
                    onInnerPostScroll = onInnerPostScroll
                )
            }
        }
    }
}

@Composable
fun MiddleBox(
    onMiddlePreScroll: (Offset, NestedScrollSource) -> Unit,
    onMiddlePostScroll: (Offset, Offset, NestedScrollSource) -> Unit,
    onInnerPreScroll: (Offset, NestedScrollSource) -> Unit,
    onInnerPostScroll: (Offset, Offset, NestedScrollSource) -> Unit
) {
    val middleNestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                onMiddlePreScroll(available, source)
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                onMiddlePostScroll(available, consumed, source)
                return Offset.Zero
            }
        }
    }

    val scrollState = rememberScrollableState { delta -> delta / 2 }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(middleNestedScrollConnection)
            .scrollable(
                orientation = Orientation.Vertical,
                state = scrollState
            )
            .padding(4.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Green.copy(alpha = 0.3f))
                    .padding(4.dp)
            ) {
                InnerBox(
                    onInnerPreScroll = onInnerPreScroll,
                    onInnerPostScroll = onInnerPostScroll
                )
            }
        }
    }
}

@Composable
fun InnerBox(
    onInnerPreScroll: (Offset, NestedScrollSource) -> Unit,
    onInnerPostScroll: (Offset, Offset, NestedScrollSource) -> Unit
) {
    val innerNestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                onInnerPreScroll(available, source)
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                onInnerPostScroll(available, consumed, source)
                return Offset(x = 0f, available.y / 2)
            }
        }
    }

    val scrollState = rememberScrollableState { delta -> delta / 2 }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(innerNestedScrollConnection)
            .scrollable(
                orientation = Orientation.Vertical,
                state = scrollState
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(4.dp)
                .background(Color.Blue.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Inner Box")
        }
    }
}


