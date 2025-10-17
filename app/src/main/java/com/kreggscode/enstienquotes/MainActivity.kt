package com.kreggscode.enstienquotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kreggscode.enstienquotes.navigation.NavGraph
import com.kreggscode.enstienquotes.navigation.Screen
import com.kreggscode.enstienquotes.notifications.NotificationScheduler
import com.kreggscode.enstienquotes.ui.components.BottomNavItem
import com.kreggscode.enstienquotes.ui.components.PremiumFloatingBottomBar
import com.kreggscode.enstienquotes.ui.screens.AnimatedSplashScreen
import com.kreggscode.enstienquotes.ui.theme.AnimatedGradientBackground
import com.kreggscode.enstienquotes.ui.theme.EinsteinTheme
import com.kreggscode.enstienquotes.ui.theme.PremiumColors
import com.kreggscode.enstienquotes.viewmodel.ChatViewModel
import com.kreggscode.enstienquotes.viewmodel.QuoteViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    
    private val quoteViewModel: QuoteViewModel by viewModels()
    private val chatViewModel: ChatViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge display (modern API for Android 15+)
        enableEdgeToEdge()
        
        // Set status bar and navigation bar icons to WHITE (light icons on dark background)
        WindowCompat.getInsetsController(window, window.decorView)?.apply {
            isAppearanceLightStatusBars = false  // false = white icons
            isAppearanceLightNavigationBars = false  // false = white icons
        }
        
        // Observe notification settings
        lifecycleScope.launch {
            quoteViewModel.isDailyNotificationEnabled.collect { enabled ->
                if (enabled) {
                    NotificationScheduler.scheduleDailyNotification(this@MainActivity)
                } else {
                    NotificationScheduler.cancelDailyNotification(this@MainActivity)
                }
            }
        }
        
        setContent {
            val isDarkMode by quoteViewModel.isDarkMode.collectAsState()
            val view = LocalView.current
            
            SideEffect {
                val window = (view.context as ComponentActivity).window
                // Set icon colors for status bar and navigation bar
                WindowCompat.getInsetsController(window, view)?.apply {
                    isAppearanceLightStatusBars = false  // White icons for dark background
                    isAppearanceLightNavigationBars = false  // White icons for dark background
                }
            }
            
            EinsteinTheme(darkTheme = isDarkMode) {
                var showSplash by remember { mutableStateOf(true) }
                
                if (showSplash) {
                    AnimatedSplashScreen(
                        onSplashComplete = { showSplash = false }
                    )
                } else {
                    MainScreen(
                        quoteViewModel = quoteViewModel,
                        chatViewModel = chatViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    quoteViewModel: QuoteViewModel,
    chatViewModel: ChatViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    val bottomNavItems = listOf(
        BottomNavItem(Screen.Home.route, Icons.Default.Home, "Home"),
        BottomNavItem(Screen.Works.route, Icons.AutoMirrored.Filled.MenuBook, "Works"),
        BottomNavItem(Screen.Favorites.route, Icons.Default.Favorite, "Favorites"),
        BottomNavItem(Screen.Chat.route, Icons.AutoMirrored.Filled.Chat, "Chat"),
        BottomNavItem(Screen.Settings.route, Icons.Default.Settings, "Settings")
    )
    
    // Hide bottom bar on Chat screen - users can only go back using back button
    val showBottomBar = currentRoute in bottomNavItems.map { it.route } && currentRoute != Screen.Chat.route
    
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
        // Animated gradient background
        AnimatedGradientBackground(
            modifier = Modifier.fillMaxSize(),
            colors = PremiumColors.CosmicDust
        )
        
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Main content
            NavGraph(
                navController = navController,
                quoteViewModel = quoteViewModel,
                chatViewModel = chatViewModel
            )
            
            // Floating bottom bar overlaid on top
            if (showBottomBar) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.navigationBars)
                ) {
                    PremiumFloatingBottomBar(
                        items = bottomNavItems,
                        currentRoute = currentRoute ?: Screen.Home.route,
                        onItemClick = { route ->
                            if (currentRoute != route) {
                                if (route == Screen.Home.route) {
                                    // When clicking Home, clear everything and go to Home
                                    navController.navigate(route) {
                                        popUpTo(Screen.Home.route) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                } else {
                                    // For other tabs, use normal navigation
                                    navController.navigate(route) {
                                        popUpTo(Screen.Home.route) {
                                            saveState = true
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
