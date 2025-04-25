package com.gmail.danylo.oliinyk.composetest

import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.gmail.danylo.oliinyk.composetest.easymigration.MiniVideoChatTheme
import com.gmail.danylo.oliinyk.composetest.easymigration.VersionChecker
import com.gmail.danylo.oliinyk.composetest.ui.NestedScrollTest

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getRootView()?.let {
            hideSystemUI(window, it)
        }
        setContent {
            MiniVideoChatTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White)
                ) {
//                    ClearMeAfterUsage()
                    NestedScrollTest()
//                    NestedScrollTestBoxes()
//                    ScrollTestBox()
                }
            }
        }
    }

    private fun getRootView(): View? = try {
        window.decorView.findViewById<View>(android.R.id.content)
    } catch (e: Exception) {
        null
    }

    fun hideSystemUI(window: Window, mainContainer: View) {
        if (VersionChecker.isApi30orAbove) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowInsetsControllerCompat(window, mainContainer).let { controller ->
                controller.hide(WindowInsetsCompat.Type.statusBars())
                controller.hide(WindowInsetsCompat.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }
}
