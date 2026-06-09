package ni.edu.uam.nightbiteapp.navigation

import android.app.Activity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ni.edu.uam.nightbiteapp.data.local.mock.GameResultsData
import ni.edu.uam.nightbiteapp.data.local.mock.NightLevelsData
import ni.edu.uam.nightbiteapp.data.local.session.SessionManager
import ni.edu.uam.nightbiteapp.data.local.session.UserSession
import ni.edu.uam.nightbiteapp.ui.components.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.model.GameResultType
import ni.edu.uam.nightbiteapp.ui.screens.AccountScreen
import ni.edu.uam.nightbiteapp.ui.screens.AgeCheckScreen
import ni.edu.uam.nightbiteapp.ui.screens.GamePlaceholderScreen
import ni.edu.uam.nightbiteapp.ui.screens.GameResultScreen
import ni.edu.uam.nightbiteapp.ui.screens.HomeScreen
import ni.edu.uam.nightbiteapp.ui.screens.LevelIntroScreen
import ni.edu.uam.nightbiteapp.ui.screens.LoginScreen
import ni.edu.uam.nightbiteapp.ui.screens.PlayerCreationScreen
import ni.edu.uam.nightbiteapp.ui.screens.PlayerDetailScreen
import ni.edu.uam.nightbiteapp.ui.screens.RegisterScreen
import ni.edu.uam.nightbiteapp.ui.screens.SettingsScreen
import ni.edu.uam.nightbiteapp.ui.screens.StartScreen
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.viewmodel.AccountCredentialsViewModel
import ni.edu.uam.nightbiteapp.viewmodel.AccountCredentialsViewModelFactory
import ni.edu.uam.nightbiteapp.viewmodel.PlayerCreationViewModel
import ni.edu.uam.nightbiteapp.viewmodel.PlayerCreationViewModelFactory
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import ni.edu.uam.nightbiteapp.data.local.mock.NightProgressData

/**
 * Componente principal de navegación de la aplicación.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val activity = context as? Activity
    val coroutineScope = rememberCoroutineScope()

    val sessionManager = remember {
        SessionManager(context.applicationContext)
    }

    val userSession by sessionManager.userSessionFlow.collectAsState(
        initial = UserSession()
    )

    var activeUserId by remember {
        mutableStateOf<Long?>(null)
    }

    /**
     * Regresa al HomeScreen y elimina las pantallas abiertas
     * por encima del menú principal.
     */
    fun navigateBackToHome() {
        navController.navigate(Routes.HOME) {
            popUpTo(Routes.HOME) {
                inclusive = false
            }

            launchSingleTop = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.START
    ) {
        composable(Routes.START) {
            StartScreen(
                onLoadingFinished  = {
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
                },
                onBackToAgeCheck = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                userId = activeUserId ?: userSession.userId,
                onNavigateToLevelIntro = { levelId ->
                    navController.navigate(Routes.levelIntro(levelId))
                },
                onNavigateToPlayerDetail = {
                    navController.navigate(Routes.PLAYER_DETAIL)
                },
                onNavigateToPlayerCreation = {
                    navController.navigate(Routes.PLAYER_CREATION)
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

        composable(
            route = Routes.LEVEL_INTRO,
            arguments = listOf(
                navArgument("levelId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val levelId = backStackEntry.arguments?.getInt("levelId")
            val selectedLevel = levelId?.let {
                NightLevelsData.getLevelById(it)
            }

            LevelIntroScreen(
                level = selectedLevel,
                onStartLevel = {
                    if (levelId != null) {
                        navController.navigate(
                            Routes.gamePlaceholder(levelId)
                        )
                    }
                },
                onBackToHome = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Routes.GAME_PLACEHOLDER,
            arguments = listOf(
                navArgument("levelId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val levelId = backStackEntry.arguments?.getInt("levelId") ?: 0

            GamePlaceholderScreen(
                levelId = levelId,
                onNavigateToResult = { resultType ->
                    navController.navigate(
                        Routes.gameResult(
                            levelId = levelId,
                            resultType = resultType.name
                        )
                    )
                },
                onRestartLevel = {
                    /*
                     * Actualmente el simulador no guarda progreso interno,
                     * por lo que permanece en la misma pantalla.
                     *
                     * Cuando se implemente LibGDX, aquí se reiniciará
                     * el estado real del nivel.
                     */
                    navController.navigate(
                        Routes.gamePlaceholder(levelId)
                    ) {
                        launchSingleTop = true
                    }
                },
                onBackToHome = {
                    navigateBackToHome()
                }
            )
        }

        composable(
            route = Routes.GAME_RESULT,
            arguments = listOf(
                navArgument("levelId") {
                    type = NavType.IntType
                },
                navArgument("resultType") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val levelId = backStackEntry.arguments?.getInt("levelId") ?: 0
            val resultTypeName =
                backStackEntry.arguments?.getString("resultType")

            val resultType = resultTypeName?.let { name ->
                runCatching {
                    GameResultType.valueOf(name)
                }.getOrNull()
            }

            if (resultType == null) {
                NightMessageDialog(
                    title = "Resultado no encontrado",
                    message = "No se pudo cargar el resultado de esta jornada.",
                    confirmText = "Volver al mapa",
                    dismissText = null,
                    icon = Icons.Default.Warning,
                    iconColor = CheeseYellow,
                    onConfirm = {
                        navigateBackToHome()
                    },
                    onDismiss = {
                        navigateBackToHome()
                    }
                )
            } else {
                val resultContent = GameResultsData.getResultContent(
                    levelId = levelId,
                    resultType = resultType
                )

                val shouldUnlockNextLevel =
                    resultType == GameResultType.TUTORIAL_THREE_STARS ||
                            resultType == GameResultType.VICTORY ||
                            resultType == GameResultType.FINAL_VICTORY

                GameResultScreen(
                    resultType = resultType,
                    content = resultContent,

                    /*
                     * Como GameResultScreen está sobre GamePlaceholderScreen,
                     * regresar en la pila vuelve al mismo nivel.
                     */
                    onRetryLevel = {
                        navController.popBackStack()
                    },

                    onContinue = {
                        if (shouldUnlockNextLevel) {
                            NightProgressData.unlockNextLevel(
                                userId = activeUserId ?: userSession.userId,
                                completedLevelId = levelId
                            )
                        }

                        if (resultType == GameResultType.FINAL_VICTORY) {
                            navigateBackToHome()
                        } else if (shouldUnlockNextLevel) {
                            val nextLevelId = levelId + 1

                            navController.navigate(
                                Routes.levelIntro(nextLevelId)
                            ) {
                                popUpTo(Routes.HOME) {
                                    inclusive = false
                                }
                            }
                        } else {
                            navigateBackToHome()
                        }
                    },

                    onBackToHome = {
                        navigateBackToHome()
                    }
                )
            }
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
                    // Pendiente: edición de Player.
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
                    navigateBackToHome()
                }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                userSession = userSession,

                onNavigateToAccount = {
                    navController.navigate(Routes.ACCOUNT)
                },

                onLogout = {
                    activeUserId = null

                    coroutineScope.launch {
                        sessionManager.clearSession()

                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.HOME) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    }
                },

                onDeleteAccount = {
                    // Pendiente: conectar endpoint para eliminar cuenta.
                },

                onBackToHome = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.ACCOUNT) {
            val accountCredentialsViewModel: AccountCredentialsViewModel = viewModel(
                factory = AccountCredentialsViewModelFactory(sessionManager)
            )

            AccountScreen(
                userSession = userSession,
                viewModel = accountCredentialsViewModel,
                onBackToSettings = {
                    navController.popBackStack()
                },
                onNavigateToLogin = {
                    activeUserId = null

                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
            )
        }
    }
}