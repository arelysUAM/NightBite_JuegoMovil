package ni.edu.uam.nightbiteapp.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import ni.edu.uam.nightbiteapp.data.local.session.SessionManager
import ni.edu.uam.nightbiteapp.data.local.session.UserSession
import ni.edu.uam.nightbiteapp.ui.screens.AgeCheckScreen
import ni.edu.uam.nightbiteapp.ui.screens.GamePlaceholderScreen
import ni.edu.uam.nightbiteapp.ui.screens.HomeScreen
import ni.edu.uam.nightbiteapp.ui.screens.LoginScreen
import ni.edu.uam.nightbiteapp.ui.screens.PlayerCreationScreen
import ni.edu.uam.nightbiteapp.ui.screens.PlayerDetailScreen
import ni.edu.uam.nightbiteapp.ui.screens.ProfileScreen
import ni.edu.uam.nightbiteapp.ui.screens.RegisterScreen
import ni.edu.uam.nightbiteapp.ui.screens.SettingsScreen
import ni.edu.uam.nightbiteapp.ui.screens.StartScreen
import ni.edu.uam.nightbiteapp.viewmodel.PlayerCreationViewModel
import ni.edu.uam.nightbiteapp.viewmodel.PlayerCreationViewModelFactory

/**
 * Componente principal de navegación de la aplicación.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val activity = context as? Activity

    val sessionManager = remember {
        SessionManager(context.applicationContext)
    }

    val coroutineScope = rememberCoroutineScope()

    val userSession by sessionManager.userSessionFlow.collectAsState(
        initial = UserSession()
    )

    var activeUserId by remember {
        mutableStateOf<Long?>(null)
    }

    NavHost(
        navController = navController,
        startDestination = Routes.START
    ) {
        composable(Routes.START) {
            StartScreen(
                onPressStart = {
                    val currentUserId = userSession.userId

                    if (userSession.isLoggedIn && currentUserId != null) {
                        activeUserId = currentUserId

                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.START) {
                                inclusive = true
                            }
                        }
                    } else {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.START) {
                                inclusive = true
                            }
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
                onNavigateToHome = { user ->
                    activeUserId = user.id

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
                onNavigateToPlayerDetail = {
                    navController.navigate(Routes.PLAYER_DETAIL)
                },
                onNavigateToAchievements = {
                    // Pendiente: crear pantalla de libro de logros.
                },
                onNavigateToSettings = {
                    navController.navigate(Routes.SETTINGS)
                },
                onExitApp = {
                    activity?.finish()
                }
            )
        }

        composable(Routes.PLAYER_DETAIL) {
            PlayerDetailScreen(
                userId = activeUserId ?: userSession.userId,
                onBackToHome = {
                    navController.popBackStack()
                },
                onNavigateToPlayerCreation = {
                    navController.navigate(Routes.PLAYER_CREATION)
                },
                onEditPlayer = {
                    // Pendiente: conectar después con pantalla o modo de edición de Player.
                }
            )
        }

        composable(Routes.PLAYER_CREATION) {
            val playerCreationViewModel: PlayerCreationViewModel = viewModel(
                factory = PlayerCreationViewModelFactory(sessionManager)
            )

            PlayerCreationScreen(
                viewModel = playerCreationViewModel,
                onPlayerCreated = {
                    navController.navigate(Routes.PLAYER_DETAIL) {
                        popUpTo(Routes.PLAYER_CREATION) {
                            inclusive = true
                        }
                    }
                },
                onBackToHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.PLAYER_CREATION) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                onNavigateToProfile = {
                    navController.navigate(Routes.PROFILE)
                },
                onBackToHome = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                userId = activeUserId ?: userSession.userId,
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }

                    coroutineScope.launch {
                        sessionManager.clearSession()
                        activeUserId = null
                    }
                },
                onDeleteAccount = {
                    // Pendiente: conectar con endpoint DELETE de cuenta cuando exista en backend.
                }
            )
        }

        composable(Routes.GAME_PLACEHOLDER) {
            GamePlaceholderScreen()
        }
    }
}