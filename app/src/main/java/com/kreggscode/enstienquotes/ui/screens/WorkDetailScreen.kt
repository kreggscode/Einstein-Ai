package com.kreggscode.enstienquotes.ui.screens

import androidx.compose.animation.*
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.kreggscode.enstienquotes.data.WorksDataLoader
import com.kreggscode.enstienquotes.ui.components.GlassmorphicCard
import com.kreggscode.enstienquotes.ui.theme.AIInsightsButton
import com.kreggscode.enstienquotes.ui.theme.PremiumColors
import kotlinx.coroutines.launch

// Data models for detailed content
data class WorkSection(
    val title: String,
    val content: String
)

data class Equation(
    val formula: String,
    val name: String,
    val explanation: String
)

data class DetailedWork(
    val id: String,
    val title: String,
    val subtitle: String,
    val year: String,
    val icon: String,
    val summary: String,
    val keyEquation: String? = null,
    val keyEquationExplanation: String? = null,
    val sections: List<WorkSection> = emptyList(),
    val equations: List<Equation> = emptyList(),
    val funFacts: List<String> = emptyList()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkDetailScreen(
    workId: String,
    onBackClick: () -> Unit,
    onChatClick: (String) -> Unit
) {
    val context = LocalContext.current
    val dataLoader = remember { WorksDataLoader(context) }
    var work by remember { mutableStateOf<com.kreggscode.enstienquotes.data.MajorWork?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    
    LaunchedEffect(workId) {
        launch {
            work = dataLoader.getMajorWorkById(workId)
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
    
    if (work == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Work not found",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        return
    }
    
    val workData = work!!
    
    val detailedWork = remember {
        DetailedWork(
            id = workData.id,
            title = workData.title,
            subtitle = workData.subtitle,
            year = workData.year,
            icon = workData.icon,
            summary = workData.summary,
            keyEquation = workData.keyEquation,
            keyEquationExplanation = workData.keyEquationExplanation,
            sections = workData.sections.map { WorkSection(it.title, it.content) },
            equations = workData.equations.map { Equation(it.formula, it.name, it.explanation) },
            funFacts = workData.funFacts
        )
    }
    
    var showEquations by remember { mutableStateOf(false) }
    var showFunFacts by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = workData.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(
                            text = workData.year,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Share */ }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Section
            item {
                HeroSection(work = detailedWork)
            }
            
            // AI Insights Button
            item {
                AIInsightsButton(
                    onClick = { onChatClick("Tell me more about ${detailedWork.title}") }
                )
            }
            
            // Key Equation (if exists)
            if (detailedWork.keyEquation != null) {
                item {
                    KeyEquationCard(
                        equation = detailedWork.keyEquation!!,
                        explanation = detailedWork.keyEquationExplanation ?: ""
                    )
                }
            }
            
            // Summary
            item {
                SummaryCard(summary = detailedWork.summary)
            }
            
            // Sections
            items(detailedWork.sections) { section ->
                SectionCard(section = section)
            }
            
            // Equations Toggle
            if (detailedWork.equations.isNotEmpty()) {
                item {
                    ExpandableSection(
                        title = "Mathematical Equations",
                        icon = "📐",
                        count = detailedWork.equations.size,
                        isExpanded = showEquations,
                        onToggle = { showEquations = !showEquations }
                    )
                }
                
                if (showEquations) {
                    items(detailedWork.equations) { equation ->
                        EquationCard(equation = equation)
                    }
                }
            }
            
            // Fun Facts Toggle
            if (detailedWork.funFacts.isNotEmpty()) {
                item {
                    ExpandableSection(
                        title = "Fun Facts",
                        icon = "🎯",
                        count = detailedWork.funFacts.size,
                        isExpanded = showFunFacts,
                        onToggle = { showFunFacts = !showFunFacts }
                    )
                }
                
                if (showFunFacts) {
                    items(detailedWork.funFacts) { fact ->
                        FunFactCard(fact = fact)
                    }
                }
            }
            
            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
private fun HeroSection(work: DetailedWork) {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animated icon
            val infiniteTransition = rememberInfiniteTransition()
            val rotation by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(20000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )
            
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.sweepGradient(
                            colors = listOf(
                                PremiumColors.ElectricPurple.copy(alpha = 0.3f),
                                PremiumColors.CyberBlue.copy(alpha = 0.3f),
                                PremiumColors.NeonPink.copy(alpha = 0.3f),
                                PremiumColors.ElectricPurple.copy(alpha = 0.3f)
                            )
                        )
                    )
                    .rotate(rotation),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = work.icon,
                        style = MaterialTheme.typography.displayLarge
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = work.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            if (work.subtitle.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = work.subtitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = PremiumColors.QuantumGold.copy(alpha = 0.2f)
            ) {
                Text(
                    text = "Published ${work.year}",
                    style = MaterialTheme.typography.labelLarge,
                    color = PremiumColors.QuantumGold,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun KeyEquationCard(equation: String, explanation: String) {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Key Equation",
                style = MaterialTheme.typography.labelLarge,
                color = PremiumColors.QuantumGold,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Equation with special styling
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
            ) {
                Text(
                    text = equation,
                    style = MaterialTheme.typography.displayMedium,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = PremiumColors.ElectricPurple,
                    modifier = Modifier.padding(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = explanation,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun SummaryCard(summary: String) {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = PremiumColors.CyberBlue,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Overview",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = summary,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
private fun SectionCard(section: WorkSection) {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = section.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = PremiumColors.ElectricPurple
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = section.content,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
private fun ExpandableSection(
    title: String,
    icon: String,
    count: Int,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggle)
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = icon,
                    style = MaterialTheme.typography.headlineSmall
                )
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$count items",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            
            Icon(
                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = if (isExpanded) "Collapse" else "Expand",
                tint = PremiumColors.ElectricPurple
            )
        }
    }
}

@Composable
private fun EquationCard(equation: Equation) {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = equation.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = PremiumColors.CyberBlue
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
            ) {
                Text(
                    text = equation.formula,
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = PremiumColors.ElectricPurple,
                    modifier = Modifier.padding(16.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = equation.explanation,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun FunFactCard(fact: String) {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "💡",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = fact,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 24.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
