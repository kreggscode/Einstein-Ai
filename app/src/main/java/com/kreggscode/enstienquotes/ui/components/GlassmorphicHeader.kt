package com.kreggscode.enstienquotes.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale

@Composable
fun GlassmorphicHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    // Animated scale for pulsing effect
    val infiniteTransition = rememberInfiniteTransition(label = "header_animation")
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Glassmorphic background with blur effect
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.08f * alpha),
                            Color.White.copy(alpha = 0.04f * alpha),
                            Color(0xFF7C3AED).copy(alpha = 0.12f * alpha)
                        )
                    )
                )
                .scale(scale)
        ) {
            // Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp, horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Animated brain emoji or icon
                Text(
                    text = "🧠",
                    fontSize = 48.sp,
                    modifier = Modifier
                        .scale(scale)
                        .alpha(alpha)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    fontSize = 42.sp,
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Subtitle
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White.copy(alpha = 0.85f),
                    fontWeight = FontWeight.Light,
                    letterSpacing = 3.sp,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
            }
        }
        
        // Glow effect overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF60A5FA).copy(alpha = 0.15f * alpha),
                            Color.Transparent
                        )
                    )
                )
                .blur(20.dp)
        )
    }
}
