package ni.edu.uam.nightbiteapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ni.edu.uam.nightbiteapp.ui.screens.GamePlaceholderScreen
import ni.edu.uam.nightbiteapp.ui.screens.HomeScreen
import ni.edu.uam.nightbiteapp.ui.screens.LoginScreen
import ni.edu.uam.nightbiteapp.ui.screens.ProfileScreen
import ni.edu.uam.nightbiteapp.ui.screens.RegisterScreen
import ni.edu.uam.nightbiteapp.ui.screens.SettingsScreen
import ni.edu.uam.nightbiteapp.ui.screens.StartScreen
import androidx.compose.ui.platform.LocalContext
import android.app.Activity
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.navArgument
import ni.edu.uam.nightbiteapp.ui.screens.AgeCheckScreen

/**
 * Componente principal de navegación de la aplicación.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val activity = context as? Activity


    NavHost(
        navController = navController,
        startDestination = Routes.START
    ) {
        composable(Routes.START) {
            StartScreen(
                onPressStart = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.START) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Routes.AGE_CHECK)
                },
                onNavigateToHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                    }
                },
                onExitApp = {
                    activity?.finish()
                }
            )
        }

        composable(Routes.AGE_CHECK) {
            AgeCheckScreen(
                onAgeApproved = { age ->
                    navController.navigate(Routes.registerWithAge(age))
                },
                onBackToLogin = {
                    navController.popBackStack(Routes.LOGIN, false)
                }
            )
        }

        composable(
            route = Routes.REGISTER_WITH_AGE,
            arguments = listOf(
                navArgument("age") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val age = backStackEntry.arguments?.getInt("age") ?: 13

            RegisterScreen(
                age = age,
                onBackToLogin = {
                    navController.popBackStack(Routes.LOGIN, false)
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                onNavigateToGame = {
                    navController.navigate(Routes.GAME_PLACEHOLDER)
                },
                onNavigateToProfile = {
                    navController.navigate(Routes.PROFILE)
                },
                onNavigateToSettings = {
                    navController.navigate(Routes.SETTINGS)
                }
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen()
        }

        composable(Routes.SETTINGS) {
            SettingsScreen()
        }

        composable(Routes.GAME_PLACEHOLDER) {
            GamePlaceholderScreen()
        }
    }
}