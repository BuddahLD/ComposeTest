package com.gmail.danylo.oliinyk.composetest.easymigration

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

object VersionChecker {

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
    @JvmStatic
    fun checkIs12OrUp(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N)
    fun checkIsSevenOrUp(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
    }

    /**
     * In API 30 (Android 11) we can use insets listener for safe area handling or keyboard
     * animated height.
     */
    @JvmStatic
    val isApi30orAbove: Boolean
        @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

    val isApi28orAbove: Boolean
        @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.P)
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
}
