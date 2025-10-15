package com.kreggscode.einsteinquotes.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.*
import com.kreggscode.einsteinquotes.model.Quote
import com.kreggscode.einsteinquotes.ui.components.*
import com.kreggscode.einsteinquotes.ui.theme.*
import com.kreggscode.einsteinquotes.viewmodel.QuoteViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteOfDayScreen(
    viewModel: QuoteViewModel,
    onBackClick: () -> Unit,
    onChatClick: (Quote) -> Unit
) {
    val allQuotes by viewModel.allQuotes.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    // Get today's quote based on day of year
    val todayQuote = remember(allQuotes) {
        if (allQuotes.isNotEmpty()) {
            val dayOfYear = LocalDate.now().dayOfYear
            allQuotes[dayOfYear % allQuotes.size]
        } else null
    }
    
    var showAIInsight by remember { mutableStateOf(false) }
    var aiInsight by remember { mutableStateOf("") }
    var isGeneratingInsight by remember { mutableStateOf(false) }
    
    // Generate AI insight
    fun generateAIInsight(quote: Quote) {
        scope.launch {
            isGeneratingInsight = true
            delay(1500) // Simulate AI processing
            aiInsight = getAIInsight(quote)
            showAIInsight = true
            isGeneratingInsight = false
        }
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
        // Animated background particles
        ParticleBackground(
            modifier = Modifier.fillMaxSize(),
            particleColor = PremiumColors.CyberBlue.copy(alpha = 0.2f)
        )
        
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "✨ Daily Wisdom",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            if (todayQuote != null) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Date Header
                    item {
                        DateHeaderCard()
                    }
                    
                    // Main Quote Card with premium design
                    item {
                        MainQuoteCard(
                            quote = todayQuote,
                            onFavoriteClick = {
                                scope.launch {
                                    viewModel.toggleFavorite(todayQuote)
                                }
                            }
                        )
                    }
                    
                    // Action Buttons
                    item {
                        ActionButtonsRow(
                            quote = todayQuote,
                            onShareClick = {
                                val shareIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "Today's Einstein Quote:\n\n\"${todayQuote.Quote}\"\n\n- From ${todayQuote.Work}"
                                    )
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share Quote"))
                            },
                            onAIChatClick = { onChatClick(todayQuote) },
                            onGenerateInsight = { generateAIInsight(todayQuote) }
                        )
                    }
                    
                    // AI Insight Section
                    if (showAIInsight || isGeneratingInsight) {
                        item {
                            AIInsightCard(
                                insight = aiInsight,
                                isLoading = isGeneratingInsight
                            )
                        }
                    }
                    
                    // Reflection Prompts
                    item {
                        ReflectionPromptsCard(quote = todayQuote)
                    }
                    
                    // Historical Context
                    item {
                        HistoricalContextCard(quote = todayQuote)
                    }
                    
                    // Bottom spacing
                    item {
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            } else {
                // Loading or empty state
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = PremiumColors.ElectricPurple
                    )
                }
            }
        }
    }
}

@Composable
fun DateHeaderCard() {
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")
    
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        glowColor = PremiumColors.QuantumTeal,
        animateGlow = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = today.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = PremiumColors.QuantumTeal
            )
            Text(
                text = today.format(formatter),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun MainQuoteCard(
    quote: Quote,
    onFavoriteClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "quote")
    val gradientOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradient"
    )
    
    PremiumGlassCard(
        modifier = Modifier.fillMaxWidth(),
        gradientColors = PremiumColors.GalaxyGradient
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = PremiumColors.GalaxyGradient.map { it.copy(alpha = 0.1f) },
                        start = Offset(gradientOffset, 0f),
                        end = Offset(gradientOffset + 500f, 500f)
                    )
                )
        ) {
            // Favorite button
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = if (quote.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (quote.isFavorite) Color(0xFFE91E63) else Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(28.dp)
                )
            }
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Quote icon
                Text(
                    text = "💡",
                    fontSize = 48.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                // Quote text
                Text(
                    text = "\"${quote.Quote}\"",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 32.sp
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Divider
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(1.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.White.copy(alpha = 0.3f),
                                    Color.Transparent
                                )
                            )
                        )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Work and year
                Text(
                    text = quote.Work,
                    style = MaterialTheme.typography.titleMedium,
                    color = PremiumColors.CyberBlue,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${quote.Year} • ${quote.Category}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun ActionButtonsRow(
    quote: Quote,
    onShareClick: () -> Unit,
    onAIChatClick: () -> Unit,
    onGenerateInsight: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FloatingGlassButton(
            modifier = Modifier.weight(1f),
            onClick = onShareClick,
            icon = {
                Icon(
                    Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            },
            text = "Share",
            primaryColor = PremiumColors.NeonPink
        )
        
        FloatingGlassButton(
            modifier = Modifier.weight(1f),
            onClick = onAIChatClick,
            icon = {
                Icon(
                    Icons.Default.Chat,
                    contentDescription = "Chat",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            },
            text = "AI Chat",
            primaryColor = PremiumColors.ElectricPurple
        )
        
        FloatingGlassButton(
            modifier = Modifier.weight(1f),
            onClick = onGenerateInsight,
            icon = {
                Icon(
                    Icons.Default.AutoAwesome,
                    contentDescription = "Insight",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            },
            text = "Insights",
            primaryColor = PremiumColors.CyberBlue
        )
    }
}

@Composable
fun AIInsightCard(
    insight: String,
    isLoading: Boolean
) {
    AnimatedVisibility(
        visible = true,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        GlassmorphicCard(
            modifier = Modifier.fillMaxWidth(),
            glowColor = PremiumColors.CosmicIndigo,
            animateGlow = true
        ) {
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
                        tint = PremiumColors.CosmicIndigo,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "AI Insight",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PremiumColors.CosmicIndigo
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = PremiumColors.CosmicIndigo,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                } else {
                    Text(
                        text = insight,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.9f),
                        lineHeight = 24.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ReflectionPromptsCard(quote: Quote) {
    val prompts = remember(quote) {
        generateReflectionPrompts(quote)
    }
    
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        glowColor = PremiumColors.QuantumTeal
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Psychology,
                    contentDescription = null,
                    tint = PremiumColors.QuantumTeal,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Reflection Prompts",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PremiumColors.QuantumTeal
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            prompts.forEachIndexed { index, prompt ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "${index + 1}.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = PremiumColors.QuantumTeal,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = prompt,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }
}

@Composable
fun HistoricalContextCard(quote: Quote) {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        glowColor = PremiumColors.PlasmaOrange
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.History,
                    contentDescription = null,
                    tint = PremiumColors.PlasmaOrange,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Historical Context",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PremiumColors.PlasmaOrange
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "This quote from \"${quote.Work}\" was written during ${quote.Year}, " +
                        "a period when Einstein was ${getHistoricalContext(quote.Year)}. " +
                        "The ${quote.Category.lowercase()} themes in this work reflect his revolutionary thinking " +
                        "that would forever change our understanding of the universe.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f),
                lineHeight = 22.sp
            )
        }
    }
}

// Helper functions
fun getAIInsight(quote: Quote): String {
    val insights = listOf(
        "This quote reflects Einstein's belief that ${quote.Category.lowercase()} is fundamental to human progress. The emphasis on ${quote.Quote.split(" ").take(3).joinToString(" ")} suggests a deep understanding of how innovation emerges from questioning established norms.",
        "Einstein's perspective here challenges us to reconsider our approach to ${quote.Category.lowercase()}. Written in ${quote.Year}, this wisdom remains remarkably relevant to modern challenges in science and society.",
        "The philosophical depth in this quote demonstrates Einstein's role not just as a scientist, but as a thinker who understood the interconnection between ${quote.Category.lowercase()} and human experience.",
        "This insight from ${quote.Work} showcases Einstein's unique ability to distill complex ideas into accessible wisdom. The focus on ${quote.Category.lowercase()} reveals his holistic approach to understanding reality."
    )
    return insights.random()
}

fun generateReflectionPrompts(quote: Quote): List<String> {
    return listOf(
        "How does this quote apply to your current challenges?",
        "What aspect of ${quote.Category.lowercase()} resonates most with you today?",
        "How might Einstein's perspective change your approach to problem-solving?",
        "What personal experience connects you to this wisdom?"
    )
}

fun getHistoricalContext(year: String): String {
    val yearInt = year.toIntOrNull() ?: return "exploring groundbreaking ideas"
    return when {
        yearInt < 1905 -> "developing his early theories"
        yearInt in 1905..1915 -> "revolutionizing physics with relativity theory"
        yearInt in 1916..1925 -> "expanding quantum mechanics understanding"
        yearInt in 1926..1935 -> "debating quantum theory implications"
        yearInt in 1936..1945 -> "contributing to wartime science efforts"
        else -> "refining his unified field theory"
    }
}
