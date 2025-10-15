package com.kreggscode.einsteinquotes.ui.theme

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kreggscode.einsteinquotes.ui.components.GlassmorphicCard
import kotlin.math.cos
import kotlin.math.sin

// Premium Color Palette with Gradients
object PremiumColors {
    val DeepSpace = Color(0xFF0A0E27)
    val MidnightBlue = Color(0xFF1A1F3A)
    val ElectricPurple = Color(0xFF7C3AED)
    val NeonPink = Color(0xFFEC4899)
    val CyberBlue = Color(0xFF3B82F6)
    val QuantumTeal = Color(0xFF14B8A6)
    val CosmicIndigo = Color(0xFF6366F1)
    val PlasmaOrange = Color(0xFFF97316)
    val NebulaMagenta = Color(0xFFA855F7)
    val QuantumGold = Color(0xFFFBBF24)
    
    // Gradient combinations
    val GalaxyGradient = listOf(
        Color(0xFF667EEA),
        Color(0xFF764BA2),
        Color(0xFFF093FB)
    )
    
    val AuroraGradient = listOf(
        Color(0xFF00D2FF),
        Color(0xFF3A7BD5),
        Color(0xFF00D2FF)
    )
    
    val SunsetGradient = listOf(
        Color(0xFFFA709A),
        Color(0xFFFEE140),
        Color(0xFFFA709A)
    )
    
    val NorthernLights = listOf(
        Color(0xFF43E97B),
        Color(0xFF38F9D7),
        Color(0xFF4FACFE)
    )
    
    val CosmicDust = listOf(
        Color(0xFF667EEA).copy(alpha = 0.3f),
        Color(0xFF764BA2).copy(alpha = 0.2f),
        Color(0xFFF093FB).copy(alpha = 0.1f)
    )
}

// Glassmorphism modifier with blur and transparency
fun Modifier.glassmorphism(
    blur: Dp = 25.dp,
    cornerRadius: Dp = 20.dp,
    glassColor: Color = Color.White.copy(alpha = 0.1f),
    borderColor: Color = Color.White.copy(alpha = 0.2f),
    borderWidth: Dp = 1.dp
) = this
    .clip(RoundedCornerShape(cornerRadius))
    .background(
        brush = Brush.verticalGradient(
            colors = listOf(
                glassColor,
                glassColor.copy(alpha = glassColor.alpha * 0.5f)
            )
        )
    )
    .drawBehind {
        drawRoundRect(
            color = borderColor,
            cornerRadius = CornerRadius(cornerRadius.toPx()),
            style = Stroke(width = borderWidth.toPx())
        )
    }

// Animated gradient background
@Composable
fun AnimatedGradientBackground(
    modifier: Modifier = Modifier,
    colors: List<Color> = PremiumColors.GalaxyGradient
) {
    val infiniteTransition = rememberInfiniteTransition(label = "gradient")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "angle"
    )
    
    Box(
        modifier = modifier
            .background(
                brush = Brush.linearGradient(
                    colors = colors,
                    start = Offset(
                        x = cos(Math.toRadians(angle.toDouble())).toFloat() * 1000,
                        y = sin(Math.toRadians(angle.toDouble())).toFloat() * 1000
                    ),
                    end = Offset(
                        x = cos(Math.toRadians(angle.toDouble() + 180)).toFloat() * 1000,
                        y = sin(Math.toRadians(angle.toDouble() + 180)).toFloat() * 1000
                    )
                )
            )
    )
}

// Shimmer effect for loading states
fun Modifier.shimmerEffect(): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer"
    )
    
    background(
        brush = Brush.horizontalGradient(
            colors = listOf(
                Color.White.copy(alpha = alpha),
                Color.White.copy(alpha = alpha * 0.3f),
                Color.White.copy(alpha = alpha)
            )
        )
    )
}

// Pulsating glow effect
fun Modifier.glowEffect(
    color: Color = PremiumColors.ElectricPurple,
    radius: Dp = 20.dp
): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )
    
    this.drawBehind {
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    color.copy(alpha = alpha),
                    color.copy(alpha = 0f)
                ),
                radius = radius.toPx()
            ),
            radius = radius.toPx()
        )
    }
}

// Particle effect background
@Composable
fun ParticleBackground(
    modifier: Modifier = Modifier,
    particleColor: Color = PremiumColors.CyberBlue.copy(alpha = 0.3f)
) {
    val particles = remember { List(50) { Particle() } }
    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    
    particles.forEachIndexed { index, particle ->
        val offsetY by infiniteTransition.animateFloat(
            initialValue = particle.startY,
            targetValue = particle.endY,
            animationSpec = infiniteRepeatable(
                animation = tween(particle.duration, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "particle_$index"
        )
        
        Canvas(modifier = modifier) {
            drawCircle(
                color = particleColor,
                radius = particle.size,
                center = Offset(particle.x, offsetY)
            )
        }
    }
}

data class Particle(
    val x: Float = (0..1000).random().toFloat(),
    val startY: Float = (0..2000).random().toFloat(),
    val endY: Float = -50f,
    val size: Float = (2..8).random().toFloat(),
    val duration: Int = (8000..15000).random()
)

// Neon text effect
fun Modifier.neonText(
    color: Color = PremiumColors.NeonPink
): Modifier = composed {
    this.drawBehind {
        drawIntoCanvas { canvas: androidx.compose.ui.graphics.Canvas ->
            val paint = Paint().apply {
                this.color = color
                style = PaintingStyle.Stroke
                strokeWidth = 2f
            }
            canvas.nativeCanvas.drawText(
                "",
                0f,
                0f,
                paint.asFrameworkPaint().apply {
                    setShadowLayer(
                        20f,
                        0f,
                        0f,
                        color.toArgb()
                    )
                }
            )
        }
    }
}

// AI Insights Button - shared composable
@Composable
fun AIInsightsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = PremiumColors.ElectricPurple,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Get AI Insights & Explanations",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// Key Point Card - shared composable
@Composable
fun KeyPointCard(point: com.kreggscode.einsteinquotes.data.KeyPoint) {
    GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = point.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = PremiumColors.ElectricPurple
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = point.content,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 24.sp
            )
        }
    }
}
