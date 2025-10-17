package com.kreggscode.enstienquotes.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import com.kreggscode.enstienquotes.model.Category
import com.kreggscode.enstienquotes.ui.components.GlassCard
import com.kreggscode.enstienquotes.ui.components.MorphismCard
import com.kreggscode.enstienquotes.ui.components.GlassmorphicHeader
import com.kreggscode.enstienquotes.viewmodel.QuoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: QuoteViewModel,
    onCategoryClick: (String) -> Unit,
    onQuoteClick: (Int) -> Unit,
    onAboutClick: () -> Unit,
    onWorksClick: () -> Unit
) {
    val categories by viewModel.categories.collectAsState()
    val allQuotes by viewModel.allQuotes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    // Edge-to-edge design without Scaffold for full control
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.systemBars),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    top = 20.dp,
                    bottom = 100.dp  // Navigation bar space
                ),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Glassmorphic animated header
                item(
                    span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }
                ) {
                    GlassmorphicHeader(
                        title = "Einstein AI",
                        subtitle = "INSIGHTS & WISDOM"
                    )
                }
                // Stunning feature cards
                item {
                    EnhancedFeatureCard(
                        title = "About Einstein",
                        icon = Icons.Default.Person,
                        gradient = listOf(Color(0xFF60A5FA), Color(0xFF7C3AED)),
                        onClick = onAboutClick
                    )
                }
                
                item {
                    EnhancedFeatureCard(
                        title = "Works & Papers",
                        icon = Icons.Default.MenuBook,
                        gradient = listOf(Color(0xFFA78BFA), Color(0xFFF472B6)),
                        onClick = onWorksClick
                    )
                }
                
                // Categories with enhanced design
                items(categories) { category ->
                    EnhancedCategoryCard(
                        category = category,
                        onClick = { onCategoryClick(category.name) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EnhancedFeatureCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    gradient: List<Color>,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = gradient[0].copy(alpha = 0.3f)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = gradient + gradient[1].copy(alpha = 0.8f)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EnhancedCategoryCard(
    category: Category,
    onClick: () -> Unit
) {
    val gradientColors = getEinsteinCategoryGradient(category.name)
    
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = gradientColors[0].copy(alpha = 0.2f)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = gradientColors,
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(1000f, 1000f)
                    )
                )
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = category.icon,
                    fontSize = 56.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
                
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontSize = 22.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${category.quoteCount} insights",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.95f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun getEinsteinCategoryGradient(categoryName: String): List<Color> {
    return when (categoryName.lowercase()) {
        // Einstein-themed cosmic gradients
        "wisdom" -> listOf(Color(0xFF6366F1), Color(0xFF8B5CF6))  // Indigo-Violet
        "freedom" -> listOf(Color(0xFFEC4899), Color(0xFFF43F5E))  // Pink-Rose
        "politics" -> listOf(Color(0xFF7C3AED), Color(0xFF5B21B6))  // Purple
        "money" -> listOf(Color(0xFFF59E0B), Color(0xFFEAB308))  // Amber-Gold
        "knowledge" -> listOf(Color(0xFF10B981), Color(0xFF059669))  // Emerald
        "friendship" -> listOf(Color(0xFFF97316), Color(0xFFEA580C))  // Orange
        "courage" -> listOf(Color(0xFFEF4444), Color(0xFFDC2626))  // Red
        "freedom of speech" -> listOf(Color(0xFF06B6D4), Color(0xFF0891B2))  // Cyan
        "war & peace" -> listOf(Color(0xFF64748B), Color(0xFF475569))  // Slate
        "history" -> listOf(Color(0xFF60A5FA), Color(0xFF3B82F6))  // Blue
        "education" -> listOf(Color(0xFFA78BFA), Color(0xFF9333EA))  // Purple-Fuchsia
        "justice" -> listOf(Color(0xFF0EA5E9), Color(0xFF0284C7))  // Sky
        "religion" -> listOf(Color(0xFFF472B6), Color(0xFFE879F9))  // Pink-Fuchsia
        "time" -> listOf(Color(0xFF14B8A6), Color(0xFF0D9488))  // Teal
        "truth" -> listOf(Color(0xFFFBBF24), Color(0xFFF59E0B))  // Yellow-Amber
        "happiness" -> listOf(Color(0xFFFB923C), Color(0xFFF97316))  // Orange-Red
        "philosophy" -> listOf(Color(0xFF3B82F6), Color(0xFF2563EB))  // Blue
        "death" -> listOf(Color(0xFF71717A), Color(0xFF52525B))  // Zinc
        "tolerance" -> listOf(Color(0xFF22D3EE), Color(0xFF06B6D4))  // Cyan
        "love" -> listOf(Color(0xFFFDA4AF), Color(0xFFFB7185))  // Rose
        "work" -> listOf(Color(0xFFFCD34D), Color(0xFFFBBF24))  // Yellow
        "science" -> listOf(Color(0xFF38BDF8), Color(0xFF0EA5E9))  // Sky-Blue
        "government" -> listOf(Color(0xFFC084FC), Color(0xFFA855F7))  // Purple
        "morality" -> listOf(Color(0xFFFDE047), Color(0xFFFACC15))  // Yellow
        "society" -> listOf(Color(0xFFFF6B6B), Color(0xFFEE5A6F))  // Coral
        "women" -> listOf(Color(0xFFF0ABFC), Color(0xFFE879F9))  // Fuchsia
        "men" -> listOf(Color(0xFF0EA5E9), Color(0xFF0284C7))  // Sky
        "humanity" -> listOf(Color(0xFFFDA4AF), Color(0xFFFB7185))  // Rose
        "success" -> listOf(Color(0xFFA78BFA), Color(0xFF8B5CF6))  // Violet
        "art" -> listOf(Color(0xFF9333EA), Color(0xFF7C3AED))  // Fuchsia-Purple
        else -> listOf(Color(0xFF60A5FA), Color(0xFF7C3AED))  // Default cosmic blue-purple
    }
}
