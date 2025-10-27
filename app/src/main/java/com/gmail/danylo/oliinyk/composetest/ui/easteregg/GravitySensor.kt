package com.gmail.danylo.oliinyk.composetest.ui.easteregg

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import timber.log.Timber

data class GravityVector(
    val x: Float,  // Horizontal gravity (left-right)
    val y: Float   // Vertical gravity (down direction)
)

@Composable
fun rememberGravitySensor(enableDeviceGravity: Boolean = true): GravityVector {
    val context = LocalContext.current
    var gravityVector by remember { mutableStateOf(GravityVector(0f, -1f)) }
    
    DisposableEffect(enableDeviceGravity, context) {
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
                            // Accelerometer values in m/s² (device orientation)
                            // values[0] = X axis (left-right tilt)
                            // values[1] = Y axis (forward-back tilt, but we want screen up-down)
                            // values[2] = Z axis (vertical when flat)
                            
                            // Extract tilt from accelerometer
                            // When flat: X=0, Y=0, Z=9.8
                            // When tilted right: X is non-zero
                            // When tilted forward (top down): Y is non-zero
                            
                            val rawX = event.values[0] / 9.8f  // Left-right tilt
                            val rawY = -event.values[1] / 9.8f  // Forward-back tilt (inverted for screen coordinates)
                            
                            // Exponential smoothing to reduce jitter
                            smooth.x = rawX * alpha + smooth.x * (1 - alpha)
                            smooth.y = rawY * alpha + smooth.y * (1 - alpha)
                            
                            // Update gravity vector
                            val newX = smooth.x.coerceIn(-1f, 1f)
                            val newY = smooth.y.coerceIn(-1f, 1f)
                            
                            // Only log occasionally to reduce spam
                            if (System.currentTimeMillis() % 1000 < 50) {
                                Timber.tag("EasterEgg").d("Gravity: raw=[%.2f,%.2f] smooth=[%.2f,%.2f]",
                                    event.values[0], event.values[1], newX, newY)
                            }
                            
                            gravityVector = GravityVector(x = newX, y = newY)
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
