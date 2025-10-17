package com.kreggscode.enstienquotes.ui.screens

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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.kreggscode.enstienquotes.model.Quote
import com.kreggscode.enstienquotes.ui.components.*
import com.kreggscode.enstienquotes.ui.theme.*
import com.kreggscode.enstienquotes.viewmodel.ChatMessage
import com.kreggscode.enstienquotes.viewmodel.ChatViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumChatScreen(
    viewModel: ChatViewModel,
    onBackClick: () -> Unit
) {
    val messages by viewModel.messages.collectAsState()
    val isTyping by viewModel.isTyping.collectAsState()
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    
    var inputText by remember { mutableStateOf("") }
    var showSuggestions by remember { mutableStateOf(true) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    
    DisposableEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
            }
        }
        onDispose { tts?.shutdown() }
    }
    
    // Auto-scroll to bottom when new message arrives
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            delay(100)
            listState.animateScrollToItem(messages.size - 1)
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
            particleColor = PremiumColors.CyberBlue.copy(alpha = 0.1f)
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .imePadding()
        ) {
            // Premium Header
            PremiumChatHeader(
                onBackClick = onBackClick,
                onDeleteClick = { showDeleteDialog = true },
                hasMessages = messages.isNotEmpty()
            )
            
            // Chat Messages
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Welcome message if no messages
                if (messages.isEmpty() && showSuggestions) {
                    item {
                        WelcomeCard()
                    }
                    
                    item {
                        SuggestionsCard(
                            onSuggestionClick = { suggestion ->
                                inputText = suggestion
                                showSuggestions = false
                                viewModel.sendMessage(suggestion)
                            }
                        )
                    }
                }
                
                // Chat messages
                items(messages) { message ->
                    AnimatedChatBubble(
                        message = message,
                        onSpeak = { text ->
                            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                        }
                    )
                }
                
                // Loading indicator
                if (isTyping) {
                    item {
                        EinsteinThinkingIndicator()
                    }
                }
            }
            
            // Input Field
            PremiumChatInput(
                text = inputText,
                onTextChange = { inputText = it },
                onSendClick = {
                    if (inputText.isNotBlank()) {
                        viewModel.sendMessage(inputText)
                        inputText = ""
                        showSuggestions = false
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                },
                isTyping = isTyping
            )
        }
        
        // Delete Confirmation Dialog
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = {
                    Text(
                        "Delete All Messages?",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        "This will permanently delete all chat messages. This action cannot be undone.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.clearChat()
                            showDeleteDialog = false
                            showSuggestions = true
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.Red
                        )
                    ) {
                        Text("Delete All", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDeleteDialog = false }
                    ) {
                        Text("Cancel")
                    }
                },
                containerColor = PremiumColors.MidnightBlue,
                titleContentColor = Color.White,
                textContentColor = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun PremiumChatHeader(
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    hasMessages: Boolean
) {
    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        glowColor = PremiumColors.CyberBlue,
        cornerRadius = 20.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.1f))
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Einstein Avatar with animation
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = PremiumColors.GalaxyGradient
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("🧠", fontSize = 24.sp)
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Chat with Einstein",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    "Genius of Relativity",
                    style = MaterialTheme.typography.labelSmall,
                    color = PremiumColors.CyberBlue
                )
            }
            
            // Delete button (only show when there are messages)
            if (hasMessages) {
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Red.copy(alpha = 0.2f))
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete All Messages",
                        tint = Color.Red.copy(alpha = 0.9f)
                    )
                }
                
                Spacer(modifier = Modifier.width(8.dp))
            }
            
            // Status indicator
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50))
            )
        }
    }
}

@Composable
fun WelcomeCard() {
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
            Text(
                "🌟",
                fontSize = 48.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                "Welcome to Einstein's Mind",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                "Ask me about physics, philosophy, life, or the mysteries of the universe!",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SuggestionsCard(onSuggestionClick: (String) -> Unit) {
    val suggestions = listOf(
        "Explain the theory of relativity in simple terms",
        "What is your philosophy on life?",
        "How did you discover E=mc²?",
        "What advice would you give to young scientists?",
        "Tell me about quantum mechanics",
        "What are your thoughts on imagination vs knowledge?"
    )
    
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            "💡 Try asking:",
            style = MaterialTheme.typography.titleSmall,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        
        suggestions.forEach { suggestion ->
            SuggestionChip(
                text = suggestion,
                onClick = { onSuggestionClick(suggestion) }
            )
        }
    }
}

@Composable
fun SuggestionChip(
    text: String,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        label = "scale"
    )
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        PremiumColors.CyberBlue.copy(alpha = 0.2f),
                        PremiumColors.ElectricPurple.copy(alpha = 0.2f)
                    )
                )
            )
            .clickable {
                isPressed = true
                onClick()
            }
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = PremiumColors.CyberBlue,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
    }
}

@Composable
fun AnimatedChatBubble(
    message: ChatMessage,
    onSpeak: (String) -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(message) {
        visible = true
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = if (message.isUser) {
            slideInHorizontally { it } + fadeIn()
        } else {
            slideInHorizontally { -it } + fadeIn()
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
        ) {
            if (!message.isUser) {
                // Einstein avatar
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = PremiumColors.GalaxyGradient
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🧠", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
            
            Column(
                modifier = Modifier.widthIn(max = 280.dp)
            ) {
                GlassmorphicCard(
                    glowColor = if (message.isUser) PremiumColors.NeonPink else PremiumColors.CyberBlue,
                    cornerRadius = 16.dp,
                    glassOpacity = if (message.isUser) 0.2f else 0.15f
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = message.text,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                        
                        if (!message.isUser) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                IconButton(
                                    onClick = { onSpeak(message.text) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        Icons.Default.VolumeUp,
                                        contentDescription = "Speak",
                                        tint = Color.White.copy(alpha = 0.7f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                
                // Time stamp
                Text(
                    text = java.text.SimpleDateFormat("HH:mm", Locale.getDefault()).format(java.util.Date(message.timestamp)),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun EinsteinThinkingIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "thinking")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = PremiumColors.GalaxyGradient
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "🧠",
                fontSize = 16.sp,
                modifier = Modifier.graphicsLayer {
                    rotationZ = rotation
                }
            )
        }
        
        Spacer(modifier = Modifier.width(8.dp))
        
        GlassmorphicCard(
            glowColor = PremiumColors.CyberBlue,
            cornerRadius = 16.dp
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = PremiumColors.CyberBlue,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Einstein is thinking...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = PremiumColors.CyberBlue
                )
            }
        }
    }
}

@Composable
fun PremiumChatInput(
    text: String,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit,
    isTyping: Boolean
) {
    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .navigationBarsPadding(),
        glowColor = PremiumColors.ElectricPurple,
        cornerRadius = 24.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            TextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        "Ask Einstein...",
                        color = Color.White.copy(alpha = 0.5f)
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = PremiumColors.CyberBlue,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                maxLines = 3
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            FloatingActionButton(
                onClick = onSendClick,
                modifier = Modifier.size(48.dp),
                containerColor = PremiumColors.ElectricPurple,
                contentColor = Color.White
            ) {
                if (isTyping) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Send"
                    )
                }
            }
        }
    }
}
