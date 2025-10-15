package com.kreggscode.einsteinquotes.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kreggscode.einsteinquotes.ui.theme.glassmorphism

@Composable
fun GlassmorphicCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    glowColor: Color = Color(0xFF7C3AED),
    blur: Dp = 30.dp,
    cornerRadius: Dp = 24.dp,
    glassOpacity: Float = 0.15f,
    borderOpacity: Float = 0.3f,
    elevation: Dp = 0.dp,
    animateGlow: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    
    val glowAlpha = if (animateGlow) {
        infiniteTransition.animateFloat(
            initialValue = 0.3f,
            targetValue = 0.6f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "glowAlpha"
        )
    } else {
        remember { mutableStateOf(0.4f) }
    }
    
    Box(
        modifier = modifier
            .drawBehind {
                // Glow effect behind the card
                if (animateGlow) {
                    drawRoundRect(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                glowColor.copy(alpha = glowAlpha.value * 0.3f),
                                glowColor.copy(alpha = 0f)
                            ),
                            radius = size.width
                        ),
                        cornerRadius = CornerRadius(cornerRadius.toPx())
                    )
                }
            }
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = glassOpacity * 0.5f),
                        Color.White.copy(alpha = glassOpacity * 0.2f)
                    )
                )
            )
            .drawWithContent {
                drawContent()
                // Border gradient
                drawRoundRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = borderOpacity),
                            Color.White.copy(alpha = borderOpacity * 0.5f),
                            Color.White.copy(alpha = borderOpacity * 0.2f)
                        ),
                        start = Offset.Zero,
                        end = Offset(size.width, size.height)
                    ),
                    style = Stroke(width = 1.dp.toPx()),
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
            }
            .then(
                if (onClick != null) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                }
            )
    ) {
        content()
    }
}

@Composable
fun PremiumGlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    gradientColors: List<Color> = listOf(
        Color(0xFF667EEA),
        Color(0xFF764BA2)
    ),
    content: @Composable BoxScope.() -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )
    
    Box(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(28.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = gradientColors.map { it.copy(alpha = 0.1f) }
                )
            )
            .drawWithContent {
                // Backdrop blur effect
                drawContent()
                
                // Glass overlay
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.2f),
                            Color.White.copy(alpha = 0.05f)
                        )
                    )
                )
                
                // Premium border
                drawRoundRect(
                    brush = Brush.linearGradient(
                        colors = gradientColors.map { it.copy(alpha = 0.5f) }
                    ),
                    style = Stroke(width = 2.dp.toPx()),
                    cornerRadius = CornerRadius(28.dp.toPx())
                )
            }
            .then(
                if (onClick != null) {
                    Modifier.clickable {
                        isPressed = true
                        onClick()
                        isPressed = false
                    }
                } else {
                    Modifier
                }
            )
    ) {
        content()
    }
}

@Composable
fun FloatingGlassButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    text: String? = null,
    primaryColor: Color = Color(0xFF7C3AED)
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "buttonScale"
    )
    
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )
    
    Box(
        modifier = modifier
            .scale(scale)
            .drawBehind {
                // Pulsing glow effect
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            primaryColor.copy(alpha = 0.3f * pulseAlpha),
                            primaryColor.copy(alpha = 0f)
                        ),
                        radius = size.width * 0.8f
                    ),
                    radius = size.width * 0.6f
                )
            }
            .clip(RoundedCornerShape(50))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        primaryColor.copy(alpha = 0.9f),
                        primaryColor.copy(alpha = 0.7f)
                    )
                )
            )
            .glassmorphism(
                blur = 20.dp,
                cornerRadius = 50.dp,
                glassColor = Color.White.copy(alpha = 0.2f),
                borderColor = Color.White.copy(alpha = 0.4f)
            )
            .clickable {
                isPressed = true
                onClick()
                isPressed = false
            }
            .padding(horizontal = if (text != null) 12.dp else 12.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            icon()
            if (text != null) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
        }
    }
}
