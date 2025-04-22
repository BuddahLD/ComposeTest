package com.gmail.danylo.oliinyk.composetest.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmail.danylo.oliinyk.composetest.easymigration.Colors
import com.gmail.danylo.oliinyk.composetest.ui.BottomSheetState.CLOSED
import com.gmail.danylo.oliinyk.composetest.ui.BottomSheetState.EXPANDED
import com.gmail.danylo.oliinyk.composetest.ui.BottomSheetState.PARTIALLY_EXPANDED

@Composable
fun BottomSheetExample() {
    val configuration = LocalConfiguration.current
    val isLandscapeOrientation = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val bottomSheetState = rememberSimpleBottomSheetState(initialState = CLOSED)
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.BrandPrimary)
    ) {
        Column(
            modifier = Modifier
                .padding(top = 64.dp)
                .align(alignment = Alignment.TopCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(6.dp))
                    .background(color = Colors.MonochromeBackground)
                    .padding(16.dp)
                    .clickable {
                        val currentState = bottomSheetState.stateForOrientation
                        if (isLandscapeOrientation) {
                            // In landscape, toggle between closed and expanded
                            if (currentState == CLOSED) {
                                bottomSheetState.expand()
                            } else {
                                bottomSheetState.close()
                            }
                        } else {
                            // In portrait, cycle through all three states
                            when (currentState) {
                                CLOSED -> bottomSheetState.partiallyExpand()
                                PARTIALLY_EXPANDED -> bottomSheetState.expand()
                                EXPANDED -> bottomSheetState.close()
                            }
                        }
                    }
            ) {
                Text(
                    "Toggle bottom sheet (${bottomSheetState.stateForOrientation.name})",
                    style = TextStyle(
                        color = Colors.FontDefault
                    )
                )
            }
        }

        SimpleBottomSheet(
            state = bottomSheetState,
            scrimColor = Color.Black.copy(alpha = 0.5f),
            closeOnClickOutside = true
        ) {
            BottomSheetContent(
                isLandscapeOrientation = isLandscapeOrientation,
                closeOnClickOutside = true
            )
        }
    }
}

@Composable
fun BottomSheetContent(
    isLandscapeOrientation: Boolean,
    closeOnClickOutside: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("This is a custom bottom sheet with three states:")
        Text("• Closed")
        if (!isLandscapeOrientation) {
            Text("• Partially expanded")
        }
        Text("• Fully expanded")
        if (isLandscapeOrientation) {
            Text("(Landscape mode: drag disabled, always expanded)")
            Text("In landscape mode, clicking outside won't close the sheet")
        } else {
            Text("Drag me up or down")
            if (closeOnClickOutside) {
                Text("Click outside to close the sheet")
            } else {
                Text("Clicking outside won't close the sheet")
            }
        }

        // 200.dp height scrollable lazy list
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray.copy(alpha = 0.2f))
        ) {
            items(List(30) { "Item ${it + 1}" }) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = item,
                        modifier = Modifier.padding(16.dp),
                        style = TextStyle(
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun BottomSheetPreview() {
    BottomSheetExample()
}

