package com.gmail.danylo.oliinyk.composetest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.gmail.danylo.oliinyk.composetest.ui.ClearMeAfterUsage

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
//            ScreenProperties()
            ClearMeAfterUsage()
        }
    }
}
