package com.kreggscode.einsteinquotes.ui.screens

import android.content.Intent
import android.speech.tts.TextToSpeech
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
import androidx.compose.ui.unit.*
import com.kreggscode.einsteinquotes.model.Quote
import com.kreggscode.einsteinquotes.ui.components.*
import com.kreggscode.einsteinquotes.ui.theme.*
import com.kreggscode.einsteinquotes.viewmodel.QuoteViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

data class Affirmation(
    val id: Int,
    val text: String,
    val category: String,
    val icon: String,
    val gradientColors: List<Color>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AffirmationsScreen(
    viewModel: QuoteViewModel,
    openFavoritesTab: Boolean = false,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedTab by remember { mutableStateOf(if (openFavoritesTab) 1 else 0) } // 0 = All, 1 = Favorites
    
    // Debug logging
    LaunchedEffect(openFavoritesTab) {
        android.util.Log.d("AffirmationsScreen", "openFavoritesTab = $openFavoritesTab, selectedTab = $selectedTab")
    }
    var dailyAffirmation by remember { mutableStateOf<Affirmation?>(null) }
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var isTtsReady by remember { mutableStateOf(false) }
    var currentlySpeaking by remember { mutableStateOf<Int?>(null) }
    var favoriteAffirmations by remember { mutableStateOf(setOf<Int>()) }
    
    // Initialize TTS
    DisposableEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
                tts?.setPitch(1.0f)
                tts?.setSpeechRate(0.85f)
                isTtsReady = true
            }
        }
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }
    
    val affirmations = remember { generateEinsteinAffirmations() }
    val categories = listOf("All", "Science", "Wisdom", "Life", "Innovation", "Peace")
    
    val filteredAffirmations = remember(selectedCategory, selectedTab, favoriteAffirmations) {
        val baseList = if (selectedTab == 1) {
            affirmations.filter { it.id in favoriteAffirmations }
        } else {
            affirmations
        }
        
        if (selectedCategory == "All") baseList
        else baseList.filter { it.category == selectedCategory }
    }
    
    // Get daily affirmation
    LaunchedEffect(Unit) {
        val dayOfYear = java.time.LocalDate.now().dayOfYear
        dailyAffirmation = affirmations[dayOfYear % affirmations.size]
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
        // Animated particles
        ParticleBackground(
            modifier = Modifier.fillMaxSize(),
            particleColor = PremiumColors.NebulaMagenta.copy(alpha = 0.2f)
        )
        
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            if (selectedTab == 1) "❤️ Favorite Affirmations" else "✨ Daily Affirmations",
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Daily Affirmation Hero Card
                dailyAffirmation?.let { affirmation ->
                    item {
                        DailyAffirmationCard(
                            affirmation = affirmation,
                            isSpeaking = currentlySpeaking == affirmation.id,
                            isFavorite = affirmation.id in favoriteAffirmations,
                            onSpeakClick = {
                                if (isTtsReady) {
                                    if (currentlySpeaking == affirmation.id) {
                                        tts?.stop()
                                        currentlySpeaking = null
                                    } else {
                                        tts?.speak(affirmation.text, TextToSpeech.QUEUE_FLUSH, null, affirmation.id.toString())
                                        currentlySpeaking = affirmation.id
                                    }
                                }
                            },
                            onShareClick = {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, "\"${affirmation.text}\"\n\n- Einstein Affirmation (${affirmation.category})")
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share Affirmation"))
                            },
                            onFavoriteClick = {
                                favoriteAffirmations = if (affirmation.id in favoriteAffirmations) {
                                    favoriteAffirmations - affirmation.id
                                } else {
                                    favoriteAffirmations + affirmation.id
                                }
                            }
                        )
                    }
                }
                
                // Breathing Exercise Card
                item {
                    BreathingExerciseCard()
                }
                
                // All/Favorites Tabs
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FilterChip(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            label = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("All Affirmations")
                                }
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PremiumColors.ElectricPurple,
                                containerColor = PremiumColors.ElectricPurple.copy(alpha = 0.2f),
                                selectedLabelColor = Color.White,
                                labelColor = Color.White.copy(alpha = 0.7f)
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        
                        FilterChip(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            label = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Favorite,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = if (selectedTab == 1) PremiumColors.NeonPink else Color.White.copy(alpha = 0.7f)
                                    )
                                    Text("Favorites (${favoriteAffirmations.size})")
                                }
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PremiumColors.NeonPink,
                                containerColor = PremiumColors.NeonPink.copy(alpha = 0.2f),
                                selectedLabelColor = Color.White,
                                labelColor = Color.White.copy(alpha = 0.7f)
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                
                // Category Filter
                item {
                    Text(
                        "Choose Your Focus",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(categories) { category ->
                            FilterChip(
                                selected = selectedCategory == category,
                                onClick = { selectedCategory = category },
                                label = {
                                    Text(
                                        category,
                                        color = if (selectedCategory == category) 
                                            Color.White else PremiumColors.CyberBlue
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PremiumColors.CyberBlue,
                                    containerColor = PremiumColors.CyberBlue.copy(alpha = 0.2f)
                                )
                            )
                        }
                    }
                }
                
                // Affirmations Grid
                item {
                    Text(
                        "🌟 Einstein-Inspired Affirmations",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                
                itemsIndexed(filteredAffirmations) { index, affirmation ->
                    AffirmationCard(
                        affirmation = affirmation,
                        index = index,
                        isSpeaking = currentlySpeaking == affirmation.id,
                        isFavorite = affirmation.id in favoriteAffirmations,
                        onClick = {
                            if (isTtsReady) {
                                if (currentlySpeaking == affirmation.id) {
                                    tts?.stop()
                                    currentlySpeaking = null
                                } else {
                                    tts?.speak(affirmation.text, TextToSpeech.QUEUE_FLUSH, null, affirmation.id.toString())
                                    currentlySpeaking = affirmation.id
                                }
                            }
                        },
                        onShareClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, "\"${affirmation.text}\"\n\n- Einstein Affirmation (${affirmation.category})")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Affirmation"))
                        },
                        onFavoriteClick = {
                            favoriteAffirmations = if (affirmation.id in favoriteAffirmations) {
                                favoriteAffirmations - affirmation.id
                            } else {
                                favoriteAffirmations + affirmation.id
                            }
                        }
                    )
                }
                
                // Bottom spacing
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
        
        // Floating Action Button for Random Affirmation
        FloatingActionButton(
            onClick = {
                scope.launch {
                    val randomAffirmation = filteredAffirmations.randomOrNull()
                    randomAffirmation?.let {
                        if (isTtsReady) {
                            tts?.speak(it.text, TextToSpeech.QUEUE_FLUSH, null, it.id.toString())
                            currentlySpeaking = it.id
                        }
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .padding(bottom = 100.dp),
            containerColor = PremiumColors.ElectricPurple
        ) {
            Icon(
                Icons.Default.AutoAwesome,
                contentDescription = "Random",
                tint = Color.White
            )
        }
    }
}

@Composable
fun DailyAffirmationCard(
    affirmation: Affirmation,
    isSpeaking: Boolean = false,
    onSpeakClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    isFavorite: Boolean = false
) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        visible = true
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically() + fadeIn()
    ) {
        PremiumGlassCard(
            modifier = Modifier.fillMaxWidth(),
            gradientColors = affirmation.gradientColors
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icon and title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Text(
                        affirmation.icon,
                        fontSize = 32.sp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            "Today's Affirmation",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            affirmation.category,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
                
                // Affirmation text
                Text(
                    text = affirmation.text,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 32.sp
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Action buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconButton(
                        onClick = onShareClick,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.White
                        )
                    }
                    
                    IconButton(
                        onClick = onSpeakClick,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isSpeaking) PremiumColors.NeonPink.copy(alpha = 0.4f) else Color.White.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            if (isSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp,
                            contentDescription = if (isSpeaking) "Stop" else "Speak",
                            tint = Color.White
                        )
                    }
                    
                    IconButton(
                        onClick = onFavoriteClick,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isFavorite) PremiumColors.NeonPink.copy(alpha = 0.4f) else Color.White.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = if (isFavorite) PremiumColors.NeonPink else Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BreathingExerciseCard() {
    var isBreathing by remember { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    
    val breathScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathScale"
    )
    
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = { isBreathing = !isBreathing },
        glowColor = PremiumColors.QuantumTeal
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "🧘 Mindful Moment",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = PremiumColors.QuantumTeal
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Breathing circle
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(if (isBreathing) breathScale else 1f)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                PremiumColors.QuantumTeal.copy(alpha = 0.3f),
                                PremiumColors.QuantumTeal.copy(alpha = 0.1f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (isBreathing) "Breathe" else "Start",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                "Take a moment to center yourself with Einstein's wisdom",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AffirmationCard(
    affirmation: Affirmation,
    index: Int,
    isSpeaking: Boolean = false,
    isFavorite: Boolean = false,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit = {},
    onShareClick: () -> Unit = {}
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
            glowColor = affirmation.gradientColors.first(),
            animateGlow = true
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icon
                    Text(
                        affirmation.icon,
                        fontSize = 40.sp,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    
                    // Text content
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = affirmation.text,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            lineHeight = 24.sp
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = affirmation.category,
                            style = MaterialTheme.typography.labelSmall,
                            color = affirmation.gradientColors.first(),
                            modifier = Modifier
                                .background(
                                    affirmation.gradientColors.first().copy(alpha = 0.2f),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Action buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Speak button
                    IconButton(
                        onClick = onClick,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isSpeaking) PremiumColors.NeonPink.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.1f))
                    ) {
                        Icon(
                            if (isSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp,
                            contentDescription = if (isSpeaking) "Stop" else "Speak",
                            tint = Color.White
                        )
                    }
                    
                    // Share button
                    IconButton(
                        onClick = onShareClick,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                    ) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.White
                        )
                    }
                    
                    // Favorite button
                    IconButton(
                        onClick = onFavoriteClick,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isFavorite) PremiumColors.NeonPink.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.1f))
                    ) {
                        Icon(
                            if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = if (isFavorite) PremiumColors.NeonPink else Color.White
                        )
                    }
                }
            }
        }
    }
}

fun generateEinsteinAffirmations(): List<Affirmation> {
    return listOf(
        Affirmation(
            1,
            "I embrace imagination as my most powerful tool for innovation.",
            "Innovation",
            "💡",
            PremiumColors.GalaxyGradient
        ),
        Affirmation(
            2,
            "Like Einstein, I question everything and seek deeper understanding.",
            "Wisdom",
            "🤔",
            PremiumColors.AuroraGradient
        ),
        Affirmation(
            3,
            "I trust that the universe is governed by elegant laws waiting to be discovered.",
            "Science",
            "🌌",
            PremiumColors.NorthernLights
        ),
        Affirmation(
            4,
            "My curiosity drives me to explore the mysteries of existence.",
            "Life",
            "🔮",
            PremiumColors.SunsetGradient
        ),
        Affirmation(
            5,
            "I choose peace and understanding in all my interactions.",
            "Peace",
            "🕊️",
            listOf(PremiumColors.QuantumTeal, PremiumColors.CyberBlue)
        ),
        Affirmation(
            6,
            "Complex problems become simple when I approach them with clarity.",
            "Science",
            "⚛️",
            listOf(PremiumColors.ElectricPurple, PremiumColors.NeonPink)
        ),
        Affirmation(
            7,
            "I am connected to the infinite wisdom of the cosmos.",
            "Wisdom",
            "✨",
            listOf(PremiumColors.CosmicIndigo, PremiumColors.NebulaMagenta)
        ),
        Affirmation(
            8,
            "My mind is open to revolutionary ideas that challenge conventions.",
            "Innovation",
            "🚀",
            listOf(PremiumColors.PlasmaOrange, PremiumColors.NeonPink)
        ),
        Affirmation(
            9,
            "I find joy in the elegant simplicity underlying nature's complexity.",
            "Life",
            "🌿",
            PremiumColors.NorthernLights
        ),
        Affirmation(
            10,
            "Every failure is a stepping stone to groundbreaking discovery.",
            "Innovation",
            "🎯",
            PremiumColors.GalaxyGradient
        ),
        Affirmation(
            11,
            "I approach challenges with the playful curiosity of a child.",
            "Life",
            "🎨",
            PremiumColors.SunsetGradient
        ),
        Affirmation(
            12,
            "My intuition guides me toward profound truths.",
            "Wisdom",
            "🧭",
            listOf(PremiumColors.CyberBlue, PremiumColors.ElectricPurple)
        ),
        Affirmation(
            13,
            "I contribute to humanity through compassion and understanding.",
            "Peace",
            "❤️",
            listOf(PremiumColors.NeonPink, PremiumColors.PlasmaOrange)
        ),
        Affirmation(
            14,
            "The universe reveals its secrets to my persistent inquiry.",
            "Science",
            "🔬",
            PremiumColors.AuroraGradient
        ),
        Affirmation(
            15,
            "I transform obstacles into opportunities for creative solutions.",
            "Innovation",
            "⚡",
            listOf(PremiumColors.ElectricPurple, PremiumColors.CyberBlue)
        ),
        Affirmation(16, "I embrace the mystery and wonder of the unknown.", "Wisdom", "🌠", PremiumColors.GalaxyGradient),
        Affirmation(17, "My thoughts shape reality through focused intention.", "Innovation", "🧠", PremiumColors.AuroraGradient),
        Affirmation(18, "I find beauty in mathematical elegance and natural harmony.", "Science", "📐", PremiumColors.NorthernLights),
        Affirmation(19, "Every moment is an opportunity for discovery and growth.", "Life", "🌱", PremiumColors.SunsetGradient),
        Affirmation(20, "I radiate peace and understanding to all beings.", "Peace", "☮️", listOf(PremiumColors.QuantumTeal, PremiumColors.CyberBlue)),
        Affirmation(21, "I trust in the interconnectedness of all things.", "Wisdom", "🕸️", listOf(PremiumColors.ElectricPurple, PremiumColors.NeonPink)),
        Affirmation(22, "My creativity flows from the infinite source within.", "Innovation", "🎭", listOf(PremiumColors.CosmicIndigo, PremiumColors.NebulaMagenta)),
        Affirmation(23, "I observe the world with childlike wonder and awe.", "Life", "👁️", listOf(PremiumColors.PlasmaOrange, PremiumColors.NeonPink)),
        Affirmation(24, "The laws of nature reveal themselves through patient study.", "Science", "📚", PremiumColors.NorthernLights),
        Affirmation(25, "I am a channel for revolutionary ideas and insights.", "Innovation", "💫", PremiumColors.GalaxyGradient),
        Affirmation(26, "My mind expands to embrace cosmic consciousness.", "Wisdom", "🌌", PremiumColors.SunsetGradient),
        Affirmation(27, "I find solutions in simplicity and elegance.", "Science", "✨", listOf(PremiumColors.CyberBlue, PremiumColors.ElectricPurple)),
        Affirmation(28, "Every challenge strengthens my resolve and wisdom.", "Life", "💪", listOf(PremiumColors.NeonPink, PremiumColors.PlasmaOrange)),
        Affirmation(29, "I contribute to world peace through my thoughts and actions.", "Peace", "🌍", PremiumColors.AuroraGradient),
        Affirmation(30, "The universe speaks to me through patterns and synchronicities.", "Wisdom", "🔮", listOf(PremiumColors.ElectricPurple, PremiumColors.CyberBlue)),
        Affirmation(31, "I am fearless in pursuing truth and understanding.", "Innovation", "🦁", PremiumColors.GalaxyGradient),
        Affirmation(32, "My intuition and logic work in perfect harmony.", "Science", "⚖️", PremiumColors.AuroraGradient),
        Affirmation(33, "I celebrate the joy of learning and discovery.", "Life", "🎉", PremiumColors.NorthernLights),
        Affirmation(34, "I choose love over fear in every situation.", "Peace", "💝", PremiumColors.SunsetGradient),
        Affirmation(35, "The cosmos reveals its secrets to my open mind.", "Wisdom", "🌟", listOf(PremiumColors.QuantumTeal, PremiumColors.CyberBlue)),
        Affirmation(36, "I breakthrough limitations with creative thinking.", "Innovation", "🚪", listOf(PremiumColors.ElectricPurple, PremiumColors.NeonPink)),
        Affirmation(37, "Energy and matter dance in perfect unity.", "Science", "💃", listOf(PremiumColors.CosmicIndigo, PremiumColors.NebulaMagenta)),
        Affirmation(38, "I live each day with passion and purpose.", "Life", "🔥", listOf(PremiumColors.PlasmaOrange, PremiumColors.NeonPink)),
        Affirmation(39, "My presence brings harmony to every situation.", "Peace", "🎵", PremiumColors.NorthernLights),
        Affirmation(40, "I see connections where others see separation.", "Wisdom", "🔗", PremiumColors.GalaxyGradient),
        Affirmation(41, "Innovation flows through me effortlessly.", "Innovation", "🌊", PremiumColors.SunsetGradient),
        Affirmation(42, "I understand the elegant simplicity of natural laws.", "Science", "🍃", listOf(PremiumColors.CyberBlue, PremiumColors.ElectricPurple)),
        Affirmation(43, "Every experience enriches my understanding of life.", "Life", "🎁", listOf(PremiumColors.NeonPink, PremiumColors.PlasmaOrange)),
        Affirmation(44, "I am an instrument of peace and compassion.", "Peace", "🎻", PremiumColors.AuroraGradient),
        Affirmation(45, "The infinite intelligence of the universe guides me.", "Wisdom", "🧘", listOf(PremiumColors.ElectricPurple, PremiumColors.CyberBlue)),
        Affirmation(46, "I pioneer new frontiers of thought and understanding.", "Innovation", "🗺️", PremiumColors.GalaxyGradient),
        Affirmation(47, "Space and time bend to reveal deeper truths.", "Science", "⏰", PremiumColors.AuroraGradient),
        Affirmation(48, "I embrace uncertainty as the birthplace of possibility.", "Life", "🌈", PremiumColors.NorthernLights),
        Affirmation(49, "My heart radiates unconditional love and acceptance.", "Peace", "💗", PremiumColors.SunsetGradient),
        Affirmation(50, "I access infinite wisdom through meditation and reflection.", "Wisdom", "🧘‍♂️", listOf(PremiumColors.QuantumTeal, PremiumColors.CyberBlue)),
        Affirmation(51, "Revolutionary ideas flow to me naturally.", "Innovation", "💡", listOf(PremiumColors.ElectricPurple, PremiumColors.NeonPink)),
        Affirmation(52, "I perceive the quantum nature of reality.", "Science", "⚛️", listOf(PremiumColors.CosmicIndigo, PremiumColors.NebulaMagenta)),
        Affirmation(53, "Life is my greatest teacher and adventure.", "Life", "🎢", listOf(PremiumColors.PlasmaOrange, PremiumColors.NeonPink)),
        Affirmation(54, "I create ripples of peace in the world.", "Peace", "🌊", PremiumColors.NorthernLights),
        Affirmation(55, "Ancient wisdom and modern science unite in my understanding.", "Wisdom", "📜", PremiumColors.GalaxyGradient),
        Affirmation(56, "I think beyond the boundaries of conventional thought.", "Innovation", "🎯", PremiumColors.SunsetGradient),
        Affirmation(57, "The universe is my laboratory for discovery.", "Science", "🔬", listOf(PremiumColors.CyberBlue, PremiumColors.ElectricPurple)),
        Affirmation(58, "I find magic in the ordinary moments of life.", "Life", "✨", listOf(PremiumColors.NeonPink, PremiumColors.PlasmaOrange)),
        Affirmation(59, "I am a beacon of hope and understanding.", "Peace", "🕯️", PremiumColors.AuroraGradient),
        Affirmation(60, "Cosmic consciousness flows through my awareness.", "Wisdom", "🌌", listOf(PremiumColors.ElectricPurple, PremiumColors.CyberBlue)),
        Affirmation(61, "I manifest breakthrough solutions effortlessly.", "Innovation", "🎪", PremiumColors.GalaxyGradient),
        Affirmation(62, "Relativity teaches me that perspective shapes reality.", "Science", "👓", PremiumColors.AuroraGradient),
        Affirmation(63, "I dance with the rhythm of the universe.", "Life", "💃", PremiumColors.NorthernLights),
        Affirmation(64, "My words and actions promote global harmony.", "Peace", "🌏", PremiumColors.SunsetGradient),
        Affirmation(65, "I channel the wisdom of great thinkers and visionaries.", "Wisdom", "📖", listOf(PremiumColors.QuantumTeal, PremiumColors.CyberBlue)),
        Affirmation(66, "Creative genius awakens within me daily.", "Innovation", "🎨", listOf(PremiumColors.ElectricPurple, PremiumColors.NeonPink)),
        Affirmation(67, "I comprehend the fundamental forces of nature.", "Science", "⚡", listOf(PremiumColors.CosmicIndigo, PremiumColors.NebulaMagenta)),
        Affirmation(68, "Every breath connects me to the cosmic dance.", "Life", "🌬️", listOf(PremiumColors.PlasmaOrange, PremiumColors.NeonPink)),
        Affirmation(69, "I embody peace in thought, word, and deed.", "Peace", "🕊️", PremiumColors.NorthernLights),
        Affirmation(70, "The mysteries of existence unfold before me.", "Wisdom", "🗝️", PremiumColors.GalaxyGradient),
        Affirmation(71, "I revolutionize my field through innovative thinking.", "Innovation", "🏆", PremiumColors.SunsetGradient),
        Affirmation(72, "Light reveals the secrets of the universe to me.", "Science", "💡", listOf(PremiumColors.CyberBlue, PremiumColors.ElectricPurple)),
        Affirmation(73, "I savor the richness of human experience.", "Life", "🍷", listOf(PremiumColors.NeonPink, PremiumColors.PlasmaOrange)),
        Affirmation(74, "I bridge differences with compassion and understanding.", "Peace", "🌉", PremiumColors.AuroraGradient),
        Affirmation(75, "Infinite intelligence expresses through my consciousness.", "Wisdom", "♾️", listOf(PremiumColors.ElectricPurple, PremiumColors.CyberBlue)),
        Affirmation(76, "I pioneer paradigm shifts in human understanding.", "Innovation", "🚀", PremiumColors.GalaxyGradient),
        Affirmation(77, "The fabric of spacetime reveals its nature to me.", "Science", "🕸️", PremiumColors.AuroraGradient),
        Affirmation(78, "I embrace the full spectrum of life's experiences.", "Life", "🌈", PremiumColors.NorthernLights),
        Affirmation(79, "My energy uplifts and inspires others.", "Peace", "🎈", PremiumColors.SunsetGradient),
        Affirmation(80, "I access the akashic records of universal knowledge.", "Wisdom", "📚", listOf(PremiumColors.QuantumTeal, PremiumColors.CyberBlue)),
        Affirmation(81, "Genius-level insights come to me naturally.", "Innovation", "🧠", listOf(PremiumColors.ElectricPurple, PremiumColors.NeonPink)),
        Affirmation(82, "I perceive the wave-particle duality of existence.", "Science", "🌊", listOf(PremiumColors.CosmicIndigo, PremiumColors.NebulaMagenta)),
        Affirmation(83, "Life's challenges sculpt me into my highest self.", "Life", "🗿", listOf(PremiumColors.PlasmaOrange, PremiumColors.NeonPink)),
        Affirmation(84, "I am a catalyst for positive transformation.", "Peace", "🦋", PremiumColors.NorthernLights),
        Affirmation(85, "The universe conspires to reveal truth to me.", "Wisdom", "🎭", PremiumColors.GalaxyGradient),
        Affirmation(86, "I create elegant solutions to complex problems.", "Innovation", "🎼", PremiumColors.SunsetGradient),
        Affirmation(87, "Quantum mechanics illuminates the nature of reality.", "Science", "💎", listOf(PremiumColors.CyberBlue, PremiumColors.ElectricPurple)),
        Affirmation(88, "I live authentically and courageously.", "Life", "🦁", listOf(PremiumColors.NeonPink, PremiumColors.PlasmaOrange)),
        Affirmation(89, "I dissolve conflict with wisdom and grace.", "Peace", "🌺", PremiumColors.AuroraGradient),
        Affirmation(90, "Cosmic consciousness awakens within me.", "Wisdom", "🌅", listOf(PremiumColors.ElectricPurple, PremiumColors.CyberBlue)),
        Affirmation(91, "I birth new realities through imaginative power.", "Innovation", "🌟", PremiumColors.GalaxyGradient),
        Affirmation(92, "The constants of nature whisper their secrets to me.", "Science", "🔢", PremiumColors.AuroraGradient),
        Affirmation(93, "I flow with the river of life's unfolding.", "Life", "🏞️", PremiumColors.NorthernLights),
        Affirmation(94, "My presence brings calm to turbulent situations.", "Peace", "🌊", PremiumColors.SunsetGradient),
        Affirmation(95, "I embody the synthesis of science and spirituality.", "Wisdom", "☯️", listOf(PremiumColors.QuantumTeal, PremiumColors.CyberBlue)),
        Affirmation(96, "Revolutionary breakthroughs are my natural state.", "Innovation", "💥", listOf(PremiumColors.ElectricPurple, PremiumColors.NeonPink)),
        Affirmation(97, "I understand the symphony of cosmic forces.", "Science", "🎼", listOf(PremiumColors.CosmicIndigo, PremiumColors.NebulaMagenta)),
        Affirmation(98, "Every moment is precious and full of potential.", "Life", "⏳", listOf(PremiumColors.PlasmaOrange, PremiumColors.NeonPink)),
        Affirmation(99, "I radiate love and light into the world.", "Peace", "☀️", PremiumColors.NorthernLights),
        Affirmation(100, "The infinite mind of the universe thinks through me.", "Wisdom", "🌌", PremiumColors.GalaxyGradient),
        Affirmation(101, "I transcend limitations through visionary thinking.", "Innovation", "🦅", PremiumColors.SunsetGradient),
        Affirmation(102, "Energy equals mass times the speed of light squared - I embody this truth.", "Science", "⚡", listOf(PremiumColors.CyberBlue, PremiumColors.ElectricPurple)),
        Affirmation(103, "I celebrate the miracle of consciousness and existence.", "Life", "🎊", listOf(PremiumColors.NeonPink, PremiumColors.PlasmaOrange)),
        Affirmation(104, "I am a peacemaker in all my relationships.", "Peace", "🤝", PremiumColors.AuroraGradient),
        Affirmation(105, "Universal wisdom flows through my intuition.", "Wisdom", "🌊", listOf(PremiumColors.ElectricPurple, PremiumColors.CyberBlue)),
        Affirmation(106, "I innovate boldly and transform paradigms.", "Innovation", "🎯", PremiumColors.GalaxyGradient),
        Affirmation(107, "The curvature of spacetime bends to my understanding.", "Science", "🌀", PremiumColors.AuroraGradient),
        Affirmation(108, "I embrace life's paradoxes with grace and humor.", "Life", "😊", PremiumColors.NorthernLights),
        Affirmation(109, "My compassion heals and unites.", "Peace", "💚", PremiumColors.SunsetGradient),
        Affirmation(110, "I am one with the cosmic intelligence.", "Wisdom", "🕉️", listOf(PremiumColors.QuantumTeal, PremiumColors.CyberBlue)),
        Affirmation(111, "Creative solutions emerge from my subconscious mind.", "Innovation", "🌙", listOf(PremiumColors.ElectricPurple, PremiumColors.NeonPink)),
        Affirmation(112, "I perceive the elegant mathematics underlying reality.", "Science", "∞", listOf(PremiumColors.CosmicIndigo, PremiumColors.NebulaMagenta)),
        Affirmation(113, "Life is my canvas for self-expression and growth.", "Life", "🖼️", listOf(PremiumColors.PlasmaOrange, PremiumColors.NeonPink)),
        Affirmation(114, "I plant seeds of peace wherever I go.", "Peace", "🌱", PremiumColors.NorthernLights),
        Affirmation(115, "The cosmos and I are one unified consciousness.", "Wisdom", "🌌", PremiumColors.GalaxyGradient)
    )
}
