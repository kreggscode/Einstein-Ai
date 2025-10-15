package com.kreggscode.einsteinquotes.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

@Composable
fun FloatingBottomBar(
    items: List<BottomNavItem>,
    currentRoute: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = MaterialTheme.colorScheme.background.value == Color(0xFF0A0E1A).value
    
    // Use a transparent background container
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .height(76.dp)
                    .fillMaxWidth(0.92f)
                    .shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(32.dp),
                        ambientColor = if (isDark) Color(0xFF60A5FA).copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.15f),
                        spotColor = if (isDark) Color(0xFF7C3AED).copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.15f)
                    ),
                shape = RoundedCornerShape(32.dp),
                color = if (isDark) {
                    Color(0xFF1A1F2E).copy(alpha = 0.92f)
                } else {
                    Color.White.copy(alpha = 0.95f)
                },
                tonalElevation = 12.dp
            ) {
                // Glassmorphism background effect
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                colors = if (isDark) {
                                    listOf(
                                        Color(0xFF1E2433).copy(alpha = 0.6f),
                                        Color(0xFF2D3548).copy(alpha = 0.4f)
                                    )
                                } else {
                                    listOf(
                                        Color.White.copy(alpha = 0.8f),
                                        Color(0xFFF0F4F8).copy(alpha = 0.6f)
                                    )
                                }
                            )
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
                            BottomNavItemView(
                                item = item,
                                isSelected = currentRoute == item.route,
                                onClick = { onItemClick(item.route) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomNavItemView(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )
    
    val iconColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        },
        animationSpec = tween(300),
        label = "iconColor"
    )
    
    Column(
        modifier = Modifier
            .width(64.dp)
            .fillMaxHeight()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }
        
        if (isSelected) {
            Text(
                text = item.label,
                style = MaterialTheme.typography.labelSmall,
                color = iconColor,
                fontSize = 10.sp,
                maxLines = 1
            )
        }
    }
}
