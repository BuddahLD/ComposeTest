package com.gmail.danylo.oliinyk.composetest.ui

import android.content.res.Resources
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlin.math.ceil
import kotlin.math.roundToInt
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ClearMeAfterUsage() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Main content
            var isSheetVisible by remember { mutableStateOf(true) }
            val scope = rememberCoroutineScope()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(48.dp)
                        .clip(shape = RoundedCornerShape(16.dp))
                        .background(color = Color(0xFF2E7D32))
                        .clickable {
                            scope.launch {
                                isSheetVisible = false
                                delay(50)
                                isSheetVisible = true
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Show Bottom Sheet", style = TextStyle(color = Color.White))
                }
            }
            // Simplified bottom sheet
            if (isSheetVisible) {
                SimpleBottomSheet()
            }
        }
    }
}

val Int.toPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

@Composable
fun SimpleBottomSheet() {
    val density = LocalDensity.current
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val expandedHeight = ceil(screenHeight.toPx * 0.95f)
    val expandedThreshold = screenHeight.toPx - expandedHeight
    var offsetY by remember { mutableFloatStateOf(500f) }
    val handleHeight = with(density) { 48.dp.toPx() }
    val minHeight = screenHeight.toPx - handleHeight - 100f
    val constrainedOffsetY = offsetY.coerceAtLeast(0f)
    var isLocked by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val isListAtTop by remember {
        derivedStateOf { listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0 }
    }

    val isExpanded by remember(offsetY, screenHeight) {
        derivedStateOf { offsetY <= expandedThreshold }
    }

    println("offsetY = $offsetY, expandedThreshold = ${expandedThreshold}, expandedHeight = ${expandedHeight}, minHeight = $minHeight, screen height ${screenHeight.toPx}")

    val nestedScrollConnection = remember(isExpanded) {
        object : NestedScrollConnection {

            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                return if (isLocked) {
                    println("sheet_scroll onPreScroll locked")
                    super.onPreScroll(available, source)
                } else {
                    println("sheet_scroll onPreScroll UNlocked")
                    if (isListAtTop) {
                        if (isExpanded) {
                            println("sheet_scroll onPreScroll top expanded")
                            if (available.y > 0) {
//                                offsetY = (offsetY + available.y)
                                offsetY = (offsetY + available.y).coerceIn(minimumValue = expandedThreshold, maximumValue = minHeight)
                                Offset(x = 0f, available.y)
                            } else {
                                Offset.Zero
                            }
                        } else {
                            println("sheet_scroll onPreScroll top NOT expanded")
//                            offsetY = (offsetY + available.y)
                            offsetY = (offsetY + available.y).coerceIn(minimumValue = expandedThreshold, maximumValue = minHeight)
                            Offset(x = 0f, available.y)
                        }
                    } else {
                        println("sheet_scroll onPreScroll NOT top")
                        Offset.Zero
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset { IntOffset(0, constrainedOffsetY.roundToInt()) }
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .pointerInput(Unit) {
                    detectDragGestures { _, dragAmount ->
                        if (!(isExpanded && dragAmount.y < 0)) {
                            offsetY = (offsetY + dragAmount.y).coerceIn(minimumValue = expandedThreshold, maximumValue = minHeight)
                        }
                    }
                }
        ) {
            Column(
                modifier = Modifier
                    .nestedScroll(nestedScrollConnection)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(Color.Gray.copy(alpha = .5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Drag Me", style = MaterialTheme.typography.labelMedium)
                }
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(bottom = 32.dp),
                    //                    userScrollEnabled = scrollEnabled
                ) {
                    items(20) { index ->
                        if (index == 3) {
                            VerticalPicker(onScrollLocked = { isLocked = it })
                        } else {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text("Item $index")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VerticalPicker(onScrollLocked: (Boolean) -> Unit) {
    val nestedScrollConnectionInner = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                onScrollLocked(true)
                return available
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                onScrollLocked(false)
                return super.onPostFling(consumed, available)
            }
        }
    }
    val listStateInner = rememberLazyListState()
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .nestedScroll(nestedScrollConnectionInner)
            .height(200.dp),
        state = listStateInner,
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        items(20) { index ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = .3f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Language $index")
                }
            }
        }
    }
}
