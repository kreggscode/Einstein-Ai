package com.kreggscode.enstienquotes.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.staggeredgrid.*
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
import com.kreggscode.enstienquotes.model.Quote
import com.kreggscode.enstienquotes.ui.components.*
import com.kreggscode.enstienquotes.ui.theme.*
import com.kreggscode.enstienquotes.viewmodel.QuoteViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PremiumFavoritesScreen(
    viewModel: QuoteViewModel,
    onQuoteClick: (Int) -> Unit
) {
    val favoriteQuotes by viewModel.favoriteQuotes.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    
    var selectedFilter by remember { mutableStateOf("All") }
    var sortBy by remember { mutableStateOf("Date Added") }
    var viewType by remember { mutableStateOf("Grid") } // Grid or List
    
    val categories = remember(favoriteQuotes) {
        listOf("All") + favoriteQuotes.map { it.Category }.distinct()
    }
    
    val filteredQuotes = remember(favoriteQuotes, selectedFilter) {
        if (selectedFilter == "All") favoriteQuotes
        else favoriteQuotes.filter { it.Category == selectedFilter }
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
        // Animated hearts background
        HeartParticles(modifier = Modifier.fillMaxSize())
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
        ) {
            // Premium Header
            PremiumFavoritesHeader(
                favoriteCount = favoriteQuotes.size,
                viewType = viewType,
                onViewTypeChange = { viewType = it }
            )
            
            if (favoriteQuotes.isEmpty()) {
                // Empty state
                EmptyFavoritesState()
            } else {
                // Filter chips
                LazyRow(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        FilterChip(
                            selected = selectedFilter == category,
                            onClick = { selectedFilter = category },
                            label = {
                                Text(
                                    category,
                                    color = if (selectedFilter == category) Color.White 
                                           else PremiumColors.NeonPink
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PremiumColors.NeonPink,
                                containerColor = PremiumColors.NeonPink.copy(alpha = 0.2f)
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                borderColor = PremiumColors.NeonPink.copy(alpha = 0.3f),
                                selectedBorderColor = Color.Transparent,
                                enabled = true,
                                selected = selectedFilter == category
                            )
                        )
                    }
                }
                
                // Quotes display
                if (viewType == "Grid") {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(bottom = 100.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalItemSpacing = 12.dp
                    ) {
                        itemsIndexed(filteredQuotes) { index, quote ->
                            FavoriteQuoteGridCard(
                                quote = quote,
                                index = index,
                                onClick = { onQuoteClick(quote.id) },
                                onUnfavorite = {
                                    scope.launch {
                                        viewModel.toggleFavorite(quote)
                                        Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                onShare = {
                                    val shareIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, "\"${quote.Quote}\"\n\n- Einstein")
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Share Quote"))
                                }
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(bottom = 100.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(filteredQuotes) { index, quote ->
                            FavoriteQuoteListCard(
                                quote = quote,
                                index = index,
                                onClick = { onQuoteClick(quote.id) },
                                onUnfavorite = {
                                    scope.launch {
                                        viewModel.toggleFavorite(quote)
                                    }
                                },
                                onCopy = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("quote", quote.Quote)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "Quote copied!", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PremiumFavoritesHeader(
    favoriteCount: Int,
    viewType: String,
    onViewTypeChange: (String) -> Unit
) {
    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        glowColor = PremiumColors.NeonPink,
        animateGlow = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "💖 My Favorites",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        "$favoriteCount quotes saved",
                        style = MaterialTheme.typography.bodyMedium,
                        color = PremiumColors.NeonPink
                    )
                }
                
                // View toggle
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { onViewTypeChange("Grid") },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (viewType == "Grid") PremiumColors.NeonPink
                                else Color.White.copy(alpha = 0.1f)
                            )
                    ) {
                        Icon(
                            Icons.Default.GridView,
                            contentDescription = "Grid View",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    
                    IconButton(
                        onClick = { onViewTypeChange("List") },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (viewType == "List") PremiumColors.NeonPink
                                else Color.White.copy(alpha = 0.1f)
                            )
                    ) {
                        Icon(
                            Icons.Default.ViewList,
                            contentDescription = "List View",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteQuoteGridCard(
    quote: Quote,
    index: Int,
    onClick: () -> Unit,
    onUnfavorite: () -> Unit,
    onShare: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(index) {
        kotlinx.coroutines.delay(index * 50L)
        visible = true
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn() + fadeIn()
    ) {
        GlassmorphicCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick,
            glowColor = PremiumColors.NeonPink,
            cornerRadius = 16.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Quote text
                Text(
                    text = "\"${quote.Quote}\"",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Category badge
                Text(
                    text = quote.Category,
                    style = MaterialTheme.typography.labelSmall,
                    color = PremiumColors.NeonPink,
                    modifier = Modifier
                        .background(
                            PremiumColors.NeonPink.copy(alpha = 0.2f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(
                        onClick = onUnfavorite,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = "Unfavorite",
                            tint = PremiumColors.NeonPink,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    
                    IconButton(
                        onClick = onShare,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteQuoteListCard(
    quote: Quote,
    index: Int,
    onClick: () -> Unit,
    onUnfavorite: () -> Unit,
    onCopy: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(index) {
        kotlinx.coroutines.delay(index * 50L)
        visible = true
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally { if (index % 2 == 0) -it else it } + fadeIn()
    ) {
        GlassmorphicCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick,
            glowColor = PremiumColors.NeonPink
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Heart icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    PremiumColors.NeonPink.copy(alpha = 0.3f),
                                    PremiumColors.NeonPink.copy(alpha = 0.1f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("💖", fontSize = 20.sp)
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // Content
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "\"${quote.Quote}\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = quote.Work,
                            style = MaterialTheme.typography.labelSmall,
                            color = PremiumColors.NeonPink,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        
                        Text(
                            text = quote.Category,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Actions
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = onCopy,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                Icons.Default.ContentCopy,
                                contentDescription = "Copy",
                                tint = Color.White.copy(alpha = 0.6f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        
                        IconButton(
                            onClick = onUnfavorite,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                Icons.Default.HeartBroken,
                                contentDescription = "Remove",
                                tint = PremiumColors.NeonPink,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyFavoritesState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        GlassmorphicCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            glowColor = PremiumColors.NeonPink,
            animateGlow = true
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "💔",
                    fontSize = 64.sp
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    "No favorites yet",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    "Tap the heart icon on quotes to save them here",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun HeartParticles(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "hearts")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset"
    )
    
    val hearts = remember {
        List(15) {
            HeartParticle(
                x = kotlin.random.Random.nextFloat() * 1000f,
                y = kotlin.random.Random.nextFloat() * 1000f,
                size = kotlin.random.Random.nextFloat() * 20f + 10f,
                speed = kotlin.random.Random.nextFloat() * 0.5f + 0.5f
            )
        }
    }
    
    Canvas(modifier = modifier) {
        
        hearts.forEach { heart ->
            val newY = (heart.y - heart.speed * offset * 500f) % size.height
            val adjustedY = if (newY < 0) size.height + newY else newY
            
            drawCircle(
                color = PremiumColors.NeonPink.copy(alpha = 0.1f),
                radius = heart.size / 2,
                center = Offset(heart.x, adjustedY),
                blendMode = BlendMode.Plus
            )
        }
    }
}

private data class HeartParticle(
    val x: Float,
    val y: Float,
    val size: Float,
    val speed: Float
)
