package com.kreggscode.einsteinquotes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kreggscode.einsteinquotes.data.PollinationsApiService
import com.kreggscode.einsteinquotes.data.PollinationsMessage
import com.kreggscode.einsteinquotes.data.PollinationsRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

class ChatViewModel : ViewModel() {
    
    private val apiService = PollinationsApiService.create()
    
    private val systemPrompt = """You are Voltaire, the famous French Enlightenment writer, historian, and philosopher.
        |You lived from 1694 to 1778 and were known for your wit, advocacy of freedom of speech, freedom of religion, and separation of church and state.
        |Respond in character as Voltaire, using his philosophical insights, wit, and wisdom.
        |Keep responses concise, thoughtful, and in the spirit of the Enlightenment.
        |Use quotes from your works when appropriate, and maintain a conversational yet intellectual tone.
        |You are passionate about reason, tolerance, and human rights.""".trimMargin()
    
    private val _messages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                text = "Bonjour! I am Voltaire, philosopher of the Enlightenment. Ask me about reason, tolerance, freedom, or any of my works. What troubles your mind today?",
                isUser = false
            )
        )
    )
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()
    
    private val _isTyping = MutableStateFlow(false)
    val isTyping: StateFlow<Boolean> = _isTyping.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    fun sendMessage(text: String) {
        if (text.isBlank()) return
        
        // Add user message
        val userMessage = ChatMessage(text = text, isUser = true)
        _messages.value = _messages.value + userMessage
        
        // Show typing indicator
        _isTyping.value = true
        _error.value = null
        
        // Call Pollinations AI
        viewModelScope.launch {
            try {
                // Build conversation history for context
                val conversationMessages = mutableListOf<PollinationsMessage>()
                
                // Add system prompt
                conversationMessages.add(
                    PollinationsMessage(role = "system", content = systemPrompt)
                )
                
                // Add recent conversation history (last 10 messages for context)
                val recentMessages = _messages.value.takeLast(11) // 10 + current message
                recentMessages.forEach { msg ->
                    conversationMessages.add(
                        PollinationsMessage(
                            role = if (msg.isUser) "user" else "assistant",
                            content = msg.text
                        )
                    )
                }
                
                // Create request with temperature = 1.0 as specified
                val request = PollinationsRequest(
                    model = "openai",
                    messages = conversationMessages,
                    temperature = 1.0,
                    stream = false,
                    isPrivate = false
                )
                
                // Make API call
                val response = apiService.chat(request)
                
                // Extract response text
                val aiResponseText = response.choices.firstOrNull()?.message?.content
                    ?: "I apologize, but I seem to have lost my train of thought. Could you rephrase your question?"
                
                // Add AI response
                _messages.value = _messages.value + ChatMessage(
                    text = aiResponseText,
                    isUser = false
                )
                
            } catch (e: Exception) {
                // Handle error
                _error.value = "Connection error: ${e.message}"
                _messages.value = _messages.value + ChatMessage(
                    text = "Pardonnez-moi, I seem to be having difficulty connecting. Perhaps the network is troubled. Please try again.",
                    isUser = false
                )
            } finally {
                _isTyping.value = false
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
    
    fun clearChat() {
        _messages.value = listOf(
            ChatMessage(
                text = "Bonjour! I am Voltaire, philosopher of the Enlightenment. Ask me about reason, tolerance, freedom, or any of my works. What troubles your mind today?",
                isUser = false
            )
        )
        _error.value = null
    }
}
