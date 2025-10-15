package com.kreggscode.einsteinquotes.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.shadow
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kreggscode.einsteinquotes.ui.components.GlassmorphicCard
import com.kreggscode.einsteinquotes.ui.theme.PremiumColors

enum class WorkCategory {
    MAJOR_WORKS, ESSAYS, LETTERS, PAPERS
}

data class WorkItem(
    val id: String,
    val title: String,
    val subtitle: String = "",
    val year: String,
    val icon: String,
    val summary: String,
    val category: WorkCategory
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EinsteinWorksScreen(
    onBackClick: () -> Unit,
    onWorkClick: (WorkItem) -> Unit,
    onChatClick: (String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf<WorkCategory?>(null) }
    
    // Sample data - will be loaded from JSON
    val majorWorks = remember {
        listOf(
            WorkItem(
                id = "special_relativity",
                title = "Special Relativity",
                subtitle = "On the Electrodynamics of Moving Bodies",
                year = "1905",
                icon = "⚡",
                summary = "Revolutionary paper introducing E=mc² and transforming our understanding of space and time",
                category = WorkCategory.MAJOR_WORKS
            ),
            WorkItem(
                id = "general_relativity",
                title = "General Relativity",
                subtitle = "The Foundation of the General Theory",
                year = "1916",
                icon = "🌌",
                summary = "Gravity as curved spacetime - Einstein's masterpiece that predicted black holes",
                category = WorkCategory.MAJOR_WORKS
            ),
            WorkItem(
                id = "photoelectric_effect",
                title = "Photoelectric Effect",
                subtitle = "On the Production of Light",
                year = "1905",
                icon = "💡",
                summary = "Nobel Prize-winning work proving light behaves as particles, founding quantum mechanics",
                category = WorkCategory.MAJOR_WORKS
            ),
            WorkItem(
                id = "brownian_motion",
                title = "Brownian Motion",
                subtitle = "On the Motion of Small Particles",
                year = "1905",
                icon = "🔬",
                summary = "Mathematical proof that atoms exist by explaining random particle motion",
                category = WorkCategory.MAJOR_WORKS
            )
        )
    }
    
    val essays = remember {
        listOf(
            WorkItem(
                id = "why_socialism",
                title = "Why Socialism?",
                subtitle = "",
                year = "1949",
                icon = "⚖️",
                summary = "Einstein's passionate essay arguing for democratic socialism and critiquing capitalism",
                category = WorkCategory.ESSAYS
            ),
            WorkItem(
                id = "world_as_i_see_it",
                title = "The World As I See It",
                subtitle = "",
                year = "1930",
                icon = "🌍",
                summary = "Philosophical reflections on life, meaning, society, and personal values",
                category = WorkCategory.ESSAYS
            ),
            WorkItem(
                id = "religion_and_science",
                title = "Religion and Science",
                subtitle = "",
                year = "1930",
                icon = "🔬",
                summary = "Exploring the relationship between scientific inquiry and cosmic religious feeling",
                category = WorkCategory.ESSAYS
            )
        )
    }
    
    val letters = remember {
        listOf(
            WorkItem(
                id = "letter_to_roosevelt",
                title = "Letter to Roosevelt",
                subtitle = "Warning About Atomic Weapons",
                year = "1939",
                icon = "⚛️",
                summary = "The letter that led to the Manhattan Project and changed history forever",
                category = WorkCategory.LETTERS
            ),
            WorkItem(
                id = "letter_to_phyllis",
                title = "Letter to Phyllis Wright",
                subtitle = "Do Scientists Pray?",
                year = "1936",
                icon = "✉️",
                summary = "Thoughtful response to a 6th grader about science and religion",
                category = WorkCategory.LETTERS
            ),
            WorkItem(
                id = "letter_to_marie_curie",
                title = "Letter to Marie Curie",
                subtitle = "Support During Scandal",
                year = "1911",
                icon = "💌",
                summary = "Einstein's fierce defense of Marie Curie against public attacks",
                category = WorkCategory.LETTERS
            ),
            WorkItem(
                id = "letter_to_freud",
                title = "Letter to Freud: Why War?",
                subtitle = "On the Psychology of Conflict",
                year = "1932",
                icon = "☮️",
                summary = "Exploring the psychological roots of war with Sigmund Freud",
                category = WorkCategory.LETTERS
            )
        )
    }
    
    val papers = remember {
        listOf(
            WorkItem(
                id = "annus_mirabilis_1905",
                title = "Annus Mirabilis Papers",
                subtitle = "The Miracle Year",
                year = "1905",
                icon = "✨",
                summary = "Four groundbreaking papers in one year that revolutionized physics",
                category = WorkCategory.PAPERS
            ),
            WorkItem(
                id = "general_relativity_1915",
                title = "Field Equations of Gravitation",
                subtitle = "Complete General Relativity",
                year = "1915",
                icon = "🌌",
                summary = "The final form of Einstein's field equations describing curved spacetime",
                category = WorkCategory.PAPERS
            ),
            WorkItem(
                id = "epr_paradox_1935",
                title = "EPR Paradox",
                subtitle = "Quantum Entanglement",
                year = "1935",
                icon = "🎲",
                summary = "Einstein's challenge to quantum mechanics that discovered 'spooky action'",
                category = WorkCategory.PAPERS
            ),
            WorkItem(
                id = "unified_field_theory",
                title = "Unified Field Theory",
                subtitle = "Einstein's Unfulfilled Dream",
                year = "1950",
                icon = "🔮",
                summary = "His last major work attempting to unify all forces of nature",
                category = WorkCategory.PAPERS
            )
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Einstein's Works",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
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
            // Header
            item {
                HeaderCard()
            }
            
            // Category Selector
            item {
                CategorySelector(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )
            }
            
            // Show content based on selected category
            when (selectedCategory) {
                WorkCategory.MAJOR_WORKS -> {
                    item {
                        SectionHeader(
                            title = "Major Works",
                            subtitle = "Revolutionary papers that changed physics",
                            icon = "⚡"
                        )
                    }
                    items(majorWorks) { work ->
                        WorkItemCard(
                            work = work,
                            onClick = { onWorkClick(work) },
                            onChatClick = { onChatClick(work.title) }
                        )
                    }
                }
                WorkCategory.ESSAYS -> {
                    item {
                        SectionHeader(
                            title = "Essays",
                            subtitle = "Einstein's philosophical writings",
                            icon = "📝"
                        )
                    }
                    items(essays) { work ->
                        WorkItemCard(
                            work = work,
                            onClick = { onWorkClick(work) },
                            onChatClick = { onChatClick(work.title) }
                        )
                    }
                }
                WorkCategory.LETTERS -> {
                    item {
                        SectionHeader(
                            title = "Letters",
                            subtitle = "Personal correspondence that shaped history",
                            icon = "✉️"
                        )
                    }
                    items(letters) { work ->
                        WorkItemCard(
                            work = work,
                            onClick = { onWorkClick(work) },
                            onChatClick = { onChatClick(work.title) }
                        )
                    }
                }
                WorkCategory.PAPERS -> {
                    item {
                        SectionHeader(
                            title = "Scientific Papers",
                            subtitle = "Published research that defined modern physics",
                            icon = "📄"
                        )
                    }
                    items(papers) { work ->
                        WorkItemCard(
                            work = work,
                            onClick = { onWorkClick(work) },
                            onChatClick = { onChatClick(work.title) }
                        )
                    }
                }
                null -> {
                    // Show all categories overview
                    item {
                        CategoryOverviewCard(
                            title = "Major Works",
                            count = majorWorks.size,
                            icon = "⚡",
                            color = PremiumColors.ElectricPurple,
                            onClick = { selectedCategory = WorkCategory.MAJOR_WORKS }
                        )
                    }
                    item {
                        CategoryOverviewCard(
                            title = "Essays",
                            count = essays.size,
                            icon = "📝",
                            color = PremiumColors.CyberBlue,
                            onClick = { selectedCategory = WorkCategory.ESSAYS }
                        )
                    }
                    item {
                        CategoryOverviewCard(
                            title = "Letters",
                            count = letters.size,
                            icon = "✉️",
                            color = PremiumColors.NeonPink,
                            onClick = { selectedCategory = WorkCategory.LETTERS }
                        )
                    }
                    item {
                        CategoryOverviewCard(
                            title = "Scientific Papers",
                            count = papers.size,
                            icon = "📄",
                            color = PremiumColors.QuantumGold,
                            onClick = { selectedCategory = WorkCategory.PAPERS }
                        )
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
private fun HeaderCard() {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                PremiumColors.ElectricPurple.copy(alpha = 0.2f),
                                PremiumColors.CyberBlue.copy(alpha = 0.2f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📚",
                    style = MaterialTheme.typography.displayMedium
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Explore Einstein's Legacy",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Dive into the revolutionary works, essays, letters, and papers that changed our understanding of the universe",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun CategorySelector(
    selectedCategory: WorkCategory?,
    onCategorySelected: (WorkCategory?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CategoryChip(
            label = "All",
            isSelected = selectedCategory == null,
            onClick = { onCategorySelected(null) }
        )
        CategoryChip(
            label = "Major Works",
            isSelected = selectedCategory == WorkCategory.MAJOR_WORKS,
            onClick = { onCategorySelected(WorkCategory.MAJOR_WORKS) }
        )
        CategoryChip(
            label = "Essays",
            isSelected = selectedCategory == WorkCategory.ESSAYS,
            onClick = { onCategorySelected(WorkCategory.ESSAYS) }
        )
        CategoryChip(
            label = "Letters",
            isSelected = selectedCategory == WorkCategory.LETTERS,
            onClick = { onCategorySelected(WorkCategory.LETTERS) }
        )
        CategoryChip(
            label = "Papers",
            isSelected = selectedCategory == WorkCategory.PAPERS,
            onClick = { onCategorySelected(WorkCategory.PAPERS) }
        )
    }
}

@Composable
private fun CategoryChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        PremiumColors.ElectricPurple
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    
    val textColor = if (isSelected) {
        Color.White
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor,
        modifier = Modifier.height(40.dp),
        border = if (!isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)) else null
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = textColor,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    subtitle: String,
    icon: String
) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = icon,
                style = MaterialTheme.typography.headlineMedium
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
private fun WorkItemCard(
    work: WorkItem,
    onClick: () -> Unit,
    onChatClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )
    
    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable {
                isPressed = true
                onClick()
            }
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Header with icon and year
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Animated icon
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        PremiumColors.ElectricPurple.copy(alpha = 0.2f),
                                        PremiumColors.CyberBlue.copy(alpha = 0.2f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = work.icon,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                    
                    Column {
                        Text(
                            text = work.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        if (work.subtitle.isNotEmpty()) {
                            Text(
                                text = work.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
                
                // Year badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = PremiumColors.QuantumGold.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = work.year,
                        style = MaterialTheme.typography.labelMedium,
                        color = PremiumColors.QuantumGold,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Summary
            Text(
                text = work.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Read More button
                Surface(
                    onClick = onClick,
                    shape = RoundedCornerShape(12.dp),
                    color = PremiumColors.ElectricPurple,
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Read More",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                // AI Insights button
                Surface(
                    onClick = onChatClick,
                    shape = RoundedCornerShape(12.dp),
                    color = PremiumColors.CyberBlue,
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI Insights",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryOverviewCard(
    title: String,
    count: Int,
    icon: String,
    color: Color,
    onClick: () -> Unit
) {
    var isHovered by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isHovered) 1.02f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )
    
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 2.dp,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    color.copy(alpha = 0.3f),
                                    color.copy(alpha = 0.1f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = icon,
                        style = MaterialTheme.typography.displaySmall
                    )
                }
                
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "$count items available",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
