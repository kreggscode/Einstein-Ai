package com.kreggscode.enstienquotes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kreggscode.enstienquotes.data.Paper
import com.kreggscode.enstienquotes.data.WorksDataLoader
import com.kreggscode.enstienquotes.ui.components.GlassmorphicCard
import com.kreggscode.enstienquotes.ui.theme.AIInsightsButton
import com.kreggscode.enstienquotes.ui.theme.PremiumColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaperDetailScreen(
    paperId: String,
    onBackClick: () -> Unit,
    onChatClick: (String) -> Unit
) {
    val context = LocalContext.current
    val dataLoader = remember { WorksDataLoader(context) }
    var paper by remember { mutableStateOf<Paper?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    
    LaunchedEffect(paperId) {
        launch {
            paper = dataLoader.getPaperById(paperId)
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
    
    if (paper == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Paper not found",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        return
    }
    
    val paperData = paper!!
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = paperData.title,
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
                PaperHeroCard(paperData)
            }
            
            // AI Insights
            item {
                AIInsightsButton(
                    onClick = { onChatClick("Discuss the paper '${paperData.title}'") }
                )
            }
            
            // Context
            item {
                ContextCard(paperData.context)
            }
            
            // Abstract
            paperData.abstract?.let { abstract ->
                item {
                    AbstractCard(abstract)
                }
            }
            
            // Legacy
            paperData.legacy?.let { legacy ->
                item {
                    LegacyCard(legacy)
                }
            }
            
            item { Spacer(Modifier.height(100.dp)) }
        }
    }
}

@Composable
private fun PaperHeroCard(paper: Paper) {
    GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = paper.icon, style = MaterialTheme.typography.displayLarge)
            Spacer(Modifier.height(16.dp))
            Text(
                text = paper.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "${paper.year} • ${paper.journal}",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = paper.summary,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
private fun ContextCard(context: String) {
    GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "📚", style = MaterialTheme.typography.headlineSmall)
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
private fun AbstractCard(abstract: String) {
    GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "📄", style = MaterialTheme.typography.headlineSmall)
                Text(
                    text = "Abstract",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PremiumColors.CyberBlue
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = abstract,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 24.sp
            )
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
