package com.kreggscode.einsteinquotes.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kreggscode.einsteinquotes.model.Quote
import com.kreggscode.einsteinquotes.ui.components.MorphismCard
import com.kreggscode.einsteinquotes.viewmodel.QuoteViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteDetailScreen(
    quoteId: Int,
    viewModel: QuoteViewModel,
    onBackClick: () -> Unit,
    onChatClick: () -> Unit
) {
    val allQuotes by viewModel.allQuotes.collectAsState()
    val quote = remember(allQuotes, quoteId) {
        allQuotes.find { it.id == quoteId }
    }
    
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var showDetails by remember { mutableStateOf(true) }
    
    // Initialize TTS
    DisposableEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
            }
        }
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quote Details") },
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
        if (quote == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Quote not found")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Main quote card with gradient
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFF667eea),
                                            Color(0xFF764ba2)
                                        )
                                    )
                                )
                                .padding(24.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Tap to expand indicator
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "💬 Quote",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = Color.White.copy(alpha = 0.9f),
                                        fontWeight = FontWeight.Bold
                                    )
                                    IconButton(
                                        onClick = { showDetails = !showDetails }
                                    ) {
                                        Icon(
                                            imageVector = if (showDetails) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                            contentDescription = "Expand",
                                            tint = Color.White
                                        )
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                Text(
                                    text = quote.Quote,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                
                                if (!quote.French.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Divider(color = Color.White.copy(alpha = 0.3f))
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "🇫🇷 French",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = Color.White.copy(alpha = 0.9f),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = quote.French.replace("[translate:", "").replace("]", ""),
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontStyle = FontStyle.Italic,
                                        color = Color.White.copy(alpha = 0.95f)
                                    )
                                }
                            }
                        }
                    }
                }
                
                // Copy and TTS buttons
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Copy English
                        Button(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("quote", quote.Quote)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "English quote copied!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50)
                            )
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null)
                            Spacer(Modifier.width(4.dp))
                            Text("Copy EN")
                        }
                        
                        // Speak English
                        Button(
                            onClick = {
                                tts?.stop()
                                tts?.language = Locale.US
                                tts?.speak(quote.Quote, TextToSpeech.QUEUE_FLUSH, null, null)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2196F3)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = null
                            )
                            Spacer(Modifier.width(4.dp))
                            Text("Speak")
                        }
                    }
                }
                
                // Copy and Speak French
                if (!quote.French.isNullOrBlank()) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Copy French
                            Button(
                                onClick = {
                                    val frenchText = quote.French?.replace("[translate:", "")?.replace("]", "") ?: ""
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("quote", frenchText)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "French quote copied!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF9C27B0)
                                )
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null)
                                Spacer(Modifier.width(4.dp))
                                Text("Copy FR")
                            }
                            
                            // Speak French
                            Button(
                                onClick = {
                                    val frenchText = quote.French?.replace("[translate:", "")?.replace("]", "") ?: ""
                                    tts?.language = Locale.FRENCH
                                    tts?.speak(frenchText, TextToSpeech.QUEUE_FLUSH, null, null)
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFF9800)
                                )
                            ) {
                                Icon(Icons.Default.VolumeUp, contentDescription = null)
                                Spacer(Modifier.width(4.dp))
                                Text("Speak FR")
                            }
                        }
                    }
                }
                
                // Action buttons row
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        MorphismCard(
                            onClick = { viewModel.toggleFavorite(quote) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (quote.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    tint = if (quote.isFavorite) Color(0xFFE91E63) else MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (quote.isFavorite) "Saved" else "Save",
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                        }
                        
                        MorphismCard(
                            onClick = {
                                val shareIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "${quote.Quote}\n\n- Voltaire, ${quote.Work} (${quote.Year})")
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share Quote"))
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Share",
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                        }
                        
                        MorphismCard(
                            onClick = onChatClick,
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Chat,
                                    contentDescription = "Chat",
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Chat",
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                        }
                    }
                }
                
                // Details (expandable)
                item {
                    AnimatedVisibility(visible = showDetails) {
                        MorphismCard(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(
                                    text = "📖 Quote Details",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Divider()
                                DetailRow(label = "📚 Work", value = quote.Work)
                                DetailRow(label = "📅 Year", value = quote.Year.toString())
                                DetailRow(label = "🏷️ Category", value = quote.Category)
                                DetailRow(label = "🔖 Tags", value = quote.Tags)
                                if (quote.Context.isNotBlank()) {
                                    DetailRow(label = "💡 Context", value = quote.Context)
                                }
                                DetailRow(label = "⭐ Popularity", value = quote.Popularity)
                                DetailRow(label = "📝 Reference", value = quote.Reference)
                                if (!quote.Bio.isNullOrBlank()) {
                                    DetailRow(label = "ℹ️ Bio Note", value = quote.Bio)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
