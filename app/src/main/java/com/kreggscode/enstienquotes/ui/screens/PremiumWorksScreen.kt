package com.kreggscode.enstienquotes.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.kreggscode.enstienquotes.ui.components.*
import com.kreggscode.enstienquotes.ui.theme.*
import com.kreggscode.enstienquotes.viewmodel.QuoteViewModel
import kotlinx.coroutines.delay

data class EinsteinWork(
    val title: String,
    val year: String,
    val description: String,
    val category: String,
    val icon: String,
    val quoteCount: Int,
    val gradientColors: List<Color>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumWorksScreen(
    viewModel: QuoteViewModel,
    onWorkClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val allQuotes by viewModel.allQuotes.collectAsState()
    val works by viewModel.works.collectAsState()
    
    // Group quotes by work
    val workGroups = remember(allQuotes) {
        allQuotes.groupBy { it.Work }
            .map { (work, quotes) ->
                EinsteinWork(
                    title = work,
                    year = quotes.firstOrNull()?.Year ?: "Unknown",
                    description = getWorkDescription(work),
                    category = getWorkCategory(work),
                    icon = getWorkIcon(work),
                    quoteCount = quotes.size,
                    gradientColors = getWorkGradient(work)
                )
            }
            .sortedByDescending { it.quoteCount }
    }
    
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Philosophy", "Science", "Letters", "Essays")
    
    val filteredWorks = remember(workGroups, selectedCategory) {
        if (selectedCategory == "All") workGroups
        else workGroups.filter { it.category == selectedCategory }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        PremiumColors.DeepSpace,
                        PremiumColors.MidnightBlue,
                        PremiumColors.DeepSpace
                    )
                )
            )
    ) {
        // Animated book particles
        BookParticles(modifier = Modifier.fillMaxSize())
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
        ) {
            // Premium Header
            PremiumWorksHeader(
                onBackClick = onBackClick,
                totalWorks = workGroups.size
            )
            
            // Category Filter
            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = {
                            Text(
                                category,
                                color = if (selectedCategory == category) Color.White 
                                       else PremiumColors.QuantumTeal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PremiumColors.QuantumTeal,
                            containerColor = PremiumColors.QuantumTeal.copy(alpha = 0.2f)
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = PremiumColors.QuantumTeal.copy(alpha = 0.3f),
                            selectedBorderColor = Color.Transparent,
                            enabled = true,
                            selected = selectedCategory == category
                        )
                    )
                }
            }
            
            // Works List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp, 8.dp, 16.dp, 100.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Featured Work
                if (filteredWorks.isNotEmpty()) {
                    item {
                        FeaturedWorkCard(
                            work = filteredWorks.first(),
                            onClick = { onWorkClick(filteredWorks.first().title) }
                        )
                    }
                }
                
                // Other Works
                itemsIndexed(
                    items = if (filteredWorks.isNotEmpty()) filteredWorks.drop(1) else emptyList()
                ) { index, work ->
                    AnimatedWorkCard(
                        work = work,
                        index = index,
                        onClick = { onWorkClick(work.title) }
                    )
                }
                
                // Statistics
                item {
                    WorksStatisticsCard(
                        totalWorks = workGroups.size,
                        totalQuotes = allQuotes.size
                    )
                }
            }
        }
    }
}

@Composable
fun PremiumWorksHeader(
    onBackClick: () -> Unit,
    totalWorks: Int
) {
    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        glowColor = PremiumColors.QuantumTeal,
        animateGlow = true
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.1f))
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "📚 Works & Papers",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    "$totalWorks works collected",
                    style = MaterialTheme.typography.bodyMedium,
                    color = PremiumColors.QuantumTeal
                )
            }
        }
    }
}

@Composable
fun FeaturedWorkCard(
    work: EinsteinWork,
    onClick: () -> Unit
) {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        glowColor = work.gradientColors.first(),
        animateGlow = true
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = work.gradientColors.map { it.copy(alpha = 0.3f) }
                    )
                )
        ) {
            // Featured badge
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .background(
                        Color(0xFFFFD700),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    "FEATURED",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        work.icon,
                        fontSize = 48.sp
                    )
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            work.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        Text(
                            work.year,
                            style = MaterialTheme.typography.bodyMedium,
                            color = work.gradientColors.first()
                        )
                    }
                }
                
                Text(
                    work.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.FormatQuote,
                            contentDescription = null,
                            tint = work.gradientColors.first(),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "${work.quoteCount} quotes",
                            style = MaterialTheme.typography.labelMedium,
                            color = work.gradientColors.first()
                        )
                    }
                    
                    Text(
                        work.category,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier
                            .background(
                                Color.White.copy(alpha = 0.1f),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedWorkCard(
    work: EinsteinWork,
    index: Int,
    onClick: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(index) {
        delay(index * 50L)
        visible = true
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally { if (index % 2 == 0) -it else it } + fadeIn()
    ) {
        GlassmorphicCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick,
            glowColor = work.gradientColors.first()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon with gradient background
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = work.gradientColors
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        work.icon,
                        fontSize = 28.sp
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        work.title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Text(
                        work.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            work.year,
                            style = MaterialTheme.typography.labelSmall,
                            color = work.gradientColors.first()
                        )
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Text(
                            "• ${work.quoteCount} quotes",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                }
                
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.3f)
                )
            }
        }
    }
}

@Composable
fun WorksStatisticsCard(
    totalWorks: Int,
    totalQuotes: Int
) {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        glowColor = PremiumColors.CosmicIndigo
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "📊 Collection Statistics",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatisticItem(
                    value = totalWorks.toString(),
                    label = "Total Works",
                    icon = "📚"
                )
                
                StatisticItem(
                    value = totalQuotes.toString(),
                    label = "Total Quotes",
                    icon = "💡"
                )
                
                StatisticItem(
                    value = (totalQuotes / maxOf(totalWorks, 1)).toString(),
                    label = "Avg per Work",
                    icon = "📈"
                )
            }
        }
    }
}

@Composable
fun StatisticItem(
    value: String,
    label: String,
    icon: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(icon, fontSize = 24.sp)
        Text(
            value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = PremiumColors.CosmicIndigo
        )
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun BookParticles(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "books")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset"
    )
    
    Canvas(modifier = modifier) {
        val books = listOf("📖", "📚", "📘", "📙", "📗")
        repeat(10) { index ->
            val x = (index * 200f) % size.width
            val y = (offset * size.height + index * 150f) % size.height
            
            drawCircle(
                color = PremiumColors.QuantumTeal.copy(alpha = 0.1f),
                radius = 30f,
                center = Offset(x, y)
            )
        }
    }
}

// Helper functions
fun getWorkDescription(work: String): String {
    return when {
        work.contains("Relativity", ignoreCase = true) -> "Revolutionary theory of space and time"
        work.contains("Letter", ignoreCase = true) -> "Personal correspondence revealing insights"
        work.contains("Essay", ignoreCase = true) -> "Philosophical and scientific reflections"
        work.contains("Physics", ignoreCase = true) -> "Groundbreaking physics discoveries"
        else -> "Profound wisdom and scientific insights"
    }
}

fun getWorkCategory(work: String): String {
    return when {
        work.contains("Letter", ignoreCase = true) -> "Letters"
        work.contains("Essay", ignoreCase = true) -> "Essays"
        work.contains("Physics", ignoreCase = true) || work.contains("Relativity", ignoreCase = true) -> "Science"
        else -> "Philosophy"
    }
}

fun getWorkIcon(work: String): String {
    return when {
        work.contains("Letter", ignoreCase = true) -> "✉️"
        work.contains("Essay", ignoreCase = true) -> "📝"
        work.contains("Physics", ignoreCase = true) -> "⚛️"
        work.contains("Relativity", ignoreCase = true) -> "🌌"
        else -> "📚"
    }
}

fun getWorkGradient(work: String): List<Color> {
    return when {
        work.contains("Letter", ignoreCase = true) -> listOf(PremiumColors.NeonPink, PremiumColors.ElectricPurple)
        work.contains("Essay", ignoreCase = true) -> listOf(PremiumColors.CyberBlue, PremiumColors.QuantumTeal)
        work.contains("Physics", ignoreCase = true) -> listOf(PremiumColors.CosmicIndigo, PremiumColors.ElectricPurple)
        work.contains("Relativity", ignoreCase = true) -> PremiumColors.GalaxyGradient
        else -> listOf(PremiumColors.PlasmaOrange, PremiumColors.NeonPink)
    }
}
