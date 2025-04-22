//package com.gmail.danylo.oliinyk.composetest.ui
//
//import android.content.res.Configuration
//import androidx.compose.animation.core.Animatable
//import androidx.compose.animation.core.LinearOutSlowInEasing
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.gestures.detectDragGestures
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Card
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableIntStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.input.pointer.pointerInput
//import androidx.compose.ui.input.pointer.util.VelocityTracker
//import androidx.compose.ui.input.pointer.util.addPointerInputChange
//import androidx.compose.ui.platform.LocalConfiguration
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.gmail.danylo.oliinyk.composetest.easymigration.Colors
//import com.gmail.danylo.oliinyk.composetest.ui.BottomSheetState.CLOSED
//import com.gmail.danylo.oliinyk.composetest.ui.BottomSheetState.EXPANDED
//import com.gmail.danylo.oliinyk.composetest.ui.BottomSheetState.PARTIALLY_EXPANDED
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import kotlin.math.abs
//
//enum class BottomSheetState {
//    CLOSED,
//    PARTIALLY_EXPANDED,
//    EXPANDED
//}
//
//class SimpleBottomSheetState(
//    initialState: BottomSheetState = CLOSED
//) {
//    private var portraitState by mutableStateOf(initialState)
//
//    private var landscapeState by mutableStateOf(
//        if (initialState == PARTIALLY_EXPANDED) EXPANDED else initialState
//    )
//
//    val stateForOrientation: BottomSheetState
//        get() {
//            val isLandscape = previousOrientation == Configuration.ORIENTATION_LANDSCAPE
//            return if (isLandscape) landscapeState else portraitState
//        }
//
//    private var previousOrientation by mutableIntStateOf(Configuration.ORIENTATION_UNDEFINED)
//
//    private var forcePartialOnNextPortrait by mutableStateOf(false)
//
//    private var previousPortraitState by mutableStateOf(initialState)
//    private var previousLandscapeState by mutableStateOf(
//        if (initialState == PARTIALLY_EXPANDED)
//            EXPANDED else initialState
//    )
//
//    fun close() {
//        portraitState = CLOSED
//        landscapeState = CLOSED
//
//        previousPortraitState = CLOSED
//        previousLandscapeState = CLOSED
//        forcePartialOnNextPortrait = false
//    }
//
//    fun expand() {
//        val isLandscape = previousOrientation == Configuration.ORIENTATION_LANDSCAPE
//        portraitState = EXPANDED
//        landscapeState = EXPANDED
//        previousPortraitState = EXPANDED
//        previousLandscapeState = EXPANDED
//
//        if (isLandscape) {
//            forcePartialOnNextPortrait = true
//        }
//    }
//
//    fun partiallyExpand() {
//        portraitState = PARTIALLY_EXPANDED
//        landscapeState = EXPANDED  // Landscape only has CLOSED and EXPANDED
//        previousPortraitState = PARTIALLY_EXPANDED
//        previousLandscapeState = EXPANDED
//    }
//
//    internal fun updateState(newState: BottomSheetState, isLandscape: Boolean) {
//        if (isLandscape) {
//            landscapeState = newState
//            previousLandscapeState = newState
//
//            if (newState == EXPANDED) {
//                forcePartialOnNextPortrait = true
//            }
//
//            if (newState == CLOSED) {
//                portraitState = CLOSED
//                previousPortraitState = CLOSED
//                forcePartialOnNextPortrait = false
//            }
//        } else {
//            portraitState = newState
//            previousPortraitState = newState
//
//            if (newState == CLOSED) {
//                landscapeState = CLOSED
//                previousLandscapeState = CLOSED
//                forcePartialOnNextPortrait = false
//            }
//        }
//    }
//
//    internal fun handleOrientationChange(newOrientation: Int) {
//        if (previousOrientation == newOrientation) return
//
//        val isFirstOrientationSet = previousOrientation == Configuration.ORIENTATION_UNDEFINED
//        val oldOrientation = previousOrientation
//        previousOrientation = newOrientation
//
//        val isNowLandscape = newOrientation == Configuration.ORIENTATION_LANDSCAPE
//        val wasInLandscape = oldOrientation == Configuration.ORIENTATION_LANDSCAPE
//
//        if (isNowLandscape) {
//            previousPortraitState = portraitState
//
//            if (portraitState == CLOSED) {
//                landscapeState = CLOSED
//            } else {
//                landscapeState = EXPANDED
//
//                if (portraitState == EXPANDED) {
//                    forcePartialOnNextPortrait = false
//                }
//            }
//        } else {
//            previousLandscapeState = landscapeState
//
//            if (landscapeState == CLOSED) {
//                portraitState = CLOSED
//                forcePartialOnNextPortrait = false
//            } else {
//                if (forcePartialOnNextPortrait) {
//                    portraitState = PARTIALLY_EXPANDED
//                    forcePartialOnNextPortrait = false
//                } else if (wasInLandscape) {
//                    portraitState = previousPortraitState
//                } else if (isFirstOrientationSet) {
//                    portraitState = PARTIALLY_EXPANDED
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun rememberSimpleBottomSheetState(
//    initialState: BottomSheetState = CLOSED
//): SimpleBottomSheetState {
//    val bottomSheetState = remember { SimpleBottomSheetState(initialState) }
//    val configuration = LocalConfiguration.current
//    LaunchedEffect(configuration) {
//        bottomSheetState.handleOrientationChange(configuration.orientation)
//    }
//    return bottomSheetState
//}
//
///**
// * Use [EmptySheetDragHandler] if no handler is needed.
// */
//@Composable
//fun SimpleBottomSheet(
//    state: SimpleBottomSheetState,
//    modifier: Modifier = Modifier,
//    dragHandler: @Composable () -> Unit = { DefaultSheetDragHandler() },
//    scrimColor: Color = Color.Black.copy(alpha = 0.5f),
//    closeOnClickOutside: Boolean = true,
//    sheetContent: @Composable () -> Unit
//) {
//    val density = LocalDensity.current
//    val configuration = LocalConfiguration.current
//    val coroutineScope = rememberCoroutineScope()
//    val dragVelocityTracker = remember { VelocityTracker() }
//    val screenHeight = configuration.screenHeightDp.dp
//    val screenHeightInPixels = with(density) { screenHeight.toPx() }
//
//    val isLandscapeOrientation = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
//    val currentBottomSheetState = state.stateForOrientation
//
//    var isDraggingInProgress by remember { mutableStateOf(false) }
//
//    val heightWhenClosed = 0f
//    val heightWhenPartiallyExpanded = screenHeightInPixels * 0.5f
//    val heightWhenFullyExpanded = screenHeightInPixels * 0.9f
//
//    val targetHeightBasedOnState = when (currentBottomSheetState) {
//        CLOSED -> heightWhenClosed
//        PARTIALLY_EXPANDED -> heightWhenPartiallyExpanded
//        EXPANDED -> heightWhenFullyExpanded
//    }
//
//    // Internal orientation change tracking
//    var previousScreenOrientation by remember { mutableStateOf(configuration.orientation) }
//    val hasOrientationChanged = previousScreenOrientation != configuration.orientation
//
//    // Internal flag to suppress animations
//    var shouldSuppressAnimation by remember { mutableStateOf(false) }
//
//    // Handle orientation changes internally
//    LaunchedEffect(configuration.orientation) {
//        if (hasOrientationChanged) {
//            shouldSuppressAnimation = true
//            previousScreenOrientation = configuration.orientation
//            // Reset the suppression after a short delay
//            delay(100)
//            shouldSuppressAnimation = false
//        }
//    }
//
//    val animatedSheetHeight = remember { Animatable(targetHeightBasedOnState) }
//    val scrimAlpha = (animatedSheetHeight.value / heightWhenPartiallyExpanded).coerceIn(0f, 1f)
//
//    LaunchedEffect(currentBottomSheetState, shouldSuppressAnimation) {
//        if (shouldSuppressAnimation) {
//            val currentTargetHeight = calculateTargetHeightForState(
//                currentBottomSheetState,
//                heightWhenPartiallyExpanded,
//                heightWhenFullyExpanded
//            )
//            animatedSheetHeight.snapTo(currentTargetHeight)
//        } else if (!isDraggingInProgress) {
//            val distanceToAnimate = abs(animatedSheetHeight.value - targetHeightBasedOnState)
//            val animationDuration = calculateAnimationDuration(
//                distanceToAnimate,
//                maxHeight = heightWhenFullyExpanded
//            )
//            animatedSheetHeight.animateTo(
//                targetValue = targetHeightBasedOnState,
//                animationSpec = tween(
//                    durationMillis = animationDuration,
//                    easing = LinearOutSlowInEasing
//                )
//            )
//        }
//    }
//
//    val displayHeight = animatedSheetHeight.value
//
//    Box(
//        modifier = modifier.fillMaxSize()
//    ) {
//        // Background scrim - directly use the calculated scrim alpha
//        if (scrimAlpha > 0f) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(scrimColor.copy(alpha = scrimColor.alpha * scrimAlpha))
//                    .clickable(
//                        enabled = true,
//                        indication = null,
//                        interactionSource = remember { MutableInteractionSource() },
//                        onClick = {
//                            if (closeOnClickOutside && currentBottomSheetState != CLOSED) {
//                                state.close()
//                            }
//                        }
//                    )
//            )
//        }
//
//        Box(
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .fillMaxWidth()
//                .height(with(density) { displayHeight.toDp() })
//                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
//                .background(Color.White)
//                .clickable(indication = null, interactionSource = null, onClick = {})
//                .then(
//                    if (!isLandscapeOrientation) {
//                        Modifier.pointerInput(Unit) {
//                            detectDragGestures(
//                                onDragStart = {
//                                    isDraggingInProgress = true
//                                    dragVelocityTracker.resetTracking()
//                                },
//                                onDragEnd = {
//                                    val finalDragVelocity = dragVelocityTracker.calculateVelocity().y
//
//                                    determineStateAfterDrag(
//                                        currentHeight = animatedSheetHeight.value,
//                                        dragVelocity = finalDragVelocity,
//                                        isLandscape = isLandscapeOrientation,
//                                        heightWhenPartiallyExpanded = heightWhenPartiallyExpanded,
//                                        heightWhenFullyExpanded = heightWhenFullyExpanded,
//                                        animatedHeight = animatedSheetHeight,
//                                        scope = coroutineScope,
//                                        onStateChange = { newState ->
//                                            state.updateState(newState, isLandscapeOrientation)
//                                        },
//                                        onDragFinished = { isDraggingInProgress = false }
//                                    )
//                                },
//                                onDragCancel = {
//                                    determineStateAfterDrag(
//                                        currentHeight = animatedSheetHeight.value,
//                                        dragVelocity = 0f,
//                                        isLandscape = isLandscapeOrientation,
//                                        heightWhenPartiallyExpanded = heightWhenPartiallyExpanded,
//                                        heightWhenFullyExpanded = heightWhenFullyExpanded,
//                                        animatedHeight = animatedSheetHeight,
//                                        scope = coroutineScope,
//                                        onStateChange = { newState ->
//                                            state.updateState(newState, isLandscapeOrientation)
//                                        },
//                                        onDragFinished = { isDraggingInProgress = false }
//                                    )
//                                },
//                                onDrag = { change, dragAmount ->
//                                    dragVelocityTracker.addPointerInputChange(change)
//                                    change.consume()
//
//                                    coroutineScope.launch {
//                                        val newHeight = (animatedSheetHeight.value - dragAmount.y).coerceIn(
//                                            heightWhenClosed,
//                                            heightWhenFullyExpanded
//                                        )
//                                        animatedSheetHeight.snapTo(newHeight)
//                                    }
//                                }
//                            )
//                        }
//                    } else {
//                        Modifier
//                    }
//                )
//        ) {
//            Box(modifier = Modifier.align(Alignment.TopCenter)) { dragHandler() }
//
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(top = 20.dp)
//            ) {
//                sheetContent()
//            }
//        }
//    }
//}
//
//@Composable
//fun DefaultSheetDragHandler() {
//    Box(
//        modifier = Modifier
//            .padding(top = 8.dp)
//            .height(4.dp)
//            .width(40.dp)
//            .background(Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(2.dp))
//    )
//}
//
//@Composable
//fun EmptySheetDragHandler() = Unit
//
//private fun calculateTargetHeightForState(
//    state: BottomSheetState,
//    heightWhenPartiallyExpanded: Float,
//    heightWhenFullyExpanded: Float
//): Float {
//    return when (state) {
//        CLOSED -> 0f
//        PARTIALLY_EXPANDED -> heightWhenPartiallyExpanded
//        EXPANDED -> heightWhenFullyExpanded
//    }
//}
//
//private fun calculateAnimationDuration(distanceToAnimate: Float, maxHeight: Float): Int {
//    val baseDurationInMillis = 300
//    val calculatedDuration = (baseDurationInMillis * (distanceToAnimate / (maxHeight * 0.5))).toInt()
//    return calculatedDuration.coerceIn(150, 250)
//}
//
//private fun determineCurrentApproximateState(
//    currentHeight: Float,
//    heightWhenPartiallyExpanded: Float,
//    heightWhenFullyExpanded: Float
//): BottomSheetState {
//    return when {
//        currentHeight < heightWhenPartiallyExpanded * 0.5f -> CLOSED
//        currentHeight < (heightWhenPartiallyExpanded + heightWhenFullyExpanded) * 0.5f -> PARTIALLY_EXPANDED
//        else -> EXPANDED
//    }
//}
//
//private fun determineStateAfterDrag(
//    currentHeight: Float,
//    dragVelocity: Float,
//    isLandscape: Boolean,
//    heightWhenPartiallyExpanded: Float,
//    heightWhenFullyExpanded: Float,
//    animatedHeight: Animatable<Float, *>,
//    scope: kotlinx.coroutines.CoroutineScope,
//    onStateChange: (BottomSheetState) -> Unit,
//    onDragFinished: () -> Unit
//) {
//    val currentApproximateState = determineCurrentApproximateState(
//        currentHeight,
//        heightWhenPartiallyExpanded,
//        heightWhenFullyExpanded
//    )
//
//    val newState = determineNewBottomSheetState(
//        isLandscape = isLandscape,
//        dragVelocity = dragVelocity,
//        currentApproximateState = currentApproximateState,
//        currentHeight = currentHeight,
//        heightWhenPartiallyExpanded = heightWhenPartiallyExpanded,
//        heightWhenFullyExpanded = heightWhenFullyExpanded
//    )
//
//    onStateChange(newState)
//    onDragFinished()
//
//    val targetHeightForState = calculateTargetHeightForState(
//        newState,
//        heightWhenPartiallyExpanded,
//        heightWhenFullyExpanded
//    )
//
//    if (abs(currentHeight - targetHeightForState) > 1f) {
//        scope.launch {
//            val distance = abs(currentHeight - targetHeightForState)
//            val animationDuration = calculateAnimationDuration(distance, heightWhenFullyExpanded)
//
//            animatedHeight.animateTo(
//                targetValue = targetHeightForState,
//                animationSpec = tween(
//                    durationMillis = animationDuration,
//                    easing = LinearOutSlowInEasing
//                )
//            )
//        }
//    }
//}
//
//private fun determineNewBottomSheetState(
//    isLandscape: Boolean,
//    dragVelocity: Float,
//    currentApproximateState: BottomSheetState,
//    currentHeight: Float,
//    heightWhenPartiallyExpanded: Float,
//    heightWhenFullyExpanded: Float
//): BottomSheetState {
//    val velocityThreshold = 300f
//    if (isLandscape) {
//        return EXPANDED
//    }
//
//    // Handle fast swipes - velocity-based decisions
//    if (dragVelocity > velocityThreshold) {
//        return when (currentApproximateState) {
//            EXPANDED -> PARTIALLY_EXPANDED
//            else -> CLOSED
//        }
//    }
//    if (dragVelocity < -velocityThreshold) {
//        return when (currentApproximateState) {
//            CLOSED -> PARTIALLY_EXPANDED
//            else -> EXPANDED
//        }
//    }
//
//    // Handle position-based decisions for normal drags
//    val expandedDragThreshold = (heightWhenFullyExpanded - heightWhenPartiallyExpanded) * 0.2f
//    val partialToExpandedThreshold = (heightWhenFullyExpanded - heightWhenPartiallyExpanded) * 0.2f
//    val partialToClosedThreshold = heightWhenPartiallyExpanded * 0.2f
//
//    val distanceToExpanded = abs(currentHeight - heightWhenFullyExpanded)
//    val distanceToPartial = abs(currentHeight - heightWhenPartiallyExpanded)
//
//    val isExpanded = currentApproximateState == EXPANDED
//    val isAboveThreshold = distanceToExpanded > expandedDragThreshold
//    if (isExpanded && isAboveThreshold) {
//        return PARTIALLY_EXPANDED
//    }
//
//    val isPartiallyExpanded = currentApproximateState == PARTIALLY_EXPANDED
//    val isReadyToExpand = currentHeight > heightWhenPartiallyExpanded + partialToExpandedThreshold
//    if (isPartiallyExpanded && isReadyToExpand) {
//        return EXPANDED
//    }
//
//    val isReadyToClose = currentHeight < heightWhenPartiallyExpanded - partialToClosedThreshold
//    if (isPartiallyExpanded && isReadyToClose) {
//        return CLOSED
//    }
//
//    // Default case: snap to nearest state
//    return when {
//        currentHeight < distanceToPartial && currentHeight < distanceToExpanded -> CLOSED
//        distanceToPartial < distanceToExpanded -> PARTIALLY_EXPANDED
//        else -> EXPANDED
//    }
//}
//
//@Composable
//fun BottomSheetExample() {
//    val configuration = LocalConfiguration.current
//    val isLandscapeOrientation = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
//
//    val bottomSheetState = rememberSimpleBottomSheetState(initialState = CLOSED)
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(color = Colors.BrandPrimary)
//    ) {
//        Column(
//            modifier = Modifier
//                .padding(top = 64.dp)
//                .align(alignment = Alignment.TopCenter)
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Box(
//                modifier = Modifier
//                    .clip(shape = RoundedCornerShape(6.dp))
//                    .background(color = Colors.MonochromeBackground)
//                    .padding(16.dp)
//                    .clickable {
//                        val currentState = bottomSheetState.stateForOrientation
//                        if (isLandscapeOrientation) {
//                            // In landscape, toggle between closed and expanded
//                            if (currentState == CLOSED) {
//                                bottomSheetState.expand()
//                            } else {
//                                bottomSheetState.close()
//                            }
//                        } else {
//                            // In portrait, cycle through all three states
//                            when (currentState) {
//                                CLOSED -> bottomSheetState.partiallyExpand()
//                                PARTIALLY_EXPANDED -> bottomSheetState.expand()
//                                EXPANDED -> bottomSheetState.close()
//                            }
//                        }
//                    }
//            ) {
//                Text(
//                    "Toggle bottom sheet (${bottomSheetState.stateForOrientation.name})",
//                    style = TextStyle(
//                        color = Colors.FontDefault
//                    )
//                )
//            }
//        }
//
//        SimpleBottomSheet(
//            state = bottomSheetState,
//            scrimColor = Color.Black.copy(alpha = 0.5f),
//            closeOnClickOutside = true
//        ) {
//            BottomSheetContent(
//                isLandscapeOrientation = isLandscapeOrientation,
//                closeOnClickOutside = true
//            )
//        }
//    }
//}
//
//@Composable
//fun BottomSheetContent(
//    isLandscapeOrientation: Boolean,
//    closeOnClickOutside: Boolean
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//    ) {
//        Text("This is a custom bottom sheet with three states:")
//        Text("• Closed")
//        if (!isLandscapeOrientation) {
//            Text("• Partially expanded")
//        }
//        Text("• Fully expanded")
//        if (isLandscapeOrientation) {
//            Text("(Landscape mode: drag disabled, always expanded)")
//            Text("In landscape mode, clicking outside won't close the sheet")
//        } else {
//            Text("Drag me up or down")
//            if (closeOnClickOutside) {
//                Text("Click outside to close the sheet")
//            } else {
//                Text("Clicking outside won't close the sheet")
//            }
//        }
//
//        // 200.dp height scrollable lazy list
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(200.dp)
//                .clip(RoundedCornerShape(8.dp))
//                .background(Color.LightGray.copy(alpha = 0.2f))
//        ) {
//            items(List(30) { "Item ${it + 1}" }) { item ->
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 8.dp, vertical = 4.dp)
//                ) {
//                    Text(
//                        text = item,
//                        modifier = Modifier.padding(16.dp),
//                        style = TextStyle(
//                            fontSize = 12.sp
//                        )
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Preview
//@Composable
//fun BottomSheetPreview() {
//    BottomSheetExample()
//}
//
