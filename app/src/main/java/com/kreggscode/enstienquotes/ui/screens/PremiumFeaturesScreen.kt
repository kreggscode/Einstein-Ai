package com.kreggscode.enstienquotes.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.*
import com.kreggscode.enstienquotes.ui.components.*
import com.kreggscode.enstienquotes.ui.theme.*
import kotlinx.coroutines.delay

data class PremiumFeature(
    val icon: String,
    val title: String,
    val description: String,
    val price: String,
    val gradient: List<Color>,
    val isPopular: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumFeaturesScreen(
    onBackClick: () -> Unit,
    onFeatureUnlock: (PremiumFeature) -> Unit
) {
    val features = listOf(
        PremiumFeature(
            icon = "👑",
            title = "Einstein Pro",
            description = "Unlock all features forever\n• Unlimited AI insights\n• Export quotes as images\n• Voice narration\n• Ad-free experience\n• Priority support",
            price = "$9.99",
            gradient = PremiumColors.GalaxyGradient,
            isPopular = true
        ),
        PremiumFeature(
            icon = "🧠",
            title = "AI Genius Mode",
            description = "Chat with Einstein AI\n• Personalized wisdom\n• Deep philosophical discussions\n• Context-aware responses\n• Daily AI insights",
            price = "$4.99/mo",
            gradient = listOf(PremiumColors.ElectricPurple, PremiumColors.NeonPink)
        ),
        PremiumFeature(
            icon = "🎨",
            title = "Quote Designer",
            description = "Create stunning quote images\n• 50+ templates\n• Custom backgrounds\n• Share on social media\n• Watermark removal",
            price = "$2.99",
            gradient = PremiumColors.SunsetGradient
        ),
        PremiumFeature(
            icon = "🎙️",
            title = "Voice & Meditation",
            description = "Einstein's voice experience\n• Quote narration\n• Guided meditation\n• Sleep stories\n• Background music",
            price = "$3.99",
            gradient = PremiumColors.NorthernLights
        ),
        PremiumFeature(
            icon = "📚",
            title = "Scholar Collection",
            description = "Access 10,000+ quotes\n• Rare manuscripts\n• Letters & essays\n• Scientific papers\n• Historical context",
            price = "$5.99",
            gradient = listOf(PremiumColors.QuantumTeal, PremiumColors.CyberBlue)
        ),
        PremiumFeature(
            icon = "⚡",
            title = "Widget Plus",
            description = "Advanced widgets\n• Interactive quotes\n• Multiple styles\n• Auto-refresh\n• Dark mode support",
            price = "$1.99",
            gradient = listOf(PremiumColors.PlasmaOrange, PremiumColors.NeonPink)
        )
    )
    
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
            particleColor = Color(0xFFFFD700).copy(alpha = 0.2f)
        )
        
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "✨ Premium Features",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "SALE",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .background(
                                        Color(0xFFFFD700),
                                        RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
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
                // Limited time offer banner
                item {
                    LimitedOfferBanner()
                }
                
                // Premium features
                itemsIndexed(features) { index, feature ->
                    PremiumFeatureCard(
                        feature = feature,
                        index = index,
                        onClick = { onFeatureUnlock(feature) }
                    )
                }
                
                // Testimonials
                item {
                    TestimonialsSection()
                }
                
                // Money-back guarantee
                item {
                    MoneyBackGuarantee()
                }
                
                // Bottom spacing
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}

@Composable
fun LimitedOfferBanner() {
    var timeLeft by remember { mutableStateOf("23:59:47") }
    
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            // Update countdown timer
            timeLeft = "23:59:${(0..59).random().toString().padStart(2, '0')}"
        }
    }
    
    PremiumGlassCard(
        modifier = Modifier.fillMaxWidth(),
        gradientColors = listOf(Color(0xFFFFD700), Color(0xFFFFA500))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "🔥 LIMITED TIME OFFER 🔥",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                "Save 50% on All Premium Features",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Timer,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "Offer ends in: $timeLeft",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun PremiumFeatureCard(
    feature: PremiumFeature,
    index: Int,
    onClick: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(index) {
        delay(index * 100L)
        visible = true
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally { if (index % 2 == 0) -it else it } + fadeIn()
    ) {
        Box {
            if (feature.isPopular) {
                // Popular badge
                Text(
                    "MOST POPULAR",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(y = (-8).dp)
                        .background(
                            Color(0xFFFFD700),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                        .zIndex(1f)
                )
            }
            
            GlassmorphicCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = onClick,
                glowColor = feature.gradient.first(),
                animateGlow = true
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = feature.gradient.map { it.copy(alpha = 0.1f) }
                            )
                        )
                        .padding(20.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // Icon
                    Text(
                        feature.icon,
                        fontSize = 48.sp,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    
                    // Content
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = feature.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = feature.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f),
                            lineHeight = 20.sp
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Original price (strikethrough)
                            val originalPrice = when(feature.price) {
                                "$9.99" -> "$19.99"
                                "$4.99/mo" -> "$9.99/mo"
                                "$2.99" -> "$5.99"
                                "$3.99" -> "$7.99"
                                "$5.99" -> "$11.99"
                                else -> "$3.99"
                            }
                            
                            Text(
                                text = originalPrice,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White.copy(alpha = 0.5f),
                                textDecoration = TextDecoration.LineThrough
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            // Sale price
                            Text(
                                text = feature.price,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Black,
                                color = feature.gradient.first()
                            )
                            
                            Spacer(modifier = Modifier.width(12.dp))
                            
                            // Unlock button
                            Button(
                                onClick = onClick,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = feature.gradient.first()
                                ),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text(
                                    "Unlock",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TestimonialsSection() {
    val testimonials = listOf(
        "\"This app changed my perspective on life!\" - Sarah M.",
        "\"Worth every penny. The AI insights are mind-blowing.\" - John D.",
        "\"I start every day with Einstein's wisdom now.\" - Maria L."
    )
    
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        glowColor = PremiumColors.CosmicIndigo
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                "⭐⭐⭐⭐⭐",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            testimonials.forEach { testimonial ->
                Text(
                    text = testimonial,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun MoneyBackGuarantee() {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        glowColor = Color(0xFF4CAF50)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.VerifiedUser,
                contentDescription = null,
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(48.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    "30-Day Money Back Guarantee",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
                Text(
                    "Not satisfied? Get a full refund, no questions asked.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}
