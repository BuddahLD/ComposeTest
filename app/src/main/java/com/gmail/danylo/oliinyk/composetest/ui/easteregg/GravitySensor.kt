package com.gmail.danylo.oliinyk.composetest.ui.easteregg

import android.content.Context
import android.content.res.Configuration
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import timber.log.Timber

data class GravityVector(
    val x: Float,  // Horizontal gravity (left-right)
    val y: Float   // Vertical gravity (down direction)
)

@Composable
fun rememberGravitySensor(enableDeviceGravity: Boolean = true): GravityVector {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    var gravityVector by remember { mutableStateOf(GravityVector(0f, -1f)) }
    
    // Get the actual device orientation
    val orientation = configuration.orientation
    
    DisposableEffect(enableDeviceGravity, context, orientation) {
        // Use a holder object for smoothing state
        class SmoothState(var x: Float = 0f, var y: Float = 0f)
        val smooth = SmoothState()
        val alpha = 0.2f  // Smoothing factor (0-1, lower = smoother, more stable)
        
        val listener: SensorEventListener? = if (enableDeviceGravity) {
            val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
            val accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            
            if (sensorManager == null || accelerometer == null) {
                Timber.tag("EasterEgg").w("No accelerometer available")
                null
            } else {
                object : SensorEventListener {
                    override fun onSensorChanged(event: SensorEvent?) {
                        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                            // Accelerometer values in m/s²
                            // For a 2D screen display, we need to extract gravity direction
                            // values[0] = X (left-right acceleration)
                            // values[1] = Y (forward-back acceleration) 
                            // values[2] = Z (vertical acceleration)
                            
                            val ax = event.values[0]
                            val ay = event.values[1]
                            val az = event.values[2]
                            
                            // Calculate magnitude for normalization
                            val magnitude = kotlin.math.sqrt(ax * ax + ay * ay + az * az)
                            if (magnitude > 0.1f) {
                            // Normalize to get direction
                            // For 2D screen where Y increases downward:
                            // - X component: invert ax because of Android coordinate system
                            // - Y component: use ay for forward-back tilt
                            val rawX = -ax / magnitude  // Horizontal component (inverted!)
                            val rawY = ay / magnitude   // Vertical component (forward-back tilt)
                                
                                // Exponential smoothing to reduce jitter
                                smooth.x = rawX * alpha + smooth.x * (1 - alpha)
                                smooth.y = rawY * alpha + smooth.y * (1 - alpha)
                                
                                // Update gravity vector based on screen orientation
                                var newX = smooth.x.coerceIn(-1f, 1f)
                                var newY = smooth.y.coerceIn(-1f, 1f)
                                
                                // Map device accelerometer to screen coordinates based on orientation
                                // In portrait: screen X = device X, screen Y = device Y
                                // In landscape: screen X = -device Y, screen Y = device X (rotated 90 degrees)
                                when (orientation) {
                                    Configuration.ORIENTATION_LANDSCAPE -> {
                                        val temp = newX
                                        newX = -newY
                                        newY = temp
                                    }
                                    else -> {
                                        // Portrait - no transformation needed
                                    }
                                }
                                
                                // Only log occasionally to reduce spam
                                if (System.currentTimeMillis() % 1000 < 50) {
                                    Timber.tag("EasterEgg").d("Gravity: values=[%.2f,%.2f,%.2f] -> dir=[%.2f,%.2f], orient=$orientation",
                                        ax, ay, az, newX, newY)
                                }
                                
                                gravityVector = GravityVector(x = newX, y = newY)
                            }
                        }
                    }
                    
                    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
                }.also { listener ->
                    sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_GAME)
                    Timber.tag("EasterEgg").d("Gravity sensor registered")
                }
            }
        } else {
            gravityVector = GravityVector(0f, -1f)
            null
        }
        
        onDispose {
            listener?.let {
                val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
                Timber.tag("EasterEgg").d("Unregistering gravity sensor")
                sensorManager?.unregisterListener(it)
            }
        }
    }
    
    return gravityVector
}
