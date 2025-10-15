package com.kreggscode.einsteinquotes.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kreggscode.einsteinquotes.ui.theme.*

@Composable
fun PremiumFloatingBottomBar(
    items: List<BottomNavItem>,
    currentRoute: String,
    onItemClick: (String) -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "bottomBar")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 24.dp)
    ) {
        // Glow effect behind the bar
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .blur(40.dp)
        ) {
            drawRoundRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        PremiumColors.ElectricPurple.copy(alpha = glowAlpha * 0.5f),
                        PremiumColors.CyberBlue.copy(alpha = glowAlpha * 0.6f),
                        PremiumColors.NeonPink.copy(alpha = glowAlpha * 0.5f)
                    )
                ),
                cornerRadius = CornerRadius(37.5f.dp.toPx())
            )
        }
        
        // Main glassmorphic navigation bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .clip(RoundedCornerShape(37.5.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0x1AFFFFFF),
                            Color(0x0DFFFFFF)
                        )
                    )
                )
                .drawWithContent {
                    // Content
                    drawContent()
                    
                    // Glass border
                    drawRoundRect(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.3f),
                                Color.White.copy(alpha = 0.1f),
                                Color.White.copy(alpha = 0.3f)
                            ),
                            start = Offset.Zero,
                            end = Offset(size.width, 0f)
                        ),
                        style = Stroke(width = 1.5.dp.toPx()),
                        cornerRadius = CornerRadius(37.5.dp.toPx())
                    )
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    PremiumNavItem(
                        item = item,
                        isSelected = currentRoute == item.route,
                        onClick = { onItemClick(item.route) }
                    )
                }
            }
        }
    }
}

@Composable
fun PremiumNavItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.15f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )
    
    val infiniteTransition = rememberInfiniteTransition(label = "navItem")
    val iconRotation by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )
    
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            when (item.label) {
                "Home" -> PremiumColors.ElectricPurple
                "Chat" -> PremiumColors.CyberBlue
                "Favorites" -> PremiumColors.NeonPink
                "Works" -> PremiumColors.QuantumTeal
                else -> PremiumColors.CosmicIndigo
            }
        } else {
            Color.White.copy(alpha = 0.1f)
        },
        animationSpec = tween(300),
        label = "bgColor"
    )
    
    Box(
        modifier = Modifier
            .scale(scale)
            .size(if (isSelected) 65.dp else 60.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            // Animated ring around selected item
            Canvas(
                modifier = Modifier
                    .size(65.dp)
                    .graphicsLayer {
                        rotationZ = iconRotation * 2
                    }
            ) {
                drawCircle(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            Color(backgroundColor.red, backgroundColor.green, backgroundColor.blue, 0.5f),
                            Color.Transparent,
                            Color(backgroundColor.red, backgroundColor.green, backgroundColor.blue, 0.5f)
                        )
                    ),
                    style = Stroke(width = 2.dp.toPx()),
                    radius = size.minDimension / 2
                )
            }
        }
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f),
                modifier = Modifier
                    .size(24.dp)
                    .then(
                        if (isSelected) {
                            Modifier.graphicsLayer {
                                rotationZ = iconRotation
                            }
                        } else Modifier
                    )
            )
            
            AnimatedVisibility(
                visible = isSelected,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Text(
                    text = item.label,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Composable
fun FloatingActionMenu(
    modifier: Modifier = Modifier,
    items: List<Pair<String, ImageVector>>,
    onItemClick: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 45f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "fabRotation"
    )
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        // Menu items
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.End
            ) {
                items.forEach { (label, icon) ->
                    FloatingGlassButton(
                        onClick = {
                            onItemClick(label)
                            isExpanded = false
                        },
                        icon = {
                            Icon(
                                icon,
                                contentDescription = label,
                                tint = Color.White
                            )
                        },
                        text = label,
                        primaryColor = PremiumColors.ElectricPurple
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Main FAB
        FloatingActionButton(
            onClick = { isExpanded = !isExpanded },
            containerColor = PremiumColors.ElectricPurple,
            contentColor = Color.White,
            modifier = Modifier.graphicsLayer {
                rotationZ = rotation
            }
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Menu",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
