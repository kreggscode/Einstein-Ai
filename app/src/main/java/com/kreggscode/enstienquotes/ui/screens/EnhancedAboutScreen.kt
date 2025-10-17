package com.kreggscode.enstienquotes.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.kreggscode.enstienquotes.ui.components.*
import com.kreggscode.enstienquotes.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedAboutScreen(
    onBackClick: () -> Unit,
    onWorkClick: ((String) -> Unit)? = null
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Biography", "Works", "Legacy", "Timeline")
    
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
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "About Einstein",
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
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Tabs
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                    edgePadding = 16.dp
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    title,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }
                
                // Content
                when (selectedTab) {
                    0 -> BiographyTab()
                    1 -> WorksTab(onWorkClick = onWorkClick)
                    2 -> LegacyTab()
                    3 -> TimelineTab()
                }
            }
        }
    }
}

@Composable
fun BiographyTab() {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            GlassmorphicCard(
                modifier = Modifier.fillMaxWidth(),
                glowColor = PremiumColors.ElectricPurple,
                animateGlow = true
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🧠", fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Albert Einstein",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        "1879 - 1955",
                        style = MaterialTheme.typography.titleMedium,
                        color = PremiumColors.CyberBlue
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Theoretical Physicist",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }
        
        item {
            InfoCard(
                title = "Early Life",
                icon = "👶",
                content = "Born in Ulm, Germany on March 14, 1879. Showed exceptional mathematical ability from an early age. Attended the Swiss Federal Polytechnic School in Zurich, graduating in 1900 with a teaching diploma in physics and mathematics."
            )
        }
        
        item {
            InfoCard(
                title = "Miracle Year (1905)",
                icon = "✨",
                content = "Published four groundbreaking papers: Photoelectric Effect, Brownian Motion, Special Relativity, and Mass-Energy Equivalence (E=mc²). These papers revolutionized physics and established Einstein as a leading scientist."
            )
        }
        
        item {
            InfoCard(
                title = "General Relativity",
                icon = "🌌",
                content = "In 1915, Einstein completed his theory of General Relativity, describing gravity as the curvature of spacetime. This theory predicted gravitational waves, confirmed 100 years later in 2015."
            )
        }
        
        item {
            InfoCard(
                title = "Nobel Prize",
                icon = "🏆",
                content = "Awarded the 1921 Nobel Prize in Physics for his discovery of the law of the photoelectric effect, a pivotal step in the development of quantum theory."
            )
        }
        
        item {
            InfoCard(
                title = "Later Years",
                icon = "📚",
                content = "Emigrated to the United States in 1933 to escape Nazi persecution. Spent his final years at Princeton University, continuing his quest to understand the fundamental laws of nature. Died on April 18, 1955."
            )
        }
    }
}

@Composable
fun WorksTab(onWorkClick: ((String) -> Unit)? = null) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HighlightCard(
                title = "E = mc²",
                subtitle = "Mass-Energy Equivalence",
                description = "Einstein's most famous equation shows that mass and energy are interchangeable. This fundamental principle led to nuclear energy and our understanding of the universe.",
                gradient = listOf(PremiumColors.PlasmaOrange, PremiumColors.NeonPink)
            )
        }
        
        item {
            Text(
                "Major Works",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        
        val works = listOf(
            Work("Special Relativity", "1905", "Revolutionized concepts of space and time", "⚡", "special_relativity"),
            Work("Photoelectric Effect", "1905", "Explained light's particle nature, won Nobel Prize", "💡", "photoelectric_effect"),
            Work("Brownian Motion", "1905", "Provided evidence for atoms and molecules", "🔬", "brownian_motion"),
            Work("General Relativity", "1915", "Described gravity as spacetime curvature", "🌀", "general_relativity"),
            Work("Cosmological Constant", "1917", "Applied relativity to cosmology", "🌌", null),
            Work("Bose-Einstein Statistics", "1924", "Predicted new state of matter", "❄️", null),
            Work("EPR Paradox", "1935", "Challenged quantum mechanics interpretation", "🎭", null),
            Work("Unified Field Theory", "1920s-1950s", "Attempted to unify all forces", "🔗", null),
            Work("Gravitational Waves", "1916", "Predicted ripples in spacetime", "🌊", null),
            Work("Black Holes", "1916", "Mathematical prediction from relativity", "⚫", null),
            Work("Quantum Theory", "1905-1925", "Contributions to quantum mechanics", "⚛️", null),
            Work("Statistical Mechanics", "1902-1904", "Molecular theory of heat", "🔥", null),
            Work("Light Quanta", "1905", "Photon concept", "✨", null),
            Work("Stimulated Emission", "1917", "Basis for lasers", "🔦", null),
            Work("Capillarity", "1901", "First published paper", "💧", null),
            Work("Critical Opalescence", "1910", "Light scattering phenomenon", "🌈", null),
            Work("Zero-Point Energy", "1913", "Quantum vacuum energy", "⚡", null),
            Work("Wave-Particle Duality", "1909", "Dual nature of light", "🌊", null),
            Work("Refrigeration", "1926", "Einstein refrigerator patent", "🧊", null),
            Work("Wormholes", "1935", "Einstein-Rosen bridges", "🕳️", null)
        )
        
        items(works) { work ->
            WorkCard(work = work, onClick = {
                work.id?.let { id -> onWorkClick?.invoke(id) }
            })
        }
    }
}

@Composable
fun LegacyTab() {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            InfoCard(
                title = "Scientific Impact",
                icon = "🔬",
                content = "Revolutionized physics and our understanding of the universe. His theories enabled GPS technology, nuclear energy, lasers, and countless other technologies. Inspired generations of scientists."
            )
        }
        
        item {
            InfoCard(
                title = "Cultural Icon",
                icon = "🎨",
                content = "Symbol of genius and intellectual achievement. His image and quotes remain iconic in popular culture. Time Magazine's Person of the Century (1999)."
            )
        }
        
        item {
            InfoCard(
                title = "Humanitarian Work",
                icon = "☮️",
                content = "Advocated for nuclear disarmament, civil rights, and world peace. Supported the civil rights movement and spoke out against racism. Helped Jewish refugees escape Nazi Germany."
            )
        }
        
        item {
            InfoCard(
                title = "Honors",
                icon = "🏅",
                content = "Nobel Prize (1921), Copley Medal (1925), Max Planck Medal (1929). Element 99 (Einsteinium) named in his honor. Countless schools, awards, and institutions bear his name."
            )
        }
        
        item {
            InfoCard(
                title = "Philosophy",
                icon = "💭",
                content = "Believed in the power of imagination over knowledge. Advocated for curiosity, independent thinking, and intellectual freedom. Saw science and religion as complementary."
            )
        }
    }
}

@Composable
fun TimelineTab() {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val timeline = listOf(
            TimelineEvent("1879", "Born in Ulm, Germany", "👶"),
            TimelineEvent("1896", "Enrolled at Swiss Federal Polytechnic", "🎓"),
            TimelineEvent("1900", "Graduated with teaching diploma", "📜"),
            TimelineEvent("1902", "Patent clerk in Bern", "💼"),
            TimelineEvent("1905", "Annus Mirabilis - 4 groundbreaking papers", "✨"),
            TimelineEvent("1909", "Professor at University of Zurich", "👨‍🏫"),
            TimelineEvent("1915", "Completed General Relativity", "🌌"),
            TimelineEvent("1919", "Solar eclipse confirms relativity", "🌑"),
            TimelineEvent("1921", "Nobel Prize in Physics", "🏆"),
            TimelineEvent("1933", "Emigrated to United States", "🗽"),
            TimelineEvent("1939", "Letter to FDR about atomic bomb", "📨"),
            TimelineEvent("1940", "Became US citizen", "🇺🇸"),
            TimelineEvent("1952", "Offered presidency of Israel (declined)", "🇮🇱"),
            TimelineEvent("1955", "Died in Princeton, New Jersey", "⭐")
        )
        
        items(timeline) { event ->
            TimelineCard(event)
        }
    }
}

data class Work(
    val title: String,
    val year: String,
    val description: String,
    val icon: String,
    val id: String? = null
)

data class TimelineEvent(
    val year: String,
    val event: String,
    val icon: String
)

@Composable
fun InfoCard(title: String, icon: String, content: String) {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        glowColor = PremiumColors.CyberBlue
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(icon, fontSize = 32.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                content,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.9f),
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
fun HighlightCard(
    title: String,
    subtitle: String,
    description: String,
    gradient: List<Color>
) {
    PremiumGlassCard(
        modifier = Modifier.fillMaxWidth(),
        gradientColors = gradient
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                title,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Black,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                subtitle,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                description,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.85f),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
fun WorkCard(work: Work, onClick: (() -> Unit)? = null) {
    val modifier = if (onClick != null && work.id != null) {
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    } else {
        Modifier.fillMaxWidth()
    }
    
    GlassmorphicCard(
        modifier = modifier,
        glowColor = if (work.id != null) PremiumColors.ElectricPurple else PremiumColors.ElectricPurple.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(work.icon, fontSize = 32.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        work.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        work.year,
                        style = MaterialTheme.typography.labelMedium,
                        color = PremiumColors.CyberBlue
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        work.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
            
            if (work.id != null) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "View details",
                    tint = PremiumColors.ElectricPurple,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun TimelineCard(event: TimelineEvent) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(PremiumColors.ElectricPurple),
                contentAlignment = Alignment.Center
            ) {
                Text(event.icon, fontSize = 20.sp)
            }
            if (event != TimelineEvent("1955", "Died in Princeton, New Jersey", "⭐")) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(60.dp)
                        .background(PremiumColors.ElectricPurple.copy(alpha = 0.3f))
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        GlassmorphicCard(
            modifier = Modifier.weight(1f),
            glowColor = PremiumColors.CyberBlue
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    event.year,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = PremiumColors.CyberBlue
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    event.event,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }
        }
    }
}
