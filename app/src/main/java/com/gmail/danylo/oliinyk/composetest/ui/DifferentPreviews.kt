package com.gmail.danylo.oliinyk.composetest.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(showSystemUi = true, widthDp = 690, heightDp = 828)
@Composable
fun Preview1() {

}

@Preview(
    showSystemUi = true,
    device = "spec:width=690dp, height=988dp, orientation=landscape"
)
@Composable
fun Preview2() {

}

@Preview(
    showSystemUi = true,
    device = "spec:width=690dp, height=828dp, orientation=portrait"
)
@Composable
fun Preview3() {

}

