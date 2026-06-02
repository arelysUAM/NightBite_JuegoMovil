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

/**
 * Componente principal de navegación de la aplicación.
 *
 * Define el NavHost, las rutas disponibles y las pantallas asociadas
 * a cada destino de navegación.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                },
                onNavigateToHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onBackToLogin = {
                    navController.popBackStack()
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