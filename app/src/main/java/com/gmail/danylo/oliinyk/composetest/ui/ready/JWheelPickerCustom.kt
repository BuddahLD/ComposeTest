package com.gmail.danylo.oliinyk.composetest.ui.ready

import android.content.Context
import android.view.HapticFeedbackConstants
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmail.danylo.oliinyk.composetest.easymigration.Colors
import com.gmail.danylo.oliinyk.composetest.easymigration.Colors.FontDefault
import io.john6.base.compose.picker.JWheelPickerHelper
import io.john6.base.compose.picker.JWheelPickerHelper.drawPickerLineOverlay
import io.john6.base.compose.picker.bean.JWheelPickerItemInfo
import io.john6.base.compose.picker.bean.getText
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin

private val TextHeight = 24.dp
val AgeData = (18..99).map { index ->
    JWheelPickerItemInfo(
        id = index.toString(),
        index = index,
        fallbackText = index.toString()
    )
}.toPersistentList()

@Composable
fun HorizontalAgePicker() {

    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val wheelHeight = 22.dp * 2 + TextHeight
        val itemWidth = 40.dp

        JWheelPickerHorizontal(
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .height(wheelHeight)
                .alpha(0.99f),
            size = maxWidth - 32.dp * 2,
            itemWidthDp = itemWidth,
            onSelectedItemChanged = {},
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 34.sp
            ),
            selectedTextColor = FontDefault,
            drawOverLay = { itemHeightPx, _ ->
                drawHorizontalPickerLineOverlay(
                    itemWidthPx = itemWidth.roundToPx(),
                    itemHeightPx = itemHeightPx,
                    windowWidthAdjustmentDp = 4.dp,
                    scrimColor = Colors.MonochromeBackground.copy(alpha = 0.5f)
                )
            },
            initialIndex = 0,
            itemCount = AgeData.size,
            itemData = { index ->
                AgeData[index]
            },
        )
    }
}

private fun ContentDrawScope.drawHorizontalPickerLineOverlay(
    itemWidthPx: Int,
    itemHeightPx: Int,
    scrimColor: Color,
    windowWidthAdjustmentDp: Dp = 0.dp
) {
    val width = this.size.width
    val height = this.size.height
    val windowWidthAdjustmentPx = windowWidthAdjustmentDp.toPx()

    // Calculate the positions for the transparent window
    val left = (width - itemWidthPx - windowWidthAdjustmentPx) / 2
    val right = (width + itemWidthPx + windowWidthAdjustmentPx) / 2
    val top = (height - itemHeightPx) / 2
    val bottom = (height + itemHeightPx) / 2

    // Draw the left semi-transparent rectangle
    drawRect(
        color = scrimColor,
        size = Size(left, height)
    )

    // Draw the right semi-transparent rectangle
    drawRect(
        color = scrimColor,
        topLeft = Offset(right, 0f),
        size = Size(width - right, height)
    )

    // Optionally, draw top and bottom rectangles if needed
    drawRect(
        color = scrimColor,
        size = Size(width, top)
    )

    drawRect(
        color = scrimColor,
        topLeft = Offset(0f, bottom),
        size = Size(width, height - bottom)
    )
}

/**
 * iOS style data picker
 *
 * @param enableHapticFeedback vibrate using [android.view.View.performHapticFeedback]
 * @param confirmSelectDistanceThreshold  item will be selected while item centerY in [picker center - this , picker center + this]
 * @param itemCount Number of scrollable items
 * @param itemData a function which takes an index parameter and returns a [JWheelPickerItemInfo]
 *
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JWheelPickerHorizontal(
    modifier: Modifier = Modifier,
    size: Dp = 240.dp,
    itemWidthDp: Dp = Dp.Unspecified,
    itemPadding: Dp = 4.dp,
    selectedTextColor: Color = Color.Unspecified,
    enableHapticFeedback: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    confirmSelectDistanceThreshold: Float = 20f,
    itemCount: Int,
    itemData: (index: Int) -> JWheelPickerItemInfo,
    initialIndex: Int = 0,
    drawOverLay: (ContentDrawScope.(itemHeightPx: Int, edgeOffsetYPx: Float) -> Unit)? = { itemHeightPx, edgeOffsetYPx ->
        drawPickerLineOverlay(edgeOffsetYPx = edgeOffsetYPx, itemHeightPx = itemHeightPx)
    },
    onSelectedItemChanged: ((itemData: JWheelPickerItemInfo) -> Unit)? = null,
) {
    if (itemWidthDp == Dp.Unspecified) {
        throw RuntimeException("Item width must be specified")
    }

    val localView = LocalView.current
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val isDragged = lazyListState.interactionSource.collectIsDraggedAsState().value
    val isScrolling = lazyListState.isScrollInProgress

    // Item height
    val fontSize = textStyle.fontSize.takeIf { it != TextUnit.Unspecified } ?: 14.sp
    val itemHeightPx by remember(fontSize, itemPadding) {
        mutableIntStateOf(with(density) { (fontSize.toPx() + (itemPadding * 2).toPx()).roundToInt() })
    }
    // Offset needed to move edge items to the center
    val edgeOffsetPx = remember(size, itemHeightPx, itemWidthDp) {
        (with(density) { size.toPx() - itemWidthDp.toPx() }) / 2f
    }
    val edgeOffsetDp = remember(size) {
        with(density) { edgeOffsetPx.absoluteValue.toDp() }
    }

    LaunchedEffect(key1 = itemCount) {
        if (itemCount == 0) {
            onSelectedItemChanged?.invoke(JWheelPickerItemInfo.EMPTY)
        } else {
            onSelectedItemChanged?.invoke(itemData(initialIndex))
        }
    }

    var currentSelectedItemIndex by remember { mutableIntStateOf(initialIndex) }
    var desireSelectedItemIndex by remember { mutableIntStateOf(initialIndex) }

    val animateScrollToItemCenter: (Pair<Int, Float>) -> Unit = remember(onSelectedItemChanged) {
        { centerItemInfo ->
            val centerItemIndex = centerItemInfo.first
            if (centerItemIndex >= 0) {
                if (currentSelectedItemIndex != centerItemIndex) {
                    currentSelectedItemIndex = centerItemIndex
                    onSelectedItemChanged?.invoke(itemData(currentSelectedItemIndex))
                }
                scope.launch { lazyListState.animateScrollToItem(centerItemIndex) }
            }
        }
    }

    val performHapticFeedback = {
        if (enableHapticFeedback) {
            scope.launch {
                localView.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
            }
        }
    }

    val getLayoutInfo = {
        lazyListState.layoutInfo
    }

    // Final text style
    val desireTextStyle: (Int) -> TextStyle = if (selectedTextColor == Color.Unspecified) {
        { // If no selected text color is set, use an immutable method
            textStyle
        }
    } else { // If selected text color is set, change color based on selected index
        remember(initialIndex, currentSelectedItemIndex, selectedTextColor, textStyle.color) {
            {
                val finalColor = if (it != currentSelectedItemIndex) {
                    textStyle.color
                } else {
                    selectedTextColor
                }
                textStyle.copy(color = finalColor)
            }
        }
    }

    var needPerformSnapScroll by remember { mutableStateOf(false) }

    var hasPerformHapticFeedback by remember {
        mutableStateOf(true)
    }

    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        LazyRow(
            modifier = modifier
                .width(size)
                .disableParentNestedHorizontalScroll()
                .drawWithContent {
                    this.drawContent()
                    drawOverLay?.invoke(this, itemHeightPx, edgeOffsetPx)
                },
            state = lazyListState,
            contentPadding = PaddingValues(horizontal = edgeOffsetDp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items(itemCount, key = { itemData(it) }) {
                HorizontalWheelPickerItem(
                    index = it,
                    itemSizeDp = DpSize(
                        width = itemWidthDp,
                        height = with(density) { itemHeightPx.toDp() }
                    ),
                    text = itemData(it).getText(),
                    textStyle = desireTextStyle(it),
                    getLayoutInfo = getLayoutInfo,
                )
            }
        }

    }

    if (isScrolling) {
        val centerItemInfo = getCurrentCenterItemInfo(getLayoutInfo)
        if (centerItemInfo.first != desireSelectedItemIndex) {
            desireSelectedItemIndex = centerItemInfo.first
            hasPerformHapticFeedback = false
        }

        // Current selected value changes && OffsetY is within the haptic feedback range
        if (!hasPerformHapticFeedback && centerItemInfo.second.absoluteValue <= confirmSelectDistanceThreshold) {
            hasPerformHapticFeedback = true
            performHapticFeedback()
        }
        if (isDragged) {
            needPerformSnapScroll = true
        }
    } else {
        // Not scrolling && User is not dragging (finger is not on the screen)
        if (needPerformSnapScroll && !isDragged) {
            needPerformSnapScroll = false
            animateScrollToItemCenter(desireSelectedItemIndex to 0f)
        }
    }
}

@Composable
private fun HorizontalWheelPickerItem(
    index: Int,
    text: String,
    itemSizeDp: DpSize,
    textStyle: TextStyle,
    getLayoutInfo: () -> LazyListLayoutInfo,
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .size(itemSizeDp)
        .graphicsLayer {
            render3DHorizontalItemEffect(index, getLayoutInfo)
        }) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
                .align(Alignment.Center),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = textStyle,
        )
    }
}

private fun GraphicsLayerScope.render3DHorizontalItemEffect(
    index: Int,
    getLayoutInfo: () -> LazyListLayoutInfo,
) {
    val layoutInfo = getLayoutInfo()
    val itemInfo = layoutInfo.visibleItemsInfo.find { i -> i.index == index }
                   ?: return

    // Item X coordinate
    val posX = getItemCenter(itemInfo) + layoutInfo.beforeContentPadding
    val centerX = layoutInfo.viewportSize.width / 2F
    // Item offset
    val offset = (centerX - posX) / centerX
    // If the item rotateX is already out of view, there's no point in displaying it
    if (offset.absoluteValue >= 1.0f) {
        alpha = 0f
        return
    }

    rotationY = -90 * offset

    val scale = 1 - (offset.absoluteValue).pow(2) * 0.37f
    scaleY = scale

    translationX = if (offset == 0f) {
        0f
    } else {
        // Radius
        val r = (2f * centerX / Math.PI).toFloat()
        // Visual Y coordinate position
        val h =
            (sin(Math.toRadians(offset.absoluteValue * 90.0)) * r * JWheelPickerHelper.defaultHorizontalWheelCurveRate).toFloat()
        val diffX = if (offset > 0) {
            (centerX - h.absoluteValue) - posX.absoluteValue
        } else {
            (centerX + h.absoluteValue) - posX.absoluteValue
        }
        diffX
    }
}

/**
 * Get the offset value of the current item's center point on the Y-axis
 */
private fun getItemCenter(itemInfo: LazyListItemInfo): Float {
    val itemCenter = itemInfo.size / 2F
    return itemInfo.offset.toFloat() + itemCenter
}

/**
 * Get the index of the item closest to the center of the control
 *
 * @return first: Index of the item closest to the center, second: diffY
 */
private fun getCurrentCenterItemInfo(
    getLayoutInfo: () -> LazyListLayoutInfo
): Pair<Int, Float> {
    return getLayoutInfo().run {
        val center = (viewportEndOffset - viewportStartOffset) / 2F
        var res = -1
        var minDiff = Float.MAX_VALUE
        visibleItemsInfo.forEach {
            val tempOffsetPos = getItemCenter(it) + beforeContentPadding - center
            if (tempOffsetPos.absoluteValue < minDiff.absoluteValue) {
                minDiff = tempOffsetPos
                res = it.index
            }
        }
        res to minDiff
    }
}

fun calculateSpForDp(context: Context, dp: Float): TextUnit {
    val density = context.resources.displayMetrics.density
    val fontScale = context.resources.configuration.fontScale

    // Convert dp to pixels
    val pixels = dp * density

    // Convert pixels to sp
    val spValue = pixels / fontScale

    return spValue.sp
}

private fun Modifier.disableParentNestedHorizontalScroll(disabled: Boolean = true) =
    if (disabled) this.nestedScroll(HorizontalParentScrollConsumer) else this

private val HorizontalParentScrollConsumer = object : NestedScrollConnection {

    override suspend fun onPostFling(consumed: Velocity, available: Velocity) = available

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset = available
}
