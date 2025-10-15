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
    object WorkDetail : Screen("work_detail/{workId}") {
        fun createRoute(workId: String) = "work_detail/$workId"
    }
    object EssayDetail : Screen("essay_detail/{essayId}") {
        fun createRoute(essayId: String) = "essay_detail/$essayId"
    }
    object LetterDetail : Screen("letter_detail/{letterId}") {
        fun createRoute(letterId: String) = "letter_detail/$letterId"
    }
    object PaperDetail : Screen("paper_detail/{paperId}") {
        fun createRoute(paperId: String) = "paper_detail/$paperId"
    }
    object About : Screen("about")
    object QuoteOfDay : Screen("quote_of_day")
    object Affirmations : Screen("affirmations?openFavorites={openFavorites}") {
        fun createRoute(openFavorites: Boolean = false) = "affirmations?openFavorites=$openFavorites"
    }
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
            PremiumHomeScreen(
                viewModel = quoteViewModel,
                onCategoryClick = { category ->
                    navController.navigate(Screen.CategoryQuotes.createRoute(category))
                },
                onQuoteClick = { quote ->
                    navController.navigate(Screen.QuoteDetail.createRoute(quote.id))
                },
                onAboutClick = {
                    navController.navigate(Screen.About.route)
                },
                onWorkClick = {
                    navController.navigate(Screen.Works.route) {
                        popUpTo(Screen.Home.route) {
                            saveState = true
                            inclusive = false
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onQuoteOfDayClick = {
                    navController.navigate(Screen.QuoteOfDay.route)
                },
                onAffirmationsClick = {
                    navController.navigate(Screen.Affirmations.createRoute(false))
                },
                onFavoritesClick = {
                    navController.navigate(Screen.Favorites.route)
                },
                onChatClick = {
                    navController.navigate(Screen.Chat.route)
                }
            )
        }
        
        composable(Screen.Chat.route) {
            PremiumChatScreen(
                viewModel = chatViewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Favorites.route) {
            PremiumFavoritesScreen(
                viewModel = quoteViewModel,
                onQuoteClick = { quoteId ->
                    navController.navigate(Screen.QuoteDetail.createRoute(quoteId))
                }
            )
        }
        
        composable(Screen.Works.route) {
            EinsteinWorksScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onWorkClick = { workItem ->
                    when (workItem.category) {
                        WorkCategory.MAJOR_WORKS -> {
                            navController.navigate(Screen.WorkDetail.createRoute(workItem.id))
                        }
                        WorkCategory.ESSAYS -> {
                            navController.navigate(Screen.EssayDetail.createRoute(workItem.id))
                        }
                        WorkCategory.LETTERS -> {
                            navController.navigate(Screen.LetterDetail.createRoute(workItem.id))
                        }
                        WorkCategory.PAPERS -> {
                            navController.navigate(Screen.PaperDetail.createRoute(workItem.id))
                        }
                    }
                },
                onChatClick = { prompt ->
                    // TODO: Pass prompt to chat
                    navController.navigate(Screen.Chat.route)
                }
            )
        }
        
        composable(Screen.Settings.route) {
            PremiumSettingsScreen(
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
            EnhancedAboutScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onWorkClick = { workId ->
                    navController.navigate(Screen.WorkDetail.createRoute(workId))
                }
            )
        }
        
        composable(Screen.QuoteOfDay.route) {
            QuoteOfDayScreen(
                viewModel = quoteViewModel,
                onBackClick = {
                    navController.popBackStack()
                },
                onChatClick = { quote ->
                    // Pass quote to chat if needed
                    navController.navigate(Screen.Chat.route)
                }
            )
        }
        
        composable(
            route = Screen.Affirmations.route,
            arguments = listOf(
                navArgument("openFavorites") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->
            val openFavorites = backStackEntry.arguments?.getBoolean("openFavorites") ?: false
            AffirmationsScreen(
                viewModel = quoteViewModel,
                openFavoritesTab = openFavorites,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        // Einstein Works Detail Screens
        composable(
            route = Screen.WorkDetail.route,
            arguments = listOf(navArgument("workId") { type = NavType.StringType })
        ) { backStackEntry ->
            val workId = backStackEntry.arguments?.getString("workId") ?: ""
            WorkDetailScreen(
                workId = workId,
                onBackClick = {
                    navController.popBackStack()
                },
                onChatClick = { prompt ->
                    navController.navigate(Screen.Chat.route)
                }
            )
        }
        
        composable(
            route = Screen.EssayDetail.route,
            arguments = listOf(navArgument("essayId") { type = NavType.StringType })
        ) { backStackEntry ->
            val essayId = backStackEntry.arguments?.getString("essayId") ?: ""
            EssayDetailScreen(
                essayId = essayId,
                onBackClick = {
                    navController.popBackStack()
                },
                onChatClick = { prompt ->
                    navController.navigate(Screen.Chat.route)
                }
            )
        }
        
        composable(
            route = Screen.LetterDetail.route,
            arguments = listOf(navArgument("letterId") { type = NavType.StringType })
        ) { backStackEntry ->
            val letterId = backStackEntry.arguments?.getString("letterId") ?: ""
            LetterDetailScreen(
                letterId = letterId,
                onBackClick = {
                    navController.popBackStack()
                },
                onChatClick = { prompt ->
                    navController.navigate(Screen.Chat.route)
                }
            )
        }
        
        composable(
            route = Screen.PaperDetail.route,
            arguments = listOf(navArgument("paperId") { type = NavType.StringType })
        ) { backStackEntry ->
            val paperId = backStackEntry.arguments?.getString("paperId") ?: ""
            PaperDetailScreen(
                paperId = paperId,
                onBackClick = {
                    navController.popBackStack()
                },
                onChatClick = { prompt ->
                    navController.navigate(Screen.Chat.route)
                }
            )
        }
    }
}
