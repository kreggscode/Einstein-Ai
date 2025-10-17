package com.kreggscode.enstienquotes.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.core.splashscreen.SplashScreen
import com.kreggscode.enstienquotes.ui.theme.PremiumColors
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.math.*

@Composable
fun AnimatedSplashScreen(
    onSplashComplete: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition(label = "splash")
    
    // Animated values
    val formulaScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "formulaScale"
    )
    
    val textAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(1200, delayMillis = 400),
        label = "textAlpha"
    )
    
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowPulse"
    )
    
    val particleOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particleOffset"
    )
    
    val orbitRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbitRotation"
    )
    
    LaunchedEffect(Unit) {
        startAnimation = true
        delay(3000) // Show splash for 3 seconds
        onSplashComplete()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0A0E27),
                        Color(0xFF1A1B3D),
                        Color(0xFF0A0E27)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Cosmic particle field
        CosmicParticles(
            modifier = Modifier.fillMaxSize(),
            particleOffset = particleOffset
        )
        
        // Orbiting energy rings
        OrbitingRings(
            modifier = Modifier.size(400.dp),
            rotation = orbitRotation,
            scale = formulaScale
        )
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            // E=mc² Formula with glow
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .scale(formulaScale)
            ) {
                // Glow effect
                Canvas(modifier = Modifier.size(250.dp)) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                PremiumColors.ElectricPurple.copy(alpha = glowPulse * 0.4f),
                                Color.Transparent
                            )
                        ),
                        radius = size.minDimension / 2
                    )
                }
                
                // Formula
                Text(
                    text = "E=mc²",
                    fontSize = 88.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier
                        .graphicsLayer {
                            shadowElevation = 24.dp.toPx()
                        }
                        .drawWithContent {
                            drawContent()
                            // Outer glow
                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        PremiumColors.CyberBlue.copy(alpha = glowPulse * 0.3f),
                                        Color.Transparent
                                    ),
                                    center = center,
                                    radius = size.minDimension
                                ),
                                radius = size.minDimension,
                                blendMode = BlendMode.Plus
                            )
                        }
                )
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // App name
            Text(
                text = "EINSTEIN",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 12.sp,
                modifier = Modifier
                    .graphicsLayer {
                        alpha = textAlpha
                    }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Tagline with gradient
            Text(
                text = "Wisdom Through Relativity",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .graphicsLayer {
                        alpha = textAlpha
                    }
                    .drawWithContent {
                        drawContent()
                    },
                color = PremiumColors.CyberBlue.copy(alpha = 0.9f)
            )
            
            Spacer(modifier = Modifier.height(60.dp))
            
            // Energy wave loading
            if (startAnimation) {
                EnergyWaveLoading(glowPulse = glowPulse)
            }
        }
        
        // Quote at bottom
        Text(
            text = "\"The most beautiful experience we can have is the mysterious\"",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = textAlpha * 0.6f),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Light,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp, start = 40.dp, end = 40.dp)
        )
    }
}

@Composable
fun CosmicParticles(
    modifier: Modifier = Modifier,
    particleOffset: Float
) {
    val particles = remember {
        List(150) {
            Particle(
                x = Random.nextFloat() * 1000f,
                y = Random.nextFloat() * 2500f,
                radius = Random.nextFloat() * 2.5f + 0.5f,
                speed = Random.nextFloat() * 1.5f + 0.3f
            )
        }
    }
    
    Canvas(modifier = modifier) {
        particles.forEach { particle ->
            val newY = (particle.y - particle.speed * particleOffset * 150f) % size.height
            val adjustedY = if (newY < 0) size.height + newY else newY
            
            // Draw star-like particles
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.9f),
                        PremiumColors.CyberBlue.copy(alpha = 0.3f),
                        Color.Transparent
                    )
                ),
                radius = particle.radius * 2,
                center = Offset(particle.x, adjustedY),
                blendMode = BlendMode.Plus
            )
        }
    }
}

@Composable
fun OrbitingRings(
    modifier: Modifier = Modifier,
    rotation: Float,
    scale: Float
) {
    Canvas(
        modifier = modifier
            .scale(scale)
            .graphicsLayer {
                rotationZ = rotation
            }
    ) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        
        // Draw multiple orbiting rings
        for (i in 1..3) {
            val radius = (size.minDimension / 2) * (0.4f + i * 0.15f)
            val alpha = 0.15f / i
            
            rotate(rotation * (i * 0.5f), pivot = Offset(centerX, centerY)) {
                drawCircle(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            PremiumColors.ElectricPurple.copy(alpha = alpha),
                            PremiumColors.NeonPink.copy(alpha = alpha),
                            PremiumColors.CyberBlue.copy(alpha = alpha),
                            PremiumColors.QuantumTeal.copy(alpha = alpha),
                            PremiumColors.ElectricPurple.copy(alpha = alpha)
                        ),
                        center = Offset(centerX, centerY)
                    ),
                    radius = radius,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
                )
            }
        }
    }
}

@Composable
fun EnergyWaveLoading(glowPulse: Float) {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")
    
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(5) { index ->
            val delay = index * 80
            val scale by infiniteTransition.animateFloat(
                initialValue = 0.5f,
                targetValue = 1.2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(800, delayMillis = delay, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "wave$index"
            )
            
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .scale(scale)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                PremiumColors.CyberBlue.copy(alpha = glowPulse),
                                PremiumColors.ElectricPurple.copy(alpha = glowPulse * 0.5f),
                                Color.Transparent
                            )
                        ),
                        radius = size.minDimension,
                        blendMode = BlendMode.Plus
                    )
                }
            }
        }
    }
}

private data class Particle(
    val x: Float,
    val y: Float,
    val radius: Float,
    val speed: Float
)

private object Random {
    fun nextFloat() = kotlin.random.Random.nextFloat()
}
