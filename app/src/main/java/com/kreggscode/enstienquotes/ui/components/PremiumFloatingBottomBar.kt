package com.kreggscode.enstienquotes.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.kreggscode.enstienquotes.ui.theme.*

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
            .padding(bottom = 28.dp)
            .clip(RoundedCornerShape(37.5.dp))
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
        
        // Main glassmorphic navigation bar with proper clipping
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .clip(RoundedCornerShape(37.5.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0x55FFFFFF),
                            Color(0x40FFFFFF)
                        )
                    )
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.3f),
                            Color.White.copy(alpha = 0.1f),
                            Color.White.copy(alpha = 0.3f)
                        )
                    ),
                    shape = RoundedCornerShape(37.5.dp)
                )
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
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )
    
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            when (item.label) {
                "Home" -> Color(0xFF6366F1)
                "Works" -> Color(0xFFEC4899)
                "Chat" -> Color(0xFF8B5CF6)
                "Profile" -> Color(0xFF10B981)
                else -> Color(0xFF6366F1)
            }
        } else {
            Color.White.copy(alpha = 0.1f)
        },
        animationSpec = tween(200),
        label = "bgColor"
    )
    
    Box(
        modifier = Modifier
            .scale(scale)
            .size(if (isSelected) 56.dp else 54.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        // Removed rotating ring animation for better performance
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
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
