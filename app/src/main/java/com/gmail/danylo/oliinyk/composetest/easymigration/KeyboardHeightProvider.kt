package com.gmail.danylo.oliinyk.composetest.easymigration

import android.app.Activity
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class KeyboardHeightProvider(
    // The root activity_main that uses this KeyboardHeightProvider
    private val activity: Activity,
    rootView: View
) : PopupWindow(activity) {

    // The parent view
    private val parentView: View

    // The keyboard height observer
    private var observer: KeyboardHeightObserver? = null

    // The view that is used to calculate the keyboard height
    private var popupView: View? = null

    private var isImeVisible = false
    private var isAnimationInProgress = false
    private var currentImeHeight = 0

    init {
        if (VersionChecker.isApi30orAbove) {
            parentView = rootView

//            ViewCompat.setOnApplyWindowInsetsListener(parentView) { _, windowInsets ->
//                Log.d("висота", "setOnApplyWindowInsetsListener")
//                if (!isAnimationInProgress) {
//                    applyKeyboardInset(windowInsets, parentView)
//                }
//                windowInsets
//            }

            ViewCompat.setWindowInsetsAnimationCallback(
                parentView,
                object : WindowInsetsAnimationCompat.Callback(DISPATCH_MODE_STOP) {
                    override fun onPrepare(animation: WindowInsetsAnimationCompat) {
                        isAnimationInProgress = true
                    }

                    override fun onProgress(
                        insets: WindowInsetsCompat,
                        runningAnimations: MutableList<WindowInsetsAnimationCompat>
                    ): WindowInsetsCompat {
                        Log.d("висота", "onProgress")
                        if (isAnimationInProgress) {
                            applyKeyboardInset(insets, parentView)
                        }
                        return insets
                    }

                    override fun onEnd(animation: WindowInsetsAnimationCompat) {
                        isAnimationInProgress = false
                    }
                }
            )
        } else {
            popupView = View(activity)
            contentView = popupView

            softInputMode =
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
            inputMethodMode = INPUT_METHOD_NEEDED

            parentView = activity.findViewById(android.R.id.content)

            width = 0
            height = WindowManager.LayoutParams.MATCH_PARENT

            popupView?.getViewTreeObserver()?.addOnGlobalLayoutListener {
                if (popupView != null) {
                    handleOnGlobalLayout()
                }
            }
        }
    }

    private fun applyKeyboardInset(windowInsets: WindowInsetsCompat, v: View) {
        isImeVisible = windowInsets.isVisible(WindowInsetsCompat.Type.ime())
        val imeHeight = windowInsets.getInsets(WindowInsetsCompat.Type.ime()).bottom
        if (currentImeHeight == imeHeight) {
            return
        }
        currentImeHeight = imeHeight
        val orientation = screenOrientation
        if (!isImeVisible) {
            notifyKeyboardHeightChanged(0, orientation)
        } else {
            notifyKeyboardHeightChanged(imeHeight, orientation)
        }
    }

    /**
     * Start the KeyboardHeightProvider, this must be called after the onResume of the Activity.
     * PopupWindows are not allowed to be registered before the onResume has finished
     * of the Activity.
     */
    fun start() {
        if (!isShowing && parentView.windowToken != null) {
            setBackgroundDrawable(ColorDrawable(0))
            showAtLocation(parentView, Gravity.NO_GRAVITY, 0, 0)
        }
    }

    /**
     * Close the keyboard height provider,
     * this provider will not be used anymore.
     */
    fun close() {
        this.observer = null
        dismiss()
    }

    /**
     * Set the keyboard height observer to this provider. The
     * observer will be notified when the keyboard height has changed.
     * For example when the keyboard is opened or closed.
     *
     * @param observer The observer to be added to this provider.
     */
    fun setKeyboardHeightObserver(observer: KeyboardHeightObserver?) {
        this.observer = observer
    }

    private val screenOrientation: Int
        /**
         * Get the screen orientation
         *
         * @return the screen orientation
         */
        get() = activity.resources.configuration.orientation

    /**
     * Popup window itself is as big as the window of the Activity.
     * The keyboard can then be calculated by extracting the popup view bottom
     * from the activity_main window height.
     */
    private fun handleOnGlobalLayout() {
        val screenSize = Point()
        activity.windowManager.defaultDisplay.getSize(screenSize)

        val rect = Rect()
        popupView!!.getWindowVisibleDisplayFrame(rect)

        val orientation = screenOrientation
        val keyboardHeight = screenSize.y - rect.bottom + rect.top
        if (keyboardHeight <= 0) {
            currentImeHeight = 0
            notifyKeyboardHeightChanged(0, orientation)
        } else {
            if (currentImeHeight == keyboardHeight) {
                return
            }
            currentImeHeight = keyboardHeight
            notifyKeyboardHeightChanged(keyboardHeight, orientation)
        }
    }

    private fun notifyKeyboardHeightChanged(height: Int, orientation: Int) {
        KeyboardHeightHolder.updateHeight(keyboardHeight = height)
        observer?.onKeyboardHeightChanged(height, orientation)
    }

    companion object {

        /**
         * The tag for logging purposes
         */
        private const val TAG = "sample_KeyboardHeightProvider"
    }
}

/**
 * Needed because using LocalProvider keyboard height is not working rn, so we pass values from
 * Activity.
 */
object KeyboardHeightHolder {
    private val _state = MutableStateFlow(0)
    val state = _state.asStateFlow()

    fun updateHeight(keyboardHeight: Int) {
        _state.update { keyboardHeight }
    }
}
