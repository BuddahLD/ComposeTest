package com.gmail.danylo.oliinyk.composetest.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imeAnimationTarget
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ClearMeAfterUsage() {
    val isImeVisible = WindowInsets.isImeVisible
    val density = LocalDensity.current
    val ime = WindowInsets.ime
    var text by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    var timer by remember { mutableLongStateOf(0L) }
    var isKeyboardVisible by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Timber.tag("інпут").d(
                          "isImeVisible = $isImeVisible, " +
                          "time = ${(System.currentTimeMillis() - timer).toString().takeLast(5)}, " +
                          "bottom = ${ime.getBottom(density)}, " +
                          "imeAnimationTarget bottom = ${WindowInsets.imeAnimationTarget.getBottom(density)}"
    )

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.weight(1f).fillMaxWidth().background(color = Color.Yellow)
        )


        Box(
            modifier = Modifier.weight(1f).fillMaxWidth().background(color = Color.Magenta)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
//            .imePadding()
            .padding(bottom = 16.dp + 48.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        BasicTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .height(44.dp)
                .padding(horizontal = 16.dp)
                .background(Color.White)
                .border(2.dp, Color.Blue.copy(alpha = .3f)),
            singleLine = true
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    val millis = System.currentTimeMillis()
                    timer = millis
                    Timber.tag("інпут").d("time: ${millis.toString().takeLast(5)}")
                    scope.launch {
                        if (isKeyboardVisible) {
                            isKeyboardVisible = false
                            //                                        focusRequester.freeFocus()
                            keyboardController?.hide()
                        } else {
                            isKeyboardVisible = true
                            focusRequester.requestFocus()
                            keyboardController?.show()
                        }
                    }
                }
        )
    }
}
