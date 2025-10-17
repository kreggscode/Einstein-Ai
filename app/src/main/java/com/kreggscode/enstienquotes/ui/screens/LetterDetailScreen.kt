package com.kreggscode.enstienquotes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.kreggscode.enstienquotes.data.Letter
import com.kreggscode.enstienquotes.data.WorksDataLoader
import com.kreggscode.enstienquotes.ui.components.GlassmorphicCard
import com.kreggscode.enstienquotes.ui.theme.AIInsightsButton
import com.kreggscode.enstienquotes.ui.theme.KeyPointCard
import com.kreggscode.enstienquotes.ui.theme.PremiumColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LetterDetailScreen(
    letterId: String,
    onBackClick: () -> Unit,
    onChatClick: (String) -> Unit
) {
    val context = LocalContext.current
    val dataLoader = remember { WorksDataLoader(context) }
    var letter by remember { mutableStateOf<Letter?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var showFullLetter by remember { mutableStateOf(false) }
    
    LaunchedEffect(letterId) {
        launch {
            letter = dataLoader.getLetterById(letterId)
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
    
    if (letter == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Letter not found",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        return
    }
    
    val letterData = letter!!
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = letterData.title,
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
                LetterHeroSection(letterData)
            }
            
            // AI Insights
            item {
                AIInsightsButton(
                    onClick = { onChatClick("Discuss Einstein's letter to ${letterData.recipient}") }
                )
            }
            
            // Letter Details
            item {
                LetterDetailsCard(letterData)
            }
            
            // Historical Context
            item {
                HistoricalContextCard(letterData.historicalContext)
            }
            
            // Full Letter Text
            item {
                FullLetterCard(
                    letterText = letterData.letterText,
                    isExpanded = showFullLetter,
                    onToggle = { showFullLetter = !showFullLetter }
                )
            }
            
            // Key Points
            items(letterData.keyPoints) { point ->
                KeyPointCard(point)
            }
            
            // Legacy
            letterData.legacy?.let { legacy ->
                item {
                    LegacyCard(legacy)
                }
            }
            
            item { Spacer(Modifier.height(100.dp)) }
        }
    }
}

@Composable
private fun LetterHeroSection(letter: Letter) {
    GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = letter.icon, style = MaterialTheme.typography.displayLarge)
            Spacer(Modifier.height(16.dp))
            Text(
                text = letter.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = letter.date,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = letter.summary,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
private fun LetterDetailsCard(letter: Letter) {
    GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Letter Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = PremiumColors.ElectricPurple
            )
            Spacer(Modifier.height(16.dp))
            
            DetailRow(label = "To:", value = letter.recipient)
            DetailRow(label = "Date:", value = letter.date)
            DetailRow(label = "Location:", value = letter.location)
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface.copy(0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun HistoricalContextCard(context: String) {
    GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "📜", style = MaterialTheme.typography.headlineSmall)
                Text(
                    text = "Historical Context",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = context,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
private fun FullLetterCard(
    letterText: String,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "✉️", style = MaterialTheme.typography.headlineSmall)
                    Text(
                        text = "Full Letter Text",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onToggle) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = PremiumColors.ElectricPurple
                    )
                }
            }
            
            if (isExpanded) {
                Spacer(Modifier.height(16.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface.copy(0.3f)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = letterText,
                            style = MaterialTheme.typography.bodyLarge,
                            fontFamily = FontFamily.Serif,
                            lineHeight = 26.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(0.9f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LegacyCard(legacy: String) {
    GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🏛️",
                style = MaterialTheme.typography.displayMedium
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Legacy",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = PremiumColors.QuantumGold
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = legacy,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }
    }
}
