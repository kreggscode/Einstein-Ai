package com.kreggscode.einsteinquotes.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.kreggscode.einsteinquotes.model.Quote
import com.kreggscode.einsteinquotes.ui.components.*
import com.kreggscode.einsteinquotes.ui.theme.*
import com.kreggscode.einsteinquotes.viewmodel.QuoteViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumHomeScreen(
    viewModel: QuoteViewModel,
    onQuoteClick: (Quote) -> Unit,
    onCategoryClick: (String) -> Unit,
    onWorkClick: () -> Unit,
    onAboutClick: () -> Unit,
    onQuoteOfDayClick: () -> Unit = {},
    onAffirmationsClick: () -> Unit = {},
    onChatClick: () -> Unit = {},
    onFavoritesClick: () -> Unit = {}
) {
    val allQuotes by viewModel.allQuotes.collectAsState()
    val favoriteQuotes by viewModel.favoriteQuotes.collectAsState()
    val scope = rememberCoroutineScope()
    
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var quoteOfTheDay by remember { mutableStateOf<Quote?>(null) }
    var showQuoteOfDay by remember { mutableStateOf(false) }
    
    // Get quote of the day
    LaunchedEffect(allQuotes) {
        if (allQuotes.isNotEmpty()) {
            val dayOfYear = LocalDateTime.now().dayOfYear
            val index = dayOfYear % allQuotes.size
            quoteOfTheDay = allQuotes[index]
            showQuoteOfDay = true
        }
    }
    
    val categories = remember(allQuotes) {
        allQuotes.map { it.Category }.distinct()
    }
    
    val filteredQuotes = remember(allQuotes, selectedCategory) {
        if (selectedCategory == null) allQuotes
        else allQuotes.filter { it.Category == selectedCategory }
    }
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars),
            contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 100.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Premium Header with animation
            item {
                AnimatedHeader()
            }
            
            // Quote of the Day
            if (quoteOfTheDay != null && showQuoteOfDay) {
                item {
                    QuoteOfTheDayCard(
                        quote = quoteOfTheDay!!,
                        onClick = { onQuoteClick(quoteOfTheDay!!) },
                        onDismiss = { showQuoteOfDay = false }
                    )
                }
            }
            
            // Quick Actions
            item {
                QuickActionsSection(
                    onAboutClick = onAboutClick,
                    onWorksClick = onWorkClick,
                    favoriteCount = favoriteQuotes.size,
                    onQuoteOfDayClick = onQuoteOfDayClick,
                    onAffirmationsClick = onAffirmationsClick,
                    onChatClick = onChatClick,
                    onFavoritesClick = onFavoritesClick
                )
            }
            
            // Categories with horizontal scroll
            if (categories.isNotEmpty()) {
                item {
                    Text(
                        "✨ Explore Categories",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            CategoryChip(
                                category = "All",
                                isSelected = selectedCategory == null,
                                onClick = { selectedCategory = null },
                                color = PremiumColors.CyberBlue
                            )
                        }
                        items(categories) { category ->
                            CategoryChip(
                                category = category,
                                isSelected = selectedCategory == category,
                                onClick = { selectedCategory = category },
                                color = when(categories.indexOf(category) % 4) {
                                    0 -> PremiumColors.ElectricPurple
                                    1 -> PremiumColors.NeonPink
                                    2 -> PremiumColors.QuantumTeal
                                    else -> PremiumColors.CosmicIndigo
                                }
                            )
                        }
                    }
                }
            }
            
            // Section title with animation
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "💡 Einstein's Wisdom",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    
                    Text(
                        text = "${filteredQuotes.size} quotes",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
            
            // Quotes Grid with staggered animation
            if (filteredQuotes.isEmpty()) {
                item {
                    EmptyStateCard()
                }
            } else {
                itemsIndexed(
                    items = filteredQuotes,
                    key = { _, quote -> quote.id }
                ) { index, quote ->
                    AnimatedQuoteCard(
                        quote = quote,
                        index = index,
                        onClick = { onQuoteClick(quote) },
                        onFavoriteClick = { scope.launch { viewModel.toggleFavorite(quote) } }
                    )
                }
            }
            
            // Bottom spacing for navigation
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun AnimatedHeader() {
    val infiniteTransition = rememberInfiniteTransition(label = "header")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        glowColor = PremiumColors.ElectricPurple,
        animateGlow = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🧠",
                fontSize = 60.sp,
                modifier = Modifier.scale(scale)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Einstein",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Black,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.graphicsLayer {
                    shadowElevation = 8.dp.toPx()
                }
            )
            
            Text(
                text = "Insights & Wisdom",
                style = MaterialTheme.typography.titleMedium,
                color = PremiumColors.CyberBlue,
                fontWeight = FontWeight.Medium,
                letterSpacing = 2.sp
            )
        }
    }
}

@Composable
fun QuoteOfTheDayCard(
    quote: Quote,
    onClick: () -> Unit,
    onDismiss: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        visible = true
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        PremiumGlassCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick,
            gradientColors = PremiumColors.AuroraGradient
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Dismiss button
                IconButton(
                    onClick = {
                        visible = false
                        onDismiss()
                    },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Dismiss",
                        tint = Color.White.copy(alpha = 0.7f)
                    )
                }
                
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Quote of the Day",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD700)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "\"${quote.Quote}\"",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        lineHeight = 24.sp
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = quote.Work,
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White.copy(alpha = 0.7f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = quote.Year.toString(),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionsSection(
    onAboutClick: () -> Unit,
    onWorksClick: () -> Unit,
    favoriteCount: Int,
    onQuoteOfDayClick: () -> Unit,
    onAffirmationsClick: () -> Unit,
    onChatClick: () -> Unit,
    onFavoritesClick: () -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // First row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Person,
                title = "About\nEinstein",
                gradient = listOf(
                    Color(0xFF667EEA),
                    Color(0xFF764BA2)
                ),
                onClick = onAboutClick
            )
            
            QuickActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.MenuBook,
                title = "Works &\nPapers",
                gradient = listOf(
                    Color(0xFFFA709A),
                    Color(0xFFFEE140)
                ),
                onClick = onWorksClick
            )
            
            QuickActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Favorite,
                title = "$favoriteCount\nFavorites",
                gradient = listOf(
                    Color(0xFF43E97B),
                    Color(0xFF38F9D7)
                ),
                onClick = onFavoritesClick
            )
        }
        
        // Second row with new features
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.AutoAwesome,
                title = "Quote of\nthe Day",
                gradient = listOf(
                    PremiumColors.CyberBlue,
                    PremiumColors.ElectricPurple
                ),
                onClick = onQuoteOfDayClick
            )
            
            QuickActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.SelfImprovement,
                title = "Daily\nAffirmations",
                gradient = listOf(
                    PremiumColors.NeonPink,
                    PremiumColors.NebulaMagenta
                ),
                onClick = onAffirmationsClick
            )
            
            QuickActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Chat,
                title = "AI\nInsights",
                gradient = listOf(
                    PremiumColors.PlasmaOrange,
                    PremiumColors.QuantumTeal
                ),
                onClick = onChatClick
            )
        }
    }
}

@Composable
fun QuickActionCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    gradient: List<Color>,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        label = "scale"
    )
    
    Box(
        modifier = modifier
            .scale(scale)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = gradient
                )
            )
            .clickable {
                isPressed = true
                onClick()
                isPressed = false
            }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
fun CategoryChip(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    color: Color
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) color else color.copy(alpha = 0.2f),
        label = "bgColor"
    )
    
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        enabled = true,
        label = {
            Text(
                category,
                color = if (isSelected) Color.White else color,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = backgroundColor,
            containerColor = backgroundColor
        ),
        border = FilterChipDefaults.filterChipBorder(
            borderColor = color.copy(alpha = 0.3f),
            selectedBorderColor = Color.Transparent,
            enabled = true,
            selected = isSelected
        )
    )
}

@Composable
fun AnimatedQuoteCard(
    quote: Quote,
    index: Int,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(index) {
        delay(index * 50L)
        visible = true
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInHorizontally(
            initialOffsetX = { if (index % 2 == 0) -it else it }
        )
    ) {
        GlassmorphicCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick,
            glowColor = when(index % 4) {
                0 -> PremiumColors.ElectricPurple
                1 -> PremiumColors.NeonPink
                2 -> PremiumColors.CyberBlue
                else -> PremiumColors.QuantumTeal
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "\"${quote.Quote}\"",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        modifier = Modifier.weight(1f),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    IconButton(
                        onClick = onFavoriteClick,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (quote.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (quote.isFavorite) Color(0xFFE91E63) else Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = PremiumColors.CyberBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = quote.Work,
                            style = MaterialTheme.typography.labelSmall,
                            color = PremiumColors.CyberBlue,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    
                    Text(
                        text = quote.Category,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier
                            .background(
                                Color.White.copy(alpha = 0.1f),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyStateCard() {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        animateGlow = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "🔍",
                fontSize = 48.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "No quotes found",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                "Try selecting a different category",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}
