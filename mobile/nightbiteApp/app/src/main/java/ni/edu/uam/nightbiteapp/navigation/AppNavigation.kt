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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import ni.edu.uam.nightbiteapp.data.local.mock.GameResultsData
import ni.edu.uam.nightbiteapp.data.local.mock.NightLevelsData
import ni.edu.uam.nightbiteapp.data.local.mock.NightProgressData
import ni.edu.uam.nightbiteapp.data.local.session.SessionManager
import ni.edu.uam.nightbiteapp.data.local.session.UserSession
import ni.edu.uam.nightbiteapp.ui.components.dialogs.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.model.GameResultType
import ni.edu.uam.nightbiteapp.ui.screens.AccountScreen
import ni.edu.uam.nightbiteapp.ui.screens.AgeCheckScreen
import ni.edu.uam.nightbiteapp.ui.screens.GamePlaceholderScreen
import ni.edu.uam.nightbiteapp.ui.screens.GameResultScreen
import ni.edu.uam.nightbiteapp.ui.screens.GenderSelectionScreen
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
import ni.edu.uam.nightbiteapp.viewmodel.StartViewModel
import ni.edu.uam.nightbiteapp.viewmodel.StartViewModelFactory
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect

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

    var pendingLoginUsername by remember {
        mutableStateOf("")
    }

    var pendingLoginPassword by remember {
        mutableStateOf("")
    }

    var showLastStepsWelcomeMessage by remember {
        mutableStateOf(false)
    }

    var showHomeWelcomeToast by remember {
        mutableStateOf(false)
    }

    fun navigateBackToHome() {
        navController.navigate(Routes.HOME) {
            popUpTo(Routes.HOME) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }

    fun navigateToLoginKeepingPrefill() {
        val returnedToLogin = navController.popBackStack(
            route = Routes.LOGIN,
            inclusive = false
        )

        if (!returnedToLogin) {
            navController.navigate(Routes.LOGIN) {
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.START
    ) {
        composable(Routes.START) {
            val startViewModel: StartViewModel = viewModel(
                factory = StartViewModelFactory(sessionManager)
            )

            StartScreen(
                viewModel = startViewModel,
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.START) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToHome = { user ->
                    activeUserId = user.id

                    if (user.player == null) {
                        showLastStepsWelcomeMessage = true

                        navController.navigate(Routes.GENDER_SELECTION) {
                            popUpTo(Routes.START) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    } else {
                        showLastStepsWelcomeMessage = false

                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.START) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                initialUsername = pendingLoginUsername,
                initialPassword = pendingLoginPassword,
                onNavigateToRegister = {
                    navController.navigate(Routes.AGE_CHECK)
                },
                onNavigateToHome = { user ->
                    activeUserId = user.id

                    val isFreshRegisterLogin = pendingLoginPassword.isNotBlank()

                    pendingLoginUsername = ""
                    pendingLoginPassword = ""

                    if (user.player == null) {
                        showLastStepsWelcomeMessage = !isFreshRegisterLogin
                        showHomeWelcomeToast = true

                        navController.navigate(Routes.GENDER_SELECTION) {
                            popUpTo(Routes.LOGIN) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    } else {
                        showLastStepsWelcomeMessage = false
                        showHomeWelcomeToast = true

                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) {
                                inclusive = true
                            }
                            launchSingleTop = true
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
                },
                onRegisterSuccess = { username, password ->
                    pendingLoginUsername = username
                    pendingLoginPassword = password

                    navigateToLoginKeepingPrefill()
                },
                onEmailAlreadyRegistered = { email ->
                    pendingLoginUsername = email
                    pendingLoginPassword = ""

                    navigateToLoginKeepingPrefill()
                }
            )
        }

        composable(Routes.GENDER_SELECTION) {
            val playerCreationViewModel: PlayerCreationViewModel = viewModel(
                factory = PlayerCreationViewModelFactory(sessionManager)
            )

            GenderSelectionScreen(
                viewModel = playerCreationViewModel,
                showWelcomeBackMessage = showLastStepsWelcomeMessage,
                onExitApp = {
                    activity?.finish()
                },
                onPlayerCreated = {
                    showLastStepsWelcomeMessage = false

                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.GENDER_SELECTION) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.HOME) {

            LaunchedEffect(showHomeWelcomeToast) {
                if (showHomeWelcomeToast) {
                    Toast.makeText(
                        context,
                        "¡Bienvenido a NightBite!",
                        Toast.LENGTH_SHORT
                    ).show()

                    showHomeWelcomeToast = false
                }
            }

            HomeScreen(
                userId = activeUserId ?: userSession.userId,
                userSession = userSession,
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
                    // Pendiente: crear pantalla de logros.
                },
                onNavigateToSettings = {
                    navController.navigate(Routes.SETTINGS)
                },
                onLogout = {
                    activeUserId = null
                    pendingLoginUsername = ""
                    pendingLoginPassword = ""
                    showLastStepsWelcomeMessage = false

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
            val resultTypeName = backStackEntry.arguments?.getString("resultType")

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
                    onRetryLevel = {
                        navController.popBackStack()
                    },
                    onContinue = {
                        val currentUserId = activeUserId ?: userSession.userId

                        if (levelId == 0) {
                            tutorialStarsForResult(resultType)?.let { stars ->
                                NightProgressData.saveTutorialStars(
                                    userId = currentUserId,
                                    stars = stars
                                )
                            }
                        }

                        if (shouldUnlockNextLevel) {
                            NightProgressData.unlockNextLevel(
                                userId = currentUserId,
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
                    // Pendiente.
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
            val accountCredentialsViewModel: AccountCredentialsViewModel = viewModel(
                factory = AccountCredentialsViewModelFactory(sessionManager)
            )

            val accountUiState by accountCredentialsViewModel.uiState.collectAsState()

            var showLogoutConfirmation by remember {
                mutableStateOf(false)
            }

            var isLeavingSession by remember {
                mutableStateOf(false)
            }

            if (!isLeavingSession) {
                SettingsScreen(
                    userSession = userSession,
                    onBackToHome = {
                        navController.popBackStack()
                    },
                    onNavigateToAccount = {
                        navController.navigate(Routes.ACCOUNT)
                    },
                    onLogout = {
                        showLogoutConfirmation = true
                    },
                    onDeleteAccountClick = {
                        accountCredentialsViewModel.onDeleteAccountClick()
                    }
                )
            }

            if (showLogoutConfirmation) {
                NightMessageDialog(
                    title = "Cerrar sesión",
                    message = "¿Deseas cerrar tu sesión actual?",
                    confirmText = "Cerrar sesión",
                    dismissText = "Cancelar",
                    icon = Icons.Default.Warning,
                    iconColor = CheeseYellow,
                    onConfirm = {
                        showLogoutConfirmation = false
                        isLeavingSession = true
                        activeUserId = null
                        pendingLoginUsername = ""
                        pendingLoginPassword = ""
                        showLastStepsWelcomeMessage = false

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
                    onDismiss = {
                        showLogoutConfirmation = false
                    }
                )
            }

            if (accountUiState.showDeleteAccountDialog) {
                NightMessageDialog(
                    title = "Eliminar cuenta",
                    message = "Esta acción eliminará tu cuenta y tu ficha de repartidor. No podrás recuperar estos datos. ¿Deseas continuar?",
                    confirmText = "Eliminar",
                    dismissText = "Cancelar",
                    icon = Icons.Default.Warning,
                    iconColor = CheeseYellow,
                    onConfirm = {
                        isLeavingSession = true
                        accountCredentialsViewModel.deleteAccount(userSession.userId)
                    },
                    onDismiss = {
                        accountCredentialsViewModel.dismissDeleteAccountDialog()
                    }
                )
            }

            if (accountUiState.showAccountDeletedDialog) {
                NightMessageDialog(
                    title = "Cuenta eliminada",
                    message = "Tu cuenta fue eliminada correctamente. Volverás al inicio de sesión.",
                    confirmText = "Aceptar",
                    dismissText = null,
                    icon = Icons.Default.Warning,
                    iconColor = CheeseYellow,
                    onConfirm = {
                        accountCredentialsViewModel.finishAccountDeletedFlow {
                            activeUserId = null
                            pendingLoginUsername = ""
                            pendingLoginPassword = ""
                            showLastStepsWelcomeMessage = false

                            navController.navigate(Routes.LOGIN) {
                                popUpTo(Routes.HOME) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    },
                    onDismiss = null
                )
            }
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
                    pendingLoginUsername = ""
                    pendingLoginPassword = ""
                    showLastStepsWelcomeMessage = false

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

private fun tutorialStarsForResult(
    resultType: GameResultType
): Int? {
    return when (resultType) {
        GameResultType.TUTORIAL_THREE_STARS -> 3
        GameResultType.TUTORIAL_TWO_STARS -> 2
        GameResultType.TUTORIAL_ONE_STAR -> 1
        GameResultType.FIRED -> 0
        else -> null
    }
}