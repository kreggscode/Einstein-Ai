package com.kreggscode.einsteinquotes.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kreggscode.einsteinquotes.ui.screens.*
import com.kreggscode.einsteinquotes.viewmodel.ChatViewModel
import com.kreggscode.einsteinquotes.viewmodel.QuoteViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Chat : Screen("chat")
    object Favorites : Screen("favorites")
    object Works : Screen("works")
    object Settings : Screen("settings")
    object QuoteDetail : Screen("quote_detail/{quoteId}") {
        fun createRoute(quoteId: Int) = "quote_detail/$quoteId"
    }
    object CategoryQuotes : Screen("category_quotes/{category}") {
        fun createRoute(category: String) = "category_quotes/$category"
    }
    object WorkQuotes : Screen("work_quotes/{work}") {
        fun createRoute(work: String) = "work_quotes/$work"
    }
    object About : Screen("about")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    quoteViewModel: QuoteViewModel,
    chatViewModel: ChatViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = quoteViewModel,
                onCategoryClick = { category ->
                    navController.navigate(Screen.CategoryQuotes.createRoute(category))
                },
                onQuoteClick = { quoteId ->
                    navController.navigate(Screen.QuoteDetail.createRoute(quoteId))
                },
                onAboutClick = {
                    navController.navigate(Screen.About.route)
                },
                onWorksClick = {
                    navController.navigate(Screen.Works.route) {
                        popUpTo(Screen.Home.route) {
                            saveState = true
                            inclusive = false
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        
        composable(Screen.Chat.route) {
            ChatScreen(
                viewModel = chatViewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Favorites.route) {
            FavoritesScreen(
                viewModel = quoteViewModel,
                onQuoteClick = { quoteId ->
                    navController.navigate(Screen.QuoteDetail.createRoute(quoteId))
                }
            )
        }
        
        composable(Screen.Works.route) {
            WorksScreen(
                viewModel = quoteViewModel,
                onWorkClick = { work ->
                    navController.navigate(Screen.WorkQuotes.createRoute(work))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                viewModel = quoteViewModel,
                onAboutClick = {
                    navController.navigate(Screen.About.route)
                }
            )
        }
        
        composable(
            route = Screen.QuoteDetail.route,
            arguments = listOf(navArgument("quoteId") { type = NavType.IntType })
        ) { backStackEntry ->
            val quoteId = backStackEntry.arguments?.getInt("quoteId") ?: 0
            QuoteDetailScreen(
                quoteId = quoteId,
                viewModel = quoteViewModel,
                onBackClick = {
                    navController.popBackStack()
                },
                onChatClick = {
                    navController.navigate(Screen.Chat.route)
                }
            )
        }
        
        composable(
            route = Screen.CategoryQuotes.route,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            CategoryQuotesScreen(
                category = category,
                viewModel = quoteViewModel,
                onQuoteClick = { quoteId ->
                    navController.navigate(Screen.QuoteDetail.createRoute(quoteId))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(
            route = Screen.WorkQuotes.route,
            arguments = listOf(navArgument("work") { type = NavType.StringType })
        ) { backStackEntry ->
            val work = backStackEntry.arguments?.getString("work") ?: ""
            WorkQuotesScreen(
                work = work,
                viewModel = quoteViewModel,
                onQuoteClick = { quoteId ->
                    navController.navigate(Screen.QuoteDetail.createRoute(quoteId))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.About.route) {
            AboutScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
