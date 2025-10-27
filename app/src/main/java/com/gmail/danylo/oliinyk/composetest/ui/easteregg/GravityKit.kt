package com.gmail.danylo.oliinyk.composetest.ui.easteregg

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.toSize
import timber.log.Timber
import kotlin.math.sqrt

// --- Public API ---

data class GravityProbe(
    val id: String,
    val label: String = "",
    val color: Color = Color(0xFF4CAF50)
)

@Composable
fun rememberGravityRegistry(): GravityRegistry = remember { GravityRegistry() }

@Stable
fun Modifier.gravityProbe(
    registry: GravityRegistry,
    probe: GravityProbe
): Modifier = this.then(
    Modifier.onGloballyPositioned { coords ->
        val pos = coords.positionInRoot()
        val size = coords.size.toSize()
        val rect = Rect(pos, Size(size.width, size.height))
        Timber.tag("EasterEgg").d("Registering ${probe.id} at pos=(${pos.x}, ${pos.y}), size=(${size.width}, ${size.height})")
        registry.items[probe.id] = ProbedItem(
            id = probe.id, label = probe.label, color = probe.color, rect = rect
        )
        Timber.tag("EasterEgg").d("Registry now has ${registry.items.size} items")
    }
)

@Composable
fun GravityOverlay(
    visible: Boolean,
    registry: GravityRegistry,
    modifier: Modifier = Modifier.fillMaxSize(),
    useDeviceGravity: Boolean = true,  // Use phone's accelerometer for gravity direction
    gravity: Float = 2800f,      // px/s^2 (tweak per device density)
    restitution: Float = 0.15f,  // 0..1, lower => stickier pile
    friction: Float = 0.985f,    // floor friction
    wallFriction: Float = 0.995f // mild damping on wall hits
) {
    if (!visible) return
    
    Timber.tag("EasterEgg").d("GravityOverlay visible=$visible, registry size=${registry.items.size}")

    // Get gravity vector
    val gravityVec = rememberGravitySensor(enableDeviceGravity = useDeviceGravity)
    val currentGravityVec = androidx.compose.runtime.rememberUpdatedState(gravityVec)
    
    // Convert probed rects to physics bodies once per session
    val initialBodies = remember(registry.items.values.toList()) {
        val bodyList = registry.items.values.map { it.toBody() }.toMutableList()
        Timber.tag("EasterEgg").d("Created ${bodyList.size} bodies")
        bodyList.forEach { body ->
            Timber.tag("EasterEgg").d("Body created: id=${body.label}, pos=(${body.x}, ${body.y}), r=${body.r}")
        }
        bodyList
    }
    val bodies = remember { initialBodies }
    var lastNanos by remember { mutableStateOf<Long?>(null) }
    var tick by remember { mutableStateOf(0) }

    BoxWithConstraints(modifier) {
        val W = constraints.maxWidth.toFloat()
        val H = constraints.maxHeight.toFloat()

        // Physics loop ~60Hz
        LaunchedEffect(W, H) {
            Timber.tag("EasterEgg").d("Starting physics loop, bounds=($W, $H), body count=${bodies.size}")
            while (true) {
                withFrameNanos { now ->
                    val dt = lastNanos?.let { (now - it) / 1_000_000_000f } ?: 0f
                    lastNanos = now
                    if (dt <= 0f || dt > 0.05f) return@withFrameNanos
                    
                    // Increment tick to trigger recomposition
                    tick = (tick + 1) % 1000

                    // Read current gravityVec inside the loop to get latest value
                    val currentVec = currentGravityVec.value
                    val gx = currentVec.x * gravity
                    val gy = currentVec.y * gravity
                    
                    // Log occasionally for debugging
                    if (tick % 100 == 0) {
                        Timber.tag("EasterEgg").d("Physics: currentGravityVec=(${String.format("%.2f", currentVec.x)}, ${String.format("%.2f", currentVec.y)}), gx=$gx, gy=$gy")
                    }

                    // 1) Integrate with directional gravity
                    bodies.forEach { b ->
                        b.vx += gx * dt
                        b.vy += gy * dt
                        b.x += b.vx * dt
                        b.y += b.vy * dt

                        // 2) Walls
                        if (b.x - b.r < 0f) { b.x = b.r; b.vx = -b.vx * (1 - restitution); b.vy *= wallFriction }
                        if (b.x + b.r > W) { b.x = W - b.r; b.vx = -b.vx * (1 - restitution); b.vy *= wallFriction }
                        if (b.y + b.r > H) {
                            b.y = H - b.r
                            b.vy = -b.vy * (1 - restitution)
                            b.vx *= friction
                        }
                        if (b.y - b.r < 0f) { b.y = b.r; b.vy = -b.vy * (1 - restitution); b.vx *= wallFriction }
                    }

                    // 3) Pairwise circle collisions (O(n^2))
                    for (i in bodies.indices) for (j in (i + 1) until bodies.size) {
                        val a = bodies[i]; val c = bodies[j]
                        val dx = c.x - a.x
                        val dy = c.y - a.y
                        val d2 = dx*dx + dy*dy
                        val minD = a.r + c.r
                        if (d2 > 0f && d2 < minD * minD) {
                            val d = sqrt(d2)
                            val nx = dx / d
                            val ny = dy / d
                            val penetration = minD - d

                            // Positional correction (mass-proportional)
                            val tm = a.mass + c.mass
                            val kA = c.mass / tm
                            val kC = a.mass / tm
                            a.x -= nx * penetration * kA
                            a.y -= ny * penetration * kA
                            c.x += nx * penetration * kC
                            c.y += ny * penetration * kC

                            // Impulse response
                            val rvx = c.vx - a.vx
                            val rvy = c.vy - a.vy
                            val velN = rvx * nx + rvy * ny
                            if (velN < 0f) {
                                val e = restitution
                                val invMA = 1f / a.mass
                                val invMC = 1f / c.mass
                                val j = -(1 + e) * velN / (invMA + invMC)
                                val impX = j * nx
                                val impY = j * ny
                                a.vx -= impX * invMA
                                a.vy -= impY * invMA
                                c.vx += impX * invMC
                                c.vy += impY * invMC
                            }
                        }
                    }
                }
            }
        }

        // Draw proxies - use tick to trigger recomposition
        key(tick) {
            val drawGravityVec = currentGravityVec.value
            Canvas(Modifier.fillMaxSize()) {
                // Draw bodies
                bodies.forEach { b ->
                    drawCircle(color = b.color.copy(alpha = 0.9f), radius = b.r, center = Offset(b.x, b.y))
                }
                
                // Draw gravity vector indicator (center of screen, pointing to gravity direction)
                val indicatorLength = 200f
                val baseX = W / 2f
                val baseY = H / 2f
                val arrowEndX = baseX + drawGravityVec.x * indicatorLength
                val arrowEndY = baseY + drawGravityVec.y * indicatorLength
                
                // Draw main arrow line
                drawLine(
                    color = Color.Black,
                    start = Offset(baseX, baseY),
                    end = Offset(arrowEndX, arrowEndY),
                    strokeWidth = 6f
                )
                
                // Draw arrow head
                val arrowSize = 30f
                val dx = arrowEndX - baseX
                val dy = arrowEndY - baseY
                val length = kotlin.math.sqrt(dx * dx + dy * dy)
                if (length > 0) {
                    val perpX = -dy / length * arrowSize
                    val perpY = dx / length * arrowSize
                    
                    // Triangle arrow head as a path
                    val arrowPath = Path().apply {
                        moveTo(arrowEndX, arrowEndY)
                        lineTo(arrowEndX - dx / length * arrowSize + perpX, arrowEndY - dy / length * arrowSize + perpY)
                        lineTo(arrowEndX - dx / length * arrowSize - perpX, arrowEndY - dy / length * arrowSize - perpY)
                        close()
                    }
                    
                    drawPath(
                        path = arrowPath,
                        color = Color.Black
                    )
                }
                
                // Draw center circle for reference
                drawCircle(color = Color.Black.copy(alpha = 0.8f), radius = 10f, center = Offset(baseX, baseY))
            }
        }
    }
}

// --- Internals ---

class GravityRegistry {
    val items = mutableStateMapOf<String, ProbedItem>()
}

data class ProbedItem(
    val id: String,
    val label: String,
    val color: Color,
    val rect: Rect  // in root coords (px)
)

private data class Body(
    var x: Float, var y: Float, var vx: Float = 0f, var vy: Float = 0f,
    val r: Float, val mass: Float, val color: Color, val label: String
)

private fun ProbedItem.toBody(): Body {
    val w = rect.width
    val h = rect.height
    val r = (minOf(w, h) / 2f).coerceAtLeast(8f)
    val cx = rect.left + w / 2f
    val cy = rect.top + h / 2f
    val mass = (w * h).coerceAtLeast(1f)
    // Optional: tiny jitter to avoid perfect overlaps at start
    val jx = (Math.random().toFloat() - 0.5f) * r * 0.2f
    return Body(x = cx + jx, y = cy, r = r, mass = mass, color = color, label = label)
}

