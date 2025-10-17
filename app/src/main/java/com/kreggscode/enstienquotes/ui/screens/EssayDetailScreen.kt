package com.kreggscode.enstienquotes.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.kreggscode.enstienquotes.data.Essay
import com.kreggscode.enstienquotes.data.WorksDataLoader
import com.kreggscode.enstienquotes.ui.components.GlassmorphicCard
import com.kreggscode.enstienquotes.ui.theme.AIInsightsButton
import com.kreggscode.enstienquotes.ui.theme.KeyPointCard
import com.kreggscode.enstienquotes.ui.theme.PremiumColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EssayDetailScreen(
    essayId: String,
    onBackClick: () -> Unit,
    onChatClick: (String) -> Unit
) {
    val context = LocalContext.current
    val dataLoader = remember { WorksDataLoader(context) }
    var essay by remember { mutableStateOf<Essay?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    
    LaunchedEffect(essayId) {
        launch {
            essay = dataLoader.getEssayById(essayId)
            isLoading = false
        }
    }
    
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = PremiumColors.ElectricPurple)
        }
        return
    }
    
    if (essay == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Essay not found",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        return
    }
    
    val essayData = essay!!
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = essayData.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Share */ }) {
                        Icon(Icons.Default.Share, "Share")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero
            item {
                EssayHeroSection(essayData)
            }
            
            // AI Insights
            item {
                AIInsightsButton(
                    onClick = { onChatClick("Discuss the essay '${essayData.title}'") }
                )
            }
            
            // Opening Quote
            item {
                QuoteCard(essayData.openingQuote)
            }
            
            // Main Themes
            item {
                ThemesCard(essayData.mainThemes)
            }
            
            // Key Points
            items(essayData.keyPoints) { point ->
                KeyPointCard(point)
            }
            
            // Relevance Today
            if (essayData.relevanceToday.isNotEmpty()) {
                item {
                    RelevanceTodayCard(essayData.relevanceToday)
                }
            }
            
            // Closing Thought
            essayData.closingThought?.let { thought ->
                item {
                    ClosingThoughtCard(thought)
                }
            }
            
            item { Spacer(Modifier.height(100.dp)) }
        }
    }
}

@Composable
private fun EssayHeroSection(essay: Essay) {
    GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = essay.icon, style = MaterialTheme.typography.displayLarge)
            Spacer(Modifier.height(16.dp))
            Text(
                text = essay.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "${essay.year} • ${essay.publication}",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = essay.summary,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
private fun QuoteCard(quote: String) {
    GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "\"",
                style = MaterialTheme.typography.displayLarge,
                color = PremiumColors.ElectricPurple.copy(0.3f),
                fontWeight = FontWeight.Bold
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Opening Quote",
                    style = MaterialTheme.typography.labelLarge,
                    color = PremiumColors.ElectricPurple,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = quote,
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 24.sp
                )
            }
        }
    }
}

@Composable
private fun ThemesCard(themes: List<String>) {
    GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Main Themes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = PremiumColors.CyberBlue
            )
            Spacer(Modifier.height(12.dp))
            themes.forEach { theme ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "•", color = PremiumColors.CyberBlue)
                    Text(
                        text = theme,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun RelevanceTodayCard(items: List<String>) {
    GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "🌟", style = MaterialTheme.typography.headlineSmall)
                Text(
                    text = "Relevance Today",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(12.dp))
            items.forEach { item ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "→", color = PremiumColors.QuantumGold)
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun ClosingThoughtCard(thought: String) {
    GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "💭",
                style = MaterialTheme.typography.displayMedium
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Closing Thought",
                style = MaterialTheme.typography.labelLarge,
                color = PremiumColors.NeonPink,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = thought,
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }
    }
}
