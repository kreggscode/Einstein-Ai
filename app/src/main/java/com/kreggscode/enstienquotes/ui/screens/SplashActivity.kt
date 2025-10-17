package com.kreggscode.enstienquotes.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import com.kreggscode.enstienquotes.MainActivity
import com.kreggscode.enstienquotes.ui.theme.PremiumColors
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge display
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.Transparent.toArgb()
        window.navigationBarColor = Color.Transparent.toArgb()
        
        setContent {
            var shouldNavigate by remember { mutableStateOf(false) }
            
            LaunchedEffect(Unit) {
                delay(3500) // Show splash for 3.5 seconds
                shouldNavigate = true
            }
            
            if (shouldNavigate) {
                LaunchedEffect(Unit) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
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
                    ),
                contentAlignment = Alignment.Center
            ) {
                AnimatedSplashScreen(
                    onSplashComplete = {
                        // This is handled by the LaunchedEffect above
                    }
                )
            }
        }
    }
}
